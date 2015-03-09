package com.tsz662.rest.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.tsz662.rest.interceptors.GzipResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 以下のテスト用
 * <ul>
 * <li>{@code @MatrixParam}
 * <li>{@code javax.ws.rs.core.StreamingOutput}
 * <li>画像返却
 * <li>application/x-www-form-urleencoded
 * <li>multipart/form-data
 * </ul>
 * @since 1.1
 * @version 1.1
 * @author tsz662
 *
 */
@Path("files")
@Api(value = "files", description = "ファイル関連のリソース")
public class MyFiles {

	/**
	 * アップロードされたファイルの情報等を返す。
	 * @param uploader うp主名
	 * @param file アップロードされたファイル(InputStream)
	 * @param contentDispositionHeader Content-Dispositionヘッダ
	 * @return うp主名とContent-Dispositionヘッダから取れる情報
	 * @HTTP 200 OK
	 */
	@POST
	@Path("multipart")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(
		value = "アップロードされたファイルの情報等を返す。",
		position = 1
	)
	public Response uploadFile(
			@ApiParam(value = "うp主名", required = true)
			@FormDataParam("uploader") String uploader,
			@ApiParam(value = "アップロードされたファイル(InputStream)", required = true)
			@FormDataParam("file") InputStream file,
			@ApiParam(value = "Content-Dispositionヘッダ")
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
		
		final String LR = "<br>";
		StringBuffer sb = new StringBuffer();
		
		Map<String, String> map = contentDispositionHeader.getParameters();
		sb.append("--params--" + LR);
		for(Map.Entry<String, String> entry : map.entrySet()) {
			sb.append("key:" + entry.getKey() + LR);
			sb.append("value:" + entry.getValue() + LR);
		}
		sb.append("-------" + LR);

		sb.append("うp主:" + uploader + LR);
		sb.append("size:" + contentDispositionHeader.getSize() + LR);
		sb.append("filename: " + contentDispositionHeader.getFileName() + LR);
		sb.append("type:" + contentDispositionHeader.getType() + LR);

		return Response.ok().entity(sb.toString()).build();
	}

	/**
	 * StreamingOutputのテスト
	 * @return test.txtの内容
	 * @ResponseHeader Content-Encoding utf-8
	 * @HTTP 200 OK
	 * @HTTP 500 サーバーエラー
	 */
	@GET
	@Path("streamingoutput")
	@Produces(MediaType.TEXT_PLAIN)
	@GzipResponse
	@ApiOperation(
		value = "StreamingOutputのテスト",
		position = 2,
		notes = "gzip圧縮して返す",
		response = StreamingOutput.class
	)
	public Response testStreamingOutput() {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) {
				try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("test.txt");
						Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
						Writer writer = new OutputStreamWriter(output)) {
					
					char[] charBuf = new char[1024];
					while (true) {
						int len = reader.read(charBuf);
						if (len < 0) break;
						writer.write(charBuf, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
				}
			}
		};
		
		return Response.ok(stream).encoding(StandardCharsets.UTF_8.displayName()).build();
	}

	/**
	 * 指定された画像ファイルの中身をGZIPして返却する。
	 * @param responseType "blob"(File)か"arraybuffer"(InputStream)
	 * @param filename 取得ファイル名
	 * @return 指定されたファイルの中身
	 * @ResponseHeader Transfer-Encoding chunked
	 * @HTTP 200 OK
	 * @HTTP 400 リクエストのレスポンスタイプがBlobかArrayBuffer以外
	 * @HTTP 500 指定ファイルがない/開けない
	 */
	@GET
	@Path("images")
	@Produces("image/jpeg")
	@GzipResponse
	@ApiOperation(
		value = "指定された画像ファイルの中身をGZIPして返却する。",
		position = 3,
		notes = "gzip圧縮して返す",
		response = File.class
	)
	public Response getImage(
			@ApiParam(value = "responseType blob(File)かarraybuffer(InputStream)", required = true)
			@QueryParam("responseType") final String responseType,
			@ApiParam(value = "取得ファイル名", required = true)
			@QueryParam("filename") final String filename) {
		
		try(final InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename)) {

			final java.nio.file.Path path = Files.createTempFile("temp_", filename.substring(filename.lastIndexOf(".")));
			final String mediaType = Files.probeContentType(path);

			switch(responseType) {
			case "arraybuffer":
				return Response.ok(this.getClass().getClassLoader().getResourceAsStream(filename), mediaType).build();
			case "blob":
				OutputStream out = null;
				
				try {
					File tempFile = path.toFile();
					tempFile.deleteOnExit();
					out = new FileOutputStream(tempFile);
					int c;
					while ((c = in.read()) != -1) {
						out.write(c);
					}
					out.flush();
					return Response.ok(tempFile, mediaType).build();
				} finally {
					if (out != null) {
						out.close();
					}
				}
			default:
				throw new WebApplicationException(Status.BAD_REQUEST);	
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.canon.tsz662.rest.interceptors;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * MessageBodyWriterから送出されたレスポンスをGZIP圧縮
 * @ResponseHeader Content-Encoding gzip
 * @author tsz662
 *
 */
@Provider
@GzipResponse
public class GzipResponseInterceptor implements WriterInterceptor {

	private final String Gzip = "gzip";
	
	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

		final OutputStream outputStream = context.getOutputStream();
		context.setOutputStream(new GZIPOutputStream(outputStream));
		
		MultivaluedMap<String, Object> map = context.getHeaders();
		if (!map.containsKey(HttpHeaders.CONTENT_ENCODING)) {
			context.getHeaders().putSingle(HttpHeaders.CONTENT_ENCODING, Gzip);
			System.out.println("Added Content-Encoding:gzip"); // debug
		} else if (map.get(HttpHeaders.CONTENT_ENCODING).contains("gzip")) {
			// do nothing.
			System.out.println("Already gzipped"); // debug
		} else {
			context.getHeaders().putSingle(HttpHeaders.CONTENT_ENCODING, Gzip);
		}

		context.proceed();
	}
}

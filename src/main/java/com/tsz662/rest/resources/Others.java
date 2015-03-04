package com.tsz662.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.tsz662.rest.exceptions.BadRequestAttemptException;

/**
 * 以下の出力テスト用
 * <ul>
 * <li>@FormParam
 * <li>@MatrixParam
 * <li>@Context
 * </ul>
 * 400が返る。<br>
 * @since 1.0
 * @version 1.1
 * @category テスト
 * @author tsz662
 */
@Path("others")
public class Others {
	
	/**
	 * ExceptionMapperテスト用。パラメータも戻りもなし。
	 */
	@POST
	public void noProduceAnnotation() {
		throw new BadRequestAttemptException();
	}

	/**
	 * FormParamアノテーションテスト用
	 * @param input 入力された文字列
	 * @return 「入力した文字列は{input}」という文字列
	 * @responseMessage 200 OK
	 */
	@POST
	@Path("form")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response testFormParam(@FormParam("input") String input) {
		return Response.ok().entity("入力した文字列は" + input).build();
	}
	
	/**
	 * Contextアノテーションテスト用
	 * @param req リクエスト
	 * @return リクエストのメソッド
	 * @responseMessage 200 OK
	 */
	@GET
	@Path("context")
	@Produces(MediaType.TEXT_PLAIN)
	public String testContext(@Context Request req) {
		return req.getMethod();
	}
	
	/**
	 * MatrixParamテスト用<br>
	 * URLがパス・セグメント/collection;itemID=itemIDValueを含んでいる場合、
	 * マトリックス・パラメーター名はitemIDでitemIDValueになる。
	 * @param page 本のページ番号
	 * @param filter 検索ワード
	 * @return "ok"の文字列
	 * @returnType java.lang.String
	 * @responseMessage 200 OK
	 */
	@GET
	@Path("matrixparam")
	public Response testMatrixParam(@DefaultValue(value = "0") @MatrixParam("page") int page, @DefaultValue(value = "none") @MatrixParam("filter") String filter) {
		return Response.ok().entity("ok").build();
	}
	
}

package com.tsz662.rest.resources;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tsz662.rest.exceptions.BadRequestAttemptException;
import com.tsz662.rest.models.v0.JsonTest;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
//import com.wordnik.swagger.model.ResponseMessage;

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
@Path("/others")
@Api(value = "others", description = "その他(ExceptionMapperとかFormとか)実験用リソース")
public class Others {
	
	/**
	 * FormParamアノテーションテスト用
	 * @param input 入力された文字列
	 * @return 「入力した文字列は{input}」という文字列
	 * @HTTP 200 OK
	 */
	@POST
	@Path("/form")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
		value = "FormParamアノテーションテスト用",
		position = 1
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功")
	})
	public Response testFormParam(
			@ApiParam(value = "FormParamアノテーションテスト用", required = true)
			@FormParam("input") String input) {
		return Response.ok().entity("入力した文字列は" + input).build();
	}
	
	/**
	 * Contextアノテーションテスト用
	 * @param req リクエスト
	 * @return リクエストのメソッド
	 * @HTTP 200 OK
	 */
	@GET
	@Path("/context")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
		value = "Contextアノテーションテスト用",
		position = 2
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功")
	})
	public String testContext(
			@ApiParam(value = "リクエスト(@Context)", name = "req")
			@Context Request req) {
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
	 * @HTTP 200 OK
	 */
	@GET
	@Path("/matrixparam")
	@ApiOperation(
		value = "MatrixParamアノテーションテスト用",
		position = 3
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功")
	})
	public Response testMatrixParam(
			@ApiParam(value = "本のページ番号", required = true)
			@DefaultValue(value = "0") @MatrixParam("page") int page, 
			@ApiParam(value = "検索ワード", required = true)
			@DefaultValue(value = "none") @MatrixParam("filter") String filter) {
		return Response.ok().entity("ok").build();
	}

	/**
	 * ExceptionMapperテスト用。パラメータも戻りもなし。
	 */
	@POST
	@Path("/exmapper")
	@ApiOperation(
		value = "ExceptionMapperテスト用。パラメータも戻りもなし。",
		position = 4
	)
	@ApiResponses(value = {
		@ApiResponse(code = 404, message = "不正リクエスト")
	})
	public void noProduceAnnotation() {
		throw new BadRequestAttemptException();
	}
	
	/**
	 * 指定されたIDのContactを更新する。
	 * @version 0.9
	 */
	@PUT
	@Path("/jsontest")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "JsonTest出力用。",
			position = 5,
			response = JsonTest.class
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "JsonTestモデル")
	})
	public JsonTest updateContact() {
		return new JsonTest();
	}

	@GET
	@Path("/java8test")
	@ApiOperation(
		value = "java8テスト", 
		notes = "何も返しません", 
		position = 6
	)
	@ApiResponses(value = {
		@ApiResponse(code = 204, message = "No content.")
	})
	public Response java8Test() {
		Arrays.asList("a","b","c").forEach(System.out::println);
		return Response.status(Status.NO_CONTENT).build();
	}
}

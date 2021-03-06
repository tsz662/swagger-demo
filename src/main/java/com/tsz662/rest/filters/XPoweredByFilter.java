package com.tsz662.rest.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.ext.Provider;

/**
 * 全てのリクエストに対するフィルタークラス。<br>
 * X-Powered-Byヘッダに"tsz662"が設定されていなければ400を返す。<br>
 * <strong>注意</strong><br>
 * Swagger Specをhttp://コンテキストルート/ApplicationPathの値/api-docsで取得できるようにする為には
 * <ol>
 * <li>{@link javax.ws.rs.ext.Provider}アノテーションを外し
 * <li>{@link com.tsz662.rest.Application}での登録を外し
 * <li>{@link com.tsz662.rest.filters.DynamicFitlerRegistere}で動的登録する
 * </ol>
 * @author tsz662
 * @HTTP 400 X-Powered-Byヘッダがない、または値が"tsz662"ではない
 *
 */
//@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class XPoweredByFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (requestContext.getHeaderString("X-Powered-By") == null 
			|| !requestContext.getHeaderString("X-Powered-By").equals("tsz662")) {
			System.out.println("Request aborted by XPoweredByFilter!");
			requestContext.abortWith(Response.status(Status.BAD_REQUEST).entity("X-Powered-By header missing!").build());
		}
	}
}

package com.tsz662.rest.resources;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.tsz662.rest.models.ContactsMap;
import com.tsz662.rest.models.v0.ErrorInfo;
import com.tsz662.rest.models.v1.Contact;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

/**
 * Contactを扱うリソースクラス。
 * @since 0.9
 * @version 1.0
 * @author tsz662
 */
// Swagger @ApiParam supports: @PathParam, @QueryParam, @HeaderParam, @FormParam, @BeanParam
@Path("/contacts")
@Api(value = "/contacts", description = "Contact関連のリソース")
public class Contacts {
	
	private final Map<Integer, Contact> contacts = ContactsMap.contacts;
	
	/**
	 * 登録済みContact全員を返す。
	 * @return 登録済みContactリスト
	 * @HTTP 200 成功 Collection<com.canon.tsz662.rest.models.v1.Contact>
	 * @HTTP 404 Contactが未登録
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "登録済みContact全員を返す。",
		position = 1,
		responseContainer = "List",
		response = Contact.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 404, message = "Contactが未登録")
	})
	public Collection<Contact> getContacts() {
		if (contacts.isEmpty()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		} else {
			return contacts.values();
		}
	}
	
	/**
	 * 指定されたIDのContactを返す。
	 * @param id 取得したいContactのID
	 * @return 指定されたIDのContact情報
	 * @HTTP 200 成功 com.canon.tsz662.rest.models.v1.Contact
	 * @HTTP 404 指定されたIDのContactがいない com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @HTTP 500 サーバーエラー com.canon.tsz662.rest.models.v0.ErrorInfo
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "指定されたIDのContactを返す。",
		position = 3,
		notes = "エラー時、詳細情報はレスポンスボディで返す。",
		response = Contact.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功", response = Contact.class),
		@ApiResponse(code = 404, message = "指定されたIDのContactがいない", response = ErrorInfo.class),
		@ApiResponse(code = 500, message = "サーバーエラー", response = ErrorInfo.class)
	})
	public Contact getContact(
			@ApiParam(value = "取得したいContactのID", required = true)
			@PathParam("id") int id) {
		try {
			if (id <= contacts.size()) {
				Contact contact = contacts.get(id);
				return contact;
			} else {
				ErrorInfo errorInfo = new ErrorInfo();
				errorInfo.error = Status.NOT_FOUND.name();
				errorInfo.error_description = "No such contact.";
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.error = Status.INTERNAL_SERVER_ERROR.name();
			errorInfo.error_description = e.getMessage();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Contactの指定された情報を返す。
	 * @param id 属性を取得したいContactのID
	 * @param cookie "hoge"クッキーの値
	 * @param attrib 取得したい属性
	 * @return 指定された属性情報
	 * @HTTP 200 OK
	 * @HTTP 400 不正リクエスト
	 * @HTTP 403 クッキーがない
	 * @HTTP 404 指定されたIDのContactがいない
	 * @since 1.0
	 */
	@GET
	@Path("/{id}/attrib")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
		value = "Contactの指定された情報を返す。", 
		position = 4
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 400, message = "不正なリクエスト"),
		@ApiResponse(code = 403, message = "クッキーがない"),
		@ApiResponse(code = 404, message = "指定されたIDのContactがいない")
	})
	public String getContactInfo(
			@ApiParam(value = "属性を取得したいContactのID", required = true)
			@PathParam("id") int id,
			@ApiParam(value = "hogeクッキーの値", required = true)
			@CookieParam("hoge") String cookie,
			@ApiParam(value = "取得したい属性", required = true)
			@QueryParam("field") String attrib) {
		
		if (cookie != null) {
			if (!cookie.equals("foobar")) {
				//throw new WebApplicationException(Status.FORBIDDEN);
			}
		}
		
		try {
			if (id <= contacts.size()) {
				Contact contact = contacts.get(id);
				
				String rtn = null;
				switch(attrib) {
				case "name":
					rtn = contact.getName();
					break;
				case "address":
					rtn = contact.getAddress();
					break;
				default:
					throw new WebApplicationException(Status.BAD_REQUEST);
				}
				return rtn;
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (NumberFormatException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
	}
	
	/**
	 * Contactを新規作成する。
	 * ※Swaggerでレスポンスヘッダを記述するアノテーションがない！？
	 * @param auth 認証トークン文字列
	 * @param csrf CSRFトークン文字列
	 * @param contact 登録するContactの情報
	 * @param uriInfo リクエストのURI情報
	 * @ResponseHeader Location 新規作成されたContactリソースのリンク
	 * @return 結果のJSON
	 * @HTTP 201 Contactが登録された
	 * @HTTP 400 不正リクエスト
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Contactを新規作成する。",
		notes = "Locationヘッダで新規作成されたContactリソースのリンクを返す",
		position = 2,
		response = String.class,
		responseContainer = "Map"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "作成成功"),
		@ApiResponse(code = 400, message = "不正なリクエスト")
	})
	public Response createContact(
			@ApiParam(value = "認証トークン文字列", required = true, defaultValue = "foo")
			@HeaderParam("X-Auth") String auth, 
			@ApiParam(value = "CSRFトークン文字列", required = true, defaultValue = "bar")
			@HeaderParam("X-CSRF") String csrf, 
			@Context UriInfo uriInfo,
			@ApiParam(value = "登録するContactの情報", required = true)
			Contact contact) {
		
		if (!auth.equals("foo") || !csrf.equals("bar")) {
			return Response.status(Status.BAD_REQUEST).entity("X-Auth is " + auth + " and X-CSRF is " + csrf + ", but they're wrong!").build();
		}
		
		try {
			int newId;
			if (contacts.isEmpty()) {
				newId = 1;
			} else {
				newId = contacts.size() + 1;
			}
			contact.setId(newId);
			contacts.put(newId, contact);
			return Response.status(Status.CREATED).location(uriInfo.getAbsolutePath()).entity("{\"result\":\"ok\"}").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("{\"result\":\"ng\"}").build();
		}
	}
	
	/**
	 * 指定されたIDのContactを更新する。
	 * @param id 更新するContactのID
	 * @param update 更新するContactの情報
	 * @return 更新されたContact情報
	 * @HTTP 200 更新成功 com.canon.tsz662.rest.models.v1.Contact
	 * @HTTP 404 指定されたIDのContactがいない com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @HTTP 500 サーバーエラー com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @since 1.0
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "指定されたIDのContactを更新する。",
		position = 5,
		response = Contact.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "指定されたIDのContact", response = Contact.class),
		@ApiResponse(code = 404, message = "指定されたIDのContactがいない", response = ErrorInfo.class),
		@ApiResponse(code = 400, message = "サーバーエラー", response = ErrorInfo.class)
	})
	public Response updateContact(
			@ApiParam(value="更新するContactのID", required = true)
			@PathParam("id") int id,
			@ApiParam(value = "更新するContactの情報", required = true)
			Contact update) {
		ErrorInfo eInfo;

		try {
			if (contacts.containsKey(id)) {
				Contact contact = contacts.get(id);
				if (update.getName() != null && !update.getName().equals(contact.getName())) {
					contact.setName(update.getName());
				}
				if (update.getAddress() != null && !update.getAddress().equals(contact.getAddress())) {
					contact.setAddress(update.getAddress());
				}
				return Response.ok().entity(contact).build();
			} else {
				eInfo = new ErrorInfo();
				eInfo.error = Status.NOT_FOUND.name();
				eInfo.error_description = Status.NOT_FOUND.getReasonPhrase();
				return Response.status(Status.NOT_FOUND).entity(eInfo).build();
			}
		} catch (Exception e) {
			eInfo = new ErrorInfo();
			eInfo.error = Status.INTERNAL_SERVER_ERROR.name();
			eInfo.error_description = e.getMessage();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(eInfo).build();
		}
	}
	
	/**
	 * 指定されたIDのContactを削除する。
	 * @param id 削除するContactのID
	 * @return なし
	 * @HTTP 204 削除成功
	 * @HTTP 404 指定されたIDのContactがいない java.lang.String
	 * @HTTP 500 サーバーエラー
	 */
	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "指定されたIDのContactを削除する。", 
		position = 6
	)
	@ApiResponses(value = {
		@ApiResponse(code = 204, message = "削除成功"),
		@ApiResponse(code = 404, message = "指定されたIDのContactがいない"),
		@ApiResponse(code = 400, message = "サーバーエラー")
	})
	public Response deleteContact(
			@ApiParam(value = "削除するContactのID", required = true)
			@PathParam("id") int id) {
		try {
			if (Integer.valueOf(id) <= contacts.size()) {
				contacts.remove(id);
				return Response.status(Status.NO_CONTENT).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}
}
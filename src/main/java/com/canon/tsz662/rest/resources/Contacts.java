package com.canon.tsz662.rest.resources;

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
import com.canon.tsz662.rest.models.ContactsMap;
import com.canon.tsz662.rest.models.v0.ErrorInfo;
import com.canon.tsz662.rest.models.v0.JsonTest;
import com.canon.tsz662.rest.models.v1.Contact;

/**
 * Contactを扱うリソースクラス。
 * @since 0.9
 * @version 1.0
 * @author tsz662
 */
@SuppressWarnings("deprecation")
@Path("contacts")
public class Contacts {
	
	private final Map<Integer, Contact> contacts = ContactsMap.contacts;
	
	/**
	 * 登録済みContact全員を返す。
	 * @return 登録済みContactリスト
	 * @responseMessage 200 成功 `Collection<com.canon.tsz662.rest.models.v1.Contact>
	 * @responseMessage 404 Contactが未登録
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
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
	 * @responseMessage 200 成功 `com.canon.tsz662.rest.models.v1.Contact
	 * @responseMessage 404 指定されたIDのContactがいない `com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @responseMessage 500 サーバーエラー `com.canon.tsz662.rest.models.v0.ErrorInfo
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Contact getContact(@PathParam("id") int id) {
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
	 * @responseMessage 200 OK
	 * @responseMessage 400 不正リクエスト
	 * @responseMessage 403 クッキーがない
	 * @responseMessage 404 指定されたIDのContactがいない
	 * @since 1.0
	 */
	@GET
	@Path("{id}/attrib")
	@Produces(MediaType.TEXT_PLAIN)
	public String getContactInfo(@PathParam("id") int id, @CookieParam("hoge") String cookie, @QueryParam("field") String attrib) {
		
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
	 * @param auth 認証トークン文字列
	 * @param csrf CSRFトークン文字列
	 * @param contact 登録するContactの情報
	 * @param uriInfo リクエストのURI情報
	 * @ResponseHeader Location 新規作成されたContactリソースのリンク(効かない)
	 * @return 結果のJSON
	 * @responseMessage 201 Contactが登録された
	 * @responseMessage 400 不正リクエスト
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createContact(@HeaderParam("X-Auth") String auth, @HeaderParam("X-CSRF") String csrf, @Context UriInfo uriInfo,Contact contact) {
		
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
	 * @responseMessage 200 更新成功 `com.canon.tsz662.rest.models.v1.Contact
	 * @responseMessage 404 指定されたIDのContactがいない `com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @responseMessage 500 サーバーエラー `com.canon.tsz662.rest.models.v0.ErrorInfo
	 * @since 1.0
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContact(@PathParam("id") int id, Contact update) {
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
	 * 指定されたIDのContactを更新する。
	 * @param id 更新するContactのID
	 * @param update アップデート情報
	 * @responseMessage 501 非対応
	 * @deprecated ボディには{@link com.canon.tsz662.rest.models.v1.Contact}を記述してください。
	 * @version 0.9
	 */
	@PUT
	@Path("{id}/deprecated")
	@Consumes(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response updateContact(@PathParam("id") int id, JsonTest update) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
	
	/**
	 * 指定されたIDのContactを削除する。
	 * @param id 削除するContactのID
	 * @return なし
	 * @responseMessage 204 削除成功
	 * @responseMessage 404 指定されたIDのContactがいない `java.lang.String
	 * @responseMessage 500 サーバーエラー
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") int id) {
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

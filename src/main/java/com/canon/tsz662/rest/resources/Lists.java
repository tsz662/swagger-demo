package com.canon.tsz662.rest.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.canon.tsz662.rest.models.ContactsMap;
import com.canon.tsz662.rest.models.v1.Contact;

/**
 * Java8テスト
 * @author tsz662
 *
 */
@Path("lists")
public class Lists {
	
	/**
	 * Contact[]を返す。returnタグの出力確認用。
	 * @return Contactのリスト
	 * @responseMessage 200 成功 `com.canon.tsz662.rest.models.v1.Contact[]
	 */
	@GET
	@Path("listtest")
	@Produces(MediaType.APPLICATION_JSON)
	public Contact[] getContactList() {
		Contact[] contacts = {new Contact(), new Contact()};
		return contacts;
	}

	/**
	 * List<Contact>を返す。returnタグの出力確認用。
	 * @return Contactのリスト
	 * @responseMessage 200 成功 `List<com.canon.tsz662.rest.models.v1.Contact>
	 */
	@GET
	@Path("listtest2")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contact> getContactList2() {
		Contact c1 = new Contact();
		c1.setName("1");
		c1.setAddress("Tokyo");
		Contact c2 = new Contact();
		c2.setName("2");
		c2.setAddress("USA");
		List<Contact> list = Arrays.asList(c1, c2);
		return list;
	}
	
	/**
	 * Boolean[]を返す。returnタグの出力確認用。
	 * @return 真偽値のリスト
	 * @responseMessage 200 成功 `Boolean[]
	 */
	@GET
	@Path("listtest3")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean[] getBooleanList() {
		Boolean[] rtn = {true, true, false};
		return rtn;
	}

	/**
	 * Map<Integer, Contact>を返す。
	 * @return マップ1
	 * @responseMessage 200 成功 `Map<Integer, com.canon.tsz662.rest.models.v1.Contact>
	 */
	@GET
	@Path("maptest")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, Contact> getContactMap() {
		return ContactsMap.contacts;
	}
	
	/**
	 * Map<String, Contact[]>を返す。
	 * @return マップ2
	 * @responseMessage 200 成功 `Map<String, com.canon.tsz662.rest.models.v1.Contact[]>
	 */
	@GET
	@Path("maptest2")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Contact[]> getContactMap2() {
		Contact[] contacts = {new Contact(), new Contact()};
		Map<String, Contact[]> rtn = new HashMap<>();
		rtn.put("contacts", contacts);
		return rtn;
	}

	/**
	 * Map<String, Collection<Contact>>を返す。<br>
	 * @return マップ3
	 * @responseMessage 200 成功 `Map<String, Collection<com.canon.tsz662.rest.models.v1.Contact>>
	 */
	@GET
	@Path("maptest3")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Collection<Contact>> getContactMap3() {
		Collection<Contact> contacts = ContactsMap.contacts.values();
		Map<String, Collection<Contact>> rtn = new HashMap<>();
		rtn.put("contacts", contacts);
		return rtn;
	}
	
	/**
	 * Map<String, List<Boolean>>を返す。<br>
	 * @return マップ3
	 * @responseMessage 200 成功 `Map<String, List<Boolean>>
	 */
	@GET
	@Path("maptest4")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<Boolean>> getContactMap4() {
		List<Boolean> bools = Arrays.asList(true, false);
		Map<String, List<Boolean>> rtn = new HashMap<>();
		rtn.put("bools", bools);
		return rtn;
	}
	
	/**
	 * Map<String, Map<String, Contact>>を返す。<br>
	 * @return マップ4
	 * @responseMessage 200 成功 `Map<String, Map<String, com.canon.tsz662.rest.models.v1.Contact>>
	 */
	@GET
	@Path("maptest5")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Map<String, Contact>> getContactMap5() {
		Map<String, Contact> map1 = new HashMap<>();
		map1.put("contact", new Contact());
		Map<String, Map<String, Contact>> rtn = new HashMap<>();
		rtn.put("v1", map1);
		return rtn;
	}

	/**
	 * Map<String, Map<String, List<Contact>>>を返す。<br>
	 * @return マップ5
	 * @responseMessage 200 成功 
	 * 			`Map<String, Map<String, List<com.canon.tsz662.rest.models.v1.Contact>>>
	 */
	@GET
	@Path("maptest6")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Map<String, List<Contact>>> getContactMap6() {
		Contact[] _contacts = {new Contact(), new Contact()};
		List<Contact> contacts = Arrays.asList(_contacts);
		Map<String, List<Contact>> map1 = new HashMap<>();
		map1.put("contacts", contacts);
		Map<String, Map<String, List<Contact>>> rtn = new HashMap<>();
		rtn.put("v1", map1);
		return rtn;
	}

}

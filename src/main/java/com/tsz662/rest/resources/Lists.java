package com.tsz662.rest.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.tsz662.rest.models.ContactsMap;
import com.tsz662.rest.models.v1.Contact;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * テスト
 * @author tsz662
 *
 */
@Path("/lists")
@Api(value = "lists", description = "配列/連想配列出力実験用リソース")
public class Lists {
	
	/**
	 * Contact[]を返す。returnタグの出力確認用。
	 * @return Contactのリスト
	 * @HTTP 200 成功 com.canon.tsz662.rest.models.v1.Contact[]
	 */
	@GET
	@Path("/listtest")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Contact[]を返す。returnタグの出力確認用。",
		response = Contact.class,
		position = 1,
		responseContainer = "Array")
	public Contact[] getContactList() {
		Contact[] contacts = {new Contact(), new Contact()};
		return contacts;
	}

	/**
	 * List&lt;Contact&gt;を返す。returnタグの出力確認用。【Swaggerはここを見ない！】
	 * @return Contactのリスト
	 * @HTTP 200 成功 List<com.canon.tsz662.rest.models.v1.Contact>
	 */
	@GET
	@Path("/listtest2")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "List&lt;Contact&gt;を返す。returnタグの出力確認用。",
		response = Contact.class,
		position = 2,
		responseContainer = "List")
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
	 * @HTTP 200 成功 Boolean[]
	 */
	@GET
	@Path("/listtest3")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Boolean[]を返す。returnタグの出力確認用。",
		response = Boolean.class,
		position = 3,
		responseContainer = "Array")
	public Boolean[] getBooleanList() {
		Boolean[] rtn = {true, true, false};
		return rtn;
	}

	/**
	 * Map<Integer, Contact>を返す。
	 * @return マップ1
	 * @HTTP 200 成功 Map<Integer, com.canon.tsz662.rest.models.v1.Contact>
	 */
	@GET
	@Path("/maptest")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;Integer, Contact&gt;を返す。returnタグの出力確認用。",
		position = 4,
		response = Contact.class,
		responseContainer = "Map"
	)
	public Map<Integer, Contact> getContactMap() {
		return ContactsMap.contacts;
	}
	
	/**
	 * Map<String, Contact[]>を返す。
	 * @return マップ2
	 * @HTTP 200 成功 Map<String, com.canon.tsz662.rest.models.v1.Contact[]>
	 */
	@GET
	@Path("/maptest2")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;String, Contact[]&gt;を返す。",
		position = 5,
		response = Contact[].class,
		responseContainer = "Map"
	)
	public Map<String, Contact[]> getContactMap2() {
		Contact[] contacts = {new Contact(), new Contact()};
		Map<String, Contact[]> rtn = new HashMap<>();
		rtn.put("contacts", contacts);
		return rtn;
	}

	/**
	 * Map<String, Collection<Contact>>を返す。<br>
	 * @return マップ3
	 * @HTTP 200 成功 Map<String, Collection<com.canon.tsz662.rest.models.v1.Contact>>
	 */
	@GET
	@Path("/maptest3")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;String, Collection&lt;Contact&gt;&gt;を返す。",
		position = 6,
		response = Contact[].class,
		responseContainer = "Map"
	)
	public Map<String, Collection<Contact>> getContactMap3() {
		Collection<Contact> contacts = ContactsMap.contacts.values();
		Map<String, Collection<Contact>> rtn = new HashMap<>();
		rtn.put("contacts", contacts);
		return rtn;
	}
	
	/**
	 * Map<String, List<Boolean>>を返す。<br>
	 * @return マップ3
	 * @HTTP 200 成功 Map<String, List<Boolean>>
	 */
	@GET
	@Path("/maptest4")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;String, List&lt;Boolean&gt;&gt;を返す。",
		position = 7,
		response = Boolean[].class,
		responseContainer = "Map"
	)
	public Map<String, List<Boolean>> getContactMap4() {
		List<Boolean> bools = Arrays.asList(true, false);
		Map<String, List<Boolean>> rtn = new HashMap<>();
		rtn.put("bools", bools);
		return rtn;
	}
	
	/**
	 * ResponseでMap<String, Map<String, Contact>>を返す。<br>
	 * @return マップ4
	 * @HTTP 200 成功 Map<String, Map<String, com.canon.tsz662.rest.models.v1.Contact>>
	 */
	@GET
	@Path("/maptest5")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;String, Map&lt;String, Contact&gt;&gt;を返す。",
		position = 8,
		response = Map.class,
		responseContainer = "Map"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = Map.class, message = "Map&lt;String, Map&lt;String, Contact&gt;&gt;")
	})
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
	 * @HTTP 200 成功 
	 * 			Map<String, Map<String, List<com.canon.tsz662.rest.models.v1.Contact>>>
	 */
	@GET
	@Path("/maptest6")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
		value = "Map&lt;String, Map&lt;String, List&lt;Contact&gt;&gt;&gt;を返す。",
		position = 9,
		response = Map.class,
		responseContainer = "Map"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = Map.class, message = "Map&lt;String, Map&lt;String, List&lt;Contact&gt;&gt;&gt;")
	})
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

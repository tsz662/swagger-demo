package com.tsz662.rest.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tsz662.rest.models.v1.Contact;

public class ContactsMap {

	public static Map<Integer, Contact> contacts = new ConcurrentHashMap<Integer, Contact>();
	
	static {
		Contact contact = new Contact();
		contact.setId(1);
		contact.setName("admin");
		contact.setAddress("Kawasaki,Japan");
		contacts.put(1, contact);
	}
}

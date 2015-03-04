package com.tsz662.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class Application extends ResourceConfig {

	final String rootPackage = this.getClass().getPackage().getName();
	
	public Application() {
		
		super.packages(
				// Resources
				this.rootPackage + "/resources",
				// ExceptionMappers
				this.rootPackage + "/exceptions/mappers",
				// Filters
				this.rootPackage + "/filters",
				// Interceptors
				this.rootPackage + "/interceptors"
		);

		// MultiPartFeatureクラス
		super.packages("org.glassfish.jersey.examples.multipart").register(MultiPartFeature.class);
		
/*
		Map<String, String> properties = new HashMap<>();
		properties.put("com.sun.jersey.config.property.packages",
				"com.wordnik.swagger.sample.resource;com.wordnik.swagger.sample.util;com.wordnik.swagger.jaxrs.listing;com.wordnik.swagger.jaxrs.jso");
		properties.put("com.sun.jersey.spi.container.ContainerRequestFilters", 
				"com.sun.jersey.api.container.filter.PostReplaceFilter");
		properties.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		properties.put("com.sun.jersey.config.feature.DisableWADL", "true");
		
		super.setProperties(properties);
*/
	}
}

package com.tsz662.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.tsz662.rest.exceptions.mappers.BadRequestAttemptExceptionMapper;
import com.tsz662.rest.filters.DynamicFilterRegisterer;
//import com.tsz662.rest.filters.XPoweredByFilter;
import com.tsz662.rest.interceptors.GzipResponseInterceptor;
import com.tsz662.rest.resources.Contacts;
import com.tsz662.rest.resources.Lists;
import com.tsz662.rest.resources.MyFiles;
import com.tsz662.rest.resources.Others;
import com.wordnik.swagger.jaxrs.config.BeanConfig;

/**
 * {@link org.glassfish.jersey.server.ResourceConfig}を使わない場合はこちら
 */
@ApplicationPath("api")
public class MyApplication extends Application {
	
	public MyApplication() {
		BeanConfig config = new BeanConfig();
		config.setVersion("1.0.0");
		config.setBasePath("http://localhost:8080/swagger-demo");
		config.setResourcePackage("com.tsz662.rest.resources");
		config.setScan(true);
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		
		Set<Class<?>> resources = new HashSet<>();

		// Resources
		resources.addAll(Arrays.asList(Contacts.class, Lists.class, MyFiles.class, Others.class));
		
		// ExceptionMappers
		resources.add(BadRequestAttemptExceptionMapper.class);
		
		// Filters
		// ここでは登録しない
		//resources.add(XPoweredByFilter.class);
		
		// DynamicFeatureサブクラスを登録
		resources.add(DynamicFilterRegisterer.class);
		
		// Interceptors
		resources.add(GzipResponseInterceptor.class);
		
		// Multipart
		resources.add(MultiPartFeature.class);
		
		// Swagger
		resources.add(com.wordnik.swagger.jersey.listing.ApiListingResourceJSON.class);
		resources.add(com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider.class);
		resources.add(com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider.class);

       return resources;
	}
}

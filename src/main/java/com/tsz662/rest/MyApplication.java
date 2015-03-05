package com.tsz662.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.tsz662.rest.exceptions.mappers.BadRequestAttemptExceptionMapper;
import com.tsz662.rest.filters.DynamicFilterRegisterer;
import com.tsz662.rest.filters.XPoweredByFilter;
import com.tsz662.rest.interceptors.GzipResponseInterceptor;
import com.tsz662.rest.resources.Contacts;
import com.tsz662.rest.resources.Lists;
import com.tsz662.rest.resources.MyFiles;
import com.tsz662.rest.resources.Others;
import com.wordnik.swagger.jaxrs.config.BeanConfig;

/**
 * <h2>Hooking up Swagger-Core in your Application</h2>
 * Need to add swagger-core's providers to the set up process.
 * ResourceConfig seems to be not usable.
 * <h2>Configure and Initialize Swagger</h2>
 * We will use this class' constructor for Swagger init/config,
 * because we don't want to use web.xml.
 * @author tsz662
 *
 */
@ApplicationPath("api")
public class MyApplication extends Application {
	
	/**
	 * Constructor.
	 * <table>
	 * <tr><th>Method<th>Property Name<th>Purpose</tr>
	 * <tr><td>setTitle(String)<td>title<td>Sets the title of the application.</tr>
	 * <tr><td>setDescription(String)<td>description<td>Sets the description of the application.</tr>
	 * <tr><td>setTermsOfServiceUrl(String)<td>termsOfServiceUrl<td>Sets the URL of the application's Terms of Service.</tr>
	 * <tr><td>setContact(String)<td>contact<td>Sets the contact information for the application.</tr>
	 * <tr><td>setLicense(String)<td>license<td>Sets the license of the application.</tr>
	 * <tr><td>setLicenseUrl(String)<td>licenseUrl<td>Sets the licesne url of the application.</tr>
	 * <tr><td>setVersion(String)<td>version<td>Sets the version of the API.</tr>
	 * <tr><td>setBasePath(String)<td>basePath<td>Sets the basePath for the API calls. This should point to the context root of your API.</tr>
	 * <tr><td>setApiReader(String)<td>apiReader<td>Sets an API Reader class for Swagger.</tr>
	 * <tr><td>setFilterClass(Sting)<td>filterClass<td>Sets a security filter for Swagger's documentation.</tr>
	 * <tr><td>setResourcePackage(String)<td>resourcePackage<td>Sets which package(s) Swagger should scan to pick up resources.
	 *  If there's more than one package, it can be a list of comma-separated packages.</tr>
	 * <tr><td>setScan(boolean)<td>scan<td>When set to true, Swagger will build the documentation.</tr>
	 * </table>
	 * In order for the Swagger to operate properly, you must set the base path of the application.
	 * In order for Swagger to actually produce the documentation, you must setScan(true).
	 */
	public MyApplication() {
		BeanConfig config = new BeanConfig();
		config.setVersion("1.0.0");
		config.setBasePath("http://localhost:8080/swagger-sample-noannotated");
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
		resources.add(XPoweredByFilter.class);
		// DynamicFeature implemented class
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

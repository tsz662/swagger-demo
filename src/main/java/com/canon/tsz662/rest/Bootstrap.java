package com.canon.tsz662.rest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.*;
import com.wordnik.swagger.reader.ClassReaders;
import com.wordnik.swagger.config.*;

@WebServlet(name = "SwaggerJaxrsConfig", loadOnStartup = 1)
public class Bootstrap extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) {
		ApiInfo info = new ApiInfo(
				  "Swagger Sample J8m",                             /* title */
				  "This is a sample server.", 
				  "http://xxx.com/terms/",                  /* TOS URL */
				  "suzuki.teruhiko662@canon.co.jp",                            /* Contact */
				  "Apache 2.0",                                     /* license */
				  "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
		);
		
		try {
			super.init(config);
			SwaggerConfig swaggerConfig = new SwaggerConfig();
			ConfigFactory.setConfig(swaggerConfig);
			swaggerConfig.setBasePath("http://localhost:8002/api");
			swaggerConfig.setApiVersion("1.0.0");
			swaggerConfig.setApiInfo(info);
			ScannerFactory.setScanner(new DefaultJaxrsScanner());
			ClassReaders.setReader(new DefaultJaxrsApiReader());
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
}

package com.tsz662.rest.filters;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * XPoweredByFilterを動的に登録する為のクラス
 * @author tsz662
 *
 */
@Provider
public class DynamicFilterRegisterer implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		if (resourceInfo.getResourceClass().getPackage().getName().equals("com.tsz662.rest.resources")) {
			context.register(XPoweredByFilter.class);
		}
	}
}

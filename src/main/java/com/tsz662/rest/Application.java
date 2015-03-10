package com.tsz662.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.tsz662.rest.filters.DynamicFilterRegisterer;
// 以下はswagger-core + swagger-jaxrs_2.11の場合
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
// 以下はswagger-jersey2-jaxrs_2.11の場合
import com.wordnik.swagger.jaxrs.config.BeanConfig;
//import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
//import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
//import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;

/**
 * web.xmlは使用しない為、通常のリソース登録に加え、Swaggerを使うためのセットアップを行う。
 * <ul>
 * <li>swagger-coreのProviderクラス群の登録
 * <li>Swaggerの設定と初期化
 * </ul>
 * <h3>注意</h3>
 * 全てのリソースに対するアクセスを制限するフィルターがある場合(このデモでは{@link com.tsz662.rest.filters.XPoweredByFilter})、
 * 本クラスでフィルターを登録してしまうとSwagger Specをhttp://コンテキストルート/@ApplicationPathの値/api-docsに対する
 * コールもブロックされてしまう。<br>
 * したがって、そのようなフィルターは
 * <ul>
 * <li>当該フィルタークラスから{@link javax.ws.rs.ext.Provider}アノテーションを外し
 * <li>{@link javax.ws.rs.container.DynamicFeature}サブクラスで動的に登録する必要がある
 * </ul>
 * <h3>Swagger設定</h3>
 * <table border="1">
 * <tr><th>メソッド<th>該当するプロパティ<th>目的</tr>
 * <tr><td>setTitle(String)<td>title<td>アプリのタイトル</tr>
 * <tr><td>setDescription(String)<td>description<td>アプリの説明</tr>
 * <tr><td>setTermsOfServiceUrl(String)<td>termsOfServiceUrl<td>アプリのTerms of ServiceのURL</tr>
 * <tr><td>setContact(String)<td>contact<td>連絡先</tr>
 * <tr><td>setLicense(String)<td>license<td>ライセンス</tr>
 * <tr><td>setLicenseUrl(String)<td>licenseUrl<td>ライセンスのURL</tr>
 * <tr><td>setVersion(String)<td>version<td>APIのバージョン</tr>
 * <tr><td>setBasePath(String)<td>basePath<td>API呼び出しの基底パス。APIのコンテキストルートと同じにすること</tr>
 * <tr><td>setApiReader(String)<td>apiReader<td>Swaggerの為のAPI Readerクラス？</tr>
 * <tr><td>setFilterClass(Sting)<td>filterClass<td>Swaggerで生成したドキュメントのセキュリティフィルター</tr>
 * <tr><td>setResourcePackage(String)<td>resourcePackage<td>Swaggerにスキャンさせたいリソースの単/複数のパッケージを指定する。
 * 複数ある場合はカンマ区切りにすること</tr>
 * <tr><td>setScan(boolean)<td>scan<td>ドキュメントを設定する場合はtrueを指定すること</tr>
 * </table>
 * @author tsz662
 *
 */
@ApplicationPath("api")
public class Application extends ResourceConfig {
	
	final String rootPackage = this.getClass().getPackage().getName();
	
	public Application() {
		BeanConfig config = new BeanConfig();
		config.setVersion("1.0.0");
		// MEMO:
		// different port is NG.
		// Different context root, eg, "http://localhost:8080/swagger", is NG.
		// Same api context root, eg, "http://localhost:8080/swagger-demo/api" is OK.
		config.setBasePath("http://localhost:8080/swagger-demo/api");
		config.setResourcePackage("com.tsz662.rest.resources");
		config.setScan(true);
		
		super.packages(
				// Resources
				this.rootPackage + "/resources",
				// ExceptionMappers
				this.rootPackage + "/exceptions/mappers",
				// Filters
				// 上述の通り、ここでは登録しない
				//this.rootPackage + "/filters",
				// Interceptors
				this.rootPackage + "/interceptors"
		);

		// MultiPartFeatureクラス
		super.packages("org.glassfish.jersey.examples.multipart").register(MultiPartFeature.class);
		
		// DynamicFeatureサブクラス
		// 登録していないフィルターを動的に呼び出す為のDynamicFeatureサブクラスを登録する
		super.packages(this.rootPackage).register(DynamicFilterRegisterer.class);
		
		super.packages("com.wordnik.swagger.jersey.listing").register(ApiListingResourceJSON.class);
		super.packages("com.wordnik.swagger.jersey.listing").register(ApiDeclarationProvider.class);
		super.packages("com.wordnik.swagger.jersey.listing").register(ResourceListingProvider.class);
//		super.packages("com.wordnik.swagger.jersey.listing").register(JerseyApiDeclarationProvider.class);
//		super.packages("com.wordnik.swagger.jersey.listing").register(JerseyResourceListingProvider.class);
	}
}

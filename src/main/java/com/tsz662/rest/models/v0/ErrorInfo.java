package com.tsz662.rest.models.v0;

//import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * エラー時にレスポンスエンティティに格納して返却する。
 * @author tsz662
 *
 */
//@XmlRootElement(name = "ErrorInfo")
@ApiModel(value = "ErrorInfo", description = "Contactsリソースで扱われるErrorInfoモデル")
public class ErrorInfo {

	/**
	 * エラーコード
	 */
	@ApiModelProperty(value = "エラーコード")
	public String error;
	/**
	 * エラー詳細情報
	 */
	@ApiModelProperty(value = "エラー詳細情報")
	public String error_description;
}

package com.tsz662.rest.models.v0;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * エラー時にレスポンスエンティティに格納して返却する。
 * @author tsz662
 *
 */
@XmlRootElement(name = "ErrorInfo")
public class ErrorInfo {

	/**
	 * エラーコード
	 */
	@XmlElement(name = "error")
	public String error;
	/**
	 * エラー詳細情報
	 */
	@XmlElement(name = "error_description")
	public String error_description;
}

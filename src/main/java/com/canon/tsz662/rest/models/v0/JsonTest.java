package com.canon.tsz662.rest.models.v0;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.canon.tsz662.rest.models.v1.Contact;

/**
 * JSONサンプル出力確認用。
 * @deprecated 使用不可
 * @author tsz662
 *
 */
@XmlRootElement
@Deprecated
public class JsonTest {
	
	/**
	 * 連想配列1(Map&lt;String, String&gt;)
	 */
	public Map<String, String> map1;
	
	/**
	 * 連想配列2(Map&lt;Integer, List&lt;String&gt;&gt;)
	 */
	public Map<Integer, List<String>> map2; 
	
	/**
	 * 連想配列3(Map&lt;Integer, Contact&gt;)
	 */
	public Map<Integer, Contact> map3; 

	/**
	 * ErrorInfoのリスト
	 */
	@XmlElementWrapper(name = "errorInfos")
	@XmlElement(name = "errorInfo")
	public List<ErrorInfo> errorInfoList;
	/**
	 * 文字列のリスト
	 */
	@XmlElementWrapper(name = "str1s")
	@XmlElement(name = "str1")
	public List<String> stringList;

	/**
	 * 文字列の配列
	 */
	@XmlElementWrapper(name = "str2s")
	@XmlElement(name = "str2")
	public String[] stringArray;
	
	/**
	 * 真偽値(true,false)
	 */
	public boolean isUse;
}

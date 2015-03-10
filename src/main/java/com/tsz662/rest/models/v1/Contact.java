package com.tsz662.rest.models.v1;

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Contactを表す。<br>
 * com.canon.tsz662.rest.resources.Contacts#getContactなどで使用する。
 * @since 1.0
 * @version 1.0
 * @author tsz662
 */
//@XmlRootElement(name = "Contact") // for enunciate. Swagger picks this up too if @ApiModel is absent.
@ApiModel(value = "Contact", description = "Contactsリソースで扱われるContactモデル")
public class Contact {
	private int id;
    private String name;
    private String address;

    /**
     * 連絡先のID
     */
//    @XmlElement(name = "id")
    @ApiModelProperty(value = "連絡先のID")
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
     * 連絡先の名前
     */
//	@XmlElement(name = "name")
    @ApiModelProperty(value = "連絡先の名前")
    public String getName() {
    	return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    /**
     * 連絡先アドレス
     */
//    @XmlElement(name = "address")
    @ApiModelProperty(value = "連絡先アドレス")
    public String getAddress() {
    	return address;
    }

    public void setAddress(String address) {
    	this.address = address;
    }
}
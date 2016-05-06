package com.iyingdi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="contribute_userinfo")
public class Userinfo {

	/*
	 * 本类为用户信息类，主要存储用户详细信息，手机、邮箱和支付宝账号等
	 */
	
	private int id;
	private int userid;			//用户ID
	private String tel;			//手机
	private String email;		//邮箱
	private String alipay;		//支付宝账号
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
}

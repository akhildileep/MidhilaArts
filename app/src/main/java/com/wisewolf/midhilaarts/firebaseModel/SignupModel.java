package com.wisewolf.midhilaarts.firebaseModel;

import com.google.gson.annotations.SerializedName;

public class SignupModel {

	@SerializedName("active")
	private boolean active;

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@SerializedName("age")
	private String age;


	@SerializedName("city")
	private String city;

	@SerializedName("device_token")
	private String device_token;

	@SerializedName("device_type")
	private String device_type;

	@SerializedName("isloged_in")
	private String isloged_in;

	@SerializedName("mobile_no")
	private String mobile_no;

	@SerializedName("password")
	private String password;

	@SerializedName("plantype")
	private String plantype;

	@SerializedName("email")
	private String email;

	@SerializedName("createdAt")
	private String createdAt;

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@SerializedName("user_name")
	private String user_name;

	@SerializedName("user_type")
	private String user_type;

	public String getAge() {
		return age;
	}

	public String getCity() {
		return city;
	}

	public String getDevice_token() {
		return device_token;
	}

	public String getDevice_type() {
		return device_type;
	}

	public String getIsloged_in() {
		return isloged_in;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public String getPassword() {
		return password;
	}

	public String getPlantype() {
		return plantype;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public void setIsloged_in(String isloged_in) {
		this.isloged_in = isloged_in;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPlantype(String plantype) {
		this.plantype = plantype;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
}
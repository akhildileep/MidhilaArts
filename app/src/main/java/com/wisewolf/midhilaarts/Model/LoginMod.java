package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class LoginMod{

	@SerializedName("user_type")
	private String userType;

	@SerializedName("city")
	private String city;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("mobile_no")
	private String mobileNo;

	@SerializedName("active")
	private boolean active;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("isloged_in")
	private String islogedIn;

	@SerializedName("id")
	private String id;

	@SerializedName("plantype")
	private String plantype;

	@SerializedName("age")
	private String age;

	public String getUserType(){
		return userType;
	}

	public String getCity(){
		return city;
	}

	public String getUserName(){
		return userName;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public boolean isActive(){
		return active;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public String getIslogedIn(){
		return islogedIn;
	}

	public String getId(){
		return id;
	}

	public String getPlantype(){
		return plantype;
	}

	public void setPlantype(String plantype) {
		this.plantype = plantype;
	}

	public String getAge(){
		return age;
	}
}
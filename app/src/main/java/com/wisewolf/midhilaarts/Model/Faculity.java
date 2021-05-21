package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class Faculity{

	@SerializedName("qualification")
	private String qualification;

	@SerializedName("name")
	private String name;

	@SerializedName("details")
	private String details;

	@SerializedName("experience")
	private String experience;

	public String getQualification(){
		return qualification;
	}

	public String getName(){
		return name;
	}

	public String getDetails(){
		return details;
	}

	public String getExperience(){
		return experience;
	}
}
package com.wisewolf.midhilaarts;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Gallery{

	@SerializedName("id")
	private String id;

	@SerializedName("url")
	private List<String> url;

	public String getId(){
		return id;
	}

	public List<String> getUrl(){
		return url;
	}
}
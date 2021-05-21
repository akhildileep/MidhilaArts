package com.wisewolf.midhilaarts.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Tools{

	@SerializedName("pencil")
	private List<String> pencil;

	@SerializedName("colors")
	private List<String> colors;

	@SerializedName("palate")
	private List<String> palate;

	public List<String> getPencil(){
		return pencil;
	}

	public List<String> getColors(){
		return colors;
	}

	public List<String> getPalate(){
		return palate;
	}
}
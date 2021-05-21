package com.wisewolf.midhilaarts;

import com.google.gson.annotations.SerializedName;

public class Date{

	@SerializedName("_nanoseconds")
	private int nanoseconds;

	@SerializedName("_seconds")
	private int seconds;

	public int getNanoseconds(){
		return nanoseconds;
	}

	public int getSeconds(){
		return seconds;
	}
}
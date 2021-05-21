package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class PriceItem{

	@SerializedName("duration")
	private String duration;

	@SerializedName("amount")
	private String amount;

	@SerializedName("discount")
	private String discount;

	public String getDuration(){
		return duration;
	}

	public String getAmount(){
		return amount;
	}

	public String getDiscount(){
		return discount;
	}
}
package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class Transactions{

	@SerializedName("date")
	private String date;

	@SerializedName("amount")
	private String amount;

	@SerializedName("paymentMode")
	private String paymentMode;

	@SerializedName("id")
	private String id;

	@SerializedName("packHead")
	private String packHead;

	@SerializedName("packName")
	private String packName;

	@SerializedName("userId")
	private String userId;

	@SerializedName("razor_id")
	private String razorId;

	@SerializedName("status")
	private String status;

	public String getDate(){
		return date;
	}

	public String getAmount(){
		return amount;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public String getId(){
		return id;
	}

	public String getPackHead(){
		return packHead;
	}

	public String getPackName(){
		return packName;
	}

	public String getUserId(){
		return userId;
	}

	public String getRazorId(){
		return razorId;
	}

	public String getStatus(){
		return status;
	}
}
package com.wisewolf.midhilaarts.firebaseModel;

import com.google.gson.annotations.SerializedName;

public class TransactionModel {
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRazor_id() {
		return razor_id;
	}

	public void setRazor_id(String razor_id) {
		this.razor_id = razor_id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPackHead() {
		return packHead;
	}

	public void setPackHead(String packHead) {
		this.packHead = packHead;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@SerializedName("amount")
	private String amount;

	@SerializedName("razor_id")
	private String razor_id;

	@SerializedName("userId")
	private String userId;

	@SerializedName("status")
	private String status;

	@SerializedName("paymentMode")
	private String paymentMode;

	@SerializedName("packHead")
	private String packHead;

	@SerializedName("packName")
	private String packName;

	@SerializedName("date")
	private String date;

	@SerializedName("billNo")
	private String billNo;

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	@SerializedName("refCode")
	private String refCode;











}
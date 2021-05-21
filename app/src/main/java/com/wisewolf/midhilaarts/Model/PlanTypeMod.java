package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class PlanTypeMod{
	public void setVideoNo(String videoNo) {
		this.videoNo = videoNo;
	}

	@SerializedName("duration")
	private String duration;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("video_no")
	private String videoNo;

	@SerializedName("vimeo_id")
	private String vimeoId;

	@SerializedName("id")
	private String id;

	@SerializedName("expiry")
	private String expiry;

	@SerializedName("pack_period")
	private String packPeriod;

	@SerializedName("start_date")
	private String startDate;

	public String getDuration(){
		return duration;
	}

	public String getUserId(){
		return userId;
	}

	public String getVideoNo(){
		return videoNo;
	}

	public String getVimeoId(){
		return vimeoId;
	}

	public String getId(){
		return id;
	}

	public String getExpiry(){
		return expiry;
	}

	public String getPackPeriod(){
		return packPeriod;
	}

	public String getStartDate(){
		return startDate;
	}
}
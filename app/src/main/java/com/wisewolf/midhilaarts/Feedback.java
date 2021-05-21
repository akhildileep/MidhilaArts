package com.wisewolf.midhilaarts;

import com.google.gson.annotations.SerializedName;

public class Feedback{

	@SerializedName("answer")
	private String answer;

	@SerializedName("question")
	private String question;

	@SerializedName("user_fid")
	private String user_fid;

	@SerializedName("user_name")
	private String user_name;

	@SerializedName("time")
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getUser_fid() {
		return user_fid;
	}

	public void setUser_fid(String user_fid) {
		this.user_fid = user_fid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
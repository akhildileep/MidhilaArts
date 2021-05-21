package com.wisewolf.midhilaarts.qa_model;

import com.google.gson.annotations.SerializedName;

public class QAModel{

	@SerializedName("question")
	private String question;

	@SerializedName("answer")
	private String answer;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("id")
	private String id;

	public String getQuestion(){
		return question;
	}

	public String getAnswer(){
		return answer;
	}

	public String getUserName(){
		return userName;
	}

	public String getId(){
		return id;
	}
}
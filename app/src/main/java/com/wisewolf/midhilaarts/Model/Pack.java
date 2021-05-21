package com.wisewolf.midhilaarts.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Pack{
	public Pack(String thumbnail, String ratedBy, String courseName, String ageGroup, String rating, List<String> benifit, Faculity faculity, String vimeoId, String totalNoVIdeos, Tools tools, String usedBy, List<PriceItem> price, String id, String courseDescription) {
		this.thumbnail = thumbnail;
		this.ratedBy = ratedBy;
		this.courseName = courseName;
		this.ageGroup = ageGroup;
		this.rating = rating;
		this.benifit = benifit;
		this.faculity = faculity;
		this.vimeoId = vimeoId;
		this.totalNoVIdeos = totalNoVIdeos;
		this.tools = tools;
		this.usedBy = usedBy;
		this.price = price;
		this.id = id;
		this.courseDescription = courseDescription;
	}

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("ratedBy")
	private String ratedBy;

	@SerializedName("course_name")
	private String courseName;

	@SerializedName("age_group")
	private String ageGroup;

	@SerializedName("rating")
	private String rating;

	@SerializedName("benifit")
	private List<String> benifit;

	@SerializedName("faculity")
	private Faculity faculity;

	@SerializedName("vimeo_id")
	private String vimeoId;

	@SerializedName("total_no_VIdeos")
	private String totalNoVIdeos;

	@SerializedName("tools")
	private Tools tools;

	@SerializedName("used_by")
	private String usedBy;

	@SerializedName("price")
	private List<PriceItem> price;

	@SerializedName("id")
	private String id;

	@SerializedName("course_description")
	private String courseDescription;

	public String getThumbnail(){
		return thumbnail;
	}

	public String getRatedBy(){
		return ratedBy;
	}

	public String getCourseName(){
		return courseName;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public String getAgeGroup(){
		return ageGroup;
	}

	public String getRating(){
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public List<String> getBenifit(){
		return benifit;
	}

	public Faculity getFaculity(){
		return faculity;
	}

	public String getVimeoId(){
		return vimeoId;
	}

	public String getTotalNoVIdeos(){
		return totalNoVIdeos;
	}

	public Tools getTools(){
		return tools;
	}

	public String getUsedBy(){
		return usedBy;
	}

	public List<PriceItem> getPrice(){
		return price;
	}

	public String getId(){
		return id;
	}

	public String getCourseDescription(){
		return courseDescription;
	}
}
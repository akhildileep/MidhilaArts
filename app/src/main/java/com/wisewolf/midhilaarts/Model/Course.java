package com.wisewolf.midhilaarts.Model;

import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("link")
    private String link;

    @SerializedName("linkmain")
    private String linkmain;

    @SerializedName("desc")
    private String desc;

    @SerializedName("thumb")
    private String thumb;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkmain() {
        return linkmain;
    }

    public void setLinkmain(String linkmain) {
        this.linkmain = linkmain;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

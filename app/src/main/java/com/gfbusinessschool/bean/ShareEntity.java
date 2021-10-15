package com.gfbusinessschool.bean;

public class ShareEntity {
    private String title;
    private String firstTagId;
    private String secondTagId;
    private String coverImgUrl;
    private String content;
    private String videoUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstTagId() {
        return firstTagId;
    }

    public void setFirstTagId(String firstTagId) {
        this.firstTagId = firstTagId;
    }

    public String getSecondTagId() {
        return secondTagId;
    }

    public void setSecondTagId(String secondTagId) {
        this.secondTagId = secondTagId;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
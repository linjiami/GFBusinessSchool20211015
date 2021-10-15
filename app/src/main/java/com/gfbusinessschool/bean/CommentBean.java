package com.gfbusinessschool.bean;

import com.gfbusinessschool.utils.Utils;

public class CommentBean {
    private String userId;
    private String userName;
    private String headImgUrl;
    private String content;
    private String comment;
    private String remarkTime;
    private float passRate;
    private String createDate;

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public float getPassRate() {
        return passRate;
    }

    public void setPassRate(float passRate) {
        this.passRate = passRate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getContent() {
        return Utils.isEmpty(content)?comment:content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemarkTime() {
        return Utils.isEmpty(remarkTime)?createDate:remarkTime;
    }

    public void setRemarkTime(String remarkTime) {
        this.remarkTime = remarkTime;
    }
}
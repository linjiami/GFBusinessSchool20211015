package com.gfbusinessschool.bean;

public class ClassifyFirstpageBean {
    private String iconName;
    private String iconUrl;
    private String type;
    private String id;
    private int newNoticeCount;//是否有消息提醒

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNewNoticeCount() {
        return newNoticeCount;
    }

    public void setNewNoticeCount(int newNoticeCount) {
        this.newNoticeCount = newNoticeCount;
    }
}
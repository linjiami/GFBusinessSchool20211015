package com.gfbusinessschool.bean;

public class AppVersionEntity {

    /**
     * id : 1410150352368906242
     * type : Android
     * newVersion : 1.0.1
     * newNum : 101
     * remark : 版本更新，bug修复
     * isMust : 0
     * createDate : 2021-06-30 16:16:00
     */

    private String id;
    private String type;
    private String newVersion;
    private int newNum;
    private String remark;
    private String isMust;
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
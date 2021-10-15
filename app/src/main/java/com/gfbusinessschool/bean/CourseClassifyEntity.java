package com.gfbusinessschool.bean;

import java.util.List;

public class CourseClassifyEntity {

    /**
     * id : 1407591134878683138
     * name : 线上运营
     * companyId : 1407585937804677121
     * companyName : null
     * status : 1
     * updateDate : 2021-06-29 13:45:04
     * createDate : 2021-06-23 14:47:32
     * parentId : 1407591038279667714
     * level : null
     */
    private String id;
    private String tagName;
    private String level;
    private String parentId;
    private String name;
    private String companyId;
    private String companyName;
    private String status;
    private String updateDate;
    private String createDate;
    private List<CourseClassifyEntity> childrenList;
    private boolean isMustStudy;//是否必修专题

    public CourseClassifyEntity(boolean isMustStudy,String name) {
        this.isMustStudy = isMustStudy;
        this.name = name;
    }

    public CourseClassifyEntity() {
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isMustStudy() {
        return isMustStudy;
    }

    public void setMustStudy(boolean mustStudy) {
        isMustStudy = mustStudy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<CourseClassifyEntity> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<CourseClassifyEntity> childrenList) {
        this.childrenList = childrenList;
    }
}
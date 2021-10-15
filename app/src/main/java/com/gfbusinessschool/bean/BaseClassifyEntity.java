package com.gfbusinessschool.bean;

import com.gfbusinessschool.utils.Utils;

import java.util.List;

public class BaseClassifyEntity {
    private String id;
    private String name;
    private String tagName;
    private String parentId;
    private List<BaseClassifyEntity> childrenList;
    private List<BaseClassifyEntity> list;
    private boolean isMustStudy;//是否必修专题

    public BaseClassifyEntity() {
    }

    public BaseClassifyEntity(String name, boolean isMustStudy, String id) {
        this.name = name;
        this.isMustStudy = isMustStudy;
        this.id = id;
    }

    public void setList(List<BaseClassifyEntity> list) {
        this.list = list;
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
        return Utils.isEmpty(name)?tagName:name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<BaseClassifyEntity> getChildrenList() {
        return childrenList==null?list:childrenList;
    }

    public void setChildrenList(List<BaseClassifyEntity> childrenList) {
        this.childrenList = childrenList;
    }
}
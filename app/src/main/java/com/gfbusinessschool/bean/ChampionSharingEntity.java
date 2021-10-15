package com.gfbusinessschool.bean;

import com.gfbusinessschool.utils.Utils;

import java.util.List;

public class ChampionSharingEntity {

    /**
     * id : 1
     * tagName : 冠军分享标签A
     * level : 1
     * parentId : 0
     * list : [{"id":"2","tagName":"冠军分享标签A-1","level":2,"parentId":1,"list":null},{"id":"3","tagName":"冠军分享标签A-2","level":2,"parentId":1,"list":null}]
     */

    private String id;
    private String tagName;
    private String name;
    private String level;
    private String parentId;
    private List<CourseClassifyEntity> list;
    private List<CourseClassifyEntity> childrenList;

    public void setChildrenList(List<CourseClassifyEntity> childrenList) {
        this.childrenList = childrenList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return Utils.isEmpty(tagName)?name:tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<CourseClassifyEntity> getList() {
        return list==null?childrenList:list;
    }

    public void setList(List<CourseClassifyEntity> list) {
        this.list = list;
    }
}
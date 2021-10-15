package com.gfbusinessschool.bean;

import java.util.List;

public class TestAnswerEntity {
    private String id;
    private String userKey;
    private String type;
    private List<String> questionSelectList;

    public List<String> getQuestionSelectList() {
        return questionSelectList;
    }

    public void setQuestionSelectList(List<String> questionSelectList) {
        this.questionSelectList = questionSelectList;
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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

}
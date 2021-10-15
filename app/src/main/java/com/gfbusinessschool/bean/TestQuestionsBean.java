package com.gfbusinessschool.bean;

import java.util.List;

public class TestQuestionsBean {

    /**
     * id : 3
     * name : 公司进行企业文化建设的作用3？
     * companyId : 1
     * companyName : null
     * type : 1
     * questionNum : 1/1
     * rightKey : A
     * questionSelectList : ["A:提高凝聚力","B:吸引员工","C:推广","D:提高业绩"]
     */

    private String id;
    private String name;
    private String companyId;
    private String companyName;
    private String type;
    private String questionNum;
    private String rightKey;
    private List<String> questionSelectList;
    private List<Boolean> checkList;

    public List<Boolean> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Boolean> checkList) {
        this.checkList = checkList;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(String questionNum) {
        this.questionNum = questionNum;
    }

    public String getRightKey() {
        return rightKey;
    }

    public void setRightKey(String rightKey) {
        this.rightKey = rightKey;
    }

    public List<String> getQuestionSelectList() {
        return questionSelectList;
    }

    public void setQuestionSelectList(List<String> questionSelectList) {
        this.questionSelectList = questionSelectList;
    }
}
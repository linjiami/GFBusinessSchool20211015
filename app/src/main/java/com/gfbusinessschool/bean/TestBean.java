package com.gfbusinessschool.bean;

import java.util.List;

public class TestBean {
    private String typeTest;
    private String titleTest;
    private List<String> selectList;

    public TestBean() {

    }

    public TestBean(String typeTest, String titleTest, List<String> selectList) {
        this.typeTest = typeTest;
        this.titleTest = titleTest;
        this.selectList = selectList;
    }

    public String getTypeTest() {
        return typeTest;
    }

    public void setTypeTest(String typeTest) {
        this.typeTest = typeTest;
    }

    public String getTitleTest() {
        return titleTest;
    }

    public void setTitleTest(String titleTest) {
        this.titleTest = titleTest;
    }

    public List<String> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<String> selectList) {
        this.selectList = selectList;
    }
}
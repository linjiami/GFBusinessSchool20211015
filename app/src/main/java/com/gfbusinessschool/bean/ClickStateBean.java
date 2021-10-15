package com.gfbusinessschool.bean;

public class ClickStateBean {

    private String content;
    private boolean isSelected;//是否被选中
    private boolean isSingleSelected =true;//单选还是多选,默认单选


    public ClickStateBean(String content, boolean isSelected) {
        this.content = content;
        this.isSelected = isSelected;
    }

    public ClickStateBean(String content, boolean isSelected, boolean isSingleSelected) {
        this.content = content;
        this.isSelected = isSelected;
        this.isSingleSelected = isSingleSelected;
    }

    public boolean isSingleSelected() {
        return isSingleSelected;
    }

    public void setSingleSelected(boolean singleSelected) {
        isSingleSelected = singleSelected;
    }

    public ClickStateBean(String content) {
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
package com.gfbusinessschool.bean;

import java.util.List;

public class SystemBasicBean {

    private List<String> education;
    private List<String> work_exp;
    private List<String> work_hours;
    private List<String> hot_words;

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getWork_exp() {
        return work_exp;
    }

    public void setWork_exp(List<String> work_exp) {
        this.work_exp = work_exp;
    }

    public List<String> getWork_hours() {
        return work_hours;
    }

    public void setWork_hours(List<String> work_hours) {
        this.work_hours = work_hours;
    }

    public List<String> getHot_words() {
        return hot_words;
    }

    public void setHot_words(List<String> hot_words) {
        this.hot_words = hot_words;
    }
}
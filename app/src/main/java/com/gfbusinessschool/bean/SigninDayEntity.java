package com.gfbusinessschool.bean;

public class SigninDayEntity {
    private String day;
    private boolean isSigin;

    public SigninDayEntity(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSigin() {
        return isSigin;
    }

    public void setSigin(boolean sigin) {
        isSigin = sigin;
    }
}
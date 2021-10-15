package com.gfbusinessschool.bean;

public class StudyTimeEntity {
    /**
     * studyLength : 0
     * totalStudy : 350
     * todayStudy : 0
     */

    private String studyLength;
    private String totalStudy;
    private String todayStudy;

    public String getStudyLength() {
        return studyLength;
    }

    public void setStudyLength(String studyLength) {
        this.studyLength = studyLength;
    }

    public String getTotalStudy() {
        return totalStudy;
    }

    public void setTotalStudy(String totalStudy) {
        this.totalStudy = totalStudy;
    }

    public String getTodayStudy() {
        return todayStudy;
    }

    public void setTodayStudy(String todayStudy) {
        this.todayStudy = todayStudy;
    }
}
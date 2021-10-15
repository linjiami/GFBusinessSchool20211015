package com.gfbusinessschool.bean;

public class ResearchEntity {

    /**
     * id : 1384787746994761702
     * name : 课程调查
     * introduction : 课程内容意见
     * companyId : 1
     * companyName : null
     * startDate : 2021-05-31 00:00:00
     * endDate : 2021-06-03 00:00:00
     * runStatus : 2
     * status : 1
     */

    private String id;
    private String name;
    private String introduction;
    private String companyId;
    private String companyName;
    private String startDate;
    private String endDate;
    private String runStatus;
    private String status;
    private String isJoin;


    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
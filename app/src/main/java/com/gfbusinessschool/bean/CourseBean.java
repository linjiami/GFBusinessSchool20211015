package com.gfbusinessschool.bean;

import java.util.List;

public class CourseBean {

    /**
     * id : 1
     * name : 店长课程精讲
     * title : 怎么快速成为店长
     * logoUrl : www.baidu.com
     * companyId : 1
     * companyName : null
     * testId : 1
     * testName : null
     * stuNum : 10
     * teacherId : 1
     * teacherName : 马月
     * introduction : 富文本课程介绍
     * isPass : null
     * sortNum : 1
     */

    private String id;
    private String name;
    private String title;
    private String logoUrl;
    private String companyId;
    private String companyName;
    private String testId;
    private String testName;
    private String stuNum;
    private String remarkNum;//评论次数
    private String teacherId;
    private String teacherName;
    private String headImgUrl;
    private String introduction;
    private String isPass;
    private String sortNum;
    private int type;
    private String classifyId;
    private String classifyName;
    private String coverImgUrl;
    private String status;
    private String createDate;
    private String firstTagId;
    private String secondTagId;
    private String content;//分享内容
    private String lookTimes;//浏览次数
    private int studyLength;//学习时间
    private String videoUrl;//视频地址
    private String personalIntroduction;
    private String isCollection;
    private String positionName;
    private String province;
    private String city;
    private String storeName;
    private String courseNum;
    private List<TeacherEntity> teacherEntityList;
    private String noPassNum;
    private String playPercent;

    public String getPlayPercent() {
        return playPercent;
    }

    public void setPlayPercent(String playPercent) {
        this.playPercent = playPercent;
    }

    public String getNoPassNum() {
        return noPassNum;
    }

    public void setNoPassNum(String noPassNum) {
        this.noPassNum = noPassNum;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public List<TeacherEntity> getTeacherEntityList() {
        return teacherEntityList;
    }

    public void setTeacherEntityList(List<TeacherEntity> teacherEntityList) {
        this.teacherEntityList = teacherEntityList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getPersonalIntroduction() {
        return personalIntroduction;
    }

    public void setPersonalIntroduction(String personalIntroduction) {
        this.personalIntroduction = personalIntroduction;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getStudyLength() {
        return studyLength;
    }

    public void setStudyLength(int studyLength) {
        this.studyLength = studyLength;
    }

    public String getLookTimes() {
        return lookTimes;
    }

    public void setLookTimes(String lookTimes) {
        this.lookTimes = lookTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFirstTagId() {
        return firstTagId;
    }

    public void setFirstTagId(String firstTagId) {
        this.firstTagId = firstTagId;
    }

    public String getSecondTagId() {
        return secondTagId;
    }

    public void setSecondTagId(String secondTagId) {
        this.secondTagId = secondTagId;
    }

    public String getRemarkNum() {
        return remarkNum;
    }

    public void setRemarkNum(String remarkNum) {
        this.remarkNum = remarkNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }
}
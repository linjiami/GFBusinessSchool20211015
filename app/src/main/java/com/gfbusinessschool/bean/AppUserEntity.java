package com.gfbusinessschool.bean;

public class AppUserEntity {

    /**
     * phone : null
     * headImgUrl : null
     * name : null
     * sex : null
     * age : null
     * storeId : 1
     * position : null
     * province : null
     * city : null
     * education : null
     * workExp : null
     * entryDate : null
     * isFirst : 1
     * integral : 0
     * companyName : 公司1
     * companyLogo : wwww.baidu.com
     */

    private String id;
    private String phone;
    private String headImgUrl;
    private String name;
    private String sex;
    private String age;
    private String storeId;
    private String storeName;
    private String positionId;
    private String positionName;
    private String province;
    private String city;
    private String cityId;
    private String education;
    private String workExp;
    private String workHours;
    private String entryDate;
    private String isFirst;
    private String integral;
    private String companyName;
    private String companyLogo;
    private String token;
    private String userType;
    private String jianjie;
    private String account;
    private String studySeconds;
    private String themeColour;
    private float passRate;
    private boolean isManagerLogin;

    public float getPassRate() {
        return passRate;
    }

    public void setPassRate(float passRate) {
        this.passRate = passRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isManagerLogin() {
        return isManagerLogin;
    }

    public void setManagerLogin(boolean managerLogin) {
        isManagerLogin = managerLogin;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStudySeconds() {
        return studySeconds;
    }

    public void setStudySeconds(String studySeconds) {
        this.studySeconds = studySeconds;
    }

    public String getThemeColour() {
        return themeColour;
    }

    public void setThemeColour(String themeColour) {
        this.themeColour = themeColour;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWorkExp() {
        return workExp;
    }

    public void setWorkExp(String workExp) {
        this.workExp = workExp;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    @Override
    public String toString() {
        return "AppUserEntity{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", positionId='" + positionId + '\'' +
                ", positionName='" + positionName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", cityId='" + cityId + '\'' +
                ", education='" + education + '\'' +
                ", workExp='" + workExp + '\'' +
                ", workHours='" + workHours + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", isFirst='" + isFirst + '\'' +
                ", integral='" + integral + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", token='" + token + '\'' +
                ", userType='" + userType + '\'' +
                ", jianjie='" + jianjie + '\'' +
                ", account='" + account + '\'' +
                ", studySeconds='" + studySeconds + '\'' +
                ", themeColour='" + themeColour + '\'' +
                ", isManagerLogin=" + isManagerLogin +
                '}';
    }
}

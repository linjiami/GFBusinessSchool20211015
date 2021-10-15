package com.gfbusinessschool.bean;

public class CertificateEntity {
    private String imgUrl;
    private String passDate;
    private String certDate;
    private String activeName;
    private String type;
    private String passRate;

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassDate() {
        return passDate;
    }

    public void setPassDate(String passDate) {
        this.passDate = passDate;
    }

    public String getCertDate() {
        return certDate;
    }

    public void setCertDate(String certDate) {
        this.certDate = certDate;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
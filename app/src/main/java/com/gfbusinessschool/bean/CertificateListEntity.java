package com.gfbusinessschool.bean;

import java.util.List;

public class CertificateListEntity {
    private CertificateEntity courseCert;
    private List<CertificateEntity> ryList;
    private List<String> tsList;

    public CertificateEntity getCourseCert() {
        return courseCert;
    }

    public void setCourseCert(CertificateEntity courseCert) {
        this.courseCert = courseCert;
    }

    public List<CertificateEntity> getRyList() {
        return ryList;
    }

    public void setRyList(List<CertificateEntity> ryList) {
        this.ryList = ryList;
    }

    public List<String> getTsList() {
        return tsList;
    }

    public void setTsList(List<String> tsList) {
        this.tsList = tsList;
    }
}
package com.gfbusinessschool.bean;

public class ClassifyEntity {

    /**
     * id : 1401776465872633858
     * categoryName : 水果
     * companyId : 1
     * status : 1
     * isDel : 1
     * createDate : 2021-06-07 13:41:00
     */

    private String id;
    private String categoryName;
    private String companyId;
    private String status;
    private String isDel;
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
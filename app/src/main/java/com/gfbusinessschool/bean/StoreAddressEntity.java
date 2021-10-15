package com.gfbusinessschool.bean;

import java.util.List;

public class StoreAddressEntity {

    /**
     * id : 1
     * addressName : 河北
     * parentId : 0
     * addressType : 1
     * companyId : 1
     * status : 1
     * isDelete : 1
     * createDate : 2021-03-11 10:25:40
     * cityList : [{"id":"2","addressName":"承德","parentId":1,"addressType":"2","companyId":1,"status":"1","isDelete":"1","createDate":"2021-03-11 10:25:41","cityList":null}]
     */

    private String id;
    private String addressName;
    private String parentId;
    private String addressType;
    private String companyId;
    private String status;
    private String isDelete;
    private String createDate;
    private List<StoreAddressEntity> cityList;
    private boolean isExpand;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<StoreAddressEntity> getCityList() {
        return cityList;
    }

    public void setCityList(List<StoreAddressEntity> cityList) {
        this.cityList = cityList;
    }
}
package com.gfbusinessschool.bean;

import java.util.List;

public class SiginEntity {

    /**
     * isFirstSign : 2
     * continueSignTimes : 2
     * signList : [{"id":"1402080720887705602","userId":"1115","phone":15600450012,"companyId":"1","signDate":"2021-06-08 09:51:07","status":"1","updateDate":"2021-06-08 09:51:07","createDate":"2021-06-08 09:51:07"},{"id":"1402454225927995393","userId":"1115","phone":15600450012,"companyId":"1","signDate":"2021-06-09 10:35:18","status":"1","updateDate":"2021-06-09 10:35:18","createDate":"2021-06-09 10:35:18"}]
     */

    private String isFirstSign;
    private String continueSignTimes;
    private String daySignIntegral;
    private String continueSignIntegral;
    private List<SignListBean> signList;

    public String getDaySignIntegral() {
        return daySignIntegral;
    }

    public void setDaySignIntegral(String daySignIntegral) {
        this.daySignIntegral = daySignIntegral;
    }

    public String getContinueSignIntegral() {
        return continueSignIntegral;
    }

    public void setContinueSignIntegral(String continueSignIntegral) {
        this.continueSignIntegral = continueSignIntegral;
    }

    public String getIsFirstSign() {
        return isFirstSign;
    }

    public void setIsFirstSign(String isFirstSign) {
        this.isFirstSign = isFirstSign;
    }

    public String getContinueSignTimes() {
        return continueSignTimes;
    }

    public void setContinueSignTimes(String continueSignTimes) {
        this.continueSignTimes = continueSignTimes;
    }

    public List<SignListBean> getSignList() {
        return signList;
    }

    public void setSignList(List<SignListBean> signList) {
        this.signList = signList;
    }

    public static class SignListBean {
        /**
         * id : 1402080720887705602
         * userId : 1115
         * phone : 15600450012
         * companyId : 1
         * signDate : 2021-06-08 09:51:07
         * status : 1
         * updateDate : 2021-06-08 09:51:07
         * createDate : 2021-06-08 09:51:07
         */

        private String id;
        private String userId;
        private String phone;
        private String companyId;
        private String signDate;
        private String status;
        private String updateDate;
        private String createDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getSignDate() {
            return signDate;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
package com.gfbusinessschool.bean;

import java.util.List;

public class ReadCollectionEntity {

    /**
     * records : [{"id":"1356886385794461698","title":"直播标题1","imgUrl":"http://test.imcfc.com/20210126/6e63d69035474f37a7bf8351fdfa42db.png","broadcastUrl":"http://test.imcfc.com/20210126/6e63d69035474f37a7bf8351fdfa42db.png","teacherId":"1","startDate":"2021-03-01 17:59:15","endDate":"2021-03-02 17:59:21","createDate":"2021-02-15 10:00:00","companyId":1,"status":"1","teacherName":"马月"},{"id":"1357524924789923842","title":"直播标题3","imgUrl":"http://test.imcfc.com/20210126/6e63d69035474f37a7bf8351fdfa42db.png","broadcastUrl":"http://test.imcfc.com/20210126/6e63d69035474f37a7bf8351fdfa42db.png","teacherId":"1","startDate":"2021-03-01 18:00:00","endDate":"2021-03-02 18:00:04","createDate":"2021-02-15 10:00:00","companyId":1,"status":"1","teacherName":"马月"}]
     * total : 2
     * size : 15
     * current : 1
     * orders : []
     * searchCount : true
     * pages : 1
     */

    private String total;
    private String size;
    private String current;
    private String searchCount;
    private String pages;
    private List<Entity> records;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public List<Entity> getRecords() {
        return records;
    }

    public void setRecords(List<Entity> records) {
        this.records = records;
    }

    public static class Entity{
        /**
         * id : 1397474546146033666
         * userId : 1115
         * headImgUrl : http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg
         * userName : 林家密
         * title : 截屏视频测试
         * content : 急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急
         * type : null
         * powerIds : null
         * fileName : null
         * fileUrl : http://test.imcfc.com/156004500121622018782811.mp4
         * status : 0
         * studyAmount : 0
         * likeAmount : 0
         * createDate : 2021-05-26 16:47:50
         * companyId : 1
         */

        private String id;
        private String userId;
        private String headImgUrl;
        private String userName;
        private String title;
        private String content;
        private String type;
        private String powerIds;
        private String fileName;
        private String fileUrl;
        private String status;
        private int studyAmount;
        private int likeAmount;
        private String createDate;
        private String companyId;
        private String isCollection;
        private float passRate;

        public float getPassRate() {
            return passRate;
        }

        public void setPassRate(float passRate) {
            this.passRate = passRate;
        }

        public String getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(String isCollection) {
            this.isCollection = isCollection;
        }

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

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPowerIds() {
            return powerIds;
        }

        public void setPowerIds(String powerIds) {
            this.powerIds = powerIds;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getStudyAmount() {
            return studyAmount;
        }

        public void setStudyAmount(int studyAmount) {
            this.studyAmount = studyAmount;
        }

        public int getLikeAmount() {
            return likeAmount;
        }

        public void setLikeAmount(int likeAmount) {
            this.likeAmount = likeAmount;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }
    }
}
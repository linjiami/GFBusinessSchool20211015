package com.gfbusinessschool.bean;

import java.util.List;

public class DataRecordsBean {

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
    private List<LiveBean> records;

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

    public List<LiveBean> getRecords() {
        return records;
    }

    public void setRecords(List<LiveBean> records) {
        this.records = records;
    }
}
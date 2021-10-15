package com.gfbusinessschool.bean;

import java.util.List;

public class ChampionShareListBean {

    /**
     * records : [{"id":"1371749295277957121","companyId":1,"storeId":1,"userId":1115,"title":"金牌店长说销售","name":"林先生","province":"河北","city":"承德","positionName":"销售","content":"金牌店长牛牛牛牛","videoUrl":"http://test.imcfc.com/20210316/73922e20c0704ad98e974fabf51ce0b2.mp4","coverImgUrl":"http://third.allgf.com.cn/files/default/2020/10-21/114518e509a9801154.png","status":"1","createDate":"2021-03-16 17:04:52","lookTimes":null,"firstTagId":1,"secondTagId":2,"storeName":null}]
     * total : 1
     * size : 15
     * current : 1
     * orders : []
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<CourseBean> records;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<CourseBean> getRecords() {
        return records;
    }

    public void setRecords(List<CourseBean> records) {
        this.records = records;
    }
}
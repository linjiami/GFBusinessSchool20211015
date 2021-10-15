package com.gfbusinessschool.bean;

import java.util.List;

public class PieChatEntity {
    private List<TableBean> list;
    private String title;

    public PieChatEntity() {
    }

    public PieChatEntity(List<TableBean> list, String title) {
        this.list = list;
        this.title = title;
    }

    public List<TableBean> getList() {
        return list;
    }

    public void setList(List<TableBean> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class TableBean{
        private int cnt;
        private String params;

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }
}
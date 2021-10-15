package com.gfbusinessschool.bean;

import java.util.List;

public class BaseDataArrayBean {

    /**
     * code : 10000
     * msg : success
     * data : [{"id":"699","defaultCourseId":"691","title":"作业及考试","taskid":"1394","courseId":"691","covers":"http://fourth.allgf.com.cn/files/default/2019/01-22/235246ecd37b582008.jpg","url":"http://fourth.allgf.com.cn/v2/mobile/course/691/task/1394/show"}]
     */

    private String code;
    private String msg;
    private List<Object> data;
    private String dataCode;
    private String time;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}

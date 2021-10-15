package com.gfbusinessschool.bean;

public class BaseDataStringBean {
    /**
     * code : 10000
     * msg : success
     * data : 0
     */

    private String code;
    private String msg;
    private String data;
    private String dataCode;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
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

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BaseDataStringBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

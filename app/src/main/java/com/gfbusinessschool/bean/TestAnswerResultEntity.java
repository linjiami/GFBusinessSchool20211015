package com.gfbusinessschool.bean;

public class TestAnswerResultEntity {


    /**
     * msg : 未通过
     * code : 201
     * errorNum : 2
     * rightNum : 1
     */

    private String msg;
    private String code;
    private String errorNum;
    private String rightNum;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum;
    }

    public String getRightNum() {
        return rightNum;
    }

    public void setRightNum(String rightNum) {
        this.rightNum = rightNum;
    }
}
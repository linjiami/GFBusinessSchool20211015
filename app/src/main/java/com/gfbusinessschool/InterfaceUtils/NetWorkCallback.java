package com.gfbusinessschool.InterfaceUtils;

public abstract class NetWorkCallback {

    /**
     * 接口请求失败
     */
    public void onRequestError(){

    }

    public void onResponse(String code, String response){

    }

    public void inProgress(float progress ){

    }
}

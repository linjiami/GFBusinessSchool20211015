package com.gfbusinessschool.InterfaceUtils;

import com.gfbusinessschool.bean.PositionEntity;
import com.gfbusinessschool.bean.SystemBasicBean;

import java.util.List;

public abstract class ResultCallBack {

    public void onResult(boolean isSuccess){

    }

    public void onResult(String result){

    }
    public void onResult(String str1,String str2){

    }

    /**
     * 刷新完成
     */
    public void onRefreshFinish(){

    }

    public void onResult(SystemBasicBean bean){

    }

    public void onResult(List<PositionEntity> list){

    }
}
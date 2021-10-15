package com.gfbusinessschool.utils;

/**
 * Created by lenovo on 2018/6/1.
 */

public class MenegerEvent {
    public static final String TYPE_AUDIO_FINISH ="关闭音频界面";
    public static final String TYPE_AUDIO_CLOSE_FLOATWIN ="关闭音频浮窗";
    public static final String TYPE_GET_CONTACTINFORMATION_WX_SUCCESS = "获取联系方式微信支付成功";

    //定义消息事件类
    public String typeEvent;//消息类型
    public String mMsg;//消息内容
    public int progressCurrentVod;//回放进度
    private boolean isPortrait;//是否是竖屏

    public String name1;

    public MenegerEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }

    public MenegerEvent(String typeEvent, String msg) {
        this.typeEvent = typeEvent;
       this.mMsg = msg;
    }

    public int getProgressCurrentVod() {
        return progressCurrentVod;
    }

    public void setProgressCurrentVod(int progressCurrentVod) {
        this.progressCurrentVod = progressCurrentVod;
    }

    public void setPortrait(boolean portrait) {
        isPortrait = portrait;
    }

    public boolean isPortrait() {
        return isPortrait;
    }
}

package com.gfbusinessschool.InterfaceUtils;

public abstract class RequestPermissionCallBack {
    /**
     * 同意授权
     */
    public void granted(){};

    /**
     * 取消授权
     */
    public void denied(){};

    /**
     * 拒绝授权（引导用户去设置界面开启）
     */
    public void refused(){};
}

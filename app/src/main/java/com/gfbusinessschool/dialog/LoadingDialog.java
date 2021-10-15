package com.gfbusinessschool.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.gfbusinessschool.R;
import com.gfbusinessschool.utils.Utils;

public class LoadingDialog extends AlertDialog {
    private String alertMsg;
    private TextView tv_message;
    private boolean mIsCancelable =true;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setmIsCancelable(boolean mIsCancelable) {
        this.mIsCancelable = mIsCancelable;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
        if (tv_message!=null && !Utils.isEmpty(alertMsg))  tv_message.setText(alertMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        if (mIsCancelable){
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }else {
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
        setContentView(R.layout.loading);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) getContext().getResources().getDimension(R.dimen.px264);
        params.height = (int) getContext().getResources().getDimension(R.dimen.px264);
        getWindow().setAttributes(params);
        tv_message = findViewById(R.id.tv_message);
        if (!Utils.isEmpty(alertMsg)){
            tv_message.setText(alertMsg);
        }
    }
    @Override
    public void show() {//开启
        super.show();
    }
    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }
}

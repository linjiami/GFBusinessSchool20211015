package com.gfbusinessschool.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gfbusinessschool.InterfaceUtils.ResultCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.utils.Utils;

public class TimePickerPopWindow {
    private PopupWindow mPopupWindow;
    private Activity mContext;
    private ResultCallBack resultCallBack;
    private boolean isOutsideTouchable =true;
    private String timeSelect ="00:00";

    public TimePickerPopWindow(Activity mContext, ResultCallBack resultCallBack) {
        this.mContext = mContext;
        this.resultCallBack = resultCallBack;
    }

    public void setOutsideTouchable(boolean outsideTouchable) {
        isOutsideTouchable = outsideTouchable;
    }

    public void showPopWindow(){
        if (mPopupWindow ==null){
            View inflate =LayoutInflater.from(mContext).inflate(R.layout.pop_window_time_picker,null,false);
            int width = (int) (Utils.getScreenWidth(mContext)-mContext.getResources().getDimension(R.dimen.px100)*2);
            mPopupWindow = new PopupWindow(inflate,
                    width, ViewGroup.LayoutParams.WRAP_CONTENT, false);

            TimePicker timePicker =inflate.findViewById(R.id.timePicker);
            TextView tvConfirm =inflate.findViewById(R.id.tvConfirm);
            tvConfirm.setOnClickListener(v -> {
                dismissPopWindow();
                if (resultCallBack!=null) {
                    //时间格式00:00
                    int hour =timePicker.getCurrentHour();
                    String hourSelected =""+hour;
                    if (hour<10)
                        hourSelected ="0"+hour;

                    int minute =timePicker.getCurrentMinute();
                    String minuteSelected =""+minute;
                    if (minute<10)
                        minuteSelected ="0"+minute;
                    resultCallBack.onResult(hourSelected+":"+minuteSelected);
                }
            });
            timePicker.setIs24HourView(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(isOutsideTouchable);
            mPopupWindow.setOnDismissListener(() -> {
                disappearPop();
            });
        }
        if (mPopupWindow!=null && !mPopupWindow.isShowing()){
            showPop();
            mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    public void dismissPopWindow(){
        if (mPopupWindow!=null && mPopupWindow.isShowing()){
            //popupWindow消失屏幕变为不透明
            mPopupWindow.dismiss();
        }
    }

    private void disappearPop(){
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mContext.getWindow().setAttributes(lp);
    }

    private void showPop(){
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mContext.getWindow().setAttributes(lp);
    }
}

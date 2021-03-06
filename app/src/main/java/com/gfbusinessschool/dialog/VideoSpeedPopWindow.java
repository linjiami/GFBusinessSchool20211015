package com.gfbusinessschool.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;

public class VideoSpeedPopWindow extends PopupWindow implements View.OnClickListener {
    private OnClickCallBack onItemClickListener;
    private Activity activity;
    private View popupWindow;

    public VideoSpeedPopWindow(Activity context, OnClickCallBack _onItemClickListener) {
        super(context);
        this.activity = context;
        this.onItemClickListener = _onItemClickListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.video_speed_selector, null);//alt+ctrl+f
        this.setContentView(popupWindow);

        initView(popupWindow);
        initPopWindow();

    }

    public int getPopWindowWidth(){
        popupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return popupWindow.getMeasuredWidth();
    }

    public int getPopWindowHeigth(){
        popupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return popupWindow.getMeasuredHeight();
    }


    private void initView(View popView) {
        popView.findViewById(R.id.speed100).setOnClickListener(this);
        popView.findViewById(R.id.speed125).setOnClickListener(this);
        popView.findViewById(R.id.speed150).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speed100:
                if (onItemClickListener!=null) onItemClickListener.onClick(0);
                break;
            case R.id.speed125:
                if (onItemClickListener!=null) onItemClickListener.onClick(1);
                break;
            case R.id.speed150:
                if (onItemClickListener!=null) onItemClickListener.onClick(2);
                break;
        }
    }

    private void initPopWindow() {
        // ????????????????????????
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ????????????????????????
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ???????????????????????????()
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        //??????SelectPicPopupWindow????????????????????????
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // ???????????????ColorDrawable??????????????????
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //???????????????????????????
        this.setBackgroundDrawable(dw);
        backgroundAlpha(activity, 0.5f);//0.0-1.0
    }

    /**
     * ????????????????????????????????????(?????????,???????????????)
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(final Activity context, float bgAlpha) {
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);

        /**
         * ??????popupWindow??????????????????
         */
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                context.getWindow().setAttributes(lp);
                dismiss();
            }
        });
    }
}

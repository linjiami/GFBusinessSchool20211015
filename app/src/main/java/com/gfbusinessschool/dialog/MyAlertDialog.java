package com.gfbusinessschool.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.databinding.PopwindowMyBinding;
import com.gfbusinessschool.utils.Utils;

public class MyAlertDialog extends AlertDialog {
    private Activity mContext;
    private MyDialogCallback myDialogCallback;
    PopwindowMyBinding binding;
    private String title;
    private CharSequence msg;
    private SpannableStringBuilder stringBuilder;
    private String positiveText;
    private String nevativigateText;
    private boolean isNegativeButtonGone;
    private boolean showSeekBar;

    public MyAlertDialog(Activity activity, MyDialogCallback myDialogCallback) {
        super(activity);
        this.mContext =activity;
        this.myDialogCallback =myDialogCallback;
    }

    public MyAlertDialog(Activity activity) {
        super(activity);
        this.mContext =activity;
    }

    public void setMyDialogCallback(MyDialogCallback myDialogCallback) {
        this.myDialogCallback = myDialogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        binding =PopwindowMyBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());//loading的xml文件
        int screenWidth =Math.min(MyApplication.getInstance().screenWidth,MyApplication.getInstance().screenHeight);
        int width = (int) (screenWidth-mContext.getResources().getDimension(R.dimen.px100));
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = width;//宽高可设置具体大小
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小
        getWindow().setAttributes(lp);

        if (showSeekBar) binding.seekBarLayout.setVisibility(View.VISIBLE);
        else binding.seekBarLayout.setVisibility(View.GONE);
        binding.layoutMyCustom.setBackgroundColor(Utils.getThemeColor(mContext));
        binding.tvCancle.setTextColor(Utils.getThemeColor(mContext));
        Utils.setBackgroundStoken(mContext,binding.tvCancle);
        Utils.setBackgroundSolid(mContext,binding.tvConfirm);

        if (isNegativeButtonGone) binding.tvCancleLayout.setVisibility(View.GONE);
        if (!Utils.isEmpty(positiveText)) binding.tvConfirm.setText(positiveText);
        if (!Utils.isEmpty(nevativigateText)) binding.tvCancle.setText(nevativigateText);
        if (msg!=null) binding.msgPopwindow.setText(msg);
        if (Utils.isEmpty(title))
            binding.titlePopWindow.setVisibility(View.GONE);
        else
            binding.titlePopWindow.setText(title);
        if (stringBuilder!=null){
            binding.msgPopwindow.setText(stringBuilder);
            binding.msgPopwindow.setMovementMethod(LinkMovementMethod.getInstance());
        }
        binding.tvConfirm .setOnClickListener(v -> {
            if (myDialogCallback!=null) {
                myDialogCallback.onPositiveClick(this);
            }
        });
        binding.tvCancle .setOnClickListener(v -> {
            dismiss();
            if (myDialogCallback!=null) myDialogCallback.onNegativeClick(this);
        });
    }
    @Override
    public void show() {//开启
        super.show();
    }
    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        this.msg = message;
        if (binding!=null) binding.msgPopwindow.setText(message);
    }

    public void setMessage(SpannableStringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
        if (binding!=null) {
            binding.msgPopwindow.setText(stringBuilder);
            binding.msgPopwindow.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public TextView getMessageView(){
        return binding!=null?binding.msgPopwindow: null;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        if (binding!=null) binding.tvConfirm.setText(positiveText);
    }

    public void setNevativigateText(String nevativigateText) {
        this.nevativigateText = nevativigateText;
        if (binding!=null) binding.tvCancle.setText(nevativigateText);
    }

    public void setNegativeButtonGone() {
        isNegativeButtonGone = true;
    }

    public void setShowSeekBar(boolean showSeekBar) {
        this.showSeekBar = showSeekBar;
    }

    public void setSeekBarProgress(int seekBarProgress) {
        if (binding!=null) {
            binding.seekBar.setProgress(seekBarProgress);
            binding.currentProgress.setText(seekBarProgress+"%");
        }
    }

    public void setPositivateClickAble(boolean isClickAble){
        if (binding!=null) binding.tvConfirm.setClickable(isClickAble);
    }
}

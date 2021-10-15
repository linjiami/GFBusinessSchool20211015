package com.gfbusinessschool.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.utils.Utils;

public class TitleBar extends ConstraintLayout implements View.OnClickListener {
    private Context mContext;
    protected View back, right,line_titlebar;
    private TextView rightTv, title,tvBack;
    private ImageView rightIconTitleBar,iconBackTitleBar;

    public TitleBar(@NonNull Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initView();
    }

    public void setRightText(String rightMsg) {
        rightTv.setText(rightMsg);
    }

    public View getBackLayout(){
        return back;
    }
    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getTitle(){
        return  title.getText().toString();
    }

    public TextView getRightTv(){
        return rightTv;
    }

    public ImageView getRightIconTitleBar(){
        return rightIconTitleBar;
    }

    public void setRightIconTitleBar(int resourdId){
        rightIconTitleBar.setVisibility(VISIBLE);
        rightTv.setVisibility(GONE);
        rightIconTitleBar.setImageResource(resourdId);
    }
    public View getRigthLayout(){
        return right;
    }

    private void initView() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.activity_navigationbar, this);
        back = inflate.findViewById(R.id.backTitleBar);
        title = inflate.findViewById(R.id.titleTitleBar);
        rightTv = inflate.findViewById(R.id.rightTvTitleBar);
        right = inflate.findViewById(R.id.rightTitleBar);
        tvBack = inflate.findViewById(R.id.tvBackTitleBar);
        rightIconTitleBar = inflate.findViewById(R.id.rightIconTitleBar);
        line_titlebar = inflate.findViewById(R.id.line_titlebar);
        iconBackTitleBar = inflate.findViewById(R.id.iconBackTitleBar);

        int color = Color.parseColor("#d1a76f");
        if (MyApplication.getInstance()!=null && MyApplication.getInstance().getAppUserEntity()!=null &&
                !Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getThemeColour())){
            color =Color.parseColor(MyApplication.getInstance().getAppUserEntity().getThemeColour());
        }
        Drawable up =iconBackTitleBar.getDrawable();
        Drawable drawable= DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawable, color);
        iconBackTitleBar.setImageDrawable(drawable);
        rightTv.setTextColor(color);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backTitleBar:
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
                break;
        }
    }

    public void setBackTextViewVisiable(int state){
        tvBack.setVisibility(state);
    }
    public void setLineBottomGone(){
        line_titlebar.setVisibility(GONE);
    }

}

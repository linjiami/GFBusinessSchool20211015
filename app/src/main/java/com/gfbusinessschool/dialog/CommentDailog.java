package com.gfbusinessschool.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ComponentActivity;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.view.CustomEditText;

public class CommentDailog extends AlertDialog implements View.OnClickListener {
    private ComponentActivity mContext;
    private ImageView rating1,rating2,rating3,rating4,rating5;
    private TextView tvRatingLevel;
    private int commentRating =5;//几级评分
    private OnClickCallBack mOnClickCallBack;
    private CustomEditText customEditText;

    public CommentDailog(@NonNull ComponentActivity context, OnClickCallBack onClickCallBack) {
        super(context);
        this.mContext =context;
        this.mOnClickCallBack =onClickCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        initView();
    }

    private void initView() {
        findViewById(R.id.fp_bc).setOnClickListener(this);
        findViewById(R.id.cancleComment).setOnClickListener(this);
        tvRatingLevel =findViewById(R.id.tvRatingLevel);
        rating1 =findViewById(R.id.rating1);
        rating2 =findViewById(R.id.rating2);
        rating3 =findViewById(R.id.rating3);
        rating4 =findViewById(R.id.rating4);
        rating5 =findViewById(R.id.rating5);
        rating1.setOnClickListener(this);
        rating2.setOnClickListener(this);
        rating3.setOnClickListener(this);
        rating4.setOnClickListener(this);
        rating5.setOnClickListener(this);
        customEditText =findViewById(R.id.fp_edtext1);
        // 接着清除flags
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // 然后弹出输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancleComment:
                dismiss();
                break;
            case R.id.rating1:
                commentRating = 1;
                setCommentRating();
                tvRatingLevel.setText(mContext.getString(R.string.comment_level1));
                break;
            case R.id.rating2:
                commentRating =2;
                setCommentRating();
                tvRatingLevel.setText(mContext.getString(R.string.comment_level2));
                break;
            case R.id.rating3:
                commentRating =3;
                setCommentRating();
                tvRatingLevel.setText(mContext.getString(R.string.comment_level3));
                break;
            case R.id.rating4:
                commentRating =4;
                setCommentRating();
                tvRatingLevel.setText(mContext.getString(R.string.comment_level4));
                break;
            case R.id.rating5:
                commentRating =5;
                setCommentRating();
                tvRatingLevel.setText(mContext.getString(R.string.comment_recommend));
                break;
            case R.id.fp_bc:
                String commentMsg= customEditText.getText().toString();
                if (commentMsg.trim().isEmpty()) {
                    ToastUtil.showToast(mContext,mContext.getString(R.string.null_comment_alert_msg));
                    return;
                }
                if (mOnClickCallBack!=null) mOnClickCallBack.onClickComment(commentRating,commentMsg);
                dismiss();
                break;
        }
    }

    private void setCommentRating() {
        switch (commentRating){
            case 1:
                rating1.setImageResource(R.mipmap.xing1);
                rating2.setImageResource(R.mipmap.mo_ren_xing);
                rating3.setImageResource(R.mipmap.mo_ren_xing);
                rating4.setImageResource(R.mipmap.mo_ren_xing);
                rating5.setImageResource(R.mipmap.mo_ren_xing);
                break;
            case 2:
                rating1.setImageResource(R.mipmap.xing1);
                rating2.setImageResource(R.mipmap.xing1);
                rating3.setImageResource(R.mipmap.mo_ren_xing);
                rating4.setImageResource(R.mipmap.mo_ren_xing);
                rating5.setImageResource(R.mipmap.mo_ren_xing);
                break;
            case 3:
                rating1.setImageResource(R.mipmap.xing1);
                rating2.setImageResource(R.mipmap.xing1);
                rating3.setImageResource(R.mipmap.xing1);
                rating4.setImageResource(R.mipmap.mo_ren_xing);
                rating5.setImageResource(R.mipmap.mo_ren_xing);
                break;
            case 4:
                rating1.setImageResource(R.mipmap.xing1);
                rating2.setImageResource(R.mipmap.xing1);
                rating3.setImageResource(R.mipmap.xing1);
                rating4.setImageResource(R.mipmap.xing1);
                rating5.setImageResource(R.mipmap.mo_ren_xing);
                break;
            case 5:
                rating1.setImageResource(R.mipmap.xing1);
                rating2.setImageResource(R.mipmap.xing1);
                rating3.setImageResource(R.mipmap.xing1);
                rating4.setImageResource(R.mipmap.xing1);
                rating5.setImageResource(R.mipmap.xing1);
                break;
        }
    }
}

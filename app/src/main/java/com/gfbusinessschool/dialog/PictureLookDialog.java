package com.gfbusinessschool.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ComponentActivity;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.databinding.ActivityCoursedetailsBinding;
import com.gfbusinessschool.databinding.DialogLookpictureBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.gfbusinessschool.view.CustomEditText;

public class PictureLookDialog extends Dialog {
    public static final String TYPE_CHAMPIONSHARE ="冠军分享/封面模板";
    public static final String TYPE_READCOLLECTION ="读书汇分享的图片";
    private String type =TYPE_CHAMPIONSHARE;
    private Activity mContext;
    private String urlImg;
    private DialogLookpictureBinding viewBinding;

    public PictureLookDialog(@NonNull Activity context, String urlImg) {
        super(context,R.style.dialogPicture);
        this.mContext =context;
        this.urlImg = urlImg;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DialogLookpictureBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        setCanceledOnTouchOutside(true);
        if (Utils.getString(type).equals(TYPE_CHAMPIONSHARE)){
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            double width = MyApplication.getInstance().screenWidth*19.0/20;
            lp.width = (int) (width);//宽高可设置具体大小
            lp.height = (int) (width*9/16);//宽高可设置具体大小
            getWindow().setAttributes(lp);
        }else if (Utils.getString(type).equals(TYPE_READCOLLECTION)){
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            double width = MyApplication.getInstance().screenWidth*19.0/20;
            lp.width = (int) (width);//宽高可设置具体大小
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小
            getWindow().setAttributes(lp);
            viewBinding.lookImg.setAdjustViewBounds(true);
        }

        GlideLoadUtils.load(mContext,urlImg,viewBinding.lookImg,GlideLoadUtils.TYPE_PLACE_HOLDER_MAXSOUCE);
        viewBinding.lookImg.setOnClickListener(v -> {
            dismiss();
        });
    }
}

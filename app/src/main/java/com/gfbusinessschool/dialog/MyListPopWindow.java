package com.gfbusinessschool.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ListPopWindowAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyListPopWindow extends PopupWindow {
    private final View view;
    private Activity context;
    private OnClickCallBack mOnClickCallBack;
    private List<String> mList =new ArrayList<>();

    public MyListPopWindow(Activity context, List<String> mList) {
        super(context);
        this.context = context;
        this.mList = mList;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popwindow_list, null);//alt+ctrl+f
        initPopWindow();
    }

    public void showPopWindow(View view){
        showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        initView();
    }

    public void setmOnClickCallBack(OnClickCallBack mOnClickCallBack) {
        this.mOnClickCallBack = mOnClickCallBack;
    }

    private void initView() {
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager =new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(new ListPopWindowAdapter(context,mList,mOnClickCallBack));
        view.findViewById(R.id.tvCancle).setOnClickListener(v -> {
            dismiss();
        });
        this.setOnDismissListener(() -> {
            backgroundAlpha(context, 1f);
        });
    }

    private void initPopWindow() {
        this.setContentView(view);
        // ????????????????????????
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // ????????????????????????
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ???????????????????????????()
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //??????SelectPicPopupWindow????????????????????????
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // ???????????????ColorDrawable??????????????????
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //???????????????????????????
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
    }

    /**
     * ????????????????????????????????????(?????????,???????????????)
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}

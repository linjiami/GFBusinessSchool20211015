package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.ItemCourseGridBinding;
import com.gfbusinessschool.databinding.ItemCourseHorizontalBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）
 */
public class CourseHorizontalAdapter extends BassRecyclerAdapter {
    private List<CourseBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public CourseHorizontalAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<CourseBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodler(ItemCourseHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            CourseBean bean =mList.get(position);
            if (bean==null) return;
            myHolder.layoutCourseHor.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            myHolder.titleCourseHor.setText(Utils.getString(bean.getName()));
            GlideLoadUtils.load(mContext,bean.getLogoUrl(),myHolder.coverCourseHor,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private View layoutCourseHor;
        private ImageView coverCourseHor;
        private TextView titleCourseHor;

        public MyViewHodler(@NonNull ItemCourseHorizontalBinding binding) {
            super(binding.getRoot());
            layoutCourseHor = binding.layoutCourseHor;
            coverCourseHor = binding.coverCourseHor;
            titleCourseHor = binding.titleCourseHor;
        }
    }

}

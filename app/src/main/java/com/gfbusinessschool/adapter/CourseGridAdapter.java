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
import com.gfbusinessschool.bean.ClassifyFirstpageBean;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.ItemClassifyFirstpageBinding;
import com.gfbusinessschool.databinding.ItemCourseGridBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）
 */
public class CourseGridAdapter extends BassRecyclerAdapter {
    private List<CourseBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;
    private int positionClick;

    public CourseGridAdapter(Context mContext, OnClickCallBack onClickCallBack) {
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
        return new MyViewHodler(ItemCourseGridBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            CourseBean bean =mList.get(position);
            if (bean==null) return;
            myHolder.layoutCourseGrid.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            myHolder.titleCourseGrid.setText(Utils.getString(bean.getName()));
            myHolder.countCourseGrid.setText(String.format(mContext.getString(R.string.count_study),Utils.getString(bean.getStuNum())));
            myHolder.teacherFlagCourseGrid.setText(Utils.getString(bean.getTeacherName()));
            GlideLoadUtils.load(mContext,bean.getLogoUrl(),myHolder.coverCourseGrid,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private View layoutCourseGrid;
        private ImageView coverCourseGrid;
        private TextView titleCourseGrid,countCourseGrid,teacherFlagCourseGrid;

        public MyViewHodler(@NonNull ItemCourseGridBinding binding) {
            super(binding.getRoot());
            layoutCourseGrid = binding.layoutCourseGrid;
            coverCourseGrid = binding.coverCourseGrid;
            titleCourseGrid = binding.titleCourseGrid;
            countCourseGrid = binding.countCourseGrid;
            teacherFlagCourseGrid = binding.teacherFlagCourseGrid;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) coverCourseGrid.getLayoutParams();
            double width =(MyApplication.getInstance().screenWidth -mContext.getResources().getDimension(R.dimen.px52))*1.0/2;
            params.height = (int) (width*9/16);
            coverCourseGrid.setLayoutParams(params);
        }
    }

}

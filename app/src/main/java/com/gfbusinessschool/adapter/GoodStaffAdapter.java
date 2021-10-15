package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.GoodStaffBean;
import com.gfbusinessschool.databinding.ItemFinepersonalBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）
 */
public class GoodStaffAdapter extends BassRecyclerAdapter {
    private List<GoodStaffBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public GoodStaffAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<GoodStaffBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodler(ItemFinepersonalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            GoodStaffBean bean =mList.get(position);
            if (bean==null) return;
            myHolder.layoutFinePersonal.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            myHolder.nameFinePersonal.setText(Utils.getString(bean.getName()));
            myHolder.teacherFlagFinePersonal.setText(Utils.getString(bean.getStoreName()));
            myHolder.pointFinePersonal.setText(String.format(mContext.getResources().getString(R.string.place_integral_count),Utils.getString(bean.getIntegral())));
            GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.headFinePersonal,R.mipmap.recruit_user_icon);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private View layoutFinePersonal;
        private ImageView headFinePersonal;
        private TextView pointFinePersonal,nameFinePersonal,teacherFlagFinePersonal;

        public MyViewHodler(@NonNull ItemFinepersonalBinding binding) {
            super(binding.getRoot());
            layoutFinePersonal = binding.layoutFinePersonal;
            headFinePersonal = binding.headFinePersonal;
            pointFinePersonal = binding.pointFinePersonal;
            nameFinePersonal = binding.nameFinePersonal;
            teacherFlagFinePersonal = binding.teacherFlagFinePersonal;
        }
    }

}

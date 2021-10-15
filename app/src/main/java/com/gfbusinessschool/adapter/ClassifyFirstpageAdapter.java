package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.ClassifyFirstpageBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemClassifyFirstpageBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页分类（课程中心、冠军分享等分类）
 */
public class ClassifyFirstpageAdapter extends BassRecyclerAdapter {
    private List<ClassifyFirstpageBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public ClassifyFirstpageAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<ClassifyFirstpageBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodler(ItemClassifyFirstpageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            ClassifyFirstpageBean bean =mList.get(position);
            if (bean==null) return;
            if (bean.getNewNoticeCount()!=0){
                myHolder.binding.msgCount.setVisibility(View.VISIBLE);
                myHolder.binding.msgCount.setText(bean.getNewNoticeCount()+"");
            }else {
                myHolder.binding.msgCount.setVisibility(View.GONE);
            }
            myHolder.binding.layoutClassifyFP.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            myHolder.binding.titleClassifyFP.setText(Utils.getString(bean.getIconName()));
            GlideLoadUtils.load(mContext,bean.getIconUrl(),myHolder.binding.iconClassifyFP,R.mipmap.guanjun);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        ItemClassifyFirstpageBinding binding;

        public MyViewHodler(@NonNull ItemClassifyFirstpageBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
            if (mList!=null && mList.size()>0 && mList.size()<=8){
                ViewGroup.LayoutParams params = binding.layoutClassifyFP.getLayoutParams();
                params.width =ViewGroup.LayoutParams.MATCH_PARENT;
                params.height =ViewGroup.LayoutParams.WRAP_CONTENT;
                binding.layoutClassifyFP.setLayoutParams(params);
                binding.layoutClassifyFP.setPadding(0, (int) mContext.getResources().getDimension(R.dimen.px27),0, (int) mContext.getResources().getDimension(R.dimen.px27));
            }else if (mList!=null && mList.size()>8){
                ViewGroup.LayoutParams params =binding.layoutClassifyFP.getLayoutParams();
                params.width = (int) ((MyApplication.getInstance().screenWidth-mContext.getResources().getDimension(R.dimen.left_app)*2
                        -mContext.getResources().getDimension(R.dimen.px10)*2)/4);
                params.height =ViewGroup.LayoutParams.WRAP_CONTENT;
                binding.layoutClassifyFP.setLayoutParams(params);
                binding.layoutClassifyFP.setPadding(0,
                        (int) mContext.getResources().getDimension(R.dimen.px27),
                        0,
                        (int) mContext.getResources().getDimension(R.dimen.px27));
            }
        }
    }

}

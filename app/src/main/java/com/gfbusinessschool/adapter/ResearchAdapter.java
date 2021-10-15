package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ResearchEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemResearchBinding;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 问卷调查
 */
public class ResearchAdapter extends BassRecyclerAdapter {
    private OnClickCallBack onClickCallBack;
    private List<ResearchEntity> mList =new ArrayList<>();
    private boolean isJoinResearch;

    public ResearchAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setIsJoinResearch(boolean joinResearch) {
        isJoinResearch = joinResearch;
    }

    public void setmList(List<ResearchEntity> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)?0:mList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1)
            return FOOT_VIEW;
        else
            return NORMAL_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new ViewHolder(ItemResearchBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            ResearchEntity entity =mList.get(position);
            if (entity==null) return;
            holder.binding.title.setText(Utils.getString(entity.getName()));
            holder.binding.content.setText(Utils.getString(entity.getIntroduction()));
            holder.binding.time.setText(String.format(mContext.getString(R.string.place_start_end_time),
                    Utils.getTimeFormat(entity.getStartDate()),Utils.getTimeFormat(entity.getEndDate())));
            if (!isJoinResearch){
                //runStatus问券状态（1进行中2未开始3已结束）
                if (Utils.getString(entity.getRunStatus()).equals("2"))
                    holder.binding.researchState.setImageResource(R.mipmap.research_nostart);
                else if (Utils.getString(entity.getRunStatus()).equals("1"))
                    holder.binding.researchState.setImageResource(R.mipmap.research_starting);
                else
                    holder.binding.researchState.setImageResource(R.mipmap.research_stop);
                Utils.setImageViewTint(mContext,holder.binding.researchState);
            }
            holder.binding.researchLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        ItemResearchBinding binding;

        public ViewHolder(@NonNull ItemResearchBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
            if (isJoinResearch){
                binding.researchState.setVisibility(View.GONE);
                binding.joinedTV.setVisibility(View.VISIBLE);
                binding.joinedTV.setTextColor(Utils.getThemeColor(mContext));
            }
        }
    }
}
package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.IntegralBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemIntegralBinding;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分列表
 */
public class IntegralAdapter extends BassRecyclerAdapter {
    private List<IntegralBean> mList = new ArrayList<>();

    public IntegralAdapter(Context mContext) {
        super(mContext);
    }

    public void setmList(List<IntegralBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mList.size())
            return FOOT_VIEW;
        else
            return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
       else
            return new ViewHolder(ItemIntegralBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder) {
            ViewHolder myHolder = (ViewHolder) holder;
            IntegralBean entity = mList.get(position);
            if (entity == null) return;
            myHolder.timeIntegral.setText(Utils.getTimeFormat(entity.getCreateDate()));
            if (Utils.getString(entity.getType()).equals("4")){//购物
                myHolder.commentIntegral.setText(String.format(mContext.getResources().getString(R.string.place_integral_delete),
                        entity.getRemark(),entity.getIntegral()));
            }else {
                myHolder.commentIntegral.setText(String.format(mContext.getResources().getString(R.string.place_integral_add),
                        entity.getRemark(),entity.getIntegral()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeIntegral;
        TextView commentIntegral;

        ViewHolder(@NonNull ItemIntegralBinding binding) {
            super(binding.getRoot());
            timeIntegral =binding.timeIntegral;
            commentIntegral =binding.commentIntegral;
        }
    }
}

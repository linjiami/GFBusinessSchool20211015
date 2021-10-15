package com.gfbusinessschool.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.CommentBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.FragmentEvaluateBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yt on 2018-5-2.
 */

public class CommentAdapter extends BassRecyclerAdapter {
    List<CommentBean> mList = new ArrayList<>();

    public CommentAdapter(Context mContext) {
        super(mContext);
    }

    public void setList(List<CommentBean> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new MyViewHolder(FragmentEvaluateBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) _holder;
            CommentBean bean = mList.get(position);
            if (bean==null) return;
            GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),holder.binding.headImg,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            if (!Utils.isEmpty(bean.getRemarkTime())){
                String remarkTime =bean.getRemarkTime();
                if (remarkTime.length()>11){//证明后面有时分秒
                    StringBuffer s = new StringBuffer(remarkTime);
                    s.delete(remarkTime.length()-8,remarkTime.length());
                    holder.binding.timeComment.setText(s);
                }else {
                    holder.binding.timeComment.setText(Utils.getString(bean.getRemarkTime()));
                }
            }
            holder.binding.userNameComment.setText(Utils.getString(bean.getUserName()));
            holder.binding.comment.setText(Utils.getString(bean.getContent()));
            if (bean.getPassRate()==1)
                holder.binding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
            else
                holder.binding.biyexuanzhangIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mList.size())
            return FOOT_VIEW;
        else
            return NORMAL_VIEW;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        FragmentEvaluateBinding binding;

        public MyViewHolder(@NonNull FragmentEvaluateBinding binding) {
            super(binding.getRoot());
           this.binding =binding;
        }
    }
}

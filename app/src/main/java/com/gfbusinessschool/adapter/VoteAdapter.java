package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.VoteUserEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemVoteBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票
 */
public class VoteAdapter extends BassRecyclerAdapter{
    private OnClickCallBack onClickCallBack;
    private List<VoteUserEntity> list =new ArrayList<>();

    public VoteAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setList(List<VoteUserEntity> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==list.size())
            return FOOT_VIEW;
        else
            return NORMAL_VIEW;
    }

    @Override
    public int getItemCount() {
        return (list==null || list.size()==0)?0:list.size()+1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new ViewHolder(ItemVoteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            VoteUserEntity entity =list.get(position);
            if (entity==null) return;
            GlideLoadUtils.load(mContext,entity.getImgUrl(),holder.binding.voteLogo,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            holder.binding.voteName.setText(Utils.getString(entity.getPlayerName()));
            if (Utils.isEmpty(entity.getVoteNums()))
                holder.binding.voteCount.setText(String.format(mContext.getResources().getString(R.string.place_people),"0"));
            else
                holder.binding.voteCount.setText(String.format(mContext.getResources().getString(R.string.place_votepeople),entity.getVoteNums()));

            holder.binding.btnVote.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ItemVoteBinding binding;

        public ViewHolder(ItemVoteBinding binding) {
            super(binding.getRoot());
            this.binding =binding;

            int width = (int) (MyApplication.getInstance().screenWidth -mContext.getResources().getDimension(R.dimen.px18)*2 -
                                mContext.getResources().getDimension(R.dimen.px16)*2);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.voteLogo.getLayoutParams();
            params.height =width/2;
            binding.voteLogo.setLayoutParams(params);
            Utils.setBackgroundSolid(mContext,binding.btnVote);
            binding.voteCount.setTextColor(Utils.getThemeColor(mContext));
        }
    }
}
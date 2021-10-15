package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.VoteEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemVoteselectionBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票
 */
public class VoteSelectionAdapter extends BassRecyclerAdapter{
    private OnClickCallBack onClickCallBack;
    private List<VoteEntity> list =new ArrayList<>();

    public VoteSelectionAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setList(List<VoteEntity> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1)
            return FOOT_VIEW;
        return NORMAL_VIEW;
    }

    @Override
    public int getItemCount() {
        return (list==null || list.size()==0)?0:list.size()+1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new ViewHolder(ItemVoteselectionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            VoteEntity entity =list.get(position);
            if (entity==null) return;
            GlideLoadUtils.load(mContext,entity.getImgUrl(),holder.binding.hotVoteIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
            holder.binding.titleVote.setText(Utils.getString(entity.getActiveName()));
            if (Utils.isEmpty(entity.getVoteUsers()))
                holder.binding.countVote.setText(String.format(mContext.getResources().getString(R.string.place_people),"0"));
            else
                holder.binding.countVote.setText(String.format(mContext.getResources().getString(R.string.place_people),entity.getVoteUsers()));
            holder.binding.timeVote.setText(String.format(mContext.getResources().getString(R.string.place_start_end_time),
                    Utils.getTimeFormat(entity.getStartDate()),Utils.getTimeFormat(entity.getEndDate())));
            holder.binding.infoVote.setText(Utils.getString(entity.getIntroduce()));
            holder.binding.layoutVoteSelection.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        ItemVoteselectionBinding binding;

        public ViewHolder(ItemVoteselectionBinding binding) {
            super(binding.getRoot());
            this.binding =binding;

            int width = (int) (MyApplication.getInstance().screenWidth-mContext.getResources().getDimension(R.dimen.left_app)*2);
            ConstraintLayout.LayoutParams lpVote = (ConstraintLayout.LayoutParams) binding.hotVoteIcon.getLayoutParams();
            lpVote.height = width * 264 / 697;
            binding.hotVoteIcon.setLayoutParams(lpVote);
        }
    }
}
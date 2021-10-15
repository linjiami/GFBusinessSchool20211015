package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.AudioEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemAudioBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频
 */
public class AudioAdapter extends BassRecyclerAdapter {
    private List<AudioEntity> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public AudioAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack =onClickCallBack;
    }

    public void setmList(List<AudioEntity> mList) {
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
        if (viewType==FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new MyViewHodler(ItemAudioBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            AudioEntity bean =mList.get(position);
            if (bean==null) return;
            GlideLoadUtils.load(mContext,bean.getLogoUrl(),myHolder.binding.coverCourseCenter,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
            myHolder.binding.titleCourseCenter.setText(Utils.getString(bean.getName()));
            myHolder.binding.countCourseCenter.setText(String.format(mContext.getString(R.string.count_study),
                    bean.getStuNum()+""));
            myHolder.binding.layoutCourseCenter.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private ItemAudioBinding binding;

        public MyViewHodler(@NonNull ItemAudioBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
            int width = (int) (MyApplication.getInstance().screenWidth*592.0/750-mContext.getResources().getDimension(R.dimen.left_app)*2);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.coverCourseCenter.getLayoutParams();
            params.width=width;
            params.height= (int) (width*9.0/16);
            binding.coverCourseCenter.setLayoutParams(params);
        }
    }
}

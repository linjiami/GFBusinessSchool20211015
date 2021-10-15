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
import com.gfbusinessschool.fragment.LiveFragment;
import com.gfbusinessschool.bean.LiveBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemLiveBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）/课程列表（课程目录）
 */
public class LiveAdapter extends BassRecyclerAdapter {
    private List<LiveBean> mList = new ArrayList<>();
    private String typeView ;
    private OnClickCallBack onClickCallBack;

    public LiveAdapter(Context mContext, String typeView, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.typeView = typeView;
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<LiveBean> mList) {
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
            return new MyViewHodler(ItemLiveBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            LiveBean bean =mList.get(position);
            if (bean==null) return;
            GlideLoadUtils.load(mContext,bean.getImgUrl(),myHolder.coverLive,GlideLoadUtils.TYPE_PLACE_HOLDER_LIVE_LIST);
            myHolder.titleLive.setText(Utils.getString(bean.getTitle()));
            myHolder.teacherLive.setText(Utils.getString(bean.getTeacherName()));
            myHolder.timeLive.setText(Utils.getTimeFormat(bean.getStartDate()));
            if (Utils.getString(typeView).equals(LiveFragment.TYPEVIEW_HISTORY_LIVE)){
                myHolder.liveState.setText(mContext.getResources().getString(R.string.history_live));
            }else{
                if (Utils.getString(bean.getZbStatus()).equals("1"))
                    myHolder.liveState.setText(mContext.getResources().getString(R.string.live_nostart));
                else if (Utils.getString(bean.getZbStatus()).equals("2"))
                    myHolder.liveState.setText(mContext.getResources().getString(R.string.living));
                else
                    myHolder.liveState.setText(mContext.getResources().getString(R.string.history_live));
            }
            myHolder.layoutLive.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private View layoutLive;
        private ImageView coverLive;
        private TextView titleLive, teacherLive,liveState,timeLive;

        public MyViewHodler(@NonNull ItemLiveBinding binding) {
            super(binding.getRoot());
            layoutLive = binding.layoutLive;
            coverLive = binding.coverLive;
            titleLive = binding.titleLive;
            teacherLive = binding.teacherLive;
            liveState = binding.liveState;
            timeLive = binding.timeLive;

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) coverLive.getLayoutParams();
            double width =(MyApplication.getInstance().screenWidth -mContext.getResources().getDimension(R.dimen.left_app)*2);
            params.height = (int) (width*262/697);
            coverLive.setLayoutParams(params);
            Utils.setBackgroundSolid(mContext,liveState);
        }
    }

}

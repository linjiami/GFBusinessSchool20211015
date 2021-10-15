package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemStoreBinding;
import com.gfbusinessschool.utils.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）/课程列表（课程目录）
 */
public class StoreAdapter extends BassRecyclerAdapter {
    private List<StoreEntity> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public StoreAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack =onClickCallBack;
    }

    public void setmList(List<StoreEntity> mList) {
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
            return new MyViewHodler(ItemStoreBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            myHolder.storeLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            StoreEntity storeEntity =mList.get(position);
            if (storeEntity==null) return;
            myHolder.nameStore.setText(Utils.getString(storeEntity.getStoreName()));
            String totalMembers =String.format(mContext.getString(R.string.place_people),
                    "0");
            if (!Utils.isEmpty(storeEntity.getTotalMembers()))
                totalMembers =String.format(mContext.getString(R.string.place_people),
                        Utils.getString(storeEntity.getTotalMembers()));
            myHolder.countStorePeople.setText(totalMembers);

            int hoursToday =0;
            double minutesAllToday =0;
            if (!Utils.isEmpty(storeEntity.getTodayStudySeconds())){
                hoursToday =Integer.parseInt(storeEntity.getTodayStudySeconds())/(60*60);
                minutesAllToday = Double.parseDouble(storeEntity.getTodayStudySeconds())%(60*60.0)/3600;
            }
            minutesAllToday =new BigDecimal(minutesAllToday).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
            if (minutesAllToday>=1) {
                hoursToday++;
                minutesAllToday =0;
            }
            myHolder.timeToday.setText(String.format(mContext.getString(R.string.place_today_time), (hoursToday+minutesAllToday)==0?"0":(hoursToday+minutesAllToday)+""));

            int hours =0;
            double minutesAll =0;
            if (!Utils.isEmpty(storeEntity.getTotalStudySeconds())){
                hours =Integer.parseInt(storeEntity.getTotalStudySeconds())/(60*60);
                minutesAll = Double.parseDouble(storeEntity.getTotalStudySeconds())%(60*60.0)/3600;
            }
            minutesAll =new BigDecimal(minutesAll).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
            if (minutesAll>=1) {
                hoursToday++;
                minutesAll =0;
            }
            myHolder.timeAll.setText(String.format(mContext.getString(R.string.place_all_time),(hours+minutesAll)==0?"0":(hours+minutesAll)+""));
            myHolder.integralCount.setText(String.format(mContext.getString(R.string.place_integral),
                    Utils.getString(storeEntity.getTotalIntegral())));
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private CardView storeLayout;
        private TextView countStorePeople, nameStore,timeToday,integralCount,timeAll;

        public MyViewHodler(@NonNull ItemStoreBinding binding) {
            super(binding.getRoot());
            storeLayout = binding.storeLayout;
            countStorePeople = binding.countStorePeople;
            nameStore = binding.nameStore;
            timeToday = binding.timeToday;
            integralCount = binding.integralCount;
            timeAll = binding.timeAll;
        }
    }

}

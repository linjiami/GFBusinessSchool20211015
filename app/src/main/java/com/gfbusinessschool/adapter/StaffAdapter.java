package com.gfbusinessschool.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StaffEntity;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemStaffBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）/课程列表（课程目录）
 */
public class StaffAdapter extends BassRecyclerAdapter {
    private List<StaffEntity> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;

    public StaffAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack =onClickCallBack;
    }

    public void setmList(List<StaffEntity> mList) {
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
            return new MyViewHodler(ItemStaffBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            myHolder.binding.staffLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            StaffEntity entity =mList.get(position);
            if (entity==null) return;
            GlideLoadUtils.load(mContext,entity.getHeadImgUrl(),myHolder.binding.headImg,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            myHolder.binding.nameStaff.setText(Utils.getString(entity.getName()));
            String age =String.format(mContext.getString(R.string.place_age),"18");
            if (!Utils.isEmpty(entity.getAge()))
                age =String.format(mContext.getString(R.string.place_age),entity.getAge());
            myHolder.binding.ageStaff.setText(age);
            Drawable drawable =null;
            if (!Utils.isEmpty(entity.getSex()) && entity.getSex().equals("男"))
                drawable= mContext.getResources().getDrawable(R.mipmap.man);
            else
                drawable= mContext.getResources().getDrawable(R.mipmap.woman);
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            myHolder.binding.ageStaff.setCompoundDrawables(null,null,drawable,null);
            myHolder.binding.addressStaff.setText(Utils.getString(entity.getStoreName()));
            myHolder.binding.jointimeStaff.setText(String.format(mContext.getString(R.string.place_entry_time),Utils.getString(entity.getEntryDate())));
            int hours =0;
            double minutesAll =0;
            if (!Utils.isEmpty(entity.getStudySeconds())){
                hours =Integer.parseInt(entity.getStudySeconds())/(60*60);
                minutesAll = Double.parseDouble(entity.getStudySeconds())%(60*60.0)/3600;
            }
            minutesAll =new BigDecimal(minutesAll).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
            if (minutesAll>=1) {
                hours++;
                minutesAll =0;
            }
            myHolder.binding.timeAll.setText(String.format(mContext.getString(R.string.place_timeall_integral),(hours+minutesAll)==0?"0":(hours+minutesAll)+"",""+entity.getIntegral()));
            if (entity.getPassRate()==1)
                myHolder.binding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
            else
                myHolder.binding.biyexuanzhangIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private ItemStaffBinding binding;

        public MyViewHodler(@NonNull ItemStaffBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

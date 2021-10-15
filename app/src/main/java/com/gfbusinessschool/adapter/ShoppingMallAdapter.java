package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemShoppingmallBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingMallAdapter extends BassRecyclerAdapter {
    private OnClickCallBack mOnClickCallBack;
    private List<GoodsEntity> mlist =new ArrayList<>();

    public ShoppingMallAdapter(Context mContext, OnClickCallBack mOnClickCallBack) {
        super(mContext);
        this.mOnClickCallBack = mOnClickCallBack;
    }

    public void setMlist(List<GoodsEntity> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mlist.size())
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
            return new ViewHolder(ItemShoppingmallBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            GoodsEntity entity =mlist.get(position);
            if (entity==null) return;
            GlideLoadUtils.load(mContext,entity.getImgUrl(),viewHolder.binding.cover,R.mipmap.place_huise);
            viewHolder.binding.goodsName.setText(Utils.getString(entity.getGoodsName()));
            if (Utils.isEmpty(entity.getPrice()))
                viewHolder.binding.countIntegral.setText(String.format(mContext.getResources().getString(R.string.place_integral_count),"0"));
            else
                viewHolder.binding.countIntegral.setText(String.format(mContext.getResources().getString(R.string.place_integral_count),entity.getPrice()));
            int surplus;//商品总量
            if (Utils.isEmpty(entity.getSurplus()))
                surplus =0;
            else
                surplus =Integer.parseInt(entity.getSurplus());
            int stock;//库存
            if (Utils.isEmpty(entity.getStock()))
                stock =0;
            else
                stock =Integer.parseInt(entity.getStock());
            viewHolder.binding.countGoods.setText(String.format(mContext.getResources().getString(R.string.point_goodscount),(surplus)+""));
            if (stock==0)
                viewHolder.binding.soldOutIcon.setVisibility(View.VISIBLE);
            else
                viewHolder.binding.soldOutIcon.setVisibility(View.GONE);
            viewHolder.binding.layoutShopping.setOnClickListener(v -> {
                if (mOnClickCallBack!=null) mOnClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mlist==null || mlist.size()==0)?0:mlist.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemShoppingmallBinding binding;
        public ViewHolder(@NonNull ItemShoppingmallBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
            double width = (MyApplication.getInstance().screenWidth -mContext.getResources().getDimension(R.dimen.px156)
                    -mContext.getResources().getDimension(R.dimen.px30))*1.0/2;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.cover.getLayoutParams();
            params.width = (int) width;
            params.height = (int) width;
            binding.cover.setLayoutParams(params);
            binding.countIntegral.setTextColor(Utils.getThemeColor(mContext));
        }
    }
}
package com.gfbusinessschool.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemShoppingmallOrderBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingMallOrderAdapter extends BassRecyclerAdapter {
    public static final String TYPE_ORDER_WAITTING_SEND ="待发货";
    public static final String TYPE_ORDER_WAITTING_GET ="待收货";
    public static final String TYPE_ORDER_FINISH ="已完成";
    private String typeView;
    private OnClickCallBack onClickCallBack;
    private List<GoodsEntity> mlist =new ArrayList<>();

    public ShoppingMallOrderAdapter(Context mContext, String typeView, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.mContext = mContext;
        this.typeView = typeView;
        this.onClickCallBack = onClickCallBack;
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
            return new ViewHolder(ItemShoppingmallOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            GoodsEntity entity =mlist.get(position);
            if (entity==null) return;
            viewHolder.binding.orderNum.setText(Utils.getString(entity.getOrderNum()));
            viewHolder.binding.orderTime.setText(Utils.getTimeFormat(entity.getCreateDate()));
            viewHolder.binding.nameGoods.setText(Utils.getString(entity.getGoodsName()));
            if (Utils.isEmpty(entity.getGoodsPrice()))
                viewHolder.binding.integralCount.setText(String.format(mContext.getString(R.string.place_integral_count),"0"));
            else
                viewHolder.binding.integralCount.setText(String.format(mContext.getString(R.string.place_integral_count),entity.getGoodsPrice()));
            if (Utils.isEmpty(entity.getGoodsNums()))
                viewHolder.binding.bugCount.setText(String.format(mContext.getString(R.string.place_buy_count),"0"));
            else
                viewHolder.binding.bugCount.setText(String.format(mContext.getString(R.string.place_buy_count),entity.getGoodsNums()));
            if (Utils.isEmpty(entity.getPayPrice()))
                viewHolder.binding.countIntegral.setText(String.format(mContext.getString(R.string.place_integral_all),"0"));
            else
                viewHolder.binding.countIntegral.setText(String.format(mContext.getString(R.string.place_integral_all),entity.getPayPrice()));
            if (Utils.getString(typeView).equals(TYPE_ORDER_FINISH) || Utils.getString(typeView).equals(TYPE_ORDER_WAITTING_GET)){
                String content = entity.getExpressName()+"  "+entity.getExpressNum();
                SpannableStringBuilder builder = new SpannableStringBuilder(
                        content);
                //设置文字
                ForegroundColorSpan span = new ForegroundColorSpan(Utils.getThemeColor(mContext));
                ForegroundColorSpan span2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_33));
                builder.setSpan(span, 0, Utils.getString(entity.getExpressName()).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                builder.setSpan(span2, Utils.getString(entity.getExpressName()).length(), content.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD),0,Utils.getString(entity.getExpressName()).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                viewHolder.binding.mailCount.setText(builder);
                viewHolder.binding.mailCount.setMovementMethod(LinkMovementMethod.getInstance());
            }
            GlideLoadUtils.load(mContext,entity.getImgUrl(),viewHolder.binding.cover,R.mipmap.place_huise);
            viewHolder.binding.layoutOrder.setOnClickListener(v -> {
                if (onClickCallBack!=null && Utils.getString(typeView).equals(TYPE_ORDER_WAITTING_GET)) onClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mlist==null || mlist.size()==0)?0:mlist.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemShoppingmallOrderBinding binding;
        public ViewHolder(@NonNull ItemShoppingmallOrderBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
            if (Utils.getString(typeView).equals(TYPE_ORDER_WAITTING_SEND)){
                binding.mailCount.setVisibility(View.GONE);
                binding.confirmGetGoods.setVisibility(View.GONE);
                binding.waittingSendGoods.setVisibility(View.VISIBLE);
                binding.waittingSendGoods.setText(mContext.getString(R.string.waitting_sendgoods));
            }else if (Utils.getString(typeView).equals(TYPE_ORDER_WAITTING_GET)){
                binding.mailCount.setVisibility(View.VISIBLE);
                binding.confirmGetGoods.setVisibility(View.VISIBLE);
                binding.waittingSendGoods.setVisibility(View.GONE);
            }else if (Utils.getString(typeView).equals(TYPE_ORDER_FINISH)){
                binding.mailCount.setVisibility(View.VISIBLE);
                binding.confirmGetGoods.setVisibility(View.GONE);
                binding.waittingSendGoods.setVisibility(View.VISIBLE);
                binding.waittingSendGoods.setText(mContext.getString(R.string.test_finish));
            }
        }
    }
}
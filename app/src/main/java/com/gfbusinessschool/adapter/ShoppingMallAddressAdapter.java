package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ShoppingMallAddressEntity;
import com.gfbusinessschool.databinding.ItemShoppingmallAddressBinding;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingMallAddressAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private OnClickCallBack onClickCallBack;
    private List<ShoppingMallAddressEntity> addressEntityList =new ArrayList<>();

    public ShoppingMallAddressAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        this.mContext = mContext;
        this.onClickCallBack = onClickCallBack;
    }

    public void setAddressEntityList(List<ShoppingMallAddressEntity> addressEntityList) {
        this.addressEntityList = addressEntityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemShoppingmallAddressBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            ShoppingMallAddressEntity entity =addressEntityList.get(position);
            if (entity==null) return;
            viewHolder.binding.address.setText(Utils.getString(entity.getAddress()));
            viewHolder.binding.name.setText(String.format(mContext.getString(R.string.place_name_phone),
                    Utils.getString(entity.getName()),Utils.getString(entity.getMobile())));
            viewHolder.binding.layoutAddress.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            viewHolder.binding.modifyLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onModifyClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return addressEntityList==null?0:addressEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemShoppingmallAddressBinding binding;
        public ViewHolder(@NonNull ItemShoppingmallAddressBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
        }
    }
}
package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.databinding.ItemAreaclassifyGroupBinding;
import com.gfbusinessschool.utils.Utils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AreaClassifyAdapter extends BassRecyclerAdapter {
    private final OnClickCallBack onClickCallBack;
    private List<StoreAddressEntity> mlist=new ArrayList<>();

    public AreaClassifyAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setList(List<StoreAddressEntity> list) {
        this.mlist = list;
    }

    @Override
    public int getItemCount() {
        return mlist==null?0:mlist.size();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(ItemAreaclassifyGroupBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        if (_holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) _holder;
            StoreAddressEntity entity =mlist.get(position);
            if (entity==null) return;
            groupViewHolder.binding.cityName.setText(Utils.getString(entity.getAddressName()));
            AreaClassifyChildAdapter adapter =new AreaClassifyChildAdapter(mContext, new OnClickCallBack() {
                @Override
                public void onClick(StoreAddressEntity storeAddressEntity) {
                    if (onClickCallBack!=null) onClickCallBack.onClick(position,storeAddressEntity);
                }
            });
            adapter.setList(entity.getCityList());
            groupViewHolder.binding.recyclerViewChild.setAdapter(adapter);
            if (entity.isExpand())
                groupViewHolder.binding.jiantou.setImageResource(R.mipmap.jiantou_bottom);
            else
                groupViewHolder.binding.jiantou.setImageResource(R.mipmap.jiantou_right);
            groupViewHolder.binding.layoutCity.setOnClickListener(v -> {
                if (entity.isExpand()){
                    groupViewHolder.binding.recyclerViewChild.setVisibility(View.GONE);
                    entity.setExpand(false);
                }else {
                    groupViewHolder.binding.recyclerViewChild.setVisibility(View.VISIBLE);
                    entity.setExpand(true);
                }
                notifyDataSetChanged();
            });
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemAreaclassifyGroupBinding binding;

        GroupViewHolder(@NotNull ItemAreaclassifyGroupBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
            binding.recyclerViewChild.setLayoutManager(new GridLayoutManager(mContext,4));
        }
    }
}
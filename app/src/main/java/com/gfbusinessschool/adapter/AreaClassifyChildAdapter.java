package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.databinding.ItemAreaclassifyChildBinding;
import com.gfbusinessschool.utils.Utils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AreaClassifyChildAdapter extends BassRecyclerAdapter {
    private final OnClickCallBack onClickCallBack;
    private List<StoreAddressEntity> mlist = new ArrayList<>();

    public AreaClassifyChildAdapter(Context mContext, OnClickCallBack onClickCallBack) {
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
        return new ChildViewHolder(ItemAreaclassifyChildBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position, @NonNull @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder viewHolder = (ChildViewHolder) holder;
            StoreAddressEntity entity = mlist.get(position);
            if (entity == null) return;
            viewHolder.binding.contentSearch.setText(Utils.getString(entity.getAddressName()));
            viewHolder.binding.childAreaLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(entity);
            });
        }
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        ItemAreaclassifyChildBinding binding;

        ChildViewHolder(@NotNull ItemAreaclassifyChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ItemListPopwindowBinding;

import java.util.ArrayList;
import java.util.List;

public class ListPopWindowAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mList =new ArrayList<>();
    private OnClickCallBack mOnClickCallBack;

    public ListPopWindowAdapter(Context mContext, List<String> mList, OnClickCallBack mOnClickCallBack) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOnClickCallBack = mOnClickCallBack;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodler(ItemListPopwindowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHodler){
            MyViewHodler myViewHodler = (MyViewHodler) holder;
            myViewHodler.tvContent.setText(mList.get(position));
            myViewHodler.tvContent.setOnClickListener(v -> {
                if (mOnClickCallBack!=null) mOnClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private TextView tvContent;

        MyViewHodler(@NonNull ItemListPopwindowBinding binding) {
            super(binding.getRoot());
            tvContent =binding.tvContent;
        }
    }
}

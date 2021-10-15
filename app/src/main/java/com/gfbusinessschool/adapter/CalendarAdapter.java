package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.SigninDayEntity;
import com.gfbusinessschool.databinding.ItemCalendarBinding;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private List<SigninDayEntity> list =new ArrayList<>();
    private Context context;

    public CalendarAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<SigninDayEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCalendarBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            SigninDayEntity entity =list.get(position);
            if (entity==null) return;
            if (Utils.isEmpty(entity.getDay())){
                viewHolder.viewbinding.todayTv.setText("");
                viewHolder.viewbinding.todayTv.setBackground(null);
            }else {
                viewHolder.viewbinding.todayTv.setText(Utils.getString(entity.getDay()));   
                if (entity.isSigin()) {
                    viewHolder.viewbinding.todayTv.setTextColor(context.getResources().getColor(R.color.white));
                    Utils.setBackgroundSolid(context,viewHolder.viewbinding.todayTv);
                } else {
                    viewHolder.viewbinding.todayTv.setTextColor(context.getResources().getColor(R.color.color_33));
                    Utils.setBackgroundSolid(viewHolder.viewbinding.todayTv,context.getResources().getColor(R.color.color_f4));
                }
            }
//            viewHolder.viewbinding.layoutCalendar.setOnClickListener(v -> {
//                viewHolder.viewbinding.todayTv.setTextColor(context.getResources().getColor(R.color.white));
//                viewHolder.viewbinding.todayTv.setBackground(context.getResources().getDrawable(R.drawable.shape30_themecolor));
//                if (clickCallBack!=null) clickCallBack.onClick(position);
//            });
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemCalendarBinding viewbinding;

        public ViewHolder(@NonNull ItemCalendarBinding viewbinding) {
            super(viewbinding.getRoot());
            this.viewbinding =viewbinding;
        }
    }
}
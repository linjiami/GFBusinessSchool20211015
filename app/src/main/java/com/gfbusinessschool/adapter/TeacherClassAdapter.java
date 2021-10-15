package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.bean.TeacherEntity;
import com.gfbusinessschool.databinding.ItemClassteacherBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.List;


/**
 * 专题老师列表
 */
public class TeacherClassAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TeacherEntity> teacherEntityList;
    private OnClickCallBack onClickCallBack;

    public TeacherClassAdapter(Context mContext, List<TeacherEntity> teacherEntityList, OnClickCallBack onClickCallBack) {
        this.mContext = mContext;
        this.teacherEntityList = teacherEntityList;
        this.onClickCallBack = onClickCallBack;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemClassteacherBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            TeacherEntity entity =teacherEntityList.get(position);
            if (entity==null) return;
            GlideLoadUtils.load(mContext,entity.getLogoUrl(),viewHolder.binding.headIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            viewHolder.binding.teacherName.setText(Utils.getString(entity.getName()));
            viewHolder.binding.teacherItemLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return teacherEntityList==null?0:teacherEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemClassteacherBinding binding;
        public ViewHolder(@NonNull ItemClassteacherBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
        }
    }
}
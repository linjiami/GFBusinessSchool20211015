package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.CourseWareEntity;
import com.gfbusinessschool.databinding.ItemCoursewareBinding;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.Utils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 课件
 */
public class CourseWareAdapter extends BassRecyclerAdapter {
    private OnClickCallBack onClickCallBack;
    private List<CourseWareEntity> list=new ArrayList<>();

    public CourseWareAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setList(List<CourseWareEntity> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCoursewareBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        ViewHolder holder = (ViewHolder) _holder;
        CourseWareEntity entity =list.get(position);
        if (entity==null) return;
        if (FileTypeUtils.isWordPDFFileType(entity.getCourseWareUrl()) ||
                FileTypeUtils.isPPTFileType(entity.getCourseWareUrl())) {//文件
            Utils.setDrawableTint(mContext, R.mipmap.file_readcollection,holder.binding.fileName,0);
        }else if (FileTypeUtils.isImageFileType(entity.getCourseWareUrl())){
            Utils.setDrawableTint(mContext, R.mipmap.img_readcollection,holder.binding.fileName,0);
        }
        holder.binding.fileName.setText(Utils.getString(entity.getName()));
        if (!Utils.isEmpty(entity.getCourseWareUrl())){
            String[] patharr =entity.getCourseWareUrl().split("\\.");
            if (patharr.length>0)
                holder.binding.fileName.setText(Utils.getString(entity.getName())+"."+patharr[patharr.length-1]);
        }

        holder.binding.fileName.setOnClickListener(v -> {
            if (onClickCallBack!=null) onClickCallBack.onClick(position);
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private ItemCoursewareBinding binding;

        public ViewHolder(@NonNull @NotNull ItemCoursewareBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }
    }
}
package com.gfbusinessschool.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemReadcollection2Binding;
import com.gfbusinessschool.databinding.ItemReadcollection3Binding;
import com.gfbusinessschool.databinding.ItemReadcollectionBinding;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的读书汇分享/读书汇分享（所有人）
 */
public class ReadCollectionAdapter extends BassRecyclerAdapter{
    public static final String TYPE_READCOLLECTION_MY ="我的读书汇";
    public static final String TYPE_READCOLLECTION_HOMEPAGER ="首页读书汇分类";
    public static final String TYPE_READCOLLECTION_COLLECTED ="读书汇收藏";
    public static final String TYPE_READCOLLECTION_NEW ="首页读书汇上新";
    private List<ReadCollectionEntity.Entity> mlist =new ArrayList<>();
    private OnClickCallBack onClickCallBack;
    private String typeView;

    public ReadCollectionAdapter(Context mContext,String typeView, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.typeView = typeView;
        this.onClickCallBack = onClickCallBack;
    }

    public void setMlist(List<ReadCollectionEntity.Entity> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getItemCount() {
        if (Utils.getString(typeView).equals(TYPE_READCOLLECTION_NEW))
            return mlist == null ? 0 : mlist.size();
        else
            return mlist == null || mlist.size() == 0 ? 0 : mlist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (Utils.getString(typeView).equals(TYPE_READCOLLECTION_NEW))
            return NORMAL_VIEW3;
        else{
            if (position==getItemCount()-1) {
                return FOOT_VIEW;
            } else {
                if (Utils.getString(typeView).equals(TYPE_READCOLLECTION_HOMEPAGER)
                || Utils.getString(typeView).equals(TYPE_READCOLLECTION_COLLECTED))
                    return NORMAL_VIEW2;
                else
                    return NORMAL_VIEW;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else if (viewType==NORMAL_VIEW)
            return new ViewHolder(ItemReadcollectionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else if (viewType==NORMAL_VIEW2)
            return new AllViewHolder(ItemReadcollection2Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new NewViewHolder(ItemReadcollection3Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            ReadCollectionEntity.Entity entity =mlist.get(position);
            if (entity==null) return;
            holder.binding.title.setText(Utils.getString(entity.getTitle()));
            holder.binding.time.setText(Utils.getTimeFormat(entity.getCreateDate()));
            holder.binding.contentShare.setText(Utils.getString(entity.getContent()));
            if (Utils.getString(entity.getFileUrl()).toLowerCase().contains("jpg")){
                Utils.setDrawableTint(mContext,R.mipmap.img_readcollection,holder.binding.title,0);
            }else if (Utils.getString(entity.getFileUrl()).toLowerCase().contains("mp4")){
                Utils.setDrawableTint(mContext,R.mipmap.video_readcollection,holder.binding.title,0);
            }else {
                Utils.setDrawableTint(mContext,R.mipmap.file_readcollection,holder.binding.title,0);
            }
            //status 状态 0待审核 1审核通过 2驳回
            if (Utils.getString(entity.getStatus()).equals("0")){
                holder.binding.stateIcon.setImageResource(R.mipmap.share_passing);
            }else  if (Utils.getString(entity.getStatus()).equals("1")){
                holder.binding.stateIcon.setImageResource(R.mipmap.share_passed);
            }else {
                holder.binding.stateIcon.setImageResource(R.mipmap.share_refuse);
            }
            Utils.setImageViewTint(mContext,holder.binding.stateIcon);
            holder.binding.readCollectionLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
        }else if (_holder instanceof AllViewHolder){
            AllViewHolder holder = (AllViewHolder) _holder;
            ReadCollectionEntity.Entity entity =mlist.get(position);
            if (entity==null) return;
            holder.binding.title.setText(Utils.getString(entity.getTitle()));
            holder.binding.time.setText(Utils.getTimeFormat(entity.getCreateDate()));
            holder.binding.userName.setText(Utils.getString(entity.getUserName()));
            holder.binding.studyCount.setText(String.format(mContext.getString(R.string.study_count),""+entity.getStudyAmount()));
            if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                Utils.setDrawableTint(mContext,R.mipmap.img_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+".JPG");
            }else if (FileTypeUtils.isVideoFileType(entity.getFileUrl())){
                Utils.setDrawableTint(mContext,R.mipmap.video_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+".MP4");
            }else {
                Utils.setDrawableTint(mContext,R.mipmap.file_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle()));
                if (!Utils.isEmpty(entity.getFileUrl())){
                    String[] patharr =entity.getFileUrl().split("\\.");
                    if (patharr.length>0)
                        holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+"."+patharr[patharr.length-1]);
                }
            }
            GlideLoadUtils.load(mContext,entity.getHeadImgUrl(),holder.binding.userHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            if (entity.getPassRate()==1)
                holder.binding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
            else
                holder.binding.biyexuanzhangIcon.setVisibility(View.GONE);
            holder.binding.readCollectionLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            holder.binding.itemReadCollection.fileName.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClickReadCollection(position);
            });
        }else if (_holder instanceof NewViewHolder){
            NewViewHolder holder = (NewViewHolder) _holder;
            ReadCollectionEntity.Entity entity =mlist.get(position);
            if (entity==null) return;
            holder.binding.title.setText(Utils.getString(entity.getTitle()));
            holder.binding.time.setText(Utils.getTimeFormat(entity.getCreateDate()));
            holder.binding.userName.setText(Utils.getString(entity.getUserName()));
            holder.binding.studyCount.setText(String.format(mContext.getString(R.string.study_count),""+entity.getStudyAmount()));
            if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                Utils.setDrawableTint(mContext,R.mipmap.img_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+".JPG");
            }else if (FileTypeUtils.isVideoFileType(entity.getFileUrl())){
                Utils.setDrawableTint(mContext,R.mipmap.video_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+".MP4");
            }else {
                Utils.setDrawableTint(mContext,R.mipmap.file_readcollection,holder.binding.itemReadCollection.fileName,0);
                holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle()));
                if (!Utils.isEmpty(entity.getFileUrl())){
                    String[] patharr =entity.getFileUrl().split("\\.");
                    if (patharr.length>0)
                        holder.binding.itemReadCollection.fileName.setText(Utils.getString(entity.getTitle())+"."+patharr[patharr.length-1]);
                }
            }
            GlideLoadUtils.load(mContext,entity.getHeadImgUrl(),holder.binding.userHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            if (entity.getPassRate()==1)
                holder.binding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
            else
                holder.binding.biyexuanzhangIcon.setVisibility(View.GONE);
            holder.binding.readCollectionLayout.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(entity);
            });
            holder.binding.itemReadCollection.fileName.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClickReadCollection(entity);
            });
            if (position!=mlist.size()-1)
                holder.binding.lineItemReadCollection.setVisibility(View.VISIBLE);
            else
                holder.binding.lineItemReadCollection.setVisibility(View.GONE);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        ItemReadcollectionBinding binding;

        public ViewHolder(ItemReadcollectionBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    private class AllViewHolder extends RecyclerView.ViewHolder{
        ItemReadcollection2Binding binding;

        public AllViewHolder(ItemReadcollection2Binding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    private class NewViewHolder extends RecyclerView.ViewHolder{
        ItemReadcollection3Binding binding;

        public NewViewHolder(ItemReadcollection3Binding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
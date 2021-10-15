package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.CertificateEntity;
import com.gfbusinessschool.bean.CertificateListEntity;
import com.gfbusinessschool.databinding.ItemCertificateBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 证书
 */
public class CertificateAdapter extends BassRecyclerAdapter{
    private List<CertificateEntity> mlist =new ArrayList<>();

    public CertificateAdapter(Context mContext) {
        super(mContext);
    }

    public void setMlist(List<CertificateEntity> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getItemCount() {
        return mlist==null?0:mlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCertificateBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        super.onBindViewHolder(_holder, position);
        if (_holder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) _holder;
            CertificateEntity entity =mlist.get(position);
            if (entity==null) return;
            AppUserEntity appUserEntity = MyApplication.getInstance().getAppUserEntity();
            GlideLoadUtils.load(mContext,entity.getImgUrl(),holder.binding.certificateIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_NO);
            if (Utils.getString(entity.getType()).equals("1")){//毕业证书
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.graductionLayout.getLayoutParams();
                float width = MyApplication.getInstance().screenWidth-mContext.getResources().getDimension(R.dimen.left_app)*2;
                params.topMargin = (int) (width*495/699*205/495);
                params.leftMargin = (int) (width*112/699);
                params.rightMargin = (int) (width*112/699);
                holder.binding.graductionLayout.setLayoutParams(params);

                holder.binding.graductionLayout.setVisibility(View.VISIBLE);
                holder.binding.honorLayout.setVisibility(View.GONE);
                holder.binding.userName.setText(Utils.getString(appUserEntity!=null?appUserEntity.getName():""));
                holder.binding.graductionTime.setText(Utils.getDate(entity.getPassDate()));
                holder.binding.graductionCompany.setText(Utils.getString(appUserEntity!=null?appUserEntity.getCompanyName():""));
            }else if (Utils.getString(entity.getType()).equals("2")){//荣誉证书
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.honorLayout.getLayoutParams();
                float width = MyApplication.getInstance().screenWidth-mContext.getResources().getDimension(R.dimen.left_app)*2;
                params.topMargin = (int) (width*495/699*217/495);
                params.leftMargin = (int) (width*112/699);
                params.rightMargin = (int) (width*112/699);
                holder.binding.honorLayout.setLayoutParams(params);

                holder.binding.graductionLayout.setVisibility(View.GONE);
                holder.binding.honorLayout.setVisibility(View.VISIBLE);
                holder.binding.honorContent.setText(String.format(mContext.getResources().getString(R.string.place_honor),
                        Utils.getString(appUserEntity!=null?appUserEntity.getName():""),Utils.getString(entity.getActiveName())));
                holder.binding.honorCompany.setText(String.format(mContext.getResources().getString(R.string.place_huanhang),
                        Utils.getString(appUserEntity!=null?appUserEntity.getCompanyName():""),Utils.getDate(entity.getCertDate())));
            }else {
                holder.binding.graductionLayout.setVisibility(View.GONE);
                holder.binding.honorLayout.setVisibility(View.GONE);
            }
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        ItemCertificateBinding binding;

        public ViewHolder(@NonNull ItemCertificateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.certificateIcon.getLayoutParams();
            float width = MyApplication.getInstance().screenWidth-mContext.getResources().getDimension(R.dimen.left_app)*2;
            params.height = (int) (width*495/699);
            binding.certificateIcon.setLayoutParams(params);
        }
    }
}
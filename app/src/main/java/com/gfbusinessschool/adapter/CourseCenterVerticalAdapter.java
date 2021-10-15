package com.gfbusinessschool.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemCourseCenterBinding;
import com.gfbusinessschool.databinding.ItemCourseVerticalBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程中心列表/冠军分享列表
 */
public class CourseCenterVerticalAdapter extends BassRecyclerAdapter {
    private List<CourseBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;
    private boolean isChampionShare;//是否是冠军分享

    public CourseCenterVerticalAdapter(Context mContext, OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack = onClickCallBack;
    }

    public void setmList(List<CourseBean> mList) {
        this.mList = mList;
    }

    public void setChampionShare(boolean championShare) {
        isChampionShare = championShare;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mList.size())
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
            return new MyViewHodler(ItemCourseCenterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            CourseBean bean =mList.get(position);
            if (bean==null) return;
            myHolder.layoutCourseCenter.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            if (Utils.isEmpty(bean.getTestId())){
                myHolder.layoutBottomTest.setVisibility(View.GONE);
            }else {
                myHolder.layoutBottomTest.setVisibility(View.VISIBLE);
                myHolder.layoutBottomTest.setOnClickListener(v -> {
                    if (onClickCallBack!=null) onClickCallBack.onClickTest(position);
                });
            }

            GlideLoadUtils.load(mContext,bean.getLogoUrl(),myHolder.coverCourseCenter,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
            myHolder.titleCourseCenter.setText(Utils.getString(bean.getName()));
            myHolder.countCourseCenter.setText(Utils.getString(bean.getStuNum()));
            if (Utils.getString(bean.getIsPass()).equals("1")){
                myHolder.testPassStateCourseVer.setText(mContext.getResources().getString(R.string.test_passed));
                myHolder.testPassStateCourseVer.setTextColor(Utils.getThemeColor(mContext));
            }else {
                myHolder.testPassStateCourseVer.setText(mContext.getResources().getString(R.string.test_pass));
                myHolder.testPassStateCourseVer.setTextColor(mContext.getResources().getColor(R.color.color_969595));
            }
            if (Utils.isEmpty(bean.getStuNum()))
                myHolder.countCourseCenter.setText(String.format(mContext.getResources().getString(R.string.look_count2),
                        ""+0));
            else
                myHolder.countCourseCenter.setText(String.format(mContext.getResources().getString(R.string.look_count2),
                        Utils.getString(bean.getStuNum())));
            if (isChampionShare){
                String title =bean.getTitle();
                if (!Utils.isEmpty(title) && title.length()>8){
                    StringBuffer s = new StringBuffer(title);
                    s.insert(8, "\n");
                    myHolder.championShareTitle.setText(s);
                }else {
                    myHolder.championShareTitle.setText(Utils.getString(bean.getTitle()));
                }
                myHolder.championShareTeacher.setText(String.format(mContext.getResources().getString(R.string.place_spaceboarf),
                        Utils.getString(bean.getStoreName()), Utils.getString(bean.getName())));

                GlideLoadUtils.load(mContext,bean.getCoverImgUrl(),myHolder.coverCourseCenter,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                myHolder.titleCourseCenter.setText(Utils.getString(bean.getTitle()));
                if (Utils.isEmpty(bean.getLookTimes()))
                    myHolder.countCourseCenter.setText(String.format(mContext.getResources().getString(R.string.look_count2),
                            ""+0));
                else
                    myHolder.countCourseCenter.setText(String.format(mContext.getResources().getString(R.string.look_count2),
                            Utils.getString(bean.getLookTimes())));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private View layoutCourseCenter,layoutBottomTest,championShareLayout;
        private ImageView coverCourseCenter,iconTest;
        private TextView countCourseCenter, titleCourseCenter,testPassStateCourseVer,championShareTitle,testCourseVer,championShareTeacher;

        public MyViewHodler(@NonNull ItemCourseCenterBinding binding) {
            super(binding.getRoot());
            layoutCourseCenter = binding.layoutCourseCenter;
            layoutBottomTest = binding.layoutBottomTest;
            coverCourseCenter = binding.coverCourseCenter;
            countCourseCenter = binding.countCourseCenter;
            titleCourseCenter = binding.titleCourseCenter;
            testPassStateCourseVer = binding.testPassStateCourseVer;
            championShareTitle = binding.championShareTitle;
            iconTest = binding.iconTest;
            testCourseVer = binding.testCourseVer;
            championShareLayout = binding.championShareLayout;
            championShareTeacher = binding.championShareTeacher;

            int width = (int) (MyApplication.getInstance().screenWidth*592.0/750-mContext.getResources().getDimension(R.dimen.left_app)*2);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) coverCourseCenter.getLayoutParams();
            params.width=width;
            params.height=width*9/16;
            coverCourseCenter.setLayoutParams(params);

            if (isChampionShare)
                championShareLayout.setVisibility(View.VISIBLE);
            Utils.setImageViewTint(mContext,iconTest);
            testCourseVer.setTextColor(Utils.getThemeColor(mContext));
        }
    }

}

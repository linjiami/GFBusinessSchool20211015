package com.gfbusinessschool.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemCourseVerticalBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）/课程列表（课程目录）
 */
public class CourseVerticalAdapter extends BassRecyclerAdapter {
    public static final String TYPE_VIEW_FIRSTPAGE ="首页正在学课程列表";
    public static final String TYPE_VIEW_COURSE_CATALOG ="课程中心/单课列表";
    public static final String TYPE_VIEW_RANKLIST_COURSE_LOOKCOUNT ="排行/课程排行/观看次数";
    public static final String TYPE_VIEW_RANKLIST_COURSE_COMMENTCOUNT="排行/课程排行/评论次数";
    public static final String TYPE_VIEW_RANKLIST_TESTNOPASS_COURSE="管理首页/考核未通过课程排行";
    public static final String TYPE_VIEW_COLLECTED="我的/收藏";
    public static final String TYPE_VIEW_SHARE="我的/分享管理";
    public static final String TYPE_VIEW_STUDY_COURSE="学习的课程";
    public static final String TYPE_VIEW_STUDYMAP_COURSE="学习地图/课程目录";
    private List<CourseBean> mList = new ArrayList<>();
    private OnClickCallBack onClickCallBack;
    private String typeView =TYPE_VIEW_FIRSTPAGE;

    public CourseVerticalAdapter(Context mContext,OnClickCallBack onClickCallBack) {
        super(mContext);
        this.onClickCallBack =onClickCallBack;
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    public void setmList(List<CourseBean> mList) {
        this.mList = mList;
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
            return new MyViewHodler(ItemCourseVerticalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            CourseBean bean =mList.get(position);
            if (bean==null) return;
            onBindData(myHolder,position);
            myHolder.binding.layoutCourseVer.setOnClickListener(v -> {
               if (onClickCallBack!=null) onClickCallBack.onClick(position);
            });
            myHolder.binding.layoutBottomTest.setOnClickListener(v -> {
                if (onClickCallBack!=null) onClickCallBack.onClickTest(position);
            });

            GlideLoadUtils.load(mContext,bean.getLogoUrl(),myHolder.binding.coverCourseVer,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
            if (Utils.getString(typeView).equals(TYPE_VIEW_COURSE_CATALOG)){
                myHolder.binding.iconRanklistCourseVer.setVisibility(View.GONE);
                if (Utils.isEmpty(bean.getTestId()))
                    myHolder.binding.layoutBottomTest.setVisibility(View.GONE);
                else
                    myHolder.binding.layoutBottomTest.setVisibility(View.VISIBLE);
                if (Utils.getString(bean.getIsPass()).equals("1")){
                    myHolder.binding.testPassStateCourseVer.setText(mContext.getResources().getString(R.string.test_passed));
                    myHolder.binding.testPassStateCourseVer.setTextColor(Utils.getThemeColor(mContext));
                }else {
                    myHolder.binding.testPassStateCourseVer.setText(mContext.getResources().getString(R.string.test_pass));
                    myHolder.binding.testPassStateCourseVer.setTextColor(mContext.getResources().getColor(R.color.color_969595));
                }
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_FIRSTPAGE) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_COURSE_LOOKCOUNT) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_TESTNOPASS_COURSE) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_COURSE_COMMENTCOUNT)){
                myHolder.binding.layoutBottomTest.setVisibility(View.GONE);
                if (position==0){
                    myHolder.binding.iconRanklistCourseVer.setVisibility(View.VISIBLE);
                    GlideLoadUtils.load(mContext, R.mipmap.rankinglist1,myHolder.binding.iconRanklistCourseVer);
                }else if (position==1){
                    myHolder.binding.iconRanklistCourseVer.setVisibility(View.VISIBLE);
                    GlideLoadUtils.load(mContext, R.mipmap.rankinglist2,myHolder.binding.iconRanklistCourseVer);
                }else if (position==2){
                    myHolder.binding.iconRanklistCourseVer.setVisibility(View.VISIBLE);
                    GlideLoadUtils.load(mContext, R.mipmap.rankinglist3,myHolder.binding.iconRanklistCourseVer);
                }else {
                    myHolder.binding.iconRanklistCourseVer.setVisibility(View.GONE);
                }
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_COLLECTED)){
                myHolder.binding.iconRanklistCourseVer.setVisibility(View.GONE);
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_SHARE)){
                //状态 0待审核 1通过 2拒绝
                if (Utils.getString(bean.getStatus()).equals("0"))
                    GlideLoadUtils.load(mContext, R.mipmap.share_passing,myHolder.binding.sharePassState);
                else if (Utils.getString(bean.getStatus()).equals("1"))
                    GlideLoadUtils.load(mContext, R.mipmap.share_passed,myHolder.binding.sharePassState);
                else
                    GlideLoadUtils.load(mContext, R.mipmap.share_refuse,myHolder.binding.sharePassState);
            }

            myHolder.binding.titleCourseVer.setText(Utils.getString(bean.getName()));
            myHolder.binding.teacherFlagCourseVer.setText(Utils.getString(bean.getTitle()));
            myHolder.binding.teacherNameCourseVer.setText(Utils.getString(bean.getTeacherName()));
            if (Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_COURSE_COMMENTCOUNT)){//评论次数
                if (Utils.isEmpty(bean.getRemarkNum()))
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.comment_count),
                            ""+0));
                else
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.comment_count),
                            Utils.getString(bean.getRemarkNum())));
            }else {
                if (Utils.isEmpty(bean.getStuNum()))
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.look_count),
                            ""+0));
                else
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.look_count),
                            Utils.getString(bean.getStuNum())));
            }
            if (Utils.getString(typeView).equals(TYPE_VIEW_SHARE)){//分享管理
                GlideLoadUtils.load(mContext, bean.getCoverImgUrl(), myHolder.binding.coverCourseVer, GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                myHolder.binding.titleCourseVer.setText(Utils.getString(bean.getTitle()));
                if (!Utils.isEmpty(bean.getCreateDate())){
                    String remarkTime =bean.getCreateDate();
                    if (remarkTime.length()>11){//证明后面有时分秒
                        StringBuffer s = new StringBuffer(remarkTime);
                        s.delete(remarkTime.length()-8,remarkTime.length());
                        myHolder.binding.teacherFlagCourseVer.setText(s);
                    }else {
                        myHolder.binding.teacherFlagCourseVer.setText(Utils.getString(bean.getCreateDate()));
                    }
                }
                Drawable drawable = mContext.getResources().getDrawable(
                        R.mipmap.time_championsharing);
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                myHolder.binding.teacherFlagCourseVer.setCompoundDrawables(drawable, null, null, null);
                myHolder.binding.teacherFlagCourseVer.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px8));
                myHolder.binding.teacherNameCourseVer.setText(Utils.getString(bean.getContent()));
                String championShareTitle =Utils.isEmpty(bean.getTitle())?"":bean.getTitle();
                if (championShareTitle.length()>8){
                    StringBuffer s = new StringBuffer(championShareTitle);
                    s.insert(7, "\n");
                    myHolder.binding.championShareTitle.setText(s);
                }else {
                    myHolder.binding.championShareTitle.setText(championShareTitle);
                }
            }
            if (Utils.getString(typeView).equals(TYPE_VIEW_STUDY_COURSE)){
                if (Utils.isEmpty(bean.getStuNum()))
                    myHolder.binding.teacherNameCourseVer.setText(String.format(mContext.getResources().getString(R.string.look_count),
                            ""+0));
                else
                    myHolder.binding.teacherNameCourseVer.setText(String.format(mContext.getResources().getString(R.string.look_count),
                        Utils.getString(bean.getStuNum())));
                myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.study_minute),
                        ""+(bean.getStudyLength()/60)));
            }
            if (Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_TESTNOPASS_COURSE)){
                if (Utils.isEmpty(bean.getNoPassNum()))
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.count_test_nopass),
                            ""+0));
                else
                    myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getResources().getString(R.string.count_test_nopass),
                            Utils.getString(bean.getNoPassNum())));
            }
            if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP_COURSE)){
                int playPercent;
                if (Utils.isEmpty(bean.getPlayPercent()))
                    playPercent =0;
                else
                    playPercent = (int) (Double.parseDouble(bean.getPlayPercent())*100);
                myHolder.binding.countLookkCourseVer.setText(String.format(mContext.getString(R.string.studyed_minute),playPercent+"%"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList==null || mList.size()==0)? 0 : mList.size()+1;
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private ItemCourseVerticalBinding binding;

        public MyViewHodler(@NonNull ItemCourseVerticalBinding binding) {
            super(binding.getRoot());
            this.binding =binding;

            if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP_COURSE)){
                binding.layoutCourseVer.setBackground(mContext.getDrawable(R.drawable.bianxian12_b2_white));
                binding.iconRanklistCourseVer.setVisibility(View.GONE);
                binding.layoutBottomTest.setVisibility(View.GONE);
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_COURSE_CATALOG)){
                binding.layoutCourseVer.setBackground(mContext.getDrawable(R.drawable.bianxian12_b2_white));
                binding.layoutBottomTest.setVisibility(View.VISIBLE);
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_COLLECTED)){
                binding.layoutBottomTest.setVisibility(View.GONE);
            } else if (Utils.getString(typeView).equals(TYPE_VIEW_SHARE)){
                binding.sharePassState.setVisibility(View.VISIBLE);
                binding.championShareTitle.setVisibility(View.VISIBLE);
                binding.layoutBottomTest.setVisibility(View.GONE);
                binding.iconRanklistCourseVer.setVisibility(View.GONE);
                binding.countLookkCourseVer.setVisibility(View.GONE);
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_STUDY_COURSE)){
                binding.iconRanklistCourseVer.setVisibility(View.GONE);
                binding.layoutBottomTest.setVisibility(View.GONE);
                binding.teacherFlagCourseVer.setVisibility(View.INVISIBLE);
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_RANKLIST_TESTNOPASS_COURSE)){
                binding.layoutBottomTest.setVisibility(View.GONE);
                binding.layoutCourseVertical.setBackground(null);
            } else{
                binding.layoutCourseVer.setBackground(mContext.getDrawable(R.color.white));
                binding.layoutBottomTest.setVisibility(View.GONE);
            }

            Utils.setImageViewTint(mContext,binding.iconTest);
            binding.testCourseVer.setTextColor(Utils.getThemeColor(mContext));
        }
    }

    protected void onBindData(MyViewHodler holder,int position) {
        holder.binding.delete.setTag(position);
        if (!holder.binding.delete.hasOnClickListeners()) {
            holder.binding.delete.setOnClickListener(v -> {
                if (onClickCallBack != null) {
                    onClickCallBack.onDeleteClick(position);
                }
            });
        }
    }
}

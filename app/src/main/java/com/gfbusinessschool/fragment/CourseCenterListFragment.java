package com.gfbusinessschool.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.CourseCenterActivity;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.bean.BaseClassifyEntity;
import com.gfbusinessschool.bean.CourseClassifyEntity;
import com.gfbusinessschool.databinding.FragmentCoursecenterListBinding;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 课程中心/冠军分享
 */
public class CourseCenterListFragment extends BaseFragment<FragmentCoursecenterListBinding> {
    /**
     * 必修，精选，冠军，音频
     */
    private String typeCourse;
    private  List<BaseClassifyEntity> classifyEntityList;


    public void setTypeCourse(String typeCourse) {
        this.typeCourse = typeCourse;
    }

    public void setClassifyEntityList(List<BaseClassifyEntity> classifyEntityList) {
        this.classifyEntityList = classifyEntityList;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.tabLayoutCourseClassify.setSelectedTabIndicatorColor(
                Utils.getThemeColor(getContext()));
        viewBinding.nodataCourseCenter.tvNodata.setText(getString(R.string.noData));
    }

    @Override
    protected void initData() {
        super.initData();
        if (Utils.getString(typeCourse).equals(CourseCenterActivity.TYPE_COURSE_CHAMPION_SHARING)){
            if (classifyEntityList==null || classifyEntityList.size()==0) {
                viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                viewBinding.viewPagerCourseClassify.setVisibility(View.GONE);
                return;
            }
            viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.GONE);
            viewBinding.viewPagerCourseClassify.setVisibility(View.VISIBLE);
            List<Fragment> fragmentList =new ArrayList<>();
            String[] arrTitle =null;
            if (classifyEntityList.size()==0){
                arrTitle =new String[1];
                arrTitle[0] ="";
                fragmentList.add(new CourseFragment(null,typeCourse));
                viewBinding.tabLayoutCourseClassify.setVisibility(View.GONE);
            }else {
                arrTitle =new String[classifyEntityList.size()];
                for (int i=0;i<classifyEntityList.size();i++){
                    BaseClassifyEntity bean =classifyEntityList.get(i);
                    fragmentList.add(new CourseFragment(bean,typeCourse));
                    arrTitle[i] =bean.getName();
                }
            }
            viewBinding.viewPagerCourseClassify.setAdapter(new ClassDetailPagerAdapter(getChildFragmentManager(),fragmentList,arrTitle));
            viewBinding.tabLayoutCourseClassify.setupWithViewPager(viewBinding.viewPagerCourseClassify);
        }else if (Utils.getString(typeCourse).equals(CourseCenterActivity.TYPE_COURSE_MUST_STUDY)){
            NetWorkUtils.getResultArray(getContext(), InterfaceClass.COURSE_MUST_STUDY, new HashMap<>(), new NetWorkCallback() {
                @Override
                public void onResponse(String code, String response) {
                    if (Utils.getString(code).equals("200")) {
                        List<BaseClassifyEntity> classifyEntityList = JSONArray.parseArray(response, BaseClassifyEntity.class);
                        if (classifyEntityList == null) return;
                        List<Fragment> fragmentList = new ArrayList<>();
                        String[] arrTitle = null;
                        if (classifyEntityList.size() == 0) {
                            arrTitle = new String[1];
                            arrTitle[0] = "";
                            fragmentList.add(new CourseFragment(null, typeCourse));
                            viewBinding.tabLayoutCourseClassify.setVisibility(View.GONE);
                        } else {
                            arrTitle = new String[classifyEntityList.size()];
                            for (int i = 0; i < classifyEntityList.size(); i++) {
                                BaseClassifyEntity bean = classifyEntityList.get(i);
                                fragmentList.add(new CourseFragment(bean, typeCourse));
                                arrTitle[i] = bean.getName();
                            }
                        }
                        viewBinding.viewPagerCourseClassify.setAdapter(new ClassDetailPagerAdapter(getChildFragmentManager(), fragmentList, arrTitle));
                        viewBinding.tabLayoutCourseClassify.setupWithViewPager(viewBinding.viewPagerCourseClassify);
                    }
                }
            });
        } else if (Utils.getString(typeCourse).equals(CourseCenterActivity.TYPEVIEW_AUDIO)){
            if (classifyEntityList==null || classifyEntityList.size()==0) {
                viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                viewBinding.viewPagerCourseClassify.setVisibility(View.GONE);
                return;
            }
            viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.GONE);
            viewBinding.viewPagerCourseClassify.setVisibility(View.VISIBLE);
            List<Fragment> fragmentList =new ArrayList<>();
            String[] arrTitle =new String[classifyEntityList.size()];
            for (int i=0;i<classifyEntityList.size();i++){
                BaseClassifyEntity bean =classifyEntityList.get(i);
                AudioFragment audioFragment =new AudioFragment();
                Bundle bundle =new Bundle();
                bundle.putString("classifyId",bean.getId());
                audioFragment.setArguments(bundle);
                fragmentList.add(audioFragment);
                arrTitle[i] =bean.getName();
            }
            viewBinding.viewPagerCourseClassify.setAdapter(new ClassDetailPagerAdapter(getChildFragmentManager(),fragmentList,arrTitle));
            viewBinding.tabLayoutCourseClassify.setupWithViewPager(viewBinding.viewPagerCourseClassify);
        }else {//精选
            if (classifyEntityList == null || classifyEntityList.size()==0) {
                viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                viewBinding.viewPagerCourseClassify.setVisibility(View.GONE);
                return;
            }
            viewBinding.nodataCourseCenter.noDataLayout.setVisibility(View.GONE);
            viewBinding.viewPagerCourseClassify.setVisibility(View.VISIBLE);
            List<Fragment> fragmentList = new ArrayList<>();
            String[] arrTitle = new String[classifyEntityList.size()];
            for (int i = 0; i < classifyEntityList.size(); i++) {
                BaseClassifyEntity bean = classifyEntityList.get(i);
                fragmentList.add(new CourseFragment(bean, typeCourse));
                arrTitle[i] = bean.getName();
            }
            viewBinding.viewPagerCourseClassify.setAdapter(new ClassDetailPagerAdapter(getChildFragmentManager(), fragmentList, arrTitle));
            viewBinding.tabLayoutCourseClassify.setupWithViewPager(viewBinding.viewPagerCourseClassify);
        }
    }
}

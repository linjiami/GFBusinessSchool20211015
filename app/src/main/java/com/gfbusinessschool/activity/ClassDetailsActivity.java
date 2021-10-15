package com.gfbusinessschool.activity;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.ActivityCoursedetailsBinding;
import com.gfbusinessschool.fragment.ClassCatalogFragment;
import com.gfbusinessschool.fragment.CourseIntroduceFragment;
import com.gfbusinessschool.fragment.CourseWareFragment;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题详情
 */

@Route(path = ARouterPath.ClassDetailsActivity)
public class ClassDetailsActivity extends BaseActivity<ActivityCoursedetailsBinding>{
    public static final String TYPEVIEW_COURSE_LIST ="必修专题";
    public static final String TYPEVIEW_COURSE_LIST_SPECIAL ="精选专题";
    public static final String TYPEVIEW_CLASS_STUDYMAP ="学习地图专题";
   private List<Fragment> fragmentList =new ArrayList<>();
    @Autowired
    String classId;
    @Autowired
    String typeView;

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setTranslucentForImageView(ClassDetailsActivity.this,0,viewBinding.playPlaceHoldLayout);
    }

    @Override
    protected void initView() {
        super.initView();
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_LIST)){
            if (Utils.isEmpty(classId)){
                ToastUtil.show(getApplicationContext(), getString(R.string.special_id_null_alert));
                finish();
            }
        }
        if (Utils.getString(typeView).equals(TYPEVIEW_CLASS_STUDYMAP)){
            viewBinding.iconCollect.setVisibility(View.GONE);
        }
        viewBinding.playIcon.setVisibility(View.GONE);
        viewBinding.iconCollect.setVisibility(View.GONE);
        viewBinding.detailPlayer.setVisibility(View.INVISIBLE);
        viewBinding.introductionTabCourse.setSelectedTabIndicatorColor(Utils.getThemeColor(getApplicationContext()));

        int width = MyApplication.getInstance().screenWidth;
        viewBinding.detailPlayer.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,width*9/16));
        String[] arrTitle = getResources().getStringArray(R.array.classTabArray);
        CourseIntroduceFragment introduceFragment =new CourseIntroduceFragment();
        if (Utils.getString(typeView).equals(TYPEVIEW_CLASS_STUDYMAP))
            introduceFragment.setTypeView(CourseIntroduceFragment.TYPE_VIEW_CLASS_STUDYMAP);
        else
            introduceFragment.setTypeView(CourseIntroduceFragment.TYPE_VIEW_CLASS);
        fragmentList.add(introduceFragment);
        fragmentList.add(new ClassCatalogFragment(typeView,classId));
        CourseWareFragment courseWareFragment =new CourseWareFragment();
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_LIST))
            courseWareFragment.setTypeView(CourseWareFragment.TYPEVIEW_CLASS);
        else if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_LIST_SPECIAL))
            courseWareFragment.setTypeView(CourseWareFragment.TYPEVIEW_CLASS_SPECIAL);
        else if (Utils.getString(typeView).equals(TYPEVIEW_CLASS_STUDYMAP))
            courseWareFragment.setTypeView(CourseWareFragment.TYPEVIEW_STUDYMAP);
        courseWareFragment.setId(classId);
        fragmentList.add(courseWareFragment);

        viewBinding.viewPagerCourse.setAdapter(new ClassDetailPagerAdapter(getSupportFragmentManager(),fragmentList,arrTitle));
        viewBinding.introductionTabCourse.setupWithViewPager(viewBinding.viewPagerCourse);
        viewBinding.viewPagerCourse.setCurrentItem(1);
        viewBinding.iconBack.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Map<String,String> map =new HashMap<>();
        String url;
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_LIST)){
            url =InterfaceClass.CLASS_DETAILS_BIXIU;
            map.put("requiredSubjectId",classId);
        }else if (Utils.getString(typeView).equals(TYPEVIEW_CLASS_STUDYMAP)){
            url =InterfaceClass.STUDYMAP_CLASS_DETAILS;
            map.put("specialSubjectId",classId);
        }else {
            url =InterfaceClass.CLASS_DETAILS_JINGXUAN;
            map.put("selectedSubjectId",classId);
        }
        NetWorkUtils.getRequest(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    CourseBean courseBean = JSONObject.parseObject(response,CourseBean.class);
                    if (courseBean==null) return;
                    GlideLoadUtils.load(getApplicationContext(),courseBean.getLogoUrl(),viewBinding.coverCourse,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    if (fragmentList!=null && fragmentList.size()>0){
                        CourseIntroduceFragment introduceFragment = (CourseIntroduceFragment) fragmentList.get(0);
                        if (introduceFragment!=null) introduceFragment.setCourseDetailsBean(courseBean);
                    }
                    if (fragmentList!=null && fragmentList.size()>1){
                        ClassCatalogFragment classCatalogFragment = (ClassCatalogFragment) fragmentList.get(1);
                        if (classCatalogFragment!=null) classCatalogFragment.setCourseBean(courseBean);
                    }
                    if (fragmentList!=null && fragmentList.size()>2){
                        CourseWareFragment courseWareFragment = (CourseWareFragment) fragmentList.get(2);
                        if (courseWareFragment!=null) courseWareFragment.setCourseBean(courseBean);
                    }
                }
            }
        });
    }

}
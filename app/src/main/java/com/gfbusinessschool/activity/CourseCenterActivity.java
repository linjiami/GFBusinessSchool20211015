package com.gfbusinessschool.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.ResultCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.BaseClassifyEntity;
import com.gfbusinessschool.fragment.CourseCenterListFragment;
import com.gfbusinessschool.fragment.StaffFragment;
import com.gfbusinessschool.bean.PositionEntity;
import com.gfbusinessschool.databinding.ActivityCoursecenterBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程中心/员工列表/冠军分享
 */
@Route(path = ARouterPath.ACTIVITY_CourseCenter)
public class CourseCenterActivity extends BaseActivity<ActivityCoursecenterBinding> {
    public static final String TYPE_COURSE_CHAMPION_SHARING ="冠军分享";
    public static final String TYPE_COURSE_MUST_STUDY ="必修课程";
    public static final String TYPE_COURSE_SPECIAL ="精选课程";
    public static final String TYPEVIEW_STAFF ="员工列表";
    public static final String TYPEVIEW_AUDIO ="音频中心";
    private ArrayList<Fragment> fragmentList =new ArrayList<>();//FrameLayout集合
    private ArrayList<RadioButton> radioButtonsList =new ArrayList<>();//FrameLayout集合
    private FragmentManager fragmentManager;
    private int lastFragmentIndex;
    @Autowired
    String typeView;
    @Autowired
    String storeId;
    @Autowired
    String title;

    @Override
    protected void initView() {
        fragmentManager =getSupportFragmentManager();
        viewBinding.titleBarCourseCenter.setTitle(Utils.getString(title));
        viewBinding.layoutNoDataCourseCenter.tvNodata.setText(getString(R.string.noData));
    }

    @Override
    protected void initData() {
        super.initData();
        if (Utils.getString(typeView).equals(TYPE_COURSE_CHAMPION_SHARING)){
            getChampionSharingClassify();
        }else if (Utils.getString(typeView).equals(TYPEVIEW_STAFF)){
            if (Utils.isEmpty(storeId)) return;
            getStaffClassify();
        }else if (Utils.getString(typeView).equals(TYPEVIEW_AUDIO)){
            getAudioClassify();
        }else {
            getCourseCenterClassify();
        }
    }

    //四个按钮对应点击事件
    private void showFragment(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentList.get(position).isAdded()){
            if (position==lastFragmentIndex) return;
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.show(fragmentList.get(position)).hide(fragmentList.get(lastFragmentIndex)).commitAllowingStateLoss();
            else
                transaction.show(fragmentList.get(position)).commitAllowingStateLoss();
        }else {
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.add(R.id.personnelFL,fragmentList.get(position)).hide(fragmentList.get(lastFragmentIndex)).commitAllowingStateLoss();
            else
                transaction.add(R.id.personnelFL,fragmentList.get(position)).commitAllowingStateLoss();
        }
        lastFragmentIndex =position;
    }

    /**
     * 获取员工分类
     */
    private void getStaffClassify() {
        Map<String,String> map =new HashMap<>();
        map.put("storeId",storeId);
        NetWorkUtils.getPositionList(getApplicationContext(),map, new ResultCallBack() {
            @Override
            public void onResult(List<PositionEntity> list) {
                if (list==null || list.size()==0) {
                    viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                    viewBinding.personnelFL.setVisibility(View.GONE);
                    viewBinding.scrollViewLeft.setVisibility(View.GONE);
                    return;
                }
                viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.GONE);
                viewBinding.personnelFL.setVisibility(View.VISIBLE);
                viewBinding.scrollViewLeft.setVisibility(View.VISIBLE);
                String[] courseCenterArray =new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    courseCenterArray[i] = list.get(i).getPositionName();
                }
                for (int i=0;i<list.size();i++){
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.height = (int) getResources().getDimension(R.dimen.px120);
                    RadioButton tv = new RadioButton(getApplicationContext());
                    tv.setGravity(Gravity.CENTER);
                    tv.setText(courseCenterArray[i]);
                    tv.setTextSize(13);
                    tv.setTextColor(getResources().getColorStateList(R.color.class_personnel_store));
                    tv.setMaxEms(10);
                    tv.setBackgroundResource(R.drawable.class_a);
                    tv.setButtonDrawable(null);
                    tv.setLayoutParams(layoutParams);
                    tv.setPadding(10,0,10,0);
                    viewBinding.personnelRG.addView(tv, layoutParams);
                    radioButtonsList.add(tv);
                    int finalI = i;
                    tv.setOnClickListener(v -> {
                        showFragment(finalI);
                    });
                    radioButtonsList.get(0).setChecked(true);
                    StaffFragment staffFragment =new StaffFragment(list.get(i).getId(),storeId);
                    fragmentList.add(staffFragment);

                }
                showFragment(lastFragmentIndex);
                if (radioButtonsList.size()!=0)  radioButtonsList.get(lastFragmentIndex).setChecked(true);
            }
        });
    }

    /**
     * 获取课程中心分类
     */
    private void getCourseCenterClassify() {
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.COURSECENTER_CLASSIFY, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")) {
                    List<BaseClassifyEntity> list = JSONArray.parseArray(response, BaseClassifyEntity.class);
                    if (list == null || list.size() == 0) {
                       list =new ArrayList<>();
                    }
                    list.add(0,new BaseClassifyEntity("必修课程",true,TYPE_COURSE_MUST_STUDY));//添加必修专题
                    for (int i = 0; i < list.size(); i++) {
                        BaseClassifyEntity entity =list.get(i);
                        if (entity==null) return;
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.height = (int) getResources().getDimension(R.dimen.px120);
                        RadioButton tv = new RadioButton(getApplicationContext());
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(entity.getName());
                        tv.setTextSize(13);
                        tv.setTextColor(getResources().getColorStateList(R.color.class_personnel_store));
                        tv.setMaxEms(10);
                        tv.setBackgroundResource(R.drawable.class_a);
                        tv.setButtonDrawable(null);
                        tv.setLayoutParams(layoutParams);
                        tv.setPadding(10, 0, 10, 0);
                        viewBinding.personnelRG.addView(tv, layoutParams);
                        radioButtonsList.add(tv);
                        int finalI = i;
                        tv.setOnClickListener(v -> {
                            showFragment(finalI);
                        });
                        radioButtonsList.get(0).setChecked(true);
                        CourseCenterListFragment fragment = new CourseCenterListFragment();
                        if (entity.isMustStudy()) {
                            fragment.setTypeCourse(TYPE_COURSE_MUST_STUDY);
                        } else {
                            fragment.setTypeCourse(TYPE_COURSE_SPECIAL);
                        }
                        fragment.setClassifyEntityList(entity.getChildrenList());
                        fragmentList.add(fragment);
                    }
                    showFragment(lastFragmentIndex);
                    if (radioButtonsList.size() != 0)
                        radioButtonsList.get(lastFragmentIndex).setChecked(true);
                }
            }
        });
    }

    /**
     * 冠军分享分类
     */
    private void getChampionSharingClassify(){
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.CHAMPIONSHARING_CLASSIFY, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    List<BaseClassifyEntity> listClassify = JSONArray.parseArray(response, BaseClassifyEntity.class);
                    if (listClassify ==null || listClassify.size()==0) {
                        viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.personnelFL.setVisibility(View.GONE);
                        viewBinding.scrollViewLeft.setVisibility(View.GONE);
                    }else {
                        viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.GONE);
                        viewBinding.personnelFL.setVisibility(View.VISIBLE);
                        viewBinding.scrollViewLeft.setVisibility(View.VISIBLE);
                        String[] courseCenterArray =new String[listClassify.size()];
                        for (int i=0; i<listClassify.size(); i++){
                            courseCenterArray[i] =listClassify.get(i).getName();
                        }
                        for (int i=0;i<courseCenterArray.length;i++){
                            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.height =(int) getResources().getDimension(R.dimen.px120);
                            RadioButton tv = new RadioButton(getApplicationContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setText(courseCenterArray[i]);
                            tv.setTextSize(13);
                            tv.setTextColor(getResources().getColorStateList(R.color.class_personnel_store));
                            tv.setMaxEms(10);
                            tv.setBackgroundResource(R.drawable.class_a);
                            tv.setButtonDrawable(null);
                            tv.setLayoutParams(layoutParams);
                            tv.setPadding(10,0,10,0);
                            viewBinding.personnelRG.addView(tv, layoutParams);
                            radioButtonsList.add(tv);
                            int finalI = i;
                            tv.setOnClickListener(v -> {
                                showFragment(finalI);
                            });
                            radioButtonsList.get(0).setChecked(true);
                            CourseCenterListFragment fragment =new CourseCenterListFragment();
                            fragment.setTypeCourse(typeView);
                            fragment.setClassifyEntityList(listClassify.get(i).getChildrenList());
                            fragmentList.add(fragment);

                        }
                        showFragment(lastFragmentIndex);
                        if (radioButtonsList.size()!=0)  radioButtonsList.get(lastFragmentIndex).setChecked(true);
                    }
                }
            }
        });

    }

    private void getAudioClassify(){
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.AUDIO_CLASSIFY, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    List<BaseClassifyEntity> listClassify = JSONArray.parseArray(response, BaseClassifyEntity.class);
                    if (listClassify ==null || listClassify.size()==0) {
                        viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.personnelFL.setVisibility(View.GONE);
                        viewBinding.scrollViewLeft.setVisibility(View.GONE);
                    }else {
                        viewBinding.layoutNoDataCourseCenter.noDataLayout.setVisibility(View.GONE);
                        viewBinding.personnelFL.setVisibility(View.VISIBLE);
                        viewBinding.scrollViewLeft.setVisibility(View.VISIBLE);
                        String[] courseCenterArray =new String[listClassify.size()];
                        for (int i=0; i<listClassify.size(); i++){
                            courseCenterArray[i] =listClassify.get(i).getName();
                        }
                        for (int i=0;i<courseCenterArray.length;i++){
                            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.height =(int) getResources().getDimension(R.dimen.px120);
                            RadioButton tv = new RadioButton(getApplicationContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setText(courseCenterArray[i]);
                            tv.setTextSize(13);
                            tv.setTextColor(getResources().getColorStateList(R.color.class_personnel_store));
                            tv.setMaxEms(10);
                            tv.setBackgroundResource(R.drawable.class_a);
                            tv.setButtonDrawable(null);
                            tv.setLayoutParams(layoutParams);
                            tv.setPadding(10,0,10,0);
                            viewBinding.personnelRG.addView(tv, layoutParams);
                            radioButtonsList.add(tv);
                            int finalI = i;
                            tv.setOnClickListener(v -> {
                                showFragment(finalI);
                            });
                            radioButtonsList.get(0).setChecked(true);
                            CourseCenterListFragment fragment =new CourseCenterListFragment();
                            fragment.setTypeCourse(typeView);
                            fragment.setClassifyEntityList(listClassify.get(i).getChildrenList());
                            fragmentList.add(fragment);

                        }
                        showFragment(lastFragmentIndex);
                        if (radioButtonsList.size()!=0)  radioButtonsList.get(lastFragmentIndex).setChecked(true);
                    }
                }
            }
        });
    }
}

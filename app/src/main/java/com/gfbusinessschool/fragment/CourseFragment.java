package com.gfbusinessschool.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.BaseActivity;
import com.gfbusinessschool.activity.ClassDetailsActivity;
import com.gfbusinessschool.activity.CourseCenterActivity;
import com.gfbusinessschool.activity.CourseDetailsActivity;
import com.gfbusinessschool.activity.CourseListActivity;
import com.gfbusinessschool.adapter.CourseCenterVerticalAdapter;
import com.gfbusinessschool.bean.BaseClassifyEntity;
import com.gfbusinessschool.bean.ChampionShareListBean;
import com.gfbusinessschool.bean.ChampionSharingEntity;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.CourseClassifyEntity;
import com.gfbusinessschool.databinding.FragmentCourseBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseFragment extends BaseFragment<FragmentCourseBinding> implements SwipeRefreshLayout.OnRefreshListener{

    private CourseCenterVerticalAdapter courseAdapter;
    private BaseClassifyEntity courseClassifyEntity;
    private List<CourseBean> listCourse =new ArrayList<>();
    private int currentPage;
    private String courseType;//必修课程/精选课程

    public CourseFragment() {
    }

    public CourseFragment(BaseClassifyEntity courseClassifyEntity, String courseType) {
        this.courseClassifyEntity = courseClassifyEntity;
        this.courseType = courseType;
    }

    @Override
    protected void initView() {
        viewBinding.courseNoData.tvNodata.setText("暂无专题");
        viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recyclerViewCourse.setLayoutManager(layoutManage);
        viewBinding.swipeCourse.setOnRefreshListener(this);

        viewBinding.nestedScrollViewCourse.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( courseAdapter!=null)
                    courseAdapter.setProgressBarVisiable(true);
                currentPage++;
                requestCourseListData(currentPage);
            }
        });

        courseAdapter =new CourseCenterVerticalAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                CourseBean bean =listCourse.get(position);
                if (bean!=null){
                    if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_CHAMPION_SHARING)){
                        ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                .withString("courseId",listCourse.get(position).getId())
                                .withString("typeView", CourseDetailsActivity.TYPE_VIEW_GUANJUN_SHARE).navigation();
                    }else {
                        if (bean.getType()==1){//类型(1专题2考核)
                            String _type ;
                            if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_MUST_STUDY))
                                _type = ClassDetailsActivity.TYPEVIEW_COURSE_LIST;
                            else
                                _type = ClassDetailsActivity.TYPEVIEW_COURSE_LIST_SPECIAL;
                            ARouter.getInstance().build(ARouterPath.ClassDetailsActivity)
                                    .withString("typeView",_type)
                                    .withString("classId",listCourse.get(position).getId()).navigation();
                        }else {
                            jumpToTestActivity(bean,position);
                        }
                    }
                }
            }

            @Override
            public void onClickTest(int position) {
                CourseBean bean =listCourse.get(position);
                if (bean!=null){
                    jumpToTestActivity(bean,position);
                }
            }
        });
        if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_CHAMPION_SHARING))
            courseAdapter.setChampionShare(true);
        viewBinding.recyclerViewCourse.setAdapter(courseAdapter);
    }

    @Override
    protected void initData() {
        if (courseClassifyEntity==null) {
            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
            viewBinding.recyclerViewCourse.setVisibility(View.GONE);
            return;
        }
        currentPage =1;
        requestCourseListData(currentPage);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        currentPage=1;
        requestCourseListData(currentPage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.REQUEST_CODE_TEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            //刷新考试通过课程
            int index = data.getIntExtra("index",-1);
            if (index!=-1 && listCourse.size()>=index+1){
                listCourse.get(index).setIsPass("1");
                courseAdapter.notifyDataSetChanged();
            }
        }
    }

    private void jumpToTestActivity(CourseBean bean, int position) {
        Map<String,String> map =new HashMap<>();
        String _courseType ="";
        if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_MUST_STUDY))// 课程类型（1必修2精选）
            _courseType ="1";
        else
            _courseType ="2";
        map.put("courseType",_courseType);
        map.put("testId",bean.getTestId());
        //testType：考试类型（1专题考核2单课考核3综合考核）
        String _testType ="";
        if (bean.getType()==1) {//类型(1专题2考核)
            _testType ="1";
        }else if (bean.getType()==2){
            _testType ="3";
        }
        map.put("testType",_testType);
        map.put("rId",bean.getId());
        String final_courseType = _courseType;
        String final_testType = _testType;
        NetWorkUtils.getRequest(getContext(), InterfaceClass.TEST_GET_QUESTION, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_Test)
                            .withInt("testPoistion",position)
                            .withString("courseType", final_courseType)
                            .withString("testType", final_testType)//考试类型（1专题考核2单课考核3综合考核）
                            .withString("rId", bean.getId())//关联id(testType=1专题id,testType=2单课id,testType=3分类id)
                            .withString("testId",bean.getTestId())
                            .withString("testName",bean.getName())
                            .navigation(getActivity(), BaseActivity.REQUEST_CODE_TEST);
                }
            }
        });
    }

    private void requestCourseListData(int page) {
        if (page==1 && listCourse !=null) {
            listCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        map.put("currPage",page+"");
        String url=null;
        if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_MUST_STUDY)){
            url = InterfaceClass.COURSE_MUST_SPECIAL;
            if (courseClassifyEntity!=null && !Utils.isEmpty(courseClassifyEntity.getId()))
                map.put("classifyId",courseClassifyEntity.getId());
            getCourseList(page,url,map);
        }else if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_SPECIAL)){
            url = InterfaceClass.COURSE_SPECIAL_SELECTED_SPECIAL;
            if (courseClassifyEntity!=null && !Utils.isEmpty(courseClassifyEntity.getId()))
                map.put("classifyId",courseClassifyEntity.getId());
            getCourseList(page,url,map);
        }else if (Utils.getString(courseType).equals(CourseCenterActivity.TYPE_COURSE_CHAMPION_SHARING)){
            url = InterfaceClass.CHAMPIONSHARING_LIST;
            map.put("secondTagId",courseClassifyEntity.getId());
            getChampionShareList(page,url,map);
        }

    }

    private void getCourseList(int page, String url, Map<String,String> map) {
        NetWorkUtils.getResultArray(getContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<CourseBean> list = JSONArray.parseArray(response, CourseBean.class);
                    if (list.size() < 15) {
                        courseAdapter.setProgressBarVisiable(false);
                    } else {
                        courseAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.recyclerViewCourse.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerViewCourse.setVisibility(View.VISIBLE);
                        }
                        listCourse =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (CourseBean bean : list){
                            listCourse.add(bean);
                        }
                    }
                    courseAdapter.setmList(listCourse);
                    courseAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getChampionShareList(int page, String url, Map<String,String> map) {
        NetWorkUtils.getRequest(getContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    ChampionShareListBean shareListBean = JSONObject.parseObject(response,ChampionShareListBean.class);
                    if (shareListBean==null) return;
                    if (shareListBean.getRecords()==null) shareListBean.setRecords(new ArrayList<>());
                    List<CourseBean> list = shareListBean.getRecords();
                    if (list.size() < 15) {
                        courseAdapter.setProgressBarVisiable(false);
                    } else {
                        courseAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.recyclerViewCourse.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerViewCourse.setVisibility(View.VISIBLE);
                        }
                        listCourse =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (CourseBean bean : list){
                            listCourse.add(bean);
                        }
                    }
                    courseAdapter.setmList(listCourse);
                    courseAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

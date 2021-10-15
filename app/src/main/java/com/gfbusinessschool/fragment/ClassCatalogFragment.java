package com.gfbusinessschool.fragment;

import android.content.Intent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.BaseActivity;
import com.gfbusinessschool.activity.ClassDetailsActivity;
import com.gfbusinessschool.activity.CourseDetailsActivity;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentCourseCommentBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题目录
 */
public class ClassCatalogFragment extends BaseFragment<FragmentCourseCommentBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private String typeView;
    private String classId;
    private CourseVerticalAdapter courseAdapter;
    private List<CourseBean> mListCourse = new ArrayList<>();
    private int currentPage;

    public ClassCatalogFragment(String typeView, String classId) {
        this.typeView = typeView;
        this.classId = classId;
    }

    public void setCourseBean(CourseBean courseBean) {
        if (courseBean==null) return;
        viewBinding.layoutCourseTop.courseTitle.setText(Utils.getString(courseBean.getName()));
        if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_CLASS_STUDYMAP)){
            viewBinding.layoutCourseTop.countCourse.setText(String.format(getString(R.string.place_course_count2),
                    Utils.getIntData(courseBean.getCourseNum())+""));
        }else {
            viewBinding.layoutCourseTop.countCourse.setText(String.format(getString(R.string.place_course_count),
                    Utils.getIntData(courseBean.getCourseNum())+"",Utils.getIntData(courseBean.getStuNum())+""));
        }
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.commentTitle.setText(getString(R.string.course_list));
        viewBinding.layoutSendComment.setVisibility(View.GONE);
        viewBinding.swipeRefreshComment.setOnRefreshListener(this);
        viewBinding.rvComment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (courseAdapter!=null)
                    courseAdapter.setProgressBarVisiable(true);
                currentPage++;
                getCourseListData(currentPage);
            }
        });
        courseAdapter =new CourseVerticalAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_CLASS_STUDYMAP))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                            .withString("typeView", CourseDetailsActivity.TYPE_VIEW_STUDYMAP)
                            .withString("courseId",mListCourse.get(position).getId())
                            .withString("classId",classId)
                            .navigation();
               else
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                            .withString("courseId",mListCourse.get(position).getId())
                            .navigation();
            }

            @Override
            public void onClickTest(int position) {
                //必修课程必须前面的课程全部考试通过才能进入开始
                boolean ispassTest =true;
                if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_COURSE_LIST)){
                    if (position!=0){
                        for (int i=0;i<position;i++){//是否通过(0未通过1已通过)
                            if (Utils.getString(mListCourse.get(i).getIsPass()).equals("0")){
                                ispassTest =false;
                                break;
                            }
                        }
                    }
                }
                if (ispassTest){
                    String courseType =null;//课程类型（1必修2精选）
                    if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_COURSE_LIST))
                        courseType ="1";
                    else if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_COURSE_LIST_SPECIAL))
                        courseType ="2";
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_Test)
                            .withInt("testPoistion",position)
                            .withString("courseType",courseType)
                            .withString("testType","2")//考试类型（1专题考核2单课考核3综合考核）
                            .withString("rId",mListCourse.get(position).getId())//关联id(testType=1专题id,testType=2单课id,testType=3分类id)
                            .withString("testId",mListCourse.get(position).getTestId())
                            .withString("testName",mListCourse.get(position).getName())
                            .navigation(getActivity(), BaseActivity.REQUEST_CODE_TEST);
                }else {
                    ToastUtil.show(getContext(),getString(R.string.alert_test));
                }
            }
        });
        if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_CLASS_STUDYMAP))//学习地图
            courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_STUDYMAP_COURSE);
        else
            courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_COURSE_CATALOG);
        viewBinding.rvComment.setAdapter(courseAdapter);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshComment.setRefreshing(true);
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.REQUEST_CODE_TEST && resultCode == getActivity().RESULT_OK && data != null) {
            //刷新考试通过课程
            int index = data.getIntExtra("index",-1);
            if (index!=-1){
                mListCourse.get(index).setIsPass("1");
                courseAdapter.notifyItemChanged(index);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage =1;
        getCourseListData(currentPage);
    }

    /**
     * 专题目录
     * @param page 0：不分页
     */
    private void getCourseListData(int page) {
        if (page==1) {
            mListCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        if (page!=0) map.put("currPage",""+page);
        String url=null;
        if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_COURSE_LIST)){//必修课程
            url = InterfaceClass.COURSE_MUST_SPECIAL_LIST;
            map.put("requiredSubjectId",classId);
        }else if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_COURSE_LIST_SPECIAL)){//精选
            url = InterfaceClass.COURSE_SPECIAL_SELECTED_LIST;
            map.put("selectedSubjectId",classId);
        }else if (Utils.getString(typeView).equals(ClassDetailsActivity.TYPEVIEW_CLASS_STUDYMAP)){//学习地图
            url = InterfaceClass.STUDYMAP_CLASS_CATALOG;
            map.put("specialSubjectId",classId);
        }
        if(url==null) return;
        NetWorkUtils.getResultArray(getContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    viewBinding.swipeRefreshComment.setRefreshing(false);
                    List<CourseBean> list = JSONArray.parseArray(response, CourseBean.class);
                    if (list.size() < 15) {
                        courseAdapter.setProgressBarVisiable(false);
                    } else {
                        courseAdapter.setProgressBarVisiable(true);
                    }
                    if (page==0){
                        mListCourse =list;
                        courseAdapter.setmList(mListCourse);
                        courseAdapter.notifyDataSetChanged();
                        return;
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false);
                        }else {
                            viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.GONE);
                            viewBinding.rvComment.setVisibility(View.VISIBLE);
                        }
                        mListCourse =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }else {
                            for (CourseBean bean : list){
                                mListCourse.add(bean);
                            }
                        }
                    }
                    courseAdapter.setmList(mListCourse);
                    courseAdapter.notifyDataSetChanged();
                }else {
                    showNullListView(true);
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshComment.setRefreshing(false);
        viewBinding.rvComment.setVisibility(View.GONE);
        viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.layoutNoDataComment.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.layoutNoDataComment.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.layoutNoDataComment.tvNodata.setText(getString(R.string.noData));
            viewBinding.layoutNoDataComment.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
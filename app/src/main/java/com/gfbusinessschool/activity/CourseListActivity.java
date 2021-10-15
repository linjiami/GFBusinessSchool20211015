package com.gfbusinessschool.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentCourseBinding;
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
 * 我的-浏览历史/我的-分享管理
 */
@Route(path = ARouterPath.ACTIVITY_CourseList)
public class CourseListActivity extends BaseActivity<FragmentCourseBinding> implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
    public static final String TYPEVIEW_COURSE_HISTORY ="我的-浏览历史";
    public static final String TYPEVIEW_COURSE_SHARE ="我的-分享管理";
    private int currentPage;
    private CourseVerticalAdapter courseAdapter;
    private List<CourseBean> mListCourse = new ArrayList<>();
    @Autowired
    String typeView =TYPEVIEW_COURSE_HISTORY;

    @Override
    protected void initView() {
        viewBinding.courseNoData.tvNodata.setText("暂无课程");
        viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
       if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_HISTORY)){
            viewBinding.titlebarCourse.setTitle(getString(R.string.look_history));
        }else  if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)){
            viewBinding.titlebarCourse.setTitle(getString(R.string.share_manager));
            viewBinding.bottomLayout.getRoot().setVisibility(View.VISIBLE);
            viewBinding.bottomLayout.releaseShareBtn.setOnClickListener(this);
            viewBinding.bottomLayout.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        }
        viewBinding.titlebarCourse.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recyclerViewCourse.setLayoutManager(layoutManage);
        viewBinding.swipeCourse.setOnRefreshListener(this);

        viewBinding.nestedScrollViewCourse.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( courseAdapter!=null)
                    courseAdapter.setProgressBarVisiable(true);
                if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)) {//冠军分享
                    currentPage ++;
                    requestShareManagerList(currentPage);
                }else {
                    currentPage++;
                    requestCourseListData(currentPage);
                }
            }
        });

        courseAdapter =new CourseVerticalAdapter(getApplicationContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)) {//冠军分享
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                            .withString("typeView",CourseDetailsActivity.TYPE_VIEW_GUANJUN_SHARE)
                            .withString("courseId",mListCourse.get(position).getId())
                            .navigation();
                }else {
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                            .withString("courseId",mListCourse.get(position).getId())
                            .navigation();
                }

            }
        });
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_HISTORY)){
            courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_COLLECTED);
        } else if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)){
            courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_SHARE);
        }else {
            courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_COURSE_CATALOG);
        }
        viewBinding.recyclerViewCourse.setAdapter(courseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.releaseShareBtn:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_ReleaseShare).navigation(CourseListActivity.this,REQUEST_CODE_RELEASE_SHARE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RELEASE_SHARE && resultCode == RESULT_OK && data != null) {
            //刷新分享管理列表
            ToastUtil.show(getApplicationContext(),"刷新分享管理列表");
        }else if (requestCode == REQUEST_CODE_TEST && resultCode == RESULT_OK && data != null) {
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
        currentPage =1;
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)) {//冠军分享
            requestShareManagerList(currentPage);
        }else {
            requestCourseListData(currentPage);
        }
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        currentPage =1;
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE_SHARE)) {//冠军分享
            requestShareManagerList(currentPage);
        }else {
            requestCourseListData(currentPage);
        }
    }

    /**
     * 我的浏览历史
     * @param page 0：不分页
     */
    private void requestCourseListData(int page) {
        if (page==1) {
            mListCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        map.put("currPage",""+page);
        String url=InterfaceClass.MY_LOOK_HISTORY;
        NetWorkUtils.getResultArray(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<CourseBean> list = JSONArray.parseArray(response, CourseBean.class);
                    if (list==null) list =new ArrayList<>();
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
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.recyclerViewCourse.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerViewCourse.setVisibility(View.VISIBLE);
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
                }
            }
        });
    }

    /**
     * 分享管理
     */
    private void requestShareManagerList(int page) {
        if (page==1) {
            mListCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        if (page!=0) map.put("currPage",""+page);
        String url = InterfaceClass.SHARING_MANAGER;
        NetWorkUtils.getRequest(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    JSONObject object = JSONObject.parseObject(response);
                    List<CourseBean> list = object.getJSONArray("records").toJavaList(CourseBean.class);
                    if (list==null) list =new ArrayList<>();
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
                }
            }
        });
    }
}
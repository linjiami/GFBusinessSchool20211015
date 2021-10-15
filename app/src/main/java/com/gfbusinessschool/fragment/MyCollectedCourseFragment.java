package com.gfbusinessschool.fragment;

import android.view.View;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentMycollectedCourseBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCollectedCourseFragment extends BaseFragment<FragmentMycollectedCourseBinding> implements SwipeRefreshLayout.OnRefreshListener{

    private int currentPage;
    private CourseVerticalAdapter courseAdapter;
    private List<CourseBean> mListCourse = new ArrayList<>();

    @Override
    protected void initView() {
        viewBinding.courseNoData.tvNodata.setText("暂无课程");
        viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        viewBinding.slideRecyclerViewCourse.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.swipeCourse.setOnRefreshListener(this);

        viewBinding.nestedScrollViewCourse.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( courseAdapter!=null)
                    courseAdapter.setProgressBarVisiable(true);
                currentPage++;
                requestCourseListData(currentPage);
            }
        });

        courseAdapter =new CourseVerticalAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                        .withString("courseId",mListCourse.get(position).getId())
                        .navigation();
            }

            @Override
            public void onDeleteClick(int position) {
                collectCourse(mListCourse.get(position).getId(),position);
            }
        });
        courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_COLLECTED);
        viewBinding.slideRecyclerViewCourse.setAdapter(courseAdapter);
    }

    @Override
    protected void initData() {
        currentPage =1;
        requestCourseListData(currentPage);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        currentPage=1;
        requestCourseListData(currentPage);
    }

    /**
     * 课程列表
     * @param page 0：不分页
     */
    private void requestCourseListData(int page) {
        if (page==1) {
            mListCourse.clear();
        }
        Map<String,String> map =new HashMap<>();
        if (page!=0) map.put("currPage",""+page);
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.MY_COLLECTION, map, new NetWorkCallback() {
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
                    if (page==0){
                        mListCourse =list;
                        courseAdapter.setmList(mListCourse);
                        courseAdapter.notifyDataSetChanged();
                        return;
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.slideRecyclerViewCourse.setVisibility(View.GONE);
                        }else {
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
                            viewBinding.slideRecyclerViewCourse.setVisibility(View.VISIBLE);
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

    private void collectCourse(String courseId,int position){
        Map<String,String> map =new HashMap<>();
        map.put("courseId",courseId);
        map.put("isCollection","0");
        NetWorkUtils.getResultString(getContext(), InterfaceClass.COURSE_COLLECT, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getContext(),response);
                    mListCourse.remove(position);
                    courseAdapter.notifyDataSetChanged();
                    viewBinding.slideRecyclerViewCourse.closeMenu();
                    if (mListCourse.size()==0){
                        viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.slideRecyclerViewCourse.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
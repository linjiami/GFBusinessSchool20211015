package com.gfbusinessschool.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentRanklistCourseBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RanklistCourseFragment extends BaseFragment<FragmentRanklistCourseBinding> implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected void initView() {
        super.initView();
        viewBinding.swipeRefreshCourseRanklist.setOnRefreshListener(this);
        viewBinding.lookCoutLine.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.commentCoutLine.setBackgroundColor(Utils.getThemeColor(getContext()));
        LinearLayoutManager managerLookCount =new LinearLayoutManager(getContext());
        managerLookCount.setOrientation(RecyclerView.VERTICAL);
        viewBinding.lookCoutRv.setLayoutManager(managerLookCount);

        LinearLayoutManager managerCommentCount =new LinearLayoutManager(getContext());
        managerCommentCount.setOrientation(RecyclerView.VERTICAL);
        viewBinding.cimmentCountRv.setLayoutManager(managerCommentCount);

    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshCourseRanklist.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        requestCourseListData(1);
        requestCourseListData(2);
    }

    /**
     * 课程列表
     * @param type 1：课程排行-->观看次数排行
     *             2：课程排行-->评论次数排行
     */
    private void requestCourseListData(int type) {
        String url=null;
        if (type==1){
            url = InterfaceClass.STORERANKLIST_LOOKCOUNT;
        }else {
            url = InterfaceClass.STORERANKLIST_COMMENTCOUNT;
        }
        NetWorkUtils.getResultArray(getContext(), url, new HashMap<>(), new NetWorkCallback() {

            @Override
            public void onRequestError() {
                viewBinding.swipeRefreshCourseRanklist.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshCourseRanklist.setRefreshing(false);
                if (Utils.getString(code).equals("200")) {
                    List<CourseBean> list = JSONArray.parseArray(response, CourseBean.class);
                    if (list==null) list =new ArrayList<>();
                    if (type==1){
                        List<CourseBean> finalList = list;
                        CourseVerticalAdapter adapterLookCount =new CourseVerticalAdapter(getContext(), new OnClickCallBack() {
                            @Override
                            public void onClick(int position) {
                                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                        .withString("courseId", finalList.get(position).getId()).navigation();
                            }
                        });
                        adapterLookCount.setmList(list);
                        adapterLookCount.setTypeView(CourseVerticalAdapter.TYPE_VIEW_RANKLIST_COURSE_LOOKCOUNT);
                        viewBinding.lookCoutRv.setAdapter(adapterLookCount);
                    }else {
                        List<CourseBean> finalList = list;
                        CourseVerticalAdapter adapterLookCount =new CourseVerticalAdapter(getContext(), new OnClickCallBack() {
                            @Override
                            public void onClick(int position) {
                                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                                        .withString("courseId",finalList.get(position).getId()).navigation();
                            }
                        });
                        adapterLookCount.setmList(list);
                        adapterLookCount.setTypeView(CourseVerticalAdapter.TYPE_VIEW_RANKLIST_COURSE_COMMENTCOUNT);
                        viewBinding.cimmentCountRv.setAdapter(adapterLookCount);
                    }

                }
            }
        });
    }
}
package com.gfbusinessschool.activity;

import android.view.View;
import androidx.core.widget.NestedScrollView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.adapter.HotSearchAdapter;
import com.gfbusinessschool.bean.ClickStateBean;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.ActivitySearchBaseBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ARouterPath.ACTIVITY_CourseSearch)
public class CourseSearchActivity extends SearchBaseActivity<ActivitySearchBaseBinding>{
    private HotSearchAdapter historyAdapter;
    private CourseVerticalAdapter courseAdapter;
    private List<CourseBean> mList =new ArrayList<>();

    @Override
    public String getTypeView() {
        return TYPE_COURSE_SEARCH;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.searchNodataLayout.iconNodata.setImageResource(R.mipmap.placeholder_list);
        viewBinding.searchNodataLayout.tvNodata.setText(getString(R.string.alert_search_course));
        //加载招聘热门搜索、历史搜索
//        MyApplication.getInstance().getSystemBasicBean(new ResultCallBack() {
//            @Override
//            public void onResult(SystemBasicBean bean) {
//                if (bean != null && bean.getHot_words() != null) {
//                    List<String> hot_words = bean.getHot_words();
//                    HotSearchAdapter hotAdapter = new HotSearchAdapter(getApplicationContext(), new OnClickCallBack() {
//                        @Override
//                        public void onClick(int position) {
//                            loadResult(false, hot_words.get(position));
//                            currentPage = 1;
//                            requestResultData(hot_words.get(position), currentPage);
//                            viewBinding.etSearch.setText(hot_words.get(position));
//                        }
//                    });
//                    List<ClickStateBean> clickStateBeanList = new ArrayList<>();
//                    for (String str : hot_words) {
//                        clickStateBeanList.add(new ClickStateBean(str, false));
//                    }
//                    hotAdapter.setmList(clickStateBeanList);
//                    viewBinding.hotSearchRV.setAdapter(hotAdapter);
//                }
//            }
//        });

        List<ClickStateBean> clickStateBeanList =new ArrayList<>();
        for (String str : listHistory){
            clickStateBeanList.add(new ClickStateBean(str));
        }
        historyAdapter = new HotSearchAdapter(getApplicationContext(), clickStateBeanList, new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                loadResult(true,listHistory.get(position));
                currentPage =1;
                requestResultData(listHistory.get(position),currentPage);
                viewBinding.etSearch.setText(listHistory.get(position));
                viewBinding.etSearch.setSelection(listHistory.get(position).length());
                hideKeyBoard();
            }
        });
        viewBinding.historySearchRV.setAdapter(historyAdapter);

        courseAdapter = new CourseVerticalAdapter(getApplicationContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseDetails)
                        .withString("courseId", Utils.getString(mList.get(position).getId())).navigation();
            }
        });
        courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_COLLECTED);
        viewBinding.recruitRecyclerView.setAdapter(courseAdapter);

        viewBinding.nestedScrollViewSearch.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( courseAdapter!=null)
                    courseAdapter.setProgressBarVisiable(true);
                currentPage++;
                requestResultData(viewBinding.etSearch.getText().toString(),currentPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Utils.isEmpty(viewBinding.etSearch.getText().toString())) {
            ToastUtil.show(getApplicationContext(),"请输入课程名/作者");
            viewBinding.swipeRefreshLayout.setRefreshing(false);
            return;
        };
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        currentPage =1;
        requestResultData(viewBinding.etSearch.getText().toString(),currentPage);
    }

    @Override
    protected void deleteHistory() {
        List<ClickStateBean> clickStateBeanList =new ArrayList<>();
        for (String str : listHistory){
            clickStateBeanList.add(new ClickStateBean(str));
        }
        historyAdapter.setmList(clickStateBeanList);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadResult(boolean isClickHistorySearch,String contentSearch) {
        super.loadResult(isClickHistorySearch,contentSearch);
        showLoadingDialog();
        viewBinding.layoutSearchHot.setVisibility(View.GONE);
        viewBinding.swipeRefreshLayout.setVisibility(View.VISIBLE);
        if (!isClickHistorySearch){
            listHistory.add(0, contentSearch);
            saveHistorySearch(listHistory);
            List<ClickStateBean> clickStateBeanList =new ArrayList<>();
            for (String str : listHistory){
                clickStateBeanList.add(new ClickStateBean(str));
            }
            historyAdapter.setmList(clickStateBeanList);
            historyAdapter.notifyDataSetChanged();
        }
        Utils.log(Utils.TAG_ORTHER, " listHistory=" + listHistory.toString());
    }

    @Override
    protected void requestResultData(String searchContent,int page) {
        if (page==1) mList.clear();
        Map<String, String> map = new HashMap<>();
        map.put("name",searchContent);//搜索关键词
        map.put("currPage",""+page);
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.SEARCH_COURSE, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                dismissLoadingDialog();
                if (Utils.getString(code).equals("200")){
                    List<CourseBean> list = JSONArray.parseArray(response,CourseBean.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        courseAdapter.setProgressBarVisiable(false);
                    } else {
                        courseAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.recruitRecyclerView.setVisibility(View.GONE);
                            viewBinding.searchNodataLayout.noDataLayout.setVisibility(View.VISIBLE);
                        }else {
                            viewBinding.recruitRecyclerView.setVisibility(View.VISIBLE);
                            viewBinding.searchNodataLayout.noDataLayout.setVisibility(View.GONE);
                        }
                        mList =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (CourseBean bean : list){
                            mList.add(bean);
                        }
                    }
                    courseAdapter.setmList(mList);
                    courseAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
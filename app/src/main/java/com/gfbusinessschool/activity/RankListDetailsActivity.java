package com.gfbusinessschool.activity;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.RanklistStoreAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.RanklistStoreBean;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.ActivityRanklistDetailsBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ARouterPath.ACTIVITY_RankListDetails)
public class RankListDetailsActivity extends BaseActivity<ActivityRanklistDetailsBinding> implements SwipeRefreshLayout.OnRefreshListener {
    @Autowired
    String typeView;
    private int currentPage;
    private RanklistStoreAdapter mAdapter;
    private List<StoreEntity> mList;
    private int typeRanklist=1;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.swipeRefresh.setOnRefreshListener(this);
        /**
         * 1：门店排行-->全员学习通关率排行
         * 2:门店排行-->学习在线平均时长排
         * 3:门店排行-->学员积分平均排名
         * 4:个人排行-->一次性通关率排行
         * 5:个人排行-->在线习时长排行
         * 6:个人排行-->个人通关率排行
         * 7:个人排行-->积分排行
         */
        if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_STORE_CLEARANCE_RATE)){
            typeRanklist =1;
            viewBinding.titleBar.setTitle(getString(R.string.allstudy_clearancerate));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_STORE_TIME_LENGTH)){
            typeRanklist =2;
            viewBinding.titleBar.setTitle(getString(R.string.time_ranklist));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_STORE_POINT)){
            typeRanklist =3;
            viewBinding.titleBar.setTitle(getString(R.string.point_store));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.point));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE)){
            typeRanklist =4;
            viewBinding.titleBar.setTitle(getString(R.string.once_clearancerate));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_TIME_LENGTH)){
            typeRanklist =5;
            viewBinding.titleBar.setTitle(getString(R.string.online_timelength));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_CLEARANCE_RATE)){
            typeRanklist =6;
            viewBinding.titleBar.setTitle(getString(R.string.clearancerate));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.clearancerate));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_POINT)){
            typeRanklist =7;
            viewBinding.titleBar.setTitle(getString(R.string.point));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.point));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_TODAY)){
            typeRanklist =8;
            viewBinding.titleBar.setTitle(getString(R.string.time_today_title));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_ALL)){
            typeRanklist =9;
            viewBinding.titleBar.setTitle(getString(R.string.time_alltime_title));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_STORE_TODAY_TIME_LENGTH)){
            typeRanklist =10;
            viewBinding.titleBar.setTitle(getString(R.string.time_today_title));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_STORE_ALLTIME_LENGTH)){
            typeRanklist =11;
            viewBinding.titleBar.setTitle(getString(R.string.time_alltime_title));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_SHARE)){
            typeRanklist =12;
            viewBinding.titleBar.setTitle(getString(R.string.share_count_ranklist));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.share_count));
        }else if (Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_STORE_POINT)){
            typeRanklist =13;
            viewBinding.titleBar.setTitle(getString(R.string.point_store_ranklist));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.point));
        }

        viewBinding.includeRanklist1.clearancerate.setVisibility(View.GONE);
        viewBinding.includeRanklist1.lookAllClearancerate.setVisibility(View.GONE);
        LinearLayoutManager managerOnceClearanceRate =new LinearLayoutManager(getApplicationContext());
        managerOnceClearanceRate.setOrientation(RecyclerView.VERTICAL);
        viewBinding.includeRanklist1.clearanceRateRv.setLayoutManager(managerOnceClearanceRate);
        mAdapter =new RanklistStoreAdapter(getApplicationContext());
        mAdapter.setOnClickCallBack(new OnClickCallBack() {
            @Override
            public void onClick(StoreEntity entity) {
                if (entity!=null && (
                        Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE)
                        || Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_TIME_LENGTH)
                        || Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_CLEARANCE_RATE)
                        || Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_POINT)
                        || Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_TODAY)
                        || Utils.getString(typeView).equals(RanklistStoreAdapter.TYPE_VIEW_MANAGER_ALL)))
                    ToastUtil.showCenterLong(getApplicationContext(), String.format(getString(R.string.place_province),
                            Utils.getString(entity.getName()),
                            Utils.getString(entity.getProvince()),
                            Utils.getString(entity.getCity()),
                            Utils.getString(entity.getArea()),
                            Utils.getString(entity.getStore())));
            }
        });
        mAdapter.setTypeView(typeView);
        mAdapter.setLoadMore(true);
        viewBinding.includeRanklist1.clearanceRateRv.setAdapter(mAdapter);

        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( mAdapter!=null)
                    mAdapter.setProgressBarVisiable(true);
                currentPage++;
                getStoreRanklist(currentPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        currentPage=1;
        getStoreRanklist(currentPage);
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage =1;
        getStoreRanklist(currentPage);
    }

    private void getStoreRanklist(int page){
        String url =null;
        Map<String,String> map =new HashMap<>();
        map.put("currPage",""+page);
        map.put("limit","15");
        if (typeRanklist==1){
            url = InterfaceClass.STORERANKLIST_PASSRATE;
        }else if (typeRanklist==2) {
            url = InterfaceClass.STORERANKLIST_STUDYTIME;
        }else if (typeRanklist==3) {
            url = InterfaceClass.STORERANKLIST_POINT;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==4) {
            url = InterfaceClass.STORERANKLIST_ONETIMEPASSRATE;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==5) {
            url = InterfaceClass.STORERANKLIST_ONLINESTUDYTIME;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==6) {
            url = InterfaceClass.STORERANKLIST_PERSONALPASSRATE;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==7) {
            url = InterfaceClass.STORERANKLIST_PERSONALPOINT;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==8) {
            url = InterfaceClass.MANAGER_FIRSTPAGER_STUDYTIME;
            map.put("type","1");//1排行2全部
        }else if (typeRanklist==9) {
            url = InterfaceClass.MANAGER_FIRSTPAGER_STUDYTIME;
            map.put("type","2");//1排行2全部
        }else if (typeRanklist==10) {
            url = InterfaceClass.MANAGER_SORE_STUDYTIME;
            map.put("type","1");
        }else if (typeRanklist==11) {
            url = InterfaceClass.MANAGER_SORE_STUDYTIME;
            map.put("type","2");
        }else if (typeRanklist==12) {
            url = InterfaceClass.MANAGER_SORE_SHARE_RANKLIST;
        }else if (typeRanklist==13) {
            url = InterfaceClass.MANAGER_SORE_POINT_RANKLIST;
        }
        if (url==null) return;
        NetWorkUtils.getResultArray(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<StoreEntity> list = JSONArray.parseArray(response,StoreEntity.class);
                    if (list.size() < 15) {
                        mAdapter.setProgressBarVisiable(false);
                    } else {
                        mAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.ranklistNodata.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.includeRanklist1.clearanceRateRv.setVisibility(View.GONE);
                        }else {
                            viewBinding.ranklistNodata.noDataLayout.setVisibility(View.GONE);
                            viewBinding.includeRanklist1.clearanceRateRv.setVisibility(View.VISIBLE);
                        }
                        mList =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (StoreEntity bean : list){
                            mList.add(bean);
                        }
                    }
                    mAdapter.setmList(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
package com.gfbusinessschool.fragment;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.RanklistStoreAdapter;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.FragmentRanklistStoreBinding;
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
 * 门店排行
 */
public class RanklistStoreFragment extends BaseFragment<FragmentRanklistStoreBinding> implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TYPEVIEW_RANKLIST_STORE ="门店排行";
    public static final String TYPEVIEW_RANKLIST_PERSONEL ="个人排行";
    public static final String TYPEVIEW_MANAGER_RANKLIST_PERSONEL ="管理员/门店排行";
    private String typeView =TYPEVIEW_RANKLIST_STORE;

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    @Override
    protected void initView() {
        if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL)) {
            viewBinding.titleBar.setVisibility(View.VISIBLE);
            viewBinding.titleBar.setTitle(getString(R.string.title_storeall));
        }
        viewBinding.titleBar.getBackLayout().setVisibility(View.INVISIBLE);
        viewBinding.swipeRefreshStoreRankList.setOnRefreshListener(this);
        Utils.setBackgroundSolid(getContext(),viewBinding.includeRanklist1.lookAllClearancerate);
        Utils.setBackgroundSolid(getContext(),viewBinding.includeRanklist2.lookAllClearancerate);
        Utils.setBackgroundSolid(getContext(),viewBinding.includeRanklist3.lookAllClearancerate);
        Utils.setBackgroundSolid(getContext(),viewBinding.includeRanklist4.lookAllClearancerate);

        viewBinding.includeRanklist1.lookAllClearancerate.setOnClickListener(v -> {
            if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_PERSONEL))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE);
            else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_STORE_TODAY_TIME_LENGTH);
            else
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_STORE_CLEARANCE_RATE);
        });
        viewBinding.includeRanklist2.lookAllClearancerate.setOnClickListener(v -> {
            if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_PERSONEL))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_TIME_LENGTH);
            else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_STORE_ALLTIME_LENGTH);
            else
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_STORE_TIME_LENGTH);
        });
        viewBinding.includeRanklist3.lookAllClearancerate.setOnClickListener(v -> {
            if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_STORE))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_STORE_POINT);
            else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL))
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_MANAGER_SHARE);
            else
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_CLEARANCE_RATE);
        });
        viewBinding.includeRanklist4.lookAllClearancerate.setOnClickListener(v -> {
            if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_PERSONEL)){
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_POINT);
            }else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL)){
                jumpToRanklistDetails(RanklistStoreAdapter.TYPE_VIEW_MANAGER_STORE_POINT);
            }
        });
        if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_PERSONEL)){
            viewBinding.includeRanklist1.clearancerate.setText(getString(R.string.once_clearancerate));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab3.setText(getString(R.string.name));

            viewBinding.includeRanklist2.clearancerate.setText(getString(R.string.online_timelength));
            viewBinding.includeRanklist2.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist2.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist2.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist2.allCleacrancerateTab.tab4.setText(getString(R.string.time));


            viewBinding.includeRanklist3.clearancerate.setText(getString(R.string.clearancerate));
            viewBinding.includeRanklist3.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist3.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist3.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist3.allCleacrancerateTab.tab4.setText(getString(R.string.clearancerate));
            viewBinding.includeRanklist3.getRoot().setVisibility(View.VISIBLE);

            viewBinding.includeRanklist4.clearancerate.setText(getString(R.string.point));
            viewBinding.includeRanklist4.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
            viewBinding.includeRanklist4.allCleacrancerateTab.tab2.setText(getString(R.string.head));
            viewBinding.includeRanklist4.allCleacrancerateTab.tab3.setText(getString(R.string.name));
            viewBinding.includeRanklist4.allCleacrancerateTab.tab4.setText(getString(R.string.point));
            viewBinding.includeRanklist4.getRoot().setVisibility(View.VISIBLE);

        }else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL)){
            viewBinding.includeRanklist3.getRoot().setVisibility(View.VISIBLE);
            viewBinding.includeRanklist1.clearancerate.setText(getString(R.string.time_today_title));
            viewBinding.includeRanklist2.clearancerate.setText(getString(R.string.time_alltime_title));
            viewBinding.includeRanklist3.clearancerate.setText(getString(R.string.share_count_ranklist));
            viewBinding.includeRanklist1.allCleacrancerateTab.tab4.setText(getString(R.string.time));
            viewBinding.includeRanklist2.allCleacrancerateTab.tab4.setText(getString(R.string.time));
            viewBinding.includeRanklist3.allCleacrancerateTab.tab4.setText(getString(R.string.share_count));

            viewBinding.includeRanklist4.getRoot().setVisibility(View.VISIBLE);
            viewBinding.includeRanklist4.clearancerate.setText(getString(R.string.point_store_ranklist));
            viewBinding.includeRanklist4.allCleacrancerateTab.tab4.setText(getString(R.string.point));
        } else {
            viewBinding.includeRanklist2.clearancerate.setText(getString(R.string.time_ranklist));
            viewBinding.includeRanklist2.allCleacrancerateTab.tab4.setText(getString(R.string.time));

            viewBinding.includeRanklist3.clearancerate.setText(getString(R.string.point_store));
            viewBinding.includeRanklist3.allCleacrancerateTab.tab4.setText(getString(R.string.point));
            viewBinding.includeRanklist3.getRoot().setVisibility(View.VISIBLE);
        }
        LinearLayoutManager managerOnceClearanceRate =new LinearLayoutManager(getContext());
        managerOnceClearanceRate.setOrientation(RecyclerView.VERTICAL);
        viewBinding.includeRanklist1.clearanceRateRv.setLayoutManager(managerOnceClearanceRate);

        LinearLayoutManager managerTimeRanklist =new LinearLayoutManager(getContext());
        managerTimeRanklist.setOrientation(RecyclerView.VERTICAL);
        viewBinding.includeRanklist2.clearanceRateRv.setLayoutManager(managerTimeRanklist);

        LinearLayoutManager managerClearanceRate =new LinearLayoutManager(getContext());
        managerClearanceRate.setOrientation(RecyclerView.VERTICAL);
        viewBinding.includeRanklist3.clearanceRateRv.setLayoutManager(managerClearanceRate);

        LinearLayoutManager managerShare =new LinearLayoutManager(getContext());
        managerShare.setOrientation(RecyclerView.VERTICAL);
        viewBinding.includeRanklist4.clearanceRateRv.setLayoutManager(managerShare);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshStoreRankList.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_STORE)) {
            getStoreRanklist(1);
            getStoreRanklist(2);
            getStoreRanklist(3);
        }else if (Utils.getString(typeView).equals(TYPEVIEW_RANKLIST_PERSONEL)) {
            getStoreRanklist(4);
            getStoreRanklist(5);
            getStoreRanklist(6);
            getStoreRanklist(7);
        }else if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER_RANKLIST_PERSONEL)){
            getStoreRanklist(8);
            getStoreRanklist(9);
            getStoreRanklist(10);
            getStoreRanklist(11);
        }
    }

    private void jumpToRanklistDetails(String type){
        ARouter.getInstance().build(ARouterPath.ACTIVITY_RankListDetails).withString("typeView",type).navigation();
    }

    /**
     * 获取排行榜
     * @param _typeRanklist 1：门店排行-->全员学习通关率排行/详情页
     *                      2:门店排行-->学习在线平均时长排行/详情页
     *                      3:门店排行-->学员积分平均排名
     *                      4:个人排行-->一次性通关率排行
     *                      5:个人排行-->在线习时长排行
     *                      6:个人排行-->个人通关率排行
     *                      7:个人排行-->积分排行
     */
    private void getStoreRanklist(int _typeRanklist){
        String url =null;
        Map<String,String> map =new HashMap<>();
        map.put("limit",""+5);
        if (_typeRanklist==1){
            url = InterfaceClass.STORERANKLIST_PASSRATE;
        }else if (_typeRanklist==2) {
            url = InterfaceClass.STORERANKLIST_STUDYTIME;
        }else if (_typeRanklist==3) {
                url = InterfaceClass.STORERANKLIST_POINT;
            map.put("type","1");//1排行2全部
        }else if (_typeRanklist==4) {
            url = InterfaceClass.STORERANKLIST_ONETIMEPASSRATE;
            map.put("type","1");//1排行2全部
        }else if (_typeRanklist==5) {
            url = InterfaceClass.STORERANKLIST_ONLINESTUDYTIME;
            map.put("type","1");//1排行2全部
        }else if (_typeRanklist==6) {
            url = InterfaceClass.STORERANKLIST_PERSONALPASSRATE;
            map.put("type","1");//1排行2全部
        }else if (_typeRanklist==7) {
            url = InterfaceClass.STORERANKLIST_PERSONALPOINT;
            map.put("type","1");//1排行2全部
        }else if (_typeRanklist==8) {
            url = InterfaceClass.MANAGER_SORE_STUDYTIME;
            map.put("type","1");
        }else if (_typeRanklist==9) {
            url = InterfaceClass.MANAGER_SORE_STUDYTIME;
            map.put("type","2");
        }else if (_typeRanklist==10) {
            url = InterfaceClass.MANAGER_SORE_SHARE_RANKLIST;
        }else if (_typeRanklist==11) {
            url = InterfaceClass.MANAGER_SORE_POINT_RANKLIST;
        }
        if (url==null) return;
        NetWorkUtils.getResultArray(getContext(), url, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefreshStoreRankList.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshStoreRankList.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<StoreEntity> _list = JSONArray.parseArray(response,StoreEntity.class);
                    if (_list==null || _list.size()==0){
                        _list =new ArrayList<>();
                    }
                    RanklistStoreAdapter adapter =new RanklistStoreAdapter(getContext());
                    adapter.setOnClickCallBack(new OnClickCallBack() {
                        @Override
                        public void onClick(StoreEntity entity) {
                            if (entity!=null && (
                                    _typeRanklist==4 || _typeRanklist==5 || _typeRanklist==6 || _typeRanklist==7))
                                ToastUtil.showCenterLong(getContext(),String.format(getString(R.string.place_province),
                                        Utils.getString(entity.getName()),
                                        Utils.getString(entity.getProvince()),
                                        Utils.getString(entity.getCity()),
                                        Utils.getString(entity.getArea()),
                                        Utils.getString(entity.getStore())));
                        }
                    });
                    adapter.setmList(_list);
                    if (_typeRanklist==1){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_STORE_CLEARANCE_RATE);
                        viewBinding.includeRanklist1.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==2){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_STORE_TIME_LENGTH);
                        viewBinding.includeRanklist2.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==3){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_STORE_POINT);
                        viewBinding.includeRanklist3.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==4){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE);
                        adapter.setShowSolid(true);
                        viewBinding.includeRanklist1.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==5){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_TIME_LENGTH);
                        adapter.setShowSolid(true);
                        viewBinding.includeRanklist2.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==6){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_CLEARANCE_RATE);
                        adapter.setShowSolid(true);
                        viewBinding.includeRanklist3.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==7){
                       adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_PERSONEL_POINT);
                       adapter.setShowSolid(true);
                        viewBinding.includeRanklist4.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==8){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_STORE_TODAY_TIME_LENGTH);
                        viewBinding.includeRanklist1.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==9){
                       adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_STORE_ALLTIME_LENGTH);
                        viewBinding.includeRanklist2.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==10){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_MANAGER_SHARE);
                        viewBinding.includeRanklist3.clearanceRateRv.setAdapter(adapter);
                    }else if (_typeRanklist==11){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_MANAGER_STORE_POINT);
                        viewBinding.includeRanklist4.clearanceRateRv.setAdapter(adapter);
                    }
                }
            }
        });
    }
}
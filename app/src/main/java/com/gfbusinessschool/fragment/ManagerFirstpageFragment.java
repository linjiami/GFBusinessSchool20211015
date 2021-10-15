package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.adapter.RanklistStoreAdapter;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.ManagerFirstPageBean;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.FragmentManagerFirstpageBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerFirstpageFragment extends BaseFragment<FragmentManagerFirstpageBinding> implements View.OnClickListener {

    @Override
    protected void initView() {
        Utils.setBackgroundSolid(getContext(),viewBinding.todayRanklist.lookAllClearancerate);
        Utils.setBackgroundSolid(getContext(),viewBinding.alltimeRanklist.lookAllClearancerate);
        viewBinding.personalBg.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.changeUser.setOnClickListener(this);
        if (MyApplication.getInstance().getAppUserEntity()!=null){
            GlideLoadUtils.load(getContext(),MyApplication.getInstance().getAppUserEntity().getCompanyLogo(),viewBinding.compayLogo,
                    GlideLoadUtils.TYPE_PLACE_HOLDER_COMPANY_LOGO);
            viewBinding.tvLoginNo.setText(Utils.getString(MyApplication.getInstance().getAppUserEntity().getCompanyName()));
        }
        viewBinding.tabLayout2.titleTablayout.setText(getString(R.string.clearancerate_max));
        viewBinding.tabLayout3.titleTablayout.setText(getString(R.string.once_clearancerate));
        viewBinding.tabLayout4.titleTablayout.setText(getString(R.string.live_count));
        viewBinding.tabLayout5.titleTablayout.setText(getString(R.string.share_count_max));
        viewBinding.tabLayout6.titleTablayout.setText(getString(R.string.studyrate_max));
        viewBinding.todayRanklist.clearancerate.setText(getString(R.string.time_today_title));
        viewBinding.todayRanklist.lookAllClearancerate.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.ACTIVITY_RankListDetails)
                    .withString("title",getString(R.string.time_today_title))
                    .withString("typeView",RanklistStoreAdapter.TYPE_VIEW_MANAGER_TODAY)
                    .navigation();
        });
        viewBinding.todayRanklist.allCleacrancerateTab.tab2.setText(getString(R.string.head));
        viewBinding.todayRanklist.allCleacrancerateTab.tab3.setText(getString(R.string.name));
        viewBinding.todayRanklist.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
        viewBinding.todayRanklist.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        LinearLayoutManager managerOnceClearanceRate =new LinearLayoutManager(getContext());
        managerOnceClearanceRate.setOrientation(RecyclerView.VERTICAL);
        viewBinding.todayRanklist.clearanceRateRv.setLayoutManager(managerOnceClearanceRate);

        viewBinding.alltimeRanklist.clearancerate.setText(getString(R.string.time_alltime_title));
        viewBinding.alltimeRanklist.lookAllClearancerate.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.ACTIVITY_RankListDetails)
                    .withString("title",getString(R.string.time_alltime_title))
                    .withString("typeView",RanklistStoreAdapter.TYPE_VIEW_MANAGER_ALL).navigation();
        });
        viewBinding.alltimeRanklist.allCleacrancerateTab.tab2.setText(getString(R.string.head));
        viewBinding.alltimeRanklist.allCleacrancerateTab.tab3.setText(getString(R.string.name));
        viewBinding.alltimeRanklist.allCleacrancerateTab.tab3.setVisibility(View.VISIBLE);
        viewBinding.alltimeRanklist.allCleacrancerateTab.tab4.setText(getString(R.string.time));
        LinearLayoutManager managerAlltime =new LinearLayoutManager(getContext());
        managerAlltime.setOrientation(RecyclerView.VERTICAL);
        viewBinding.alltimeRanklist.clearanceRateRv.setLayoutManager(managerAlltime);
        viewBinding.testNoPassRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        viewBinding.lineTestNopassRanklist.setBackgroundColor(Utils.getThemeColor(getContext()));
    }

    @Override
    protected void initData() {
        getData();
        getRanklistData(1);
        getRanklistData(2);
        getTestNoPassRanklist();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeUser:
                AppUserEntity appUserEntity = MyApplication.getInstance().getAppUserEntity();
                if (appUserEntity==null) return;
                appUserEntity.setManagerLogin(false);
                MyApplication.getInstance().saveUserEntity(appUserEntity);
                ActivityCollector.finishAllActivity();
                ARouter.getInstance().build(ARouterPath.MainActivity).navigation();
                break;
        }
    }

    private void getData(){
        NetWorkUtils.getRequest(getContext(), InterfaceClass.MANAGER_FIRSTPAGER, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ManagerFirstPageBean firstPageBean = JSONObject.parseObject(response,ManagerFirstPageBean.class);
                    if (firstPageBean==null) return;
                    viewBinding.timeToday.setText(Utils.getString(firstPageBean.getTodayStudyLength()));
                    viewBinding.timeAll.setText(Utils.getString(firstPageBean.getTotalStudyLength()));
                    viewBinding.staffCount.setText(String.format(getString(R.string.people_position_count),
                            Utils.getString(firstPageBean.getUserNum())));
                    viewBinding.tabLayout1.countTablayout.setText(Utils.getString(firstPageBean.getAvgPassRate()));
                    viewBinding.tabLayout2.countTablayout.setText(Utils.getString(firstPageBean.getMaxPassRate()));
                    viewBinding.tabLayout3.countTablayout.setText(Utils.getString(firstPageBean.getOnePassRate()));
                    viewBinding.tabLayout4.countTablayout.setText(String.format(getString(R.string.count_study),
                            Utils.getString(firstPageBean.getShareNum())));
                    viewBinding.tabLayout5.countTablayout.setText(Utils.getString(firstPageBean.getMaxShareUser()));
                    viewBinding.tabLayout6.countTablayout.setText(Utils.getString(firstPageBean.getMaxStuNumCourse()));
                }
            }
        });
    }

    /**
     *
     * @param type 1:今日学习时间
     *             2：累计学习时间
     */
    private void getRanklistData(int type){
        Map<String,String> map =new HashMap<>();
        map.put("currPage","1");
        map.put("type",""+type);
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.MANAGER_FIRSTPAGER_STUDYTIME, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<StoreEntity> storeBeanList = JSONArray.parseArray(response,StoreEntity.class);
                    if (storeBeanList==null) storeBeanList =new ArrayList<>();
                    RanklistStoreAdapter adapter =new RanklistStoreAdapter(getContext());
                    adapter.setOnClickCallBack(new OnClickCallBack() {
                        @Override
                        public void onClick(StoreEntity entity) {
                            if (entity!=null && (type==1 || type==2))
                                ToastUtil.showCenterLong(getContext(),String.format(getString(R.string.place_province),
                                        Utils.getString(entity.getName()),
                                        Utils.getString(entity.getProvince()),
                                        Utils.getString(entity.getCity()),
                                        Utils.getString(entity.getArea()),
                                        Utils.getString(entity.getStore())));
                        }
                    });
                    adapter.setmList(storeBeanList);
                    if (type==1){
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_MANAGER_TODAY);
                        viewBinding.todayRanklist.clearanceRateRv.setAdapter(adapter);
                    }else {
                        adapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_MANAGER_ALL);
                        viewBinding.alltimeRanklist.clearanceRateRv.setAdapter(adapter );
                    }
                }
            }
        });
    }

    /**
     * 考核未通过课程排行
     */
    private void getTestNoPassRanklist(){
        Map<String,String> map =new HashMap<>();
        map.put("currPage","1");
        map.put("limit","5");
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.RANKLIST_TESTNOPASS, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                List<CourseBean> list =JSONArray.parseArray(response,CourseBean.class);
                if (list==null || list.size()==0){
                    viewBinding.layoutRanklistTestNopass.setVisibility(View.GONE);
                }else {
                    viewBinding.layoutRanklistTestNopass.setVisibility(View.VISIBLE);
                    CourseVerticalAdapter adapterLookCount =new CourseVerticalAdapter(getContext(), null);
                    adapterLookCount.setmList(list);
                    adapterLookCount.setTypeView(CourseVerticalAdapter.TYPE_VIEW_RANKLIST_TESTNOPASS_COURSE);
                    viewBinding.testNoPassRv.setAdapter(adapterLookCount);
                }
            }
        });
    }
}
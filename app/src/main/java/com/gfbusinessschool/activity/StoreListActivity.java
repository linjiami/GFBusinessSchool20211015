package com.gfbusinessschool.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.BaseActivity;
import com.gfbusinessschool.activity.CourseCenterActivity;
import com.gfbusinessschool.adapter.StoreAdapter;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.databinding.RecyclerviewLayoutBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Route(path = ARouterPath.StoreListActivity)
public class StoreListActivity extends BaseActivity<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {
    @Autowired
    String cityId;
    @Autowired
    String cityName;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(Utils.getString(cityName));
        LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.recyclerView.setLayoutManager(manager);
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        this.initData();
    }

    @Override
    protected void initData() {
        super.initData();
        Map<String,String> map =new HashMap<>();
        map.put("cityId",cityId);
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.STORE_INFO, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                showNullListView(true,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                        viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,viewBinding.noDataLayout.iconNodata);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    List<StoreEntity> listStore = JSONArray.parseArray(response,StoreEntity.class);
                    if (listStore==null) listStore =new ArrayList<>();
                    if (listStore.size()==0){
                        showNullListView(false,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                                viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,viewBinding.noDataLayout.iconNodata);
                    }else {
                        viewBinding.noDataLayout.noDataLayout.setVisibility(View.GONE);
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                    }
                    List<StoreEntity> finalListStore = listStore;
                    StoreAdapter adapter=new StoreAdapter(getApplicationContext(), new OnClickCallBack() {
                        @Override
                        public void onClick(int position) {
                            ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseCenter)
                                    .withString("title", getString(R.string.title_stafflist))
                                    .withString("typeView", CourseCenterActivity.TYPEVIEW_STAFF)
                                    .withString("storeId", finalListStore.get(position).getId()).navigation();
                        }
                    });
                    adapter.setmList(listStore);
                    viewBinding.recyclerView.setAdapter(adapter);
                }else {
                    showNullListView(true,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                            viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,viewBinding.noDataLayout.iconNodata);
                }
            }
        });
    }
}
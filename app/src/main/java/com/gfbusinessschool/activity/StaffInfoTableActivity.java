package com.gfbusinessschool.activity;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.PieChatAdapter;
import com.gfbusinessschool.bean.PieChatEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 员工信息统计扇形图
 */
@Route(path = ARouterPath.ACTIVITY_StaffInfoTable)
public class StaffInfoTableActivity extends BaseActivity<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.staff_info_manager));
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        getTableData();
    }

    private void getTableData(){
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.STAFF_INFO_TABLE, new HashMap<>(), new NetWorkCallback() {

            @Override
            public void onRequestError() {
                showNullListView(true,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                        viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,
                        viewBinding.noDataLayout.iconNodata);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<PieChatEntity> mlist = JSONArray.parseArray(response,PieChatEntity.class);
                    if (mlist==null) mlist =new ArrayList<>();
                    if (mlist.size()==0){
                        showNullListView(false,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                                viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,
                                viewBinding.noDataLayout.iconNodata);
                    }else {
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        viewBinding.noDataLayout.noDataLayout.setVisibility(View.GONE);
                        PieChatAdapter adapter =new PieChatAdapter(getApplicationContext());
                        adapter.setMlist(mlist);
                        viewBinding.recyclerView.setAdapter(adapter);
                    }
                }else {
                    showNullListView(true,viewBinding.swipeRefreshLayout,viewBinding.recyclerView,
                            viewBinding.noDataLayout.noDataLayout,viewBinding.noDataLayout.tvNodata,
                            viewBinding.noDataLayout.iconNodata);
                }
            }
        });
    }
}
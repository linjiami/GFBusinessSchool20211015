package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.StaffAdapter;
import com.gfbusinessschool.bean.StaffEntity;
import com.gfbusinessschool.databinding.RecyclerviewLayoutBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffFragment extends BaseFragment<RecyclerviewLayoutBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private String positionId;
    private String storeId;
    private StaffAdapter mAdapter;
    private List<StaffEntity> mList =new ArrayList<>();
    private int currentPage;

    public StaffFragment() {
    }

    public StaffFragment(String positionId, String storeId) {
        this.positionId = positionId;
        this.storeId = storeId;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.noDataLayoutRecycler.iconNodata.setImageResource(R.mipmap.placeholder_list);
        viewBinding.noDataLayoutRecycler.tvNodata.setText(getString(R.string.staff_nodata));
        viewBinding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager manager =new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.recyclerView.setLayoutManager(manager);
        mAdapter=new StaffAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                if (mList.get(position)!=null && !Utils.isEmpty(mList.get(position).getAccount()))
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_StaffStudyDetails)
                            .withString("account",mList.get(position).getAccount()).navigation();
            }
        });
        viewBinding.recyclerView.setAdapter(mAdapter);

        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( mAdapter!=null)
                    mAdapter.setProgressBarVisiable(true);
                currentPage++;
                getStaffList(currentPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        currentPage=1;
        getStaffList(currentPage);
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage=1;
        getStaffList(currentPage);
    }

    private void getStaffList(int page){
        if (Utils.isEmpty(positionId) || Utils.isEmpty(storeId)) return;
        if (page==1) mList.clear();
        Map<String,String> map =new HashMap<>();
        map.put("currPage",""+page);
        map.put("positionId",""+positionId);
        map.put("storeId",""+storeId);
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.STAFF_LIST, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                viewBinding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    List<StaffEntity> list = JSONArray.parseArray(response,StaffEntity.class);
                    if (list.size() < 15) {
                        mAdapter.setProgressBarVisiable(false);
                    } else {
                        mAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.recyclerView.setVisibility(View.GONE);
                        }else {
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        mList =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }else {
                            for (StaffEntity bean : list){
                                mList.add(bean);
                            }
                        }
                    }
                    mAdapter.setmList(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
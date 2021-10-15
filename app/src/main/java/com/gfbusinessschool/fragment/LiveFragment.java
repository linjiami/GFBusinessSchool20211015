package com.gfbusinessschool.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.LiveAdapter;
import com.gfbusinessschool.bean.DataRecordsBean;
import com.gfbusinessschool.bean.LiveBean;
import com.gfbusinessschool.databinding.RecyclerviewLayoutBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveFragment extends BaseFragment<RecyclerviewLayoutBinding> implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TYPEVIEW_LIVING ="进行中";
    public static final String TYPEVIEW_HISTORY_LIVE ="历史直播";
    private String typeView =TYPEVIEW_LIVING;
    private List<LiveBean> mlist =new ArrayList<>();
    private LiveAdapter mAdapter;
    private int currentPage;

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.swipeRefresh.setOnRefreshListener(this);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( mAdapter!=null)
                    mAdapter.setProgressBarVisiable(true);
                currentPage++;
                getLiveList(currentPage);
            }
        });
        viewBinding.noDataLayoutRecycler.iconNodata.setImageResource(R.mipmap.placeholder_list);
        if (Utils.getString(typeView).equals(TYPEVIEW_LIVING))
            viewBinding.noDataLayoutRecycler.tvNodata.setText(getString(R.string.alert_live_no));
        else
            viewBinding.noDataLayoutRecycler.tvNodata.setText(getString(R.string.alert_historylive_no));
        LinearLayoutManager manager =new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.recyclerView.setLayoutManager(manager);
        mAdapter =new LiveAdapter(getContext(), typeView, new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                LiveBean liveBean = mlist.get(position);
                if (liveBean==null || Utils.isEmpty(liveBean.getBroadcastUrl())) return;
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(liveBean.getBroadcastUrl());
                intent.setData(content_url);
                startActivity(intent);}
        });
        viewBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage =1;
        getLiveList(currentPage);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        currentPage =1;
        getLiveList(currentPage);
    }

    private void getLiveList(int page){
        if (page==1)mlist.clear();
        Map<String,String> map =new HashMap<>();
        map.put("currPage",page+"");
        //类型 1进行中 2已过期
        if (Utils.getString(typeView).equals(TYPEVIEW_LIVING))
            map.put("type","1");
        else
            map.put("type","2");
        NetWorkUtils.getRequest(getContext(), InterfaceClass.LIVE_LIST, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefresh.setRefreshing(false);
                viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.VISIBLE);
                viewBinding.nestedScrollView.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    DataRecordsBean shareListBean = JSONObject.parseObject(response,DataRecordsBean.class);
                    if (shareListBean==null) return;
                    if (shareListBean.getRecords()==null) shareListBean.setRecords(new ArrayList<>());
                    List<LiveBean> list = shareListBean.getRecords();
                    if (list.size() < 15) {
                        mAdapter.setProgressBarVisiable(false);
                    } else {
                        mAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.nestedScrollView.setVisibility(View.GONE);
                        }else {
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.GONE);
                            viewBinding.nestedScrollView.setVisibility(View.VISIBLE);
                        }
                       mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }else {
                            for (LiveBean liveBean : list){
                                mlist.add(liveBean);
                            }
                        }
                    }
                    mAdapter.setmList(mlist);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
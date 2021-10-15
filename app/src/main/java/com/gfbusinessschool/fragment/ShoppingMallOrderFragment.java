package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ShoppingMallAdapter;
import com.gfbusinessschool.adapter.ShoppingMallOrderAdapter;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城订单
 */
public class ShoppingMallOrderFragment extends BaseFragment<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private String typeView;
    private ShoppingMallOrderAdapter orderAdapter;
    private List<GoodsEntity> mlist =new ArrayList<>();
    private int currPage;
    private OnClickCallBack onClickCallBack;

    public ShoppingMallOrderFragment(String typeView) {
        this.typeView = typeView;
    }

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setVisibility(View.GONE);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));

        orderAdapter =new ShoppingMallOrderAdapter(getContext(), typeView, new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                GoodsEntity goodsEntity =mlist.get(position);
                if (goodsEntity==null || Utils.isEmpty(goodsEntity.getId())) return;
                confirmReceipt(goodsEntity.getId());
            }
        });
        viewBinding.recyclerView.setAdapter(orderAdapter);
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( orderAdapter!=null)
                    orderAdapter.setProgressBarVisiable(true);
                currPage++;
                getReadCollectionData(currPage);
            }
        });
    }
    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        currPage =1;
        getReadCollectionData(currPage);
    }

    private void getReadCollectionData(int page){
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        //status 1待发货、2待收货、3已完成
        if (Utils.getString(typeView).equals(ShoppingMallOrderAdapter.TYPE_ORDER_WAITTING_SEND))
            map.put("status","1");
        else if (Utils.getString(typeView).equals(ShoppingMallOrderAdapter.TYPE_ORDER_WAITTING_GET))
            map.put("status","2");
        else
            map.put("status","3");
        map.put("currPage",""+page);
        NetWorkUtils.getResultList(getContext(), InterfaceClass.SHOPMALL_ORDER_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<GoodsEntity> list = JSONObject.parseArray(response,GoodsEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        orderAdapter.setProgressBarVisiable(false);
                    } else {
                        orderAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false);
                        }else {
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                            viewBinding.noDataLayout.noDataLayout.setVisibility(View.GONE);
                        }
                        mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currPage--;
                        }
                        for (GoodsEntity bean : list){
                            mlist.add(bean);
                        }
                    }
                    orderAdapter.setMlist(mlist);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        viewBinding.recyclerView.setVisibility(View.GONE);
        viewBinding.noDataLayout.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.noDataLayout.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_goods_order));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }

    private void confirmReceipt(String id){
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        NetWorkUtils.getRequest(getContext(), InterfaceClass.SHOPMALL_ORDER_CONFIRM_RECEIPT, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getContext(),"您已确认收货");
                    initData();
                    if (onClickCallBack!=null) onClickCallBack.onResultCallBack();
                }
            }
        });
    }
}
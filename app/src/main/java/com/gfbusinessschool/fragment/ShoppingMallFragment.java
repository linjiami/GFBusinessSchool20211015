package com.gfbusinessschool.fragment;

import android.view.View;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ShoppingMallAdapter;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城列表
 */
public class ShoppingMallFragment extends BaseFragment<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private String categoryId;
    private ShoppingMallAdapter shoppingMallAdapter;
    private List<GoodsEntity> mlist =new ArrayList<>();
    private int currPage;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setVisibility(View.GONE);
        viewBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        shoppingMallAdapter =new ShoppingMallAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                GoodsEntity entity =mlist.get(position);
                if (entity==null || Utils.isEmpty(entity.getId())) return;
                ARouter.getInstance().build(ARouterPath.GoodsDetailsActivity)
                        .withString("goodId",entity.getId()).navigation();
            }
        });
        viewBinding.recyclerView.setAdapter(shoppingMallAdapter);
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( shoppingMallAdapter!=null)
                    shoppingMallAdapter.setProgressBarVisiable(true);
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
        if (Utils.isEmpty(categoryId)) return;
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("categoryId",categoryId);
        map.put("currPage",""+page);
        NetWorkUtils.getResultList(getContext(), InterfaceClass.SHOPMALL_GOODS_LIST, map, new NetWorkCallback() {
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
                        shoppingMallAdapter.setProgressBarVisiable(false);
                    } else {
                        shoppingMallAdapter.setProgressBarVisiable(true);
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
                    shoppingMallAdapter.setMlist(mlist);
                    shoppingMallAdapter.notifyDataSetChanged();
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
            viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_goods));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
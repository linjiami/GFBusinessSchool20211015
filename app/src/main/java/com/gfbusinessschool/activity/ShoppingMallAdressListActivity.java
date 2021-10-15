package com.gfbusinessschool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ShoppingMallAddressAdapter;
import com.gfbusinessschool.bean.ShoppingMallAddressEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收货地址列表（积分商城）
 */
@Route(path = ARouterPath.ShoppingMallAdressListActivity)
public class ShoppingMallAdressListActivity extends BaseActivity<LayoutListBinding>
        implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private List<ShoppingMallAddressEntity> addressEntityList =new ArrayList<>();
    private ShoppingMallAddressAdapter addressAdapter;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.shoppingmall_address));
        viewBinding.titleBar.setRightText(getString(R.string.shoppingmall_address_new));
        viewBinding.titleBar.getRigthLayout().setOnClickListener(this);
        viewBinding.swipeRefreshLayout.setBackgroundColor(getResources().getColor(R.color.white));
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);

        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        addressAdapter =new ShoppingMallAddressAdapter(getApplicationContext(), new OnClickCallBack() {

            @Override
            public void onModifyClick(int position) {
                ARouter.getInstance().build(ARouterPath.ShoppingMallAddAddressActivity)
                        .withParcelable("AddressEntity",addressEntityList.get(position))
                        .navigation(ShoppingMallAdressListActivity.this,REQUEST_CODE_MODIFY_ADDRESS);
            }

            @Override
            public void onClick(int position) {
                Intent intent=new Intent();
                Bundle bundle =new Bundle();
                bundle.putParcelable("address",addressEntityList.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        addressAdapter.setAddressEntityList(addressEntityList);
        viewBinding.recyclerView.setAdapter(addressAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTitleBar://新增地址
                ARouter.getInstance().build(ARouterPath.ShoppingMallAddAddressActivity).navigation(this,REQUEST_CODE_ADD_ADDRESS);
                break;
        }
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.SHOPMALL_ADDRESS_LIST, new HashMap<>(), new NetWorkCallback() {

            @Override
            public void onRequestError() {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    addressEntityList = JSONArray.parseArray(response,ShoppingMallAddressEntity.class);
                    if (addressEntityList==null) addressEntityList =new ArrayList<>();
                    if (addressEntityList.size()==0){
                        viewBinding.noDataLayout.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.recyclerView.setVisibility(View.GONE);
                        viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_address_list));
                    }else {
                        viewBinding.noDataLayout.noDataLayout.setVisibility(View.GONE);
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                    }
                    addressAdapter.setAddressEntityList(addressEntityList);
                    addressAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_ADD_ADDRESS && resultCode==RESULT_OK){
            initData();
        }else if (requestCode==REQUEST_CODE_MODIFY_ADDRESS && resultCode==RESULT_OK){
            initData();
        }
    }
}
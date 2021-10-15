package com.gfbusinessschool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ShoppingMallAddressEntity;
import com.gfbusinessschool.databinding.ActivityShoppingmallAddressBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 收货地址（积分商城）
 */
@Route(path = ARouterPath.ShoppingMallAddAddressActivity)
public class ShoppingMallAddAddressActivity extends BaseActivity<ActivityShoppingmallAddressBinding>
        implements View.OnClickListener {
    @Autowired(name = "AddressEntity")
    ShoppingMallAddressEntity addressEntity;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.shoppingmall_address));
        viewBinding.bottomLayout.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.bottomLayout.releaseShareBtn.setOnClickListener(this);
        viewBinding.bottomLayout.releaseShareBtn.setText(getString(R.string.save));
        if (addressEntity!=null){
            viewBinding.titleBar.getRightTv().setText(getString(R.string.delete_address));
            viewBinding.titleBar.getRightTv().setTextSize(15);
            viewBinding.titleBar.getRightTv().setTextColor(Utils.getThemeColor(getApplicationContext()));
            viewBinding.titleBar.getRigthLayout().setOnClickListener(this);
            if (!Utils.isEmpty(addressEntity.getName()))
                viewBinding.name.setText(addressEntity.getName());
            if (!Utils.isEmpty(addressEntity.getAddress()))
                viewBinding.address.setText(addressEntity.getAddress());
            if (!Utils.isEmpty(addressEntity.getMobile()))
                viewBinding.mobile.setText(addressEntity.getMobile());
        }else {
            addressEntity =new ShoppingMallAddressEntity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTitleBar://删除地址
                deleteAddress();
                break;
            case R.id.releaseShareBtn:
                String name =viewBinding.name.getText().toString().trim();
                if (Utils.isEmpty(name)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_connect_people));
                    return;
                }
                addressEntity.setName(name);

                String mobile =viewBinding.mobile.getText().toString().trim();
                if (Utils.isEmpty(mobile)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_connect_phone));
                    return;
                }
                addressEntity.setMobile(mobile);

                String connectAddress =viewBinding.address.getText().toString().trim();
                if (Utils.isEmpty(connectAddress)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_connect_address));
                    return;
                }
                addressEntity.setAddress(connectAddress);
                addAddress();
                break;
        }
    }

    private void deleteAddress() {
        Map<String,String> map =new HashMap<>();
        map.put("id",addressEntity.getId());
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.SHOPMALL_ADDRESS_DELETE, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.address_delete));
                    Intent intent =new Intent();
                    Bundle bundle =new Bundle();
                    bundle.putParcelable("address",addressEntity);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void addAddress() {
        NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.SHOPMALL_ADDRESS_ADD, JSONObject.toJSONString(addressEntity), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    MyAlertDialog myAlertDialog = new MyAlertDialog(ShoppingMallAddAddressActivity.this, new MyDialogCallback() {
                        @Override
                        public void onPositiveClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            Intent intent =new Intent();
                            Bundle bundle =new Bundle();
                            bundle.putParcelable("address",addressEntity);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                    myAlertDialog.setMessage(getString(R.string.address_add));
                    myAlertDialog.setNegativeButtonGone();
                    myAlertDialog.setCanceledOnTouchOutside(false);
                    myAlertDialog.show();
                }
            }
        });
    }

}
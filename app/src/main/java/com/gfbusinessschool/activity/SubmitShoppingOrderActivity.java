package com.gfbusinessschool.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.bean.ShoppingMallAddressEntity;
import com.gfbusinessschool.databinding.ActivitySubmitshoppingorderBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交订单（商城）
 */
@Route(path = ARouterPath.SubmitShoppingOrderActivity)
public class SubmitShoppingOrderActivity extends BaseActivity<ActivitySubmitshoppingorderBinding>
        implements View.OnClickListener {
    @Autowired
    GoodsEntity goodsEntity;
    private int countGoods=1;
    private ShoppingMallAddressEntity addressEntity;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.title_submitorder));
        viewBinding.pay.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.countIntegral.setTextColor(Utils.getThemeColor(getApplicationContext()));
        Utils.setImageViewTint(getApplicationContext(),viewBinding.minusIcon);
        Utils.setImageViewTint(getApplicationContext(),viewBinding.addIcon);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.address,2);
        viewBinding.address.setOnClickListener(this);
        viewBinding.addIcon.setOnClickListener(this);
        viewBinding.minusIcon.setOnClickListener(this);
        viewBinding.pay.setOnClickListener(this);
        viewBinding.countGoods.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isEmpty(s.toString())) return;
                countGoods =Integer.parseInt(s.toString());
                if (countGoods==0){
                    ToastUtil.show(getApplicationContext(),"兑换数量不能为0");
                    countGoods =1;
                    viewBinding.countGoods.setText(""+countGoods);
                    viewBinding.countGoods.setSelection(viewBinding.countGoods.getText().length());
                }
                int price;
                if (Utils.isEmpty(goodsEntity.getPrice()))
                    price=0;
                else
                    price =Integer.parseInt(goodsEntity.getPrice());
                viewBinding.countAllIntegral.setText(String.format(getString(R.string.place_pay_integralcount),
                        ""+(countGoods*price)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (goodsEntity!=null){
            GlideLoadUtils.load(getApplicationContext(),goodsEntity.getImgUrl(),viewBinding.cover,GlideLoadUtils.TYPE_PLACE_HOLDER_NO);
            viewBinding.goodsName.setText(Utils.getString(goodsEntity.getGoodsName()));
            if (Utils.isEmpty(goodsEntity.getPrice()))
                viewBinding.countIntegral.setText(String.format(getString(R.string.place_integral_count),"0"));
            else
                viewBinding.countIntegral.setText(String.format(getString(R.string.place_integral_count),goodsEntity.getPrice()));
            int priceAdd;
            if (Utils.isEmpty(goodsEntity.getPrice()))
                priceAdd=0;
            else
                priceAdd =Integer.parseInt(goodsEntity.getPrice());
            viewBinding.countAllIntegral.setText(String.format(getString(R.string.place_pay_integralcount),
                    ""+(countGoods*priceAdd)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay:
                if (goodsEntity==null) return;
                Map<String,String> map =new HashMap<>();
                if (addressEntity==null || Utils.isEmpty(addressEntity.getAddress())){
                    ToastUtil.show(getApplicationContext(),"请选择收货地址");
                    return;
                }
                map.put("addressId",addressEntity.getId());
                map.put("goodsNums",viewBinding.countGoods.getText().toString().trim());
                map.put("id",goodsEntity.getId());
                MyAlertDialog myAlertDialog = new MyAlertDialog(SubmitShoppingOrderActivity.this, new MyDialogCallback() {
                    @Override
                    public void onPositiveClick(MyAlertDialog dialog) {
                        super.onPositiveClick(dialog);
                        commitGoodsData(map);
                    }
                });
                myAlertDialog.setMessage(getString(R.string.alert_buy_shop));
                myAlertDialog.show();
                break;
            case R.id.address:
                ARouter.getInstance().build(ARouterPath.ShoppingMallAdressListActivity).navigation(this,REQUEST_CODE_CITY_SELECT);
                break;
            case R.id.addIcon:
                if (goodsEntity==null) return;
                String _count=viewBinding.countGoods.getText().toString().trim();
                countGoods =Integer.parseInt(_count);
                countGoods++;
                viewBinding.countGoods.setText(""+countGoods);
                viewBinding.countGoods.setSelection(viewBinding.countGoods.getText().length());
                int price;
                if (Utils.isEmpty(goodsEntity.getPrice()))
                    price=0;
                else
                    price =Integer.parseInt(goodsEntity.getPrice());
                viewBinding.countAllIntegral.setText(String.format(getString(R.string.place_pay_integralcount),
                        ""+(countGoods*price)));
                break;
            case R.id.minusIcon:
                String count=viewBinding.countGoods.getText().toString().trim();
                countGoods =Integer.parseInt(count);
                if (countGoods==1) return;
                countGoods--;
                viewBinding.countGoods.setText(""+countGoods);
                viewBinding.countGoods.setSelection(viewBinding.countGoods.getText().length());
                int priceAdd;
                if (Utils.isEmpty(goodsEntity.getPrice()))
                    priceAdd=0;
                else
                    priceAdd =Integer.parseInt(goodsEntity.getPrice());
                viewBinding.countAllIntegral.setText(String.format(getString(R.string.place_pay_integralcount),
                        ""+(countGoods*priceAdd)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_CITY_SELECT && resultCode == RESULT_OK){
            if (data==null) return;
            addressEntity =data.getParcelableExtra("address");
            if (addressEntity==null)return;
            viewBinding.address.setText(String.format(getString(R.string.place_address_phone),
                    addressEntity.getAddress(),addressEntity.getName(),addressEntity.getMobile()));
        }
    }

    private void commitGoodsData(Map<String, String> map) {
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.SHOPMALL_GOODS_BUY, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    MyAlertDialog myAlertDialog = new MyAlertDialog(SubmitShoppingOrderActivity.this, new MyDialogCallback() {
                        @Override
                        public void onPositiveClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            ARouter.getInstance().build(ARouterPath.ShoppingMallOrderActivity).navigation();
                            finish();
                        }
                    });
                    myAlertDialog.setPositiveText(getString(R.string.look_order));
                    myAlertDialog.setMessage(getString(R.string.goods_buy_success));
                    myAlertDialog.setCanceledOnTouchOutside(false);
                    myAlertDialog.show();
                }
            }
        });
    }
}
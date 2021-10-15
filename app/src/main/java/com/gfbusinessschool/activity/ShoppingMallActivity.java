package com.gfbusinessschool.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.ClassifyEntity;
import com.gfbusinessschool.fragment.ShoppingMallFragment;
import com.gfbusinessschool.databinding.ActivityShoppingmallBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 积分商城
 */
@Route(path = ARouterPath.ShoppingMallActivity)
public class ShoppingMallActivity extends BaseActivity<ActivityShoppingmallBinding>{
    private ArrayList<Fragment> fragmentList =new ArrayList<>();//FrameLayout集合
    private ArrayList<RadioButton> radioButtonsList =new ArrayList<>();//FrameLayout集合
    private FragmentManager fragmentManager;
    private int positionSelected;

    @Override
    protected void initView() {
        super.initView();
        fragmentManager =getSupportFragmentManager();
        viewBinding.titleBar.setTitle(getString(R.string.integral_store));

        viewBinding.shoppingMallOrder.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.ShoppingMallOrderActivity).navigation();
        });
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        if (appUserEntity!=null){
            GlideLoadUtils.load(getApplicationContext(),appUserEntity.getHeadImgUrl(),viewBinding.headIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            viewBinding.userName.setText(Utils.getString(appUserEntity.getName()));
            if (Utils.isEmpty(appUserEntity.getIntegral()))
                viewBinding.countUserIntegral.setText(String.format(getString(R.string.place_integral_count),"0"));
            else
                viewBinding.countUserIntegral.setText(String.format(getString(R.string.place_integral_count),appUserEntity.getIntegral()));
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getShopMallClassify();
    }

    //四个按钮对应点击事件
    private void showFragment(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentList.get(position).isAdded()){
            if (position==positionSelected) return;
            if (fragmentList.get(positionSelected).isAdded())
                transaction.show(fragmentList.get(position)).hide(fragmentList.get(positionSelected)).commitAllowingStateLoss();
            else
                transaction.show(fragmentList.get(position)).commitAllowingStateLoss();
        }else {
            if (fragmentList.get(positionSelected).isAdded())
                transaction.add(R.id.fragment,fragmentList.get(position)).hide(fragmentList.get(positionSelected)).commitAllowingStateLoss();
            else
                transaction.add(R.id.fragment,fragmentList.get(position)).commitAllowingStateLoss();
        }
        positionSelected =position;
    }

    private void getShopMallClassify(){
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.SHOPMALL_CLASSIFY, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<ClassifyEntity> list = JSONArray.parseArray(response,ClassifyEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size()==0){
                        viewBinding.scrollViewLeft.setVisibility(View.GONE);
                        viewBinding.fragment.setVisibility(View.GONE);
                        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.layoutNoData.tvNodata.setText(getString(R.string.noData));
                        return;
                    }
                    viewBinding.scrollViewLeft.setVisibility(View.VISIBLE);
                    viewBinding.fragment.setVisibility(View.VISIBLE);
                    viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                    viewBinding.personnelRG.removeAllViews();
                    for (int i=0;i<list.size();i++){
                        ClassifyEntity entity =list.get(i);
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.height =  (int) getResources().getDimension(R.dimen.px120);
                        RadioButton tv = new RadioButton(getApplicationContext());
                        tv.setGravity(Gravity.CENTER);
                        if (entity!=null)tv.setText(Utils.getString(entity.getCategoryName()));
                        tv.setTextSize(14);
                        tv.setTextColor(getResources().getColorStateList(R.color.class_personnel_store));
                        tv.setMaxEms(10);
                        tv.setBackgroundResource(R.drawable.class_a);
                        tv.setButtonDrawable(null);
                        tv.setLayoutParams(layoutParams);
                        tv.setPadding(10,0,10,0);
                        viewBinding.personnelRG.addView(tv, layoutParams);
                        radioButtonsList.add(tv);
                        int finalI = i;
                        tv.setOnClickListener(v -> {
                            showFragment(finalI);
                        });
                        radioButtonsList.get(0).setChecked(true);
                        ShoppingMallFragment shoppingMallFragment =new ShoppingMallFragment();
                        if (entity!=null) shoppingMallFragment.setCategoryId(entity.getId());
                        fragmentList.add(shoppingMallFragment);
                    }
                    if (radioButtonsList.size()!=0) {
                        showFragment(0);
                        radioButtonsList.get(0).setChecked(true);
                    }
                }
            }
        });
    }
}
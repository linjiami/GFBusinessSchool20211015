package com.gfbusinessschool.activity;

import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.fragment.ShoppingMallOrderFragment;
import com.gfbusinessschool.adapter.ShoppingMallOrderAdapter;
import com.gfbusinessschool.databinding.ActivityMyfollowBinding;
import com.gfbusinessschool.utils.ARouterPath;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城订单
 */
@Route(path = ARouterPath.ShoppingMallOrderActivity)
public class ShoppingMallOrderActivity extends BaseActivity<ActivityMyfollowBinding>
        implements View.OnClickListener {
    private int lastFragmentIndex=0;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList =new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.shoppingmall_order));
        viewBinding.btnCourse.setText(getString(R.string.waitting_sendgoods));
        viewBinding.btnAuthor.setText(getString(R.string.waitting_getgoods));
        viewBinding.btnLive.setText(getString(R.string.test_finish));
        viewBinding.btnCourse.setOnClickListener(this);
        viewBinding.btnAuthor.setOnClickListener(this);
        viewBinding.btnLive.setOnClickListener(this);
        fragmentManager =getSupportFragmentManager();
        fragmentList.clear();
        fragmentList.add(new ShoppingMallOrderFragment(ShoppingMallOrderAdapter.TYPE_ORDER_WAITTING_SEND));
        fragmentList.add(new ShoppingMallOrderFragment(ShoppingMallOrderAdapter.TYPE_ORDER_WAITTING_GET));
        ShoppingMallOrderFragment finishFragment =new ShoppingMallOrderFragment(ShoppingMallOrderAdapter.TYPE_ORDER_FINISH);
        finishFragment.setOnClickCallBack(new OnClickCallBack() {
            @Override
            public void onResultCallBack() {
                //刷新已完成的订单列表
                if (fragmentList!=null && fragmentList.size()>=3){
                    ShoppingMallOrderFragment fragment = (ShoppingMallOrderFragment) fragmentList.get(2);
                    fragment.onRefresh();
                }
            }
        });
        fragmentList.add(finishFragment);
        showFragment(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCourse:
                if (lastFragmentIndex==0) return;
                showFragment(0);
                break;
            case R.id.btnAuthor:
                if (lastFragmentIndex==1) return;
                showFragment(1);
                break;
            case R.id.btnLive:
                if (lastFragmentIndex==2) return;
                showFragment(2);
                break;
        }
    }


    private void showFragment(int postion) {
        setTabState(postion);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentList.get(postion).isAdded()){
            if (postion==lastFragmentIndex) return;
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.show(fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commit();
            else
                transaction.show(fragmentList.get(postion)).commit();
        }else {
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.add(R.id.frameLayoutFollow,fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commit();
            else
                transaction.add(R.id.frameLayoutFollow,fragmentList.get(postion)).commit();
        }
        lastFragmentIndex =postion;
    }

    private void setTabState(int postion) {
        viewBinding.btnCourse.setBackground(getApplicationContext().getDrawable(R.drawable.shape6_white_left));
        viewBinding.btnCourse.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        viewBinding.btnAuthor.setBackgroundColor(getResources().getColor(R.color.white));
        viewBinding.btnAuthor.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        viewBinding.btnLive.setBackground(getApplicationContext().getDrawable(R.drawable.shape6_white_right));
        viewBinding.btnLive.setTextColor(getApplicationContext().getResources().getColor(R.color.color_14));
        if (postion==0){
            viewBinding.btnCourse.setBackground(getApplicationContext().getDrawable(R.drawable.shape6_themecolor_left));
            viewBinding.btnCourse.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }else if (postion==1){
            viewBinding.btnAuthor.setBackgroundColor(getResources().getColor(R.color.theme_text_color));
            viewBinding.btnAuthor.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }else {
            viewBinding.btnLive.setBackground(getApplicationContext().getDrawable(R.drawable.shape6_themecolor_right));
            viewBinding.btnLive.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }
    }
}
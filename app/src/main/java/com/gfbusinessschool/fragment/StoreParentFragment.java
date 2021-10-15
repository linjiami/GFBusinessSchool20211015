package com.gfbusinessschool.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.databinding.ActivityCoursecenterBinding;
import com.gfbusinessschool.databinding.FragmentStoreparentBinding;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工（员工首页显示的是店面列表，店面列表点击去是员工列表）
 */
public class StoreParentFragment extends BaseFragment<FragmentStoreparentBinding>
        implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Fragment> fragmentList =new ArrayList<>();//FrameLayout集合
    private ArrayList<RadioButton> radioButtonsList =new ArrayList<>();//FrameLayout集合
    private FragmentManager fragmentManager;
    private int lastFragmentIndex;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBarCourseCenter.getBackLayout().setVisibility(View.INVISIBLE);
        viewBinding.titleBarCourseCenter.setTitle(getString(R.string.store_list));
        //暂时隐藏员工信息统计
//        viewBinding.titleBarCourseCenter.setRightText(getString(R.string.staff_info_manager));
//        viewBinding.titleBarCourseCenter.getRigthLayout().setOnClickListener(v -> {
//            ARouter.getInstance().build(ARouterPath.ACTIVITY_StaffInfoTable).navigation();
//        });
        fragmentManager =getFragmentManager();
        viewBinding.swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        viewBinding.personnelFL.removeAllViews();
        fragmentList.clear();
        radioButtonsList.clear();
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        requestCourseClassifyList();
    }

    private void requestCourseClassifyList() {
        Map<String,String> map =new HashMap<>();
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.STORE_ADDRESS, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                viewBinding.layoutNoData.tvNodata.setText(getString(R.string.error_service_msg));
                viewBinding.scrollViewLeft.setVisibility(View.GONE);
                viewBinding.personnelFL.setVisibility(View.GONE);
                viewBinding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<StoreAddressEntity> classifyBeanList = JSONArray.parseArray(response, StoreAddressEntity.class);
                    if (classifyBeanList==null || classifyBeanList.size()==0){
                        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                        viewBinding.layoutNoData.tvNodata.setText(getString(R.string.noData));
                        viewBinding.scrollViewLeft.setVisibility(View.GONE);
                        viewBinding.personnelFL.setVisibility(View.GONE);
                        return;
                    }
                    viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                    viewBinding.scrollViewLeft.setVisibility(View.VISIBLE);
                    viewBinding.personnelFL.setVisibility(View.VISIBLE);
                    viewBinding.personnelRG.removeAllViews();
                    List<String> positionsList =new ArrayList<>();
                    for (int i=0;i<classifyBeanList.size();i++){
                        StoreAddressEntity classifyBean =classifyBeanList.get(i);
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.height = (int) getResources().getDimension(R.dimen.px120);
                        positionsList.add(classifyBean.getAddressName());
                        RadioButton tv = new RadioButton(getContext());
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(classifyBean.getAddressName());
                        tv.setTextSize(13);
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
                        AreaClassifyFragment fragment =new AreaClassifyFragment();
                        fragment.setMlist(classifyBeanList);
                        fragmentList.add(fragment);
                    }
                    showFragment(lastFragmentIndex);
                    if (radioButtonsList.size()!=0)  radioButtonsList.get(lastFragmentIndex).setChecked(true);
                }else {
                    viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                    viewBinding.layoutNoData.tvNodata.setText(getString(R.string.error_service_msg));
                    viewBinding.scrollViewLeft.setVisibility(View.GONE);
                    viewBinding.personnelFL.setVisibility(View.GONE);
                }
            }
        });

    }

    private void showFragment(int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentList.size()==0){
            return;
        }
        if (fragmentList.get(postion).isAdded()){
            if (postion==lastFragmentIndex) return;
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.show(fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commitAllowingStateLoss();
            else
                transaction.show(fragmentList.get(postion)).commitAllowingStateLoss();
        }else {
            if (fragmentList.get(lastFragmentIndex).isAdded())
                transaction.add(R.id.personnelFL,fragmentList.get(postion)).hide(fragmentList.get(lastFragmentIndex)).commitAllowingStateLoss();
            else
                transaction.add(R.id.personnelFL,fragmentList.get(postion)).commitAllowingStateLoss();
        }
        lastFragmentIndex =postion;
    }
}
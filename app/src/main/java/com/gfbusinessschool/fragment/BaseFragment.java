package com.gfbusinessschool.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.BaseActivity;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.dialog.LoadingDialog;
import com.gfbusinessschool.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {

    private Context mContext;
    protected boolean isFirstLoad = true; // 是否第一次加载
    protected LoadingDialog loadingDialog;
    protected T viewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            viewBinding = (T) inflate.invoke(null, inflater, container, false);
            ARouter.getInstance().inject(BaseFragment.this);
            initView();
        }  catch (NoSuchMethodException | IllegalAccessException| InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData();
            initEvent();
            isFirstLoad = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> allFragments = getChildFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    /**
     * 初始化视图
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化事件
     */
    protected void initEvent() {

    }

    protected void showLoadingDialog(){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(getContext());
        }
        if (!loadingDialog.isShowing()) loadingDialog.show();
    }

    protected void dismissLoadingDialog(){
        if (loadingDialog!=null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    public boolean isLogin(){
        AppUserEntity appUserEntity = MyApplication.getInstance().getAppUserEntity();
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getPhone()))
            return true;
        else
            return false;
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void showKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            editText.requestFocus();
            editText.setShowSoftInputOnFocus(true);
        }
    }

    protected void showNullListView(boolean isError, SwipeRefreshLayout swipeRefresh,
                                    RecyclerView recyclerView, ViewGroup viewGroup, TextView tvNodata, ImageView iconNodata){
        try {
            if (getActivity() instanceof BaseActivity){
                BaseActivity activity = (BaseActivity) getActivity();
                activity.showNullListView(isError,swipeRefresh,recyclerView,viewGroup,tvNodata,iconNodata);
            }else {
                dismissLoadingDialog();
                if (swipeRefresh!=null)swipeRefresh.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                viewGroup.setVisibility(View.VISIBLE);
                if (isError){
                    tvNodata.setText("加载失败，请刷新后重试!");
                    iconNodata.setImageResource(R.mipmap.placeholder_network);
                }else {
                    tvNodata.setText("暂无数据");
                    iconNodata.setImageResource(R.mipmap.placeholder_list);
                }
            }
        } catch (Exception e) {
            Utils.log(Utils.TAG_ERROR,"Exception "+e.getMessage());
        }
    }
}
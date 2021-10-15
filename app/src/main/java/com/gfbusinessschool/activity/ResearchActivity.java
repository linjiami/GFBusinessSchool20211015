package com.gfbusinessschool.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ResearchAdapter;
import com.gfbusinessschool.bean.ResearchEntity;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.LayoutListBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问卷调研列表
 */
@Route(path = ARouterPath.ResearchActivity)
public class ResearchActivity extends BaseActivity<LayoutListBinding> implements SwipeRefreshLayout.OnRefreshListener {
    @Autowired
    boolean isJoinResearch;//是否是我参与的调研
    private ResearchAdapter researchAdapter;
    private int currentPage;
    private List<ResearchEntity> mlist =new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        viewBinding.swipeRefreshLayout.setBackgroundColor(getResources().getColor(R.color.white));
        viewBinding.titleBar.setTitle(getString(R.string.question_research));
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        researchAdapter =new ResearchAdapter(getApplicationContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                ResearchEntity researchEntity =mlist.get(position);
                if (researchEntity==null) return;
                if (isJoinResearch){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_research_already));
                    return;
                }
                //runStatus问券状态（1进行中2未开始3已结束）isJoin 用户是否参与（0未参与1已参与）
                if (Utils.getString(researchEntity.getRunStatus()).equals("2")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_research_nostart));
                }else if (Utils.getString(researchEntity.getRunStatus()).equals("3")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_research_stop));
                }else if (Utils.getString(researchEntity.getRunStatus()).equals("1")){
                    if (Utils.getString(researchEntity.getIsJoin()).equals("0"))
                        ARouter.getInstance().build(ARouterPath.ACTIVITY_Test)
                                .withBoolean("isQuestionResearch",true)
                                .withString("testId",mlist.get(position).getId())
                                .withString("testName",mlist.get(position).getName())
                                .navigation(ResearchActivity.this,REQUEST_CODE_RESEARCH);
                    else
                        ToastUtil.show(getApplicationContext(),getString(R.string.alert_research_already));
                }

            }
        });
        if (isJoinResearch) researchAdapter.setIsJoinResearch(true);
        viewBinding.recyclerView.setAdapter(researchAdapter);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (researchAdapter!=null)
                    researchAdapter.setProgressBarVisiable(true);
                currentPage++;
                getResearchData(currentPage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_RESEARCH && resultCode==RESULT_OK){
            initData();
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
        currentPage =1;
        getResearchData(currentPage);
    }

    private void getResearchData(int page) {
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        map.put("currPage",""+page);
        String url =InterfaceClass.RESEARCH_LIST;
        if (isJoinResearch)
            url =InterfaceClass.RESEARCH_MY_LIST;
        NetWorkUtils.getResultArray(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                dismissLoadingDialog();
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                dismissLoadingDialog();
                if (Utils.getString(code).equals("200")){
                    List<ResearchEntity> list = JSONObject.parseArray(response,ResearchEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        researchAdapter.setProgressBarVisiable(false);
                    } else {
                        researchAdapter.setProgressBarVisiable(true);
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
                            currentPage--;
                        }
                        for (ResearchEntity bean : list){
                            mlist.add(bean);
                        }
                    }
                    researchAdapter.setmList(mlist);
                    researchAdapter.notifyDataSetChanged();
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
            if (isJoinResearch)
                viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_research_my));
            else
                viewBinding.noDataLayout.tvNodata.setText(getString(R.string.place_research));
            viewBinding.noDataLayout.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
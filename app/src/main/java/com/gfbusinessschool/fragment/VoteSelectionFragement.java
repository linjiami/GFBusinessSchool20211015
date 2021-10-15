package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.VoteSelectionAdapter;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.bean.VoteEntity;
import com.gfbusinessschool.databinding.RecyclerviewLayoutBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票评选
 */
public class VoteSelectionFragement extends BaseFragment<RecyclerviewLayoutBinding> implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TYPE_SATRTING ="进行中";
    public static final String TYPE_NO_SATRT ="未开始";
    public static final String TYPE_COMPLETE ="已结束";
    private int currentPage;
    private VoteSelectionAdapter voteSelectionAdapter;
    private String typeView =TYPE_SATRTING;//投票是否进行中
    private List<VoteEntity> mlist =new ArrayList<>();


    public VoteSelectionFragement(String typeView) {
        this.typeView = typeView;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.swipeRefresh.setOnRefreshListener(this);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (voteSelectionAdapter!=null)
                    voteSelectionAdapter.setProgressBarVisiable(true);
                currentPage++;
                getVoteSelectionData(currentPage);
            }
        });
         voteSelectionAdapter =new VoteSelectionAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                if (Utils.getString(typeView).equals(TYPE_SATRTING)){
                    ARouter.getInstance().build(ARouterPath.VoteDetailsActivity)
                            .withParcelable("voteEntity",mlist.get(position)).navigation();
                }else if (Utils.getString(typeView).equals(TYPE_NO_SATRT)){
                    showAlertDialog(getString(R.string.alert_vote_nostart));
                }else {
                    ARouter.getInstance().build(ARouterPath.VoteRanklistActivity)
                            .withString("activeId",mlist.get(position).getId()).navigation();
                }
            }
        });
        viewBinding.recyclerView.setAdapter(voteSelectionAdapter);
        showLoadingDialog();
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage =1;
        getVoteSelectionData(1);
    }

    private void getVoteSelectionData(int page) {
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        //1未开始，2进行中，3已结束
        if (Utils.getString(typeView).equals(TYPE_SATRTING)){
            map.put("type","2");
        }else if (Utils.getString(typeView).equals(TYPE_NO_SATRT)){
            map.put("type","1");
        }else {
            map.put("type","3");
        }
        map.put("currPage",""+page);
        NetWorkUtils.getResultList(getContext(), InterfaceClass.VOTE_ACTIVE_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeRefresh.setRefreshing(false);
                dismissLoadingDialog();
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                dismissLoadingDialog();
                if (Utils.getString(code).equals("200")){
                    List<VoteEntity> list = JSONObject.parseArray(response,VoteEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        voteSelectionAdapter.setProgressBarVisiable(false);
                    } else {
                        voteSelectionAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false);
                        }else {
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                            viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.GONE);
                        }
                        mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (VoteEntity bean : list){
                            mlist.add(bean);
                        }
                    }
                    voteSelectionAdapter.setList(mlist);
                    voteSelectionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showAlertDialog(String msg){
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity(), new MyDialogCallback() {
            @Override
            public void onPositiveClick(MyAlertDialog dialog) {
                super.onPositiveClick(dialog);
            }
        });
        myAlertDialog.setMessage(msg);
        myAlertDialog.show();
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefresh.setRefreshing(false);
        viewBinding.recyclerView.setVisibility(View.GONE);
        viewBinding.noDataLayoutRecycler.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.noDataLayoutRecycler.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.noDataLayoutRecycler.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.noDataLayoutRecycler.tvNodata.setText(getString(R.string.place_vote));
            viewBinding.noDataLayoutRecycler.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
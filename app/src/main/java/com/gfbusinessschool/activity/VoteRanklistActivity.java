package com.gfbusinessschool.activity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.RanklistStoreAdapter;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.bean.VoteUserEntity;
import com.gfbusinessschool.databinding.ActivityVoteRanklistBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票排行
 */
@Route(path = ARouterPath.VoteRanklistActivity)
public class VoteRanklistActivity extends BaseActivity<ActivityVoteRanklistBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private RanklistStoreAdapter ranklistStoreAdapter;
    @Autowired
    String activeId;
    private int currentPage;
    private List<StoreEntity> mlist =new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.vote_ranklist));
        viewBinding.tabranklist.tab3.setVisibility(View.VISIBLE);
        viewBinding.tabranklist.tab2.setText(getString(R.string.head));
        viewBinding.tabranklist.tab3.setText(getString(R.string.name));
        viewBinding.tabranklist.tab4.setText(getString(R.string.vote_count));
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        ranklistStoreAdapter = new RanklistStoreAdapter(getApplicationContext());
        ranklistStoreAdapter.setLoadMore(true);
        ranklistStoreAdapter.setTypeView(RanklistStoreAdapter.TYPE_VIEW_VOTE_RANKLIST);
        viewBinding.recyclerView.setAdapter(ranklistStoreAdapter);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (ranklistStoreAdapter!=null)
                    ranklistStoreAdapter.setProgressBarVisiable(true);
                currentPage++;
                getVoteSelectionData(currentPage);
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
        getVoteInfo();
        currentPage =1;
        getVoteSelectionData(currentPage);
    }

    private void getVoteSelectionData(int page){
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        map.put("activeId",activeId);
        map.put("currPage",""+page);
        map.put("sort","1");//排序，1名次排序、2时间排序
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.VOTE_ACTIVE_USER_LIST, map, new NetWorkCallback() {
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
                    List<StoreEntity> list = JSONObject.parseArray(response,StoreEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        ranklistStoreAdapter.setProgressBarVisiable(false);
                    } else {
                        ranklistStoreAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false);
                        }else {
                            viewBinding.layoutVoteRanklist.setVisibility(View.VISIBLE);
                            viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                        }
                        mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (StoreEntity bean : list){
                            mlist.add(bean);
                        }
                    }
                    ranklistStoreAdapter.setmList(mlist);
                    ranklistStoreAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        viewBinding.layoutVoteRanklist.setVisibility(View.GONE);
        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.place_vote_user));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }

    private void getVoteInfo(){
        Map<String, String> map = new HashMap<>();
        map.put("activeId",activeId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.VOTE_ACTIVE_INFO, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    JSONObject object =JSONObject.parseObject(response);
                    if (object==null) return;
                    String totalPlayers =String.format(getString(R.string.place_competitioncount),
                            Utils.getString(object.getString("totalPlayers")));
                    SpannableStringBuilder builder = new SpannableStringBuilder(totalPlayers);
                    //设置文字
                    builder.setSpan(new AbsoluteSizeSpan(12, true), 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 4, totalPlayers.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.color_33));
                    ForegroundColorSpan span2 = new ForegroundColorSpan(getResources().getColor(R.color.color_B97322));
                    builder.setSpan(span, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    builder.setSpan(span2, 4, totalPlayers.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.competitionCount.setText(builder);
                    viewBinding.competitionCount.setMovementMethod(LinkMovementMethod.getInstance());

                    String voteNums =String.format(getString(R.string.place_votecount),
                            Utils.getString(object.getString("voteNums")));
                    SpannableStringBuilder buildervoteNums = new SpannableStringBuilder(voteNums);
                    //设置文字
                    buildervoteNums.setSpan(new AbsoluteSizeSpan(12, true), 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    buildervoteNums.setSpan(new AbsoluteSizeSpan(16, true), 4, voteNums.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan spanvoteNums = new ForegroundColorSpan(getResources().getColor(R.color.color_33));
                    ForegroundColorSpan spanvoteNums2 = new ForegroundColorSpan(getResources().getColor(R.color.color_B97322));
                    buildervoteNums.setSpan(spanvoteNums, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    buildervoteNums.setSpan(spanvoteNums2, 4, voteNums.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.voteCount.setText(buildervoteNums);
                    viewBinding.voteCount.setMovementMethod(LinkMovementMethod.getInstance());

                    String voteUsers =String.format(getString(R.string.place_joincount),
                            Utils.getString(object.getString("voteUsers")));
                    SpannableStringBuilder buildervoteUsers = new SpannableStringBuilder(voteUsers);
                    //设置文字
                    buildervoteUsers.setSpan(new AbsoluteSizeSpan(12, true), 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    buildervoteUsers.setSpan(new AbsoluteSizeSpan(16, true), 4, voteUsers.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan spanvoteUsers = new ForegroundColorSpan(getResources().getColor(R.color.color_33));
                    ForegroundColorSpan spanvoteUsers2 = new ForegroundColorSpan(getResources().getColor(R.color.color_B97322));
                    buildervoteUsers.setSpan(spanvoteUsers, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    buildervoteUsers.setSpan(spanvoteUsers2, 4, voteUsers.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.joinCount.setText(buildervoteUsers);
                    viewBinding.joinCount.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }
}
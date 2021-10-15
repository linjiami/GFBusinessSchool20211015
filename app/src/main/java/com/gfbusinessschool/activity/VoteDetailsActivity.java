package com.gfbusinessschool.activity;

import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.VoteAdapter;
import com.gfbusinessschool.adapter.VoteSelectionAdapter;
import com.gfbusinessschool.bean.VoteEntity;
import com.gfbusinessschool.bean.VoteUserEntity;
import com.gfbusinessschool.databinding.ActivityVoteDetailsBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.gfbusinessschool.view.MyGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票详情页
 */
@Route(path = ARouterPath.VoteDetailsActivity)
public class VoteDetailsActivity extends BaseActivity<ActivityVoteDetailsBinding> implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private VoteAdapter voteAdapter;
    @Autowired
    VoteEntity voteEntity;
    private int currentPage;
    private List<VoteUserEntity> mlist =new ArrayList<>();

    @Override
    protected void initView() {
        super.initView();
        if (voteEntity==null || Utils.isEmpty(voteEntity.getId())){
            ToastUtil.show(getApplicationContext(),getString(R.string.alet_vote_id));
            finish();
            return;
        }
        Utils.setBackgroundStoken(getApplicationContext(),viewBinding.layoutSearchBtn1.searchNameTV);
        Utils.setBackgroundStoken(getApplicationContext(),viewBinding.layoutSearchBtn2.searchNameEt);
        Utils.setBackgroundSolid(getApplicationContext(),viewBinding.layoutSearchBtn2.cancleBtn);
        viewBinding.bottomLayout.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.titleBar.setTitle(getString(R.string.vote_details));
        viewBinding.bottomLayout.releaseShareBtn.setText(getString(R.string.vote_ranklist));
        GlideLoadUtils.load(getApplicationContext(),voteEntity.getImgUrl(),viewBinding.voteIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.iconActivitiesNotice.setOnClickListener(this);
        viewBinding.bottomLayout.releaseShareBtn.setOnClickListener(this);
        viewBinding.layoutSearchBtn1.searchNameTV.setOnClickListener(this);
        viewBinding.layoutSearchBtn2.cancleBtn.setOnClickListener(this);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewBinding.voteIcon.getLayoutParams();
        int width = (int) (MyApplication.getInstance().screenWidth-getResources().getDimension(R.dimen.left_app)*2);
        lp.height = width * 264 / 697;
        viewBinding.voteIcon.setLayoutParams(lp);
        viewBinding.recyclerView.setLayoutManager(new MyGridLayoutManager(this,2));
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (voteAdapter!=null)
                    voteAdapter.setProgressBarVisiable(true);
                currentPage++;
                String contentSearch = viewBinding.layoutSearchBtn2.searchNameEt.getText().toString();
                getVoteSelectionData(currentPage,Utils.isEmpty(contentSearch)?"":contentSearch.trim());
            }
        });
        voteAdapter =new VoteAdapter(getApplicationContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                VoteUserEntity entity =mlist.get(position);
                if (entity!=null && !Utils.isEmpty(entity.getId()))
                    postVote(entity.getId(),position);
            }
        });
        viewBinding.recyclerView.setAdapter(voteAdapter);
        viewBinding.layoutSearchBtn2.searchNameEt.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
                String contentSearch = viewBinding.layoutSearchBtn2.searchNameEt.getText().toString();
                if (!Utils.isEmpty(contentSearch)) {
                    hideKeyBoard();
                    currentPage=1;
                    getVoteSelectionData(currentPage,contentSearch.trim());
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入搜索内容");
                }
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancleBtn://取消
                viewBinding.layoutSearchBtn1.getRoot().setVisibility(View.VISIBLE);
                viewBinding.layoutSearchBtn2.getRoot().setVisibility(View.GONE);
                viewBinding.layoutSearchBtn2.searchNameEt.setText("");
                initData();
                break;
            case R.id.searchNameTV://搜索
                viewBinding.layoutSearchBtn2.getRoot().setVisibility(View.VISIBLE);
                viewBinding.layoutSearchBtn1.getRoot().setVisibility(View.GONE);
                showKeyBoard(viewBinding.layoutSearchBtn2.searchNameEt);
                break;
            case R.id.iconActivitiesNotice://投票须知
                if (voteEntity==null) return;
                String msg =String.format(getString(R.string.place_vote_dialog),Utils.getTimeFormat(voteEntity.getStartDate()),
                        Utils.getTimeFormat(voteEntity.getEndDate()),
                        voteEntity.getIntroduce(),voteEntity.getVoteAstrict(),voteEntity.getPrize());
                MyAlertDialog myAlertDialog = new MyAlertDialog(VoteDetailsActivity.this);
                myAlertDialog.setTitle(getString(R.string.vote_notice));
                myAlertDialog.setMessage(msg);
                myAlertDialog.setMyDialogCallback(new MyDialogCallback() {
                    @Override
                    public void onPositiveClick(MyAlertDialog dialog) {
                        super.onPositiveClick(dialog);
                    }
                });
                if (myAlertDialog.getMessageView()!=null)myAlertDialog.getMessageView().setGravity(Gravity.LEFT);
                myAlertDialog.show();
                break;
            case R.id.releaseShareBtn://投票排行
                if (voteEntity==null) return;
                ARouter.getInstance().build(ARouterPath.VoteRanklistActivity).withString("activeId",voteEntity.getId()).navigation();
                break;
        }
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshLayout.setRefreshing(true);
        if (viewBinding.layoutSearchBtn2.getRoot().getVisibility() == View.VISIBLE){
            viewBinding.layoutSearchBtn1.getRoot().setVisibility(View.VISIBLE);
            viewBinding.layoutSearchBtn2.getRoot().setVisibility(View.GONE);
            viewBinding.layoutSearchBtn2.searchNameEt.setText("");
            hideKeyBoard();
        }
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage=1;
        getVoteSelectionData(currentPage,"");
    }

    private void getVoteSelectionData(int page,String searchContent){
        if (voteEntity==null || Utils.isEmpty(voteEntity.getId()))return;
        if (page==1 && mlist!=null) mlist.clear();
        Map<String, String> map = new HashMap<>();
        map.put("activeId",voteEntity.getId());
        map.put("currPage",""+page);
        map.put("sort","2");//排序，1名次排序、2时间排序
        if (!Utils.isEmpty(searchContent)) map.put("playerName",searchContent);
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
                    List<VoteUserEntity> list = JSONObject.parseArray(response,VoteUserEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        voteAdapter.setProgressBarVisiable(false);
                    } else {
                        voteAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            showNullListView(false);
                        }else {
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                            viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                        }
                        mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (VoteUserEntity bean : list){
                            mlist.add(bean);
                        }
                    }
                    voteAdapter.setList(mlist);
                    voteAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        viewBinding.recyclerView.setVisibility(View.GONE);
        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.place_vote_user));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }

    private void postVote(String playerId,int position){
        Map<String, String> map = new HashMap<>();
        map.put("playerId",""+playerId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.VOTE_ACTIVE_POST, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_vote_success));
                    int voteNums =0;
                    if (!Utils.isEmpty(mlist.get(position).getVoteNums()))
                        voteNums =Integer.parseInt(mlist.get(position).getVoteNums());
                    voteNums++;
                    mlist.get(position).setVoteNums(voteNums+"");
                    voteAdapter.notifyItemChanged(position);
                }
            }
        });
    }
}
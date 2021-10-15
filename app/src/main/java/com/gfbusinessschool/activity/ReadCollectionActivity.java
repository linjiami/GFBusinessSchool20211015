package com.gfbusinessschool.activity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ReadCollectionAdapter;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.databinding.ActivityReadcollectionBinding;
import com.gfbusinessschool.dialog.PictureLookDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读书汇分享（所有的）
 */
@Route(path = ARouterPath.ReadCollectionActivity)
public class ReadCollectionActivity extends BaseActivity<ActivityReadcollectionBinding> implements SwipeRefreshLayout.OnRefreshListener ,
        View.OnClickListener {
    private ReadCollectionAdapter readCollectionAdapter;
    private List<ReadCollectionEntity.Entity> mlist =new ArrayList<>();
    private int currPage;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.read_collection));
        viewBinding.layoutSearchBtn1.searchNameTV.setText(getString(R.string.hint_search_readcollection));
        viewBinding.layoutSearchBtn2.searchNameEt.setHint(getString(R.string.hint_search_readcollection));
        Utils.setBackgroundStoken(getApplicationContext(),viewBinding.layoutSearchBtn1.searchNameTV);
        Utils.setBackgroundStoken(getApplicationContext(),viewBinding.layoutSearchBtn2.searchNameEt);
        Utils.setBackgroundSolid(getApplicationContext(),viewBinding.layoutSearchBtn2.cancleBtn);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.layoutSearchBtn1.searchNameTV.setOnClickListener(this);
        viewBinding.layoutSearchBtn2.cancleBtn.setOnClickListener(this);
        viewBinding.layoutSearchBtn2.searchNameEt.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
                String contentSearch = viewBinding.layoutSearchBtn2.searchNameEt.getText().toString().trim();
                if (!Utils.isEmpty(contentSearch)) {
                    currPage=1;
                    getReadCollectionData(currPage,contentSearch);
                    hideKeyBoard();
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入搜索内容");
                }
            }
            return false;
        });
        readCollectionAdapter =new ReadCollectionAdapter(getApplicationContext(),ReadCollectionAdapter.TYPE_READCOLLECTION_HOMEPAGER, new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                ReadCollectionEntity.Entity entity =mlist.get(position);
                if (entity!=null)
                    ARouter.getInstance().build(ARouterPath.ReadCollectionDetailsActivity)
                            .withString("shareId",entity.getId()).navigation();
            }

            @Override
            public void onClickReadCollection(int position) {
                if (mlist.get(position)==null) return;
                ReadCollectionEntity.Entity entity =mlist.get(position);
                if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                    if (!Utils.isEmpty(entity.getFileUrl())){
                        PictureLookDialog dialog =new PictureLookDialog(ReadCollectionActivity.this,entity.getFileUrl());
                        dialog.setType(PictureLookDialog.TYPE_READCOLLECTION);
                        dialog.show();
                    }
                }else if (FileTypeUtils.isVideoFileType(entity.getFileUrl())){
                    ARouter.getInstance().build(ARouterPath.VideoFullScreenActivity)
                            .withString("videoUrl",entity.getFileUrl()).navigation();
                }else if (Utils.getString(entity.getFileUrl()).toLowerCase().endsWith("pdf")){
                    ARouter.getInstance().build(ARouterPath.PdfViewerActvity)
                            .withString("urlPdf",entity.getFileUrl())
                            .withString("pdfTitle",entity.getTitle()).navigation();
                }else if (FileTypeUtils.isWordFileType(entity.getFileUrl()) ||
                        FileTypeUtils.isPPTFileType(entity.getFileUrl())) {
                    ARouter.getInstance().build(ARouterPath.WordViewerActivity)
                            .withString("urlPdf",entity.getFileUrl())
                            .withString("pdfName",entity.getTitle())
                            .withString("pdfTitle",entity.getTitle()).navigation();
                }
            }
        });
        viewBinding.recyclerView.setAdapter(readCollectionAdapter);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( readCollectionAdapter!=null)
                    readCollectionAdapter.setProgressBarVisiable(true);
                currPage++;
                getReadCollectionData(currPage,"");
            }
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
        currPage =1;
        getReadCollectionData(currPage,"");
    }


    private void getReadCollectionData(int page,String searchContent){
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("queryType","0");
        map.put("currPage",""+page);
        map.put("title",searchContent);
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.READCOLLECTION_LIST, map, new NetWorkCallback() {
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
                    List<ReadCollectionEntity.Entity> list = JSONObject.parseArray(response,ReadCollectionEntity.Entity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size() < 15) {
                        readCollectionAdapter.setProgressBarVisiable(false);
                    } else {
                        readCollectionAdapter.setProgressBarVisiable(true);
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
                            currPage--;
                        }
                        for (ReadCollectionEntity.Entity bean : list){
                            mlist.add(bean);
                        }
                    }
                    readCollectionAdapter.setMlist(mlist);
                    readCollectionAdapter.notifyDataSetChanged();
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
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.place_no_readcollection));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
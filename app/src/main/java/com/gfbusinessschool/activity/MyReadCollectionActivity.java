package com.gfbusinessschool.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
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
import com.gfbusinessschool.databinding.FragmentCourseBinding;
import com.gfbusinessschool.dialog.PictureLookDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的分享读书汇列表
 */
@Route(path = ARouterPath.MyReadCollectionActivity)
public class MyReadCollectionActivity extends BaseActivity<FragmentCourseBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private int currPage;
    private List<ReadCollectionEntity.Entity> mlist =new ArrayList<>();
    private ReadCollectionAdapter readCollectionAdapter;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titlebarCourse.setVisibility(View.VISIBLE);
        viewBinding.bottomLayout.getRoot().setVisibility(View.VISIBLE);
        viewBinding.bottomLayout.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.swipeCourse.setBackgroundColor(getResources().getColor(R.color.color_f4));
        viewBinding.titlebarCourse.setTitle(getString(R.string.read_collection_share));
        viewBinding.courseNoData.tvNodata.setText(getString(R.string.place_myreadcollcetion));
        viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        viewBinding.recyclerViewCourse.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        viewBinding.swipeCourse.setOnRefreshListener(this);

        readCollectionAdapter =new ReadCollectionAdapter(getApplicationContext(),ReadCollectionAdapter.TYPE_READCOLLECTION_MY, new OnClickCallBack() {
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
                        PictureLookDialog dialog =new PictureLookDialog(MyReadCollectionActivity.this,entity.getFileUrl());
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
        viewBinding.recyclerViewCourse.setAdapter(readCollectionAdapter);
        viewBinding.bottomLayout.releaseShareBtn.setOnClickListener(v -> {
            //发布分享
            ARouter.getInstance().build(ARouterPath.ReleaseReadCollectionActivity).navigation(this,REQUEST_CODE_RELEASE_READCOLLECTION);
        });
        viewBinding.nestedScrollViewCourse.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( readCollectionAdapter!=null)
                    readCollectionAdapter.setProgressBarVisiable(true);
                currPage++;
                getReadCollectionData(currPage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RELEASE_READCOLLECTION && resultCode == RESULT_OK) {
            initData();
        }
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        currPage =1;
        getReadCollectionData(currPage);
    }

    private void getReadCollectionData(int page){
        if (page==1) mlist.clear();
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("queryType","1");
        map.put("currPage",""+page);
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.READCOLLECTION_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
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
                            viewBinding.recyclerViewCourse.setVisibility(View.GONE);
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
                        }else {
                            viewBinding.recyclerViewCourse.setVisibility(View.VISIBLE);
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
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
}
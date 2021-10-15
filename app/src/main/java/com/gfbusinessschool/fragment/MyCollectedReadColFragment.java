package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.adapter.ReadCollectionAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.databinding.FragmentMycollectedCourseBinding;
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

public class MyCollectedReadColFragment extends BaseFragment<FragmentMycollectedCourseBinding> implements SwipeRefreshLayout.OnRefreshListener{

    private int currentPage;
    private ReadCollectionAdapter readCollectionAdapter;
    private List<ReadCollectionEntity.Entity> mlist = new ArrayList<>();

    @Override
    protected void initView() {
        viewBinding.rootLayoutMyCollected.setBackgroundColor(getResources().getColor(R.color.color_f4));
        viewBinding.courseNoData.tvNodata.setText(getString(R.string.noData));
        viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        viewBinding.slideRecyclerViewCourse.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.swipeCourse.setOnRefreshListener(this);

        viewBinding.nestedScrollViewCourse.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( readCollectionAdapter !=null)
                    readCollectionAdapter.setProgressBarVisiable(true);
                currentPage++;
                requestCourseListData(currentPage);
            }
        });

        readCollectionAdapter =new ReadCollectionAdapter(getContext(),ReadCollectionAdapter.TYPE_READCOLLECTION_COLLECTED, new OnClickCallBack() {
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
                        PictureLookDialog dialog =new PictureLookDialog(getActivity(),entity.getFileUrl());
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
        viewBinding.slideRecyclerViewCourse.setAdapter(readCollectionAdapter);
    }

    @Override
    protected void initData() {
        currentPage =1;
        requestCourseListData(currentPage);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        initData();
    }

    /**
     * 课程列表
     * @param page 0：不分页
     */
    private void requestCourseListData(int page) {
        if (page==1) mlist.clear();
        Map<String,String> map =new HashMap<>();
        map.put("currPage",""+page);
        map.put("queryType","2");//0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        NetWorkUtils.getResultList(getContext(), InterfaceClass.READCOLLECTION_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
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
                            showNullListView(false);
                        }else {
                            viewBinding.slideRecyclerViewCourse.setVisibility(View.VISIBLE);
                            viewBinding.courseNoData.noDataLayout.setVisibility(View.GONE);
                        }
                        mlist =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
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
        viewBinding.swipeCourse.setRefreshing(false);
        viewBinding.slideRecyclerViewCourse.setVisibility(View.GONE);
        viewBinding.courseNoData.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.courseNoData.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.courseNoData.tvNodata.setText(getString(R.string.place_no_readcollection));
            viewBinding.courseNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}
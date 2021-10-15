package com.gfbusinessschool.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CommentAdapter;
import com.gfbusinessschool.bean.CommentBean;
import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.databinding.ActivityReadcollectionDetailsBinding;
import com.gfbusinessschool.dialog.PictureLookDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读书汇分享详情页
 */
@Route(path = ARouterPath.ReadCollectionDetailsActivity)
public class ReadCollectionDetailsActivity extends BaseActivity<ActivityReadcollectionDetailsBinding> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private CommentAdapter commentAdapter;
    private int currentPage;
    private List<CommentBean> mList = new ArrayList<>();
    @Autowired
    String shareId;
    private ReadCollectionEntity.Entity entity;
    private int priseCountLimit =4;//点赞个数限制，5次发一次请求

    @Override
    protected void initView() {
        super.initView();
        if (Utils.isEmpty(shareId)){
            ToastUtil.show(getApplicationContext(),getString(R.string.alert_readcollectionId));
            finish();
        }
        viewBinding.titleBar.setTitle(getString(R.string.details));
        Utils.setImageViewTint(getApplicationContext(),viewBinding.priseBtn);
        viewBinding.titleBar.getRightIconTitleBar().setVisibility(View.VISIBLE);
        viewBinding.titleBar.getRigthLayout().setOnClickListener(this);
        viewBinding.layoutNoData.tvNodata.setText(getString(R.string.comment_no_data_alert));
        viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.place_comment);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if ( commentAdapter!=null)
                    commentAdapter.setProgressBarVisiable(true);
                currentPage++;
                getCommentData(currentPage);
            }
        });
        viewBinding.itemCourseWare.fileName.setOnClickListener(this);
        viewBinding.priseBtn.setOnClickListener(this);
        commentAdapter =new CommentAdapter(getApplicationContext());
        viewBinding.recyclerView.setAdapter(commentAdapter);
        viewBinding.conmmentEt.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEND) && event != null) {
                String content = viewBinding.conmmentEt.getText().toString().trim();
                if (!Utils.isEmpty(content)) {
                    hideKeyBoard();
                    postComment(content);
                }else {
                    ToastUtil.show(getApplicationContext(),getString(R.string.null_comment_alert_msg));
                }
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTitleBar://收藏
                if (Utils.getString(entity.getIsCollection()).equals("1")){
                    cancleFollow();
                }else {
                    postPriseFollow(1);
                }
                break;
            case R.id.priseBtn://点赞
                priseCountLimit++;
                if (priseCountLimit==5){
                    postPriseFollow(0);
                    priseCountLimit =0;
                }
                viewBinding.flowLikeView.addLikeView();
                break;
            case R.id.fileName:
                if (entity==null) return;
                if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                    if (!Utils.isEmpty(entity.getFileUrl())){
                        PictureLookDialog dialog =new PictureLookDialog(this,entity.getFileUrl());
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
                            .withString("pdfName",viewBinding.itemCourseWare.fileName.getText().toString())
                            .withString("pdfTitle",entity.getTitle()).navigation();
                }
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
        getReadCollectionDetails();
        currentPage =1;
        getCommentData(currentPage);
    }

    private void getReadCollectionDetails() {
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("id",shareId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.READCOLLECTION_INFO, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    entity = JSONObject.parseObject(response,ReadCollectionEntity.Entity.class);
                    if (entity==null) return;
                    viewBinding.title.setText(Utils.getString(entity.getTitle()));
                    viewBinding.shareName.setText(Utils.getString(entity.getUserName()));
                    viewBinding.time.setText(Utils.getString(entity.getCreateDate()));
                    viewBinding.shareContent.setText(Utils.getString(entity.getContent()));
                    GlideLoadUtils.load(getApplicationContext(),entity.getHeadImgUrl(),viewBinding.headIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                    if (Utils.getString(entity.getIsCollection()).equals("1"))
                        viewBinding.titleBar.getRightIconTitleBar().setImageResource(R.mipmap.followed);
                    else
                        viewBinding.titleBar.getRightIconTitleBar().setImageResource(R.mipmap.follow);
                    Utils.setImageViewTint(getApplicationContext(),viewBinding.titleBar.getRightIconTitleBar());
                    if (FileTypeUtils.isImageFileType(entity.getFileUrl())){
                        Utils.setDrawableTint(getApplicationContext(),R.mipmap.img_readcollection,viewBinding.itemCourseWare.fileName,0);
                        viewBinding.itemCourseWare.fileName.setText(Utils.getString(entity.getTitle())+".JPG");
                    }else if (FileTypeUtils.isVideoFileType(entity.getFileUrl())){
                        Utils.setDrawableTint(getApplicationContext(),R.mipmap.video_readcollection,viewBinding.itemCourseWare.fileName,0);
                        viewBinding.itemCourseWare.fileName.setText(Utils.getString(entity.getTitle())+".MP4");
                    }else {
                        Utils.setDrawableTint(getApplicationContext(),R.mipmap.file_readcollection,viewBinding.itemCourseWare.fileName,0);
                        viewBinding.itemCourseWare.fileName.setText(Utils.getString(entity.getTitle()));
                        if (!Utils.isEmpty(entity.getFileUrl())){
                            String[] patharr =entity.getFileUrl().split("\\.");
                            if (patharr.length>0)
                                viewBinding.itemCourseWare.fileName.setText(Utils.getString(entity.getTitle())+"."+patharr[patharr.length-1]);
                        }
                    }
                    viewBinding.countPrise.setText(String.format(getString(R.string.place_prise), ""+entity.getLikeAmount()));
                    if (entity.getPassRate()==1)
                        viewBinding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
                    else
                        viewBinding.biyexuanzhangIcon.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 评价列表
     * @param page
     */
    private void getCommentData(int page) {
        if (page==1) mList.clear();
        Map<String, String> map = new HashMap<>();
        map.put("id",shareId);
        map.put("currPage",""+page);
        NetWorkUtils.getResultList(getApplicationContext(), InterfaceClass.READCOLLECTION_COMMENT_LIST, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                viewBinding.recyclerView.setVisibility(View.GONE);
                viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshLayout.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    List<CommentBean> list = JSONArray.parseArray(response,CommentBean.class);
                    if (list.size() < 15) {
                        commentAdapter.setProgressBarVisiable(false);
                    } else {
                        commentAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.recyclerView.setVisibility(View.GONE);
                        }else {
                            viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        mList =list;
                    }else {
                        if (list.size() == 0) {
                            currentPage--;
                        }
                        for (CommentBean bean : list){
                            mList.add(bean);
                        }
                    }
                    commentAdapter.setList(mList);
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 点赞/收藏
     * @param type 0点赞 1收藏(默认点赞)
     */
    private void postPriseFollow(int type) {
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("id",shareId);
        map.put("type",""+type);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.READCOLLECTION_LIKE_FOLLOW, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    if (type==0){
                        ToastUtil.show(getApplicationContext(),getString(R.string.prise_success));
                        int priseCount =entity.getLikeAmount();
                        priseCount++;
                        entity.setLikeAmount(priseCount);
                        viewBinding.countPrise.setText(String.format(getString(R.string.place_prise), ""+priseCount));
                    }else {
                        ToastUtil.show(getApplicationContext(),getString(R.string.followed_success));
                        if (entity!=null) entity.setIsCollection("1");
                        viewBinding.titleBar.getRightIconTitleBar().setImageResource(R.mipmap.followed);
                        Utils.setImageViewTint(getApplicationContext(),viewBinding.titleBar.getRightIconTitleBar());
                    }
                }
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancleFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("id",shareId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.READCOLLECTION_CANCLEFOLLOW, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.follow_success));
                    if (entity!=null) entity.setIsCollection("0");
                    viewBinding.titleBar.getRightIconTitleBar().setImageResource(R.mipmap.follow);
                    Utils.setImageViewTint(getApplicationContext(),viewBinding.titleBar.getRightIconTitleBar());
                }
            }
        });
    }

    /**
     * 发表评论
     */
    private void postComment(String comment) {
        Map<String, String> map = new HashMap<>();
        //0查询所有分享，1查询个人分享，2查询我的收藏(默认查询所有)
        map.put("id",shareId);
        map.put("comment",comment);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.READCOLLECTION_COMMENT, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.show(getApplicationContext(),getString(R.string.comment_success));
                    currentPage=1;
                    getCommentData(currentPage);
                }
            }
        });
    }
}
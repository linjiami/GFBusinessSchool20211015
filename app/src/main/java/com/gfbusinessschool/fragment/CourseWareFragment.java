package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseWareAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.CourseWareEntity;
import com.gfbusinessschool.databinding.FragmentCourseCommentBinding;
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

public class CourseWareFragment extends BaseFragment<FragmentCourseCommentBinding> implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TYPEVIEW_CLASS ="必修专题课件";
    public static final String TYPEVIEW_CLASS_SPECIAL ="精选专题课件";
    public static final String TYPEVIEW_STUDYMAP ="学习地图课件";
    public static final String TYPEVIEW_COURSE ="课程课件";
    public static final String TYPEVIEW_AUDIO ="音频课件";
    private String typeView;
    private CourseWareAdapter courseWareAdapter;
    private List<CourseWareEntity> mListCourse = new ArrayList<>();
    private String id;
    private OnClickCallBack onClickCallBack;

    public CourseWareFragment(String typeView, String id, OnClickCallBack onClickCallBack) {
        this.typeView = typeView;
        this.id = id;
        this.onClickCallBack = onClickCallBack;
    }

    public CourseWareFragment() {
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourseBean(CourseBean courseBean) {
        if (courseBean==null) return;
        viewBinding.layoutCourseTop.courseTitle.setText(Utils.getString(courseBean.getName()));
        if (Utils.getString(typeView).equals(TYPEVIEW_COURSE)){
            viewBinding.layoutCourseTop.countCourse.setText(courseBean.getTitle());
        }else if (Utils.getString(typeView).equals(TYPEVIEW_STUDYMAP)){
            viewBinding.layoutCourseTop.countCourse.setText(String.format(getString(R.string.place_course_count2),
                    Utils.getIntData(courseBean.getCourseNum())+""));
        } else {
            viewBinding.layoutCourseTop.countCourse.setText(String.format(getString(R.string.place_course_count),
                    Utils.getIntData(courseBean.getCourseNum())+"",Utils.getIntData(courseBean.getStuNum())+""));
        }
    }

    public void setTopText(String title, String subtitle) {
        viewBinding.layoutCourseTop.courseTitle.setText(Utils.getString(title));
        viewBinding.layoutCourseTop.countCourse.setText(Utils.getString(subtitle));
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.layoutSendComment.setVisibility(View.GONE);
        viewBinding.commentTitle.setText(getString(R.string.courseware_list));
        viewBinding.rvComment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        courseWareAdapter =new CourseWareAdapter(getContext(), new OnClickCallBack() {
            @Override
            public void onClick(int position) {
                CourseWareEntity entity =mListCourse.get(position);
                if (entity==null) return;
                if (Utils.getString(entity.getCourseWareUrl()).toLowerCase().endsWith("pdf")){
                    ARouter.getInstance().build(ARouterPath.PdfViewerActvity)
                            .withString("urlPdf",entity.getCourseWareUrl())
                            .withBoolean("isAudioCourseWare",Utils.getString(typeView).equals(TYPEVIEW_AUDIO))
                            .withString("pdfTitle",entity.getName()).navigation();
                } else if (FileTypeUtils.isWordFileType(entity.getCourseWareUrl()) ||
                        FileTypeUtils.isPPTFileType(entity.getCourseWareUrl())) {
                    ARouter.getInstance().build(ARouterPath.WordViewerActivity)
                            .withString("urlPdf",entity.getCourseWareUrl())
                            .withString("pdfName",entity.getName())
                            .withBoolean("isAudioCourseWare",Utils.getString(typeView).equals(TYPEVIEW_AUDIO))
                            .withString("pdfTitle",entity.getName()).navigation();
                }else if (FileTypeUtils.isImageFileType(entity.getCourseWareUrl())){
                    PictureLookDialog dialog =new PictureLookDialog(getActivity(),entity.getCourseWareUrl());
                    dialog.setType(PictureLookDialog.TYPE_READCOLLECTION);
                    dialog.show();
                }
                if (onClickCallBack!=null) onClickCallBack.onClick();
            }
        });
        viewBinding.rvComment.setAdapter(courseWareAdapter);
        viewBinding.swipeRefreshComment.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh() {
        viewBinding.swipeRefreshComment.setRefreshing(true);
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        getCourseListData();
    }

    private void getCourseListData() {
        mListCourse.clear();
        Map<String,String> map =new HashMap<>();
        //课件类型（1必修专题2课程中心专题3学习地图专题4课程5音频）
        if (Utils.getString(typeView).equals(TYPEVIEW_CLASS))
            map.put("type","1");
        else if (Utils.getString(typeView).equals(TYPEVIEW_CLASS_SPECIAL))
            map.put("type","2");
        else if (Utils.getString(typeView).equals(TYPEVIEW_STUDYMAP))
            map.put("type","3");
        else if (Utils.getString(typeView).equals(TYPEVIEW_COURSE))
            map.put("type","4");
        else if (Utils.getString(typeView).equals(TYPEVIEW_AUDIO))
            map.put("type","5");
        map.put("rid",id);
        NetWorkUtils.getResultArray(getContext(), InterfaceClass.COURSEWARE_LIST, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    viewBinding.swipeRefreshComment.setRefreshing(false);
                    mListCourse = JSONArray.parseArray(response, CourseWareEntity.class);
                    if (mListCourse==null) mListCourse =new ArrayList<>();
                    if (mListCourse.size() == 0) {
                        showNullListView(false);
                    }else {
                        viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.GONE);
                        viewBinding.rvComment.setVisibility(View.VISIBLE);
                        courseWareAdapter.setList(mListCourse);
                        courseWareAdapter.notifyDataSetChanged();
                    }
                }else {
                    showNullListView(true);
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.swipeRefreshComment.setRefreshing(false);
        viewBinding.rvComment.setVisibility(View.GONE);
        viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.layoutNoDataComment.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.layoutNoDataComment.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.layoutNoDataComment.tvNodata.setText(getString(R.string.noData));
            viewBinding.layoutNoDataComment.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}

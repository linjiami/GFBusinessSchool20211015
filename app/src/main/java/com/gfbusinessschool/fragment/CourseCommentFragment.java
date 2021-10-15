package com.gfbusinessschool.fragment;

import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CommentAdapter;
import com.gfbusinessschool.bean.CommentBean;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentCourseCommentBinding;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程评价
 */
public class CourseCommentFragment extends BaseFragment<FragmentCourseCommentBinding> implements SwipeRefreshLayout.OnRefreshListener
, View.OnClickListener {
    public static final String TYPE_COMMENT_COURSE = "课程评价";
    private String courseid;
    private CommentAdapter commentAdapter;
    private int currentPage;
    private List<CommentBean> mList = new ArrayList<>();
    private String typeView;

    public CourseCommentFragment() {
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    public CourseCommentFragment(String courseid) {
        this.courseid = courseid;
    }

    public void setCourseBean(CourseBean courseBean) {
        if (courseBean==null) return;
        viewBinding.layoutCourseTop.courseTitle.setText(Utils.getString(courseBean.getName()));
        if (Utils.getString(typeView).equals(TYPE_COMMENT_COURSE))
            viewBinding.layoutCourseTop.countCourse.setText(Utils.getString(courseBean.getTitle()));
        else
            viewBinding.layoutCourseTop.countCourse.setText(String.format(getString(R.string.place_course_count),
                Utils.getString(courseBean.getTestId()),Utils.getString(courseBean.getStuNum())));
    }

    @Override
    protected void initView() {
        MyStatusBarUtils.setTransparent(getActivity(),true);
        viewBinding.rootLayout.setOnClickListener(this);
        viewBinding.swipeRefreshComment.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.rvComment.setLayoutManager(layoutManager);
        viewBinding.layoutNoDataComment.tvNodata.setText(getString(R.string.comment_no_data_alert));
        viewBinding.layoutNoDataComment.iconNodata.setImageResource(R.mipmap.place_comment);
        viewBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    currentPage++;
                    commentAdapter.setProgressBarVisiable(true);
                    loadCommentListData(currentPage);
                }
            }
        });
        commentAdapter = new CommentAdapter(getContext());
        viewBinding.rvComment.setAdapter(commentAdapter);
        viewBinding.sendComment.setOnClickListener(this);
    }

    private void onClickSendBtn() {
        String commentMsg= viewBinding.commentET.getText().toString();
        if (commentMsg.trim().isEmpty()) {
            ToastUtil.showToast(getContext(),getString(R.string.null_comment_alert_msg));
            return;
        }
        viewBinding.commentET.setText("");
        hideKeyBoard();
        postCommentApi(commentMsg);
    }

    @Override
    protected void initData() {
        showLoadingDialog();
        currentPage = 1;
        loadCommentListData(currentPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rootLayout:
                if (getActivity() != null) Utils.hideSoftInputFromWindow(getActivity(), viewBinding.rootLayout);
                break;
            case R.id.sendComment:
                onClickSendBtn();
                break;
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadCommentListData(currentPage);
        viewBinding.swipeRefreshComment.setRefreshing(true);
    }

    private void loadCommentListData(final int page) {
        if (courseid == null) return;
        if (page == 1) {
            mList.clear();
        }
        String url = InterfaceClass.COURSE_COMMENT_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("courseId", courseid);
        map.put("currPage", page + "");
        NetWorkUtils.getResultArray(getContext(), url, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                dismissLoadingDialog();
                viewBinding.swipeRefreshComment.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefreshComment.setRefreshing(false);
                dismissLoadingDialog();
                if (Utils.getString(code).equals("200")){
                    List<CommentBean> list = JSONArray.parseArray(response,CommentBean.class);
                    if (list.size() < 15) {
                        commentAdapter.setProgressBarVisiable(false);
                    } else {
                        commentAdapter.setProgressBarVisiable(true);
                    }
                    if (page==1){
                        if (list.size() == 0) {
                            viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.VISIBLE);
                            viewBinding.rvComment.setVisibility(View.GONE);
                        }else {
                            viewBinding.layoutNoDataComment.noDataLayout.setVisibility(View.GONE);
                            viewBinding.rvComment.setVisibility(View.VISIBLE);
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
     * 评价
     */
    private void postCommentApi(String comment) {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", courseid);
        map.put("content", comment.trim());
        NetWorkUtils.postResultString(getContext(), InterfaceClass.COURSE_COMMENT_SEND, map, new NetWorkCallback() {

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    ToastUtil.showToast(getContext(), "评价成功");
                    currentPage=1;
                    loadCommentListData(currentPage);
                }
            }
        });
    }
}

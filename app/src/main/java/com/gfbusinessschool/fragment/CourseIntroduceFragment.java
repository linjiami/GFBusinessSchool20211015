package com.gfbusinessschool.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.ClassDetailsActivity;
import com.gfbusinessschool.activity.CourseDetailsActivity;
import com.gfbusinessschool.adapter.TeacherClassAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.FragmentCourseIntroduceBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;

/**
 * 课程介绍
 */
public class CourseIntroduceFragment extends BaseFragment<FragmentCourseIntroduceBinding> {
    public static final String TYPE_VIEW_COURSE ="课程介绍";
    public static final String TYPE_VIEW_CLASS ="专题介绍";
    public static final String TYPE_VIEW_CLASS_STUDYMAP ="学习地图专题介绍";
    private String typeView = TYPE_VIEW_COURSE;

    public CourseIntroduceFragment() {
    }

    public void setCourseDetailsBean(CourseBean courseDetailsBean) {
        if (courseDetailsBean != null) {
            refreshDataView(courseDetailsBean);
        }
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    @Override
    protected void initView() {
        if (Utils.getString(typeView).equals(TYPE_VIEW_CLASS) ||
                Utils.getString(typeView).equals(TYPE_VIEW_CLASS_STUDYMAP)){
            viewBinding.titleCourseIntroduction.setText(getString(R.string.class_introduce));
            viewBinding.recyclerViewTeacher.setVisibility(View.VISIBLE);
            viewBinding.courseTeacherLayuout.setVisibility(View.GONE);
            viewBinding.recyclerViewTeacher.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            String packageName = getActivity().getPackageName();
            if (!packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
        //设置自适应屏幕，两者合用
        viewBinding.webViewCourse.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        viewBinding.webViewCourse.getSettings().setLoadWithOverviewMode(true);
        //支持js
        viewBinding.webViewCourse.getSettings().setJavaScriptEnabled(true);
        viewBinding.webViewCourse.getSettings().setBlockNetworkImage(false);
        viewBinding.webViewCourse.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webViewCourse.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        viewBinding.webViewCourse.loadUrl("");
    }

    @Override
    protected void initData() {
    }

    public void refreshDataView(CourseBean courseDetailsBean) {
        viewBinding.titleCourseDetails.setText(Utils.getString(courseDetailsBean.getName()));
        viewBinding.subtitleCourseDetails.setText(Utils.getString(courseDetailsBean.getTitle()));
        GlideLoadUtils.load(getContext(), courseDetailsBean.getHeadImgUrl(), viewBinding.headIcon, GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
        viewBinding.teacherName.setText(Utils.getString(courseDetailsBean.getTeacherName()));
        viewBinding.teacherFlag.setText(Utils.getString(courseDetailsBean.getPersonalIntroduction()));
        if (courseDetailsBean.getTeacherEntityList()!=null && courseDetailsBean.getTeacherEntityList().size()>0){
            TeacherClassAdapter classAdapter =new TeacherClassAdapter(getContext(), courseDetailsBean.getTeacherEntityList(),null);
            viewBinding.recyclerViewTeacher.setAdapter(classAdapter);
        }
        if (Utils.getString(typeView).equals(TYPE_VIEW_CLASS_STUDYMAP)){
            viewBinding.subtitleCourseDetails.setText(String.format(getString(R.string.place_course_count2),
                    Utils.getIntData(courseDetailsBean.getCourseNum())+""));
            if (!Utils.isEmpty(courseDetailsBean.getIntroduction())){
                StringBuilder sb = new StringBuilder(courseDetailsBean.getIntroduction());
                sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
                viewBinding.webViewCourse.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
            }
        }else if (Utils.getString(typeView).equals(TYPE_VIEW_CLASS)){
            viewBinding.subtitleCourseDetails.setText(String.format(getString(R.string.place_class_count),
                    Utils.getIntData(courseDetailsBean.getCourseNum())+"",
                    Utils.getIntData(courseDetailsBean.getStuNum())+""));
            if (!Utils.isEmpty(courseDetailsBean.getIntroduction())){
                StringBuilder sb = new StringBuilder(courseDetailsBean.getIntroduction());
                sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
                viewBinding.webViewCourse.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
            }
        } else{
            viewBinding.titleCourseDetails.setText(Utils.getString(courseDetailsBean.getName()));
            if (!Utils.isEmpty(courseDetailsBean.getIntroduction())){
                StringBuilder sb = new StringBuilder(courseDetailsBean.getIntroduction());
                sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
                viewBinding.webViewCourse.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
            }
        }
    }

    private String getProcessName() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}

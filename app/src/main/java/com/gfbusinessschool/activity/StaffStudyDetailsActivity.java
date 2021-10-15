package com.gfbusinessschool.activity;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CourseVerticalAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.StudyTimeEntity;
import com.gfbusinessschool.bean.StudyTimeTableEntity;
import com.gfbusinessschool.databinding.ActivityStaffstudydetailsBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店员学习情况
 */
@Route(path = ARouterPath.ACTIVITY_StaffStudyDetails)
public class StaffStudyDetailsActivity extends BaseActivity<ActivityStaffstudydetailsBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private List<CourseBean> mListCourse = new ArrayList<>();
    private CourseVerticalAdapter courseAdapter;
    @Autowired
    String account;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.lineCourseRecommend.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        initParams(viewBinding.lineChart);
        viewBinding.titleBar.setTitle(getString(R.string.title_studydetails));
        viewBinding.swipeCourse.setOnRefreshListener(this);

        LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.studyCourseRv.setLayoutManager(manager);
        courseAdapter =new CourseVerticalAdapter(getApplicationContext(), null);
        courseAdapter.setTypeView(CourseVerticalAdapter.TYPE_VIEW_STUDY_COURSE);
        viewBinding.studyCourseRv.setAdapter(courseAdapter);

        initLineChart(viewBinding.lineChart);
    }

    @Override
    protected void initData() {
        super.initData();
        requestCourseListData();
        getStudyTimeDate();
        get7DayStudyTime();
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeCourse.setRefreshing(true);
        requestCourseListData();
        getStudyTimeDate();
        get7DayStudyTime();
    }

    private void initParams(LineChart lineChart) {
        float width =MyApplication.getInstance().screenWidth-getResources().getDimension(R.dimen.left_app)*2;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) lineChart.getLayoutParams();
        params.height = (int) (width*3.5/7);
        lineChart.setLayoutParams(params);
    }

    private void initLineChart(LineChart lineChart) {
        //无数据时显示
        lineChart.setNoDataText("没有获取到数据哦~");
        lineChart.setNoDataTextColor(Color.parseColor("#00bcef"));
        //取消缩放
        lineChart.setScaleEnabled(false);
        //不显示高亮
        lineChart.setHighlightPerTapEnabled(false);
        //不显示description
        lineChart.getDescription().setEnabled(false);
        //不显示边界
        lineChart.setDrawBorders(false);
        //不显示图例
        lineChart.getLegend().setEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setExtraBottomOffset(10f);
        //获取X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.parseColor("#d1a76f"));
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setEnabled(false);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
    }

    private void requestCourseListData() {
        if (Utils.isEmpty(account)) return;
        mListCourse.clear();
        Map<String,String> map =new HashMap<>();
        map.put("account",account);
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.STUDY_COURSE, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                viewBinding.swipeCourse.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeCourse.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    mListCourse = JSONArray.parseArray(response, CourseBean.class);
                    if (mListCourse==null) mListCourse =new ArrayList<>();
                    courseAdapter.setmList(mListCourse);
                    courseAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 学习时长
     */
    private void getStudyTimeDate() {
        if (Utils.isEmpty(account)) return;
        Map<String,String> map =new HashMap<>();
        map.put("account",account);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.STUDY_TIME, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    StudyTimeEntity studyTimeEntity = JSONObject.parseObject(response,StudyTimeEntity.class);
                    if (studyTimeEntity==null) return;
                    int mintunes =0;
                    if (!Utils.isEmpty(studyTimeEntity.getTodayStudy())){
                        mintunes =Integer.parseInt(studyTimeEntity.getTodayStudy())/60;
                    }
                    String minuteContent =String.format(getString(R.string.place_today_time2),""+mintunes);
                    SpannableStringBuilder builder = new SpannableStringBuilder(minuteContent);
                    builder.setSpan(new AbsoluteSizeSpan(27, true), 4, minuteContent.length()-2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.timeToday.setText(builder);

                    String studyLength ="0";
                    if (!Utils.isEmpty(studyTimeEntity.getStudyLength()))
                        studyLength =studyTimeEntity.getStudyLength();
                    String dayContent =String.format(getString(R.string.place_day),""+studyLength);
                    SpannableStringBuilder builderDay = new SpannableStringBuilder(dayContent);
                    builderDay.setSpan(new AbsoluteSizeSpan(27, true), 4, dayContent.length()-1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.timeContinue.setText(builderDay);

                    int hours =0;
                    double minutesAll =0;
                    if (!Utils.isEmpty(studyTimeEntity.getTotalStudy())){
                        hours =Integer.parseInt(studyTimeEntity.getTotalStudy())/(60*60);
                        minutesAll = Double.parseDouble(studyTimeEntity.getTotalStudy())%(60*60.0)/3600;
                    }
                    minutesAll =new BigDecimal(minutesAll).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (minutesAll>=1) {
                        hours++;
                        minutesAll =0;
                    }
                    String hoursContent =String.format(getString(R.string.place_time_hours),(hours+minutesAll)==0?"0":(hours+minutesAll)+"");
                    SpannableStringBuilder builderAll = new SpannableStringBuilder(hoursContent);
                    builderAll.setSpan(new AbsoluteSizeSpan(27, true), 4, hoursContent.length()-2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.timeAll.setText(builderAll);
                }
            }
        });
    }

    /**
     * 7天学习列表
     */
    private void get7DayStudyTime() {
        if (Utils.isEmpty(account)) return;
        Map<String,String> map =new HashMap<>();
        map.put("account",account);
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.SEVENDAY_STUDY_TIME, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<StudyTimeTableEntity> listStudyTime = JSONObject.parseArray(response,StudyTimeTableEntity.class);
                    if (listStudyTime==null) return;
                    XAxis xAxis = viewBinding.lineChart.getXAxis();
                    String[] dayArr = new String[listStudyTime.size()];
                    for (int i = 0; i < listStudyTime.size(); i++) {
                        StudyTimeTableEntity entity = listStudyTime.get(i);
                        if (entity == null)
                            dayArr[i] = Utils.getString("");
                        else
                            dayArr[i] = Utils.getString(entity.getDateDays());
                    }
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dayArr));
                    //初始化显示数据
                    List<Entry> entries = new ArrayList<>();
                    for (int i = 0; i < listStudyTime.size(); i++) {
                        StudyTimeTableEntity entity = listStudyTime.get(i);
                        if (Utils.isEmpty(entity.getStudyLength()))
                            entries.add(new Entry(i, 0));
                        else
                            entries.add(new Entry(i, Integer.parseInt(entity.getStudyLength())/60));
                    }
                    //将数据赋给数据集,一个数据集表示一条线
                    LineDataSet lineDataSet = new LineDataSet(entries,"");
                    lineDataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return (int)value+"分钟";
                        }
                    });
                    //线颜色
                    lineDataSet.setColor(Color.parseColor("#d1a76f"));
                    //线宽度
                    lineDataSet.setLineWidth(1.0f);
                    //显示圆点
                    lineDataSet.setDrawCircles(true);
                    //设置圆点颜色(外圈)
                    lineDataSet.setCircleColor(Color.parseColor("#d1a76f"));
                    //设置圆点填充颜色
                    lineDataSet.setCircleHoleColor(Color.parseColor("#f4f4f4"));
                    //设置线条为平滑曲线
                    lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                    //设置直线图填充
                    lineDataSet.setDrawFilled(true);
                    //设置填充颜色
                    lineDataSet.setFillColor(Color.parseColor("#FFA2A2"));
                    LineData lineData = new LineData(lineDataSet);
                    //显示曲线点的具体数值
                    lineData.setDrawValues(true);
                    lineData.setValueTextSize(10f);
                    lineData.setValueTextColor(Color.parseColor("#d1a76f"));
                    viewBinding.lineChart.setData(lineData);
                    viewBinding.lineChart.invalidate();
                }
            }
        });
    }


}
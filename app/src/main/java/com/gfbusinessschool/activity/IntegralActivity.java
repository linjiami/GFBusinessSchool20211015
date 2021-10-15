package com.gfbusinessschool.activity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.databinding.ActivityIntegralBinding;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.IntegralAdapter;
import com.gfbusinessschool.bean.IntegralBean;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分
 */
@Route(path = ARouterPath.ACTIVITY_URL_Integral)
public class IntegralActivity extends BaseActivity<ActivityIntegralBinding> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private IntegralAdapter mAdapter;
    private int currentPage;
    private List<IntegralBean> mlist =new ArrayList<>();
    private String dateSelect ="";

    @Override
    protected void initView() {
        viewBinding.titleBar.setTitle(getString(R.string.mypoint));
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.bottom_jiantou,viewBinding.tvTimeSelect,2);
        if (MyApplication.getInstance().getAppUserEntity()!=null) {
            String integralCount =String.format(getString(R.string.integral_count),MyApplication.getInstance().getAppUserEntity().getIntegral());
            if (Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getIntegral()))
                integralCount =String.format(getString(R.string.integral_count),"0");
            SpannableStringBuilder builder = new SpannableStringBuilder(integralCount);
            builder.setSpan(new AbsoluteSizeSpan(35, true), 0, integralCount.length()-2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(24, true), integralCount.length()-2, integralCount.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            viewBinding.countIntegral.setText(builder);
        }
        viewBinding.swipeRefresh.setOnRefreshListener(this);
        viewBinding.tvTimeSelect.setOnClickListener(this);
        viewBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                if (mAdapter!=null)
                    mAdapter.setProgressBarVisiable(true);
                currentPage++;
                getIntegralList(currentPage);
            }
        });
        viewBinding.ruleIntegral.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recyclerView.setLayoutManager(manager);
        mAdapter = new IntegralAdapter(getApplicationContext());
        viewBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ruleIntegral:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_IntegralRule).navigation();
                break;
            case R.id.tvTimeSelect:
                TimePickerView pickerView =new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Calendar calendar =Calendar.getInstance();
                        calendar.setTime(date);
                        int month =calendar.get(Calendar.MONTH)+1;
                        dateSelect =calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                        currentPage=1;
                        getIntegralList(currentPage);
                    }
                }).build();
                pickerView.show();
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        currentPage=1;
        getIntegralList(currentPage);
    }

    @Override
    public void onRefresh() {
        viewBinding.swipeRefresh.setRefreshing(true);
        currentPage =1;
        getIntegralList(currentPage);
    }

    private void getIntegralList(int page){
        if (page==1)mlist.clear();
        Map<String,String> map =new HashMap<>();
        map.put("currPage",page+"");
        map.put("date",dateSelect);
        map.put("limit","15");
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.MY_INTEGRAL, map, new NetWorkCallback() {

            @Override
            public void onRequestError() {
                super.onRequestError();
                viewBinding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(String code, String response) {
                viewBinding.swipeRefresh.setRefreshing(false);
                if (Utils.getString(code).equals("200")){
                    JSONObject object =JSONObject.parseObject(response);
                    List<IntegralBean> list= new ArrayList<>();
                    if (object!=null){
                        JSONArray array = object.getJSONArray("records");
                        if (array!=null) list= array.toJavaList(IntegralBean.class);
                        if (list==null) list= new ArrayList<>();
                        if (list.size() < 15) {
                            mAdapter.setProgressBarVisiable(false);
                        } else {
                            mAdapter.setProgressBarVisiable(true);
                        }
                        if (page==1){
                            mlist =list;
                        }else {
                            if (list.size() == 0) {
                                currentPage--;
                            }else {
                                for (IntegralBean liveBean : list){
                                    mlist.add(liveBean);
                                }
                            }
                        }
                        mAdapter.setmList(mlist);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}

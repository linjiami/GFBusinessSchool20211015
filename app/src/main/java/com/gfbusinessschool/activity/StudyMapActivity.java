package com.gfbusinessschool.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StudyMapEntity;
import com.gfbusinessschool.databinding.ActivityStudymapBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 学习地图
 */
@Route(path = ARouterPath.StudyMapActivity)
public class StudyMapActivity extends BaseActivity<ActivityStudymapBinding> implements View.OnClickListener {

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setTranslucentForImageView(StudyMapActivity.this,0,viewBinding.iconBack);
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.layoutNoData.noDataLayout.setOnClickListener(this);
        viewBinding.iconBack.setOnClickListener(this);
        viewBinding.layoutNoData.tvNodata.setTextColor(getResources().getColor(R.color.white));
        //线路图高度
        double height =MyApplication.getInstance().screenWidth*1981.0/1242;
        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) viewBinding.icon1.getLayoutParams();
        params1.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params1.leftMargin =(int) (MyApplication.getInstance().screenWidth*303.0/750.0);
        params1.topMargin =(int) (height*98/1191);
        viewBinding.icon1.setLayoutParams(params1);

        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) viewBinding.icon2.getLayoutParams();
        params2.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params2.leftMargin =(int) (MyApplication.getInstance().screenWidth*475.0/750.0);
        params2.topMargin =(int) (height*199/1191);
        viewBinding.icon2.setLayoutParams(params2);

        ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) viewBinding.icon3.getLayoutParams();
        params3.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params3.leftMargin =(int) (MyApplication.getInstance().screenWidth*153.0/750.0);
        params3.topMargin =(int) (height*306/1191);
        viewBinding.icon3.setLayoutParams(params3);

        ConstraintLayout.LayoutParams params4 = (ConstraintLayout.LayoutParams) viewBinding.icon4.getLayoutParams();
        params4.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params4.leftMargin =(int) (MyApplication.getInstance().screenWidth*322.0/750.0);
        params4.topMargin =(int) (height*390/1191);
        viewBinding.icon4.setLayoutParams(params4);

        ConstraintLayout.LayoutParams params5 = (ConstraintLayout.LayoutParams) viewBinding.icon5.getLayoutParams();
        params5.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params5.leftMargin =(int) (MyApplication.getInstance().screenWidth*490.0/750.0);
        params5.topMargin =(int) (height*487/1191);
        viewBinding.icon5.setLayoutParams(params5);

        ConstraintLayout.LayoutParams params6 = (ConstraintLayout.LayoutParams) viewBinding.icon6.getLayoutParams();
        params6.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params6.leftMargin =(int) (MyApplication.getInstance().screenWidth*322.0/750.0);
        params6.topMargin =(int) (height*564/1191);
        viewBinding.icon6.setLayoutParams(params6);

        ConstraintLayout.LayoutParams params7 = (ConstraintLayout.LayoutParams) viewBinding.icon7.getLayoutParams();
        params7.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params7.leftMargin =(int) (MyApplication.getInstance().screenWidth*153.0/750.0);
        params7.topMargin =(int) (height*661/1191);
        viewBinding.icon7.setLayoutParams(params7);

        ConstraintLayout.LayoutParams params8 = (ConstraintLayout.LayoutParams) viewBinding.icon8.getLayoutParams();
        params8.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params8.leftMargin =(int) (MyApplication.getInstance().screenWidth*322.0/750.0);
        params8.topMargin =(int) (height*740/1191);
        viewBinding.icon8.setLayoutParams(params8);

        ConstraintLayout.LayoutParams params9 = (ConstraintLayout.LayoutParams) viewBinding.icon9.getLayoutParams();
        params9.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params9.leftMargin =(int) (MyApplication.getInstance().screenWidth*490.0/750.0);
        params9.topMargin =(int) (height*834/1191);
        viewBinding.icon9.setLayoutParams(params9);

        ConstraintLayout.LayoutParams params10 = (ConstraintLayout.LayoutParams) viewBinding.icon10.getLayoutParams();
        params10.width = (int) (MyApplication.getInstance().screenWidth*106.0/750);
        params10.leftMargin =(int) (MyApplication.getInstance().screenWidth*324.0/750.0);
        params10.topMargin =(int) (height*910/1191);
        viewBinding.icon10.setLayoutParams(params10);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iconBack:
                finish();
                break;
            case R.id.noDataLayout:
                initData();
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.STUDYMAP, new HashMap<>(), new NetWorkCallback() {

            @Override
            public void onRequestError() {
                showNullListView(true);
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<StudyMapEntity> list = JSONArray.parseArray(response,StudyMapEntity.class);
                    if (list==null) list =new ArrayList<>();
                    if (list.size()==0){
                        showNullListView(false);
                        return;
                    }
                    viewBinding.layoutStudyMapLine.setVisibility(View.VISIBLE);
                    viewBinding.layoutNoData.noDataLayout.setVisibility(View.GONE);
                    if (list.size()>0 && list.get(0)!=null){
                        StudyMapEntity entity =list.get(0);
                        setLevelState(viewBinding.icon1,entity.getStudyStatus(),viewBinding.text1,entity.getName(),entity.getId());
                    }
                    if (list.size()>1 && list.get(1)!=null){
                        StudyMapEntity entity =list.get(1);
                        setLevelState(viewBinding.icon2,entity.getStudyStatus(),viewBinding.text2,entity.getName(),entity.getId());
                    }
                    if (list.size()>2 && list.get(2)!=null){
                        StudyMapEntity entity =list.get(2);
                        setLevelState(viewBinding.icon3,entity.getStudyStatus(),viewBinding.text3,entity.getName(),entity.getId());
                    }
                    if (list.size()>3 && list.get(3)!=null){
                        StudyMapEntity entity =list.get(3);
                        setLevelState(viewBinding.icon4,entity.getStudyStatus(),viewBinding.text4,entity.getName(),entity.getId());
                    }
                    if (list.size()>4 && list.get(4)!=null){
                        StudyMapEntity entity =list.get(4);
                        setLevelState(viewBinding.icon5,entity.getStudyStatus(),viewBinding.text5,entity.getName(),entity.getId());
                    }
                    if (list.size()>5 && list.get(5)!=null){
                        StudyMapEntity entity =list.get(5);
                        setLevelState(viewBinding.icon6,entity.getStudyStatus(),viewBinding.text6,entity.getName(),entity.getId());
                    }
                    if (list.size()>6 && list.get(6)!=null){
                        StudyMapEntity entity =list.get(6);
                        setLevelState(viewBinding.icon7,entity.getStudyStatus(),viewBinding.text7,entity.getName(),entity.getId());
                    }
                    if (list.size()>7 && list.get(7)!=null){
                        StudyMapEntity entity =list.get(7);
                        setLevelState(viewBinding.icon8,entity.getStudyStatus(),viewBinding.text8,entity.getName(),entity.getId());
                    }
                    if (list.size()>8 && list.get(8)!=null){
                        StudyMapEntity entity =list.get(8);
                        setLevelState(viewBinding.icon9,entity.getStudyStatus(),viewBinding.text9,entity.getName(),entity.getId());
                    }
                    if (list.size()>9 && list.get(9)!=null){
                        StudyMapEntity entity =list.get(9);
                        setLevelState(viewBinding.icon10,entity.getStudyStatus(),viewBinding.text10,entity.getName(),entity.getId());
                    }
                }else {
                    showNullListView(true);
                }
            }
        });
    }

    private void showNullListView(boolean isError){
        viewBinding.layoutStudyMapLine.setVisibility(View.GONE);
        viewBinding.layoutNoData.noDataLayout.setVisibility(View.VISIBLE);
        if (isError){
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.error_loading));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            viewBinding.layoutNoData.tvNodata.setText(getString(R.string.noData));
            viewBinding.layoutNoData.iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }

    private void setLevelState(ImageView icon, String studyStatus,TextView textView,String name,String id){
        // studyStatus 学习进度(0未解锁1进行中2已通关)
        if (Utils.getString(studyStatus).equals("0"))
            icon.setImageResource(R.mipmap.studymap_lock);
        else if (Utils.getString(studyStatus).equals("1"))
            icon.setImageResource(R.mipmap.studymap_ongoing);
        else
            icon.setImageResource(R.mipmap.studymap_pass);
        textView.setText(Utils.getString(name));
        icon.setOnClickListener(v -> {
            if (!Utils.getString(studyStatus).equals("0"))
                ARouter.getInstance().build(ARouterPath.ClassDetailsActivity)
                    .withString("typeView",ClassDetailsActivity.TYPEVIEW_CLASS_STUDYMAP)
                    .withString("classId",id).navigation();
            else
                ToastUtil.show(getApplicationContext(),"当前专题未解锁");
        });
    }
}
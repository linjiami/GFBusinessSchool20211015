package com.gfbusinessschool.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.InterfaceUtils.ResultCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.PositionEntity;
import com.gfbusinessschool.bean.SystemBasicBean;
import com.gfbusinessschool.cityselect.CitySelectActivity;
import com.gfbusinessschool.databinding.ActivityPerfectInfoBinding;
import com.gfbusinessschool.dialog.MyListPopWindow;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Route(path = ARouterPath.PrefectInfoActivity)
public class PrefectInfoActivity extends BaseActivity<ActivityPerfectInfoBinding> implements View.OnClickListener {
    private AppUserEntity appUserEntity;
    @Autowired
    boolean ishowPasswordDialog;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.perfect_info));
        viewBinding.releaseShareBottom.releaseShareBtn.setText(getString(R.string.finish));
        appUserEntity = MyApplication.getInstance().getAppUserEntity();
        viewBinding.releaseShareBottom.releaseShareBtn.setOnClickListener(this);
        viewBinding.sexTV.setOnClickListener(this);
        viewBinding.eduction.setOnClickListener(this);
        viewBinding.positionTv.setOnClickListener(this);
        viewBinding.workExp1.setOnClickListener(this);
        viewBinding.workHours.setOnClickListener(this);
        viewBinding.entryDate.setOnClickListener(this);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.sexTV,2);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.eduction,2);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.positionTv,2);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.entryDate,2);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.workExp1,2);
        Utils.setDrawableTint(getApplicationContext(),R.mipmap.right_jiantou_personel,viewBinding.workHours,2);
        viewBinding.releaseShareBottom.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
    }

    @Override
    protected void initData() {
        super.initData();
        if (appUserEntity==null) return;
        if (!Utils.isEmpty(appUserEntity.getName())){
            viewBinding.nameET.setText(appUserEntity.getName());
        }
        if (!Utils.isEmpty(appUserEntity.getSex())){
            viewBinding.sexTV.setText(appUserEntity.getSex());
        }
        if (!Utils.isEmpty(appUserEntity.getAge())){
            viewBinding.ageEt.setText(appUserEntity.getAge());
        }
        if (!Utils.isEmpty(appUserEntity.getEducation())){
            viewBinding.eduction.setText(appUserEntity.getEducation());
        }
        if (!Utils.isEmpty(appUserEntity.getPhone())){
            viewBinding.phoneEt.setText(appUserEntity.getPhone());
        }
        if (!Utils.isEmpty(appUserEntity.getProvince()) && !Utils.isEmpty(appUserEntity.getCity())){
            viewBinding.cityTv.setText(String.format(getString(R.string.place_hengxian),
                    appUserEntity.getProvince(),appUserEntity.getCity()));
        }
        if (!Utils.isEmpty(appUserEntity.getStoreName())){
            viewBinding.store.setText(appUserEntity.getStoreName());
        }
        if (!Utils.isEmpty(appUserEntity.getPositionName())){
            viewBinding.positionTv.setText(appUserEntity.getPositionName());
        }
        if (!Utils.isEmpty(appUserEntity.getEntryDate())){
            viewBinding.entryDate.setText(appUserEntity.getEntryDate());
        }
        if (!Utils.isEmpty(appUserEntity.getWorkExp())){
            viewBinding.workExp1.setText(appUserEntity.getWorkExp());
        }
        if (!Utils.isEmpty(appUserEntity.getWorkHours())){
            viewBinding.workHours.setText(appUserEntity.getWorkHours());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.releaseShareBtn:
                String name = viewBinding.nameET.getText().toString();
                if (Utils.isEmpty(name)) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_name));
                    return;
                }
                appUserEntity.setName(name);
                if (Utils.isEmpty(appUserEntity.getSex())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_sex));
                    return;
                }
                String age = viewBinding.ageEt.getText().toString();
                if (Utils.isEmpty(age)) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_age));
                    return;
                }
                appUserEntity.setAge(age);
                if (Utils.isEmpty(appUserEntity.getEducation())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_eduction));
                    return;
                }
                String phone = viewBinding.phoneEt.getText().toString();
                if (Utils.isEmpty(phone)) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_phone));
                    return;
                }
                appUserEntity.setPhone(phone);
                if (Utils.isEmpty(appUserEntity.getPositionId())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_position));
                    return;
                }
                //入职时间
                if (Utils.isEmpty(appUserEntity.getEntryDate())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_entrydate));
                    return;
                }
                //DO 工作经历
                if (Utils.isEmpty(appUserEntity.getWorkExp())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_workexp));
                    return;
                }
                //工作经验
                if (Utils.isEmpty(appUserEntity.getWorkHours())) {
                    ToastUtil.show(getApplicationContext(), getString(R.string.alert_login_workhours));
                    return;
                }
                updateUserInfo();
                break;
            case R.id.sexTV:
                String[] sexArr = getResources().getStringArray(R.array.sexUserArray);
                List<String> listSex = new ArrayList<>();
                for (String mySex : sexArr) {
                    listSex.add(mySex);
                }
                MyListPopWindow myListPopWindow = new MyListPopWindow(this, listSex);
                myListPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        appUserEntity.setSex(listSex.get(position));
                        viewBinding.sexTV.setText(listSex.get(position));
                        myListPopWindow.dismiss();
                    }
                });
                myListPopWindow.showPopWindow(viewBinding.sexTV);
                break;
            case R.id.eduction://学历
                MyApplication.getInstance().getSystemBasicBean(new ResultCallBack() {
                    @Override
                    public void onResult(SystemBasicBean bean) {
                        if (bean != null && bean.getEducation() != null
                                && bean.getEducation().size() != 0) {
                            List<String> _list = bean.getEducation();
                            MyListPopWindow companyMembersPopWindow = new MyListPopWindow(PrefectInfoActivity.this, _list);
                            companyMembersPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    appUserEntity.setEducation(_list.get(position));
                                    viewBinding.eduction.setText(_list.get(position));
                                    companyMembersPopWindow.dismiss();
                                }
                            });
                            companyMembersPopWindow.showPopWindow(viewBinding.eduction);
                        }
                    }
                });
                break;
            case R.id.positionTv:
                NetWorkUtils.getPositionList(getApplicationContext(),new HashMap<>(), new ResultCallBack() {
                    @Override
                    public void onResult(List<PositionEntity> list) {
                        if (list != null && list.size() != 0) {
                            List<String> positionList =new ArrayList<>();
                            for (PositionEntity entity : list){
                                positionList.add(entity.getPositionName());
                            }
                            MyListPopWindow companyMembersPopWindow = new MyListPopWindow(PrefectInfoActivity.this, positionList);
                            companyMembersPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    appUserEntity.setPositionId(list.get(position).getId());
                                    viewBinding.positionTv.setText(list.get(position).getPositionName());
                                    companyMembersPopWindow.dismiss();
                                }
                            });
                            companyMembersPopWindow.showPopWindow(viewBinding.positionTv);
                        }
                    }
                });
                break;
            case R.id.workExp1://工作经历(实木套房销售、软体销售)
                MyApplication.getInstance().getSystemBasicBean(new ResultCallBack() {
                    @Override
                    public void onResult(SystemBasicBean bean) {
                        if (bean != null && bean.getWork_exp() != null
                                && bean.getWork_exp().size() != 0) {
                            List<String> _list = bean.getWork_exp();
                            MyListPopWindow companyMembersPopWindow = new MyListPopWindow(PrefectInfoActivity.this, _list);
                            companyMembersPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    appUserEntity.setWorkExp(_list.get(position));
                                    viewBinding.workExp1.setText(_list.get(position));
                                    companyMembersPopWindow.dismiss();
                                }
                            });
                            companyMembersPopWindow.showPopWindow(viewBinding.workHours);
                        }
                    }
                });
                break;
            case R.id.workHours://工作时长
                MyApplication.getInstance().getSystemBasicBean(new ResultCallBack() {
                    @Override
                    public void onResult(SystemBasicBean bean) {
                        if (bean != null && bean.getWork_hours() != null
                                && bean.getWork_hours().size() != 0) {
                            List<String> _list = bean.getWork_hours();
                            MyListPopWindow companyMembersPopWindow = new MyListPopWindow(PrefectInfoActivity.this, _list);
                            companyMembersPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                                @Override
                                public void onClick(int position) {
                                    appUserEntity.setWorkHours(_list.get(position));
                                    viewBinding.workHours.setText(_list.get(position));
                                    companyMembersPopWindow.dismiss();
                                }
                            });
                            companyMembersPopWindow.showPopWindow(viewBinding.workHours);
                        }
                    }
                });
                break;
            case R.id.entryDate:
                TimePickerView pickerView =new TimePickerBuilder(this, (date, v1) -> {
                    Calendar calendar =Calendar.getInstance();
                    calendar.setTime(date);
                    int month =calendar.get(Calendar.MONTH)+1;
                    String time =calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                    viewBinding.entryDate.setText(time);
                    appUserEntity.setEntryDate(time);
                }).build();
                pickerView.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CITY_SELECT:
                if (resultCode == RESULT_OK) {
                    String selectedCity = data.getStringExtra(CitySelectActivity.KEY_CITY_SELECTED);
                    viewBinding.cityTv.setText(selectedCity);
                    appUserEntity.setCity(selectedCity);
                }
                break;
        }
    }

    private void updateUserInfo(){
        AppUserEntity _appUserEntity =appUserEntity;
        _appUserEntity.setProvince(null);
        _appUserEntity.setCity(null);
        _appUserEntity.setCityId(null);
        _appUserEntity.setStoreId(null);
        _appUserEntity.setStoreName(null);
        NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.UPDATE_USERINFO, JSONObject.toJSONString(_appUserEntity), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    appUserEntity.setIsFirst("0");
                    MyApplication.getInstance().saveUserEntity(appUserEntity);
                    ActivityCollector.finishAllActivity();
                    MyApplication.getInstance().setShowingLoginActivity(false);
                    ARouter.getInstance().build(ARouterPath.MainActivity)
                            .withBoolean("ishowPasswordDialog",ishowPasswordDialog)
                            .withString("typeView",appUserEntity.isManagerLogin()?MainActivity.TYPEVIEW_MANAGER:"")
                            .navigation();
                }
            }
        });
    }

}
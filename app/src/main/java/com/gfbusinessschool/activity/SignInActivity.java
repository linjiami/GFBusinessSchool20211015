package com.gfbusinessschool.activity;

import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.CalendarAdapter;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.SiginEntity;
import com.gfbusinessschool.bean.SigninDayEntity;
import com.gfbusinessschool.databinding.ActivityEverysigninBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Route(path = ARouterPath.SignInActivity)
public class SignInActivity extends BaseActivity<ActivityEverysigninBinding>{

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.singin_everyday));
        GridLayoutManager manager =new GridLayoutManager(getApplicationContext(),7);
        viewBinding.dayRv.setLayoutManager(manager);
        viewBinding.dayTV.setTextColor(Utils.getThemeColor(getApplicationContext()));
        Utils.setBackgroundSolid(getApplicationContext(),viewBinding.daySignin);
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        if (appUserEntity!=null){
            GlideLoadUtils.load(getApplicationContext(),appUserEntity.getHeadImgUrl(),viewBinding.headIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            viewBinding.myName.setText(Utils.getString(appUserEntity.getName()));
            viewBinding.integralCount.setText(String.format(getString(R.string.place_integral_point),appUserEntity.getIntegral()));
            if (appUserEntity.getPassRate()==1)
                viewBinding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
            else
                viewBinding.biyexuanzhangIcon.setVisibility(View.GONE);
        }
        Utils.setImageViewTint(getApplicationContext(),viewBinding.signnin1);
        viewBinding.shopMallIcon.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.ShoppingMallActivity).navigation();
        });
        GlideLoadUtils.load(getApplicationContext(),InterfaceClass.SHOPMALL_ICON,viewBinding.shopMallIcon,R.mipmap.shangcheng);
    }

    @Override
    protected void initData() {
        super.initData();
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.SIGIN, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    SiginEntity siginEntity = JSONObject.parseObject(response,SiginEntity.class);
                    if (siginEntity==null) return;
                    //isFirstSign 签到接口访问状态(1今日首次签到2今日非首次签到)
                    if (Utils.getString(siginEntity.getIsFirstSign()).equals("1")){
                        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
                        MyAlertDialog myAlertDialog = new MyAlertDialog(SignInActivity.this, new MyDialogCallback() {
                            @Override
                            public void onPositiveClick(MyAlertDialog dialog) {
                                super.onPositiveClick(dialog);
                            }
                        });
                        if (Utils.getString(siginEntity.getContinueSignTimes()).equals("7")) {
                            int continueSignIntegral =0;
                            if (!Utils.isEmpty(siginEntity.getContinueSignIntegral()))
                                continueSignIntegral =Integer.parseInt(siginEntity.getContinueSignIntegral());
                            int daySignIntegral =0;
                            if (!Utils.isEmpty(siginEntity.getDaySignIntegral()))
                                daySignIntegral =Integer.parseInt(siginEntity.getDaySignIntegral());
                            myAlertDialog.setMessage(String.format(getString(R.string.alert_sigin_success), (continueSignIntegral+daySignIntegral)+""));
                            if (appUserEntity!=null){
                                GlideLoadUtils.load(getApplicationContext(),appUserEntity.getHeadImgUrl(),viewBinding.headIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                                viewBinding.myName.setText(Utils.getString(appUserEntity.getName()));
                                int myIntergral =0;
                                if (!Utils.isEmpty(appUserEntity.getIntegral()))
                                    myIntergral =Integer.parseInt(appUserEntity.getIntegral());
                                viewBinding.integralCount.setText(String.format(getString(R.string.place_integral_point),(continueSignIntegral+daySignIntegral+myIntergral)+""));
                            }
                        }else {
                            myAlertDialog.setMessage(String.format(getString(R.string.alert_sigin_success2), siginEntity.getDaySignIntegral()));
                            if (appUserEntity!=null){
                                int myIntergral =0;
                                if (!Utils.isEmpty(appUserEntity.getIntegral()))
                                    myIntergral =Integer.parseInt(appUserEntity.getIntegral());
                                int daySignIntegral =0;
                                if (!Utils.isEmpty(siginEntity.getDaySignIntegral()))
                                    daySignIntegral =Integer.parseInt(siginEntity.getDaySignIntegral());
                                viewBinding.integralCount.setText(String.format(getString(R.string.place_integral_point),(daySignIntegral+myIntergral)+""));
                            }
                        }
                        myAlertDialog.setNegativeButtonGone();
                        myAlertDialog.setCanceledOnTouchOutside(false);
                        myAlertDialog.show();
                    }
                    viewBinding.contentSignin.setText(String.format(getString(R.string.place_sigin_7),siginEntity.getContinueSignIntegral()));
                    if (Utils.isEmpty(siginEntity.getContinueSignTimes())) {
                        viewBinding.daySignin.setText(String.format(getString(R.string.place_sigin), "1"));
                        setContinueSiginState(1);
                    } else {
                        viewBinding.daySignin.setText(String.format(getString(R.string.place_sigin), siginEntity.getContinueSignTimes()));
                        setContinueSiginState(Integer.parseInt(siginEntity.getContinueSignTimes()));
                    }
                    CalendarAdapter calendarAdapter =new CalendarAdapter(getApplicationContext());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    int month = calendar.get(Calendar.MONTH)+1;
                    int year = calendar.get(Calendar.YEAR);
                    viewBinding.dayTV.setText(String.format(getString(R.string.place_year_month),""+year,""+(month>9?month:"0"+month)));
                    String firstDay =year+"-"+month+"-"+"01";
                    try {
                        calendar.setTime(format.parse(firstDay));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int week=calendar.get(Calendar.DAY_OF_WEEK);
                    List<SigninDayEntity> list =new ArrayList<>();
                    for (int i=0;i<week-1;i++){
                        list.add(new SigninDayEntity(""));
                    }

                    for (int i=1;i<getMonthLastDay(year,month)+1;i++){
                        SigninDayEntity entity =new SigninDayEntity(""+i);
                        List<SiginEntity.SignListBean> siginList =siginEntity.getSignList();
                        if (siginList==null) siginList =new ArrayList<>();
                        for (SiginEntity.SignListBean bean : siginList){
                            if (!Utils.isEmpty(bean.getSignDate())){
                                try {
                                    SimpleDateFormat format1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Calendar calendar1=Calendar.getInstance();
                                    calendar1.setTime(format1.parse(bean.getSignDate()));
                                    int _day = calendar1.get(Calendar.DATE);
                                    if (i==_day) entity.setSigin(true);
                                } catch (Exception e) {
                                    Calendar calendar1=Calendar.getInstance();
                                    if (i==calendar1.get(Calendar.DATE)) entity.setSigin(true);
                                }
                            }
                        }
                        list.add(entity);
                    }
                    calendarAdapter.setList(list);
                    viewBinding.dayRv.setAdapter(calendarAdapter);
                }
            }
        });
    }


    /**
     * 得到指定月的天数
     */
    private static int getMonthLastDay(int year, int month)
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    private void setContinueSiginState(int continueSiginTimes){
        viewBinding.signnin2.setImageResource(R.mipmap.sigin_no);
        viewBinding.signnin3.setImageResource(R.mipmap.sigin_no);
        viewBinding.signnin4.setImageResource(R.mipmap.sigin_no);
        viewBinding.signnin5.setImageResource(R.mipmap.sigin_no);
        viewBinding.signnin6.setImageResource(R.mipmap.sigin_no);
        viewBinding.signnin7.setImageResource(R.mipmap.sigin_no);
        if (continueSiginTimes==2){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
        }else if (continueSiginTimes==3){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
            viewBinding.signnin3.setImageResource(R.mipmap.sigined);
        }else if (continueSiginTimes==4){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
            viewBinding.signnin3.setImageResource(R.mipmap.sigined);
            viewBinding.signnin4.setImageResource(R.mipmap.sigined);
        }else if (continueSiginTimes==5){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
            viewBinding.signnin3.setImageResource(R.mipmap.sigined);
            viewBinding.signnin4.setImageResource(R.mipmap.sigined);
            viewBinding.signnin5.setImageResource(R.mipmap.sigined);
        } else if (continueSiginTimes==6){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
            viewBinding.signnin3.setImageResource(R.mipmap.sigined);
            viewBinding.signnin4.setImageResource(R.mipmap.sigined);
            viewBinding.signnin5.setImageResource(R.mipmap.sigined);
            viewBinding.signnin6.setImageResource(R.mipmap.sigined);
        }else if (continueSiginTimes==7){
            viewBinding.signnin2.setImageResource(R.mipmap.sigined);
            viewBinding.signnin3.setImageResource(R.mipmap.sigined);
            viewBinding.signnin4.setImageResource(R.mipmap.sigined);
            viewBinding.signnin5.setImageResource(R.mipmap.sigined);
            viewBinding.signnin6.setImageResource(R.mipmap.sigined);
            viewBinding.signnin7.setImageResource(R.mipmap.sigined);
        }
    }

}
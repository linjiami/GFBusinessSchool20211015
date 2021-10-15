package com.gfbusinessschool.fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.CourseListActivity;
import com.gfbusinessschool.activity.MainActivity;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.StudyTimeEntity;
import com.gfbusinessschool.databinding.FragmentPersonalBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.MyCache;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends BaseFragment<FragmentPersonalBinding> implements View.OnClickListener {

    @Override
    protected void initView() {
        Utils.setBackgroundSolid(getContext(),viewBinding.integralShoppingMall);
        viewBinding.personalBg.setBackgroundColor(Utils.getThemeColor(getContext()));
        Utils.setDrawableTint2(getContext(),R.mipmap.upload_share_personel,R.mipmap.right_jiantou_personel, viewBinding.uploadShare,0,2);
        Utils.setDrawableTint(getContext(),R.mipmap.clearcatch_personel,viewBinding.titleClearCatch,0);
        Utils.setDrawableTint(getContext(),R.mipmap.right_jiantou_personel,viewBinding.countCatch,2);
        Utils.setDrawableTint2(getContext(),R.mipmap.privacyagreement_personel,R.mipmap.right_jiantou_personel, viewBinding.privacyAgreement,0,2);
        Utils.setDrawableTint2(getContext(),R.mipmap.privacypolicy_personel, R.mipmap.right_jiantou_personel,viewBinding.privacyPolicy,0,2);
        Utils.setDrawableTint2(getContext(),R.mipmap.dushuhui_personel, R.mipmap.right_jiantou_personel,viewBinding.readCollection,0,2);
        Utils.setDrawableTint2(getContext(),R.mipmap.zhengshu_personel, R.mipmap.right_jiantou_personel,viewBinding.myCertificate,0,2);
        Utils.setDrawableTint2(getContext(),R.mipmap.diaoyan_personel, R.mipmap.right_jiantou_personel,viewBinding.joinResearch,0,2);
        viewBinding.leftLayoutLine.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.rigtjLayoutLine.setBackgroundColor(Utils.getThemeColor(getContext()));
        viewBinding.logoutTv.setTextColor(Utils.getThemeColor(getContext()));
        viewBinding.tvCollected.setOnClickListener(this);
        viewBinding.tvHistory.setOnClickListener(this);
        viewBinding.uploadShare.setOnClickListener(this);
        viewBinding.tvIntegral.setOnClickListener(this);
        viewBinding.layoutUser.setOnClickListener(this);
        viewBinding.changeUser.setOnClickListener(this);
        viewBinding.tvSigin.setOnClickListener(this);
        viewBinding.logout.setOnClickListener(this);
        viewBinding.joinResearch.setOnClickListener(this);
        viewBinding.integralShoppingMall.setOnClickListener(this);
        viewBinding.privacyAgreement.setOnClickListener(this);
        viewBinding.privacyPolicy.setOnClickListener(this);
        viewBinding.layoutClearCatch.setOnClickListener(this);
        viewBinding.myCertificate.setOnClickListener(this);
        viewBinding.readCollection.setOnClickListener(this);
        viewBinding.changePassword.setOnClickListener(this);
        try {
            viewBinding.countCatch.setText(MyCache.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            if (Utils.isTest) e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        isFirstLoad =true;
        super.onResume();
    }

    @Override
    protected void initData() {
        getUserInfo();
        getStudyTimeDate();
        if (!MyApplication.getInstance().isShowReadCollectionShare()){
            viewBinding.readCollection.setVisibility(View.GONE);
            viewBinding.linereadCollection.setVisibility(View.GONE);
        }else {
            viewBinding.readCollection.setVisibility(View.VISIBLE);
            viewBinding.linereadCollection.setVisibility(View.VISIBLE);
        }

        if (!MyApplication.getInstance().isShowChamPionShare()){
            viewBinding.uploadShare.setVisibility(View.GONE);
            viewBinding.lineuploadShare.setVisibility(View.GONE);
        }else {
            viewBinding.uploadShare.setVisibility(View.VISIBLE);
            viewBinding.lineuploadShare.setVisibility(View.VISIBLE);
        }
        if (!MyApplication.getInstance().isShowResearch()){
            viewBinding.joinResearch.setVisibility(View.GONE);
            viewBinding.linejoinResearch.setVisibility(View.GONE);
        }else {
            viewBinding.joinResearch.setVisibility(View.VISIBLE);
            viewBinding.linejoinResearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readCollection://读书汇分享
                ARouter.getInstance().build(ARouterPath.MyReadCollectionActivity).navigation();
                break;
            case R.id.myCertificate://我的证书
                ARouter.getInstance().build(ARouterPath.MyCertificateActivity).navigation();
                break;
            case R.id.joinResearch://问卷调研
                ARouter.getInstance().build(ARouterPath.ResearchActivity).withBoolean("isJoinResearch",true).navigation();
                break;
            case R.id.tvSigin://签到
                ARouter.getInstance().build(ARouterPath.SignInActivity).navigation();
                break;
            case R.id.integralShoppingMall://积分商城
                ARouter.getInstance().build(ARouterPath.ShoppingMallActivity).navigation();
                break;
            case R.id.changeUser:
                EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_FINISH));
                ActivityCollector.finishAllActivity();
                AppUserEntity appUserEntity = MyApplication.getInstance().getAppUserEntity();
                if (appUserEntity==null) return;
                appUserEntity.setManagerLogin(true);
                MyApplication.getInstance().saveUserEntity(appUserEntity);
                ARouter.getInstance().build(ARouterPath.MainActivity)
                        .withString("typeView", MainActivity.TYPEVIEW_MANAGER).navigation();
                break;
            case R.id.tvCollected:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_MyCollected).navigation();
                break;
            case R.id.tvHistory:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseList)
                        .withString("typeView", CourseListActivity.TYPEVIEW_COURSE_HISTORY).navigation();
                break;
            case R.id.uploadShare:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_CourseList)
                        .withString("typeView", CourseListActivity.TYPEVIEW_COURSE_SHARE).navigation();
                break;
            case R.id.tvIntegral:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_Integral).navigation();
                break;
            case R.id.layoutUser:
                //进入个人主页
                ARouter.getInstance().build(ARouterPath.ACTIVITY_Personalinfo).navigation();
                break;
            case R.id.logout:
                MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
                myAlertDialog.setMessage(getString(R.string.alert_logout));
                myAlertDialog.setMyDialogCallback(new MyDialogCallback() {
                    @Override
                    public void onPositiveClick(MyAlertDialog dialog) {
                        super.onPositiveClick(dialog);
                        EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_FINISH));
                        ActivityCollector.finishAllActivity();
                        MyApplication.getInstance().saveUserEntity(null);
                        ARouter.getInstance().build(ARouterPath.LoginActivity).navigation();
                    }
                });
                myAlertDialog.show();
                break;
            case R.id.layoutClearCatch:
                MyAlertDialog dialogbuilderCatch = new MyAlertDialog(getActivity());
                dialogbuilderCatch.setMessage(getString(R.string.alert_clearcatch));
                dialogbuilderCatch.setMyDialogCallback(new MyDialogCallback() {
                    @Override
                    public void onPositiveClick(MyAlertDialog dialog) {
                        super.onPositiveClick(dialog);
                        clearAppCache();
                        viewBinding.countCatch.setText("0MB");
                    }
                });
                dialogbuilderCatch.show();
                break;
            case R.id.privacyAgreement:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_WebView)
                        .withString("title",getString(R.string.pricacy_agreement))
                        .withString("url", InterfaceClass.PRIVACY_AGREEMENT_URL).navigation();
                break;
            case R.id.privacyPolicy:
                ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_WebView)
                        .withString("title",getString(R.string.pricacy_policy))
                        .withString("url", InterfaceClass.PRIVACY_POLICY_URL).navigation();
                break;
            case R.id.changePassword:
                ARouter.getInstance().build(ARouterPath.ResetPasswordActivity).navigation();
                break;
        }
    }

    private void getUserInfo(){
        NetWorkUtils.getRequest(getContext(), InterfaceClass.GET_USERINFO, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    AppUserEntity appUserEntity = JSONObject.parseObject(response,AppUserEntity.class);
                    if (appUserEntity==null) return;
                    appUserEntity.setToken(MyApplication.getInstance().getAppUserEntity().getToken());
                    appUserEntity.setManagerLogin(MyApplication.getInstance().getAppUserEntity().isManagerLogin());
                    MyApplication.getInstance().saveUserEntity(appUserEntity);
                    GlideLoadUtils.load(getContext(),appUserEntity.getCompanyLogo(),
                            viewBinding.compayLogo,GlideLoadUtils.TYPE_PLACE_HOLDER_COMPANY_LOGO);
                    viewBinding.companyName.setText(Utils.getString(appUserEntity.getCompanyName()));
                    GlideLoadUtils.load(getContext(),appUserEntity.getHeadImgUrl(),
                            viewBinding.userIcon,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                    viewBinding.nameUser.setText(Utils.getString(appUserEntity.getName()));
                    viewBinding.countPointUser.setText(String.format(getString(R.string.place_integral_count),appUserEntity.getIntegral()));
                    viewBinding.addressUser.setText(Utils.getString(appUserEntity.getProvince())+"-"+
                            Utils.getString(appUserEntity.getCity())+"     "+Utils.getString(appUserEntity.getStoreName()));
                    if (Utils.getString(appUserEntity.getUserType()).equals("2")){//管理员身份
                        viewBinding.changeUser.setVisibility(View.VISIBLE);
                    }else {
                        viewBinding.changeUser.setVisibility(View.GONE);
                    }
                    if (appUserEntity.getPassRate()==1)
                        viewBinding.biyexuanzhangIcon.setVisibility(View.VISIBLE);
                    else
                        viewBinding.biyexuanzhangIcon.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 清除app缓存
     *
     * @param
     */
    public void clearAppCache() {
      new Thread(() -> {
          //清理缓存
          MyCache.clearAllCache(getContext());
      }).start();
    }

    /**
     * 学习时长
     */
    private void getStudyTimeDate() {
        if (MyApplication.getInstance().getAppUserEntity()==null ||
                Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getAccount())) return;
        Map<String,String> map =new HashMap<>();
        map.put("account",MyApplication.getInstance().getAppUserEntity().getAccount());
        NetWorkUtils.getRequest(getContext(), InterfaceClass.STUDY_TIME, map, new NetWorkCallback() {
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
                    viewBinding.tvStudyMinute.setText(builder);

                    String studyLength ="0";
                    if (!Utils.isEmpty(studyTimeEntity.getStudyLength()))
                        studyLength =studyTimeEntity.getStudyLength();
                    String dayContent =String.format(getString(R.string.place_day),""+studyLength);
                    SpannableStringBuilder builderDay = new SpannableStringBuilder(dayContent);
                    builderDay.setSpan(new AbsoluteSizeSpan(27, true), 4, dayContent.length()-1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewBinding.tvStudyDay.setText(builderDay);

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
                    viewBinding.tvStudyHour.setText(builderAll);
                }
            }
        });
    }
}
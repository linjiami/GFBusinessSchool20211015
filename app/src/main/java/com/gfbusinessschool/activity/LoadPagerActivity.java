package com.gfbusinessschool.activity;


import android.Manifest;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.RequestPermissionCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.BannerBean;
import com.gfbusinessschool.databinding.ActivityLoadpagerBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadPagerActivity extends BaseActivity<ActivityLoadpagerBinding> {
    private CountDownTimer countDownTimer;

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setTransparentForImageView(LoadPagerActivity.this,viewBinding.btnSkip);
    }

    @Override
    protected void initView() {
        MyApplication.getInstance().setShowingLoginActivity(false);
        MyApplication.getInstance().screenWidth = Utils.getScreenWidth(LoadPagerActivity.this);
        MyApplication.getInstance().screenHeight =Utils.getScreenHeight(LoadPagerActivity.this);

        if (MyApplication.getInstance().isFirstStart()){
            MyAlertDialog myAlertDialog =new MyAlertDialog(LoadPagerActivity.this, new MyDialogCallback() {
                @Override
                public void onPositiveClick(MyAlertDialog dialog) {
                    super.onPositiveClick(dialog);
                    MyApplication.getInstance().saveFirstLoginState();
                    getPermissionState();
                }

                @Override
                public void onNegativeClick(MyAlertDialog dialog) {
                    super.onPositiveClick(dialog);
                    finish();
                    ActivityCollector.finishAllActivity();
                }
            });
            String str = "感谢您对企业云教育的支持!我们非常重视您的个人信息和隐私保护。为了更好地保障您的个人权益，在您使用我们的产品前，请务必审慎阅读《隐私政策》和《隐私协议》内的所有条款，" +
                    "您点击“同意”的行为即表示您已阅读完毕并同意以上协议的全部内容。";
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            //设置文字
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_WebView)
                            .withString("title",getString(R.string.pricacy_policy))
                            .withString("url", InterfaceClass.PRIVACY_POLICY_URL).navigation();
                }
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            }, 63, 69, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    ARouter.getInstance().build(ARouterPath.ACTIVITY_URL_WebView)
                            .withString("title",getString(R.string.pricacy_agreement))
                            .withString("url", InterfaceClass.PRIVACY_AGREEMENT_URL).navigation();
                }
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            }, 70, 76, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置部分文字颜色
            ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.red));
            style.setSpan(foregroundColorSpan1, 63,69, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.red));
            style.setSpan(foregroundColorSpan2, 70,76, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            myAlertDialog.setMessage(style);
            myAlertDialog.setPositiveText(getString(R.string.privacy_agree));
            myAlertDialog.setNevativigateText(getString(R.string.privacy_agree_no));
            myAlertDialog.setCanceledOnTouchOutside(false);
            myAlertDialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            });
            myAlertDialog.show();
        }else {
            getPermissionState();
        }
        viewBinding.btnSkip.setOnClickListener(v -> {
            if (countDownTimer!=null) countDownTimer.cancel();
            jumpToActivity();
        });
    }

    @Override
    protected void initData() {
        requestBannerData();
    }

    private void getPermissionState(){
        String[] arrPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        List<String> permissionsNotAllow= getNotAllowPersimission(arrPermission);
        if (permissionsNotAllow.size()==0){
            countDown();
        }else {
            String[] arr =new String[permissionsNotAllow.size()];
            for (int i=0;i<permissionsNotAllow.size();i++){
                arr[i] =permissionsNotAllow.get(i);
            }
            requestPermissions(arr, new RequestPermissionCallBack() {
                @Override
                public void granted() {
                    countDown();
                }

                @Override
                public void refused() {
                    countDown();
                }

                @Override
                public void denied() {
                    countDown();
                }
            });
        }
    }

    private void countDown() {
        if (countDownTimer == null)
            countDownTimer = new CountDownTimer(3 * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    viewBinding.btnSkip.setText(String.format(getString(R.string.place_countdown_time), "" + millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    jumpToActivity();
                }
            };
        countDownTimer.start();
    }

    private void jumpToActivity() {
        if (MyApplication.getInstance().getAppUserEntity()==null ||
                Utils.getString(MyApplication.getInstance().getAppUserEntity().getIsFirst()).equals("1"))//第一次登录
            ARouter.getInstance().build(ARouterPath.LoginActivity).navigation();
        else {
            if (!MyApplication.getInstance().getAppUserEntity().isManagerLogin()){
                ARouter.getInstance().build(ARouterPath.MainActivity).navigation();
            }else{
                ARouter.getInstance().build(ARouterPath.MainActivity)
                        .withString("typeView", MainActivity.TYPEVIEW_MANAGER).navigation();
            }
        }
        finish();
    }

    /**
     * 获取banner
     */
    private void requestBannerData(){
        if (MyApplication.getInstance().getAppUserEntity()==null ||
                Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getToken()))
            return;
        Map<String, String> map = new HashMap<>();
        map.put("showposition", "1");//1开屏展示 2首页顶部
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.FIRSTPAGE_BANNER, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.isEmpty(response)) return;
                List<BannerBean> recruitBeanList = JSONArray.parseArray(response, BannerBean.class);
                if (recruitBeanList!=null && recruitBeanList.size()!=0 && recruitBeanList.get(0)!=null){
                    GlideLoadUtils.load(getApplicationContext(),recruitBeanList.get(0).getImgUrl(),viewBinding.loadpageIcon,
                            R.mipmap.loadpage);
                }
            }
        });
    }
}

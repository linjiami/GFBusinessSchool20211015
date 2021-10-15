package com.gfbusinessschool.activity;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.databinding.ActivityLoginBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.HashMap;
import java.util.Map;

@Route(path = ARouterPath.LoginActivity)
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {
    private boolean isManagerLogin;
    private boolean isChecked ;

    @Override
    protected void initView() {
        super.initView();
        MyApplication.getInstance().setShowingLoginActivity(true);
        if (!MyApplication.getInstance().isFirstAgreePermission()){
            isChecked =true;
            viewBinding.checkPrivate.setImageResource(R.mipmap.checked_login);
        }
        viewBinding.staffLoginBtn.setOnClickListener(this);
        viewBinding.checkPrivate.setOnClickListener(this);
        viewBinding.loginAgreement.setOnClickListener(this);
        viewBinding.managerLoginBtn.setOnClickListener(this);
        viewBinding.loginBtn.setOnClickListener(this);
        String commentCount =getString(R.string.login_agreeement);
        SpannableStringBuilder builder = new SpannableStringBuilder(commentCount);
        //设置文字
        builder.setSpan(new ClickableSpan() {
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
        }, commentCount.length()-9, commentCount.length() - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
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
        }, commentCount.length() - 4, commentCount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.theme_text_color));
        ForegroundColorSpan span2 = new ForegroundColorSpan(getResources().getColor(R.color.theme_text_color));
        builder.setSpan(span, commentCount.length()-9, commentCount.length() - 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(span2, commentCount.length() - 4, commentCount.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        viewBinding.loginAgreement.setText(builder);
        viewBinding.loginAgreement.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginAgreement:
            case R.id.checkPrivate:
                if (isChecked){
                    isChecked=false;
                    viewBinding.checkPrivate.setImageResource(R.mipmap.check_login);
                }else {
                    isChecked=true;
                    viewBinding.checkPrivate.setImageResource(R.mipmap.checked_login);
                }
                break;
            case R.id.staffLoginBtn:
                if (isManagerLogin){
                    isManagerLogin =false;
                    viewBinding.staffLoginBtn.setTextColor(getResources().getColor(R.color.black));
                    viewBinding.managerLoginBtn.setTextColor(getResources().getColor(R.color.color_66));
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewBinding.lineLogin.getLayoutParams();
                    params.leftToLeft =R.id.staffLoginBtn;
                    params.rightToRight =R.id.staffLoginBtn;
                    viewBinding.lineLogin.setLayoutParams(params);
                }
                break;
            case R.id.managerLoginBtn:
                if (!isManagerLogin){
                    isManagerLogin =true;
                    viewBinding.staffLoginBtn.setTextColor(getResources().getColor(R.color.color_66));
                    viewBinding.managerLoginBtn.setTextColor(getResources().getColor(R.color.black));
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewBinding.lineLogin.getLayoutParams();
                    params.leftToLeft =R.id.managerLoginBtn;
                    params.rightToRight =R.id.managerLoginBtn;
                    viewBinding.lineLogin.setLayoutParams(params);
                }
                break;
            case R.id.loginBtn:
                if (!isChecked){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_login_private));
                    return;
                }
                String account =viewBinding.accountET.getText().toString();
                account =account.replaceAll(" ","");
                if (Utils.isEmpty(account)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_login_account));
                    return;
                }
                String password =viewBinding.passwordET.getText().toString();
                password =password.replaceAll(" ","");
                if (Utils.isEmpty(password)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_login_password));
                    return;
                }
                Map<String,String> map =new HashMap<>();
                map.put("account",account);
                map.put("password",password);
                if (!isManagerLogin)
                    map.put("userType","1");
                else
                    map.put("userType","2");
                viewBinding.loginBtn.setClickable(false);
                String finalPassword = password;
                NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.LOGIN, JSONObject.toJSON(map).toString(), new NetWorkCallback() {

                    @Override
                    public void onRequestError() {
                        viewBinding.loginBtn.setClickable(true);
                    }

                    @Override
                    public void onResponse(String code, String response) {
                        if (Utils.getString(code).equals("200")){
                            JSONObject object = JSONObject.parseObject(response);
                            String token =object.getString("token");
                            AppUserEntity appUserEntity = object.getObject("userInfo",AppUserEntity.class);
                            if (appUserEntity==null) return;
                            appUserEntity.setToken(token);
                            appUserEntity.setManagerLogin(isManagerLogin);
                            MyApplication.getInstance().saveUserEntity(appUserEntity);
                            MyApplication.getInstance().saveFirstAgreePermissionState();
                            //密码如果为6个0，需要弹出安全密码提示窗
                            boolean ishowPasswordDialog =false;
                            if (Utils.getString(finalPassword).equals("000000"))
                                ishowPasswordDialog =true;
                            if (Utils.getString(appUserEntity.getIsFirst()).equals("1")){
                                ARouter.getInstance().build(ARouterPath.PrefectInfoActivity)
                                        .withBoolean("ishowPasswordDialog",ishowPasswordDialog).navigation();
                            }else {
                                if (!isManagerLogin){
                                    ARouter.getInstance().build(ARouterPath.MainActivity)
                                            .withBoolean("ishowPasswordDialog",ishowPasswordDialog).navigation();
                                }else{
                                    ARouter.getInstance().build(ARouterPath.MainActivity)
                                            .withString("typeView", MainActivity.TYPEVIEW_MANAGER)
                                            .withBoolean("ishowPasswordDialog",ishowPasswordDialog).navigation();
                                }
                                MyApplication.getInstance().setShowingLoginActivity(false);
                                finish();
                            }
                            viewBinding.loginBtn.setClickable(true);
                        }else {
                            viewBinding.loginBtn.setClickable(true);
                        }
                    }
                });

                break;
        }
    }
}
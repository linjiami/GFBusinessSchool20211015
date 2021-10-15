package com.gfbusinessschool.activity;

import android.view.View;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityResetPasswordBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码/修改密码（重置密码）
 */
@Route(path = ARouterPath.ResetPasswordActivity)
public class ResetPasswordActivity extends BaseActivity<ActivityResetPasswordBinding> implements View.OnClickListener {

    @Override
    protected void initView() {
        viewBinding.titleBar.setTitle(getString(R.string.title_setting_password));
        viewBinding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String password = viewBinding.etNewPassword.getText().toString().trim();
                password =password.replaceAll(" ","");
                String confirmPassword = viewBinding.etConfirmPassword.getText().toString().trim();
                confirmPassword =confirmPassword.replaceAll(" ","");
                if (Utils.isEmpty(password)) {
                    ToastUtil.show(getApplicationContext(), "密码不能为空，请重新输入");
                    return;
                }
                if (!Utils.getString(password).equals(Utils.getString(confirmPassword))) {
                    ToastUtil.show(getApplicationContext(), "两次输入的密码不一致，请重新输入");
                    return;
                }
                if (password.length() < 6 || password.length()>18) {
                    ToastUtil.show(getApplicationContext(), "密码格式为6-18位，请重新输入");
                    return;
                }
                postlogin(password);
                break;
        }
    }

    private void postlogin(String password) {
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        NetWorkUtils.getResultString(getApplicationContext(), InterfaceClass.LOGIN_CHANGE_PASSWORD, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")) {
                    ToastUtil.show(getApplicationContext(),"密码修改成功");
                    MyApplication.getInstance().saveUserEntity(null);
                    ActivityCollector.finishAllActivity();
                    ARouter.getInstance().build(ARouterPath.LoginActivity).navigation();
                    finish();
                }
            }
        });
    }
}

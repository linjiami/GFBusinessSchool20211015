package com.gfbusinessschool.activity;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.BuildConfig;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.AppVersionEntity;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.fragment.ManagerFirstpageFragment;
import com.gfbusinessschool.fragment.RanklistFragment;
import com.gfbusinessschool.fragment.FirstPageFragment;
import com.gfbusinessschool.fragment.PersonalFragment;
import com.gfbusinessschool.fragment.RanklistStoreFragment;
import com.gfbusinessschool.fragment.StoreParentFragment;
import com.gfbusinessschool.databinding.ActivityMainBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 员工/管理员
 */
@Route(path = ARouterPath.MainActivity)
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String apkDownloadPath ;
    public static final String TYPEVIEW_MANAGER ="管理员";
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentview =new ArrayList<>();//FrameLayout集合
    private int positionSelected ;
    @Autowired
    String typeView;
    @Autowired
    boolean ishowPasswordDialog;
    private String nameApk;
    private MyAlertDialog myAlertDialog;
    private boolean isErrorInstall;

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setColor(this,Utils.getThemeColor(getApplicationContext()),0);
    }

    @Override
    protected void initView() {
        setRadioButtonState(0);
        if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER)){//管理员
            fragmentview.add(new ManagerFirstpageFragment());
            RanklistStoreFragment storeFragment =new RanklistStoreFragment();
            storeFragment.setTypeView(RanklistStoreFragment.TYPEVIEW_MANAGER_RANKLIST_PERSONEL);
            fragmentview.add(storeFragment);
            fragmentview.add(new StoreParentFragment());//员工首页加载的是店面列表
            viewBinding.classifyRB.setText(getString(R.string.store_ranklist));
            viewBinding.personalRB.setText(getString(R.string.staff));
        }else {//员工
            fragmentview.add(new FirstPageFragment());
            fragmentview.add(new RanklistFragment());
            fragmentview.add(new PersonalFragment());
        }
        fragmentManager = getSupportFragmentManager();
        showFragment(0);
        viewBinding.mainBottomRG.setOnCheckedChangeListener((radioGroup, i) ->{
            switch (i) {
                case R.id.firstPageRB:
                    MyStatusBarUtils.setColor(this,Utils.getThemeColor(getApplicationContext()),false);
                    showFragment(0);
                    setRadioButtonState(0);
                    break;
                case R.id.classifyRB:
                    MyStatusBarUtils.setColor(this,getResources().getColor(R.color.white),true);
                    showFragment(1);
                    setRadioButtonState(1);
                    break;
                case R.id.personalRB:
                    if (Utils.getString(typeView).equals(TYPEVIEW_MANAGER))
                        MyStatusBarUtils.setColor(this,getResources().getColor(R.color.white),true);
                    else
                        MyStatusBarUtils.setColor(this,Utils.getThemeColor(getApplicationContext()),false);
                    showFragment(2);
                    setRadioButtonState(2);
                    break;
            }
        });
        if (ishowPasswordDialog){
            MyAlertDialog myAlertDialog =new MyAlertDialog(MainActivity.this, new MyDialogCallback() {
                @Override
                public void onPositiveClick(MyAlertDialog dialog) {
                    super.onPositiveClick(dialog);
                    ARouter.getInstance().build(ARouterPath.ResetPasswordActivity).navigation();
                }
            });
            myAlertDialog.setMessage(getString(R.string.alert_password_dialog));
            myAlertDialog.setPositiveText(getString(R.string.go_to_setting));
            myAlertDialog.show();
        }
    }

    private void setRadioButtonState(int indexRadioButton) {
        viewBinding.firstPageRB.setTextColor(getResources().getColor(R.color.color_595757));
        viewBinding.classifyRB.setTextColor(getResources().getColor(R.color.color_595757));
        viewBinding.personalRB.setTextColor(getResources().getColor(R.color.color_595757));
        Utils.setButtonDrawableTint(viewBinding.firstPageRB,R.color.color_595757,1);
        Utils.setButtonDrawableTint(viewBinding.classifyRB,R.color.color_595757,1);
        Utils.setButtonDrawableTint(viewBinding.personalRB,R.color.color_595757,1);
        if (indexRadioButton==0) {
            Utils.setButtonDrawableTint(viewBinding.firstPageRB,Utils.getThemeColor(getApplicationContext()), 1);
            viewBinding.firstPageRB.setTextColor(Utils.getThemeColor(getApplicationContext()));
        } else if (indexRadioButton==1) {
            Utils.setButtonDrawableTint(viewBinding.classifyRB, Utils.getThemeColor(getApplicationContext()),1);
            viewBinding.classifyRB.setTextColor(Utils.getThemeColor(getApplicationContext()));
        } else {
            Utils.setButtonDrawableTint(viewBinding.personalRB,Utils.getThemeColor(getApplicationContext()),1);
            viewBinding.personalRB.setTextColor(Utils.getThemeColor(getApplicationContext()));
        }
    }

    //四个按钮对应点击事件
    private void showFragment(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentview.get(position).isAdded()){
            if (position==positionSelected) return;
            if (fragmentview.get(positionSelected).isAdded())
                transaction.show(fragmentview.get(position)).hide(fragmentview.get(positionSelected)).commitAllowingStateLoss();
            else
                transaction.show(fragmentview.get(position)).commitAllowingStateLoss();
        }else {
            if (fragmentview.get(positionSelected).isAdded())
                transaction.add(R.id.fragmentMain,fragmentview.get(position)).hide(fragmentview.get(positionSelected)).commitAllowingStateLoss();
            else
                transaction.add(R.id.fragmentMain,fragmentview.get(position)).commitAllowingStateLoss();
        }
        positionSelected =position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByClick(); //点击两次返回退出
        }
        return false;
    }

    private boolean isExit;

    private void exitByClick() {
        Timer timer = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再次点击退出程序哦~", Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; //取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_FINISH));
            finish();
            ActivityCollector.finishAllActivity(); //封装退出方法
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getAppVersionData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAlertDialog!=null && myAlertDialog.isShowing()) myAlertDialog.dismiss();
    }

    private void getAppVersionData() {
        nameApk = getString(R.string.app_name) + ".apk";
        apkDownloadPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";
        Map<String, String> map = new HashMap<>();
        map.put("type", "Android");
        int versionCode;
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (Exception e) {
            versionCode = 0;
        }
        if (versionCode == 0) return;
        map.put("versionNum", "" + versionCode);
        int finalVersionCode = versionCode;
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.APP_UPDATE, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")) {
                    AppVersionEntity appVersionEntity = JSONObject.parseObject(response, AppVersionEntity.class);
                    if (appVersionEntity == null) return;
                    if (finalVersionCode < appVersionEntity.getNewNum() && Utils.getString(appVersionEntity.getIsMust()).equals("1")) {
                        //强制更新
                        myAlertDialog = new MyAlertDialog(MainActivity.this, new MyDialogCallback() {
                            @Override
                            public void onPositiveClick(MyAlertDialog dialog) {
                                if (isErrorInstall) {
                                    finish();
                                    ActivityCollector.finishAllActivity();
                                } else {
                                    boolean haveInstallPermission = true;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        haveInstallPermission = getPackageManager().canRequestPackageInstalls();
                                    }
                                    if (haveInstallPermission) {
                                        myAlertDialog.setPositiveText(getString(R.string.update_alert_installing));
                                        myAlertDialog.setPositivateClickAble(false);
                                        downloadApk();
                                    } else {
                                        ToastUtil.showLong(getApplicationContext(), "安装应用需要您手动在设置中开启权限");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            //注意这个是8.0新API
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                                            startActivityForResult(intent, REQUEST_CODE_INSTALL_APK);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onNegativeClick(MyAlertDialog dialog) {
                                super.onNegativeClick(dialog);
                                finish();
                                ActivityCollector.finishAllActivity();
                            }
                        });
                        myAlertDialog.setTitle(getString(R.string.update_alert));
                        myAlertDialog.setMessage(Utils.getString(appVersionEntity.getRemark()));
                        myAlertDialog.setPositiveText(getString(R.string.update_rightnow));
                        myAlertDialog.setShowSeekBar(true);
                        myAlertDialog.setNegativeButtonGone();
                        myAlertDialog.setCanceledOnTouchOutside(false);
                        myAlertDialog.setOnKeyListener((dialog, keyCode, event) -> {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                return true;
                            }
                            return false;
                        });
                        myAlertDialog.show();
                    }
                }
            }
        });
    }

    private void downloadApk(){
        //删除个人信息
        MyApplication.getInstance().saveUserEntity(null);
        File file =new File(apkDownloadPath+nameApk);
        if (file.exists()) file.delete();
        OkHttpUtils.get().url(InterfaceClass.APP_UPDATE_URL).tag(this).build().execute(new FileCallBack(apkDownloadPath,nameApk) {
            @Override
            public void onError(Call call, Exception e, int id) {
                myAlertDialog.setMessage(getString(R.string.error_download));
                myAlertDialog.setPositiveText(getString(R.string.confirm2));
                isErrorInstall =true;
                myAlertDialog.setPositivateClickAble(true);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                runOnUiThread(()->{
                    if (myAlertDialog!=null) myAlertDialog.setSeekBarProgress(Math.round(progress*100));
                });
            }

            @Override
            public void onResponse(File response, int id) {
                installApk(file);
            }
        });
    }

    private void installApk(File file){
        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (!file.exists() || dManager==null) {
            myAlertDialog.setMessage(getString(R.string.error_install));
            myAlertDialog.setPositiveText(getString(R.string.confirm2));
            isErrorInstall =true;
            myAlertDialog.setPositivateClickAble(true);
            return;
        }
        isErrorInstall =false;
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        startActivity(install);
        finish();
        ActivityCollector.finishAllActivity(); //封装退出方法
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_INSTALL_APK &&resultCode==RESULT_OK){
            myAlertDialog.setPositiveText(getString(R.string.update_alert_installing));
            myAlertDialog.setPositivateClickAble(false);
            downloadApk();
        }
    }
}

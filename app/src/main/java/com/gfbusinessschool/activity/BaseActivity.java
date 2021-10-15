package com.gfbusinessschool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.RequestPermissionCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.dialog.LoadingDialog;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.PhotoUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    private final int mRequestCode = 9000;
    public static final int REQUEST_CODE_MY = 1000;
    protected static final int REQUEST_CODE_ALBUM = REQUEST_CODE_MY +1;
    protected static final int REQUEST_CODE_CAMERA = REQUEST_CODE_ALBUM +1;
    protected static final int REQUEST_CODE_CITY_SELECT = REQUEST_CODE_CAMERA +1;
    protected static final int REQUEST_CODE_ALBUM_VIDEO = REQUEST_CODE_CITY_SELECT +1;
    protected static final int REQUEST_CODE_RELEASE_SHARE= REQUEST_CODE_ALBUM_VIDEO +1;
    public static final int REQUEST_CODE_TEST= REQUEST_CODE_RELEASE_SHARE +1;
    public static final int REQUEST_CODE_MODIFY_ADDRESS= REQUEST_CODE_TEST +1;
    public static final int REQUEST_CODE_ADD_ADDRESS= REQUEST_CODE_MODIFY_ADDRESS +1;
    public static final int RESULT_CODE_DELETE= REQUEST_CODE_ADD_ADDRESS +1;
    public static final int REQUEST_CODE_PHONE_FILE= RESULT_CODE_DELETE +1;
    public static final int REQUEST_CODE_RELEASE_READCOLLECTION= REQUEST_CODE_PHONE_FILE +1;
    public static final int REQUEST_CODE_RESEARCH= REQUEST_CODE_RELEASE_READCOLLECTION +1;
    public static final int REQUEST_CODE_INSTALL_APK= REQUEST_CODE_RESEARCH +1;
    public static final int REQUEST_CODE_OPEN_FLOATWINDOW= REQUEST_CODE_INSTALL_APK +1;

    protected LoadingDialog loadingDialog;
    private RequestPermissionCallBack mRequestPermissionCallBack;
    protected T viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            viewBinding = (T) inflate.invoke(null, getLayoutInflater());
            setContentView(viewBinding.getRoot());
            ActivityCollector.addActivity(BaseActivity.this);
            setStateBar();
            //ARouter inject 注入
            ARouter.getInstance().inject(BaseActivity.this);
            initView();
            initData();
        } catch (NoSuchMethodException | IllegalAccessException| InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void initView(){

    }

    protected void initData(){

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStateBar();
        //ARouter inject 注入
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//当一个Activity调用onDestroy方法时，就将其从activity列表中删除
    }

    protected void setStateBar(){
        MyStatusBarUtils.setTransparent(BaseActivity.this,true);
    }

    protected void showLoadingDialog(){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(BaseActivity.this);
        }
        if (!loadingDialog.isShowing()) loadingDialog.show();
    }

    protected void showLoadingDialog(boolean isCancelable){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(BaseActivity.this);
        }
        loadingDialog.setmIsCancelable(isCancelable);
        if (!loadingDialog.isShowing()) loadingDialog.show();
    }

    protected void showLoadingDialog(String alertMsg){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(BaseActivity.this);
            loadingDialog.setAlertMsg(alertMsg);
        }
        if (!loadingDialog.isShowing()) loadingDialog.show();
    }


    protected void dismissLoadingDialog(){
        if (loadingDialog!=null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void showKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            imm.showSoftInput(editText,0);
        }
    }

    /**
     * 权限请求结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        StringBuilder permissionName = new StringBuilder();
        for (String s : permissions) {
            permissionName = permissionName.append(s + "\r\n");
        }
        switch (requestCode) {
            case mRequestCode: {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false;
                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                        // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            mRequestPermissionCallBack.refused();
                        } else {
                            //用户拒绝权限请求，但未选中“不再提示”选项
                            mRequestPermissionCallBack.denied();
                        }
                        break;
                    }
                }
                if (hasAllGranted) {
                    mRequestPermissionCallBack.granted();
                }
            }
        }
    }

    public boolean isAllowedPermission(String[] permissions){
        ArrayList<String> listPermission =new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i=0;i<permissions.length;i++){
                if (ContextCompat.checkSelfPermission(getApplicationContext(),permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(permissions[i]);
                }
            }
            if (listPermission.size()>0){
                return false;
            }
        }
        return true;
    }

    public List<String> getNotAllowPersimission(String[] permissions){
        List<String> permissionsNotAllow =new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i=0;i<permissions.length;i++){
                if (ContextCompat.checkSelfPermission(getApplicationContext(),permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotAllow.add(permissions[i]);
                }
            }
        }
        return permissionsNotAllow;
    }

    /**
     * 权限已被拒绝，打开App设置权限页
     * @param msg
     */
    public void showPermissionAlert(Activity activity, String msg){
        MyAlertDialog myAlertDialog =new MyAlertDialog(this, new MyDialogCallback() {
            @Override
            public void onPositiveClick(MyAlertDialog dialog) {
                super.onPositiveClick(dialog);
                Utils.loadPermissionRequestView(activity);
            }
        });
        myAlertDialog.setMessage(msg);
        myAlertDialog.setPositiveText(getString(R.string.go_to_setting));
        myAlertDialog.show();
    }

    /**
     * 发起权限请求
     *
     * @param permissions
     * @param callback
     */
    public void requestPermissions(final String[] permissions,
                                   RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                ActivityCompat.requestPermissions(this, permissions, mRequestCode);
                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    protected void callHotline(String phone){
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(getApplicationContext(),getString(R.string.personal_noSimCard));
        }
    }

    protected void loadAlbumIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } catch (Exception e) {
            Toast.makeText(BaseActivity.this, "很抱歉，当前您的手机不支持相册选择功能，请安装相册软件", Toast.LENGTH_SHORT).show();
        }
    }

    protected String loadCameraIcon(){
        String rootPath = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
        File photo_file = new File(rootPath);
        if (!photo_file.exists()) {
            photo_file.mkdirs();
        }
        String pathCamera =photo_file + "/拍照头像"+System.currentTimeMillis()+".jpg";
        photo_file = new File(pathCamera);
        Uri imageUri = FileProvider.getUriForFile(this, "com.gfbusinessschool.provider", photo_file);
        PhotoUtils.takePicture(this, imageUri, REQUEST_CODE_CAMERA);
        return pathCamera;
    }

    protected void loadAlbumVideo(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_ALBUM_VIDEO);
    }

    protected void loadPhoneFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf;application/doc;application/docx;application/pptx;application/xlsx;application/xls");
        startActivityForResult(intent, REQUEST_CODE_PHONE_FILE);
    }

    public void showNullListView(boolean isError, SwipeRefreshLayout swipeRefresh,
                                 RecyclerView recyclerView, ViewGroup viewGroup, TextView tvNodata, ImageView iconNodata){
        dismissLoadingDialog();
        swipeRefresh.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
        viewGroup.setVisibility(View.VISIBLE);
        if (isError){
            tvNodata.setText("加载失败，请刷新后重试!");
            iconNodata.setImageResource(R.mipmap.placeholder_network);
        }else {
            tvNodata.setText("暂无数据");
            iconNodata.setImageResource(R.mipmap.placeholder_list);
        }
    }
}

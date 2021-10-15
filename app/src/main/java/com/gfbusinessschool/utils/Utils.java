package com.gfbusinessschool.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.loader.content.CursorLoader;

import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static com.yalantis.ucrop.util.FileUtils.getDataColumn;
import static com.yalantis.ucrop.util.FileUtils.isDownloadsDocument;
import static com.yalantis.ucrop.util.FileUtils.isExternalStorageDocument;
import static com.yalantis.ucrop.util.FileUtils.isGooglePhotosUri;
import static com.yalantis.ucrop.util.FileUtils.isMediaDocument;

public class Utils {
    //cd E:\Android\Sdk\platform-tools
    // adb connect 127.0.0.1:7555 mumu
    // adb connect 127.0.0.1:21503
    //$ logcat -v time | grep -Ei “[jiguang” | tee /sdcard/111.log

    public static final String TAG_LOGIN ="MyLog 登录";
    public static final String TAG_NETWORK ="MyLog 接口";
    public static final String TAG_ORTHER ="MyLog 其他";
    public static final String TAG_FILE ="MyLog 文件";
    public static final String TAG_ERROR ="MyLog MyError";
    public static final String TAG_ModifyHeadImg ="MyLog 修改头像";
    public static final String TAG_UPLOAD_IMG_FILE ="MyLog 图片上传";
    public static final String TAG_UPLOAD_QINIU="MyLog qiniu";
    public static final String TAG_JIGUANG="JIGUANG";

    public static final boolean isTest =true;//测试环境打印数据

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static String getTimeFormat(String time){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return simpleDateFormat.format(simpleDateFormat.parse(time));
        } catch (Exception e) {
            return simpleDateFormat.format(new Date());
        }
    }

    public static String getDate(String time){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.format(simpleDateFormat.parse(time));
        } catch (Exception e) {
            return simpleDateFormat.format(new Date());
        }
    }

    public static void log(String tag,String msg){
        if (isTest)
            Log.e(tag,msg);
    }

    public static int getScreenWidth(Activity context){
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity context){
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 是否是商学院vip
     * @param vip
     * @return
     */
    public static boolean isSXYVip(String vip){
        if (Integer.parseInt(vip)==1 || Integer.parseInt(vip)==3){
            return true;
        }
        return false;
    }

    public static String isFree(String price){
        if (price==null || price.trim().isEmpty() ||price.trim().equalsIgnoreCase("null") || price.trim().equals("0")
                || price.trim().equals("0.00")){
            return "免费";
        }else {
            return price;
        }
    }

    public static boolean isFreeBool(String price){
        if (price==null || price.trim().isEmpty() ||price.trim().equalsIgnoreCase("null") || price.trim().equals("0")
                || price.trim().equals("0.00")){
            return true;
        }else {
            return false;
        }
    }

    public static String getPrice(String price){
        if (price==null || price.trim().isEmpty() ||price.trim().equalsIgnoreCase("null") || price.equals("0") || price.equals("0.00") ){
            return "免费";
        }else {
            return price+"鲁班币";
        }
    }

    public static void hideSoftInputFromWindow(Activity activity , View view){
        if (activity==null || activity.isFinishing() || activity.getWindow()==null) return;
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);//获取当前界面可视部分
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();//获取屏幕高度
        int heiDifference = screenHeight - rect.bottom;//获取键盘高度，键盘没有弹出时，高度为0，键盘弹出时，高度为正数
        if (heiDifference != 0) {
            //do：键盘弹出时
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        }
    }

    public static ProgressDialog showProgressDialog(Context context){
        ProgressDialog pro = new ProgressDialog(context);
        pro.setMessage(context.getString(R.string.fs_loading));
        pro.setCancelable(true);
        pro.show();
        return pro;
    }

    public static boolean isAllowedPermission(ArrayList<String> list, Activity activity){
        ArrayList<String> listPermission =new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i=0;i<list.size();i++){
                if (ContextCompat.checkSelfPermission(activity,list.get(i)) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(list.get(i));
                }
            }
            if (listPermission.size()>0){
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(Activity activity,ArrayList<String> list,int requestCode){
        String[] ps = new String[list.size()];
        list.toArray(ps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            activity.requestPermissions(ps, requestCode);
    }

    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    public static boolean isEmpty(String str){
        if (str==null || str.trim().isEmpty() || str.trim().equals("") || str.trim().equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    public static String getString(String str){
        if (str==null || str.trim().isEmpty() || str.trim().equals("") || str.trim().equalsIgnoreCase("null"))
            return "";
        else
            return str;
    }
    /**
     * 加载手机权限列表界面
     */
    public static void loadPermissionRequestView(Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * 判断手机号是否正确
     * @param mobileNums 手机号
     * @return
     */
    public static boolean isPhoneNo(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 裁剪头像方形
     * @param context
     * @param sourceUri 原图片地址
     * @param headName 修改的图片名字
     */
    public static void startHeadUCropRectangle(Activity context, Uri sourceUri, String headName){
        //裁剪后保存到文件中
        String rootPath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
        File photo_file = new File(rootPath);
        if (!photo_file.exists()) {
            photo_file.mkdirs();
        }
        Uri destinationUri = Uri.fromFile(new File(photo_file, headName));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        uCrop.withAspectRatio(1,1);
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(context, R.color.theme_text_color));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(context, R.color.theme_text_color));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        uCrop.start(context);
    }

    /**
     * 裁剪头像（设置不裁剪比例）
     * @param context
     * @param sourceUri 原图片地址
     * @param headName 修改的图片名字
     */
    public static void startHeadUCrop(Activity context, Uri sourceUri, String headName){
        //裁剪后保存到文件中
        String rootPath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
        File photo_file = new File(rootPath);
        if (!photo_file.exists()) {
            photo_file.mkdirs();
        }
        Uri destinationUri = Uri.fromFile(new File(photo_file, headName));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(context, R.color.theme_text_color));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(context, R.color.theme_text_color));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        uCrop.start(context);
    }


    public static String getFileRealNameFromUri(Context context, Uri fileUri) {
        if (context == null || fileUri == null) return null;
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, fileUri);
        if (documentFile == null) return null;
        return documentFile.getName();
    }

    public static int getThemeColor(Context context){
        return (MyApplication.getInstance().getAppUserEntity()==null ||
                Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getThemeColour()))?
                context.getResources().getColor(R.color.theme_text_color):
                Color.parseColor(MyApplication.getInstance().getAppUserEntity().getThemeColour());
    }

    public static void setImageViewTint(Context context, ImageView imageView) {
        int color =getThemeColor(context);
        Drawable up =imageView.getDrawable();
        Drawable drawable= DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawable, color);
        imageView.setImageDrawable(drawable);
    }

    public static void setImageViewTint(ImageView imageView,int color) {
        Drawable up =imageView.getDrawable();
        Drawable drawable= DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawable, color);
        imageView.setImageDrawable(drawable);
    }

    public static void setDrawableTint(Context context,int drawableId, TextView btn, int type) {
        int color =getThemeColor(context);
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),drawableId,context.getTheme());
        if (drawable==null) return;
        drawable.setBounds(0, 0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        Drawable newdrawable= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newdrawable, color);
        if (type == 0) {
            btn.setCompoundDrawables(newdrawable, null, null, null);
        } else if (type == 1) {
            btn.setCompoundDrawables(null, newdrawable, null, null);
        } else if (type == 2) {
            btn.setCompoundDrawables(null, null, newdrawable, null);
        } else {
            btn.setCompoundDrawables(null, null, null, newdrawable);
        }
    }

    /**
     * 设置两个drawable
     * @param context
     * @param drawableId1
     * @param drawableId2
     * @param btn
     * @param type1
     * @param type2
     */
    public static void setDrawableTint2(Context context,int drawableId1, int drawableId2, TextView btn, int type1, int type2) {
        int color =getThemeColor(context);
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),drawableId1,context.getTheme());
        if (drawable==null) return;
        drawable.setBounds(0, 0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        Drawable newdrawable= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newdrawable, color);

        Drawable drawable2 = ResourcesCompat.getDrawable(context.getResources(),drawableId2,context.getTheme());
        if (drawable2==null) return;
        drawable2.setBounds(0, 0,drawable2.getMinimumWidth(),drawable2.getMinimumHeight());
        Drawable newdrawable2= DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(newdrawable2, color);
        if (type1 == 0) {
            if (type2 == 1)
                btn.setCompoundDrawables(newdrawable, newdrawable2, null, null);
            else if (type2 == 2)
                btn.setCompoundDrawables(newdrawable, null, newdrawable2, null);
            else
                btn.setCompoundDrawables(newdrawable, null, null, newdrawable2);

        } else if (type1 == 1) {
            if (type2 == 2)
                btn.setCompoundDrawables( null,newdrawable, newdrawable2, null);
            else
                btn.setCompoundDrawables( null,newdrawable, null, newdrawable2);
        } else if (type1 == 2) {
            btn.setCompoundDrawables(null, null, newdrawable2, newdrawable);
        } else {
            btn.setCompoundDrawables(null, null, null, newdrawable2);
        }
    }

    public static void setButtonDrawableTint(TextView btn, int color, int type) {
        Drawable up;
        if (type==0)
            up =btn.getCompoundDrawables()[0];
        else if (type==1)
            up =btn.getCompoundDrawables()[1];
        else if (type==2)
            up =btn.getCompoundDrawables()[2];
        else
            up =btn.getCompoundDrawables()[3];
        Drawable drawable= DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawable, color);
        if (type==0)
            btn.setCompoundDrawables(drawable,null,null,null);
        else if (type==1)
            btn.setCompoundDrawables(null,drawable,null,null);
        else if (type==2)
            btn.setCompoundDrawables(null,null,drawable,null);
        else
            btn.setCompoundDrawables(null,null,null,drawable);
    }

    public static void setBackgroundSolid(Context context, View view){
        try {
            GradientDrawable cate_bg =(GradientDrawable)view.getBackground();
            cate_bg.setColor(getThemeColor(context));
        } catch (Exception e) {
            if (Utils.isTest)
                e.printStackTrace();
        }
    }

    public static void setBackgroundSolid(View view,int color){
        try {
            GradientDrawable cate_bg =(GradientDrawable)view.getBackground();
            cate_bg.setColor(color);
        } catch (Exception e) {
            if (Utils.isTest)
                e.printStackTrace();
        }
    }

    public static void setBackgroundStoken(Context context, View view){
        try {
            GradientDrawable cate_bg =(GradientDrawable)view.getBackground();
            cate_bg.setStroke((int) context.getResources().getDimension(R.dimen.px1),getThemeColor(context));
        } catch (Exception e) {
            if (Utils.isTest)
                e.printStackTrace();
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * Java文件操作 获取文件扩展名
     * */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static int getIntData(String content){
        try {
            if (isEmpty(content))
                return 0;
            else
                return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForegroundActivity(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> list = am.getAppTasks();
            for (ActivityManager.AppTask appTask: list) {
                if (appTask.getTaskInfo().topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
                    return true;
                }
            }
        }else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            for (ActivityManager.RunningTaskInfo taskInfo : list) {
                if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue 要转换的dp值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue 要转换的px值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

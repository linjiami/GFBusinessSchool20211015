package com.gfbusinessschool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gfbusinessschool.R;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyStatusBarUtils extends StatusBarUtil {

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (context==null) return 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    /**
     * 修改状态栏颜色
     * @param activity
     * @param color
     * @param isEditStatusbarTextColor 是否修改状态栏文字颜色，针对白色相近的颜色，因为状态栏文字是白色
     */
    public static void setColor(Activity activity, int color, boolean isEditStatusbarTextColor){
        StatusBarUtil.setColor(activity,color,0);
        if (isEditStatusbarTextColor)
            setStatusBarLightMode(activity,true);
    }
    /**
     * 修改状态栏全透明
     * @param activity
     * @param isEditStatusbarTextColor 是否修改状态栏文字颜色，针对白色相近的颜色，因为状态栏文字是白色
     */
    public static void setTransparent(Activity activity,boolean isEditStatusbarTextColor){
        StatusBarUtil.setTransparent(activity);
        if (isEditStatusbarTextColor)
            setStatusBarLightMode(activity,true);
    }

    /**
     * 修改状态栏透明度
     * @param activity
     * @param alpha
     */
    public static void setTransparentAipha(Activity activity,int alpha){
        StatusBarUtil.setTranslucent(activity,alpha);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity       需要设置的activity
     */
    public static void setTranslucentForImageView(Activity activity) {
        StatusBarUtil.setTranslucentForImageView(activity,0,null);
    }
    /**
     * 设置状态栏文字颜色  true 为黑色  false 为白色（XML跟布局配置  android:fitsSystemWindows="true"，否则布局会上移）
     */
    private static void setStatusBarLightMode(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (dark) {  //
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //其他
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
    }
    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void setStatusBar(Activity activity, boolean isImageviewFragemnt){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ("Xiaomi".equals(Build.MANUFACTURER)) {
                if (isImageviewFragemnt) {
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(Color.TRANSPARENT);
                        window.getDecorView()
                                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        CommonUtil.StatusBarLightMode(activity, 1);
                    }
                } else {
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(Color.TRANSPARENT);
                        window.getDecorView()
                                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                    StatusBarUtil.setColor(activity, activity.getResources().getColor(R.color.white), 0);
                    CommonUtil.StatusBarLightMode(activity, 1);
                }
            } else if ("MEIZU".equals(Build.MANUFACTURER)) {
                //识别魅族手机 测试下魅族5.0的手机
                if (isImageviewFragemnt) {
                    StatusBarUtil.setTransparentForImageViewInFragment(activity, null);
                } else {
                    StatusBarUtil.setColor(activity, Color.WHITE, 0);
                }
                CommonUtil.StatusBarLightMode(activity, 2);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isImageviewFragemnt) {
                    StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, null);
                } else {
                    StatusBarUtil.setColor(activity, Color.WHITE, 0);
                }
                CommonUtil.StatusBarLightMode(activity, 3);
            } else {
                StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, null);
            }
        } else {
            StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, null);
        }
    }
}
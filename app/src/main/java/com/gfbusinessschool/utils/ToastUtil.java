package com.gfbusinessschool.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    private static Toast sToast;
    private static Handler handler=new Handler(Looper.getMainLooper());
    public static void showToast(Context context, String msg){
        if(sToast==null){
            sToast=Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        if(Looper.myLooper()==Looper.getMainLooper()){
            sToast.show();
        }else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sToast.show();
                }
            });
        }
    }

    static Toast toast = null;
    public static void show(Context context, String text) {
        try {
            toast= Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setText(text);
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void showLong(Context context, String text) {
        try {
            toast= Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setText(text);
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void showCenterLong(Context context, String text) {
        try {
            toast= Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setText(text);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void show(Context context, String text,int duration) {
        try {
            toast= Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setDuration(duration);
            toast.setText(text);
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}

package com.gfbusinessschool.activity;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.R;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.MenegerEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AudioFloatWindowService extends Service {
    private ObjectAnimator mCircleAnimator;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager mWindowManager;
    private View mAudioView;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(AudioFloatWindowService.this);
        //初始化悬浮窗UI
        initWindowData();
        MyApplication.getInstance().setIsStartAudioService(true);
    }

    private void initWindowData() {
        layoutParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mAudioView =  LayoutInflater.from(this).inflate(R.layout.audio_floatwindow, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 20;
        layoutParams.y = mWindowManager.getDefaultDisplay().getHeight()/5;
        mWindowManager.addView(mAudioView, layoutParams);
        mAudioView.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.AudioDetailsActivity).navigation();
            if (mCircleAnimator!=null && mCircleAnimator.isRunning())
                mCircleAnimator.cancel();
            if (mWindowManager!=null && mAudioView!=null)
                mWindowManager.removeView(mAudioView);
            stopSelf();
        });
        startAudioAnimation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(AudioFloatWindowService.this))
            EventBus.getDefault().unregister(AudioFloatWindowService.this);
        MyApplication.getInstance().setIsStartAudioService(false);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MenegerEvent event) {
        if (event==null) return;
        switch (event.typeEvent){
            case MenegerEvent.TYPE_AUDIO_CLOSE_FLOATWIN:
            case MenegerEvent.TYPE_AUDIO_FINISH:
                if (mCircleAnimator!=null && mCircleAnimator.isRunning())
                    mCircleAnimator.cancel();
                if (mWindowManager!=null && mAudioView!=null)
                    mWindowManager.removeView(mAudioView);
                stopSelf();
                break;
        }
    }

    private void startAudioAnimation(){
        mCircleAnimator = ObjectAnimator.ofFloat(mAudioView, "rotation", 0.0f, 360.0f);
        mCircleAnimator.setDuration(20000);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.setRepeatCount(-1);
        mCircleAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mCircleAnimator.start();
    }
}
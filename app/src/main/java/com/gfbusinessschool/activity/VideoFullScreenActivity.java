package com.gfbusinessschool.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityVideofullscreenBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 优秀学员视频（全屏播放）
 */
@Route(path = ARouterPath.VideoFullScreenActivity)
public class VideoFullScreenActivity extends BaseActivity<ActivityVideofullscreenBinding>{
    @Autowired
    String videoUrl;
    private Timer mTimer;

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setTranslucentForImageView(VideoFullScreenActivity.this,viewBinding.backImg);
    }

    @Override
    protected void initView() {
        super.initView();
        if (Utils.isEmpty(videoUrl)){
            ToastUtil.show(getApplicationContext(),getString(R.string.alert_null_videourl));
            finish();
        }
        //关闭音频播放
        EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_FINISH));
        initPlayerParams();
        viewBinding.detailPlayer.setUp(videoUrl,true,"");
        viewBinding.detailPlayer.startPlayLogic();
        delayRun();
    }

    private void initPlayerParams() {
        viewBinding.backImg.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        viewBinding.detailPlayer.getFullscreenButton().setVisibility(View.GONE);
        viewBinding.detailPlayer.getBackButton().setVisibility(View.GONE);
        viewBinding.detailPlayer.setIsTouchWiget(true);
        //关闭自动旋转
        viewBinding.detailPlayer.setRotateViewAuto(false);
        viewBinding.detailPlayer.setLockLand(false);
        viewBinding.detailPlayer.setShowFullAnimation(false);
        //detailPlayer.setNeedLockFull(true);
        viewBinding.detailPlayer.setAutoFullWithSize(false);
        viewBinding.detailPlayer.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {

            }

            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {

            }

            @Override
            public void onClickStartError(String url, Object... objects) {

            }

            @Override
            public void onClickStop(String url, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {

            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                viewBinding.backImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete(String url, Object... objects) {

            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {

            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {

            }

            @Override
            public void onClickBlank(String url, Object... objects) {
                if (viewBinding.backImg.getVisibility() == View.GONE){
                    viewBinding.backImg.setVisibility(View.VISIBLE);
                    delayRun();
                }else {
                    mTimer.cancel();
                    mTimer =null;
                    viewBinding.backImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {

            }
        });
    }

    private void delayRun() {
        if (mTimer==null)
            mTimer =new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> viewBinding.backImg.setVisibility(View.GONE));
            }
        },2500);
    }

    @Override
    public void onBackPressed() {
        viewBinding.detailPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBinding.detailPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.detailPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
package com.gfbusinessschool.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.bean.AudioEntity;
import com.gfbusinessschool.databinding.ActivityAudiodetailsBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.fragment.AudioIntroduceFragment;
import com.gfbusinessschool.fragment.CourseWareFragment;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音频详情页
 */
@Route(path = ARouterPath.AudioDetailsActivity)
public class AudioDetailsActivity extends BaseActivity<ActivityAudiodetailsBinding> implements View.OnClickListener {
    @Autowired
    String courseId;
    private MediaPlayer mediaPlayer;
    private AudioEntity audioEntity;
    private boolean isInitFinish;//是否初始化完成
    private Timer timer;
    private int lookVideoTime;//观看视频时长
    private int currentProgress;
    private Handler handler=new Handler();
    private List<Fragment> fragmentList =new ArrayList<>();
    Runnable runnableUpdate =new Runnable(){

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            try {
                if (mediaPlayer==null) return;
                handler.postDelayed(runnableUpdate, 1000);//每秒钟更新一次
                if (mediaPlayer.isPlaying()){
                    viewBinding.progressCase.setText(generateTime(mediaPlayer.getCurrentPosition())+"/"+generateTime(mediaPlayer.getDuration()));
                    currentProgress =mediaPlayer.getCurrentPosition()/1000;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void setStateBar() {
        MyStatusBarUtils.setTranslucentForImageView(AudioDetailsActivity.this,0,viewBinding.iconBack);
    }

    @Override
    protected void initView() {
        super.initView();
        if (Utils.isEmpty(courseId)){
            ToastUtil.show(getApplicationContext(),"id不能为空");
            finish();
        }
        EventBus.getDefault().register(AudioDetailsActivity.this);
        ConstraintLayout.LayoutParams params= (ConstraintLayout.LayoutParams) viewBinding.cover.getLayoutParams();
        params.height = (int) (MyApplication.getInstance().screenWidth*9.0/16);
        viewBinding.cover.setLayoutParams(params);
        viewBinding.iconBack.setOnClickListener(this);
        viewBinding.closeIconCase.setOnClickListener(this);
        viewBinding.playIconCase.setOnClickListener(this);
        fragmentList.add(new AudioIntroduceFragment());
        fragmentList.add(new CourseWareFragment(CourseWareFragment.TYPEVIEW_AUDIO, courseId, new OnClickCallBack() {
            @Override
            public void onClick() {
                moveTaskToBack(true);
            }
        }));
        viewBinding.viewPagerCourse.setAdapter(new ClassDetailPagerAdapter(getSupportFragmentManager(),
                fragmentList,getResources().getStringArray(R.array.audioTabArray)));
        viewBinding.introductionTabCourse.setupWithViewPager(viewBinding.viewPagerCourse);
        showLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iconBack:
                onClickBack();
                break;
            case R.id.playIconCase:
                if (audioEntity==null) return;
                if (!isInitFinish){
                    ToastUtil.show(getApplicationContext(),getString(R.string.tools_audio_loading));
                    return;
                }
                if (mediaPlayer==null){
                    initMediaPlayer(audioEntity.getAudioUrl());
                } else if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    viewBinding.playIconCase.setImageResource(R.mipmap.audio_pause);
                    if (timer!=null) {
                        timer.cancel();
                        timer=null;
                    }
                }else if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    handler.post(runnableUpdate);
                    viewBinding.playIconCase.setImageResource(R.mipmap.audio_play);
                    startTimer();
                }
                break;
            case R.id.closeIconCase:
                viewBinding.musicLayoutCase.setVisibility(View.GONE);
                if (mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                if (timer!=null) {
                    timer.cancel();
                    timer=null;
                }
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (Utils.isEmpty(courseId)) return;
        Map<String,String> map =new HashMap<>();
        map.put("courseId",courseId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.AUDIO_DETAILS, map, new NetWorkCallback() {
            @Override
            public void onRequestError() {
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String code, String response) {
                dismissLoadingDialog();
                if (Utils.getString(code).equals("200")){
                    audioEntity = JSONObject.parseObject(response,AudioEntity.class);
                    if (audioEntity==null){
                        return;
                    }
                    GlideLoadUtils.load(getApplicationContext(),audioEntity.getLogoUrl(),viewBinding.cover,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    GlideLoadUtils.load(getApplicationContext(),audioEntity.getLogoUrl(),viewBinding.iconMusicCase,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    for (Fragment fragment : fragmentList){
                        if (fragment instanceof AudioIntroduceFragment){
                            AudioIntroduceFragment introduceFragment = (AudioIntroduceFragment) fragment;
                            introduceFragment.setAudioEntity(audioEntity);
                        }else if (fragment instanceof CourseWareFragment){
                            CourseWareFragment wareFragment = (CourseWareFragment) fragment;
                            wareFragment.setTopText(Utils.getString(audioEntity.getName()),
                                    String.format(getString(R.string.place_study_count),
                                            audioEntity.getStuNum()+""));
                        }
                    }
                    viewBinding.titleCase.setText(Utils.getString(audioEntity.getName()));
                    initMediaPlayer(audioEntity.getAudioUrl());
                }
            }
        })                     ;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onClickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(AudioDetailsActivity.this)){
            EventBus.getDefault().unregister(AudioDetailsActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_OPEN_FLOATWINDOW){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)){//允许悬浮窗权限
                    backToHomeTask();
                }else {
                    closeCurrentActivity();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MenegerEvent event) {
        if (event==null) return;
        switch (event.typeEvent){
            case MenegerEvent.TYPE_AUDIO_FINISH:
                if (timer!=null) {
                    timer.cancel();
                    timer=null;
                }
                closeCurrentActivity();
                break;
        }
    }

    private void onClickBack(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)){
                    MyAlertDialog myAlertDialog =new MyAlertDialog(AudioDetailsActivity.this, new MyDialogCallback() {
                        @Override
                        public void onPositiveClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            //启动Activity让用户授权
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent,REQUEST_CODE_OPEN_FLOATWINDOW);
                        }

                        @Override
                        public void onNegativeClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            closeCurrentActivity();
                        }
                    });
                    myAlertDialog.setCanceledOnTouchOutside(false);
                    myAlertDialog.setMessage("您如果要在后台播放音频需要开启“悬浮窗权限”，请在设置中开启");
                    myAlertDialog.show();
                }else {
                    backToHomeTask();
                }
            }else {
                backToHomeTask();
            }
        }else {
            closeCurrentActivity();
        }
    }

    private void closeCurrentActivity(){
        if (mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            if (lookVideoTime!=0)NetWorkUtils.postCourseLookTime(getApplicationContext(), courseId, currentProgress,lookVideoTime,2);
        }
        EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_CLOSE_FLOATWIN));
        releaseMediaPlayer();
        finish();
    }

    private void backToHomeTask(){
        moveTaskToBack(true);
        if (!MyApplication.getInstance().isStartAudioService()){
            startService(new Intent(AudioDetailsActivity.this,AudioFloatWindowService.class));
        }
    }

    private void initMediaPlayer(String music) {
        if (Utils.isEmpty(music)) {
            ToastUtil.show(getApplicationContext(), getString(R.string.alert_audio));
            viewBinding.musicLayoutCase.setVisibility(View.GONE);
        } else {
            viewBinding.musicLayoutCase.setVisibility(View.VISIBLE);
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(music);
                    mediaPlayer.setOnPreparedListener(mp -> {
                        if (audioEntity.getStudyLength()*1000<mediaPlayer.getDuration())
                            mediaPlayer.seekTo(audioEntity.getStudyLength()*1000);
                        isInitFinish = true;
                        dismissLoadingDialog();
                        viewBinding.progressCase.setText("00:00/" + generateTime(mediaPlayer.getDuration()) + "");
                    });
                    mediaPlayer.setOnCompletionListener(mp -> {
                        runOnUiThread(() -> {
                            if (Utils.isForegroundActivity(getApplicationContext(), getLocalClassName())) {
                                viewBinding.playIconCase.setImageResource(R.mipmap.audio_pause);
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                NetWorkUtils.postCourseLookTime(getApplicationContext(), courseId, mediaPlayer.getDuration() / 1000, lookVideoTime, 2);
                                lookVideoTime = 0;
                                releaseMediaPlayer();
                            } else {
                                //在后台播放完成，删除
                                closeCurrentActivity();
                            }
                        });
                    });
                    mediaPlayer.prepareAsync();
                }
            } catch (IOException e) {
                Utils.log(Utils.TAG_ERROR,"initMediaPlayer "+e.getMessage());
            }
        }
    }

    /**
     * 时长格式化显示
     */
    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    private void releaseMediaPlayer(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            viewBinding.playIconCase.setImageResource(R.mipmap.audio_pause);
            handler.removeCallbacks(runnableUpdate);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }

    private void startTimer(){
        if (timer!=null) timer =null;
        timer =new Timer();
        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                lookVideoTime++;
            }
        };
        timer.schedule(task,0,1000);
    }
}
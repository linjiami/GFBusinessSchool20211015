package com.gfbusinessschool.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.StudyTimeEntity;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.fragment.ChampionIntroduceFragment;
import com.gfbusinessschool.fragment.CourseCommentFragment;
import com.gfbusinessschool.fragment.CourseIntroduceFragment;
import com.gfbusinessschool.adapter.ClassDetailPagerAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.databinding.ActivityCoursedetailsBinding;
import com.gfbusinessschool.dialog.VideoSpeedPopWindow;
import com.gfbusinessschool.fragment.CourseWareFragment;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ActivityCollector;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.MenegerEvent;
import com.gfbusinessschool.utils.MyStatusBarUtils;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.gfbusinessschool.view.SpeedVideoPlayer;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = ARouterPath.ACTIVITY_CourseDetails)
public class CourseDetailsActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> implements GSYVideoProgressListener
        , View.OnClickListener {
    public static final String TYPE_VIEW_COURSE ="课程";
    public static final String TYPE_VIEW_GUANJUN_SHARE ="冠军分享课程";
    public static final String TYPE_VIEW_STUDYMAP ="学习地图课程";
    private ActivityCoursedetailsBinding viewBinding;
    @Autowired
    String courseId;
    @Autowired
    String classId;
    @Autowired
    String typeView =TYPE_VIEW_COURSE;
    private CourseBean courseBean;
    private int currentProgress;//当前播放时间，秒
    List<Fragment> fragmentList =new ArrayList<>();
    private int currentSpeedIndex=-1;
    VideoSpeedPopWindow videoSpeedPopWindow;
    private TextView btnSpeed,btnSpeedHor;
    private int progressPlay;//播放进度
    private Timer timer;
    private int lookVideoTime;//观看视频时长
    private boolean isStudyMore6Hours;//今日学习时长是否超过6小时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding =ActivityCoursedetailsBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        //关闭音频播放
        EventBus.getDefault().post(new MenegerEvent(MenegerEvent.TYPE_AUDIO_FINISH));
        MyStatusBarUtils.setTransparent(this,true);
        ActivityCollector.addActivity(CourseDetailsActivity.this);
        //ARouter inject 注入
        ARouter.getInstance().inject(CourseDetailsActivity.this);

        if (Utils.isEmpty(courseId)){
            ToastUtil.show(getApplicationContext(), getString(R.string.course_id_null_alert));
            finish();
            return;
        }
        initView();
        initData();
    }

    protected void initView() {
        viewBinding.userId.setText(Utils.getString(MyApplication.getInstance().getAppUserEntity().getId()));
        int color = (MyApplication.getInstance().getAppUserEntity()==null ||
                Utils.isEmpty(MyApplication.getInstance().getAppUserEntity().getThemeColour()))?
                getResources().getColor(R.color.theme_text_color):
                Color.parseColor(MyApplication.getInstance().getAppUserEntity().getThemeColour());
        viewBinding.introductionTabCourse.setSelectedTabIndicatorColor(color);
        if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)){
            viewBinding.iconCollect.setVisibility(View.GONE);
        }
        int width = MyApplication.getInstance().screenWidth;
        viewBinding.detailPlayer.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,width*9/16));
        viewBinding.iconBack.setOnClickListener(this);
        viewBinding.iconCollect.setOnClickListener(this);
        viewBinding.playIcon.setOnClickListener(this);
        String[] arrTitle;
        if (Utils.getString(typeView).equals(TYPE_VIEW_COURSE) ||
                Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)){
            arrTitle = getResources().getStringArray(R.array.courseTab);
            CourseCommentFragment commentFragment =new CourseCommentFragment(courseId);
            commentFragment.setTypeView(CourseCommentFragment.TYPE_COMMENT_COURSE);
            fragmentList.add(commentFragment);
            fragmentList.add(new CourseWareFragment(CourseWareFragment.TYPEVIEW_COURSE, courseId,new OnClickCallBack() {
                @Override
                public void onClick() {
                    //暂停视频播放
                    if (viewBinding.detailPlayer.getCurrentState()==GSYVideoView.CURRENT_STATE_PLAYING){
                        viewBinding.detailPlayer.onVideoPause();
                    }
                }
            }));
        }else{
            arrTitle =new String[]{""};
            ChampionIntroduceFragment courseIntroduceFragment =new ChampionIntroduceFragment();
            fragmentList.add(courseIntroduceFragment);
            viewBinding.introductionTabCourse.setVisibility(View.GONE);
            viewBinding.iconCollect.setVisibility(View.GONE);
        }
        viewBinding.viewPagerCourse.setAdapter(new ClassDetailPagerAdapter(getSupportFragmentManager(),fragmentList,arrTitle));
        viewBinding.introductionTabCourse.setupWithViewPager(viewBinding.viewPagerCourse);

        initPlayerParams();
    }

    private void initPlayerParams() {
        //普通模式
        initVideo();
        btnSpeed = showSpeedView();

        viewBinding.detailPlayer.setGSYVideoProgressListener(this);
        //隐藏返回键和title
        viewBinding.detailPlayer.getTitleTextView().setVisibility(View.GONE);
        viewBinding.detailPlayer.getStartButton().setVisibility(View.GONE);
        viewBinding.detailPlayer.setIsTouchWiget(true);
        viewBinding.detailPlayer.getBackButton().setImageResource(R.mipmap.back_course);
        viewBinding.detailPlayer.getBackButton().setOnClickListener(v -> {
            closeCurrentActivity();
        });
        //禁止拖动进度
        if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)){
            viewBinding.detailPlayer.setIsTouchWiget(false);
            viewBinding.detailPlayer.setIsTouchWigetFull(false);
            viewBinding.detailPlayer.getSeekBar().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        //关闭自动旋转
        viewBinding.detailPlayer.setRotateViewAuto(false);
        viewBinding.detailPlayer.setLockLand(false);
        viewBinding.detailPlayer.setShowFullAnimation(false);
        //detailPlayer.setNeedLockFull(true);
        viewBinding.detailPlayer.setAutoFullWithSize(false);
        //音频焦点冲突时是否释放
        viewBinding.detailPlayer.setReleaseWhenLossAudio(false);
        viewBinding.detailPlayer.setVideoAllCallBack(this);

        viewBinding.detailPlayer.setLockClickListener((view, lock) -> {
            if (orientationUtils != null) {
                //配合下方的onConfigurationChanged
                orientationUtils.setEnable(!lock);
            }
        });
    }

    protected void initData() {
        getCourseDetails();
        getStudyTimeLenth();
    }

    @Override
    protected void onPause() {
        if (viewBinding.detailPlayer.getCurrentState()==GSYVideoView.CURRENT_STATE_PLAYING && timer!=null) {
            timer.cancel();
            timer=null;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewBinding.detailPlayer.getCurrentState()==GSYVideoView.CURRENT_STATE_PLAYING) {
            startTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            timer.cancel();
            timer =null;
        }
        ActivityCollector.removeActivity(CourseDetailsActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            closeCurrentActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeCurrentActivity() {
        if (viewBinding.detailPlayer!=null && viewBinding.detailPlayer.isIfCurrentIsFullscreen()){
            viewBinding.detailPlayer.onBackFullscreen();
            return;
        }
        if (lookVideoTime != 0) {
            if (Utils.getString(typeView).equals(TYPE_VIEW_COURSE)) {
                NetWorkUtils.postCourseLookTime(getApplicationContext(), courseId, currentProgress, lookVideoTime, 1);
            } else if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)) {
                NetWorkUtils.postStudyMapCourseLookTime(getApplicationContext(), courseId, classId, currentProgress, progressPlay);
            }
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iconBack:
                closeCurrentActivity();
                break;
            case R.id.iconCollect:
                if (Utils.getString(courseBean.getIsCollection()).equals("1")){//是否收藏（0未收藏1已收藏）
                    collectCourse("0");
                }else {
                    collectCourse("1");
                }
                break;
            case R.id.playIcon:
                if (isStudyMore6Hours){
                    MyAlertDialog myAlertDialog =new MyAlertDialog(CourseDetailsActivity.this, new MyDialogCallback() {
                        @Override
                        public void onPositiveClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            startPlayVideo();
                        }
                    });
                    myAlertDialog.setPositiveText(getString(R.string.continue_study));
                    myAlertDialog.setMessage(getString(R.string.alert_study_timeout));
                    myAlertDialog.setTitle(getString(R.string.alert_study_timeout_title));
                    myAlertDialog.show();
                }else {
                    startPlayVideo();
                }
                break;
        }
    }

    private void startPlayVideo() {
        startPlayVideo();
        startTimer();
        if (viewBinding.detailPlayer.getCurrentState() == GSYVideoView.CURRENT_STATE_NORMAL) {
            //设置播放进度
            if (viewBinding.detailPlayer.getCurrentState()!=GSYVideoView.CURRENT_STATE_PLAYING){
                viewBinding.detailPlayer.setSeekOnStart(courseBean.getStudyLength()*1000);
            }
            viewBinding.detailPlayer.startPlayLogic();
        } else if (viewBinding.detailPlayer.getCurrentState() == GSYVideoView.CURRENT_STATE_PAUSE) {
            viewBinding.detailPlayer.onVideoResume();
        } else {
            viewBinding.detailPlayer.onVideoPause();
        }
        viewBinding.playPlaceHoldLayout.setVisibility(View.GONE);
        viewBinding.coverCourse.setVisibility(View.GONE);
        viewBinding.playIcon.setVisibility(View.GONE);
    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return viewBinding.detailPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        return null;
    }

    @Override
    public void clickForFullScreen() {
        btnSpeedHor = showSpeedView();
        showUserIdView();
    }

    /**
     * 是否启动旋转横屏，true表示启动
     *
     * @return true
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    @Override
    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
        this.currentProgress =currentPosition/1000;
        this.progressPlay =progress;
    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
        super.onAutoComplete(url, objects);
        if (Utils.getString(typeView).equals(TYPE_VIEW_COURSE)) {
            NetWorkUtils.postCourseLookTime(getApplicationContext(), courseId, currentProgress, lookVideoTime, 1);
        } else if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)) {
            NetWorkUtils.postStudyMapCourseLookTime(getApplicationContext(), courseId, classId, currentProgress, progressPlay);
        }
        lookVideoTime =0;
        if (timer!=null) {
            timer.cancel();
            timer=null;
        }
        courseBean.setStudyLength(0);
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
        super.onClickResumeFullscreen(url, objects);
        startTimer();
    }

    @Override
    public void onClickResume(String url, Object... objects) {
        super.onClickResume(url, objects);
        startTimer();
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
        super.onClickStopFullscreen(url, objects);
        if (timer!=null) {
            timer.cancel();
            timer=null;
        }
    }

    @Override
    public void onClickStop(String url, Object... objects) {
        super.onClickStop(url, objects);
        if (timer!=null) {
            timer.cancel();
            timer=null;
        }
    }

    private void getCourseDetails(){
        Map<String,String> map =new HashMap<>();
        String url = InterfaceClass.COURSE_DETAILS;
        if (Utils.getString(typeView).equals(TYPE_VIEW_GUANJUN_SHARE)){
            url =InterfaceClass.CHAMPIONSHARE_COURSE_DETAILS;
            map.put("id",courseId);
        }else if (Utils.getString(typeView).equals(TYPE_VIEW_STUDYMAP)){
            url =InterfaceClass.STUDYMAP_COURSE_DETAILS;
            map.put("courseId",courseId);
            map.put("specialSubjectId",classId);
        }else {
            map.put("courseId",courseId);
        }
        NetWorkUtils.getRequest(getApplicationContext(), url, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    courseBean = JSONObject.parseObject(response,CourseBean.class);
                    if (courseBean==null) return;
                    if (Utils.getString(typeView).equals(TYPE_VIEW_GUANJUN_SHARE)){
                        viewBinding.detailPlayer.setUp(courseBean.getVideoUrl(), true,Utils.getString(courseBean.getTitle()));
                        GlideLoadUtils.load(getApplicationContext(),courseBean.getCoverImgUrl(),viewBinding.coverCourse,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                    }else {
                        viewBinding.detailPlayer.setUp(courseBean.getVideoUrl(), true,Utils.getString(courseBean.getName()));
                        GlideLoadUtils.load(getApplicationContext(),courseBean.getLogoUrl(),viewBinding.coverCourse,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                        if (Utils.getString(courseBean.getIsCollection()).equals("1")){//是否收藏（0未收藏1已收藏）
                            viewBinding.iconCollect.setImageResource(R.mipmap.collected_course);
                        }else {
                            viewBinding.iconCollect.setImageResource(R.mipmap.collect_course);
                        }
                    }
                    if (Utils.getString(typeView).equals(TYPE_VIEW_GUANJUN_SHARE)){
                        if (fragmentList.size()>0 && fragmentList.get(0) instanceof ChampionIntroduceFragment){
                            ChampionIntroduceFragment courseCommentFragment = (ChampionIntroduceFragment) fragmentList.get(0);
                            courseCommentFragment.refreshDataView(courseBean);
                        }
                    }else {
                        for (Fragment fragment : fragmentList){
                            if (fragment instanceof CourseCommentFragment){
                                CourseCommentFragment courseCommentFragment = (CourseCommentFragment) fragmentList.get(0);
                                courseCommentFragment.setCourseBean(courseBean);
                            } else if (fragment instanceof CourseIntroduceFragment){
                                CourseIntroduceFragment courseCommentFragment = (CourseIntroduceFragment) fragmentList.get(0);
                                courseCommentFragment.setCourseDetailsBean(courseBean);
                            }else if (fragment instanceof CourseWareFragment){
                                CourseWareFragment courseWareFragment = (CourseWareFragment) fragment;
                                courseWareFragment.setCourseBean(courseBean);
                            }
                        }
                    }
                }
            }
        });
    }

    private void collectCourse(String isCollection){
        Map<String,String> map =new HashMap<>();
        map.put("courseId",courseId);
        map.put("isCollection",isCollection);
        NetWorkUtils.getResultString(getApplicationContext(), InterfaceClass.COURSE_COLLECT, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    courseBean.setIsCollection(isCollection);
                    ToastUtil.show(getApplicationContext(),response);
                    if (Utils.getString(courseBean.getIsCollection()).equals("1")){//是否收藏（0未收藏1已收藏）
                        viewBinding.iconCollect.setImageResource(R.mipmap.collected_course);
                    }else {
                        viewBinding.iconCollect.setImageResource(R.mipmap.collect_course);
                    }
                }
            }
        });
    }

    private void showUserIdView(){
        SpeedVideoPlayer currentPlayer = (SpeedVideoPlayer) viewBinding.detailPlayer.getCurrentPlayer();
        ViewGroup bottomViewGroup = (ViewGroup) currentPlayer.getBottomContainer().getParent();
        TextView _userIdTv = new TextView(CourseDetailsActivity.this);
        _userIdTv.setText(Utils.getString(MyApplication.getInstance().getAppUserEntity().getId()));
        _userIdTv.setTextSize(14);
        _userIdTv.setTextColor(getResources().getColor(R.color.color_B3ffffff));
        _userIdTv.setShadowLayer(1,3,3,getResources().getColor(R.color.color_99));
        bottomViewGroup.addView(_userIdTv);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _userIdTv.getLayoutParams();
        params.width =RelativeLayout.LayoutParams.WRAP_CONTENT;
        params.height =RelativeLayout.LayoutParams.WRAP_CONTENT;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = (int) getResources().getDimension(R.dimen.px200);
        _userIdTv.setLayoutParams(params);
    }

    private TextView showSpeedView(){
        SpeedVideoPlayer currentPlayer = (SpeedVideoPlayer) viewBinding.detailPlayer.getCurrentPlayer();
        LinearLayout bottomViewGroup = (LinearLayout) currentPlayer.getBottomContainer();
        TextView _btnSpeed =new TextView(CourseDetailsActivity.this);
        if (bottomViewGroup!=null){
            switch (currentSpeedIndex){
                case 0:
                    _btnSpeed.setText(getString(R.string.speed_1));
                    break;
                case 1:
                    _btnSpeed.setText(getString(R.string.speed_125));
                    break;
                case 2:
                    _btnSpeed.setText(getString(R.string.speed_150));
                    break;
                default:
                    _btnSpeed.setText(getString(R.string.speed));
                    break;
            }
            _btnSpeed.setGravity(Gravity.CENTER);
            _btnSpeed.setTextSize(12);
            _btnSpeed.setTextColor(Color.WHITE);
            bottomViewGroup.addView(_btnSpeed,bottomViewGroup.getChildCount()-1);
            _btnSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSpeedPopWindow();
                }
            });

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _btnSpeed.getLayoutParams();
            params.height =bottomViewGroup.getLayoutParams().height;
            _btnSpeed.setPadding(0,0, (int) getResources().getDimension(R.dimen.px30),0);
            _btnSpeed.setLayoutParams(params);
        }

        return _btnSpeed;
    }

    private void showSpeedPopWindow(){
        Configuration mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        final boolean isHor =ori == mConfiguration.ORIENTATION_LANDSCAPE;
        videoSpeedPopWindow=new VideoSpeedPopWindow(CourseDetailsActivity.this, new OnClickCallBack() {

            @Override
            public void onClick(int position) {
                if (viewBinding.detailPlayer!=null){
                    currentSpeedIndex =position;
                    switch (position){
                        case 0:
                            viewBinding.detailPlayer.setSpeed(1);
                            if (isHor)
                                btnSpeedHor.setText(getString(R.string.speed_1));
                            else
                                btnSpeed.setText(getString(R.string.speed_1));
                            break;
                        case 1:
                            viewBinding.detailPlayer.setSpeed((float) 1.25);
                            if (isHor)
                                btnSpeedHor.setText(getString(R.string.speed_125));
                            else
                                btnSpeed.setText(getString(R.string.speed_125));
                            break;
                        case 2:
                            viewBinding.detailPlayer.setSpeed((float) 1.5);
                            if (isHor)
                                btnSpeedHor.setText(getString(R.string.speed_150));
                            else
                                btnSpeed.setText(getString(R.string.speed_150));
                            break;
                    }
                    videoSpeedPopWindow.dismiss();
                }
            }
        });
        int[] location = new int[2];
        // 获得位置
        if (isHor) {
            btnSpeedHor.getLocationOnScreen(location);
            if (!videoSpeedPopWindow.isShowing())
                videoSpeedPopWindow.showAtLocation(btnSpeedHor, Gravity.NO_GRAVITY,
                        (location[0] + btnSpeedHor.getWidth() / 2) - videoSpeedPopWindow.getPopWindowWidth() / 2,
                        location[1] - videoSpeedPopWindow.getPopWindowHeigth());
        } else{
            btnSpeed.getLocationOnScreen(location);
            if (!videoSpeedPopWindow.isShowing())
                videoSpeedPopWindow.showAtLocation(btnSpeed, Gravity.NO_GRAVITY,
                        (location[0] + btnSpeed.getWidth() / 2) - videoSpeedPopWindow.getPopWindowWidth() / 2,
                        location[1] - videoSpeedPopWindow.getPopWindowHeigth());
        }
    }

    /**
     * 开始计时
     */
    private void startTimer(){
        timer =new Timer();
        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                lookVideoTime++;
            }
        };
        timer.schedule(task,0,1000);
    }

    private void getStudyTimeLenth() {
        Map<String,String> map =new HashMap<>();
        map.put("account",MyApplication.getInstance().getAppUserEntity().getAccount());
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.STUDY_TIME, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    StudyTimeEntity studyTimeEntity = JSONObject.parseObject(response,StudyTimeEntity.class);
                    if (studyTimeEntity==null) return;
                    if (!Utils.isEmpty(studyTimeEntity.getTodayStudy())){
                        isStudyMore6Hours =Integer.parseInt(studyTimeEntity.getTodayStudy())>=6*60*60;
                    }
                }
            }
        });
    }
}
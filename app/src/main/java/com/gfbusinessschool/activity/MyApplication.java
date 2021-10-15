package com.gfbusinessschool.activity;

import android.app.Application;
import android.content.SharedPreferences;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.ResultCallBack;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.SystemBasicBean;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.concurrent.TimeUnit;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyApplication extends Application {
    private AppUserEntity mAppUserEntity;
    private static final String KEY_USERENTITY = "商学院用户信息";
    private static final String SHAREPREFERENCE_NAME_ISFIRSTSTART = "app启动信息";
    private static final String KEY_ISFIRSTSTART ="第一次启动";
    private static final String KEY_ISFIRST_AGREEPERMISSION ="第一次同意隐私权限";
    private static MyApplication app;
    public int screenWidth;//手机屏幕宽度
    public int screenHeight;//手机屏幕高度
    private SystemBasicBean systemBasicBean;
    /**
     * 是否显示读书汇分享
     */
    private boolean isShowReadCollectionShare;
    /**
     * 是否显示冠军分享
     */
    private boolean isShowChamPionShare;
    /**
     * 是否显示问卷调研
     */
    private boolean isShowResearch;
    /**
     * 是否已经启动浮窗service
     */
    private boolean isStartAudioService;
    /**
     * 是否正在显示登录界面
     */
    private boolean isShowingLoginActivity=false;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initOkhttpUtils();
        initARouter();
        //获取系统常量
        getSystemBasicBean(new ResultCallBack() {
            @Override
            public void onResult(SystemBasicBean bean) {
                if (bean != null) systemBasicBean = bean;
            }
        });

        initGsyVideoPlayer();
        initJGData();
    }

    /**
     * 初始化极光数据
     */
    private void initJGData(){
        //极光统计分析
        JAnalyticsInterface.setDebugMode(Utils.isTest);
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.initCrashHandler(this);
    }

    public static MyApplication getInstance() {
        return app;
    }

    private void initARouter() {
        if (Utils.isTest) {
            ARouter.openLog();     // Print log
            ARouter.openDebug();
        }
        ARouter.init(MyApplication.this);
    }

    private void initOkhttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(5*60, TimeUnit.MINUTES)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public AppUserEntity getAppUserEntity() {
        if (mAppUserEntity == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(KEY_USERENTITY, 0);
            String appUserEntty = sharedPreferences.getString("AppUserEntity", "");
            if (!Utils.isEmpty(appUserEntty)) {
                mAppUserEntity = JSONObject.parseObject(appUserEntty, AppUserEntity.class);
                if (mAppUserEntity != null) Utils.log(Utils.TAG_LOGIN, mAppUserEntity.toString());
            }
        }
        return mAppUserEntity;
    }

    public void saveUserEntity(AppUserEntity appUserEntity) {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_USERENTITY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (appUserEntity == null)
            editor.clear();
        else
            editor.putString("AppUserEntity", JSONObject.toJSON(appUserEntity).toString());
        editor.commit();
        mAppUserEntity = appUserEntity;
        if (mAppUserEntity != null) Utils.log(Utils.TAG_LOGIN, JSONObject.toJSONString(appUserEntity));
    }

    public void getSystemBasicBean(ResultCallBack resultCallBack) {
        if (systemBasicBean == null) {
            //获取系统常量
            NetWorkUtils.getSystemBasicBean(getApplicationContext(), resultCallBack);
        } else {
            if (resultCallBack != null) resultCallBack.onResult(systemBasicBean);
        }
    }

    private void initGsyVideoPlayer() {
        //EXOPlayer内核，支持格式更多
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        //系统内核模式
        PlayerFactory.setPlayManager(SystemPlayerManager.class);
        //ijk内核，默认模式
        PlayerFactory.setPlayManager(IjkPlayerManager.class);
        //exo缓存模式，支持m3u8，只支持exo
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager.class);
        //切换渲染模式
        //默认显示比例 GSYVideoType.SCREEN_TYPE_DEFAULT = 0;
        // 16:9GSYVideoType.SCREEN_TYPE_16_9 = 1;
        // 4:3 GSYVideoType.SCREEN_TYPE_4_3 = 2;
        //全屏裁减显示，为了显示正常 CoverImageView 建议使用FrameLayout作为父布局 GSYVideoType.SCREEN_TYPE_FULL = 4;
        //全屏拉伸显示，使用这个属性时，surface_container建议使用FrameLayout GSYVideoType.SCREEN_MATCH_FULL = -4;
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);
        //ijk关闭log
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
    }

    /**
     * 第一次登录状态保存
     */
    public void saveFirstLoginState(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREPREFERENCE_NAME_ISFIRSTSTART, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISFIRSTSTART,false);
        editor.commit();
    }

    public boolean isFirstAgreePermission(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREPREFERENCE_NAME_ISFIRSTSTART, 0);
        return sharedPreferences.getBoolean(KEY_ISFIRST_AGREEPERMISSION, true);
    }

    /**
     * 第一次登录状态保存
     */
    public void saveFirstAgreePermissionState(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREPREFERENCE_NAME_ISFIRSTSTART, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISFIRST_AGREEPERMISSION,false);
        editor.commit();
    }

    public boolean isFirstStart(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREPREFERENCE_NAME_ISFIRSTSTART, 0);
        return sharedPreferences.getBoolean(KEY_ISFIRSTSTART, true);
    }

    public boolean isShowReadCollectionShare() {
        return isShowReadCollectionShare;
    }

    public void setShowReadCollectionShare(boolean showReadCollectionShare) {
        isShowReadCollectionShare = showReadCollectionShare;
    }

    public boolean isShowChamPionShare() {
        return isShowChamPionShare;
    }

    public void setShowChamPionShare(boolean showChamPionShare) {
        isShowChamPionShare = showChamPionShare;
    }

    public boolean isShowResearch() {
        return isShowResearch;
    }

    public void setShowResearch(boolean showResearch) {
        isShowResearch = showResearch;
    }

    public boolean isStartAudioService() {
        return isStartAudioService;
    }

    public void setIsStartAudioService(boolean isStartAudioService) {
        this.isStartAudioService = isStartAudioService;
    }

    public boolean isShowingLoginActivity() {
        return isShowingLoginActivity;
    }

    public void setShowingLoginActivity(boolean showingLoginActivity) {
        isShowingLoginActivity = showingLoginActivity;
    }
}

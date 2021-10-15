package com.gfbusinessschool.utils;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.ResultCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.MyApplication;
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.bean.BaseData;
import com.gfbusinessschool.bean.BaseDataArrayBean;
import com.gfbusinessschool.bean.BaseDataStringBean;
import com.gfbusinessschool.bean.BaseListData;
import com.gfbusinessschool.bean.PositionEntity;
import com.gfbusinessschool.bean.SystemBasicBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class NetWorkUtils {
    private static final String RESULT_NETWORK_OK ="200";
    private static final String RESULT_TOKENP_ERROR ="5000";//token失效

    public static void getRequest(final Context context, String url, Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity = MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("token",token)
                .params(map)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseData bean = JSONObject.parseObject(response, BaseData.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                if (bean.getData()!=null)
                                    callback.onResponse(bean.getDataCode(),bean.getData().toString());
                                else
                                    callback.onResponse(bean.getDataCode(),"");
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     *
     * @param context
     * @param url
     * @param map
     * @param callback
     */
    public static void postRequest(final Context context, final String url, final Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.post()
                .url(url)
                .params(map)
                .addHeader("token",token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseData bean =JSONObject.parseObject(response, BaseData.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                if (bean.getData()!=null)
                                    callback.onResponse(bean.getDataCode(),bean.getData().toString());
                                else
                                    callback.onResponse(bean.getDataCode(),"");
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     * 请求参数是json
     * @param context
     * @param url
     * @param json
     * @param callback
     */
    public static void postJson(final Context context, final String url, String json, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.postString()
                .url(url)
                .addHeader("token",token)
                .mediaType(MediaType.parse("application/json; charset=utf-8;"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" json="+json+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" json="+json+"\n response="+response);
                            BaseData bean =JSONObject.parseObject(response, BaseData.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                if (bean.getData()!=null)
                                    callback.onResponse(bean.getDataCode(),bean.getData().toString());
                                else
                                    callback.onResponse(bean.getDataCode(),"");
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     * 返回数据类型为数组{
     *   "code": "10000",
     *   "msg": "success",
     *   "data": [
     *     {
     *       }
     *   ]
     * }
     * @param context
     * @param url
     * @param map
     * @param callback
     */
    public static void getResultArray(final Context context, final String url, final Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.get()
                .url(url)
                .params(map)
                .addHeader("token",token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseDataArrayBean bean =JSONObject.parseObject(response, BaseDataArrayBean.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                callback.onResponse(bean.getDataCode(),bean.getData()==null?"":bean.getData().toString());
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     * 返回数据类型为数组{
     *   "code": "10000",
     *   "msg": "success",
     *   "data": {
     *       "records":[]
     *   }
     *
     * }
     */
    public static void getResultList(final Context context, final String url, final Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.get()
                .url(url)
                .params(map)
                .addHeader("token",token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseListData bean =JSONObject.parseObject(response, BaseListData.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                if (bean!=null && bean.getData()!=null && bean.getData().getOrders()!=null)
                                    callback.onResponse(bean.getDataCode(),bean.getData().getRecords().toString());
                                else
                                    callback.onResponse(bean.getDataCode(),"[]");
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     * 返回结果是string
     * @param context
     * @param url
     * @param json
     * @param callback
     */
    public static void getResultString(final Context context, final String url, final Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.get()
                .url(url)
                .params(map)
                .addHeader("token",token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_service_msg));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseDataStringBean bean =JSONObject.parseObject(response, BaseDataStringBean.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                callback.onResponse(bean.getDataCode(),Utils.isEmpty(bean.getData())?"":bean.getData());
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    /**
     * 返回结果是string
     * @param context
     * @param url
     * @param map
     * @param callback
     */
    public static void postResultString(final Context context, final String url, final Map<String,String> map, final NetWorkCallback callback){
        AppUserEntity appUserEntity =MyApplication.getInstance().getAppUserEntity();
        String token ="";
        if (appUserEntity!=null && !Utils.isEmpty(appUserEntity.getToken())){
            token =appUserEntity.getToken();
        }
        OkHttpUtils.post()
                .url(url)
                .addHeader("token",token)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.log(Utils.TAG_ERROR,"url ="+url +" map"+map.toString()+"\n Exception="+e.getMessage());
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Utils.log(Utils.TAG_NETWORK,"url ="+url +" map"+map.toString()+"\n response="+response);
                            BaseDataStringBean bean =JSONObject.parseObject(response, BaseDataStringBean.class);
                            if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                                reLogin();
                            }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                                callback.onResponse(bean.getDataCode(),Utils.isEmpty(bean.getData())?"":bean.getData());
                            }else {
                                callback.onRequestError();
                                if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                            }
                        } catch (Exception e){
                            if (Utils.isTest)
                                e.printStackTrace();
                            callback.onRequestError();
                            if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                        }
                    }
                });
    }

    public static void upLoadImg(Context context, File file, final NetWorkCallback callback){
        if (file==null || !file.exists()) return;
        Utils.log(Utils.TAG_NETWORK,"图片或者文件地址："+file.getPath());
        OkHttpUtils.post().url(InterfaceClass.UPLOAD_IMG_FILE)
                .addFile("file",file.getName(),file)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Utils.log(Utils.TAG_UPLOAD_IMG_FILE,"Exception "+e.getMessage());
                if (context!=null)ToastUtil.showToast(context,"文件上传失败"+e.getMessage());
                if (callback!=null) callback.onRequestError();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                if (callback!=null) callback.inProgress(progress);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Utils.log(Utils.TAG_NETWORK,"url ="+InterfaceClass.UPLOAD_IMG_FILE +" response="+response);
                    BaseData bean =JSONObject.parseObject(response, BaseData.class);
                    if (Utils.getString(bean.getCode()).equals(RESULT_TOKENP_ERROR)){
                        reLogin();
                    }else if (Utils.getString(bean.getCode()).equals(RESULT_NETWORK_OK)) {
                        if (bean.getData()!=null)
                            callback.onResponse(bean.getDataCode(),bean.getData().toString());
                    }else {
                        callback.onRequestError();
                        if (context!=null)ToastUtil.showToast(context,bean.getMsg());
                    }
                } catch (Exception e) {
                    if (Utils.isTest) e.getMessage();
                    if (context!=null)ToastUtil.showToast(context,context.getString(R.string.error_json_msg));
                }
            }
        });
    }

    /**
     * 获取系统常量
     */
    public static void getSystemBasicBean(Context context,ResultCallBack resultCallBack){
        getRequest(context, InterfaceClass.SYSTEM_CONSTANT, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    if (!Utils.isEmpty(response) && resultCallBack!=null)
                        resultCallBack.onResult(JSONObject.parseObject(response, SystemBasicBean.class));
                }
            }
        });
    }

    public static void getPositionList(Context context,Map<String,String> map, ResultCallBack resultCallBack){
        getResultArray(context, InterfaceClass.POSITION_LIST, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    if (!Utils.isEmpty(response) && resultCallBack!=null)
                        resultCallBack.onResult(JSONObject.parseArray(response, PositionEntity.class));
                }
            }
        });
    }

    /**
     * 课程视频观看时长
     * @param context
     * @param courseId
     * @param studyLength 观看时长，秒
     * @param studyTime 学习时长，秒
     * @param type 1视频 2音频
     */
    public static void postCourseLookTime(Context context,String courseId,int studyLength,long studyTime,int type){
        //学习进度、时长过滤
        if (studyLength==0) return;
        if (studyTime>5*60*60) return;
        Map<String,String> map=new HashMap<>();
        map.put("courseId",courseId);
        map.put("studyLength",studyLength+"");
        map.put("studyTimeLength",studyTime+"");
        if (type==1)
            map.put("type",type+"");
        else
            map.put("type",type+"");
        getResultString(context, InterfaceClass.COURSE_ADD_STUDYRECORD, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {

            }
        });
    }

    /**
     * 添加用户学习地图课程记录
     * @param context
     * @param courseId 课程id
     * @param specialSubjectId 专题id
     * @param studyLength 当前学习的位置
     * @param playPercent 学习进度
     */
    public static void postStudyMapCourseLookTime(Context context,String courseId,String specialSubjectId,int studyLength,int playPercent){
        Map<String,String> map=new HashMap<>();
        map.put("courseId",courseId);
        map.put("specialSubjectId",specialSubjectId);
        map.put("studyLength",studyLength+"");
        if (playPercent>95) map.put("playPercent","1.0");
        else map.put("playPercent",new BigDecimal(playPercent*1.0/100).setScale(2, RoundingMode.HALF_UP).doubleValue()+"");
        getResultString(context, InterfaceClass.STUDYMAP_ADDSTUDYINFO, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {

            }
        });
    }

    private static void reLogin(){
        if (MyApplication.getInstance().isShowingLoginActivity()) return;
        ActivityCollector.finishAllActivity();
        MyApplication.getInstance().saveUserEntity(null);
        ARouter.getInstance().build(ARouterPath.LoginActivity).navigation();
    }
}

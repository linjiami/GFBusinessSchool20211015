package com.gfbusinessschool.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.RequestPermissionCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ChampionShareCoverEntity;
import com.gfbusinessschool.bean.ChampionSharingEntity;
import com.gfbusinessschool.bean.CourseClassifyEntity;
import com.gfbusinessschool.bean.ShareEntity;
import com.gfbusinessschool.databinding.ActivityReleaseShareBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.dialog.PictureLookDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 冠军分享
 */
@Route(path = ARouterPath.ACTIVITY_URL_ReleaseShare)
public class ReleaseShareActivity extends BaseActivity<ActivityReleaseShareBinding> implements View.OnClickListener {
    private ShareEntity shareEntity =new ShareEntity();
    private List<ChampionSharingEntity> listClassify;
    private List<ChampionShareCoverEntity> listCover;
    @Override
    protected void initView() {
        super.initView();
        viewBinding.releaseShareBottom.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.releaseVideo.setOnClickListener(this);
        viewBinding.releaseShareBottom.releaseShareBtn.setOnClickListener(this);
        viewBinding.classifyShare.setOnClickListener(this);
        viewBinding.coverMode1.setOnClickListener(this);
        viewBinding.coverMode2.setOnClickListener(this);
        viewBinding.coverMode3.setOnClickListener(this);
        viewBinding.cirCoverMode1.setOnClickListener(this);
        viewBinding.cirCoverMode2.setOnClickListener(this);
        viewBinding.cirCoverMode3.setOnClickListener(this);

        viewBinding.titleBar.setTitle(getString(R.string.releaseshare));
        double width =MyApplication.getInstance().screenWidth*171.0/750;
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) viewBinding.coverMode1.getLayoutParams();
        params1.width = (int) width;
        params1.height = (int) (width*9/16);
        viewBinding.coverMode1.setLayoutParams(params1);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) viewBinding.coverMode2.getLayoutParams();
        params2.width = (int) width;
        params2.height = (int) (width*9/16);
        viewBinding.coverMode2.setLayoutParams(params2);

        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) viewBinding.coverMode3.getLayoutParams();
        params3.width = (int) width;
        params3.height = (int) (width*9/16);
        viewBinding.coverMode3.setLayoutParams(params3);
    }

    @Override
    protected void initData() {
        super.initData();
        getChampionSharingClassify(false);
        getShareCoverModel();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.releaseVideo:
               String[] arrPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
               if (isAllowedPermission(arrPermission)) {
                   loadAlbumVideo();
               } else {
                   requestPermissions(arrPermission, new RequestPermissionCallBack() {
                       @Override
                       public void granted() {
                           loadAlbumVideo();
                       }

                       @Override
                       public void refused() {
                           showPermissionAlert(ReleaseShareActivity.this, getString(R.string.album_permission_alert));
                       }
                   });
               }
               break;
           case R.id.releaseShareBtn:
               String title =viewBinding.titleShare.getText().toString().trim();
               if (Utils.isEmpty(title)){
                   ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_title));
                   return;
               }
               shareEntity.setTitle(title);
               if (Utils.isEmpty(shareEntity.getFirstTagId()) || Utils.isEmpty(shareEntity.getSecondTagId())){
                   ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_classify));
                   return;
               }
               String content =viewBinding.contentShare.getText().toString().trim();
               if (Utils.isEmpty(content)){
                   ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_content));
                   return;
               }
               shareEntity.setContent(content);
               if (Utils.isEmpty(shareEntity.getCoverImgUrl())){
                   ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_cover));
                   return;
               }
               if (Utils.isEmpty(shareEntity.getVideoUrl())){
                   ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_video));
                   return;
               }
               releaseShare();
               break;
           case R.id.classifyShare:
               if (listClassify ==null || listClassify.size()==0)
                   getChampionSharingClassify(true);
               else
                   onClickClassifyBtn();
               break;
           case R.id.coverMode1:
               if (listCover!=null && listCover.size()>0){
                   PictureLookDialog dailog =new PictureLookDialog(ReleaseShareActivity.this,listCover.get(0).getImgUrl());
                   dailog.show();
               }
               break;
           case R.id.cirCoverMode1:
               viewBinding.cirCoverMode1.setImageResource(R.mipmap.selected_share);
               viewBinding.cirCoverMode2.setImageResource(R.mipmap.select_share);
               viewBinding.cirCoverMode3.setImageResource(R.mipmap.select_share);
               if (listCover!=null && listCover.size()>=1)
                   shareEntity.setCoverImgUrl(listCover.get(0).getImgUrl());
               break;
           case R.id.coverMode2:
               if (listCover!=null && listCover.size()>1){
                   PictureLookDialog dailog =new PictureLookDialog(ReleaseShareActivity.this,listCover.get(1).getImgUrl());
                   dailog.show();
               }
               break;
           case R.id.cirCoverMode2:
               viewBinding.cirCoverMode1.setImageResource(R.mipmap.select_share);
               viewBinding.cirCoverMode2.setImageResource(R.mipmap.selected_share);
               viewBinding.cirCoverMode3.setImageResource(R.mipmap.select_share);
               if (listCover!=null && listCover.size()>=2)
                   shareEntity.setCoverImgUrl(listCover.get(1).getImgUrl());
               break;
           case R.id.coverMode3:
               if (listCover!=null && listCover.size()>2){
                   PictureLookDialog dailog =new PictureLookDialog(ReleaseShareActivity.this,listCover.get(2).getImgUrl());
                   dailog.show();
               }
               break;
           case R.id.cirCoverMode3:
               viewBinding.cirCoverMode1.setImageResource(R.mipmap.select_share);
               viewBinding.cirCoverMode2.setImageResource(R.mipmap.select_share);
               viewBinding.cirCoverMode3.setImageResource(R.mipmap.selected_share);
               if (listCover!=null && listCover.size()>=3)
                   shareEntity.setCoverImgUrl(listCover.get(2).getImgUrl());
               break;
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM_VIDEO && resultCode == RESULT_OK && data != null) {
            showLoadingDialog(false);
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String  videoPath = cursor.getString(columnIndex);
            cursor.close();
            Utils.log(Utils.TAG_ORTHER,"video length="+(new File(videoPath)).length());
            if ((new File(videoPath)).length()>800*1024*1024){
                ToastUtil.show(getApplicationContext(),getString(R.string.video_file_limit));
                dismissLoadingDialog();
                return;
            }
            GlideLoadUtils.loadVideoCover(getApplicationContext(),videoPath,viewBinding.releaseVideo);
            getUploadToken(videoPath);
        }
    }

    /**
     * 获取七牛上传视频token
     */
    private void getUploadToken(String videoPath) {
        NetWorkUtils.getResultString(getApplicationContext(), InterfaceClass.UPLOAD_TOKEN, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onRequestError() {
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    uploadQiNiu(videoPath,response);
                }else {
                    dismissLoadingDialog();
                }
            }
        });
    }

    /**
     * 上传视频到七牛
     * @param videoPath 视频地址
     * @param token
     */
    private void uploadQiNiu(String videoPath, String token) {
        UploadManager uploadManager = new UploadManager();
        String nameVideo =MyApplication.getInstance().getAppUserEntity().getAccount()+System.currentTimeMillis();
        uploadManager.put(videoPath, nameVideo, token, (key, info, response) -> {
            //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
            if(info.isOK()) {
                String url ="http://test.imcfc.com/"+nameVideo;
                shareEntity.setVideoUrl(url);
                Utils.log(Utils.TAG_UPLOAD_QINIU, "Upload Success response="+response.toString()+" url="+url);
                dismissLoadingDialog();
            } else {
                ToastUtil.show(getApplicationContext(),getString(R.string.failed_upload));
                dismissLoadingDialog();
                viewBinding.releaseVideo.setImageResource(R.mipmap.add_share);
                Utils.log(Utils.TAG_UPLOAD_QINIU, "Upload Fail");
            }
        }, new UploadOptions(null, null, false,
                (key, percent) -> {
                    loadingDialog.setAlertMsg("已上传"+(Math.round(percent*100))*100/100+"%");
                }, null));
    }

    /**
     * 冠军分享分类
     */
    private void getChampionSharingClassify(boolean isClickClassifyBtn){
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.CHAMPIONSHARING_CLASSIFY, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    listClassify = JSONArray.parseArray(response,ChampionSharingEntity.class);
                    if (listClassify ==null || listClassify.size()==0) return;
                    if (isClickClassifyBtn) onClickClassifyBtn();
                }
            }
        });

    }

    private void onClickClassifyBtn() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String province =listClassify.get(options1).getTagName();
                String city="";
                if (listClassify.get(options1).getList()!=null && listClassify.get(options1).getList().size()!=0)
                    city =listClassify.get(options1).getList().get(option2).getTagName();
                shareEntity.setFirstTagId(listClassify.get(options1).getId());
                if (listClassify.get(options1).getList()!=null && listClassify.get(options1).getList().size()!=0)
                    shareEntity.setSecondTagId(listClassify.get(options1).getList().get(option2).getId());
                viewBinding.classifyShare.setText(String.format(getString(R.string.place_hengxian),province,city));
            }
        }).build();
        List<List<String>> list =new ArrayList<>();
        List<String> options1Items =new ArrayList<>();
        for (int i=0;i<listClassify.size();i++){
            String provinceName =listClassify.get(i).getTagName();
            options1Items.add(provinceName);
            List<String> listChild =new ArrayList<>();
            List<CourseClassifyEntity> listCity =listClassify.get(i).getList();
            if (listCity!=null && listCity.size()!=0){
                for (int j=0;j<listCity.size();j++){
                    String cityName =listCity.get(j).getTagName();
                    listChild.add(cityName);
                }
            }
            list.add(listChild);
        }
        pvOptions.setPicker(options1Items, list);
        pvOptions.show();
    }

    private void getShareCoverModel(){
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.CHAMPIONSHARING_COVER, new HashMap<>(), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200") && !Utils.isEmpty(response)){
                    listCover = JSONArray.parseArray(response, ChampionShareCoverEntity.class);
                    if (listCover ==null || listCover.size()==0) return;
                    for (int i=0;i<listCover.size();i++){
                        if (i==0){
                            shareEntity.setCoverImgUrl(listCover.get(0).getImgUrl());
                            GlideLoadUtils.load(getApplicationContext(),listCover.get(0).getImgUrl(),viewBinding.coverMode1,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                        }else if (i==1){
                            GlideLoadUtils.load(getApplicationContext(),listCover.get(1).getImgUrl(),viewBinding.coverMode2,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                        }else if (i==2){
                            GlideLoadUtils.load(getApplicationContext(),listCover.get(2).getImgUrl(),viewBinding.coverMode3,GlideLoadUtils.TYPE_PLACE_HOLDER_LIST);
                        }
                    }
                }
            }
        });
    }

    private void releaseShare(){
        NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.CHAMPIONSHARING_RELEASE, JSONObject.toJSONString(shareEntity), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    MyAlertDialog myAlertDialog = new MyAlertDialog(ReleaseShareActivity.this, new MyDialogCallback() {
                        @Override
                        public void onPositiveClick(MyAlertDialog dialog) {
                            super.onPositiveClick(dialog);
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    myAlertDialog.setMessage(getString(R.string.success_release_share));
                    myAlertDialog.setNegativeButtonGone();
                    myAlertDialog.setCanceledOnTouchOutside(false);
                    myAlertDialog.show();
                }
            }
        });
    }
}
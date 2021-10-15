package com.gfbusinessschool.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import androidx.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.InterfaceUtils.RequestPermissionCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ReleaseReadCollectionEntity;
import com.gfbusinessschool.databinding.ActivityReleaseReadcollectionBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.dialog.MyListPopWindow;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.FileTypeUtils;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 读书汇分享（发布）
 */
@Route(path = ARouterPath.ReleaseReadCollectionActivity)
public class ReleaseReadCollectionActivity extends BaseActivity<ActivityReleaseReadcollectionBinding> implements View.OnClickListener {
    private int positionType;
    private String pathCamera;
    private ReleaseReadCollectionEntity entity =new ReleaseReadCollectionEntity();

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.releaseshare));
        viewBinding.typeVideo.setOnClickListener(this);
        viewBinding.typeFile.setOnClickListener(this);
        viewBinding.typeImg.setOnClickListener(this);
        viewBinding.releaseVideo.setOnClickListener(this);
        viewBinding.releaseShareBottom.releaseShareBtn.setOnClickListener(this);
        viewBinding.releaseShareBottom.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.typeImg:
                setTypeView(2);
                break;
            case R.id.typeFile:
                setTypeView(1);
                break;
            case R.id.typeVideo:
                setTypeView(0);
                break;
            case R.id.releaseShareBtn://发布分享
                String titleShare =viewBinding.titleShare.getText().toString();
                if (Utils.isEmpty(titleShare)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_title));
                    return;
                }
                entity.setTitle(titleShare);
                String content =viewBinding.contentShare.getText().toString().trim();
                if (Utils.isEmpty(content)){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_content));
                    return;
                }
                entity.setContent(content);
                if (Utils.isEmpty(entity.getFileUrl())){
                    ToastUtil.show(getApplicationContext(),getString(R.string.alert_share_readcollection));
                    return;
                }
                releaseShare();
                break;
            case R.id.releaseVideo://上传
                String[] arrPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                if (isAllowedPermission(arrPermission)) {
                    loadPhoneContent();
                } else {
                    requestPermissions(arrPermission, new RequestPermissionCallBack() {
                        @Override
                        public void granted() {
                            loadPhoneContent();
                        }

                        @Override
                        public void refused() {
                            showPermissionAlert(ReleaseReadCollectionActivity.this, getString(R.string.album_permission_alert));
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            //加载拍照后的图片
            Utils.startHeadUCrop(this, Uri.fromFile(new File(pathCamera)), System.currentTimeMillis() + ".jpg");
        }else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {//裁剪图片
            showLoadingDialog(false);
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(this).load(resultUri).into(viewBinding.releaseVideo);
            getUploadToken(resultUri.getPath());
        } else if ((requestCode==REQUEST_CODE_PHONE_FILE || requestCode==REQUEST_CODE_ALBUM || requestCode==REQUEST_CODE_ALBUM_VIDEO)
                && resultCode == RESULT_OK && data != null){
            String path =Utils.getPath(getApplicationContext(),data.getData());
            Utils.log(Utils.TAG_ModifyHeadImg, "path= " + path);
            if (Utils.isEmpty(path)) return;
            if (FileTypeUtils.isWordPDFFileType(path) || FileTypeUtils.isPPTFileType(path)){//文件
                setTypeView(1);
                viewBinding.releaseVideo.setImageResource(R.mipmap.file_readcollection);
                Utils.log(Utils.TAG_ORTHER,path);
                if (Utils.isEmpty(path)) return;
                showLoadingDialog(false);
                getUploadToken(path);
            }else if (FileTypeUtils.isVideoFileType(path)){//视频
                setTypeView(0);
                showLoadingDialog(false);
                if ((new File(path)).length()>800*1024*1024){
                    ToastUtil.show(getApplicationContext(),getString(R.string.video_file_limit));
                    dismissLoadingDialog();
                }else {
                    GlideLoadUtils.loadVideoCover(getApplicationContext(),path,viewBinding.releaseVideo);
                    getUploadToken(path);
                }
            }else if (FileTypeUtils.isImageFileType(path)) {//图片
                setTypeView(2);
                Utils.startHeadUCrop(this, Uri.fromFile(new File(path)), System.currentTimeMillis() + ".jpg");
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void setTypeView(int position){
        positionType =position;
        Drawable drawable = getResources().getDrawable(
                R.mipmap.check_login);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        viewBinding.typeVideo.setCompoundDrawables(drawable,null,null,null);
        viewBinding.typeFile.setCompoundDrawables(drawable,null,null,null);
        viewBinding.typeImg.setCompoundDrawables(drawable,null,null,null);
        Drawable drawableSelected = getResources().getDrawable(
                R.mipmap.checked_login);
        // / 这一步必须要做,否则不会显示.
        drawableSelected.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        if (position==0)
            viewBinding.typeVideo.setCompoundDrawables(drawableSelected,null,null,null);
        else if (position==1)
            viewBinding.typeFile.setCompoundDrawables(drawableSelected,null,null,null);
        else
            viewBinding.typeImg.setCompoundDrawables(drawableSelected,null,null,null);
    }

    private void loadPhoneContent(){
        if (positionType==0){//视频
            loadAlbumVideo();
        }else if (positionType==1){//文件
            loadPhoneFile();
        }else {//图片
            List<String> list = new ArrayList();
            list.add(getResources().getString(R.string.album_path));
            list.add(getResources().getString(R.string.carema_path));
            MyListPopWindow listPopWindow = new MyListPopWindow(this, list);
            listPopWindow.setmOnClickCallBack(new OnClickCallBack() {
                @Override
                public void onClick(int position) {
                    if (position == 0) {
                        String[] arrPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                        if (isAllowedPermission(arrPermission)) {
                            loadAlbumIcon();
                        } else {
                            requestPermissions(arrPermission, new RequestPermissionCallBack() {
                                @Override
                                public void granted() {
                                    loadAlbumIcon();
                                }

                                @Override
                                public void refused() {
                                    showPermissionAlert(ReleaseReadCollectionActivity.this, getString(R.string.album_permission_alert));
                                }
                            });
                        }
                    } else {
                        String[] arrPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                        if (isAllowedPermission(arrPermission)) {
                            pathCamera =loadCameraIcon();
                        } else {
                            requestPermissions(arrPermission, new RequestPermissionCallBack() {
                                @Override
                                public void granted() {
                                    pathCamera =loadCameraIcon();
                                }

                                @Override
                                public void refused() {
                                    showPermissionAlert(ReleaseReadCollectionActivity.this, getString(R.string.carema_permission_alert));
                                }
                            });
                        }
                    }
                    listPopWindow.dismiss();
                }
            });
            listPopWindow.showPopWindow(viewBinding.releaseVideo);
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
        if (positionType==0) {
            nameVideo = nameVideo + ".mp4";
        } else if (positionType==2) {
            nameVideo = nameVideo + ".jpg";
        } else {
             if (Utils.isEmpty(Utils.getExtensionName(videoPath)))
                 nameVideo = nameVideo + ".doc";
             else
                 nameVideo = nameVideo + "."+ Utils.getExtensionName(videoPath);
        }
        String finalNameVideo = nameVideo;
        uploadManager.put(videoPath, nameVideo, token, (key, info, response) -> {
            //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
            if(info.isOK()) {
                String url ="http://test.imcfc.com/"+ finalNameVideo;
                entity.setFileUrl(url);
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

    private void releaseShare(){
        NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.UPLOAD_READCOLLECTION, JSONObject.toJSONString(entity), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    MyAlertDialog myAlertDialog = new MyAlertDialog(ReleaseReadCollectionActivity.this, new MyDialogCallback() {
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
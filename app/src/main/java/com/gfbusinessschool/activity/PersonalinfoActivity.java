package com.gfbusinessschool.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.gfbusinessschool.bean.AppUserEntity;
import com.gfbusinessschool.databinding.ActivityPersonalInfoBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.dialog.MyListPopWindow;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑资料
 */
@Route(path = ARouterPath.ACTIVITY_Personalinfo)
public class PersonalinfoActivity extends BaseActivity<ActivityPersonalInfoBinding> implements View.OnClickListener {
    private String pathCamera;
    private String nameImg;
    private AppUserEntity changeUserEntity;

    @Override
    protected void initView() {
        super.initView();
        Utils.setBackgroundSolid(getApplicationContext(),viewBinding.userBg);
        viewBinding.bottomLayoutPersonal.releaseShareBtn.setText(getString(R.string.save));
        viewBinding.bottomLayoutPersonal.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.changeUserHead.setOnClickListener(this);
        viewBinding.position.setOnClickListener(this);
        viewBinding.bottomLayoutPersonal.releaseShareBtn.setOnClickListener(this);
        viewBinding.titleBar.setTitle(getString(R.string.title_change_personalinfo));
        changeUserEntity =MyApplication.getInstance().getAppUserEntity();
        if (changeUserEntity != null) {
            GlideLoadUtils.load(getApplicationContext(), changeUserEntity.getHeadImgUrl(), viewBinding.userImg, GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
            viewBinding.nickName.setText(Utils.getString(changeUserEntity.getName()));
            viewBinding.sex.setText(Utils.getString(changeUserEntity.getSex()));
            viewBinding.address.setText(String.format(getString(R.string.place_hengxian),
                    Utils.getString(changeUserEntity.getProvince()),Utils.getString(changeUserEntity.getCity())));
            viewBinding.store.setText(Utils.getString(changeUserEntity.getStoreName()));
            viewBinding.position.setText(Utils.getString(changeUserEntity.getPositionName()));
            if (!Utils.isEmpty(changeUserEntity.getJianjie()))
                viewBinding.introduction.setText(Utils.getString(changeUserEntity.getJianjie()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeUserHead:
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
                                        showPermissionAlert(PersonalinfoActivity.this, getString(R.string.album_permission_alert));
                                    }
                                });
                            }
                        } else {
                            String[] arrPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                            if (isAllowedPermission(arrPermission)) {
                                pathCamera = loadCameraIcon();
                            } else {
                                requestPermissions(arrPermission, new RequestPermissionCallBack() {
                                    @Override
                                    public void granted() {
                                        pathCamera = loadCameraIcon();
                                    }

                                    @Override
                                    public void refused() {
                                        showPermissionAlert(PersonalinfoActivity.this, getString(R.string.carema_permission_alert));
                                    }
                                });
                            }
                        }
                        listPopWindow.dismiss();
                    }
                });
                listPopWindow.showPopWindow(viewBinding.changeUserHead);
                break;
            case R.id.releaseShareBtn:
                //防止传null，这里用map转json上传
                Map<String, String> map = new HashMap<>();
                if (!Utils.isEmpty(changeUserEntity.getHeadImgUrl()))
                    map.put("headImgUrl", changeUserEntity.getHeadImgUrl());
                String _introduction = viewBinding.introduction.getText().toString().trim();
                if (!Utils.isEmpty(_introduction)) map.put("jianjie", _introduction);
                NetWorkUtils.postJson(getApplicationContext(), InterfaceClass.UPDATE_USERINFO, JSONObject.toJSONString(map), new NetWorkCallback() {
                    @Override
                    public void onResponse(String code, String response) {
                        if (Utils.getString(code).equals("200")) {
                            MyAlertDialog myAlertDialog = new MyAlertDialog(PersonalinfoActivity.this, new MyDialogCallback() {
                                @Override
                                public void onPositiveClick(MyAlertDialog dialog) {
                                    super.onPositiveClick(dialog);
                                    finish();
                                }
                            });
                            myAlertDialog.setMessage("修改成功!");
                            myAlertDialog.setNegativeButtonGone();
                            myAlertDialog.setCanceledOnTouchOutside(false);
                            myAlertDialog.show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK && data != null) {
            //加载相册图片
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            c.close();
            nameImg = "相册头像" + System.currentTimeMillis() + ".jpg";
            Utils.startHeadUCropRectangle(this, Uri.fromFile(new File(imagePath)), nameImg);
            Utils.log(Utils.TAG_ModifyHeadImg, "imagePath= " + imagePath);
            return;
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {//裁剪图片
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(this).load(resultUri).into(viewBinding.userImg);
            uploadHeaderIcon(nameImg);
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            //加载拍照后的图片
            nameImg = "拍照头像" + System.currentTimeMillis() + ".jpg";
            Utils.startHeadUCropRectangle(this, Uri.fromFile(new File(pathCamera)), nameImg);
        }
    }

    /**
     * 上传头像
     *
     * @param _nameImg
     */
    private void uploadHeaderIcon(String _nameImg) {
        String rootPath = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
        NetWorkUtils.upLoadImg(getApplicationContext(), new File(rootPath + "/" + _nameImg), new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object == null) return;
                    String url = object.getString("url");
                    if (Utils.isEmpty(url)) return;
                    if (changeUserEntity != null) changeUserEntity.setHeadImgUrl(url);
                }
            }
        });
    }
}
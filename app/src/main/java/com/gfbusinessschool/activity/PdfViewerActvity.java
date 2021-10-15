package com.gfbusinessschool.activity;

import android.os.Environment;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityPdfBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import java.io.File;
import okhttp3.Call;

/**
 * pdf查看器
 */
@Route(path = ARouterPath.PdfViewerActvity)
public class PdfViewerActvity extends BaseActivity<ActivityPdfBinding> {
    private final String pathDownloadPdf = Environment.getExternalStorageDirectory()+"/file";
    @Autowired
    String urlPdf;
    @Autowired
    String pdfTitle;
    @Autowired
    boolean isAudioCourseWare;//是否是音频课件（返回时需要重新唤醒音频界面）

    @Override
    protected void initView() {
        super.initView();
        if (Utils.isEmpty(urlPdf)){
            ToastUtil.show(getApplicationContext(),getString(R.string.null_pdfurl));
            return;
        }
        viewBinding.titleBar.setTitle(Utils.getString(pdfTitle));
        viewBinding.titleBar.getBackLayout().setOnClickListener(v -> {
            if (isAudioCourseWare)
                ARouter.getInstance().build(ARouterPath.AudioDetailsActivity).navigation();
            finish();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isAudioCourseWare)
                ARouter.getInstance().build(ARouterPath.AudioDetailsActivity).navigation();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initData() {
        super.initData();
        showLoadingDialog("加载中...");
        loadPdf();
    }

    private void loadPdf(){
        OkHttpUtils.get().url(urlPdf).tag(this).build().execute(new FileCallBack(pathDownloadPdf,pdfTitle+".pdf") {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();
                ToastUtil.show(getApplicationContext(),getString(R.string.error_loading));
            }

            @Override
            public void onResponse(File response, int id) {
                dismissLoadingDialog();
                if (response==null || !response.exists()){
                    ToastUtil.show(getApplicationContext(),getString(R.string.error_loading));
                    return;
                }
                viewBinding.pdfView.fromFile(response)
                        .defaultPage(1)//默认展示第一页
                        .enableSwipe(true)
                        .onPageChange(null)
                .load();//监听页面切换
            }
        });
    }
}
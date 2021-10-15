package com.gfbusinessschool.activity;

import android.os.Build;
import android.os.Environment;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityWebviewBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

/**
 * word文档查看
 */
@Route(path = ARouterPath.WordViewerActivity)
public class WordViewerActivity extends BaseActivity<ActivityWebviewBinding>{
    @Autowired
    String urlPdf;
    @Autowired
    String pdfTitle;
    @Autowired
    String pdfName;
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
        //支持js
        viewBinding.webViewNotice.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        viewBinding.webViewNotice.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webViewNotice.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        viewBinding.webViewNotice.getSettings().setJavaScriptEnabled(true);
        viewBinding.webViewNotice.getSettings().setBlockNetworkImage(false);
        viewBinding.webViewNotice.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webViewNotice.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        viewBinding.webViewNotice.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
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
        viewBinding.webViewNotice.loadUrl("http://view.officeapps.live.com/op/view.aspx?src="+urlPdf);
    }
}
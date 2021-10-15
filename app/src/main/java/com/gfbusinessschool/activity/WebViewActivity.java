package com.gfbusinessschool.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityNoticeCompanyDetailsBinding;
import com.gfbusinessschool.databinding.ActivityWebviewBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.Utils;

@Route(path = ARouterPath.ACTIVITY_URL_WebView)
public class WebViewActivity extends BaseActivity<ActivityWebviewBinding>{
    @Autowired
    String title;
    @Autowired
    String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        super.initView();
        if (!Utils.isEmpty(title))
            viewBinding.titleBar.setTitle(title);
        //支持js
        WebSettings settings = viewBinding.webViewNotice.getSettings();
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        settings.setBuiltInZoomControls(true);//设置是否支持缩放
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);// 页面支持缩放：
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptEnabled(true);  //支持js
        settings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        settings.setSupportZoom(true);  //支持缩放    webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();  //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        viewBinding.webViewNotice.requestFocusFromTouch(); //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。

        viewBinding.webViewNotice.loadUrl(url);
        Utils.log(Utils.TAG_NETWORK,"url="+url);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        viewBinding.webViewNotice.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Utils.isEmpty(title) && view!=null && view.getTitle() != null)
                    viewBinding.titleBar.setTitle(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
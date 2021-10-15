package com.gfbusinessschool.activity;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.CompanyNoticeEntity;
import com.gfbusinessschool.databinding.ActivityNoticeCompanyDetailsBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业公告详情
 */
@Route(path = ARouterPath.ACTIVITY_NoticeCompanyDetails)
public class NoticeCompanyDetailsActivity extends BaseActivity<ActivityNoticeCompanyDetailsBinding>{
    @Autowired
    String titleNotice;
    @Autowired
    String id;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.title_notice));
        WebSettings webSettings = viewBinding.webViewNotice.getSettings();
        webSettings.setTextZoom(100);
        webSettings.setJavaScriptEnabled(true); //-> 是否开启JS支持
        webSettings.setUseWideViewPort(true); //-> 缩放至屏幕大小
        webSettings.setLoadWithOverviewMode(true);// -> 缩放至屏幕大小
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        viewBinding.webViewNotice.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (Utils.isEmpty(id)) return;
        Map<String,String> map =new HashMap<>();
        map.put("id",id);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.COMPANY_NOTICE_DETAILS, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    CompanyNoticeEntity entity = JSONObject.parseObject(response,CompanyNoticeEntity.class);
                    if (entity==null) return;
                    viewBinding.titleNotice.setText(Utils.getString(entity.getTitle()));
                    viewBinding.timeNotice.setText(Utils.getString(entity.getCreateDate()));
                    if (!Utils.isEmpty(entity.getContent())){
                        StringBuilder sb = new StringBuilder(entity.getContent());
                        sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
                        viewBinding.webViewNotice.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
                    }
                }
            }
        });
    }
}
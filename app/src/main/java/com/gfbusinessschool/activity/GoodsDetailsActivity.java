package com.gfbusinessschool.activity;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.GoodsEntity;
import com.gfbusinessschool.databinding.ActivityGoodsDetailsBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情
 */
@Route(path = ARouterPath.GoodsDetailsActivity)
public class GoodsDetailsActivity extends BaseActivity<ActivityGoodsDetailsBinding> {
    @Autowired
    String goodId;
    private GoodsEntity goodsEntity;

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.title_goods));
        viewBinding.line2.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        viewBinding.bottomLayout.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        //支持js
        viewBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        viewBinding.webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        viewBinding.webView.getSettings().setJavaScriptEnabled(true);
        viewBinding.webView.getSettings().setBlockNetworkImage(false);
        viewBinding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        viewBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        viewBinding.bottomLayout.releaseShareBtn.setOnClickListener(v -> {
            if (goodsEntity==null) return;
            if (viewBinding.bottomLayout.releaseShareBtn.getText().toString().equals(getString(R.string.soldout)))
                ToastUtil.show(getApplicationContext(),"该商品已售罄，看看其他商品吧");
            else {
                ARouter.getInstance().build(ARouterPath.SubmitShoppingOrderActivity)
                        .withParcelable("goodsEntity",goodsEntity).navigation();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (Utils.isEmpty(goodId)) return;
        Map<String, String> map = new HashMap<>();
        map.put("id",goodId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.SHOPMALL_GOODS_DETAILS, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    goodsEntity = JSONArray.parseObject(response,GoodsEntity.class);
                    if (goodsEntity==null) return;
                    GlideLoadUtils.load(getApplicationContext(),goodsEntity.getImgUrl(),viewBinding.goodsCover,GlideLoadUtils.TYPE_PLACE_HOLDER_NO);
                    viewBinding.goodsName.setText(Utils.getString(goodsEntity.getGoodsName()));
                    if (Utils.isEmpty(goodsEntity.getPrice()))
                        viewBinding.integralCount.setText(String.format(getString(R.string.place_integral_count),"0"));
                    else
                        viewBinding.integralCount.setText(String.format(getString(R.string.place_integral_count),goodsEntity.getPrice()));
                    int stock;//库存
                    if (Utils.isEmpty(goodsEntity.getStock()))
                        stock =0;
                    else
                        stock =Integer.parseInt(goodsEntity.getStock());
                    viewBinding.goodsCount.setText(String.format(getString(R.string.place_goodscount),stock+""));
                    if (stock==0){
                        viewBinding.bottomLayout.releaseShareBtn.setText(getString(R.string.soldout));
                    }else {
                        viewBinding.bottomLayout.releaseShareBtn.setText(getString(R.string.rightnow_buy));
                    }
                    if (!Utils.isEmpty(goodsEntity.getGoodsIntroduce())){
                        StringBuilder sb = new StringBuilder(goodsEntity.getGoodsIntroduce());
                        sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
                        viewBinding.webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
                    }
                }
            }
        });
    }

}
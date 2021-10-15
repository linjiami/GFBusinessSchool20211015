package com.gfbusinessschool.fragment;

import android.os.Build;
import android.webkit.WebSettings;

import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.AudioEntity;
import com.gfbusinessschool.databinding.FragmentAudiointroduceBinding;
import com.gfbusinessschool.utils.Utils;

/**
 * 音频介绍
 */
public class AudioIntroduceFragment extends BaseFragment<FragmentAudiointroduceBinding> {

    @Override
    protected void initView() {
        super.initView();
        //设置自适应屏幕，两者合用
        viewBinding.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        viewBinding.webView.getSettings().setLoadWithOverviewMode(true);
        //支持js
        viewBinding.webView.getSettings().setJavaScriptEnabled(true);
        viewBinding.webView.getSettings().setBlockNetworkImage(false);
        viewBinding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        viewBinding.webView.loadUrl("");
    }

    public void setAudioEntity(AudioEntity audioEntity){
        if (audioEntity==null) return;
        viewBinding.title.setText(Utils.getString(audioEntity.getName()));
        viewBinding.studyCount.setText(String.format(getString(R.string.place_study_count),
                audioEntity.getStuNum()+""));
        if (!Utils.isEmpty(audioEntity.getIntroduction())){
            StringBuilder sb = new StringBuilder(audioEntity.getIntroduction());
            sb.insert(0,"<html><head><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" name=\"viewport\"><style type=\"text/css\">img{display: inline-block;max-width:100%}</style></head><body></body></html> ");
            viewBinding.webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
        }
    }

}
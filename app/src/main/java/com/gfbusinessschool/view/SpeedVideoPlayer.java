package com.gfbusinessschool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class SpeedVideoPlayer extends StandardGSYVideoPlayer {


    public SpeedVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SpeedVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void clearFullscreenLayout() {
        super.clearFullscreenLayout();
    }

    public ViewGroup getBottomContainer(){
        return mBottomContainer;
    }

    public RelativeLayout getParentRelativeLayout(){
        return mThumbImageViewLayout;
    }

    public SeekBar getSeekBar(){
        return mProgressBar;
    }
}
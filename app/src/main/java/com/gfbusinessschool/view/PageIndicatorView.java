package com.gfbusinessschool.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.gfbusinessschool.R;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shichaohui on 2015/7/10 0010.
 * <p/>
 * 页码指示器类，获得此类实例后，可通过{@link PageIndicatorView#initIndicator(int)}方法初始化指示器
 * </P>
 */
public class PageIndicatorView extends LinearLayout {

    private Context mContext = null;
    private int dotSize = 5; // 指示器的大小（dp）
    private int dotSize2 = 30;
    private int margins = 1; // 指示器间距（dp）
    private List<View> indicatorViews = null; // 存放指示器
    private LayoutParams mParams;

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        dotSize = Utils.dip2px(context, dotSize);
        margins = Utils.dip2px(context, margins);
    }

    /**
     * 初始化指示器，默认选中第一页
     *
     * @param count 指示器数量，即页数
     */
    public void initIndicator(int count) {

        if (indicatorViews == null) {
            indicatorViews = new ArrayList<>();
        } else {
            indicatorViews.clear();
            removeAllViews();
        }
        View view;
        mParams = new LayoutParams(dotSize, dotSize);
        mParams.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < count; i++) {
            view = new View(mContext);
            addView(view, mParams);
            indicatorViews.add(view);
        }
        if (indicatorViews.size() > 0) {
            indicatorViews.get(0).setBackgroundResource(R.mipmap.select_yes);
        }
    }

    /**
     * 设置选中页
     *
     * @param selected 页下标，从0开始
     */
    public void setSelectedPage(int selected) {
        LayoutParams params = new LayoutParams(dotSize2, dotSize);
        params.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < indicatorViews.size(); i++) {
            //选中
            if (i == selected) {
                indicatorViews.get(i).setLayoutParams(params);
                indicatorViews.get(i).setBackgroundResource(R.drawable.shape50_themecolor);
                Utils.setBackgroundSolid(indicatorViews.get(i),Utils.getThemeColor(mContext));
            } else {//未选中
                indicatorViews.get(i).setLayoutParams(mParams);
                indicatorViews.get(i).setBackgroundResource(R.mipmap.select_no);//未选中
            }
        }
    }

}
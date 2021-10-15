package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.R;

public class BassRecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //这两个静态变量来区分是normalView还是footView。
    protected static final int NORMAL_VIEW = 0;
    protected static final int FOOT_VIEW = 1;
    protected static final int NORMAL_VIEW2 = 2;
    protected static final int NORMAL_VIEW3 = 3;
    protected boolean isVisiableProgressBar =true;
    protected boolean isVisiableFootView =false;
    protected Context mContext;

    public BassRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 显示底部加载
     * @param _isVisiableProgressBar 加载图片是否显示
     */
    public void setProgressBarVisiable(boolean _isVisiableProgressBar) {
        isVisiableFootView =true;
        isVisiableProgressBar = _isVisiableProgressBar;
        notifyItemChanged(getItemCount()-1);
    }

    /**
     * 显示底部加载
     */
    public void setFootViewVisiable(boolean isVisiable) {
        isVisiableFootView = isVisiable;
        notifyItemChanged(getItemCount()-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        if (_holder instanceof FootViewHolder){
            FootViewHolder footViewHolder = (FootViewHolder) _holder;
            if (isVisiableFootView){
                footViewHolder.footview_root_layout.setVisibility(View.VISIBLE);
                if (isVisiableProgressBar){//数据加载中
                    footViewHolder.loading_text.setText(mContext.getString(R.string.foot_layout_loading));
                    footViewHolder.progress_bar.setVisibility(View.VISIBLE);
                }else {//没有数据了
                    footViewHolder.progress_bar.setVisibility(View.GONE);
                    footViewHolder.loading_text.setText(mContext.getString(R.string.foot_layout_no_data));
                }
            }else {
                footViewHolder.footview_root_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.ClickStateBean;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HotSearchAdapter extends BassRecyclerAdapter {
    private List<ClickStateBean> mList = new ArrayList<>();
    private OnClickCallBack mOnItemClickLinstener;


    public HotSearchAdapter(Context mContext, List<ClickStateBean> mList, OnClickCallBack onItemClickListener) {
        super(mContext);
        this.mList = mList;
        this.mOnItemClickLinstener = onItemClickListener;
    }

    public HotSearchAdapter(Context mContext, OnClickCallBack onItemClickListener) {
        super(mContext);
        this.mOnItemClickLinstener = onItemClickListener;
    }

    public void setmList(List<ClickStateBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodler(LayoutInflater.from(mContext).inflate(R.layout.item_hot_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler viewHodler = (MyViewHodler) holder;
            ClickStateBean clickStateBean =mList.get(position);
            if (clickStateBean.isSingleSelected()){
                 if (clickStateBean.isSelected()){
                     viewHodler.contentSearch.setTextColor(mContext.getResources().getColor(R.color.white));
                     Utils.setBackgroundSolid(mContext,viewHodler.contentSearch);
//                     viewHodler.contentSearch.setBackgroundResource(R.drawable.bianxian24_theme_color);
                 }else {
                     viewHodler.contentSearch.setTextColor(mContext.getResources().getColor(R.color.color_33));
                     viewHodler.contentSearch.setBackgroundResource(R.drawable.bianxian24_color_f8);
                 }
            }
            viewHodler.contentSearch.setText(mList.get(position).getContent());
            viewHodler.contentSearch.setOnClickListener(v -> {
                if (mList.get(position).isSelected()){
                    mList.get(position).setSelected(false);
                    viewHodler.contentSearch.setTextColor(mContext.getResources().getColor(R.color.color_33));
                    viewHodler.contentSearch.setBackgroundResource(R.drawable.bianxian24_color_f8);
                }else {
                    viewHodler.contentSearch.setTextColor(mContext.getResources().getColor(R.color.white));
                    viewHodler.contentSearch.setBackgroundResource(R.drawable.bianxian24_theme_color);
                    if (clickStateBean.isSingleSelected()) {
                        //清除其他选中状态
                        for (ClickStateBean bean : mList){
                            bean.setSelected(false);
                        }
                        mList.get(position).setSelected(true);
                        notifyDataSetChanged();
                    }else {
                        mList.get(position).setSelected(true);
                    }
                }

                if (mOnItemClickLinstener!=null) mOnItemClickLinstener.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private TextView contentSearch;

        public MyViewHodler(@NonNull View itemView) {
            super(itemView);
            contentSearch = itemView.findViewById(R.id.contentSearch);
        }
    }

}

package com.gfbusinessschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.bean.RanklistStoreBean;
import com.gfbusinessschool.bean.StoreEntity;
import com.gfbusinessschool.databinding.FooterLoadingLayoutBinding;
import com.gfbusinessschool.databinding.ItemStoreRanklistBinding;
import com.gfbusinessschool.utils.GlideLoadUtils;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程表格列表（首页）/课程列表（课程目录）
 */
public class RanklistStoreAdapter extends BassRecyclerAdapter {
    public static final String TYPE_VIEW_STORE_CLEARANCE_RATE ="门店排行/全员学习通关率";
    public static final String TYPE_VIEW_STORE_TIME_LENGTH ="门店排行/学习在线平均时长排名";
    public static final String TYPE_VIEW_STORE_POINT ="门店排行/学员积分平均排名";
    public static final String TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE ="个人排行/一次性通关率";
    public static final String TYPE_VIEW_PERSONEL_TIME_LENGTH ="个人排行/在线学习时长";
    public static final String TYPE_VIEW_PERSONEL_CLEARANCE_RATE ="个人排行/通关率";
    public static final String TYPE_VIEW_PERSONEL_POINT="个人排行/积分";
    public static final String TYPE_VIEW_MANAGER_TODAY ="管理员首页/今日学习时间排行榜";
    public static final String TYPE_VIEW_MANAGER_ALL ="管理员首页/累计学习时间排行榜";
    public static final String TYPE_VIEW_MANAGER_SHARE ="管理门店排行/累计分享排行榜";
    public static final String TYPE_VIEW_STORE_TODAY_TIME_LENGTH ="管理门店排行/今日学习时间排名";
    public static final String TYPE_VIEW_STORE_ALLTIME_LENGTH ="管理门店排行/累计学习时间排名";
    public static final String TYPE_VIEW_MANAGER_STORE_POINT ="管理门店排行/累计积分排名";
    public static final String TYPE_VIEW_VOTE_RANKLIST ="投票评选/投票排行";

    private List<StoreEntity> mList = new ArrayList<>();
    private boolean isLoadMore;//是否分页
    private String typeView =TYPE_VIEW_STORE_CLEARANCE_RATE;
    private boolean isShowSolid;//是否显示边框（个人排行需要）
    private OnClickCallBack onClickCallBack;

    public RanklistStoreAdapter(Context mContext) {
        super(mContext);
    }

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }

    public void setShowSolid(boolean showSolid) {
        isShowSolid = showSolid;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public void setmList(List<StoreEntity> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMore){
            if (position==mList.size())
                return FOOT_VIEW;
            else
                return NORMAL_VIEW;
        }else {
            return NORMAL_VIEW;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==FOOT_VIEW)
            return new FootViewHolder(FooterLoadingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new MyViewHodler(ItemStoreRanklistBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof MyViewHodler) {
            MyViewHodler myHolder = (MyViewHodler) holder;
            StoreEntity bean =mList.get(position);
            if (bean==null) return;
            if (position==0){
                myHolder.ranklistIcon.setVisibility(View.VISIBLE);
                myHolder.positionRanklist.setVisibility(View.GONE);
                GlideLoadUtils.load(mContext,R.mipmap.ranklist_store1,myHolder.ranklistIcon);
            }else if (position==1){
                myHolder.ranklistIcon.setVisibility(View.VISIBLE);
                myHolder.positionRanklist.setVisibility(View.GONE);
                GlideLoadUtils.load(mContext,R.mipmap.ranklist_store2,myHolder.ranklistIcon);
            } else if (position==2){
                myHolder.ranklistIcon.setVisibility(View.VISIBLE);
                myHolder.positionRanklist.setVisibility(View.GONE);
                GlideLoadUtils.load(mContext,R.mipmap.ranklist_store3,myHolder.ranklistIcon);
            }else {
                myHolder.ranklistIcon.setVisibility(View.GONE);
                myHolder.positionRanklist.setVisibility(View.VISIBLE);
                myHolder.positionRanklist.setText(""+(position+1));
            }
            if (isShowSolid && position==mList.size()-1 && position<3){
                myHolder.ranklistIcon.setVisibility(View.GONE);
                myHolder.positionRanklist.setVisibility(View.VISIBLE);
                myHolder.positionRanklist.setText(Utils.getString(bean.getSortNum()));
            }
            if (Utils.getString(typeView).equals(TYPE_VIEW_STORE_CLEARANCE_RATE)){
                myHolder.nameStore.setText(Utils.getString(bean.getStoreName()));
                myHolder.finishRate.setText(Utils.getString(bean.getTestPassRate()));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_STORE_TIME_LENGTH) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_STORE_TODAY_TIME_LENGTH) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_STORE_ALLTIME_LENGTH)){
                myHolder.nameStore.setText(Utils.getString(bean.getStoreName()));
                myHolder.finishRate.setText(Utils.getString(bean.getStudyLength()));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_STORE_POINT)){
                myHolder.nameStore.setText(Utils.getString(bean.getStoreName()));
                myHolder.finishRate.setText(Utils.getString(bean.getAvgIntegral()));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE)){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getName()));
                myHolder.finishRate.setText(Utils.getString(bean.getPassRate()));
                if (isShowSolid && position==mList.size()-1){
                    myHolder.positionRanklist.setText(Utils.getString(bean.getSortNum()));
                    myHolder.layoutItemRanklist.setBackgroundResource(R.drawable.bianxian6_b2_white);
                }else {
                    myHolder.layoutItemRanklist.setBackgroundResource(R.color.white);
                }
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_TIME_LENGTH)){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getName()));
                myHolder.finishRate.setText(Utils.getString(bean.getStudyLength()));
                if (isShowSolid && position==mList.size()-1){
                    myHolder.positionRanklist.setText(Utils.getString(bean.getSortNum()));
                    myHolder.layoutItemRanklist.setBackgroundResource(R.drawable.bianxian6_b2_white);
                }else {
                    myHolder.layoutItemRanklist.setBackgroundResource(R.color.white);
                }
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_CLEARANCE_RATE)){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getName()));
                myHolder.finishRate.setText(Utils.getString(bean.getPassRate()));
                if (isShowSolid && position==mList.size()-1){
                    myHolder.positionRanklist.setText(Utils.getString(bean.getSortNum()));
                    myHolder.layoutItemRanklist.setBackgroundResource(R.drawable.bianxian6_b2_white);
                }else {
                    myHolder.layoutItemRanklist.setBackgroundResource(R.color.white);
                }
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_POINT)){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getName()));
                myHolder.finishRate.setText(Utils.getString(bean.getIntegral()));
                if (isShowSolid && position==mList.size()-1){
                    myHolder.positionRanklist.setText(Utils.getString(bean.getSortNum()));
                    myHolder.layoutItemRanklist.setBackgroundResource(R.drawable.bianxian6_b2_white);
                }else {
                    myHolder.layoutItemRanklist.setBackgroundResource(R.color.white);
                }
            } else if (Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_TODAY) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_ALL) ){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getName()));
                myHolder.finishRate.setText(Utils.getString(bean.getStudyLength()));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_SHARE) ){
                myHolder.nameStore.setText(Utils.getString(bean.getStoreName()));
                myHolder.finishRate.setText(String.format(mContext.getString(R.string.count_study),Utils.getString(bean.getShareNum())));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_STORE_POINT)){
                GlideLoadUtils.load(mContext,bean.getHeadImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getStoreName()));
                myHolder.finishRate.setText(Utils.getString(bean.getTotalIntegral()));
            }else if (Utils.getString(typeView).equals(TYPE_VIEW_VOTE_RANKLIST)){
                GlideLoadUtils.load(mContext,bean.getImgUrl(),myHolder.ranklistHead,GlideLoadUtils.TYPE_PLACE_HOLDER_HEAD);
                myHolder.nameStore.setText(Utils.getString(bean.getPlayerName()));
                if (Utils.isEmpty(bean.getVoteNums()))
                    myHolder.finishRate.setText("0");
                else
                    myHolder.finishRate.setText(Utils.getString(bean.getVoteNums()));
            }
           myHolder.binding.layoutItemRanklist.setOnClickListener(v -> {
               if (onClickCallBack!=null) onClickCallBack.onClick(bean);
           });
        }
    }

    @Override
    public int getItemCount() {
        if (isLoadMore)
            return (mList==null || mList.size()==0)? 0 : mList.size()+1;
        else
            return (mList==null || mList.size()==0)? 0 : mList.size();
    }

    private class MyViewHodler extends RecyclerView.ViewHolder {
        private ImageView ranklistIcon,ranklistHead;
        private ViewGroup headLayout,layoutItemRanklist;
        private TextView nameStore, finishRate,positionRanklist;
        private ItemStoreRanklistBinding binding;

        public MyViewHodler(@NonNull ItemStoreRanklistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            ranklistIcon = binding.ranklistIcon;
            nameStore = binding.nameStore;
            finishRate = binding.finishRate;
            positionRanklist = binding.positionRanklist;
            headLayout = binding.headLayout;
            layoutItemRanklist = binding.layoutItemRanklist;
            ranklistHead = binding.ranklistHead;
            if (Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_ONCE_CLEARANCE_RATE) ||
                    Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_TIME_LENGTH)||
                    Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_CLEARANCE_RATE)||
                    Utils.getString(typeView).equals(TYPE_VIEW_PERSONEL_POINT)||
                    Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_TODAY)||
                    Utils.getString(typeView).equals(TYPE_VIEW_VOTE_RANKLIST)||
                    Utils.getString(typeView).equals(TYPE_VIEW_MANAGER_ALL)){
                headLayout.setVisibility(View.VISIBLE);
            }else {
                headLayout.setVisibility(View.GONE);
            }
        }
    }

}

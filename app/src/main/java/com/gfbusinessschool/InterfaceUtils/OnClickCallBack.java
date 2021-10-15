package com.gfbusinessschool.InterfaceUtils;

import androidx.appcompat.app.AlertDialog;

import com.gfbusinessschool.bean.ReadCollectionEntity;
import com.gfbusinessschool.bean.StoreAddressEntity;
import com.gfbusinessschool.bean.StoreEntity;

public abstract class OnClickCallBack {

    public void onClick(int position){

    }

    public void onClickTest(int position){

    }

    public void onDeleteClick(int position){

    }

    public void onClickTestAnswer(int position,int indexAnswer,boolean isCheck){

    }

    public void onClick(){

    }
    public void onClick(StoreEntity entity){

    }
    public void onClick(String id){

    }
    public void onClick(String type, String id, String status){

    }

    /**
     * 评论
     * @param commentRating 评论几颗星
     * @param comment 评论内容
     */
    public void onClickComment(int commentRating,String comment){

    }

    public void onClick(int position, StoreAddressEntity storeAddressEntity){ }

    public void onClick(StoreAddressEntity storeAddressEntity){ }
    /**
     * 奖励金点击事件
     * @param dialog
     * @param typeReward 类型，1鲁班币，2微信，3支付宝 4其他
     * @param coins 打赏金额
     */
    public void onRewardClick(AlertDialog dialog, int typeReward, int coins){ }

    /**
     * 自定义打赏金额
     * @param ortherCoins
     */
    public void onRewardOrtherCoins(int ortherCoins){}

    /**
     * 取消订单
     */
    public void onCancleOrderClick(int position){}

    /**
     * 立即支付订单
     */
    public void onRightNowOrderClick(int position){}

    /**
     * 删除订单
     */
    public void onDeleteOrderClick(int position){}

    public void onModifyClick(int position){

    }

    public void onClickEdittext(int position,String content){

    }

    public void onResultCallBack(){

    }

    public void onClickReadCollection(int position){

    }

    public void onClickReadCollection(ReadCollectionEntity.Entity entity){

    }
    public void onClick(ReadCollectionEntity.Entity entity){

    }
}

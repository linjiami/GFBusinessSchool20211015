package com.gfbusinessschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品
 */
public class GoodsEntity implements Parcelable {

    /**
     * id : 1
     * goodsName : 西瓜
     * imgUrl : 1
     * price : 100
     * stock : 40
     * surplus : 50
     * goodsIntroduce : 西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜西瓜
     * status : 1
     * isDel : 1
     * createDate : 2021-06-07 13:56:12
     * companyId : 1
     * categoryId : 1401776465872633858
     */

    private String id;
    private String goodsName;
    private String imgUrl;
    private String price;
    private String stock;
    private String surplus;
    private String goodsIntroduce;
    private String status;
    private String isDel;
    private String createDate;
    private String companyId;
    private String categoryId;
    private String orderNum;//订单号
    private String expressNum;//快递单号
    private String expressName;//快递名称
    private String goodsPrice;
    private String payPrice;
    private String goodsNums;

    public GoodsEntity() {
    }

    protected GoodsEntity(Parcel in) {
        id = in.readString();
        goodsName = in.readString();
        imgUrl = in.readString();
        price = in.readString();
        stock = in.readString();
        surplus = in.readString();
        goodsIntroduce = in.readString();
        status = in.readString();
        isDel = in.readString();
        createDate = in.readString();
        companyId = in.readString();
        categoryId = in.readString();
        orderNum = in.readString();
        expressNum = in.readString();
        expressName = in.readString();
        goodsPrice = in.readString();
        payPrice = in.readString();
        goodsNums = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(goodsName);
        dest.writeString(imgUrl);
        dest.writeString(price);
        dest.writeString(stock);
        dest.writeString(surplus);
        dest.writeString(goodsIntroduce);
        dest.writeString(status);
        dest.writeString(isDel);
        dest.writeString(createDate);
        dest.writeString(companyId);
        dest.writeString(categoryId);
        dest.writeString(orderNum);
        dest.writeString(expressNum);
        dest.writeString(expressName);
        dest.writeString(goodsPrice);
        dest.writeString(payPrice);
        dest.writeString(goodsNums);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel in) {
            return new GoodsEntity(in);
        }

        @Override
        public GoodsEntity[] newArray(int size) {
            return new GoodsEntity[size];
        }
    };

    public String getGoodsNums() {
        return goodsNums;
    }

    public void setGoodsNums(String goodsNums) {
        this.goodsNums = goodsNums;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getGoodsIntroduce() {
        return goodsIntroduce;
    }

    public void setGoodsIntroduce(String goodsIntroduce) {
        this.goodsIntroduce = goodsIntroduce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
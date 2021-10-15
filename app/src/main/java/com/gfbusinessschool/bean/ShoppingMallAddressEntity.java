package com.gfbusinessschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingMallAddressEntity implements Parcelable {
    private String mobile;
    private String name;
    private String address;
    private String id;

    public ShoppingMallAddressEntity() {
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected ShoppingMallAddressEntity(Parcel in) {
        mobile = in.readString();
        name = in.readString();
        address = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingMallAddressEntity> CREATOR = new Creator<ShoppingMallAddressEntity>() {
        @Override
        public ShoppingMallAddressEntity createFromParcel(Parcel in) {
            return new ShoppingMallAddressEntity(in);
        }

        @Override
        public ShoppingMallAddressEntity[] newArray(int size) {
            return new ShoppingMallAddressEntity[size];
        }
    };
}
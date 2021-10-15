package com.gfbusinessschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChangeUserEntity implements Parcelable {
    private String address;
    private String age;
    private String headImgUrl;
    private String jianjie;
    private String nickname;
    private String position;
    private String sex;


    public ChangeUserEntity() {
    }

    protected ChangeUserEntity(Parcel in) {
        address = in.readString();
        age = in.readString();
        headImgUrl = in.readString();
        jianjie = in.readString();
        nickname = in.readString();
        position = in.readString();
        sex = in.readString();
    }

    public static final Creator<ChangeUserEntity> CREATOR = new Creator<ChangeUserEntity>() {
        @Override
        public ChangeUserEntity createFromParcel(Parcel in) {
            return new ChangeUserEntity(in);
        }

        @Override
        public ChangeUserEntity[] newArray(int size) {
            return new ChangeUserEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(age);
        dest.writeString(headImgUrl);
        dest.writeString(jianjie);
        dest.writeString(nickname);
        dest.writeString(position);
        dest.writeString(sex);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public static Creator<ChangeUserEntity> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "ChangeUserEntity{" +
                "address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", jianjie='" + jianjie + '\'' +
                ", nickname='" + nickname + '\'' +
                ", position='" + position + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}

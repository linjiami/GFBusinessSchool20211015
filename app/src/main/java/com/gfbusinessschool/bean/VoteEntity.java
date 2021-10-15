package com.gfbusinessschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VoteEntity implements Parcelable {

    /**
     * id : 1399987596596494337
     * activeName : 水果大比拼
     * introduce : 选出最受大家欢迎的水果
     * prize : 第一名：20斤的大西瓜，第二名：15斤苹果，第三名：10斤台湾凤梨
     * startDate : 2021-06-02 00:00:00
     * endDate : 2021-06-30 00:00:00
     * imgUrl : http://test.imcfc.com/20210602/f62d573b2c0643c0b8191b220c38b54a.jpg
     * voteAstrict : 5
     * voteUsers : 1
     * certNum : 5
     * playerType : 2
     * status : 2
     * playerPowerIds : 1397821588354568194,1398106688069775362,1398196685330423810,1399267155496583169
     * votePewerIds : 1397821588354568194,1398106688069775362,1398196685330423810,1399267155496583169
     * createDate : 2021-06-30 00:00:00
     * companyId : 1
     */

    private String id;
    private String activeName;
    private String introduce;
    private String prize;
    private String startDate;
    private String endDate;
    private String imgUrl;
    private String voteAstrict;
    private String voteUsers;
    private String certNum;
    private String playerType;
    private String status;
    private String playerPowerIds;
    private String votePewerIds;
    private String createDate;
    private String companyId;

    public VoteEntity() {
    }

    protected VoteEntity(Parcel in) {
        id = in.readString();
        activeName = in.readString();
        introduce = in.readString();
        prize = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        imgUrl = in.readString();
        voteAstrict = in.readString();
        voteUsers = in.readString();
        certNum = in.readString();
        playerType = in.readString();
        status = in.readString();
        playerPowerIds = in.readString();
        votePewerIds = in.readString();
        createDate = in.readString();
        companyId = in.readString();
    }

    public static final Creator<VoteEntity> CREATOR = new Creator<VoteEntity>() {
        @Override
        public VoteEntity createFromParcel(Parcel in) {
            return new VoteEntity(in);
        }

        @Override
        public VoteEntity[] newArray(int size) {
            return new VoteEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVoteAstrict() {
        return voteAstrict;
    }

    public void setVoteAstrict(String voteAstrict) {
        this.voteAstrict = voteAstrict;
    }

    public String getVoteUsers() {
        return voteUsers;
    }

    public void setVoteUsers(String voteUsers) {
        this.voteUsers = voteUsers;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlayerPowerIds() {
        return playerPowerIds;
    }

    public void setPlayerPowerIds(String playerPowerIds) {
        this.playerPowerIds = playerPowerIds;
    }

    public String getVotePewerIds() {
        return votePewerIds;
    }

    public void setVotePewerIds(String votePewerIds) {
        this.votePewerIds = votePewerIds;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(activeName);
        dest.writeString(introduce);
        dest.writeString(prize);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(imgUrl);
        dest.writeString(voteAstrict);
        dest.writeString(voteUsers);
        dest.writeString(certNum);
        dest.writeString(playerType);
        dest.writeString(status);
        dest.writeString(playerPowerIds);
        dest.writeString(votePewerIds);
        dest.writeString(createDate);
        dest.writeString(companyId);
    }
}
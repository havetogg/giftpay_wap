package org.jumutang.giftpay.model;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public class AdvertiseRecordModel {
    private String id;
    private String userNum;
    private String advertiseType;
    private String ip;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getAdvertiseType() {
        return advertiseType;
    }

    public void setAdvertiseType(String advertiseType) {
        this.advertiseType = advertiseType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

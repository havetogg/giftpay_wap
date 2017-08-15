package org.jumutang.giftpay.entity;

/**
 * Created by RuanYJ on 2017/8/11.
 */
public class UserOilRecord {
    private String id;
    private String openId;
    private String userId;
    private String oilType;
    private String createTime;
    private String oilNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOilNum() {
        return oilNum;
    }

    public void setOilNum(String oilNum) {
        this.oilNum = oilNum;
    }
}

package org.jumutang.giftpay.entity;

/**
 * Created by RuanYJ on 2017/1/14.
 */
public class UserModel {

    public UserModel() {
    }

    private String id;
    private String loginMobile;
    private String wechatNikeName;
    private String loginPassword;
    private String wechatUnionID;
    private String headImgUrl;
    private String rediectUrl;
    private String openId;
    private String identity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile;
    }

    public String getWechatNikeName() {
        return wechatNikeName;
    }

    public void setWechatNikeName(String wechatNikeName) {
        this.wechatNikeName = wechatNikeName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getWechatUnionID() {
        return wechatUnionID;
    }

    public void setWechatUnionID(String wechatUnionID) {
        this.wechatUnionID = wechatUnionID;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getRediectUrl() {
        return rediectUrl;
    }

    public void setRediectUrl(String rediectUrl) {
        this.rediectUrl = rediectUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}

package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/5/22.
 */
public class AdvertiseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String advertiseId;
    private String advertiseName;
    private String advertiseImgUrl;
    private String advertiseDesc;
    private String advertiseHref;
    private String createTime;
    private String status;
    private String clickNum;
    private String rate;


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    public String getAdvertiseName() {
        return advertiseName;
    }

    public void setAdvertiseName(String advertiseName) {
        this.advertiseName = advertiseName;
    }

    public String getAdvertiseImgUrl() {
        return advertiseImgUrl;
    }

    public void setAdvertiseImgUrl(String advertiseImgUrl) {
        this.advertiseImgUrl = advertiseImgUrl;
    }

    public String getAdvertiseDesc() {
        return advertiseDesc;
    }

    public void setAdvertiseDesc(String advertiseDesc) {
        this.advertiseDesc = advertiseDesc;
    }

    public String getAdvertiseHref() {
        return advertiseHref;
    }

    public void setAdvertiseHref(String advertiseHref) {
        this.advertiseHref = advertiseHref;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

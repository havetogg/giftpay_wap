package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/5/22.
 */
public class PrizeModel implements Serializable {

    private static final long serialVersionUID = 1L;


    private String prizeId;
    private String prizeName;
    private String prizeType;
    private String prizeNum;
    private String prizeChance;
    private String createTime;
    private String status;
    private String clickNum;

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(String prizeNum) {
        this.prizeNum = prizeNum;
    }

    public String getPrizeChance() {
        return prizeChance;
    }

    public void setPrizeChance(String prizeChance) {
        this.prizeChance = prizeChance;
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

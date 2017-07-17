package org.jumutang.giftpay.entity;

/**
 * Created by RuanYJ on 2017/6/5.
 */
public class GoodsListModel {
    private String id;
    private String lineNum;
    private String goodsPrice;
    private String realPrice;
    private String goodsType;
    private String goodsSubType;
    private String status;
    private String createTime;
    private String goodsMd5;

    public String getGoodsMd5() {
        return goodsMd5;
    }

    public void setGoodsMd5(String goodsMd5) {
        this.goodsMd5 = goodsMd5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getGoodsType() {
        return goodsType;
    }

    /**
     * 1加油卡充值  2话费充值  3流量充值
     * @param goodsType
     */
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsSubType() {
        return goodsSubType;
    }

    /**
     * 1:0-默认 1-中石化 2-中石油
     * 2:0-默认 1-移动 2-联通 3-电信
     * 3:0-默认 1-移动 2-联通 3-电信
     * @param goodsSubType
     */
    public void setGoodsSubType(String goodsSubType) {
        this.goodsSubType = goodsSubType;
    }

    public String getStatus() {
        return status;
    }

    /**
     * 0可用  1禁用
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

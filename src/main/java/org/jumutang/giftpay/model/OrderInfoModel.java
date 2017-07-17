package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/6/4.
 */
public class OrderInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String openId;
    private String goodsName;
    private String goodsId;
    private String payMoney;
    private String goodsMoney;
    private String ip;
    private String status;
    private String createTime;
    private String orderNo;
    private String wxOrder;
    private String fromType;
    private String payStatus;
    private String remark;
    private String md5Sign;
    private String orderParams;
    private String dealState;

    public String getDealState() {
        return dealState;
    }

    public void setDealState(String dealState) {
        this.dealState = dealState;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(String goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWxOrder() {
        return wxOrder;
    }

    public void setWxOrder(String wxOrder) {
        this.wxOrder = wxOrder;
    }

    public String getFromType() {
        return fromType;
    }

    /**
     * 渠道 1:话费 2:油卡 3:流量 4:95加油 5余额充值 6生活缴费 99其他
     * @param fromType
     */
    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMd5Sign() {
        return md5Sign;
    }

    public void setMd5Sign(String md5Sign) {
        this.md5Sign = md5Sign;
    }

    public String getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(String orderParams) {
        this.orderParams = orderParams;
    }
}

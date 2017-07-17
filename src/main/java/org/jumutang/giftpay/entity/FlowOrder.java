package org.jumutang.giftpay.entity;

/**
 * Created by Administrator on 2017/4/5.
 */
public class FlowOrder {

    //流量充值订单号 唯一
    private String flowOrderId;

    //流量充值时间
    private String flowOrderTime;

    //订单状态
    private String flowOrderStatus;

    //流量充值手机号码
    private String flowOrderMobile;

    //流量包代码
    private String flowPackCode;

    public String getFlowPackCode() {
        return flowPackCode;
    }

    public void setFlowPackCode(String flowPackCode) {
        this.flowPackCode = flowPackCode;
    }

    public String getFlowOrderId() {
        return flowOrderId;
    }

    public void setFlowOrderId(String flowOrderId) {
        this.flowOrderId = flowOrderId;
    }

    public String getFlowOrderTime() {
        return flowOrderTime;
    }

    public void setFlowOrderTime(String flowOrderTime) {
        this.flowOrderTime = flowOrderTime;
    }

    public String getFlowOrderStatus() {
        return flowOrderStatus;
    }

    public void setFlowOrderStatus(String flowOrderStatus) {
        this.flowOrderStatus = flowOrderStatus;
    }

    public String getFlowOrderMobile() {
        return flowOrderMobile;
    }

    public void setFlowOrderMobile(String flowOrderMobile) {
        this.flowOrderMobile = flowOrderMobile;
    }
}

package org.jumutang.caranswer.wechat.viewmodel;

import java.io.Serializable;

import org.jumutang.caranswer.model.BaseModel;

public class OrderInfoView extends BaseModel implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -4427368398429044560L;
    
    
    //支付金额
    private Double price;
    //套餐期限
    private Integer monthCount;
    //套餐额度
    private Double allMoney;
    //订单时间
    private String orderTime;
    //发放状态
    private String orderState;
    //返还月份
    private String backMonth;
    //订单号
    private String orderId;
    //易宝支付订单号
    private String businessorderid;
    
    
    
    
    public String getOrderId()
    {
        return orderId;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    public String getBusinessorderid()
    {
        return businessorderid;
    }
    public void setBusinessorderid(String businessorderid)
    {
        this.businessorderid = businessorderid;
    }
    public String getBackMonth()
    {
        return backMonth;
    }
    public void setBackMonth(String backMonth)
    {
        this.backMonth = backMonth;
    }
    public Double getPrice()
    {
        return price;
    }
    public OrderInfoView setPrice(Double price)
    {
        this.price = price;
        return this;
    }
    public Integer getMonthCount()
    {
        return monthCount;
    }
    public OrderInfoView setMonthCount(Integer monthCount)
    {
        this.monthCount = monthCount;
        return this;
    }
    public Double getAllMoney()
    {
        return allMoney;
    }
    public OrderInfoView setAllMoney(Double allMoney)
    {
        this.allMoney = allMoney;
        return this;
    }
    public String getOrderTime()
    {
        return orderTime;
    }
    public OrderInfoView setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
        return this;
    }
    public String getOrderState()
    {
        return orderState;
    }
    public OrderInfoView setOrderState(String orderState)
    {
        this.orderState = orderState;
        return this;
    }
    
    

}

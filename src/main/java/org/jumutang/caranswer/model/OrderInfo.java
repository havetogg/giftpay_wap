package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订单表
 * 
 * @author YuanYu
 *
 */
public class OrderInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1236722149511910347L;

	// 订单主键
	private String orderID;
	// 加油卡套餐主键
	private String gasPackagesID;
	// 用户主键
	private String userID;
	// 订单价格
	private Double price;
	// 第三方订单号
	private String businessOrderID;
	// 订单支付状态
	private String status;
	// 第三方支付渠道返回通知
	private String result;
	// 创建时间
	@JSONField(format="yyyy-MM-dd HH:mm")
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 删除时间
	private Date deleteTime;
	//支付链接
	private String payurl;
	
	
    public String getPayurl()
    {
        return payurl;
    }

    public OrderInfo setPayurl(String payurl)
    {
        this.payurl = payurl;
        return this;
    }

    public String getOrderID() {
		return orderID;
	}

	public OrderInfo setOrderID(String orderID) {
		this.orderID = orderID;
		return this;
	}

	public String getGasPackagesID() {
		return gasPackagesID;
	}

	public OrderInfo setGasPackagesID(String gasPackagesID) {
		this.gasPackagesID = gasPackagesID;
		return this;
	}

	public String getUserID() {
		return userID;
	}

	public OrderInfo setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public Double getPrice() {
		return price;
	}

	public OrderInfo setPrice(Double price) {
		this.price = price;
		return this;
	}

	public String getBusinessOrderID() {
		return businessOrderID;
	}

	public OrderInfo setBusinessOrderID(String businessOrderID) {
		this.businessOrderID = businessOrderID;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public OrderInfo setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getResult() {
		return result;
	}

	public OrderInfo setResult(String result) {
		this.result = result;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public OrderInfo setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public OrderInfo setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public OrderInfo setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

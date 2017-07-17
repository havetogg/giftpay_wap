package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 红包信息
 * 
 * @author YuanYu
 *
 */
public class RedInfo extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5696249841990911234L;

	// 红包主键
	private String redID;
	// 用户主键
	private String userID;
	// 订单主键
	private String orderID;
	// 红包名称
	private String redName;
	// 红包金额
	private String redMoney;
	// 红包有效期开始时间
	@JSONField(format="yyyy-MM-dd")
	private Date startTime;
	// 红包有效期结束时间
	private Date endTime;
	// 红包创建时间
	private Date createTime;
	// 红包修改时间
	private Date updateTime;
	// 红包删除时间
	private Date deleteTime;
	//红包优惠
	private String preferential;
	//活动信息
	private Activity activity;
	

	public Activity getActivity()
    {
        return activity;
    }

    public RedInfo setActivity(Activity activity)
    {
        this.activity = activity;
        return this;
    }

    public String getPreferential()
    {
        return preferential;
    }

    public RedInfo setPreferential(String preferential)
    {
        this.preferential = preferential;
        return this;
    }

    public String getRedID() {
		return redID;
	}

	public RedInfo setRedID(String redID) {
		this.redID = redID;
		return this;
	}


	public String getUserID() {
		return userID;
	}

	public RedInfo setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getOrderID() {
		return orderID;
	}

	public RedInfo setOrderID(String orderID) {
		this.orderID = orderID;
		return this;
	}

	public String getRedName() {
		return redName;
	}

	public RedInfo setRedName(String redName) {
		this.redName = redName;
		return this;
	}

	public String getRedMoney() {
		return redMoney;
	}

	public RedInfo setRedMoney(String redMoney) {
		this.redMoney = redMoney;
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public RedInfo setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public RedInfo setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public RedInfo setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public RedInfo setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public RedInfo setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

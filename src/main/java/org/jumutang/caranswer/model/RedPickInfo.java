package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 红包提取信息表
 * 
 * @author YuanYu
 *
 */
public class RedPickInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1544419885354421583L;
	// 红包提取信息主键
	private String redpickID;
	// 用户主键
	private String userID;
	// 提取状态     1成功  0失败   2创建
	private String status;
	// 第三方返回结果集
	private String result;
	// 第三方订单号
	private String businessOrderID;
	// 提取用途备注
	private String pickMeno;
	// 创建时间
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 修改时间
	@JSONField(format="yyyy-MM-dd")
	private Date updateTime;
	// 删除时间
	@JSONField(format="yyyy-MM-dd")
	private Date deleteTime;
	//红包金额
	private double redMoney;
	//红包数量
	private int redNumber;
	

    public double getRedMoney()
    {
        return redMoney;
    }

    public RedPickInfo setRedMoney(double redMoney)
    {
        this.redMoney = redMoney;
        return this;
    }

    public int getRedNumber()
    {
        return redNumber;
    }

    public RedPickInfo setRedNumber(int redNumber)
    {
        this.redNumber = redNumber;
        return this;
    }

    public String getRedpickID() {
		return redpickID;
	}

	public RedPickInfo setRedpickID(String redpickID) {
		this.redpickID = redpickID;
		return this;
	}

	public String getUserID() {
		return userID;
	}

	public RedPickInfo setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public RedPickInfo setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getResult() {
		return result;
	}

	public RedPickInfo setResult(String result) {
		this.result = result;
		return this;
	}

	public String getBusinessOrderID() {
		return businessOrderID;
	}

	public RedPickInfo setBusinessOrderID(String businessOrderID) {
		this.businessOrderID = businessOrderID;
		return this;
	}

	public String getPickMeno() {
		return pickMeno;
	}

	public RedPickInfo setPickMeno(String pickMeno) {
		this.pickMeno = pickMeno;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public RedPickInfo setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public RedPickInfo setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public RedPickInfo setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

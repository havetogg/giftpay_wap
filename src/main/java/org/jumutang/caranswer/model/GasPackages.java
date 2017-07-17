package org.jumutang.caranswer.model;

import java.io.Serializable;

import java.util.Date;
/**
 * 加油卡套餐信息
 * @author YuanYu
 *
 */
public class GasPackages extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4551797846091000648L;
	//加油卡套餐主键
	private String gasPackagesID;
	//加油卡套餐名称
    private String gasPackagesName;
	//套餐价格
	private Double price;
	//套餐返还的月份数量
	private Integer monthCount;
	//每月返还的金额
	private Double monthMoney;
	//套餐总返还金额
	private Double allMoney;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//删除时间
	private Date deleteTime;
	
	
	public String getGasPackagesName()
    {
        return gasPackagesName;
    }

    public GasPackages setGasPackagesName(String gasPackagesName)
    {
        this.gasPackagesName = gasPackagesName;
        return this;
    }


    public String getGasPackagesID() {
		return gasPackagesID;
	}

	public GasPackages setGasPackagesID(String gasPackagesID) {
		this.gasPackagesID = gasPackagesID;
		return this;
	}

	public Double getPrice() {
		return price;
	}

	public GasPackages setPrice(Double price) {
		this.price = price;
		return this;
	}

	public Integer getMonthCount() {
		return monthCount;
	}

	public GasPackages setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
		return this;
	}

	public Double getMonthMoney() {
		return monthMoney;
	}

	public GasPackages setMonthMoney(Double monthMoney) {
		this.monthMoney = monthMoney;
		return this;
	}

	public Double getAllMoney() {
		return allMoney;
	}

	public GasPackages setAllMoney(Double allMoney) {
		this.allMoney = allMoney;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public GasPackages setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public GasPackages setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public GasPackages setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

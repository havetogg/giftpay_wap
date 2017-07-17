package org.jumutang.caranswer.model;

import java.io.Serializable;

import java.util.Date;


/**
 * 用户加油卡信息
 * 
 * @author YuanYu
 *
 */
public class GasCard extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7252946474258771802L;
	// 加油卡信息主键
	private String gascardID;
	// 用户主键
	private String userID;
	// 加油卡号
	private String gasCardNumber;
	// 加油卡绑定手机号
	private String buildTel;
	// 加油卡绑定的姓名
	private String buildUname;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 删除时间
	private Date deleteTime;

	public String getGascardID() {
		return gascardID;
	}

	public GasCard setGascardID(String gascardID) {
		this.gascardID = gascardID;
		return this;
	}

	public String getUserID() {
		return userID;
	}

	public GasCard setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getGasCardNumber() {
		return gasCardNumber;
	}

	public GasCard setGasCardNumber(String gasCardNumber) {
		this.gasCardNumber = gasCardNumber;
		return this;
	}

	public String getBuildTel() {
		return buildTel;
	}

	public GasCard setBuildTel(String buildTel) {
		this.buildTel = buildTel;
		return this;
	}

	public String getBuildUname() {
		return buildUname;
	}

	public GasCard setBuildUname(String buildUname) {
		this.buildUname = buildUname;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public GasCard setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public GasCard setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public GasCard setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    @Override
    public String toString()
    {
        return "GasCard [gascardID=" + gascardID + ", userID=" + userID + ", gasCardNumber=" + gasCardNumber
                + ", buildTel=" + buildTel + ", buildUname=" + buildUname + ", createTime=" + createTime
                + ", updateTime=" + updateTime + ", deleteTime=" + deleteTime + "]";
    }
}

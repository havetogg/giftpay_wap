package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 加油卡变动记录表
 * 
 * @author YuanYu
 *
 */
public class GasCardChangeRecord extends BaseModel implements Serializable {

	private static final long serialVersionUID = -8893042155178380313L;
	// 加油卡变动主键
	private String changeID;
	// 加油卡信息主键
	private String gascardID;
	// 旧的加油卡号
	private String oldGasCard;
	// 新的加油卡号
	private String newGasCard;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 删除时间
	private Date deleteTime;

	public String getChangeID() {
		return changeID;
	}

	public GasCardChangeRecord setChangeID(String changeID) {
		this.changeID = changeID;
		return this;
	}

	public String getGascardID() {
		return gascardID;
	}

	public GasCardChangeRecord setGascardID(String gascardID) {
		this.gascardID = gascardID;
		return this;
	}

	public String getOldGasCard() {
		return oldGasCard;
	}

	public GasCardChangeRecord setOldGasCard(String oldGasCard) {
		this.oldGasCard = oldGasCard;
		return this;
	}

	public String getNewGasCard() {
		return newGasCard;
	}

	public GasCardChangeRecord setNewGasCard(String newGasCard) {
		this.newGasCard = newGasCard;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public GasCardChangeRecord setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public GasCardChangeRecord setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public GasCardChangeRecord setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

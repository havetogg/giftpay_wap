package org.jumutang.caranswer.model;

import java.io.Serializable;
/**
 * 红包提取详情
 * @author YuanYu
 *
 */
import java.util.Date;
public class RedPickDetail extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1917880980516193829L;
	//红包提取详情
	private String redPickDetailID;
	//红包提取信息主键
	private String redPickID;
	//红包主键
	private String redID;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//删除时间
	private Date deleteTime;

	public String getRedPickDetailID() {
		return redPickDetailID;
	}

	public RedPickDetail setRedPickDetailID(String redPickDetailID) {
		this.redPickDetailID = redPickDetailID;
		return this;
	}

	public String getRedPickID() {
		return redPickID;
	}

	public RedPickDetail setRedPickID(String redPickID) {
		this.redPickID = redPickID;
		return this;
	}

	public String getRedID() {
		return redID;
	}

	public RedPickDetail setRedID(String redID) {
		this.redID = redID;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public RedPickDetail setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public RedPickDetail setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public RedPickDetail setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

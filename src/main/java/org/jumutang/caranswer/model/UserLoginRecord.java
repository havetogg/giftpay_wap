package org.jumutang.caranswer.model;

import java.io.Serializable;

import java.util.Date;

/**
 * 用户登录记录表
 * 
 * @author YuanYu
 *
 */
public class UserLoginRecord extends BaseModel implements Serializable {

	private static final long serialVersionUID = 4655091068727857346L;
	// 用户登录记录主键
	private String loginRecordID;
	// 用户主键
	private String userID;
	// 登录记录创建时间
	private Date createTime;
	// 登录记录修改时间
	private Date updateTime;
	// 登录记录删除时间
	private Date deleteTime;

	public String getLoginRecordID() {
		return loginRecordID;
	}

	public UserLoginRecord setLoginRecordID(String loginRecordID) {
		this.loginRecordID = loginRecordID;
		return this;
	}

	public String getUserID() {
		return userID;
	}

	public UserLoginRecord setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public UserLoginRecord setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public UserLoginRecord setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public UserLoginRecord setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

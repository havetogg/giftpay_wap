package org.jumutang.caranswer.model;

import java.io.Serializable;

import java.util.Date;
/**
 * 用户信息
 * @author YuanYu
 *
 */
public class UserInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = 5551254035314679723L;
	// 用户主键
	private String userID;
	// 来源主键
	private String sourceID;
	// 用户注册手机号
	private String userTel;
	// 用户推荐码
	private String recommendCode;
	// 用户注册时间
	private Date createTime;
	// 用户修改时间
	private Date updateTime;
	// 用户删除时间
	private Date deleteTime;
	
	private Authorize authorize;
	

	public Authorize getAuthorize()
    {
        return authorize;
    }

    public UserInfo setAuthorize(Authorize authorize)
    {
        this.authorize = authorize;
        return this;
    }

    public String getUserID() {
		return userID;
	}

	public UserInfo setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getSourceID() {
		return sourceID;
	}

	public UserInfo setSourceID(String sourceID) {
		this.sourceID = sourceID;
		return this;
	}

	public String getUserTel() {
		return userTel;
	}

	public UserInfo setUserTel(String userTel) {
		this.userTel = userTel;
		return this;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public UserInfo setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public UserInfo setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public UserInfo setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public UserInfo setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author 鲁雨
 * @since 20170120
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public class UserInfoModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * id
	 */
	private String userId;
	
	/*
	 * 用户姓名
	 */
	private String userName;
	
	/*
	 * 所在地区
	 */
	private String userArea;
	
	/*
	 * 用户电话
	 */
	private String userPhone;
	
	/*
	 * 用户登录密码
	 */
	private String password;
	
	/*
	 * 身份证号
	 */
	private String userIdCard;
	
	/*
	 * openid
	 */
	private String openId;
	
	/*
	 * 头像地址
	 */
	private String userHeadUrl;
	
	/*
	 * 用户状态
	 */
	private Short userState;
	
	/*
	 * 用户类型
	 */
	private Short userType;
	
	/*
	 * 用户备注
	 */
	private String userMark;
	
	/*
	 * 用户绑定银行卡
	 */
	private String userBanks;
	/*
	 * 用户生日
	 */
	private String userBirthday;
	
	/*
	 * 创建时间
	 */
	private String createTime;
	
	

	public String getUserBanks() {
		return userBanks;
	}

	public void setUserBanks(String userBanks) {
		this.userBanks = userBanks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserIdCard() {
		return userIdCard;
	}

	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUserHeadUrl() {
		return userHeadUrl;
	}

	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}

	public Short getUserState() {
		return userState;
	}

	public void setUserState(Short userState) {
		this.userState = userState;
	}

	public Short getUserType() {
		return userType;
	}

	public void setUserType(Short userType) {
		this.userType = userType;
	}

	public String getUserMark() {
		return userMark;
	}

	public void setUserMark(String userMark) {
		this.userMark = userMark;
	}

	public String getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}

package org.jumutang.giftpay.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 余额实体
 * 
 * @author 鲁雨
 * @since 20170117
 * @version v1.0
 * 
 *          copyright Luyu(18994139782@163.com)
 *
 */
public class BalanceModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 余额id
	 */
	private String balanceId;

	/*
	 * 账户id
	 */
	private String accountId;

	/*
	 * 余额
	 */
	private Double balanceNumber;

	/*
	 * 冻结余额
	 */
	private Double congealBalance;

	/*
	 * 支付密码
	 */
	private String payPassword;

	/*
	 * 余额改变时间
	 */
	private String changeTime;

	/*
	 * 余额状态
	 */
	private Short balanceState;

	/*
	 * 备注信息
	 */
	private String remark;

	/*
	 * 创建时间
	 */
	private Date createTime;

	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/*
         * 用户姓名
         */
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(String balanceId) {
		this.balanceId = balanceId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Double getBalanceNumber() {
		return balanceNumber;
	}

	public void setBalanceNumber(Double balanceNumber) {
		this.balanceNumber = balanceNumber;
	}

	public Double getCongealBalance() {
		return congealBalance;
	}

	public void setCongealBalance(Double congealBalance) {
		this.congealBalance = congealBalance;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	public Short getBalanceState() {
		return balanceState;
	}

	public void setBalanceState(Short balanceState) {
		this.balanceState = balanceState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BalanceModel [balanceId=" + balanceId + ", accountId=" + accountId + ", balanceNumber=" + balanceNumber
				+ ", congealBalance=" + congealBalance + ", payPassword=" + payPassword + ", changeTime=" + changeTime
				+ ", balanceState=" + balanceState + ", remark=" + remark + ", createTime=" + createTime + ", userName="
				+ userName + "]";
	}

}

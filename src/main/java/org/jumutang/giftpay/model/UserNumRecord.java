package org.jumutang.giftpay.model;

/**
 * 
 * @author oywj
 *
 */
public class UserNumRecord {

	/*
	 * 主键
	 */
	private String id;
	/*
	 * 用户id
	 */
	private String userId;
	/*
	 * 油卡卡号或者手机号
	 */
	private String  number;
	/*
	* number 描述
	* */
	private String number_desc;
	/*
	 * 号码类型：1是油卡，2是电话号码
	 */
	private int numberType;
	/*
	* 创建时间
	* */
	private String createTime;


	public String getNumber_desc() {
		return number_desc;
	}

	public void setNumber_desc(String number_desc) {
		this.number_desc = number_desc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getNumberType() {
		return numberType;
	}

	public void setNumberType(int numberType) {
		this.numberType = numberType;
	}

	@Override
	public String toString() {
		return "UserNumRecord [id=" + id + ", userId=" + userId + ", number=" + number + ", number_desc=" + number_desc
				+ ", numberType=" + numberType + ", createTime=" + createTime + "]";
	}
	
	
	
}

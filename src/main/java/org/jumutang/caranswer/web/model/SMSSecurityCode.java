package org.jumutang.caranswer.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码对象
 * 
 * @author YuanYu
 *
 */
public class SMSSecurityCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234923928958031153L;

	// 验证码
	private final String securityCode;
	// 验证时间
	private final Date securityTime;
	// 验证码有效时间
	private final Integer validMinute;

	public SMSSecurityCode(String securityCode, Date securityTime, Integer validMinute) {
		super();
		this.securityCode = securityCode;
		this.securityTime = securityTime;
		this.validMinute = validMinute;
	}

	/**
	 * 获取验证码
	 * @return 验证码
	 */
	public String getSecurityCode() {
		return securityCode;
	}
	/**
	 * 验证码生成时间
	 * @return 生成时间
	 */
	public Date getSecurityTime() {
		return securityTime;
	}
	/**
	 * 验证码有效时间
	 * @return 有效时间
	 */
	public Integer getValidMinute() {
		return validMinute;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

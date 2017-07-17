package org.jumutang.caranswer.framework.x;

import java.util.Random;

public final class TELX {

	private TELX() {
	}

	/**
	 * 加密手机号中间四位
	 * 
	 * @param phone
	 * @return
	 */
	public static final String encryption(String phone) {
		String prefix = phone.substring(0, 3);
		String suffix = phone.substring(7, 11);
		return prefix + "****" + suffix;
	}

	/**
	 * 生成6位数短信验证码
	 * 
	 * @return
	 */
	public static String generateSecurityCode() {
		Random random = new Random();
		String securityCode = String.valueOf(random.nextInt(1000000));
		if (securityCode.length() != 6)
			return generateSecurityCode();
		else
			return securityCode;
	}
}

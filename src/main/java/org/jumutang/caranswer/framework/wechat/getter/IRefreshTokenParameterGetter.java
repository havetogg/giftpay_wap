package org.jumutang.caranswer.framework.wechat.getter;

/**
 * 刷新access_token参数获取器
 * 
 * @author YuanYu
 *
 */
public interface IRefreshTokenParameterGetter {
	/**
	 * 公众号的唯一标识
	 */
	public String getAppID();
	/**
	 * 填写通过access_token获取到的refresh_token参数
	 */
	public String getRefreshToken();
}

package org.jumutang.caranswer.framework.wechat.getter;
/**
 * access_token接口参数获取器
 * @author YuanYu
 *
 */
public interface IAccessTokenParamterGetter {

	
	/**
	 * 公众号的唯一标识
	 */
	public String getAppID();
	/**
	 * 公众号的appsecret
	 */
	public String getSecret();
	/**
	 * 填写第一步获取的code参数
	 */
	public String getCode();
	
}

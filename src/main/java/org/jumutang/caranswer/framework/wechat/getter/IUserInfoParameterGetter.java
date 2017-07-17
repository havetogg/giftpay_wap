package org.jumutang.caranswer.framework.wechat.getter;
/**
 * 拉取用户信息(需scope为 snsapi_userinfo)
 * @author YuanYu
 *
 */
public interface IUserInfoParameterGetter {

	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	public String getAccessToken();
	/**
	 * 用户的唯一标识
	 */
	public String getOpenID();
}

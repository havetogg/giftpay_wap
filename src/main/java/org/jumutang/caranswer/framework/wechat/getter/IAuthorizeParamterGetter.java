package org.jumutang.caranswer.framework.wechat.getter;

import javax.servlet.http.HttpServletResponse;

import org.jumutang.caranswer.framework.wechat.e.EAuthorizeScope;

/**
 * 授权参数获取器
 * @author YuanYu
 *
 */
public interface IAuthorizeParamterGetter {

	/**
	 * 公众号的唯一标识
	 */
	public String getAppID();

	/**
	 * 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
	 */
	public String getRedirectUrL();

	/**
	 * 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo
	 * （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 */
	public EAuthorizeScope getScope();

	/**
	 * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 */
	public String getState();

	/**
	 * 获取HttpServletResponse
	 */
	public HttpServletResponse getHttpServletResponse();

}

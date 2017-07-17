package org.jumutang.caranswer.wechat.web.controller.getter;

import javax.servlet.http.HttpServletResponse;

import org.jumutang.caranswer.framework.wechat.e.EAuthorizeScope;
import org.jumutang.caranswer.framework.wechat.getter.IAuthorizeParamterGetter;

public class AuthorizeParamterGetterImpl implements IAuthorizeParamterGetter {

	private final String wechatAppID;

	private final String wechatAuthorizeRedirectUrL;

	private final HttpServletResponse response;
	
	private final String target;

	public AuthorizeParamterGetterImpl(String wechatAppID, String wechatAuthorizeRedirectUrL,
			HttpServletResponse response, String target) {
		super();
		this.wechatAppID = wechatAppID;
		this.wechatAuthorizeRedirectUrL = wechatAuthorizeRedirectUrL;
		this.response = response;
		this.target = target;
	}

	@Override
	public String getAppID() {
		return wechatAppID;
	}

	@Override
	public String getRedirectUrL() {
		return wechatAuthorizeRedirectUrL;
	}

	@Override
	public EAuthorizeScope getScope() {
		return EAuthorizeScope._SNSAPI_USERINFO;
	}

	@Override
	public String getState() {
		return target;
	}

	@Override
	public HttpServletResponse getHttpServletResponse() {
		return response;
	}

}

package org.jumutang.caranswer.wechat.web.controller.getter;

import org.jumutang.caranswer.framework.wechat.getter.IAccessTokenParamterGetter;

public class AccessTokenParamterGetterImpl implements IAccessTokenParamterGetter {

	private final String wechatAppID;

	private final String wechatAppSecret;

	private final String wechatCode;

	public AccessTokenParamterGetterImpl(String wechatAppID, String wechatAppSecret, String wechatCode) {
		super();
		this.wechatAppID = wechatAppID;
		this.wechatAppSecret = wechatAppSecret;
		this.wechatCode = wechatCode;
	}

	@Override
	public String getAppID() {
		return wechatAppID;
	}

	@Override
	public String getSecret() {
		return wechatAppSecret;
	}

	@Override
	public String getCode() {
		return wechatCode;
	}

}

package org.jumutang.caranswer.wechat.web.controller.getter;

import org.jumutang.caranswer.framework.wechat.getter.IUserInfoParameterGetter;

public class UserInfoParameterGetterImpl implements IUserInfoParameterGetter {

	private final String accessToken;

	private final String openID;

	public UserInfoParameterGetterImpl(String accessToken, String openID) {
		super();
		this.accessToken = accessToken;
		this.openID = openID;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String getOpenID() {
		return openID;
	}

}

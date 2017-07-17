package org.jumutang.caranswer.framework.wechat.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信accessToken
 * 
 * @author YuanYu
 *
 */
public class WeChatAccessToken implements Serializable {

	private static final long serialVersionUID = -3319980048421069155L;

	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	@JSONField(name = "access_token")
	private String accessToken;
	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	@JSONField(name = "expires_in")
	private String expiresIn;
	/**
	 * 用户刷新access_token
	 */
	@JSONField(name = "refresh_token")
	private String refreshToken;
	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	@JSONField(name = "openid")
	private String openID;
	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	@JSONField(name = "scope")
	private String scope;
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	@JSONField(name = "unionid")
	private String unionid;
	/**
	 * 错误码
	 */
	@JSONField(name = "errcode")
	private String errorCode;
	/**
	 * 错误信息
	 */
	@JSONField(name = "errmsg")
	private String errorMess;

	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	public WeChatAccessToken setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	public String getExpiresIn() {
		return expiresIn;
	}

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	public WeChatAccessToken setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}

	/**
	 * 用户刷新access_token
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * 用户刷新access_token
	 */
	public WeChatAccessToken setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	public String getOpenID() {
		return openID;
	}

	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	public WeChatAccessToken setOpenID(String openID) {
		this.openID = openID;
		return this;
	}

	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	public WeChatAccessToken setScope(String scope) {
		this.scope = scope;
		return this;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	public String getUnionid() {
		return unionid;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	public WeChatAccessToken setUnionid(String unionid) {
		this.unionid = unionid;
		return this;
	}

	/**
	 * 错误码
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 错误码
	 */
	public WeChatAccessToken setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	/**
	 * 错误信息
	 */
	public String getErrorMess() {
		return errorMess;
	}

	/**
	 * 错误信息
	 */
	public WeChatAccessToken setErrorMess(String errorMess) {
		this.errorMess = errorMess;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
}

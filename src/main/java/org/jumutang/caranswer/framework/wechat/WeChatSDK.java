package org.jumutang.caranswer.framework.wechat;

import java.io.IOException;
import java.net.URLDecoder;

import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.wechat.getter.IAccessTokenParamterGetter;
import org.jumutang.caranswer.framework.wechat.getter.IAuthorizeParamterGetter;
import org.jumutang.caranswer.framework.wechat.getter.IRefreshTokenParameterGetter;
import org.jumutang.caranswer.framework.wechat.getter.IUserInfoParameterGetter;
import org.jumutang.caranswer.framework.wechat.model.WeChatAccessToken;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.framework.x.StringX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信常用API
 * 
 * @author YuanYu
 *
 */
public final class WeChatSDK {

	private static final Logger _LOGGER = LoggerFactory.getLogger(WeChatSDK.class);

	/**
	 * 用户授权请求地址
	 */
	private static final String _WECHAT_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";
	/**
	 * 获取access_token
	 */
	private static final String _WECHAT_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/**
	 * 刷新access_token
	 */
	private static final String _WECHAT_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	/**
	 * 拉取用户信息
	 */
	private static final String _WECHAT_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

	static {
		_LOGGER.info("初始化wechatSDK!");
		_LOGGER.info(StringX.stringFormat("用户授权请求地址：[0]", _WECHAT_AUTHORIZE));
		_LOGGER.info(StringX.stringFormat("获取access_token请求地址：[0]", _WECHAT_ACCESS_TOKEN));
		_LOGGER.info(StringX.stringFormat("刷新access_token请求地址：[0]", _WECHAT_REFRESH_TOKEN));
		_LOGGER.info(StringX.stringFormat("拉取用户信息请求地址：[0]", _WECHAT_USER_INFO));
		_LOGGER.info("初始化wechatSDK完毕！请检查wechat链接的正确性");
	}

	/**
	 * 像微信发送授权请求！
	 * 
	 * @param authorizeParamterGetter
	 * @param response
	 */
	public static void authorize(IAuthorizeParamterGetter authorizeParamterGetter) {
		_LOGGER.debug("准备像微信发送授权请求！");
		PostOrGet pg = new PostOrGet("utf-8");
		_LOGGER.debug("准备预编译授权请求url！");
		String url = pg.preGetRequestUrl(_WECHAT_AUTHORIZE, ESendHTTPModel._SEND_MODEL_DECODER, new NamedValue("appid", authorizeParamterGetter.getAppID()),
				new NamedValue("redirect_uri", authorizeParamterGetter.getRedirectUrL()),
				new NamedValue("response_type", "code"),
				new NamedValue("scope", authorizeParamterGetter.getScope().getJava_code()),
				new NamedValue("state", authorizeParamterGetter.getState()));
		try {
			_LOGGER.debug(StringX.stringFormat("即将像微信发送授权请求！完整的请求url信息为：[0]", url));
			authorizeParamterGetter.getHttpServletResponse().sendRedirect(url + "#wechat_redirect");
		} catch (IOException e) {
			_LOGGER.error("请求微信授权发生异常!");
			_LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取accessToken
	 * 
	 * @param accessTokenParamterGetter
	 * @return
	 */
	public static WeChatAccessToken accessToken(IAccessTokenParamterGetter accessTokenParamterGetter) {
		WeChatAccessToken accessToken = new WeChatAccessToken();
		PostOrGet pg = new PostOrGet("utf-8");
		String result = pg.sendGet(_WECHAT_ACCESS_TOKEN, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("appid", accessTokenParamterGetter.getAppID()),
				new NamedValue("secret", accessTokenParamterGetter.getSecret()),
				new NamedValue("code", accessTokenParamterGetter.getCode()),
				new NamedValue("grant_type", "authorization_code"));
		_LOGGER.debug(StringX.stringFormat("微信授权请求返回结果：[0],开始封装javabean", result));
		try {
			accessToken = JSONObject.parseObject(result, WeChatAccessToken.class);
		} catch (Exception e) {
			_LOGGER.error("封装javabean失败！");
			_LOGGER.error(e.getMessage(), e);
		}
		return accessToken;
	}

	/**
	 * 刷新用户accesstoken
	 * 
	 * @param refreshTokenParameterGetter
	 * @return
	 */
	public static WeChatAccessToken refreshToken(IRefreshTokenParameterGetter refreshTokenParameterGetter) {
		WeChatAccessToken accessToken = new WeChatAccessToken();
		PostOrGet pg = new PostOrGet("utf-8");
		_LOGGER.debug("准备向微信发送刷新用户accessToken请求！");
		String result = pg.sendGet(_WECHAT_REFRESH_TOKEN, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("appid", refreshTokenParameterGetter.getAppID()),
				new NamedValue("grant_type", "refresh_token"),
				new NamedValue("refresh_token", refreshTokenParameterGetter.getRefreshToken()));
		_LOGGER.debug(StringX.stringFormat("刷新用户accesstoken请求的返回结果为：[0],开始封装javabean", result));
		try {
			accessToken = JSONObject.parseObject(result, WeChatAccessToken.class);
		} catch (Exception e) {
			_LOGGER.error("封装javabean失败！");
			_LOGGER.error(e.getMessage(), e);
		}
		return accessToken;
	}
	/**
	 * 获取用户信息
	 * @param userInfoParameterGetter
	 * @return
	 */
	public static WeChatUserInfo userInfo(IUserInfoParameterGetter userInfoParameterGetter){
		_LOGGER.debug("开始执行像微信拉去用户信息动作！");
		WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
		PostOrGet pg = new PostOrGet("utf-8");
		_LOGGER.debug("向微信发送拉取用户信息请求！");
		String result = pg.sendGet(_WECHAT_USER_INFO, ESendHTTPModel._SEND_MODEL_ENCODE, 
				new NamedValue("access_token", userInfoParameterGetter.getAccessToken()) , 
				new NamedValue("openid", userInfoParameterGetter.getOpenID()) ,
				new NamedValue("lang", "zh_CN"));
		try {
			_LOGGER.debug(StringX.stringFormat("微信返回结果为：[0],开始封装javabean",URLDecoder.decode(result, "utf-8")));
			weChatUserInfo = JSONObject.parseObject(result, WeChatUserInfo.class);
			weChatUserInfo.setNickName(filterEmoji(weChatUserInfo.getNickName()));
		} catch (Exception e) {
			_LOGGER.debug("封装javabean失败！");
			_LOGGER.debug(e.getMessage(), e);
		}
		return weChatUserInfo;
	}
	
    public static String filterEmoji(String source) {
        if (!StringUtils.isEmpty(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
        } else {
            return source;
        }
    }
}

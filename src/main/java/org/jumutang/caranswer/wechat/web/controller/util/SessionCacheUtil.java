package org.jumutang.caranswer.wechat.web.controller.util;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.web.model.SMSSecurityCode;

/**
 * session缓存工具类
 * 
 * @author YuanYu
 *
 */
public final class SessionCacheUtil {

	// 微信用户
	private static final String _WECHAT_USERINFO_SESSION_KEY = "wechatUserInfo";
	// 车答应用户
	private static final String _CURRENT_LOGIN_USERINFO_SESSION_KEY = "currLoginUser";
	// 注册验证码
	private static final String _REGISTER_SECURITY_CODE = "registerSecirityCode";
	// 激活验证码
	private static final String _ACTIVITY_SECURITY_CODE = "activitySecurityCode";

	private SessionCacheUtil() {
	}

	/**
	 * 将微信用户信息存入进HttpSession中
	 * 
	 * @param session
	 *            HttpSession信息
	 * @param weChatUserInfo
	 *            微信用户信息
	 */
	public static final void saveWechatUserInfo(HttpSession session, WeChatUserInfo wechatUserInfo) {
		session.setAttribute(_WECHAT_USERINFO_SESSION_KEY, wechatUserInfo);
	}

	/**
	 * 从HttpSession中获取微信的用户信息
	 * 
	 * @param session
	 *            HttpSession
	 * @return 微信的用户信息
	 */
	public static final WeChatUserInfo loadWechatUserInfo(HttpSession session) {
		return (WeChatUserInfo) session.getAttribute(_WECHAT_USERINFO_SESSION_KEY);
	}

	/**
	 * 从HttpSession中将微信的用户信息移除
	 * 
	 * @param session
	 *            HttpSession
	 */
	public static final void removeWeChatUserInfo(HttpSession session) {
		session.removeAttribute(_WECHAT_USERINFO_SESSION_KEY);
	}

	/**
	 * 将车答应用户信息存入HttpSession中
	 * 
	 * @param session
	 *            HttpSession
	 * @param userInfo
	 *            车答应用户信息
	 */
	public static final void saveCurrentLoginUserInfo(HttpSession session, UserInfo userInfo) {
		session.setAttribute(_CURRENT_LOGIN_USERINFO_SESSION_KEY, userInfo);
	}

	/**
	 * 从HttpSession中获取当前登陆的车答应用户信息
	 * 
	 * @param session
	 *            HttpSession
	 * @return userInfo 当前登陆的用户信息
	 */
	public static final UserInfo loadCurrentLoginUserInfo(HttpSession session) {
		return (UserInfo) session.getAttribute(_CURRENT_LOGIN_USERINFO_SESSION_KEY);
	}

	/**
	 * 从HttpSession中将当前登陆的车答应用户移除
	 * 
	 * @param session
	 *            HttpSession
	 */
	public static final void removeCurrentLoginUserInfo(HttpSession session) {
		session.removeAttribute(_CURRENT_LOGIN_USERINFO_SESSION_KEY);
	}

	/**
	 * 将注册短信验证码存入进HttpSession
	 * 
	 * @param session
	 *            HttpSession
	 * @param securityCode
	 *            短信验证码
	 */
	public static final void saveRegisterSecurityCode(HttpSession session, SMSSecurityCode securityCode) {
		session.setAttribute(_REGISTER_SECURITY_CODE, securityCode);
	}

	/**
	 * 从HttpSession中获取到注册短信验证码
	 * 
	 * @param session
	 *            HttpSession
	 * @return 注册短信验证码信息
	 */
	public static final SMSSecurityCode loadRegisterSecurityCode(HttpSession session) {
		return (SMSSecurityCode) session.getAttribute(_REGISTER_SECURITY_CODE);
	}

	/**
	 * 冲HttpSession中移除注册短信验证码
	 * 
	 * @param session
	 *            HttpSession
	 */
	public static final void removeRegisterSecurityCode(HttpSession session) {
		session.removeAttribute(_REGISTER_SECURITY_CODE);
	}

	/**
	 * 向HttpSession中存储激活验证码信息
	 * @param session
	 * @param smsSecurityCode 验证码
	 */
	public static final void saveActivitySecurityCode(HttpSession session, SMSSecurityCode smsSecurityCode) {
		session.setAttribute(_ACTIVITY_SECURITY_CODE, smsSecurityCode);
	}
	/**
	 * 从HttpSession中获取存储的激活码验证信息
	 * @param session HttpSession
	 * @return SMSSecurityCode对象
	 */
	public static final SMSSecurityCode loadActivitySecurityCode(HttpSession session){
		return  (SMSSecurityCode) session.getAttribute(_ACTIVITY_SECURITY_CODE);
	}

}

package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.wechat.WeChatSDK;
import org.jumutang.caranswer.framework.wechat.model.WeChatAccessToken;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.service.IUserInfoService;
import org.jumutang.caranswer.wechat.web.controller.getter.AccessTokenParamterGetterImpl;
import org.jumutang.caranswer.wechat.web.controller.getter.AuthorizeParamterGetterImpl;
import org.jumutang.caranswer.wechat.web.controller.getter.UserInfoParameterGetterImpl;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 入口controller
 * 
 * @author YuanYu
 *
 */
@Controller
@RequestMapping(value = "/wechat/entranceC", method = { RequestMethod.GET, RequestMethod.POST })
public class EntranceController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(EntranceController.class);

	@Autowired
	private IUserInfoService userInfoService;

	// 微信appid
	@Value(value = "#{propertyFactoryBean['wechat.appID']}")
	private String wechatAppID;
	// 微信appsecret
	@Value(value = "#{propertyFactoryBean['wechat.appSecret']}")
	private String wechatAppSecret;
	// 微信授权回调地址
	@Value(value = "#{propertyFactoryBean['wechat.authorizeRedirectUrL']}")
	private String wechatAuthorizeRedirectUrL;

	/**
	 * 请求微信授权接口，首次加载入口
	 * 
	 * @param response
	 *            HttpServletResponse 由Spring注入，无需理会
	 */
	@RequestMapping(value = "/authorizeM", method = { RequestMethod.GET })
	public void authorize(HttpServletResponse response,
			@RequestParam(value = "target", required = false, defaultValue = "index") String target) {
		try {
			_LOGGER.info("准备开始请求微信授权接口！");
			WeChatSDK.authorize(new AuthorizeParamterGetterImpl(wechatAppID, wechatAuthorizeRedirectUrL, response,target));
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 微信授权回调
	 * 
	 * @param session
	 *            HttpSession 由Spring注入无需理会
	 * @param code
	 *            微信发过来的code信息 无需理会
	 * @return
	 */
	@RequestMapping(value = "/accessTokenM", method = { RequestMethod.GET, RequestMethod.POST })
	public String accessToken(HttpSession session, @RequestParam(value = "code", required = true) String wechatCode,
			@RequestParam(value = "state" , required = false , defaultValue = "index")String state) {
		try {
			_LOGGER.info(StringX.stringFormat("微信授权接口成功触发回调操作，回调我们的接口代入的参数code的值为：[0],准备开始获取accessToken。", wechatCode));
			// 获取accesstoken信息
			WeChatAccessToken accessToken = WeChatSDK
					.accessToken(new AccessTokenParamterGetterImpl(wechatAppID, wechatAppSecret, wechatCode));
			_LOGGER.info(StringX.stringFormat("调用获取微信授权accessToken接口的返回值为：[0],准备请求微信获取userinfo接口！",
					accessToken.toJsonString()));
			// 抓去用户信息
			WeChatUserInfo wechatUserInfo = WeChatSDK
					.userInfo(new UserInfoParameterGetterImpl(accessToken.getAccessToken(), accessToken.getOpenID()));
			_LOGGER.info(StringX.stringFormat("调用获取微信userinfo接口的返回值为：[0]", wechatUserInfo.toJsonString()));
			// 只要授权通过获取到了微信的用户信息，不管三七二十一，先将用户的微信信息存入进session中！
			SessionCacheUtil.saveWechatUserInfo(session, wechatUserInfo);
			ContextResult<UserInfo> cr = userInfoService.loginValidation(accessToken.getOpenID());
			// 判断是否 注册用户，如若是 用户信息也需要缓存
			if (null != cr.getResultObject()) {
				SessionCacheUtil.saveCurrentLoginUserInfo(session, cr.getResultObject());
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}
		return "redirect:../../"+state+".jsp";
	}

}

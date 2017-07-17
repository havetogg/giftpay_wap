package org.jumutang.caranswer.wechat.web.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.compoent.IJuHeComponent;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.TELX;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.web.model.SMSSecurityCode;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IUserInfoService;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户controller
 * 
 * @author YuanYu
 *
 */
@Controller
@RequestMapping(value = "/wechat/userC", method = { RequestMethod.GET, RequestMethod.POST })
public class UserInfoController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	private IUserInfoService userService;
	@Autowired
	private IJuHeComponent juHeComponent;

	/**
	 * 用户注册接口
	 * 
	 * @param session
	 *            HttpSession 由Spring注入，无需理会
	 * @param tel
	 *            用户手机号
	 * @param securityCode
	 *            验证码
	 * @param recommendCode
	 *            推荐码，可不传
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/registerM", method = { RequestMethod.GET, RequestMethod.POST })
	public String register(HttpSession session, @RequestParam(value = "tel", required = true) String tel,
			@RequestParam(value = "securityCode", required = true) String securityCode,
			@RequestParam(value = "recommendCode", required = false, defaultValue = "") String recommendCode) {
		_LOGGER.info(StringX.stringFormat("注册接口,手机号:[0],验证码:[1],推荐码:[2]", tel, securityCode, recommendCode));
		ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
		SMSSecurityCode smsSecurityCode = SessionCacheUtil.loadRegisterSecurityCode(session);
		WeChatUserInfo wechatUserInfo;
		try {
			if (smsSecurityCode == null) {
				cr.setCodeWithMess(ErrorCodePools._FAIL_10003);
				_LOGGER.info("未发送验证码!");
				return cr.toJsonString();
			}
			if (new Date().getTime() - smsSecurityCode.getSecurityTime().getTime() > smsSecurityCode.getValidMinute()
					* 60 * 1000) {
				cr.setCodeWithMess(ErrorCodePools._FAIL_10005);
				return cr.toJsonString();
			}
			if (!securityCode.equals(smsSecurityCode.getSecurityCode())) {
				_LOGGER.info(StringX.stringFormat("验证码不正确！请求的 ：[0],session中的:[1]", securityCode,
						smsSecurityCode.getSecurityCode()));
				cr.setCodeWithMess(ErrorCodePools._FAIL_10002);
				return cr.toJsonString();
			}

			wechatUserInfo = SessionCacheUtil.loadWechatUserInfo(session);
			if (wechatUserInfo == null) {
				cr.setCodeWithMess(ErrorCodePools._FAIL_10004);
				_LOGGER.info("用户微信授权信息失效!");
				return cr.toJsonString();
			}
			cr = userService.registerUser(wechatUserInfo,
					new UserInfo().setUserTel(tel).setRecommendCode(recommendCode));

			SessionCacheUtil.saveCurrentLoginUserInfo(session, cr.getResultObject());
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCodeWithMess(ErrorCodePools._FAIL_0);
		}
		return cr.toJsonString();
	}

	/**
	 * 获取短信验证码 发送短信
	 * 
	 * @param session
	 *            HttpSession 由Spring负责注入，无需理会
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRegisterSecurityCodeM", method = { RequestMethod.GET, RequestMethod.POST })
	public String getRegisterSecurityCode(HttpSession session,
			@RequestParam(value = "tel", required = true) String tel) {
		ContextResult<String> cr = new ContextResult<String>();
		try {
			_LOGGER.info(StringX.stringFormat("用户请求获取验证码接口,用户的手机号为:[0]", TELX.encryption(tel)));
			String securityCode = TELX.generateSecurityCode();
			_LOGGER.info(StringX.stringFormat("系统生成的验证码信息为：[0],准备向聚合发送请求信息！", securityCode));
			cr = juHeComponent.sendSMSSecurityCode(tel, securityCode);
			_LOGGER.info(StringX.stringFormat("聚合组件返回的结果集为：[0]", cr.toJsonString()));
			if (ContextContast._TRUE_CODE.equals(cr.getCode())) {
				_LOGGER.info("聚合组件返回成功！短信成功发出！将验证码存入HttpSession中！");
				SessionCacheUtil.saveRegisterSecurityCode(session, new SMSSecurityCode(securityCode, new Date(), 1));
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}
		return cr.toJsonString();
	}

	/**
	 * 验证当前登陆的微信用户是否为注册用户
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validationWechatIsRegisterM", method = { RequestMethod.GET, RequestMethod.POST })
	public String validationWechatIsRegister(HttpSession session) {
		ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
		try {
			WeChatUserInfo weChatUserInfo = SessionCacheUtil.loadWechatUserInfo(session);
			cr = userService.loginValidation(weChatUserInfo.getOpenID());
			if (ErrorCodePools._SUCCESS_1.equals(cr.getCode())) {
				cr.getResultObject().setUserTel(TELX.encryption(cr.getResultObject().getUserTel()));
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}
		return cr.toJsonString();
	}

	/**
	 * 获取 第三方账户激活的验证码信息
	 * 
	 * @param session
	 *            HttpSession 由Spring负责注入，无需理会
	 * @param tel
	 *            用户手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getActivitySecurityCodeM", method = { RequestMethod.GET, RequestMethod.POST })
	public String getActivitySecurityCode(HttpSession session,
			@RequestParam(value = "tel", required = true) String tel) {
		ContextResult<String> cr = new ContextResult<String>();
		try {
			_LOGGER.info(StringX.stringFormat("用户请求获取验证码接口,用户的手机号为:[0]", TELX.encryption(tel)));
			String securityCode = TELX.generateSecurityCode();
			_LOGGER.info(StringX.stringFormat("系统生成的验证码信息为：[0],准备向聚合发送请求信息！", securityCode));
			cr = juHeComponent.sendSMSSecurityCode(tel, securityCode);
			_LOGGER.info(StringX.stringFormat("聚合组件返回的结果集为：[0]", cr.toJsonString()));
			if (ContextContast._TRUE_CODE.equals(cr.getCode())) {
				_LOGGER.info("聚合组件返回成功！短信成功发出！将验证码存入HttpSession中！");
				SessionCacheUtil.saveActivitySecurityCode(session, new SMSSecurityCode(securityCode, new Date(), 1));
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}
		return cr.toJsonString();
	}

	/**
	 * 第三方用户激活接口
	 * 
	 * @param session
	 *            HttpSession 由Spring注入，无需理会
	 * @param tel
	 *            用户手机号
	 * @param securityCode
	 *            验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/activateUserInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String activateUserInfo(HttpSession session, @RequestParam(value = "tel", required = true) String tel,
			@RequestParam(value = "securityCode", required = true) String securityCode) {
		ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
		try {
			_LOGGER.info(StringX.stringFormat("第三方平台激活验证 验证码,手机号:[0],验证码:[1]", tel, securityCode));
			SMSSecurityCode smsSecurityCode = SessionCacheUtil.loadActivitySecurityCode(session);

			if (smsSecurityCode == null) {
				cr.setCode("0").setMess("验证码错误!");
				_LOGGER.info("验证码错误!");
				return cr.toJsonString();
			}
			if (!securityCode.equals(smsSecurityCode.getSecurityCode())) {
				_LOGGER.info(StringX.stringFormat("验证码不正确！请求的 ：[0],session中的:[1]", securityCode,
						smsSecurityCode.getSecurityCode()));
				cr.setCode("0").setMess("验证码不正确！");
				return cr.toJsonString();
			}

			WeChatUserInfo wechatUserInfo = SessionCacheUtil.loadWechatUserInfo(session);
			if (wechatUserInfo == null) {
				cr.setCode("0").setMess("用户微信授权信息失效!");
				_LOGGER.info("用户微信授权信息失效!");
				return cr.toJsonString();
			}
			_LOGGER.info(StringX.stringFormat("抓取微信用户 授权信息:[0]", wechatUserInfo.toJsonString()));
			cr = userService.activateUserInfo(wechatUserInfo, tel);

		} catch (Exception e) {
			e.printStackTrace();
			_LOGGER.error(StringX.stringFormat("验证码激活错误:[1]", e));
			cr.setCode("0").setMess("激活用户失败!");
		}
		return cr.toJsonString();
	}

	/**
	 * 获取微信信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryWetInfo" , method = {RequestMethod.GET , RequestMethod.POST})
	public String queryWetInfo(HttpSession session)
	{
	    ContextResult<WeChatUserInfo> cr = new ContextResult<WeChatUserInfo>();
	    try
        {
	        WeChatUserInfo wechatUserInfo = SessionCacheUtil.loadWechatUserInfo(session);
	        UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
	        _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
	        wechatUserInfo.setuTel(userInfo.getUserTel());
	        _LOGGER.info(StringX.stringFormat("微信授权信息:[0]", wechatUserInfo.toJsonString()));
	        cr.setCodeWithMess(ErrorCodePools._SUCCESS_1).setResultObject(wechatUserInfo);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(),e);
            cr.setCodeWithMess(ErrorCodePools._FAIL_0);
        }
	    return cr.toJsonString();
	}

}

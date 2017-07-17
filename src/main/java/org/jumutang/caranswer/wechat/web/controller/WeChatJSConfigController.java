package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.util.WXShareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/wechat/weChatJSConfigC", method = { RequestMethod.GET, RequestMethod.POST })
public class WeChatJSConfigController {

	@Value(value = "#{propertyFactoryBean['wechat.appID']}")
	private String appID;

	@Value(value = "#{propertyFactoryBean['wechat.appSecret']}")
	private String appSecret;

	private static final Logger _LOGGER = LoggerFactory.getLogger(WeChatJSConfigController.class);

	/**
	 * 微信分享
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param shareUrl
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWeCharJSConfigM", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWeCharJSConfig(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "currUrl", required = true) String currUrl) {
		ContextResult<String> cr = new ContextResult<String>();
		_LOGGER.info(StringX.stringFormat("前端提交的currURL信息为：[0]", currUrl));
		try {
			_LOGGER.info("开始获取微信分享的conf信息！");
			_LOGGER.info(StringX.stringFormat("appID:[0],appSecret:[1]", appID,appSecret));
			WXShareUtil shareUtil = WXShareUtil.getInstance(appID.trim(), appSecret.trim());
			String config = shareUtil.genJSSDKConf(currUrl);
			_LOGGER.info(StringX.stringFormat("成功获取到的微信分享的conf为：[0]", config));
			cr.setCode("1").setMess("获取微信shareconfig成功！").setResultObject(config);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCodeWithMess(ErrorCodePools._FAIL_0);
		}
		return cr.toJsonString();
	}

}

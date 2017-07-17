package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.RedPickInfo;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IRedPickInfoService;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 红包提取信息 [一句话功能简述]
 * <p>
 * [功能详细描述]
 * <p>
 * 
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
@RequestMapping(value = "/wechat/redPickInfoC", method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class RedPickInfoController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(RedPickInfoController.class);

	@Autowired
	private IRedPickInfoService redPickInfoService;

	/**
	 * 加油红包提现记录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/redOutRecords", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String redOutRecords(HttpSession session) {
		ContextResult<RedPickInfo> cr = new ContextResult<RedPickInfo>();
		try {
			UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
			_LOGGER.info(StringX.stringFormat("当前登录用户信息:[0]", userInfo.toJsonString()));
			cr = redPickInfoService.loadRedPickInfo(userInfo.getUserID());
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCodeWithMess(ErrorCodePools._FAIL_0);
		}

		return cr.toJsonString();
	}
}


package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.compoent.model.OFPayOrderInfo;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IRedInfoService;
import org.jumutang.caranswer.wechat.viewmodel.MyRedInfoView;
import org.jumutang.caranswer.wechat.viewmodel.RedInfoView;
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
 * 加油红包管理 [一句话功能简述]
 * <p>
 * [功能详细描述]
 * <p>
 * 
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
@RequestMapping(value = "/wechat/redInfoC", method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class RedInfoController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RedInfoController.class);

	@Autowired
	private IRedInfoService redInfoService;

	/**
	 * 我的加油红包首页
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/redIndex", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String redInfoIndex(HttpSession session) {
		ContextResult<MyRedInfoView> cr = new ContextResult<MyRedInfoView>();
		try {
			UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
			_LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
			cr = redInfoService.queryAllRedMoney(userInfo.getUserID());
			_LOGGER.info(StringX.stringFormat("我的加油红包首页:[0]", cr.toJsonString()));

		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess("我的加油红包首页查询失败!");
		}
		return cr.toJsonString();
	}

	/**
	 * 查询红包领取记录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/loadRedGetInfo", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String loadRedGetInfo(HttpSession session) {
		ContextResult<RedInfoView> cr = new ContextResult<RedInfoView>();
		try {
			UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);

			cr = redInfoService.loadRedGetInfo(userInfo.getUserID());
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess("加油红包领取记录查询失败!");
		}
		_LOGGER.info(StringX.stringFormat("查询红包领取记录接口最终返回给前端的数据为：[0]", cr.toJsonString()));
		return cr.toJsonString();
	}

	/**
	 * 查看可提现的红包列表
	 * 
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/canExtractRed", method = { RequestMethod.GET, RequestMethod.POST })
	public String canExtractRed(HttpSession session) {
		ContextResult<RedInfo> cr = new ContextResult<RedInfo>();
		try {
			_LOGGER.info("查看可提现的红包列表!");
			UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
			_LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
			cr = redInfoService.canExtractRed(userInfo.getUserID());
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess("查看可提现的红包列表错误!");
		}
		return cr.toJsonString();
	}

	/**
	 * 加油红包提现
	 * 
	 * @param session
	 * @param redids
	 *            红包字符串 ,分割 123,456,789
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/extractRed", method = { RequestMethod.GET, RequestMethod.POST })
	public String extractRed(HttpSession session, @RequestParam(value = "redids", required = true) String redids) {
		ContextResult<OFPayOrderInfo> cr = new ContextResult<OFPayOrderInfo>();
		try {
			_LOGGER.info(StringX.stringFormat("红包id:[0]", redids));
			UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
			_LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
			cr = redInfoService.applyPickRedInfo(userInfo.getUserID(), redids.split(","));
		} catch (Exception e) {
			cr.setCode(ErrorCodePools._FAIL_0).setMess(ErrorCodePools.getMess(ErrorCodePools._FAIL_0));
		}

		return cr.toJsonString();
	}

	/**
	 * 油卡充值 回调接口
	 * 
	 * @param session
	 * @param ret_code
	 *            充值后状态，1代表成功，9代表撤消
	 * @param sporder_id
	 *            SP订单号
	 * @param ordersuccesstime
	 *            处理时间
	 * @param err_msg
	 *            失败原因
	 * @param gascard_code
	 *            官方订单号
	 */
	@RequestMapping(value = { "/callback" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void callback(HttpSession session, @RequestParam("ret_code") String ret_code,
			@RequestParam("sporder_id") String sporder_id, @RequestParam("ordersuccesstime") String ordersuccesstime,
			@RequestParam("err_msg") String err_msg, @RequestParam("gascard_code") String gascard_code) {
		_LOGGER.info("ret_code=" + ret_code + " , sporder_id=" + sporder_id + " ,ordersuccesstime= " + ordersuccesstime
				+ " ,err_msg=" + err_msg + " ,gascard_code=" + gascard_code);

		try {
			redInfoService.ofCallBack(ret_code, sporder_id, ordersuccesstime, err_msg, gascard_code);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
		}

	}

}

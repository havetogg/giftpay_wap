package org.jumutang.giftpay.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.*;
import org.jumutang.giftpay.tools.DateFormatUtil;
import org.jumutang.giftpay.tools.UniqueX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "/oil")
public class OilController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(OilController.class);

	@Autowired
	private IUserOilInfoService userOilInfoService;

	@Autowired
	private BalanceServiceI balanceServiceI;

	@Autowired
	private IOilRecordService oilRecordService;

	@Autowired
	private IOilSubRecordService oilSubRecordService;

	@Autowired
	private PayInfoServiceI payInfoServiceI;

	@Autowired
	private OilServiceService oilServiceService;

	/**
	 * 查询用户加油信息并返回
	 */
	@ResponseBody
	@RequestMapping("/queryUserOilInfo")
	public String queryUserInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//session.setAttribute("userId","b65c1fcd2bb146008b9e6d5bf5f2dfaf");
		//session.setAttribute("openId","oUAows5hD5KcYDUwuY8DB9zp2g9s");
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		if (userIdObj == null||openIdObj == null) {
			_LOGGER.info("参数错误");
		} else {
			UserOilInfoModel userOilInfoModel = new UserOilInfoModel();
			userOilInfoModel.setUserid(userIdObj.toString());
			_LOGGER.info("开始查询用户，userId：" + userOilInfoModel.getUserid());
			UserOilInfoModel resultUserOilInfo = userOilInfoService.queryUserOilInfo(userOilInfoModel);
			if (resultUserOilInfo == null) {// 查无用户加油信息，新增一条
				_LOGGER.info("查无此用户，新增用户加油信息");
				userOilInfoService.insertUserOilInfo(userOilInfoModel);
				result.put("data", userOilInfoModel);
			} else
				result.put("data", resultUserOilInfo);
			result.put("success", true);
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 查询加油记录列表
	 */
	@ResponseBody
	@RequestMapping("/queryOilRecordList")
	public String queryOilRecordList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj == null||openIdObj == null) {
			result.put("success", false);
		}else{
			OilRecordModel oilRecordModel = new OilRecordModel();
			oilRecordModel.setUserid(userIdObj.toString());
			List<OilRecordModel> resultList = oilRecordService.queryOilRecordModelList(oilRecordModel);
			if (resultList != null && resultList.size() > 0) {
				result.put("data", resultList);
			}else{
				result.put("data", null);
			}
			result.put("success", true);
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 转账到零钱账户
	 */
	@Transactional(rollbackFor=Exception.class)
	@ResponseBody
	@RequestMapping("/transferAccount")
	public String transferAccount(HttpServletRequest request, String amount,String randomString) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		_LOGGER.info("查询用户账户信息");
		HttpSession session = request.getSession();
		Object oilRandomStr = session.getAttribute("oilRandomString");
		if(oilRandomStr == null||randomString == null||"".equals(randomString)||!randomString.equals(String.valueOf(oilRandomStr))){
			result.put("errMsg", "验证错误");
		}else{
			session.removeAttribute("oilRandomString");
			Object userIdObj = session.getAttribute("userId");
			Object openIdObj = session.getAttribute("openId");
			if(userIdObj == null||openIdObj == null){
				result.put("errMsg", "用户未登录");
			}else if(Double.parseDouble(amount) <= 0) {
				result.put("errMsg", "转入零钱金额必须大于0");
			}else{
				String userId = String.valueOf(userIdObj);
				BalanceModel balanceModel = new BalanceModel();
				balanceModel.setAccountId(userId);
				List<BalanceModel> resultBalanceList = balanceServiceI.queryBalances(balanceModel);
				UserOilInfoModel userOilInfoModel = new UserOilInfoModel();
				userOilInfoModel.setUserid(userId);
				userOilInfoModel = userOilInfoService.queryUserOilInfo(userOilInfoModel);
				if (resultBalanceList != null && resultBalanceList.size() > 0 && userOilInfoModel!=null && (Double.parseDouble(userOilInfoModel.getBalance()) >= Double.parseDouble(amount))) {
					_LOGGER.info("查询用户加油信息并更新余额");
					userOilInfoModel.setBalance(Double.parseDouble(userOilInfoModel.getBalance()) - Double.parseDouble(amount) + "");
					int userOilInfoServiceResult = userOilInfoService.updateUserOilInfo(userOilInfoModel);
					if(userOilInfoServiceResult == 0){
						throw new RuntimeException("出错了");
					}

					double balance = resultBalanceList.get(0).getBalanceNumber();
					balanceModel.setBalanceNumber(balance + Double.parseDouble(amount));
					_LOGGER.info("更新用户账户信息：" + balanceModel);
					int balanceServiceResult = balanceServiceI.updateBalance(balanceModel);
					if(balanceServiceResult == 0){
						throw new RuntimeException("出错了");
					}

					PayInfoModel payInfoModel = new PayInfoModel();
					payInfoModel.setDealId(UniqueX.randomUUID());
					payInfoModel.setAccountId(userId);
					payInfoModel.setBusinessInfo("有礼付易加油");
					payInfoModel.setDealInfo("转入余额");
					payInfoModel.setDealMoney(Double.parseDouble(amount));
					payInfoModel.setDealRealMoney(Double.parseDouble(amount));
					payInfoModel.setDealTime(DateFormatUtil.formateString());
					payInfoModel.setDealType(new Short("5"));// 转入余额
					payInfoModel.setDealState(new Short("1"));// 成功
					_LOGGER.info("更新用户支付记录信息：" + payInfoModel);
					int payInfoServiceResult = payInfoServiceI.insertPayInfo(payInfoModel);
					if(payInfoServiceResult == 0){
						throw new RuntimeException("出错了");
					}
					result.put("success", true);
				}else{
					result.put("errMsg", "转入余额不足");
				}
			}
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 查询用户余额
	 *
	 */
	/*@ResponseBody
	@RequestMapping("/queryBalance")
	public String queryBalance(HttpServletRequest request, String userId,int num) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj == null||openIdObj == null) {
			result.put("success", false);
		}else{
			result.put("success", false);
			BalanceModel balanceModel = new BalanceModel();
			balanceModel.setAccountId(userId);
			List<BalanceModel> resultBalanceList = balanceServiceI.queryBalances(balanceModel);
			if (resultBalanceList != null && resultBalanceList.size() > 0) {
				double balanceNumber = resultBalanceList.get(0).getBalanceNumber();
				OilServiceModel oilServiceModel = oilServiceService.queryOilService();
				if(oilServiceModel!=null&&oilServiceModel.getPayAmount()!=0.0d){
					double doubleAmount = oilServiceModel.getPayAmount()*num;//用户实付金额=单价*份数
					if (doubleAmount > balanceNumber) {// 余额不够
						_LOGGER.info("余额不够，前端准备跳转页面发起支付");
					} else {
						_LOGGER.info("余额够，直接扣，更新余额");
						balanceModel.setBalanceNumber(balanceNumber - doubleAmount);
						balanceServiceI.updateBalance(balanceModel);

						PayInfoModel payInfoModel = new PayInfoModel();
						payInfoModel.setDealId(UniqueX.randomUUID());
						payInfoModel.setAccountId(userId);
						if(session.getAttribute("entryType")!=null){
							if((int)session.getAttribute("entryType")==1){
								payInfoModel.setBusinessInfo("有礼付易加油-光大途径");
							}else if((int)session.getAttribute("entryType")==0){
								payInfoModel.setBusinessInfo("有礼付易加油");
							}else if((int)session.getAttribute("entryType")==2){
								payInfoModel.setBusinessInfo("有礼付易加油-招行途径");
							}
						}else{
							payInfoModel.setBusinessInfo("有礼付易加油-未知途径");
						}
						payInfoModel.setDealInfo("余额支付");
						payInfoModel.setDealMoney(doubleAmount);
						payInfoModel.setDealRealMoney(doubleAmount);
						payInfoModel.setDealTime(DateFormatUtil.formateString());
						payInfoModel.setDealType(new Short("3"));// 支付
						payInfoModel.setDealState(new Short("1"));// 成功

						_LOGGER.info("新增扣款记录:" + payInfoModel.toString());
						payInfoServiceI.insertPayInfo(payInfoModel);

						result.put("success", true);
					}
				}
			}
		}
		return JSON.toJSONString(result);
	}*/

	/**
	 * 插入购买加油记录
	 */
	/*@ResponseBody
	@RequestMapping("/insertOilRecord")
	public String insertOilRecord(String userId,String openId,Integer num) {
		Map<String, Object> result = new HashMap<>();
		if (userId == null) {
			_LOGGER.info("新增加油记录参数错误");
			result.put("success", false);
			return JSON.toJSONString(result);
		} else {
			UserOilInfoModel userOilInfoModel = new UserOilInfoModel();
			userOilInfoModel.setUserid(userId);
			UserOilInfoModel resultUserOilInfo = userOilInfoService.queryUserOilInfo(userOilInfoModel);
			if (resultUserOilInfo == null) {
				_LOGGER.info("查无该用户信息：" + userOilInfoModel.toString());
				result.put("success", false);
				return JSON.toJSONString(result);
			} else {
				OilServiceModel oilServiceModel = oilServiceService.queryOilService();
				if(oilServiceModel!=null&&oilServiceModel.getPayAmount()!=0.0d){
					double tAmount = oilServiceModel.getTotalAmount() * num;
					double pAmount = oilServiceModel.getPayAmount() * num;
					resultUserOilInfo.setWaitsendmoney(Double.parseDouble(resultUserOilInfo.getWaitsendmoney())
							+ tAmount + "");// 更新待发送总金额
					resultUserOilInfo.setTotalsavemoey(Double.parseDouble(resultUserOilInfo.getTotalsavemoey())
							+ tAmount
							- pAmount + "");// 更新累计优惠金额
					_LOGGER.info("更新用户加油信息：" + resultUserOilInfo.toString());
					userOilInfoService.updateUserOilInfo(resultUserOilInfo);


					_LOGGER.info("循环插入用户加油记录信息");
					for(int i = 0; i < num; i ++){
						OilRecordModel oilRecordModel = new OilRecordModel();
						oilRecordModel.setId(UniqueX.randomUUID());
						oilRecordModel.setUserid(userId);
						oilRecordModel.setOpenid(openId);
						oilRecordModel.setPayamount(Double.toString(oilServiceModel.getPayAmount()));
						oilRecordModel.setTotalamount(Double.toString(oilServiceModel.getTotalAmount()));
						oilRecordModel.setTermsurplus(String.valueOf(oilServiceModel.getCycle()));
						oilRecordModel.setCycle(String.valueOf(oilServiceModel.getCycle()));
						oilRecordModel.setDiscount(String.valueOf(oilServiceModel.getDiscount()));
						oilRecordModel.setMonthamount(String.valueOf(oilServiceModel.getMonthAmount()));
						oilRecordService.insertOilRecord(oilRecordModel);
					}
					Map<String,Object> param = new HashMap<>();
					param.put("num",num);
					oilServiceService.updateOilService(param);
				}

			}
		}
		return JSON.toJSONString(result);
	}*/

	//通过一码付前奏
	@RequestMapping("/getPayPre")
	public String getPayPre(HttpServletRequest request, int num) {
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj != null||openIdObj != null) {
			Map<String, Object> result = new HashMap<>();
			result.put("success", false);
			OilServiceModel oilServiceModel = oilServiceService.queryOilService();
			if (oilServiceModel != null && oilServiceModel.getPayAmount() != 0.0d) {
				double doubleAmount = oilServiceModel.getPayAmount() * num;//用户实付金额=单价*份数
				request.setAttribute("oilDealMoney", doubleAmount);
				request.setAttribute("oilPayMoney", doubleAmount);
				request.setAttribute("num", num);
			}
			return "forward:/giftpay/oilRecharge/preOrder.htm";
		}
		return null;
	}

	/**
	 * 查询加油记录信息
	 *
	 */
	@ResponseBody
	@RequestMapping("/queryOilRecord")
	public String queryOilRecord(HttpServletRequest request, String recordId) {
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj != null||openIdObj != null) {
			Map<String, Object> result = new HashMap<>();
			OilRecordModel oilRecordModel = new OilRecordModel();
			oilRecordModel.setId(recordId);
			oilRecordModel = oilRecordService.queryOilRecordModel(oilRecordModel);
			if (oilRecordModel != null) {
				result.put("data", oilRecordModel);
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errMsg", "查询加油记录信息失败");
			}
			return JSON.toJSONString(result);
		}
		return null;
	}

	/**
	 * 查询加油记录明细信息
	 *
	 */
	@ResponseBody
	@RequestMapping("/queryOilSubRecord")
	public String queryOilSubRecord(HttpServletRequest request, String recordId) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj != null||openIdObj != null) {
			OilSubRecordModel oilSubRecordModel = new OilSubRecordModel();
			oilSubRecordModel.setRecordid(recordId);
			oilSubRecordModel.setUserid(userIdObj.toString());
			List<OilSubRecordModel> list = oilSubRecordService.queryOilSubRecordModelList(oilSubRecordModel);
			if (list != null) {
				result.put("data", list);
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errMsg", "查无数据");
			}
			return JSON.toJSONString(result);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/queryPayInfoList")
	public String queryPayInfoList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj != null||openIdObj != null) {
			PayInfoModel payInfoModel = new PayInfoModel();
			payInfoModel.setAccountId(userIdObj.toString());
			payInfoModel.setBusinessInfo("有礼付易加油");
			List<PayInfoModel> list = payInfoServiceI.queryPayInfosForOil(payInfoModel);
			if (list != null) {
				result.put("data", list);
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errMsg", "查无数据");
			}
			return JSON.toJSONString(result);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/queryOilService")
	public String queryOilService(){
		Map<String, Object> result = new HashMap<>();
		OilServiceModel oilServiceModel = oilServiceService.queryOilService();
		if (oilServiceModel != null) {
			result.put("data", oilServiceModel);
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("errMsg", "查无数据");
		}
		return JSON.toJSONString(result);
	}

	@ResponseBody
	@RequestMapping("/queryOilServiceList")
	public String queryOilServiceList(){
		Map<String, Object> result = new HashMap<>();
		List<OilServiceModel> oilServiceModelList = oilServiceService.queryOilServiceList();
		if (oilServiceModelList != null&&oilServiceModelList.size() > 0) {
			result.put("data", oilServiceModelList);
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("errMsg", "查无数据");
		}
		return JSON.toJSONString(result);
	}

	//查询用户零钱
	@ResponseBody
	@RequestMapping("/queryUserMoney")
	public String queryUserMoney(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		if(session.getAttribute("userId") != null){
			BalanceModel balanceModel = new BalanceModel();
			balanceModel.setAccountId(session.getAttribute("userId").toString());
			List<BalanceModel> resultBalanceList = balanceServiceI.queryBalances(balanceModel);
			if (resultBalanceList != null && resultBalanceList.size() > 0) {
				double balanceNumber = resultBalanceList.get(0).getBalanceNumber();
				result.put("success", true);
				result.put("data", balanceNumber);
			}else{
				result.put("success", false);
				result.put("errMsg", "获取用户信息失败");
			}
		}else{
			result.put("success", false);
			result.put("errMsg", "获取用户信息失败");
		}
		return JSON.toJSONString(result);
	}

	@ResponseBody
	@RequestMapping("/getOilRandomString")
	public String getOilRandomString(HttpServletRequest request){
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("result",false);
		HttpSession session = request.getSession();
		Object userIdObj = session.getAttribute("userId");
		Object openIdObj = session.getAttribute("openId");
		if (userIdObj != null||openIdObj != null) {
			Object oilTransferObj = session.getAttribute("oilTransferTime");
			if(oilTransferObj == null){
				session.setAttribute("oilTransferTime",new Date());
			}else{
				Date oilTransferDate = (Date)oilTransferObj;
				Date currentDate = new Date();
				int interval = (int)(currentDate.getTime() - oilTransferDate.getTime())/1000;
				session.setAttribute("oilTransferTime",new Date());
				if(interval < 20){
					returnMap.put("result",false);
					returnMap.put("data","请求次数过多,请稍后重试");
					return JSON.toJSONString(returnMap);
				}
			}
			String randomString = getRandomString(10);
			session.setAttribute("oilRandomString",randomString);
			returnMap.put("result",true);
			returnMap.put("data",randomString);
			return JSON.toJSONString(returnMap);
		}
		return JSON.toJSONString(returnMap);
	}

	//获取随机字符串
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}

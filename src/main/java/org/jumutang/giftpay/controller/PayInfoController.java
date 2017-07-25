/*
package org.jumutang.giftpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.jumutang.caranswer.wechat.web.controller.CarAnswerIndexController;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.PayInfoServiceI;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.ResResult;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

*/
/**
 * 交易记录控制层
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 *//*

@Controller
@Transactional
public class PayInfoController {

    private static final Logger _LOGGER = LoggerFactory.getLogger(PayInfoController.class);
	@Autowired
	private PayInfoServiceI payInfoServiceI;
	@Autowired
	private BalanceServiceI balanceServiceI;
	
	*/
/**
	 * 查询交易信息
	 * @param payInfoModel
	 * @return
	 *//*

	@ResponseBody
	@RequestMapping("/queryPayInfo")
	public String queryPayInfo(PayInfoModel payInfoModel,HttpServletRequest request){
	HttpSession session = request.getSession();
	String userId = request.getParameter("userId");
		String orderId = request.getParameter("orderId");
	if(userId==null){
		userId = (String)session.getAttribute("userId");
	}
	
	Map<String,Object> map = new HashMap<String, Object>();
	if(userId==null){
		map.put("state", 1);
	}
	payInfoModel.setAccountId(userId);
		payInfoModel.setOrderNo(orderId);
	List<PayInfoModel> list = 	payInfoServiceI.queryPayInfos(payInfoModel);
	
	if(list.size()==0){
		map.put("state", 2);
		map.put("message", "暂无数据");
		return JSON.toJSONString(map);
	}else{
		map.put("state", 0);
		map.put("list", list);
		return JSON.toJSONString(map);
	}
	
	}
	
	*/
/**
	 * 生成交易
	 * @param payInfoModel
	 * @return
	 *//*

	@ResponseBody
		@RequestMapping("/savePayInfo")
	public String insertPayInfo(PayInfoModel payInfoModel,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Map<String,Object> map = new HashMap<String,Object>();
		String userId = request.getParameter("userId");
		if(userId==null){
			userId= (String)session.getAttribute("userId");
		}
		
		payInfoModel.setAccountId(userId);
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(userId);
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		BalanceModel resultModel = list.get(0);
		//提现或是支付
		int result1= 0 ;
		if(payInfoModel.getDealType()==1){
			String payPwd = request.getParameter("payPassword");
			String mdPayPwd = MD5X.getLowerCaseMD5For32(payPwd);
			if(userId==null){
				map.put("message","请重新登陆");
			}
			if(!mdPayPwd.equals(resultModel.getPayPassword())){
				map.put("state", 2);
				map.put("message","支付密码错误");
				return JSON.toJSONString(map);
			}
			if(resultModel.getBalanceNumber()>payInfoModel.getDealMoney()){
				Double newBalance = resultModel.getBalanceNumber()-payInfoModel.getDealMoney();
				balanceModel.setBalanceNumber(newBalance);
				result1 = balanceServiceI.updateBalance(balanceModel);
				if(result1 == 0 ){
					map.put("state", 3);
					map.put("message", "提现失败");
					return JSON.toJSONString(map);
				}else{
					//设置状态再提现中
					payInfoModel.setDealState(new Short("3"));
				}
			}else{
				map.put("state",1 );
				map.put("message", "余额不足");
				return JSON.toJSONString(map);
			}
			
		}
		//充值
		if(payInfoModel.getDealType()==2){
			Double newBalance = resultModel.getBalanceNumber()+payInfoModel.getDealMoney();
			balanceModel.setBalanceNumber(newBalance);
			result1 = balanceServiceI.updateBalance(balanceModel);
			if(result1 == 0 ){
				map.put("state", 3);
				map.put("message", "提现失败");
				return JSON.toJSONString(map);
			}else{
				//设置状态再提现中
				payInfoModel.setDealState(new Short("1"));
			}
			
		}
		
		if(payInfoModel.getDealType() == 3){
			String payPwd = request.getParameter("payPassword");
			String mdPayPwd = MD5X.getLowerCaseMD5For32(payPwd);
			if(userId==null){
				map.put("message","请重新登陆");
			}
		}
		
		if(payInfoModel.getDealType() == 4){
			Double newBalance = resultModel.getBalanceNumber()+payInfoModel.getDealMoney();
			balanceModel.setBalanceNumber(newBalance);
			result1 = balanceServiceI.updateBalance(balanceModel);
			if(result1==0){
				map.put("state", 1);
				map.put("message", "操作失败");
			}else{
				map.put("state", 0);
			}
			return JSON.toJSONString(map);
		}
		
		int result = payInfoServiceI.insertPayInfo(payInfoModel);
		map.put("orderId", payInfoModel.getOrderNo());
		if(result>0 && result1>0){
			map.put("state", 0);
		}else{
			map.put("message", "操作失败");
			throw new RuntimeException();
		}
		return JSON.toJSONString(map);
	}

	*/
/**
	 * 保存订单信息
	 * @param payInfoModel
	 * @return
	 *//*

	@ResponseBody
	@RequestMapping(value = "/saveOrderInfo",method =RequestMethod.POST)
	public String saveOrderInfo(PayInfoModel payInfoModel,String dealMoney,String dealRealMoney,String sign,HttpServletRequest request){
		String str = payInfoModel.getBusinessInfo().trim()
				+payInfoModel.getDealInfo().trim()+dealMoney.trim()+dealRealMoney.trim()
				+payInfoModel.getDealTime().trim()+payInfoModel.getDealType()
				+payInfoModel.getDealState()+payInfoModel.getAccountId().trim()+payInfoModel.getOrderNo().trim();
		if(payInfoModel.getDealRemark()!=null){
			str += payInfoModel.getDealRemark();
		}
		_LOGGER.error("测试");
		_LOGGER.info("字符串拼接====="+str);
		_LOGGER.info("-----------"+sign);
		_LOGGER.info("-------------"+payInfoModel.getBusinessInfo());
		_LOGGER.info("-------------"+payInfoModel.getDealInfo());
		_LOGGER.info("-------------"+payInfoModel.getDealMoney());
		_LOGGER.info("-------------"+payInfoModel.getDealRealMoney());
		_LOGGER.info("-------------"+payInfoModel.getDealTime());
		_LOGGER.info("-------------"+payInfoModel.getDealType());
		_LOGGER.info("-------------"+payInfoModel.getDealState());
		_LOGGER.info("-------------"+payInfoModel.getAccountId());
		_LOGGER.info("-------------"+payInfoModel.getOrderNo());
		Map<String,Object> result = new HashMap<String,Object>();
		String param  = MD5X.getLowerCaseMD5For32(str);
		_LOGGER.info("获取签名信息---------------------"+param);
		if(!sign.equals(param)){
			//签名错误
			result.put("state", 3);
			return JSON.toJSONString(result);
		} 
		List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
		if(list.size()!=0){
			//接受成功但数据已经具备
			result.put("state", 2);
			return JSON.toJSONString(result);
		}
		
		int result1 = payInfoServiceI.insertPayInfo(payInfoModel);
		if(result1!=0){
			result.put("state", 0);
			return JSON.toJSONString(result);
		}else{
			result.put("state", 1);
			return JSON.toJSONString(result);
		}
		
	}
}
*/

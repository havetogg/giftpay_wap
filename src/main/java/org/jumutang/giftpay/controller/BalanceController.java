package org.jumutang.giftpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserInfoModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.service.impl.UserSubServiceImpl;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.ResResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 余额控制层
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
@Controller
public class BalanceController {

	@Autowired
	private BalanceServiceI balanceServiceI;
	@Autowired
	private UserSubServiceImpl userSubService;
	Logger logger =LoggerFactory.getLogger(BalanceController.class);
	
	/**
	 * 查询余额信息
	 * @param balanceModel
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBalanceList")
	public String queryBalances(BalanceModel balanceModel,HttpServletRequest request){
		HttpSession session = request.getSession();
		UserModel userModel =(UserModel) session.getAttribute("wxUser");
		Map<String,Object> result = new HashMap<String,Object>();
		String userId = request.getParameter("openId");
		logger.info("查询余额++++");
		if(StringUtils.isEmpty(userId)){
			userId = (String)session.getAttribute("userId");
		}
		if(userId==null){
			result.put("state", 1);
			return JSON.toJSONString(result);
		}
		balanceModel.setAccountId(userId);
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		if(list.size()==0){
			UserSubModel subModel=new UserSubModel();
			subModel.setUserId(userId);
			List<UserSubModel> subList=this.userSubService.queryUserSubModel(subModel);
			if(subList.size()==0){
				//无用户
				result.put("data", "");
			}else{
				this.balanceServiceI.insertBalace(balanceModel);
				list = balanceServiceI.queryBalances(balanceModel);
				result.put("data", list.get(0));
			}
		}else{
			result.put("data", list.get(0));
		}
		logger.info("查询的数据++++++++++++++++++++++"+list.size());
		result.put("state", 0);
//		result.put("headUrl",userModel.getHeadImgUrl());
//		result.put("name",userModel.getWechatNikeName());
		return JSON.toJSONString(result);
	}
	/**
	 * 查询余额信息
	 * @param balanceModel
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBalanceListByOpenId")
	public String queryBalancesByOpenId(BalanceModel balanceModel,HttpServletRequest request){
		HttpSession session = request.getSession();
		UserModel userModel =(UserModel) session.getAttribute("wxUser");
		Map<String,Object> result = new HashMap<String,Object>();
		String userId="";
		String openId = request.getParameter("openId");
		logger.info("查询余额++++");
		if(userModel==null){
			//userId = (String)session.getAttribute("userId");
			UserSubModel userSubModel=new UserSubModel();
			userSubModel.setOpenId(openId);
			List<UserSubModel> list=this.userSubService.queryUserSubModel(userSubModel);
			if(list.size()==0){
				result.put("state", 1);
				return JSON.toJSONString(result);
			}else{
				userId=list.get(0).getUserId();
			}
		}
		if(StringUtils.isEmpty(userId)){
			result.put("state", 1);
			return JSON.toJSONString(result);
		}
		balanceModel.setAccountId(userId);
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		logger.info("查询的数据++++++++++++++++++++++"+list.size());
		result.put("state", 0);
		result.put("data", list.get(0));
		result.put("headUrl",userModel.getHeadImgUrl());
		result.put("name",userModel.getWechatNikeName());
		return JSON.toJSONString(result);
	}
	/**
	 * 支付、充值余额信息
	 * @param balanceModel
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/withdrawalsBalance")
	public String updateBalance(BalanceModel balanceModel,HttpSession session){
		Map<String,Object> map = new HashMap<String,Object>();
		String phone = (String)session.getAttribute("loginMobile");
		if(phone==null){
			map.put("state", 3);
		}
		String id = (String)session.getAttribute("id");
		if(id==null){
			map.put("state", 1);
		}
		int result = balanceServiceI.updateBalance(balanceModel);
		if(result>0){
			map.put("state", 0);
		}else{
			map.put("state", 2);
			map.put("message", "充值失败");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 创建余额账户
	 * @param openId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveBalance",method=RequestMethod.POST)
	public String insertBalance(String openId,HttpServletRequest request){
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(openId);
		int result = balanceServiceI.insertBalace(balanceModel);
		Map<String,Object> param = new HashMap<String,Object>();
		if(result>0){
		param.put("statue", 0);
		}else{
			param.put("message", "创建失败");
		}
		return JSON.toJSONString(param);
	}
	
	/**
	 * 设置支付密码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setPayPwd")
	public String setPayPassword(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("userId");
		if(id==null){
			map.put("state", 1);
			return JSON.toJSONString(map);
		}
		String password = request.getParameter("pwd");
		password = MD5X.getLowerCaseMD5For32(password);
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(id);
		balanceModel.setPayPassword(password);
		int result = balanceServiceI.updateBalance(balanceModel);
		if(result >0 ){
			map.put("state", 0);
		}else{
			map.put("state", 2);
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPayPwd")
	public String editPayPwd(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		HttpSession session = request.getSession();
		String oldPayPassWord = request.getParameter("oldPay");
		oldPayPassWord = MD5X.getLowerCaseMD5For32(oldPayPassWord);
		String newPayPassword = request.getParameter("newPay");
		newPayPassword = MD5X.getLowerCaseMD5For32(newPayPassword);
		String id = (String)session.getAttribute("userId");
		if(id==null){
			map.put("state", 1);
			return JSON.toJSONString(map);
		}
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(id);
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		//判断原先密码输入是否正确
		if(list.size()!=0){
			if(!oldPayPassWord.equals(list.get(0).getPayPassword())){
				map.put("state", 1);
				map.put("message", "原密码错误");
				return JSON.toJSONString(map);
			}
		}
		balanceModel.setPayPassword(newPayPassword);
		int result = balanceServiceI.updateBalance(balanceModel);
		if(result>0){
			map.put("state", 0);
		}else{
			map.put("message", "修改失败");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 查询用户的余额信息
	 * @param openId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBalanceInfo")
	public ResResult queryBalanceInfo(String openId){
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(openId.trim());
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		if(list.size()!=0){
			BalanceModel resultBalance = list.get(0);
			return new ResResult(0,resultBalance);
		}else{
			return new ResResult(1,"暂无数据");
		}
	}
	
	/**
	 * app端充值
	 * @param sign
	 * @param changeTime
	 * @param accountId
	 * @param rechargeMoney
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSave")
	public String updateBalanceInfo(String accountId,String rechargeMoney,String sign,String changeTime){
		Map<String,Object> resultMap = new HashMap<>();
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(accountId);
		balanceModel.setChangeTime(changeTime);
		BalanceModel paramModel = new BalanceModel();
		//验证签名信息
		String str = accountId.trim()+rechargeMoney.trim()+changeTime.trim();
		str = MD5X.getLowerCaseMD5For32(str);
		if(!str.equals(sign.trim())){
			resultMap.put("state", 2);
			return JSON.toJSONString(resultMap);
		}
		paramModel.setAccountId(balanceModel.getAccountId());
		List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
		if(list.size()!=0){
			BalanceModel resultModel = list.get(0);
			balanceModel.setBalanceNumber(resultModel.getBalanceNumber()+new Double(rechargeMoney));
			int result = balanceServiceI.updateBalance(balanceModel);
			if(result==0){
				resultMap.put("state", 1);
				return JSON.toJSONString(resultMap);
			}else{
				resultMap.put("state", 0);
				return JSON.toJSONString(resultMap);
			}
		}else{
			resultMap.put("state", 1);
			return JSON.toJSONString(resultMap);
		}
	}
}

package org.jumutang.giftpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jumutang.giftpay.model.UserInfoModel;
import org.jumutang.giftpay.service.UserInfoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 用户信息控制层
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserInfoServiceI userInfoServiceI;
	
	/**
	 * 查询用户信息
	 * @param userInfoModel
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryUserInfo")
	public String queryUserInfo(UserInfoModel userInfoModel,HttpServletRequest request){
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("id");
		userInfoModel.setUserId(userId);
		List<UserInfoModel> list = userInfoServiceI.queryUserInfo(userInfoModel); 
		Map<String,Object> result = new HashMap<>();
		if(list.size()==0){
			result.put("message", "无数据");
		}else{
			result.put("success", true);
			result.put("data", list);
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 更新用户信息
	 * @param userInfoModel
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUserInfo")
	public String updateUserInfo(UserInfoModel userInfoModel,HttpServletRequest request){
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		userInfoModel.setUserId(id);
		int result = userInfoServiceI.updateUserInfo(userInfoModel);
		Map<String,Object> result1 = new HashMap<>();
		if(result>0){
			result1.put("success", true);
		}else{
			result1.put("message", "更新信息失败");
		}
		return JSON.toJSONString(result1);
	}
	
	/**
	 * 插入信息
	 * @param userInfoModel
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertUseInfo")
	public String insertUserInfo(UserInfoModel userInfoModel,HttpServletRequest request){
		userInfoModel.setOpenId("1");
		int result = userInfoServiceI.insertUserInfo(userInfoModel);
		Map<String,Object> param = new HashMap<>();
		if(result>0){
			param.put("success", true);
		}else{
			param.put("message", "插入失败");
		}
		return JSON.toJSONString(param);
	}
	
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public String login(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("id", "1");
		return JSON.toJSONString("成功");
	}
}

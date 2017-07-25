package org.jumutang.giftpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.service.UserMainService;
import org.jumutang.giftpay.tools.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 用户信息调用控制层
 * 
 * @author 鲁雨
 * @since 20170120
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
@Controller
public class MemberController {
	@Autowired
	private UserMainService userMainService;
 Logger logger = LoggerFactory.getLogger(MemberController.class);
	/**
	 * 获取用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMember")
	public String getMember(){
		String url = "http://ityuany.com:8080/user/findAllUser";
		String identity =  "5c8b58d99aee469d96a9e315f5ac4c2b";
		String result = HttpUtil.sendGet(url,"UTF-8",identity);
		return result;
	}

	/**
	 * 验证用户是否绑定手机
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/isBuildPhone")
	public String isBildPhone(HttpServletRequest request){
		Map<String,Object> param = new HashMap<String,Object>();
		HttpSession session = request.getSession();
		UserModel  userModel = (UserModel)session.getAttribute("wxUser");
		if(userModel == null){
			param.put("state", 2);
			param.put("message", "请先登录");
			return JSON.toJSONString(param);
		}
		String name = userModel.getWechatNikeName();
		logger.info("-------------------------"+name);
		
		String headUrl = userModel.getHeadImgUrl();
		logger.info("-------------------------"+headUrl);
		String id = (String) session.getAttribute("userId");
		logger.info("------------------------------获取到用户id"+id);
		UserMainModel userMainModel = new UserMainModel();
		userMainModel.setId(id);
		List<UserMainModel> list = userMainService.queryUserMainModel(userMainModel);
		logger.info("获取信息条数--------------"+list.size());
		
		UserMainModel mainModel = list.get(0);
		if(mainModel.getPhone() == null || "".equals(mainModel.getPhone())){
			logger.info("未绑定手机--------------");
			param.put("state", 1);
			param.put("headUrl", headUrl);
			param.put("name",name);
			param.put("id", id);
		}else{
			logger.info("已绑定手机-------------------");
			param.put("state",0);
			param.put("phone", list.get(0).getPhone());
			param.put("headUrl", headUrl);
			param.put("name", name);
			param.put("id", id);
		}
		return JSON.toJSONString(param);
	}
	
	/**
	 * 用户绑定手机
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/buildPhone")
	public String register(HttpServletRequest request){
		String url = "http://ityuany.com:8080/user/addUser";
		HttpSession session = request.getSession();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		UserModel userModel = (UserModel)session.getAttribute("userInfo");
		String wechatNikeName =userModel.getWechatNikeName();
		String wechatUnionID = userModel.getWechatUnionID();
		String headImgUrl = userModel.getHeadImgUrl();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loginMobile", phone);
		map.put("loginPassword", password);
		map.put("wechatNikeName", wechatNikeName);
		map.put("wechatUnionID", wechatUnionID);
		map.put("headImgUrl", headImgUrl);
		String param = JSON.toJSONString(map);
		return HttpUtil.sendPost(url, "utf-8", param, null);
	}
}

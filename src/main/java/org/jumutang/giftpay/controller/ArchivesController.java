package org.jumutang.giftpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.UserMainService;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.tools.HttpUtil;
import org.jumutang.giftpay.tools.MD5X;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 身份验证调用转发控制层
 * 
 * @author 鲁雨
 * @since 20170120
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
@Controller
public class ArchivesController {

	@Autowired
	private BalanceServiceI balanceServiceI;
	@Autowired
	private UserMainService userMainService;
	@Autowired
	private UserSubService userSubService;
	/**
	 * 获取档案信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getArchives")
	public String  getArchives(HttpServletRequest request,HttpServletResponse response){
		String url = "http://ityuany.com:8080/archives/queryArchives";
		String identity = "5c8b58d99aee469d96a9e315f5ac4c2b";
//		String result = HttpUtil.sendGet(url,"UTF-8",identity);
		Map<String,Object> result = new HashMap<>();
		result.put("code", 10000);
//		System.out.println("请求发送完毕");
		return JSON.toJSONString(result);
	}
	
	/**
	 * 创建档案信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveArchives")
	public String saveArchives(HttpServletRequest request,String name,String code){
		String url = "http://ityuany.com:8080/archives/addArchives";
		String id = "5c8b58d99aee469d96a9e315f5ac4c2b";
		Map<String,Object> param = new HashMap<>();
		param.put("name",name);
		param.put("cert",code );
//		String result = HttpUtil.sendPost(url, "utf-8", JSON.toJSONString(param), id);
		Map<String,Object> result = new HashMap<>();
		result.put("code", 10000);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 检验身份信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkArchives")
	public String checkArchives(HttpServletRequest request){
		HttpSession session = request.getSession();
		Map<String,Object> result = new HashMap<>();
		String userId = (String)session.getAttribute("userId");
		if(userId==null){
			result.put("state", "1");
			result.put("message", "未登录");
			return JSON.toJSONString(result);
		}
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String pwd = request.getParameter("pwd");
		UserMainModel userMainModel = new UserMainModel();
		userMainModel.setUserName(name);
		userMainModel.setIdCard(code);
		List<UserMainModel> list = userMainService.queryUserMainModel(userMainModel);
		if(list.size()==0){
			result.put("state", 2);
			result.put("message", "身份信息错误");
			return JSON.toJSONString(result);
		}
		pwd = MD5X.getLowerCaseMD5For32(pwd);
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(userId);
		balanceModel.setPayPassword(pwd);
		int result1 =  balanceServiceI.updateBalance(balanceModel);
		if(result1 >0){
			result.put("state", 0);
		}else{
			result.put("state", 3);
			result.put("message", "设置失败");
		}
		return JSON.toJSONString(result);
	}
	
	
	/**
	 * 查询是否绑定身份信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkIdCard")
	public String checkIdCard(HttpServletRequest request){
		HttpSession session = request.getSession();
		Map<String,Object> map = new HashMap<>();
		String userId =  (String)session.getAttribute("userId");
		UserMainModel userMainModel = new UserMainModel();
		userMainModel.setId(userId);
		List<UserMainModel> userList = userMainService.queryUserMainModel(userMainModel);
		UserMainModel userMainModelResult = userList.get(0);
		if(userMainModelResult.getIdCard()==null){
			map.put("state", 3);
			map.put("message", "未身份验证");
			return JSON.toJSONString(map);
		}else{
			map.put("state", 4);
			map.put("message", "查询失败");
			return JSON.toJSONString(map);
		}
	}
	
	/**
	 * 保存身份证信息
	 * @param request
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/saveIdCard")
	public String saveIdCard(HttpServletRequest request){
    	String idCard = request.getParameter("idCard");
    	String userName = request.getParameter("userName");
		HttpSession session = request.getSession();
		Map<String,Object> map = new HashMap<>();
		String userId = (String)session.getAttribute("userId");
		UserMainModel userMainModel = new UserMainModel();
		userMainModel.setIdCard(idCard);
		userMainModel.setUserName(userName);
		userMainModel.setId(userId);
		int result = userMainService.updateUserMainModel(userMainModel);
		if(result!=0){
			map.put("state", 0);
			return JSON.toJSONString(map);
		}else{
			map.put("state", 3);
			map.put("message", "绑定身份失败");
			return JSON.toJSONString(map);
		}
	}
    
    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/isSettingInfo")
    public String isSetingInfo(HttpServletRequest request){
    	Map<String,Object> map = new HashMap<>();
    	HttpSession session = request.getSession();
    	String userId = (String)session.getAttribute("userId");
    	if(userId == null){
    		map.put("state", "1");
			map.put("message", "未登录");
			return JSON.toJSONString(map);
    	}
		//查询支付密码
		BalanceModel balanceModel = new BalanceModel();
		balanceModel.setAccountId(userId);
		List<BalanceModel> balanceList = balanceServiceI.queryBalances(balanceModel);
		if(balanceList.size()==0){
			map.put("state", 3);
			map.put("message", "无账户");
			return JSON.toJSONString(map);
		}else{
			map.put("pwd", balanceList.get(0).getPayPassword());
		}
		//查询用户信息
		UserMainModel userMainModel = new UserMainModel();
		userMainModel.setId(userId);
		List<UserMainModel> resultList = userMainService.queryUserMainModel(userMainModel);
		map.put("state", 0);
		map.put("data", resultList.get(0));
		return JSON.toJSONString(map);
    }
    
    /**
     * 验证身份修改密码
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/chechIdCode")
    public String checkId(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	Map<String, Object> map = new HashMap<>();
    	String userId = (String)session.getAttribute("userId");
    	if(userId == null){
    		map.put("state", "1");
			map.put("message", "未登录");
			return JSON.toJSONString(map);
    	}
    	String password = request.getParameter("pwd");
    	String name = request.getParameter("name");
    	String code = request.getParameter("code");
    	UserMainModel userMainModel = new UserMainModel();
    	userMainModel.setIdCard(code);
    	userMainModel.setUserName(name);
    	List<UserMainModel> list = userMainService.queryUserMainModel(userMainModel);
    	if(list.size()==0){
    		map.put("state",1);
    		map.put("message", "身份信息错误");
    		return JSON.toJSONString(map);
    	}
    	BalanceModel balanceModel = new BalanceModel();
    	balanceModel.setAccountId(userId);
    	balanceModel.setPayPassword(MD5X.getLowerCaseMD5For32(password));
    	int result = balanceServiceI.updateBalance(balanceModel);
    	if(result == 0){
    		map.put("state", 2);
    		map.put("message", "设置失败");
    		return JSON.toJSONString(map);
    	}else{
    		map.put("state", 0);
    		return JSON.toJSONString(map);
    	}
    }
}

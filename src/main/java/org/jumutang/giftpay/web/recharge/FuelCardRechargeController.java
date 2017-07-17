package org.jumutang.giftpay.web.recharge;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.MD5X;
import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.giftpay.base.entity.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.entity.OilCardConstantModel;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.UserNumRecord;
import org.jumutang.giftpay.service.PayInfoServiceI;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.jumutang.giftpay.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 油卡充值控制层
 * 
 * @author luyuanwen
 * @date 2017年4月6日下午4:53:01
 */
@Controller
@RequestMapping(value = "/giftpay/FuelCardrecharge")
public class FuelCardRechargeController  extends BaseController {

	Logger logger = LoggerFactory.getLogger(FuelCardRechargeController.class);
	
	@Autowired
	private PayInfoServiceI payInfoServiceI;
	
	@Autowired
	private UserNumRecordService userNumRecordService;
	
	
	/**
	 * 查询油卡卡号信息url
	 */
	
	@Value("#{propertyFactoryBean['oufei.yoka.queryCardUrl']}")
	private String getFuelCardNumberUrl;
	
	/**
	 * 充值回调url
	 */
	private String rechargeReturnUrl = "giftpay/FuelCardrecharge/returnUrl.htm";
	
	/**
	 * 跳转页面本地测试url
	 */
	private String redirectUrl = "giftpay/liftpayment/orderPay.html";
	/**
	 * 商户编号
	 */
	@Value("#{propertyFactoryBean['oufei.yoka.userid']}")
	private String userid;
	
	/**
	 * 商户密码
	 */
	@Value("#{propertyFactoryBean['oufei.yoka.userpws']}")
	private String userpws;
	
	/**
	 * 固定值6.0版本号
	 */
	private final String version = "6.0";

	/**
	 * 油卡密匙
	 */
	@Value("#{propertyFactoryBean['oufei.yoka.KeyStr']}")
	private String key_str1;
	
	/**
	 * 号码类型常量 油卡
	 */
	private final int NUM_RECORD_TYPE = 1;
	
	/**
	 * 红包状态 已激活
	 */
    private final int REDPKG_STATE_ACTIVE = 1;
    
    /**
	 * 红包状态 已经使用
	 */
    private final int REDPKG_STATE_USED = 4;
    
    /**
     * 充值缴费类型
     */
    private static final String YOUKA="youka";


	@Value("#{propertyFactoryBean['white_open_id']}")
	private String whiteOpenId;

	/**
	 * 油卡充值 支付
	 * @author luyaunwen 
	 * @data 2017年4月7日上午9:30:28
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
//	@RequestMapping(value = "/fuelCardRecharge.htm")
//	@ResponseBody
//	public String fuelCardRecharge(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		String userId=getUserId(session);
//		String openId = (String) session.getAttribute("openId");
//		if(userId == null) {
//			CodeMess code = new CodeMess("0", "用户超时", "");
//          return JSONObject.toJSONString(code);
//		}
//		String game_userid = request.getParameter("game_userid");
//		String cardid = request.getParameter("cardid");
//		String cardnumStr = request.getParameter("cardnum");
//		String cardValueStr = request.getParameter("cardValue");
//		String redPkgId = request.getParameter("redpkgId");
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		String sporder_id = sdf.format(new Date());
//		String sporder_time = sdf.format(new Date());;
//		String md5_str = MD5X.getUpperCaseMD5For32((new StringBuffer(userid).append(userpws).append(cardid)
//                .append(cardnumStr).append(sporder_id).append(sporder_time).append(game_userid).append(key_str1)
//                .toString()));
//		PostOrGet postOrGet = new PostOrGet("GB2312");
//		PostOrGet postOrGetRed = new PostOrGet("utf-8");
//		
//		//如果使用了红包，要更新红包信息
//		if(!"".equals(redPkgId)) {
//			String updateRedPkgUrl = updateRedUrl + "?openId=" + openId + "&redpkgId=" + redPkgId + "&state=" + REDPKG_STATE_USED;
//			String updateRedPkgResult = postOrGetRed.sendPost(updateRedPkgUrl, ESendHTTPModel._SEND_MODEL_ENCODE, new NamedValue("", ""));
//			if("".equals(updateRedPkgResult)) {
//				logger.error("========================更新红包状态接口调用失败");
//			}else {
//				logger.info("=====================更新红包状态为已使用: " + updateRedPkgResult);
//			}
//		}
//		
//		//生成初始订单信息
//		PayInfoModel payInfo = new PayInfoModel();
//		payInfo.setAccountId("TestYOKA");
//		payInfo.setBusinessInfo("有礼付易加油");
//		payInfo.setDealId(UniqueX.randomUUID());
//		payInfo.setDealInfo("油卡充值");
//		if("1".equals(cardnumStr)) {
//			Double cardValue = Double.valueOf(cardValueStr);
//			payInfo.setDealMoney(cardValue);
//		}else {
//			Double cardnum = Double.valueOf(cardnumStr);
//			payInfo.setDealMoney(cardnum);
//		}
//		short dealState = 2;//1.成功 2.未成功 （提现状态）3. 提现中 4. 已提现 5.提现失败
//		short dealType = 3;//交易类型 1.提现 2.充值 3.支付 4.退款
//		payInfo.setDealRealMoney(0d);
//		payInfo.setDealState(dealState);
//		payInfo.setDealType(dealType);
//		payInfo.setDealRemark(game_userid);
//		payInfo.setOrderNo(sporder_id);
//		payInfoServiceI.insertPayInfo(payInfo);
//		logger.info("=================生成初始订单信息: " + payInfo.toString());
//		
//		//拼接参数，发送油卡充值请求
//		NamedValue param_userid = new NamedValue("userid", userid);
//		NamedValue param_userpws = new NamedValue("userpws", userpws);
//		NamedValue param_cardid = new NamedValue("cardid", cardid);
//		NamedValue param_cardnum = new NamedValue("cardnum", cardnumStr);
//		NamedValue param_sporder_id = new NamedValue("sporder_id", sporder_id);
//		NamedValue param_sporder_time = new NamedValue("sporder_time", sporder_time);
//		NamedValue param_game_userid = new NamedValue("game_userid", game_userid);
//		NamedValue param_md5_str = new NamedValue("md5_str", md5_str);
//		NamedValue param_version = new NamedValue("version", version);
//		NamedValue param_url = new NamedValue("ret_url", this.getBaseUrl(request, response) + rechargeReturnUrl);
//		String completeUrl = postOrGet.preGetRequestUrl(fuelCardRechargeUrl,ESendHTTPModel._SEND_MODEL_ENCODE , param_userid, param_userpws,
//				param_cardid, param_cardnum, param_sporder_id, param_sporder_time, param_game_userid, param_md5_str, param_version, param_url);
//		logger.info("=================油卡充值url和传递参数：" + completeUrl);
//		String result = postOrGet.sendPost(completeUrl, ESendHTTPModel._SEND_MODEL_ENCODE, new NamedValue("", ""));
//		logger.info("=================油卡充值result" + result);
//		if("".equals(result)) {
//			logger.error("=================调用欧飞接口异常");
//			CodeMess codeMess = new CodeMess("-1", "油卡充值失败", "");
//			return JSON.toJSONString(codeMess);
//		}
//		String jsonDataStr = "";
//		try {
//			jsonDataStr = new XMLSerializer().read(result).toString();
//		} catch (Exception e) {
//			logger.error("=================xml文件解析异常");
//			CodeMess codeMess = new CodeMess("-1", "油卡充值失败", "");
//			return JSON.toJSONString(codeMess);
//		}
//		JSONObject jsonObj = JSONObject.parseObject(jsonDataStr);
//		logger.info("=================油卡充值，欧飞接口调用返回结果：" + jsonDataStr);
//		if("1".equals(jsonObj.getString("retcode"))) {
//			payInfo.setDealInfo(jsonObj.getString("cardname"));
//			payInfo.setWxOrder(jsonObj.getString("orderid"));
//			if("1".equals(jsonObj.get("game_state"))){
//				//game_state=1表示充值成功，更新订单状态为1，表示成功
//				dealState = 1;
//				payInfo.setDealState(dealState);
//			}
//			payInfoServiceI.updatePayInfo(payInfo);
//			logger.info("=================油卡充值成功，更新油卡欧飞返回订单编号、状态");
//			CodeMess codeMess = new CodeMess("1", "充值成功", "");
//			return JSON.toJSONString(codeMess);
//		}else {
//			logger.error("=================油卡充值欧飞接口调用发生错误：" + jsonObj.getString("err_mess"));
//			CodeMess codeMess = new CodeMess(jsonObj.getString("retcode"), jsonObj.getString("err_mess"), "");
//			return JSON.toJSONString(codeMess);
//		}
//	}
	
	/**
	 * 查询用户输入的油卡卡号信息
	 * @author luyaunwen 
	 * @data 2017年4月7日上午9:29:54
	 * @param request
	 * @param response
	 * @param session
	 * @return  卡号的用户名 
	 */
	@RequestMapping(value = "/queryfuelCardInfo.htm")
	@ResponseBody
	public String queryfuelCardInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userId=getUserId(session);
		String game_userid = request.getParameter("game_userid") == null? "" : request.getParameter("game_userid").toString();
		String chargeType=request.getParameter("chargeType");
		String card_md5_str = MD5X.getUpperCaseMD5For32(new StringBuffer(userid).append(userpws).append(game_userid).append(key_str1).toString());
		NamedValue param_userid = new NamedValue("userid", userid);
		NamedValue param_userpws = new NamedValue("userpws", userpws);
		NamedValue param_card_md5_str = new NamedValue("md5_str", card_md5_str);
		NamedValue param_version = new NamedValue("version", version);
		NamedValue param_game_userid = new NamedValue("game_userid", game_userid);
		NamedValue chargeT = new NamedValue("chargeType", chargeType);
		PostOrGet postOrGet = new PostOrGet("GB2312");
		String queryFuelCardNumUrl = postOrGet.preGetRequestUrl(getFuelCardNumberUrl, ESendHTTPModel._SEND_MODEL_ENCODE, param_userid, 
				param_userpws, param_game_userid, param_card_md5_str, param_version,chargeT);
		logger.info("=================查询卡号信息url和传递参数：" + queryFuelCardNumUrl);
		String queryCardNumberResult = postOrGet.sendPost(queryFuelCardNumUrl, ESendHTTPModel._SEND_MODEL_ENCODE, new NamedValue("", ""));
		String jsonDataStr = "";
		try {
			jsonDataStr = new XMLSerializer().read(queryCardNumberResult).toString();
		} catch (Exception e) {
			logger.error("=================xml文件解析异常。");
			throw new RuntimeException("xml文件解析异常。");
		}
		JSONObject jsonObj = JSONObject.parseObject(jsonDataStr);
		if("1".equals(jsonObj.getString("retcode"))) {
			//生成油卡号码记录
			UserNumRecord userNumRecord = new UserNumRecord();
			userNumRecord.setId(UniqueX.randomUUID());
			userNumRecord.setNumber(jsonObj.getString("game_userid"));
			userNumRecord.setNumberType(NUM_RECORD_TYPE);
			userNumRecord.setUserId(userId);
			userNumRecord.setNumber_desc(jsonObj.getString("username"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userNumRecord.setCreateTime(sdf.format(new Date()));
			if(!userNumRecordService.isNumExist(userNumRecord.getUserId(), userNumRecord.getNumber(), 1)){
				//没有改油卡号码记录时添加记录
				logger.info("=================生成油卡充值号码记录: " + userNumRecord.toString());
				userNumRecordService.addUserNumRecord(userNumRecord);
			}else {
				//有记录时更新记录时间
				logger.info("=================更新油卡号码记录时间: " + userNumRecord.toString());
				userNumRecordService.updateCreateTime(userNumRecord.getUserId(), userNumRecord.getNumber(), userNumRecord.getCreateTime());
			}
			return jsonDataStr;
		}else {
			jsonObj.put("err_msg", "油卡卡号错误");
			logger.error("=================油卡卡号错误");
			return jsonObj.toJSONString();
		}
	} 
	
	/**
	 * 油卡充值回调的url 用来更新油卡充值订单的状态
	 * @author luyaunwen 
	 * @data 2017年4月7日下午5:19:07
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "/returnUrl.htm")
	@ResponseBody
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		logger.info("=================进入油卡欧飞接口回调方法");
		String ret_code=request.getParameter("ret_code");
        String sporder_id=request.getParameter("sporder_id");
        String ordersuccesstime=request.getParameter("ordersuccesstime");
        String err_msg=request.getParameter("err_msg "); 
        String gascard_code=request.getParameter("gascard_code ");
        logger.info("=================回调支付信息:ret_code: " + ret_code +"sporder_id: " + sporder_id + "ordersuccesstime: " + ordersuccesstime + "err_msg: " + err_msg + "gascard_code: " + gascard_code);
        PayInfoModel infoModel = new PayInfoModel();
        infoModel.setOrderNo(sporder_id);
        List<PayInfoModel> payInfoLists = payInfoServiceI.queryPayInfos(infoModel);
        if(payInfoLists != null && payInfoLists.size() != 0 ) {
        	PayInfoModel oldPayInfo = payInfoLists.get(0); 		
		if("1".equals(ret_code)) {
				if(oldPayInfo.getDealState()==0||oldPayInfo.getDealState()==2) {
					oldPayInfo.setDealState((short)1);
					oldPayInfo.setDealTime(ordersuccesstime);
					payInfoServiceI.updatePayInfo(oldPayInfo);
					logger.info("=================订单充值成功，更新订单状态");
				}
				//加油成功推送消息
				logger.info("开始进入推送消息");
				String[] whitelist=whiteOpenId.split(",");
				for(String str:whitelist){
					logger.info("推送对象:"+str);
					TemplateMessageUtil.sendPaySuccessByOilRecharge(oldPayInfo.getOrderNo(),String.valueOf(oldPayInfo.getDealMoney()),
							oldPayInfo.getDealTime(),oldPayInfo.getDealRemark(),str,String.valueOf(oldPayInfo.getDealRealMoney()),"",1);
				}
				logger.info("推送消息结束");
		}else if("9".equals(ret_code)){
			logger.info("=================订单撤销，更新订单状态" + err_msg);
			oldPayInfo.setDealState(new Short(ret_code));
			this.payInfoServiceI.updatePayInfo(oldPayInfo);
			
			//充值失败，将支付金额返回到有礼付余额中。
            BalanceModel balanceModel = new BalanceModel();
            balanceModel.setAccountId(oldPayInfo.getAccountId());
            List<BalanceModel> balanceModels = this.balanceServiceI.queryBalances(balanceModel);
            balanceModel.setChangeTime(DateFormatUtil.formateString());
            balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() + oldPayInfo.getDealRealMoney());
            int balanceResult = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
            logger.info("=======================充值失败，将支付金额返还到有礼付余额=更新余额表balanceResult：" + balanceResult);
		}
	}
		return "";
	}
	
	/**
	 * 查询用户充值过的油卡卡号信息
	 * @author luyaunwen 
	 * @data 2017年4月11日下午3:52:30
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryFuelCardRecord.htm")
	@ResponseBody
	public String queryFuelCardRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        session.setAttribute("userId", "TestYOKAwww");
//        session.setAttribute("openId", "lywwww");
		String userId=getUserId(session);
		System.out.println("--------------userId="+userId);
		logger.info("++++++++++++++++userId="+userId);
		if(userId == null) {
		  CodeMess code = new CodeMess("0", "用户超时", "");
		  logger.error("=============查询充值记录失败，用户超时");
          return JSONObject.toJSONString(code);
		}
		List<UserNumRecord> numRecords = userNumRecordService.queryNumRecordByType(NUM_RECORD_TYPE, userId);
		if(numRecords.size() == 0) {
			CodeMess code = new CodeMess("0", "暂无记录", "");
			logger.error("=============无充值记录");
	        return JSONObject.toJSONString(code);
		}
		return JSONObject.toJSONString(numRecords);
	}
	
	/**
	 * 删除全部油卡账号记录
	 * @author luyaunwen 
	 * @data 2017年4月10日下午3:23:44
	 * @return
	 */
	@RequestMapping(value = "/delAllRecord.htm")
	public void delAllRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userId=getUserId(session);
		if(userId == null) {
			logger.error("======================用户登录超时:" + userId);
	    }
		logger.info("======================删除该用户全部油卡号码记录:" + userId);
		boolean result = userNumRecordService.delAll(userId);
		if(result) {
			logger.info("======================删除该用户全部油卡号码记录成功");
		}else {
			logger.error("======================删除该用户全部油卡号码记录失败");
		}
	}
	
	@RequestMapping(value = "/delOneRecord.htm")
	public void delOneRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userId= getUserId(session);
		if(userId == null) {
			logger.error("======================用户登录超时:" + userId);
	    }
		String game_userid = request.getParameter("game_userid") == null? "" : request.getParameter("game_userid").toString();
		logger.info("======================删除指定油卡号码记录: " + userId + ","+game_userid);
		boolean result = userNumRecordService.delUserNumRecord(userId, game_userid);
		if(result) {
			logger.info("======================删除指定油卡号码记录成功");
		}else {
			logger.error("======================删除指定油卡号码记录失败");
		}
	}
	

	/**
	 * 查询油卡充值记录
	 * @author luyaunwen 
	 * @data 2017年4月11日上午10:42:42
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryFuelCardRechargeRecord.htm")
	@ResponseBody
	public String queryFuelCardRechargeRecord(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userId= getUserId(session);
		if(userId == null) {
			CodeMess code = new CodeMess("0", "用户超时", "");
	        return JSONObject.toJSONString(code);
	   }
		String payInfoModelList = payInfoServiceI.fuelCardHistorySearch(userId);
		logger.info("=======================查询油卡充值记录: " + payInfoModelList);
		if("".equals(payInfoModelList)) {
			CodeMess code = new CodeMess("0", "暂无记录", "");
	        return JSONObject.toJSONString(code);
		}
		return JSON.toJSONString(payInfoModelList);
	}
	
	 /**
     * 查询用户红包
     * @author luyaunwen 
     * @data 2017年4月12日下午3:30:05
     * @return 
     */
	@RequestMapping(value = "/queryRedPkgInfo.htm")
	@ResponseBody
    public String queryRedPkgInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response){
		String openId = (String) session.getAttribute("openId");//有礼付openId
    	PostOrGet postOrGet = new PostOrGet("utf-8");
		String queryRedUrl = queryAllRedUrl + "?openId=" + openId + "&state=" + REDPKG_STATE_ACTIVE;
		logger.info("=====================查询红包接口url和传递参数：" + queryRedUrl);
		String queryRedUrlResult = postOrGet.sendPost(queryRedUrl, ESendHTTPModel._SEND_MODEL_ENCODE, new NamedValue("", ""));
		if("".equals(queryRedUrlResult)) {
			logger.error("＝＝＝＝＝＝＝＝＝＝＝＝========查询红包接口调用失败");
		}else {
			logger.info("＝＝＝＝＝＝＝＝＝＝=========== 红包查询结果：" + queryRedUrlResult);
		}
    	return queryRedUrlResult;
    }
	
	@RequestMapping(value = "/toOrderPay.htm")
	@ResponseBody
	public void toOrderPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        }
		String game_userid = request.getParameter("game_userid");
		String cardid = request.getParameter("cardid");
		String cardnumStr = request.getParameter("cardnum");
		String cardValueStr = request.getParameter("cardValue");
		String redPkgId = request.getParameter("redPkgId");
		String redPkgValue = request.getParameter("redPkgValue");
		String chargeType=request.getParameter("chargeType");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sporder_id = UUIDUtil.getUUID();
		String sporder_time = sdf.format(new Date());
		String md5_str = MD5X.getUpperCaseMD5For32((new StringBuffer(userid).append(userpws).append(cardid)
                .append(cardnumStr).append(sporder_id).append(sporder_time).append(game_userid).append(key_str1)
                .toString()));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", userid);
		params.put("userpws", userpws);
		params.put("cardid", cardid);
		params.put("cardnum", cardnumStr);
		params.put("sporder_id", sporder_id);
		params.put("sporder_time", sporder_time);
        params.put("game_userid", game_userid);
        params.put("md5_str", md5_str);
        params.put("version", version);
        params.put("ret_url", this.getBaseUrl(request, response) + rechargeReturnUrl);
        params.put("cardValueStr", cardValueStr);
		params.put("chargeType",chargeType);
		params.put("timestamp",DateFormatUtil.formateString());
        logger.info("=============油卡充值参数:"+JSONObject.toJSONString(params));
        session.setAttribute("fuelRechargeParams",params);

        String payamount = cardValueStr;
        String rechargeType = YOUKA;

        String targetUrl = this.getBaseUrl(request, response) + redirectUrl + "?payamount=" + payamount + "&rechargeType=" + rechargeType + "&lifeType=" + "&redPkgId=" + redPkgId + "&redPkgValue=" + redPkgValue;
		logger.info("=====================跳转支付页面地址:" + targetUrl);
		response.sendRedirect(response.encodeRedirectURL(targetUrl));
		
	}



	@RequestMapping(value = "/toOrderPayFuelCard.htm")
	@ResponseBody
	public String toOrderPayFuelCard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		logger.error("进入油卡预支付方法");
		net.sf.json.JSONObject object=new net.sf.json.JSONObject();
		String userId = getUserId(session);
		String openId = (String) session.getAttribute("openId");
		logger.error("用户OPENID:"+openId+",USERID:"+userId);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
			object.put("success","0");
			object.put("errMsg","请求超时,请重新登录!");
			return object.toString();
		} else {
			session.setAttribute("userId", userId);
			session.setAttribute("openId", openId);
		}
		String dateTime= (String) session.getAttribute("OilTime");
		if(StringUtils.isEmpty(dateTime)||dateTime==null){
			session.setAttribute("OilTime",DateFormatUtil.formateString());
		}else{
			String now=DateFormatUtil.formateString();
			if(DateFormatUtil.compare_date(dateTime,now)){
				//超过2分钟
				session.setAttribute("OilTime",now);
			}else{
				object.put("success","0");
				object.put("errMsg","订单提交过快，请稍后再试");
				return object.toString();
			}
		}
		String goodsId=request.getParameter("goodsID");
		if(goodsId==null||StringUtils.isEmpty(goodsId)){
			object.put("success","0");
			object.put("errMsg","订单参数错误");
			return object.toString();
		}
		//获取商品cardid
		String oilCardId=OilCardConstantModel.CARDNUM(goodsId);
		String cardid="";
		String cardnumStr="";//充值面额
		String cardValueStr =request.getParameter("rechargeAmount");//付款金额
		if(oilCardId==null){
			logger.error("商品参数异常");
			object.put("success","0");
			object.put("errMsg","商品参数异常");
			return object.toString();
		}else{
			JSONObject oilObj=JSON.parseObject(oilCardId);
			logger.error("商品信息:"+oilObj.toJSONString());
			cardid=oilObj.getString("cardNum");
		}
		GoodsListModel goodsListModel=new GoodsListModel();
		goodsListModel.setId(goodsId);
		List<GoodsListModel> godList=this.goodsListService.queryGoodsList(goodsListModel);
		if(godList.size()==0){
			logger.error("未获取到商品数据");
			object.put("success","0");
			object.put("errMsg","商品参数异常");
			return object.toString();
		}
		if(!godList.get(0).getRealPrice().equals(cardValueStr)){
			logger.error("付款金额不统一");
			object.put("success","0");
			object.put("errMsg","商品参数异常");
			return object.toString();
		}
		String game_userid = request.getParameter("game_userid");//订单号
		String redPkgId = request.getParameter("redPkgId");
		String redPkgValue = request.getParameter("redPkgValue");
		String chargeType=request.getParameter("chargeType");//1中石化 2中石油
		/*if(chargeType.equals("1")){
			cardnumStr="1";
		}else{
			cardnumStr=godList.get(0).getGoodsPrice();
		}*/
		cardnumStr="1";//默认为1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sporder_id = UUIDUtil.getUUID();
		String sporder_time = sdf.format(new Date());
		String md5_str = MD5X.getUpperCaseMD5For32((new StringBuffer(userid).append(userpws).append(cardid)
				.append(cardnumStr).append(sporder_id).append(sporder_time).append(game_userid).append(key_str1)
				.toString()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", userid);
		params.put("userpws", userpws);
		params.put("cardid", cardid);
		params.put("cardnum", cardnumStr);
		params.put("sporder_id", sporder_id);
		params.put("sporder_time", sporder_time);
		params.put("game_userid", game_userid);
		params.put("md5_str", md5_str);
		params.put("version", version);
		params.put("ret_url", this.getBaseUrl(request, response) + rechargeReturnUrl);
		params.put("cardValueStr", cardValueStr);
		params.put("chargeType",chargeType);
		logger.info("=============油卡充值参数:"+JSONObject.toJSONString(params));
		session.setAttribute("fuelRechargeParams",params);

		String goodsName=chargeType.equals("1")?"中石化":"中石油";


		String realMoney = org.apache.commons.lang.StringUtils.isBlank(redPkgValue)?String.valueOf(Double.parseDouble(cardValueStr)):String.valueOf(Double.parseDouble(cardValueStr)-Double.parseDouble(redPkgValue));

		OrderInfoModel order=new OrderInfoModel();
		order.setId(UUIDUtil.getUUID());
		order.setGoodsName(goodsName+godList.get(0).getGoodsPrice());
		order.setCreateTime(DateFormatUtil.formateString());
		order.setOrderNo(params.get("sporder_id"));
		order.setFromType("2");
		order.setOpenId(openId);
		order.setGoodsMoney(godList.get(0).getGoodsPrice());
		order.setPayMoney(cardValueStr);
		order.setIp(IPUtil.getIpAddr(request));
		order.setMd5Sign(MD5Util.MD5Encode(goodsName+order.getOrderNo()+realMoney+order.getIp()).toUpperCase());
		order.setOrderParams(net.sf.json.JSONObject.fromObject(params).toString());
		order.setPayStatus("0");
		order.setRemark("");
		order.setStatus("0");
		order.setWxOrder("");
		order.setGoodsId(chargeType);
		this.orderService.addOrderInfo(order);
		logger.error("油卡预支付订单参数:"+ net.sf.json.JSONObject.fromObject(order).toString());

		object.put("payamount",cardValueStr);
		object.put("rechargeType",YOUKA);
		object.put("lifeType","");
		object.put("redPkgId",redPkgId);
		object.put("redPkgValue",redPkgValue);
		object.put("payInfo",order.getMd5Sign());
		object.put("success","1");
		object.put("errMsg","");
		return object.toString();
	}

	public static Map<String, String> getResponseMap(HttpServletRequest request) throws IOException, DocumentException {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            return null;
        }

        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

	public static void main(String[] args) {
	}
}

package org.jumutang.caranswer.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.epay.Digest;
import org.jumutang.caranswer.framework.epay.ZGTService;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.MD5X;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.service.IThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class ThirdPartyServiceImpl implements IThirdPartyService {

	private static final Logger _LOGGER = LoggerFactory.getLogger(ThirdPartyServiceImpl.class);

	/**
	 * 聚合全局的openID
	 */
	@Value(value = "#{propertyFactoryBean['juhe.openID']}")
	private String juHeOpenID;

	/**
	 * 聚合加油卡模块的配置信息
	 */
	// 聚合加油卡充值接口appkey
	@Value(value = "#{propertyFactoryBean['juhe.fuelCard.appID']}")
	private String juHeFuelCardAppKey;
	// 聚合加油卡充值请求接口
	@Value(value = "#{propertyFactoryBean['juhe.fuelCard.recharge.URL']}")
	private String juHeFuelCardRechargeUrl;
	// 聚合加油卡状态查询接口请求地址
	@Value(value = "#{propertyFactoryBean['juhe.fuelCard.status.query.URL']}")
	private String juHeFuelCardStatusQueryUrl;

	// 聚合sms接口的appkey
	@Value(value = "#{propertyFactoryBean['juhe.SMS.appID']}")
	private String juHeSMSAppKey;
	// 聚合sms接口的模板id
	@Value(value = "#{propertyFactoryBean['juhe.SMS.register.TPLID']}")
	private String juHeSMSTPLID;
	// 聚合sms接口请求地址
	@Value(value = "#{propertyFactoryBean['juhe.SMS.URL']}")
	private String juHeSMSSendUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.service.IThirdPartyService#getSMSSecurityCode(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public ContextResult<Object> getSMSSecurityCode(String userTel, String securityCode) {
		ContextResult<Object> cr = new ContextResult<Object>();
		PostOrGet ps = new PostOrGet("utf-8");
		_LOGGER.info("准备向聚合发送短信验证请求！");
		String result = ps.sendGet(this.juHeSMSSendUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("mobile", userTel), new NamedValue("tpl_id", this.juHeSMSTPLID),
				new NamedValue("tpl_value", StringX.stringFormat("#code#=[0]", securityCode)),
				new NamedValue("key", this.juHeSMSAppKey), new NamedValue("dtype", "json"));
		_LOGGER.info(StringX.stringFormat("聚合sms接口返回信息为:[0]", result));
		try {
			JSONObject json = JSONObject.parseObject(result);
			if ("0".equals(String.valueOf(json.get("error_code")))) {
				cr.setCode(ContextContast._TRUE_CODE);
			} else {
				cr.setCode(ContextContast._FALSE_CODE);
			}
			cr.setMess(String.valueOf(json.get("reason"))).setResultObject(result);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess(ContextContast._OPERATION_FALSE_MESS)
					.setResultObject(result);
		}
		return (cr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.service.IThirdPartyService#applyRechargeFuelCard(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ContextResult<Object> applyRechargeFuelCard(String cardNum, String iOrderID, String gameUserID,
			String gasCardTel, String gasCardName) {
		_LOGGER.info("用户请求申请加油卡充值接口！");
		ContextResult<Object> cr = new ContextResult<Object>();
		_LOGGER.info("加密参数信息！");
		EFuelCardType cardType = this.validateFuelCard(gameUserID);
		if (null == cardType) {
			cr.setCode(ContextContast._FALSE_CODE).setMess("加油卡号不正确！");
		} else {
			String sign = MD5X.getLowerCaseMD5For32(new StringBuffer(this.juHeOpenID).append(this.juHeFuelCardAppKey)
					.append(cardType.getProd_code()).append(cardNum).append(gameUserID).append(iOrderID).toString());
			_LOGGER.info("向聚合发送申请加油卡充值请求！");
			PostOrGet pg = new PostOrGet("utf-8");
			String result = pg.sendGet(this.juHeFuelCardRechargeUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
					new NamedValue("proid", cardType.getProd_code()), new NamedValue("cardnum", cardNum),
					new NamedValue("orderid", iOrderID), new NamedValue("game_userid", gameUserID),
					new NamedValue("gasCardTel", gasCardTel), new NamedValue("gasCardName", gasCardName),
					new NamedValue("chargeType", cardType.getJava_code()),
					new NamedValue("key", this.juHeFuelCardAppKey), new NamedValue("sign", sign));
			_LOGGER.info(StringX.stringFormat("聚合申请加油卡充值接口的返回结果为：[0]", result));
			try {
				JSONObject json = JSONObject.parseObject(result);
				if ("0".equals(String.valueOf(json.get("error_code")))) {
					cr.setCode(ContextContast._TRUE_CODE);
				} else {
					cr.setCode(ContextContast._FALSE_CODE);
				}
				cr.setMess(String.valueOf(json.get("reason"))).setResultObject(result);
			} catch (Exception e) {
				_LOGGER.error("聚合返回结果解析json失败！");
				_LOGGER.error(e.getMessage(), e);
				cr.setCode(ContextContast._FALSE_CODE).setMess(ContextContast._OPERATION_FALSE_MESS)
						.setResultObject(result);
			}
		}
		return cr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jumutang.caranswer.service.IThirdPartyService#
	 * queryFuelCardOrderStatus(java.lang.String)
	 */
	@Override
	public ContextResult<Object> queryFuelCardOrderStatus(String iOrderid) {
		_LOGGER.info("用户请求查询加油卡订单状态接口！");
		ContextResult<Object> cr = new ContextResult<Object>();
		PostOrGet pg = new PostOrGet("utf-8");
		_LOGGER.info("准备向聚合发送查询加油卡充值状态查询请求！");
		String result = pg.sendGet(this.juHeFuelCardStatusQueryUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("orderid", iOrderid), new NamedValue("key", this.juHeFuelCardAppKey));
		_LOGGER.info(StringX.stringFormat("聚合返回的结果信息为：[0]", result));
		try {
			JSONObject json = JSONObject.parseObject(result);
			if ("0".equals(String.valueOf(json.get("error_code")))) {
				cr.setCode(ContextContast._TRUE_CODE);
			} else {
				cr.setCode(ContextContast._FALSE_CODE);
			}
			cr.setMess(String.valueOf(json.get("reason"))).setResultObject(result);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess(ContextContast._OPERATION_FALSE_MESS)
					.setResultObject(result);
		}
		return cr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.service.IThirdPartyService#epay(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ContextResult<Object> epay(String produceName, String iOrderID, String rechargeMoney, String callbackurl,
			String webcallbackurl) {
		ContextResult<Object> cr = new ContextResult<Object>();
		Map<String, String> resultMap = null;
		try {
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("requestid", iOrderID);
			parameterMap.put("productname", produceName);
			parameterMap.put("amount", rechargeMoney);
			parameterMap.put("callbackurl", callbackurl);
			parameterMap.put("webcallbackurl", webcallbackurl);
			parameterMap.put("payproducttype", "ONEKEY");
			resultMap = ZGTService.paymentRequest(parameterMap);
			cr.setCode(ContextContast._TRUE_CODE).setMess(ContextContast._OPERATION_TRUE_MESS)
					.setResultObject(JSONObject.toJSONString(resultMap));
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess(ContextContast._OPERATION_FALSE_MESS)
					.setResultObject(JSONObject.toJSONString(resultMap));
		}
		return cr;
	}

	/**
	 * 生成hmac方法
	 * 
	 * @param arr
	 *            请求参数拼接的字符串
	 * @param keyValue
	 *            商户密钥
	 * @return hmac
	 */
	public static String getHmac(String[] arr, String keyValue) {
		String hmac = "";
		StringBuffer stringValue = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			stringValue.append(StringX.nullToEmpty(arr[i]));
		}
		hmac = Digest.hmacSign(stringValue.toString(), keyValue);
		return (hmac);
	}

	/**
	 * 校验加油卡为中石油还是中石化
	 * 
	 * @param gameUserID
	 * @return
	 */
	private EFuelCardType validateFuelCard(String gameUserID) {
		_LOGGER.info("开始校验加油卡号信息！");
		// 中石化
		String shiHuaregEx = "100011[0-9]+";
		// 中石油
		String shiYouregEx = "9[0-9]+";
		Pattern shiHuaPattern = Pattern.compile(shiHuaregEx);
		Pattern shiYouPattern = Pattern.compile(shiYouregEx);
		if (shiHuaPattern.matcher(gameUserID).matches())
			return EFuelCardType.SHIHUA;
		if (shiYouPattern.matcher(gameUserID).matches())
			return EFuelCardType.SHIYOU;
		return null;
	}

	enum EFuelCardType {
		/**
		 * 产品id: 10000(中石化50元加油卡) 10001(中石化100元加油卡) 10002(中石化200元加油卡)
		 * 10003(中石化500元加油卡) 10004(中石化1000元加油卡) 10005(中石化2000元加油卡)
		 * 10006(中石化5000元加油卡) 10007(中石化任意金额充值) 10008(中石油任意金额充值)
		 * 
		 * 
		 * 加油卡类型 （1:中石化、2:中石油；默认为1)
		 */
		SHIHUA("1", "1", "中石化", "10007"), SHIYOU("2", "2", "中石油", "10008");

		private final String java_code;
		private final String data_save;
		private final String view_show;
		private final String prod_code;

		private EFuelCardType(String java_code, String data_save, String view_show, String prod_code) {
			this.java_code = java_code;
			this.data_save = data_save;
			this.view_show = view_show;
			this.prod_code = prod_code;
		}

		public String getProd_code() {
			return prod_code;
		}

		public String getJava_code() {
			return java_code;
		}

		public String getData_save() {
			return data_save;
		}

		public String getView_show() {
			return view_show;
		}

	}
}

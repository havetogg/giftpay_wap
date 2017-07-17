package org.jumutang.caranswer.compoent.impl;

import org.jumutang.caranswer.compoent.IJuHeComponent;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.service.impl.ThirdPartyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class JuHeComponentImpl implements IJuHeComponent {

	private static final Logger _LOGGER = LoggerFactory.getLogger(ThirdPartyServiceImpl.class);

	/**
	 * 聚合全局的openID
	 */
	@Value(value = "#{propertyFactoryBean['juhe.openID']}")
	private String juHeOpenID;

	// 聚合sms接口的appkey
	@Value(value = "#{propertyFactoryBean['juhe.SMS.appID']}")
	private String juHeSMSAppKey;
	// 聚合sms接口的模板id
	@Value(value = "#{propertyFactoryBean['juhe.SMS.register.TPLID']}")
	private String juHeSMSTPLID;
	//充值成功
	@Value(value = "#{propertyFactoryBean['juhe.SMS.recharge.TPLID']}")
	private String juHeSMSrechargeTPLID;
	//返款成功
	@Value(value = "#{propertyFactoryBean['juhe.SMS.returnMoney.TPLID']}")
	private String juHeSMSReturnMoneyTPLID;
	
	// 聚合sms接口请求地址
	@Value(value = "#{propertyFactoryBean['juhe.SMS.URL']}")
	private String juHeSMSSendUrl;

	@Override
	public ContextResult<String> sendSMSSecurityCode(String userTel, String securityCode) {
		ContextResult<String> cr = new ContextResult<String>();
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
	
	private ContextResult<String> sendNotice(String userTel , String tplID){
		ContextResult<String> cr = new ContextResult<String>();
		PostOrGet ps = new PostOrGet("utf-8");
		_LOGGER.info("准备向聚合发送短信验证请求！");
		String result = ps.sendGet(this.juHeSMSSendUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("mobile", userTel), new NamedValue("tpl_id", tplID),
				new NamedValue("tpl_value", ""),
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

	@Override
	public ContextResult<String> sendSMSrechargeNotice(String userTel) {
		return this.sendNotice(userTel, this.juHeSMSrechargeTPLID);
	}

	@Override
	public ContextResult<String> sendReturnmoneyNotice(String userTel) {
		return this.sendNotice(userTel, this.juHeSMSReturnMoneyTPLID);
	}
}

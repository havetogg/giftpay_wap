package org.jumutang.caranswer.compoent.impl;

import org.jumutang.caranswer.compoent.IEPayComponent;
import org.jumutang.caranswer.compoent.model.EPayPayModel;
import org.jumutang.caranswer.compoent.model.EPayPayOrderModel;
import org.jumutang.caranswer.compoent.util.AESUtil;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.epay.Digest;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.StringX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class EPayComponentImpl implements IEPayComponent {

	private static final Logger _LOGGER = LoggerFactory.getLogger(EPayComponentImpl.class);

	// 易宝支付商户编号
	@Value(value = "#{propertyFactoryBean['epay.customernumber']}")
	private String customerNumber;
	// 后台回调地址
	@Value(value = "#{propertyFactoryBean['epay.callbackurl']}")
	private String callBackUrl;
	// web回调地址
	@Value(value = "#{propertyFactoryBean['epay.webcallbackurl']}")
	private String webCallBackUrl;
	// 商户密钥
	@Value(value = "#{propertyFactoryBean['epay.keyValue']}")
	private String keyValue;
	// 支付功能请求地址
	@Value(value = "#{propertyFactoryBean['epay.payurl']}")
	private String epayUrl;
	// 订单查询请求地址
	@Value(value = "#{propertyFactoryBean['epay.queryurl']}")
	private String epayOrderUrl;
	// 支付方式
	private final String payProductType = "ONEKEY";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jumutang.caranswer.compoent.IEPayComponent#pay(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ContextResult<EPayPayModel> pay(String produceName, String iOrderID, String amount) {
		_LOGGER.info("开始请求易宝支付组件！");
		ContextResult<EPayPayModel> cr = new ContextResult<EPayPayModel>();
		EPayPayModel eppr = new EPayPayModel();
		// 封装参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("customernumber", customerNumber);
		paramJson.put("requestid", iOrderID);
		paramJson.put("amount", amount);
		paramJson.put("assure", "");
		paramJson.put("productname", produceName);
		paramJson.put("productcat", "");
		paramJson.put("productdesc", "");
		paramJson.put("divideinfo", "");
		paramJson.put("callbackurl", callBackUrl);
		paramJson.put("webcallbackurl", webCallBackUrl);
		paramJson.put("bankid", "");
		paramJson.put("period", "");
		paramJson.put("memo", "");
		// 加密签名
		_LOGGER.info(StringX.stringFormat("基本参数信息为：[0],准备进行签名加密操作！", paramJson.toJSONString()));
		String hMac = this.getHmac(new String[] { customerNumber, iOrderID, amount, "", produceName, "", "", "",
				callBackUrl, webCallBackUrl, "", "", "" }, this.keyValue);
		_LOGGER.info(StringX.stringFormat("签名加密之后的结果为：[0]", hMac));
		paramJson.put("hmac", hMac);
		_LOGGER.info("添加额外无需进行签名加密的参数信息！");
		paramJson.put("payproducttype", payProductType);
		// AES加密
		_LOGGER.info("对所有参数进行AES加密操作！");
		String keyValueForAES = this.keyValue.substring(0, 16);
		String requestData = AESUtil.encrypt(paramJson.toJSONString(), keyValueForAES);
		_LOGGER.info(StringX.stringFormat("所有参数加密完毕，加密之后的结果集为：[0]，准备请求易宝支付远程接口！", requestData));
		// 发送请求
		String resultData = new PostOrGet("utf-8").sendGet(this.epayUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("customernumber", this.customerNumber), new NamedValue("data", requestData));
		try {
			_LOGGER.info(StringX.stringFormat("易宝支付远程服务返回的结果集为：[0]，准备进行解密操作！", resultData));
			JSONObject json = JSONObject.parseObject(resultData);
			String decryptData = AESUtil.decrypt(json.getString("data"), keyValueForAES);
			_LOGGER.info(StringX.stringFormat("易宝支付远程服务返回的结果集data数据解密之后的信息为：[0],准备封装javaBean！", decryptData));
			eppr = JSONObject.parseObject(decryptData, EPayPayModel.class);
			if ("1".equals(eppr.getCode())) {
				cr.setCode(ContextContast._TRUE_CODE).setMess("调用易宝支付接口成功！").setResultObject(eppr);
			} else {
				cr.setCode(ContextContast._FALSE_CODE).setMess("调用易宝支付接口失败！").setResultObject(eppr);
			}
		} catch (Exception e) {
			_LOGGER.info("json解析失败！");
			_LOGGER.error(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess("调用易宝支付接口失败！");
		}
		return cr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.compoent.IEPayComponent#payOrder(java.lang.String)
	 */
	@Override
	public ContextResult<EPayPayOrderModel> payOrder(String iOrderID) {
		_LOGGER.info(StringX.stringFormat("用户请求订单查询组件！用户传入的iOrderID:[0]", iOrderID));
		ContextResult<EPayPayOrderModel> cr = new ContextResult<EPayPayOrderModel>();
		EPayPayOrderModel eppom = new EPayPayOrderModel();
		_LOGGER.info("处理明文参数信息！");
		JSONObject paramJson = new JSONObject();
		paramJson.put("customernumber", this.customerNumber);
		paramJson.put("requestid", iOrderID);
		_LOGGER.info("对明文参数进行签名加密处理！");
		String hMac = this.getHmac(new String[] { customerNumber, iOrderID }, this.keyValue);
		paramJson.put("hmac", hMac);
		_LOGGER.info(StringX.stringFormat("加密之后的签名数据为：[0]", hMac));
		String keyValueForAES = this.keyValue.substring(0, 16);
		String requestData = AESUtil.encrypt(paramJson.toJSONString(), keyValueForAES);
		_LOGGER.info(StringX.stringFormat("参数进行AES加密之后的结果为：[0],准备请求易宝支付的远程服务接口！", paramJson.toJSONString()));
		String resultData = new PostOrGet("utf-8").sendGet(this.epayOrderUrl, ESendHTTPModel._SEND_MODEL_ENCODE,
				new NamedValue("customernumber", this.customerNumber), new NamedValue("data", requestData));
		_LOGGER.info(StringX.stringFormat("易宝支付远程服务接口的返回值为：[0],准备进行解密操作", resultData));
		try {
			JSONObject json = JSONObject.parseObject(resultData);
			String decryptData = AESUtil.decrypt(json.getString("data"), keyValueForAES);
			_LOGGER.info(StringX.stringFormat("易宝支付接口返回的数据，data解密之后为：[0],准备封装javaBean", decryptData));
			eppom = JSONObject.parseObject(decryptData, EPayPayOrderModel.class);
			if ("1".equals(eppom.getCode())) {
				cr.setCode(ContextContast._TRUE_CODE).setMess("调用易宝支付查询订单接口成功！").setResultObject(eppom);
			} else {
				cr.setCode(ContextContast._FALSE_CODE).setMess(eppom.getMess()).setResultObject(eppom);
			}
		} catch (Exception e) {
			_LOGGER.info("解析json失败！");
			_LOGGER.info(e.getMessage(), e);
			cr.setCode(ContextContast._FALSE_CODE).setMess("调用易宝支付查询订单接口失败！");
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
	private String getHmac(String[] arr, String keyValue) {
		String hmac = "";
		StringBuffer stringValue = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			stringValue.append(StringX.nullToEmpty(arr[i]));
		}
		hmac = Digest.hmacSign(stringValue.toString(), keyValue);
		return (hmac);
	}
}

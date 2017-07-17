package org.jumutang.giftpay.entity;

/**
 * 数云网讯流量数据处理接口
 * @author 26061
 *
 */
public class Traffic {
	
	/**
	 * 订购
	 */
	public static String ORDER_TYPE_BUY = "1";
	
	/**
	 * 手机号生效方式-立即生效
	 */
	public static String EFFECT_TYPE_BUY = "1";
	
	/**
	 * 充值中
	 */
	public static String STATUS_WATING = "0";
	
	/**
	 * 充值成功
	 */
	public static String STATUS_SUCCESS = "1";
	
	/**
	 * 充值失败
	 */
	public static String STATUS_FAIL = "-1";
	
	
	
	/**
	 * 应用ID
	 */
	private String pid;
	/**
	 * 客户接入ID
	 * 网关平台分配
	 */
	private String appId;
	
	/**
	 * 密钥
	 */
	private String appSecret;
	
	/**
	 * 订单号
	 * 由客户提供，必须唯一
	 */
	private String orderId;
	
	/**
	 * 订单类型
	 * 1：订购 
	 */
	private String orderType;
	
	/**
	 * 随机字符串
	 * 生成签名使用
	 */
	private String echoStr;
	
	/**
	 * 时间戳
	 * 生成签名使用，格式：yyyyMMddHHmmss
	 */
	private String timeStamp;
	
	/**
	 * 签名
	 * Md5(AppID + OrderID + AppSecret + Echostr + Timestamp)生成32位小写字符串
	 */
	private String signature;
	
	/**
	 * 流量包编码
	 * 要订购的流量包编码
	 */
	private String packCode;
	
	/**
	 * 手机号
	 * 要订购的手机号
	 */
	private String mobile;
	
	/**
	 * 生效方式
	 * 1：立即生效
	 */
	private String effectType;

	/**
	 * 订单状态
	 */
	private String status;
	
	/**
	 * 第三方回调地址
	 */
	private String backUrl;

	/**
	 * 产品ID(外部第三方调用可能传)
	 */
	private String proId;




	/**
	 * 第三方回调地址
	 * @return
	 */
	public String getBackUrl() {
		return backUrl;
	}

	/**
	 * 第三方回调地址
	 * @param backUrl
	 */
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}


	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getEchoStr() {
		return echoStr;
	}

	public void setEchoStr(String echoStr) {
		this.echoStr = echoStr;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	
}

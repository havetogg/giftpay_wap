package org.jumutang.caranswer.compoent.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 支付接口结果集
 * @author YuanYu
 *
 */
public class EPayPayModel implements Serializable {

	private static final long serialVersionUID = -3867383411269337696L;
	// 商户编号
	@JSONField(name = "customernumber")
	private String customerNumber;
	// 商户订单号
	@JSONField(name = "requestid")
	private String iOrderID;
	// 返回码
	@JSONField(name = "code")
	private String code;
	// 易宝交易流水号
	@JSONField(name = "externalid")
	private String externalID;
	// 订单金额
	@JSONField(name = "amount")
	private String amount;
	// 支付链接
	@JSONField(name = "payurl")
	private String payUrl;
	// 绑卡 ID
	@JSONField(name = "bindid")
	private String bindID;
	// 银行编码
	@JSONField(name = "bankcode")
	private String bankCode;
	// 签名信息
	@JSONField(name = "hmac")
	private String hmac;
	// 错误信息
	@JSONField(name = "msg")
	private String mess;
	/**
	 * 获取商户编号
	 * @return 商户编号
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}
	/**
	 * 设置商户编号
	 * @param customerNumber 商户编号
	 * @return 对象本身
	 */
	public EPayPayModel setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}
	/**
	 * 获取商户订单号
	 * @return	商户订单号
	 */
	public String getiOrderID() {
		return iOrderID;
	}
	/**
	 * 设置商户订单号
	 * @param iOrderID	商户订单号
	 * @return	对象本身
	 */
	public EPayPayModel setiOrderID(String iOrderID) {
		this.iOrderID = iOrderID;
		return this;
	}
	/**
	 * 获取返回码
	 * @return 返回码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置返回码
	 * @param code	返回码
	 * @return 对象本身
	 */
	public EPayPayModel setCode(String code) {
		this.code = code;
		return this;
	}
	/**
	 * 获取易宝交易流水号
	 * @return 易宝交易流水号
	 */
	public String getExternalID() {
		return externalID;
	}
	/**
	 * 设置易宝交易流水号
	 * @param externalID	易宝交易流水号
	 * @return	对象本身
	 */
	public EPayPayModel setExternalID(String externalID) {
		this.externalID = externalID;
		return this;
	}
	/**
	 * 获取订单金额
	 * @return	订单金额
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * 设置订单金额
	 * @param amount	订单金额
	 * @return	对象本身
	 */
	public EPayPayModel setAmount(String amount) {
		this.amount = amount;
		return this;
	}
	/**
	 * 获取支付链接
	 * @return	支付链接
	 */
	public String getPayUrl() {
		return payUrl;
	}
	/**
	 * 设置支付链接
	 * @param payUrl	支付链接
	 * @return	对象本身
	 */
	public EPayPayModel setPayUrl(String payUrl) {
		this.payUrl = payUrl;
		return this;
	}
	/**
	 * 获取绑卡 ID
	 * @return	绑卡 ID
	 */
	public String getBindID() {
		return bindID;
	}
	/**
	 * 设置绑卡 ID
	 * @param bindID	绑卡 ID
	 * @return	对象本身
	 */
	public EPayPayModel setBindID(String bindID) {
		this.bindID = bindID;
		return this;
	}
	/**
	 * 获取银行编码
	 * @return	银行编码
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * 设置银行编码
	 * @param bankCode	银行编码
	 * @return	对象本身
	 */
	public EPayPayModel setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}
	/**
	 * 获取签名信息
	 * @return	签名信息
	 */
	public String getHmac() {
		return hmac;
	}
	/**
	 * 设置签名信息
	 * @param hmac	签名信息
	 * @return	对象本身
	 */
	public EPayPayModel setHmac(String hmac) {
		this.hmac = hmac;
		return this;
	}
	/**
	 * 获取错误信息
	 * @return	错误信息
	 */
	public String getMess() {
		return mess;
	}
	/**
	 * 设置错误信息
	 * @param mess	错误信息
	 * @return	对象本身
	 */
	public EPayPayModel setMess(String mess) {
		this.mess = mess;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

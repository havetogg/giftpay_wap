package org.jumutang.caranswer.compoent.model;

import java.io.Serializable;

import org.jumutang.caranswer.model.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 支付通知回调参数model
 * 
 * @author YuanYu
 *
 */
public class EPayPayNoticeModel extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4569197183913164911L;

	// 商户编号
	@JSONField(name = "customernumber")
	private String customerNumber;
	// 商户订单号
	@JSONField(name = "requestid")
	private String iOrderID;
	// 返回码
	@JSONField(name = "code")
	private String code;
	// 通知类型
	@JSONField(name = "notifytype")
	private String notifyType;
	// 易宝交易流水号
	@JSONField(name = "externalid")
	private String externalID;
	// 订单金额
	@JSONField(name = "amount")
	private String amount;
	// 暂时没有启用
	@JSONField(name = "cardno")
	private String cardNO;
	// 银行编码
	@JSONField(name = "bankcode")
	private String bankCode;
	// 签名信息
	@JSONField(name = "hmac")
	private String hmac;

	public String getCustomerNumber() {
		return customerNumber;
	}

	public EPayPayNoticeModel setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public String getiOrderID() {
		return iOrderID;
	}

	public EPayPayNoticeModel setiOrderID(String iOrderID) {
		this.iOrderID = iOrderID;
		return this;
	}

	public String getCode() {
		return code;
	}

	public EPayPayNoticeModel setCode(String code) {
		this.code = code;
		return this;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public EPayPayNoticeModel setNotifyType(String notifyType) {
		this.notifyType = notifyType;
		return this;
	}

	public String getExternalID() {
		return externalID;
	}

	public EPayPayNoticeModel setExternalID(String externalID) {
		this.externalID = externalID;
		return this;
	}

	public String getAmount() {
		return amount;
	}

	public EPayPayNoticeModel setAmount(String amount) {
		this.amount = amount;
		return this;
	}

	public String getCardNO() {
		return cardNO;
	}

	public EPayPayNoticeModel setCardNO(String cardNO) {
		this.cardNO = cardNO;
		return this;
	}

	public String getBankCode() {
		return bankCode;
	}

	public EPayPayNoticeModel setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}

	public String getHmac() {
		return hmac;
	}

	public EPayPayNoticeModel setHmac(String hmac) {
		this.hmac = hmac;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

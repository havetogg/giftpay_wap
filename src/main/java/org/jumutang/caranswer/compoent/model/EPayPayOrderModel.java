package org.jumutang.caranswer.compoent.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 查询订单状态结果集
 * 
 * @author YuanYu
 *
 */
public class EPayPayOrderModel implements Serializable {

	private static final long serialVersionUID = 4895173546904552559L;
	// 商户编号
	@JSONField(name = "customernumber")
	private String customerNumber;
	// 商户订单号
	@JSONField(name = "requestid")
	private String iOrderID;
	// 易宝交易流水号
	@JSONField(name = "externalid")
	private String externalID;
	// 订单金额
	@JSONField(name = "amount")
	private String amount;
	// 商品名称
	@JSONField(name = "productname")
	private String productName;
	// 商品类别
	@JSONField(name = "productcat")
	private String productCat;
	// 商品描述
	@JSONField(name = "productdesc")
	private String productDesc;
	// 订单状态
	@JSONField(name = "status")
	private String status;
	// 订单类型
	@JSONField(name = "ordertype")
	private String orderType;
	// 业务类型
	@JSONField(name = "busitype")
	private String busiType;
	// 下单时间
	@JSONField(name = "orderdate")
	private String orderDate;
	// 订单创建时间
	@JSONField(name = "createdate")
	private String createDate;
	// 银行通道编号
	@JSONField(name = "bankid")
	private String bankID;
	// 银行编码
	@JSONField(name = "bankcode")
	private String bankCode;
	// 用户标识
	@JSONField(name = "userno")
	private String userNO;
	// 易宝会员编号
	@JSONField(name = "memberno")
	private String memberNO;
	// 商户手续费
	@JSONField(name = "fee")
	private String fee;
	// 姓名
	@JSONField(name = "name")
	private String name;
	// 手机号
	@JSONField(name = "phone")
	private String tel;
	// 银行卡后四位
	@JSONField(name = "lastno")
	private String lastNO;
	// 签名信息
	@JSONField(name = "hmac")
	private String hMac;
	// 返回码
	@JSONField(name = "code")
	private String code;
	// 错误信息
	@JSONField(name = "msg")
	private String mess;

	/**
	 * 获取商户编号
	 * 
	 * @return 商户编号
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * 设置商户编号
	 * 
	 * @param customerNumber
	 *            商户编号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	/**
	 * 获取订单号
	 * 
	 * @return 订单号
	 */
	public String getiOrderID() {
		return iOrderID;
	}

	/**
	 * 设置订单号
	 * 
	 * @param iOrderID
	 *            订单号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setiOrderID(String iOrderID) {
		this.iOrderID = iOrderID;
		return this;
	}

	/**
	 * 获取易宝交易流水号
	 * 
	 * @return 易宝交易流水号
	 */
	public String getExternalID() {
		return externalID;
	}

	/**
	 * 设置易宝交易流水号
	 * 
	 * @param externalID
	 *            易宝交易流水号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setExternalID(String externalID) {
		this.externalID = externalID;
		return this;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * 设置订单金额
	 * 
	 * @param amount
	 *            订单金额
	 * @return 对象本身
	 */
	public EPayPayOrderModel setAmount(String amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * 获取商品名称
	 * 
	 * @return 商品名称
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * 设置商品名称
	 * 
	 * @param productName
	 *            商品名称
	 * @return 对象本身
	 */
	public EPayPayOrderModel setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	/**
	 * 获取商品类别
	 * 
	 * @return 商品类别
	 */
	public String getProductCat() {
		return productCat;
	}

	/**
	 * 设置商品类别
	 * 
	 * @param productCat
	 *            商品类别
	 * @return 对象本身
	 */
	public EPayPayOrderModel setProductCat(String productCat) {
		this.productCat = productCat;
		return this;
	}
	/**
	 * 获取商品描述
	 * @return 商品描述
	 */
	public String getProductDesc() {
		return productDesc;
	}
	/**
	 * 设置商品描述
	 * @param productDesc 商品描述
	 * @return	对象本身
	 */
	public EPayPayOrderModel setProductDesc(String productDesc) {
		this.productDesc = productDesc;
		return this;
	}
	/**
	 * 获取订单状态
	 * @return 订单状态
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置订单状态
	 * @param status 订单状态
	 * @return 对象本身
	 */
	public EPayPayOrderModel setStatus(String status) {
		this.status = status;
		return this;
	}
	/**
	 * 获取订单类型
	 * @return 订单类型
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * 设置订单类型
	 * @param orderType 订单类型
	 * @return 对象本身
	 */
	public EPayPayOrderModel setOrderType(String orderType) {
		this.orderType = orderType;
		return this;
	}
	/**
	 * 获取业务类型
	 * @return 业务类型
	 */
	public String getBusiType() {
		return busiType;
	}
	/**
	 * 设置业务类型
	 * @param busiType 业务类型
	 * @return 对象本身
	 */
	public EPayPayOrderModel setBusiType(String busiType) {
		this.busiType = busiType;
		return this;
	}
	/**
	 * 获取下单时间
	 * @return 下单时间
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * 设置下单时间
	 * @param orderDate 下单时间
	 * @return 对象本身
	 */
	public EPayPayOrderModel setOrderDate(String orderDate) {
		this.orderDate = orderDate;
		return this;
	}
	/**
	 * 获取订单创建时间
	 * @return 订单创建时间
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * 设置订单创建时间
	 * @param createDate 订单创建时间
	 * @return 对象本身
	 */
	public EPayPayOrderModel setCreateDate(String createDate) {
		this.createDate = createDate;
		return this;
	}
	/**
	 * 获取银行通道编号
	 * @return 银行通道编号
	 */
	public String getBankID() {
		return bankID;
	}
	/**
	 * 设置银行通道编号
	 * @param bankID 银行通道编号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setBankID(String bankID) {
		this.bankID = bankID;
		return this;
	}
	/**
	 * 获取银行编码
	 * @return 银行编码
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * 设置银行编码
	 * @param bankCode 银行编码
	 * @return 对象本身
	 */
	public EPayPayOrderModel setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}
	/**
	 * 获取用户标识
	 * @return 用户标识
	 */
	public String getUserNO() {
		return userNO;
	}
	/**
	 * 设置用户标识
	 * @param userNO 用户标识
	 * @return 对象本身
	 */
	public EPayPayOrderModel setUserNO(String userNO) {
		this.userNO = userNO;
		return this;
	}
	/**
	 * 获取易宝会员编号
	 * @return 易宝会员编号
	 */
	public String getMemberNO() {
		return memberNO;
	}
	/**
	 * 设置易宝会员编号
	 * @param memberNO 易宝会员编号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setMemberNO(String memberNO) {
		this.memberNO = memberNO;
		return this;
	}
	/**
	 * 获取商户手续费
	 * @return 商户手续费
	 */
	public String getFee() {
		return fee;
	}
	/**
	 * 设置商户手续费
	 * @param fee 商户手续费
	 * @return 对象本身
	 */
	public EPayPayOrderModel setFee(String fee) {
		this.fee = fee;
		return this;
	}
	/**
	 * 获取姓名
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置姓名
	 * @param name 姓名
	 * @return 对象本身
	 */
	public EPayPayOrderModel setName(String name) {
		this.name = name;
		return this;
	}
	/**
	 * 获取手机号
	 * @return 手机号
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * 设置手机号
	 * @param tel 手机号
	 * @return 对象本身
	 */
	public EPayPayOrderModel setTel(String tel) {
		this.tel = tel;
		return this;
	}
	/**
	 * 获取银行卡后四位
	 * @return 银行卡后四位
	 */
	public String getLastNO() {
		return lastNO;
	}
	/**
	 * 设置银行卡后四位
	 * @param lastNO 银行卡后四位
	 * @return 对象本身
	 */
	public EPayPayOrderModel setLastNO(String lastNO) {
		this.lastNO = lastNO;
		return this;
	}
	/**
	 * 获取签名信息
	 * @return 签名信息
	 */
	public String gethMac() {
		return hMac;
	}
	/**
	 * 设置签名信息
	 * @param hMac 签名信息
	 * @return 对象本身
	 */
	public EPayPayOrderModel sethMac(String hMac) {
		this.hMac = hMac;
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
	 * @param code 返回码 
	 * @return 对象本身
	 */
	public EPayPayOrderModel setCode(String code) {
		this.code = code;
		return this;
	}
	/**
	 * 获取错误信息
	 * @return 错误信息
	 */
	public String getMess() {
		return mess;
	}
	/**
	 * 设置错误信息
	 * @param mess 错误信息
	 * @return 对象本身
	 */
	public EPayPayOrderModel setMess(String mess) {
		this.mess = mess;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

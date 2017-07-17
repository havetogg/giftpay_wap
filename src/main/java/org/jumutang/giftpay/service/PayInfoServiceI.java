package org.jumutang.giftpay.service;

import java.util.ArrayList;
import java.util.List;

import org.jumutang.giftpay.dao.PayInfoDaoI;
import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.model.PayInfoModel;

/**
 * 交易服务层接口
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface PayInfoServiceI {
	
	/**
	 * 查询支付信息
	 * @param infoModel
	 * @return
	 */
	public List<PayInfoModel> queryPayInfos(PayInfoModel infoModel);


	public List<PayInfoModel> queryAllPayInfos(PayInfoModel infoModel);

	//九五折易加油查询
	List<PayInfoModel> queryPayInfosForOil(PayInfoModel infoModel);
	/**
	 * 更新支付信息
	 * @param payInfoModel
	 * @return
	 */
	public int updatePayInfo(PayInfoModel payInfoModel);
	
	/**
	 * 生成支付信息
	 * @param payInfoModel
	 * @return
	 */
	public int insertPayInfo(PayInfoModel payInfoModel);


	//流量充值 查询分组条件 ‘年月’
	public ArrayList<String> historyYear(PayInfoModel payInfoModel);

	//流量充值 查询流量充值订单记录
	public ArrayList<PayInfoModel> historyPayInfo(PayInfoModel payInfoModel);


//	public ArrayList<PayInfoModel> HisFlowOrderRecord(PayInfoModel payInfoModel); //原版
	public ArrayList<OrderInfoModel> HisFlowOrderRecord(OrderInfoModel orderInfoModel);  //新版


	//查询orderNo下的订单预支付金额
	public int selDealmoneyByOrderNo (String orderNo);


	//流量充值 查询当前流量充值是否成功
//	public PayInfoModel searchFlowStatus(String orderNo);
	public OrderInfoModel searchFlowStatus(String orderNo);

	/*
	* 流量充值
	* 添加 payInfo表的记录
	* */
	public int addUserRecordPayInfo(String dealId,String userId,String orderNo,String businessInfo,String dealInfo,String Time,int dealType,int dealState,double dealMoney,String mobile,int dealReaMoney);

	/**
	 * 油卡充值记录查询
	 * @author luyaunwen 
	 * @data 2017年4月10日下午5:30:57
	 * @return 
	 */
	public String fuelCardHistorySearch(String userId);




}

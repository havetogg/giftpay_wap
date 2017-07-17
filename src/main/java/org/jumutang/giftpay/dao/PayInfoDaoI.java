package org.jumutang.giftpay.dao;

import java.util.ArrayList;
import java.util.List;

import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.model.PayInfoModel;

/**
 * 交易记录余额
 * 
 * @author 鲁雨
 * @since 20170117
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface PayInfoDaoI {

	/**
	 * 查询支付信息
	 * @param infoModel
	 * @return
	 */
	public List<PayInfoModel> queryPayInfos(PayInfoModel infoModel);

	/**
	 * 查询所有近期支付信息，包括手机号
	 * @param infoModel
	 * @return
     */
	public List<PayInfoModel> queryAllPayInfos(PayInfoModel infoModel);

	//九五易加油查询
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

	//流量充值 查询当前流量充值是否成功
	public OrderInfoModel searchFlowStatus(String orderNo);

	//流量充值 查询流量充值订单记录
	public ArrayList<PayInfoModel> historyPayInfo(PayInfoModel payInfoModel);

	//更新 方法
//	public ArrayList<PayInfoModel> HisFlowOrderRecord(PayInfoModel payInfoModel);
	public ArrayList<OrderInfoModel> HisFlowOrderRecord(OrderInfoModel orderInfoModel);


	//查询orderNo下的订单预支付金额
	public int selDealmoneyByOrderNo (String orderNo);


	/**
	 * 油卡充值记录查询分组条件年月
	 * @author luyaunwen
	 * @data 2017年4月10日下午5:29:58
	 * @param payInfoModel
	 * @return
	 */
    public List<String> fuelCardHistoryYear(PayInfoModel payInfoModel);

    /**
     * 按月查询油卡充值记录
     * @author luyaunwen 
     * @data 2017年4月10日下午5:50:04
     * @param payInfoModel
     * @return
     */
    public List<PayInfoModel> fuelCardHistoryPayInfo(PayInfoModel payInfoModel);

	//更新 payInfo的数据
//	public int UpdatePayinfoByOrderNo (String orderNo,int deal_realMoney);




}

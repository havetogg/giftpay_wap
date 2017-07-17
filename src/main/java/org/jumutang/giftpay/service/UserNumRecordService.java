package org.jumutang.giftpay.service;

import java.util.ArrayList;
import java.util.List;

import org.jumutang.giftpay.model.UserNumRecord;

/**
 * 
 * @author oywj
 *
 */
public interface UserNumRecordService {
         
	public int addUserNumRecord(UserNumRecord numRecord);
	
	/**
	 * 根据号码的类型查询指定用户使用过的充值号码，可以是油卡类型，或者是手机号码类型
	 * @author luyaunwen 
	 * @data 2017年4月7日上午9:37:17
	 * @param type 号码类型
	 * @param userId 用户id
	 * @return
	 */
	public List<UserNumRecord> queryNumRecordByType(int type, String userId);
	
	/**
	 * 判断用户名与号码记录是否已经存在
	 * @author luyaunwen 
	 * @data 2017年4月7日上午10:30:44
	 * @param userId 账户id
	 * @param number 号码
	 * @return
	 */
	public boolean isNumExist(String userId, String number,int type);

	/*
	* 流量充值
	* 添加 用户充值历史记录
	* */
	public int addUserNumRecordByFlow(String uuid,int numberType,String mobile,String usreId,String number_desc,String createTime);


	/*
	* 流量充值
	* 根据用户提供的userId 查询当前用户下最近充值的5条记录
	*
	* */
	public ArrayList<UserNumRecord> historyFlowOrder(String userId,String number);

	/*
	* 流浪充值
	* 更新createTime
	* */
	public void  updateCreateTime(String userId,String number ,String createTime);


	/*
	* 流量充值
	*删除userNumRecord表的单个记录
	* */
	public boolean delUserNumRecord(String userId ,String number);

	/*
	* 流量充值
	* 删除userNumRecord表中此用户的所有记录
	* */
	public boolean delAll(String userId);


}

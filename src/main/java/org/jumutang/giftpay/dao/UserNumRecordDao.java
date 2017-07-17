package org.jumutang.giftpay.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.UserNumRecord;

public interface UserNumRecordDao {

	public int addUserNumRecord(UserNumRecord numRecord) ;
	
	public List<UserNumRecord> queryNumRecordByType(UserNumRecord numRecord);

	public int isNumExist(UserNumRecord numRecord);

	//流量充值 -- 查询当前用户流量充值的历史记录
	public ArrayList<UserNumRecord> historyFlowOrder(String userId,String number);

	//流量充值 当userNumRecord记录表中存在相同userId和number的数据时，更新createTime
	public void updateCreateTime (UserNumRecord userNumRecord);

	//流量充值 删除userNumRecord表中的记录
	public int delUserNumRecord (UserNumRecord userNumRecord);

	//流量充值 查询payInfo的记录 按月份groupBy
	public Map<String ,ArrayList<PayInfoModel>> searchPayInfoOrder();

	public int delAll(String userId);



}

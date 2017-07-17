package org.jumutang.giftpay.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jumutang.giftpay.dao.UserNumRecordDao;
import org.jumutang.giftpay.model.UserNumRecord;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userNumRecordServiceI")
public class UserNumRecordServiceImpl implements UserNumRecordService{

	@Autowired
	UserNumRecordDao userNumRecordDao;



	@Override
	public int addUserNumRecord(UserNumRecord numRecord) {
		
		return userNumRecordDao.addUserNumRecord(numRecord);
	}

	@Override
	public List<UserNumRecord> queryNumRecordByType(int type, String userId) {
		UserNumRecord numRecord = new UserNumRecord();
		numRecord.setNumberType(type);
		numRecord.setUserId(userId);
		return userNumRecordDao.queryNumRecordByType(numRecord);
	}

	@Override
	public boolean isNumExist(String userId, String number,int type) {
		UserNumRecord numRecord = new UserNumRecord();
		numRecord.setUserId(userId);
		numRecord.setNumber(number);
		numRecord.setNumberType(type);
		return userNumRecordDao.isNumExist(numRecord) > 0 ? true : false;
	}

	@Override
	public ArrayList<UserNumRecord> historyFlowOrder(String userId,String number) {

		ArrayList<UserNumRecord> list = new ArrayList<UserNumRecord>();

		list = userNumRecordDao.historyFlowOrder(userId,number);

		return list;
	}

	@Override
	public void  updateCreateTime(String userId, String number, String createTime) {
		UserNumRecord numRecord = new UserNumRecord();
		numRecord.setUserId(userId);
		numRecord.setNumber(number);
		numRecord.setCreateTime(createTime);

	    userNumRecordDao.updateCreateTime(numRecord);
	}

	@Override
	public boolean delUserNumRecord(String userId, String number) {
		UserNumRecord numRecord = new UserNumRecord();
		numRecord.setUserId(userId);
		numRecord.setNumber(number);

		return userNumRecordDao.delUserNumRecord(numRecord)>0?true:false;
	}

	@Override
	public boolean delAll(String userId) {
		
		return userNumRecordDao.delAll(userId)>0?true:false;
	}

	@Override
	public int addUserNumRecordByFlow(String uuid, int numberType,String mobile , String userId, String number_desc, String createTime) {

		UserNumRecord u = new UserNumRecord();

		u.setUserId(userId);
		u.setNumber_desc(number_desc);
		u.setCreateTime(createTime);
		u.setId(uuid);
		u.setNumberType(numberType);
		u.setNumber(mobile);

		return addUserNumRecord(u);
	}



}

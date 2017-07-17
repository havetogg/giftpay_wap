package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IUserOilInfoDao;
import org.jumutang.giftpay.model.UserOilInfoModel;
import org.jumutang.giftpay.service.IUserOilInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userOilInfoService")
public class UserOilInfoServiceImpl implements IUserOilInfoService {

	@Autowired
	private IUserOilInfoDao userOilInfoDao;

	@Override
	public int insertUserOilInfo(UserOilInfoModel userOilInfoModel) {
		return userOilInfoDao.insertUserOilInfo(userOilInfoModel);
	}

	@Override
	public int updateUserOilInfo(UserOilInfoModel userOilInfoModel) {
		return userOilInfoDao.updateUserOilInfo(userOilInfoModel);
	}

	@Override
	public UserOilInfoModel queryUserOilInfo(UserOilInfoModel userOilInfoModel) {
		return userOilInfoDao.queryUserOilInfo(userOilInfoModel);
	}

}

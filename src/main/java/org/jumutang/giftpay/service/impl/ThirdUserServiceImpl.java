package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IThirdUserDao;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.service.IThirdUserService;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("thirdUserService")
public class ThirdUserServiceImpl implements IThirdUserService {
	
	@Autowired
	private IThirdUserDao thirdUserDao;


	@Override
	public List<ThirdUserModel> queryThirdUserList(ThirdUserModel thirdUserModel) {
		return this.thirdUserDao.queryThirdUserList(thirdUserModel);
	}

	@Override
	public int addThirdUserModel(ThirdUserModel thirdUserModel) {
		thirdUserModel.settId(UUIDUtil.getUUID());
		return this.thirdUserDao.addThirdUserModel(thirdUserModel);
	}

	@Override
	public int updateThirdUserModel(ThirdUserModel thirdUserModel) {
		return this.thirdUserDao.updateThirdUserModel(thirdUserModel);
	}
}

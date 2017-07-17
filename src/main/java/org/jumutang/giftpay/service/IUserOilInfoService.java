package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.UserOilInfoModel;

public interface IUserOilInfoService {
	public int insertUserOilInfo(UserOilInfoModel userOilInfoModel);

	public UserOilInfoModel queryUserOilInfo(UserOilInfoModel userOilInfoModel);

	public int updateUserOilInfo(UserOilInfoModel userOilInfoModel);
}

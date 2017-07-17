package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.ThirdUserModel;

import java.util.List;

public interface IThirdUserService {
	List<ThirdUserModel> queryThirdUserList(ThirdUserModel thirdUserModel);

	int addThirdUserModel(ThirdUserModel thirdUserModel);

	int updateThirdUserModel(ThirdUserModel thirdUserModel);
}

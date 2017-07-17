package org.jumutang.giftpay.service.impl;

import java.util.List;

import org.jumutang.giftpay.dao.UserInfoDaoI;
import org.jumutang.giftpay.model.UserInfoModel;
import org.jumutang.giftpay.service.UserInfoServiceI;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("userInfoServiceI")
public class UserInfoServiceImpl implements UserInfoServiceI {

	@Autowired
	private UserInfoDaoI userInfoDaoI;
	/**
	 * 查询用户信息
	 * @param userInfoModel
	 * @return
	 */
	public List<UserInfoModel> queryUserInfo(UserInfoModel userInfoModel){
		return userInfoDaoI.queryUserInfo(userInfoModel);
	}
	
	/**
	 * 插入用户信息
	 * @param userInfoModel
	 * @return
	 */
	public int insertUserInfo(UserInfoModel userInfoModel){
		userInfoModel.setUserId("1");
		userInfoModel.setUserState(new Short("1"));
		userInfoModel.setUserType(new Short("1"));
		return userInfoDaoI.insertUserInfo(userInfoModel);
	}
	
	/**
	 * 更新用户信息
	 * @param userInfoModel
	 * @return
	 */
	public int updateUserInfo(UserInfoModel userInfoModel){
		return userInfoDaoI.updateUserInfo(userInfoModel);
	}
}

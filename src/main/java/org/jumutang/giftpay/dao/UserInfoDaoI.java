package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.giftpay.model.UserInfoModel;

/**
 * 用户信息dao接口
 * 
 * @author 鲁雨
 * @since 20170117
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface UserInfoDaoI {

	/**
	 * 查询用户信息
	 * @param userInfoModel
	 * @return
	 */
	public List<UserInfoModel> queryUserInfo(UserInfoModel userInfoModel);
	
	/**
	 * 插入用户信息
	 * @param userInfoModel
	 * @return
	 */
	public int insertUserInfo(UserInfoModel userInfoModel);
	
	/**
	 * 更新用户信息
	 * @param userInfoModel
	 * @return
	 */
	public int updateUserInfo(UserInfoModel userInfoModel);
}

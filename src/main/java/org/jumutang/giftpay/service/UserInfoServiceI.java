package org.jumutang.giftpay.service;

import java.util.List;

import org.jumutang.giftpay.model.UserInfoModel;

/**
 * 用户信息服务层接口
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface UserInfoServiceI {

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

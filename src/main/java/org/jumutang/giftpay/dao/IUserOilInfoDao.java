package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.UserOilInfoModel;

public interface IUserOilInfoDao {
	/**
	 * 插入用户加油信息
	 * @param userOilInfoModel
	 * @return
	 */
	public int insertUserOilInfo(UserOilInfoModel userOilInfoModel);
	
	/**
	 * 查询用户加油信息
	 * @param userOilInfoModel
	 * @return
	 */
	public UserOilInfoModel queryUserOilInfo(UserOilInfoModel userOilInfoModel);
	
	/**
	 * 更新用户加油信息
	 * @param userOilInfoModel
	 * @return
	 */
	public int updateUserOilInfo(UserOilInfoModel userOilInfoModel);
}

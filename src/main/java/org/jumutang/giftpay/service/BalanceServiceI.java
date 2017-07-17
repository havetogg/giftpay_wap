package org.jumutang.giftpay.service;

import java.util.List;

import org.jumutang.giftpay.model.BalanceModel;

/**
 * 余额服务层接口
 * 
 * @author 鲁雨
 * @since 20170118
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface BalanceServiceI {
	
	/**
	 * 账户id查询账户信息
	 * @param balanceId
	 * @return
	 */
	public List<BalanceModel> queryBalances(BalanceModel balanceModel);

	/**
	 * 插入余额时间
	 * @param balanceModel
	 * @return
	 */
	public int  insertBalace(BalanceModel balanceModel);
	
	/**
	 * 修改余额
	 * @param balanceModel
	 * @return
	 */
	public int updateBalance(BalanceModel balanceModel);


	public List<BalanceModel> queryBalancesNoNull(BalanceModel balanceModel);
}

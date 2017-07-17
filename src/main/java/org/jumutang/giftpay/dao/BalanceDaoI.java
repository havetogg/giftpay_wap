package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.giftpay.model.BalanceModel;

/**
 * 账户余额
 * 
 * @author 鲁雨
 * @since 20170117
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public interface BalanceDaoI {

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

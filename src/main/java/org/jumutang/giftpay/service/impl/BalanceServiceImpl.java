package org.jumutang.giftpay.service.impl;

import java.util.Date;
import java.util.List;

import org.jumutang.giftpay.dao.BalanceDaoI;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.tools.DateFormatUtil;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("balanceServiceI")
public class BalanceServiceImpl implements BalanceServiceI {
	
	@Autowired
	private BalanceDaoI balanceDaoI;

	/**
	 * 账户id查询账户信息
	 * @return
	 */
	public List<BalanceModel> queryBalances(BalanceModel balanceModel) {
		
		return balanceDaoI.queryBalances(balanceModel);
	}

	/**
	 * 插入余额时间
	 * @param balanceModel
	 * @return
	 */
	public int insertBalace(BalanceModel balanceModel) {
		balanceModel.setBalanceId(UUIDUtil.getUUID());
		balanceModel.setCreateTime(new Date());
		balanceModel.setBalanceState(new Short("1"));
		balanceModel.setBalanceNumber(0.0);
		balanceModel.setCongealBalance(0.0);
		return balanceDaoI.insertBalace(balanceModel);
	}

	/**
	 * 修改余额
	 * @param balanceModel
	 * @return
	 */
	public int updateBalance(BalanceModel balanceModel) {
		if(balanceModel.getChangeTime()==null){
			balanceModel.setChangeTime(DateFormatUtil.formateString());
		}
		return balanceDaoI.updateBalance(balanceModel);
	}

	@Override
	public List<BalanceModel> queryBalancesNoNull(BalanceModel balanceModel) {
		return this.balanceDaoI.queryBalancesNoNull(balanceModel);
	}

}

package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.GasPackages;

/**
 * 加油卡套餐接口
 * 
 * @author YuanYu
 *
 */
public interface IGasPackagesService {

	/**
	 * <p>
	 * 根据套餐的总返款金额+返款的月份数量来查询出指定的加油卡套餐信息
	 * </p>
	 * 
	 * @param packagesAllMoney
	 *            总返款金额
	 * @param packagesMonthCount
	 *            总返款月份数量
	 * @return 全局结果集
	 */
	public ContextResult<GasPackages> loadPackage(String packagesAllMoney, String packagesMonthCount);

}

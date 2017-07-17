package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.RedPickInfo;

/**
 * 红包提现业务
 * @author YuanYu
 *
 */
public interface IRedPickInfoService {

	
	/**
	 * <p>红包提取记录</p>
	 * <p>
	 * 根据用户主键，查询出当前用户红包提取成功的提取订单信息<br>
	 * </p>
	 * @param userID 用户主键
	 * @return 全局结果集
	 */
	public ContextResult<RedPickInfo> loadRedPickInfo(String userID);
}

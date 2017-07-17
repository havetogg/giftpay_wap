package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.RedPickDetail;

/**
 * 红包提取订单详情业务接口
 * @author YuanYu
 *
 */
public interface IRedPickDetailService {
	
	/**
	 * <p>查询红包提取订单详情</p>
	 * <p>
	 * 根据红包提取主键查询出指定提取订单的提取详情信息
	 * </p>
	 * @param redPickID 红包提取主键
	 * @return 全局结果集
	 */
	public ContextResult<RedPickDetail> loadRedPickDetail(String redPickID);
	
}

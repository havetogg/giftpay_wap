package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.GasCard;

/**
 * 加油卡信息业务接口
 * 
 * @author YuanYu
 *
 */
public interface IGasCardService {

	/**
	 * <p>
	 * 用户绑定加油卡信息
	 * </p>
	 * <p>
	 * 将用户在表单中填写的加油卡信息与用户自身进行绑定操作
	 * </p>
	 * 
	 * @param gasCard
	 *            加油卡信息
	 * @return 全局结果集
	 */
	public ContextResult<GasCard> buildGasCard(GasCard gasCard);
	
	/**
	 * 油卡变更
	 * @param gasCard
	 * @return 全局结果集
	 */
	public ContextResult<GasCard> changeGasCard(GasCard gasCard);
	
    /**
     * 加油卡信息查询
     * @param gasCard
     * @return GasCard
     */
    public ContextResult<GasCard> queryGasCard(GasCard gasCard);
	
}

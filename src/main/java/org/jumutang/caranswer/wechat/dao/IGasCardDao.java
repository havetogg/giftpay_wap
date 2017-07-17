package org.jumutang.caranswer.wechat.dao;

import org.jumutang.caranswer.model.GasCard;

/**
 * 加油卡信息
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IGasCardDao
{
    /**
     * 用户绑定油卡
     * @param gasCard
     */
    public void bindingUserCard(GasCard gasCard);
    
    
    /**
     * 用户油卡变更
     * @param gasCard
     */
    public void changeGasCard(GasCard gasCard);
    
    /**
     * 加油卡信息查询
     * @param gasCard
     * @return GasCard
     */
    public GasCard queryGasCard(GasCard gasCard);
    
    
    /**
     * 判断油卡是否绑定过 
     * @param gasCard
     * @return
     */
    public int vaildGasCard(GasCard gasCard);
}

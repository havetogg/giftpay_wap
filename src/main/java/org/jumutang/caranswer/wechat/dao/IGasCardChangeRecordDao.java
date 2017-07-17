package org.jumutang.caranswer.wechat.dao;

import org.jumutang.caranswer.model.GasCardChangeRecord;

/**
 * 油卡信息变更记录
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IGasCardChangeRecordDao
{
    /**
     * 添加油卡变更记录
     * @param gasCardChangeRecord
     */
    public void addChangeGasCardInfo(GasCardChangeRecord gasCardChangeRecord);
}

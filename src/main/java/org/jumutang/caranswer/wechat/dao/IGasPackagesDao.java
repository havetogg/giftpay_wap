package org.jumutang.caranswer.wechat.dao;

import org.jumutang.caranswer.model.GasPackages;

/**
 * 加油卡套餐维护
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IGasPackagesDao
{
    public GasPackages queryGasPackage(GasPackages gasPackage);
}

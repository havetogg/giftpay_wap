package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.model.BusinessInfo;
import org.jumutang.caranswer.model.MaintenanceInfo;

/**
 * 代办保养
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月9日
 * @see
 * @since gframe-v100
 */
public interface IMaintenanceService
{
    /**
     * 添加预约保养服务
     * @param info
     */
    public void addMaintenance(MaintenanceInfo info);
    
    /**
     * 添加商务合作
     * @param info
     */
    public void addBusiness(BusinessInfo info);
}

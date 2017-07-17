package org.jumutang.caranswer.wechat.service.impl;

import org.jumutang.caranswer.model.BusinessInfo;
import org.jumutang.caranswer.model.MaintenanceInfo;
import org.jumutang.caranswer.wechat.dao.IMaintenanceDao;
import org.jumutang.caranswer.wechat.service.IMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl implements IMaintenanceService
{
    
    @Autowired
    private IMaintenanceDao dao;
    
    @Override
    public void addMaintenance(MaintenanceInfo info)
    {
        dao.addMaintenance(info);
    }

    @Override
    public void addBusiness(BusinessInfo info)
    {
        dao.addBusiness(info);
    }

}

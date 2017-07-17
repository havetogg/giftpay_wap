package org.jumutang.caranswer.wechat.service.impl;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.GasPackages;
import org.jumutang.caranswer.wechat.dao.IGasPackagesDao;
import org.jumutang.caranswer.wechat.service.IGasPackagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GasPackagesServiceImpl implements IGasPackagesService {

    
    private static final Logger _LOGGER = LoggerFactory.getLogger(GasPackagesServiceImpl.class); 
    
    @Autowired
    private IGasPackagesDao  gasPackageDao;
    
    
    
	/*
	 * 根据套餐总返款金额 ，总返还月份 得到套餐id
	 * @see
	 * org.jumutang.caranswer.wechat.service.IGasPackagesService#loadAllPackages
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public ContextResult<GasPackages> loadPackage(String packagesAllMoney, String packagesMonthCount) {
	    
	    ContextResult<GasPackages> cr = new ContextResult<GasPackages>();
	    try
        {
            GasPackages gasPackage = new GasPackages();
            if(packagesAllMoney != null && packagesMonthCount != null)
                gasPackage.setAllMoney(Double.parseDouble(packagesAllMoney)).setMonthCount(Integer.parseInt(packagesMonthCount));
            
            _LOGGER.info(StringX.stringFormat("取得套餐id 参数=[0]", gasPackage.toJsonString()));
            
            gasPackage = gasPackageDao.queryGasPackage(gasPackage);
            
            _LOGGER.info(StringX.stringFormat("取得套餐id 结果=[0]", gasPackage.toJsonString()));
            
            cr.setCode("1").setMess("取得套餐信息成功!").setResultObject(gasPackage);
            
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            _LOGGER.error(StringX.stringFormat("取得套餐信息失败=[0]", e));
            cr.setCode("0").setMess("取得套餐信息失败");
        }
		return cr;
	}

}

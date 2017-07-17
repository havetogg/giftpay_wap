package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IOilServiceDao;
import org.jumutang.giftpay.model.OilServiceModel;
import org.jumutang.giftpay.service.OilServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 13:57 2017/5/25
 * @Modified By:
 */
@Service("OilServiceService")
public class OilServiceServiceImpl implements OilServiceService{

    @Autowired
    private IOilServiceDao iOilServiceDao;

    @Override
    public int addOilService(OilServiceModel oilServiceModel) {
        return iOilServiceDao.addOilService(oilServiceModel);
    }

    @Override
    public OilServiceModel queryOilService() {
        return iOilServiceDao.queryOilService();
    }

    @Override
    public List<OilServiceModel> queryOilServiceList() {
        return iOilServiceDao.queryOilServiceList();
    }

    @Override
    public int updateOilService(Map<String, Object> param) {
        return iOilServiceDao.updateOilService(param);
    }
}

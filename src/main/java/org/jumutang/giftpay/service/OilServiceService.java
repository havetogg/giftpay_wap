package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.OilServiceModel;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 13:56 2017/5/25
 * @Modified By:
 */
public interface OilServiceService {
    int addOilService(OilServiceModel oilServiceModel);

    OilServiceModel queryOilService();

    List<OilServiceModel> queryOilServiceList();

    int updateOilService(Map<String,Object> param);
}

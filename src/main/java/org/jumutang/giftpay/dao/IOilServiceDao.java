package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.OilServiceModel;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 13:53 2017/5/25
 * @Modified By:
 */
public interface IOilServiceDao {
    int addOilService(OilServiceModel oilServiceModel);

    OilServiceModel queryOilService();

    List<OilServiceModel> queryOilServiceList();

    int updateOilService(Map<String,Object> param);
}

package org.jumutang.giftpay.service;

import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.model.AdvertiseModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/22.
 */
public interface IAdvertiseService {
    public List<AdvertiseModel> queryAdvertiseList(AdvertiseModel advertiseModel);
    public int updateAdverClickNum(AdvertiseModel advertiseModel);
    public int addAdvertise(AdvertiseModel advertiseModel);
    public int updateAdvertId(Integer newId,Integer oldId);
}

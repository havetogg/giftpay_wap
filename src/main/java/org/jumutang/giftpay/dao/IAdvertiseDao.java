package org.jumutang.giftpay.dao;

import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.model.AdvertiseModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/22.
 */
public interface IAdvertiseDao {
    public List<AdvertiseModel> queryAdvertiseList(AdvertiseModel advertiseModel);
    public int updateAdverClickNum(AdvertiseModel advertiseModel);
	public int addAdvertise(AdvertiseModel advertiseModel);
	public int updateAdvertId(@Param("newId") Integer newId,@Param("oldId") Integer oldId);

}

package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.giftpay.model.OilSubRecordModel;


public interface IOilSubRecordDao {
	public int insertOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public OilSubRecordModel queryOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public int updateOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public List<OilSubRecordModel> queryOilSubRecordModelList(OilSubRecordModel oilSubRecordModel);
}

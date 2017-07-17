package org.jumutang.giftpay.service;

import java.util.List;

import org.jumutang.giftpay.model.OilSubRecordModel;

public interface IOilSubRecordService {
	public int insertOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public OilSubRecordModel queryOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public int updateOilSubRecordModel(OilSubRecordModel oilSubRecordModel);

	public List<OilSubRecordModel> queryOilSubRecordModelList(OilSubRecordModel oilSubRecordModel);
}

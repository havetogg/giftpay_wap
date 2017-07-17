package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.giftpay.model.OilRecordModel;

public interface IOilRecordDao {
	public int insertOilRecord(OilRecordModel oilRecordModel);

	public OilRecordModel queryOilRecordModel(OilRecordModel oilRecordModel);

	public int updateOilRecordModel(OilRecordModel oilRecordModel);

	public List<OilRecordModel> queryOilRecordModelList(OilRecordModel oilRecordModel);

	public List<OilRecordModel> queryOilRecordModelListForTask();

	public List<OilRecordModel> queryOilRecordModelListMobile(OilRecordModel oilRecordModel);
}

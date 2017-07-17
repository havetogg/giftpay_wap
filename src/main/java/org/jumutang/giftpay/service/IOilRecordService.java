package org.jumutang.giftpay.service;

import java.util.List;

import org.jumutang.giftpay.model.OilRecordModel;

public interface IOilRecordService {
	public int insertOilRecord(OilRecordModel oilRecordModel);

	public OilRecordModel queryOilRecordModel(OilRecordModel oilRecordModel);

	public int updateOilRecordModel(OilRecordModel oilRecordModel);

	public List<OilRecordModel> queryOilRecordModelList(OilRecordModel oilRecordModel);

	public List<OilRecordModel> queryOilRecordModelListForTask();


	public List<OilRecordModel> queryOilRecordModelListMobile(OilRecordModel oilRecordModel);
}

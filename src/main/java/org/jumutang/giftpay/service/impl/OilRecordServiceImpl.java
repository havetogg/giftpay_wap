package org.jumutang.giftpay.service.impl;

import java.util.List;

import org.jumutang.giftpay.dao.IOilRecordDao;
import org.jumutang.giftpay.model.OilRecordModel;
import org.jumutang.giftpay.service.IOilRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oilRecordService")
public class OilRecordServiceImpl implements IOilRecordService {

	@Autowired
	private IOilRecordDao oilRecordDao;

	@Override
	public int insertOilRecord(OilRecordModel oilRecordModel) {
		return oilRecordDao.insertOilRecord(oilRecordModel);
	}

	@Override
	public OilRecordModel queryOilRecordModel(OilRecordModel oilRecordModel) {
		return oilRecordDao.queryOilRecordModel(oilRecordModel);
	}

	@Override
	public int updateOilRecordModel(OilRecordModel oilRecordModel) {
		return oilRecordDao.updateOilRecordModel(oilRecordModel);
	}

	@Override
	public List<OilRecordModel> queryOilRecordModelList(OilRecordModel oilRecordModel) {
		return oilRecordDao.queryOilRecordModelList(oilRecordModel);
	}

	@Override
	public List<OilRecordModel> queryOilRecordModelListForTask() {
		return oilRecordDao.queryOilRecordModelListForTask();
	}

	@Override
	public List<OilRecordModel> queryOilRecordModelListMobile(OilRecordModel oilRecordModel) {
		return this.oilRecordDao.queryOilRecordModelListMobile(oilRecordModel);
	}

}

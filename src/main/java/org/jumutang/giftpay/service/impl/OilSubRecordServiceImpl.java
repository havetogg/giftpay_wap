package org.jumutang.giftpay.service.impl;

import java.util.List;

import org.jumutang.giftpay.dao.IOilSubRecordDao;
import org.jumutang.giftpay.model.OilSubRecordModel;
import org.jumutang.giftpay.service.IOilSubRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oilSubRecordService")
public class OilSubRecordServiceImpl implements IOilSubRecordService {

	@Autowired
	public IOilSubRecordDao oilSubRecordDao;
	
	@Override
	public int insertOilSubRecordModel(OilSubRecordModel oilSubRecordModel) {
		return oilSubRecordDao.insertOilSubRecordModel(oilSubRecordModel);
	}

	@Override
	public OilSubRecordModel queryOilSubRecordModel(OilSubRecordModel oilSubRecordModel) {
		return oilSubRecordDao.queryOilSubRecordModel(oilSubRecordModel);
	}

	@Override
	public int updateOilSubRecordModel(OilSubRecordModel oilSubRecordModel) {
		return oilSubRecordDao.updateOilSubRecordModel(oilSubRecordModel);
	}

	@Override
	public List<OilSubRecordModel> queryOilSubRecordModelList(OilSubRecordModel oilSubRecordModel) {
		return oilSubRecordDao.queryOilSubRecordModelList(oilSubRecordModel);
	}


}

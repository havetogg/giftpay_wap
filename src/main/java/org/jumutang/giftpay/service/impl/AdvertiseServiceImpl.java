package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IAdvertiseDao;
import org.jumutang.giftpay.model.AdvertiseModel;
import org.jumutang.giftpay.service.IAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("advertiseService")
public class AdvertiseServiceImpl implements IAdvertiseService {

	@Autowired
	private IAdvertiseDao advertiseDao;

	@Override
	public List<AdvertiseModel> queryAdvertiseList(AdvertiseModel advertiseModel) {
		return advertiseDao.queryAdvertiseList(advertiseModel);
	}

	@Override
	public int updateAdverClickNum(AdvertiseModel advertiseModel) {
		return this.advertiseDao.updateAdverClickNum(advertiseModel);
	}

	@Override
	public int addAdvertise(AdvertiseModel advertiseModel) {
		return this.advertiseDao.addAdvertise(advertiseModel);
	}

	@Override
	public int updateAdvertId( Integer newId,Integer oldId) {
		return this.advertiseDao.updateAdvertId(newId,oldId);
	}
}

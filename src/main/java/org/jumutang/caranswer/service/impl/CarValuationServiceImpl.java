package org.jumutang.caranswer.service.impl;

import java.util.List;

import org.jumutang.caranswer.model.CarValuation;
import org.jumutang.caranswer.service.CarValuationService;
import org.jumutang.giftpay.dao.CarValuationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("carValuationService")
public class CarValuationServiceImpl implements CarValuationService{

	@Autowired
	private CarValuationDao carValuationDao;
	
	@Override
	public int addValuationInfo(CarValuation carValuation) {
		return carValuationDao.addValuationInfo(carValuation);
	}

	@Override
	public  List<CarValuation> isExist(CarValuation carValuation) {
		return carValuationDao.isExist(carValuation);
	}

}

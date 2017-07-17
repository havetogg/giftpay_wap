package org.jumutang.caranswer.service.impl;

import org.jumutang.caranswer.model.SellCarInfo;
import org.jumutang.caranswer.service.SellCarInfoService;
import org.jumutang.giftpay.dao.SellCarInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sellCarInfoService")
public class SellCarInfoServiceImpl implements SellCarInfoService {
    
	@Autowired
	private SellCarInfoDao carInfoDao;
	
	@Override
	public int addCarInfo(SellCarInfo sellCarInfo) {
		return carInfoDao.addCarInfo(sellCarInfo);
	}

	@Override
	public int updateCarInfo(SellCarInfo sellCarInfo) {
		
		return carInfoDao.updateCarInfo(sellCarInfo);
	}

	@Override
	public List<SellCarInfo> queryAllSellCarInfo(SellCarInfo sellCarInfo) {
		return this.carInfoDao.queryAllSellCarInfo(sellCarInfo);
	}


}

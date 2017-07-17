package org.jumutang.giftpay.dao;

import org.jumutang.caranswer.model.SellCarInfo;

import java.util.List;

public interface SellCarInfoDao {
     
	public int addCarInfo(SellCarInfo sellCarInfo);
	public int updateCarInfo(SellCarInfo sellCarInfo);

	public List<SellCarInfo> queryAllSellCarInfo(SellCarInfo sellCarInfo);
}

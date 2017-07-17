package org.jumutang.caranswer.service;

import org.jumutang.caranswer.model.SellCarInfo;

import java.util.List;

public interface SellCarInfoService {
     
	  public int addCarInfo(SellCarInfo sellCarInfo);
	  
	  public int updateCarInfo(SellCarInfo sellCarInfo);

	public List<SellCarInfo> queryAllSellCarInfo(SellCarInfo sellCarInfo);
}

package org.jumutang.caranswer.service;

import java.util.List;

import org.jumutang.caranswer.model.CarValuation;

public interface CarValuationService {
	
	public int addValuationInfo(CarValuation carValuation);
	
	public  List<CarValuation> isExist(CarValuation carValuation);

}

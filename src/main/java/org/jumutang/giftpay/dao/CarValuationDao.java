package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.caranswer.model.CarValuation;

/**
 * 
 * @author oywj
 *
 * @Date 2017年6月14日 上午10:49:49
 */
public interface CarValuationDao {
	
	public int addValuationInfo(CarValuation carValuation);

	public List<CarValuation> isExist(CarValuation carValuation);
} 

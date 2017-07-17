package org.jumutang.giftpay.service;

import java.util.List;

import org.jumutang.giftpay.model.AdvertiseModel;
import org.jumutang.giftpay.model.RecoGoodsModel;

/**
 * 
 * @author oywj
 *
 * @Date 2017年7月6日 下午4:21:01
 */
public interface IRecoGoodsService {
	
	  public int addGoods(RecoGoodsModel goodsModel);
	  public List<RecoGoodsModel> queryGoodsList();
	  public int updateClickNum(RecoGoodsModel goodsModel);

}

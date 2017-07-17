package org.jumutang.giftpay.dao;

import java.util.List;

import org.jumutang.giftpay.model.RecoGoodsModel;

public interface IRecoGoodsDao {

	 public int addGoods(RecoGoodsModel goodsModel);

	 public List<RecoGoodsModel> queryGoodsList();

	public int updateClickNum(RecoGoodsModel goodsModel);
}

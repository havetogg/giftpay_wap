package org.jumutang.giftpay.service.impl;

import java.util.List;

import org.jumutang.giftpay.dao.IRecoGoodsDao;
import org.jumutang.giftpay.model.RecoGoodsModel;
import org.jumutang.giftpay.service.IRecoGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("recoGoodsService")
public class RecoGoodsServiceImpl  implements IRecoGoodsService{

	@Autowired
	private IRecoGoodsDao recoGoodsDao;
	
	@Override
	public int addGoods(RecoGoodsModel goodsModel) {
		return this.recoGoodsDao.addGoods(goodsModel);
	}

	@Override
	public List<RecoGoodsModel> queryGoodsList() {
		return this.recoGoodsDao.queryGoodsList();
	}

	@Override
	public int updateClickNum(RecoGoodsModel goodsModel) {
		return this.recoGoodsDao.updateClickNum(goodsModel);
	}

}

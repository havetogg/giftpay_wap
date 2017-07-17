package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.GoodsListModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/7.
 */
public interface IGoodsListModelDao {

    List<GoodsListModel> queryGoodsList(GoodsListModel goodsListModel);
    int updateGoodsMd5(GoodsListModel goodsListModel);
}

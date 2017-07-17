package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.GoodsListModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/7.
 */
public interface IGoodsListService {

    List<GoodsListModel> queryGoodsList(GoodsListModel goodsListModel);
}

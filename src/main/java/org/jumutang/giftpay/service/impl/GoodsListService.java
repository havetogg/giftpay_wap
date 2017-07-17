package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IGoodsListModelDao;
import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.service.IGoodsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/23.
 */
@Service("goodsListService")
public class GoodsListService implements IGoodsListService
{

    @Autowired
    private IGoodsListModelDao goodsListModelDao;

    @Override
    public List<GoodsListModel> queryGoodsList(GoodsListModel goodsListModel) {
        return goodsListModelDao.queryGoodsList(goodsListModel);
    }
}

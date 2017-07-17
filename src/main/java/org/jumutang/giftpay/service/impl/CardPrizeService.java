package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IPrizeDao;
import org.jumutang.giftpay.model.PrizeModel;
import org.jumutang.giftpay.service.ICardPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/23.
 */
@Service("cardPrizeService")
public class CardPrizeService implements ICardPrizeService {

    @Autowired
    private IPrizeDao prizeDao;

    @Override
    public List<PrizeModel> queryPrizeList(PrizeModel prizeModel) {
        return this.prizeDao.queryPrizeList(prizeModel);
    }

    @Override
    public int updatePrizeNum(PrizeModel prizeModel) {
        return this.prizeDao.updatePrizeNum(prizeModel);
    }

    @Override
    public int updatePrizeClickNum(PrizeModel prizeModel) {
        return this.prizeDao.updatePrizeClickNum(prizeModel);
    }
}

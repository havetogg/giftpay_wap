package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.PrizeModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/23.
 */
public interface ICardPrizeService {

    public List<PrizeModel> queryPrizeList(PrizeModel prizeModel);

    public int updatePrizeNum(PrizeModel prizeModel);
    public int updatePrizeClickNum(PrizeModel prizeModel);
}

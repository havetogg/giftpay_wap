package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.PrizeModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/5/22.
 */
public interface IPrizeDao {

    public List<PrizeModel> queryPrizeList(PrizeModel prizeModel);

    public int updatePrizeNum(PrizeModel prizeModel);
    public int updatePrizeClickNum(PrizeModel prizeModel);
}

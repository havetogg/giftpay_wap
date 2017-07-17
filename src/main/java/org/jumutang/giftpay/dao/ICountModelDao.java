package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.CountModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public interface ICountModelDao {
    public List<CountModel> queryCountModelByType(CountModel countModel);
}

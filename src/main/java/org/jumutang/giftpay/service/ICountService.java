package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.CountModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public interface ICountService {
    public List<CountModel> queryCountModelByType(CountModel countModel);
}

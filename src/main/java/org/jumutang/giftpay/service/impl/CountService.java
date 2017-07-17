package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.ICountModelDao;
import org.jumutang.giftpay.entity.CountModel;
import org.jumutang.giftpay.service.ICountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
@Service("countService")
public class CountService implements ICountService {

    @Autowired
    private ICountModelDao countModelDao;

    @Override
    public List<CountModel> queryCountModelByType(CountModel countModel) {
        return countModelDao.queryCountModelByType(countModel);
    }
}

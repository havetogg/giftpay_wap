package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IAdvertiseRecordDao;
import org.jumutang.giftpay.model.AdvertiseRecordModel;
import org.jumutang.giftpay.service.IAdvertiseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
@Service("advertiseRecordService")
public class AdvertiseRecordService implements IAdvertiseRecordService {
    @Autowired
    private IAdvertiseRecordDao advertiseRecordDao;


    @Override
    public List<AdvertiseRecordModel> queryAdvertiseRecordList(AdvertiseRecordModel advertiseRecordModel) {
        return advertiseRecordDao.queryAdvertiseRecordList(advertiseRecordModel);
    }

    @Override
    public int addAdvertiseRecordModel(AdvertiseRecordModel advertiseRecordModel) {
        return advertiseRecordDao.addAdvertiseRecordModel(advertiseRecordModel);
    }
}

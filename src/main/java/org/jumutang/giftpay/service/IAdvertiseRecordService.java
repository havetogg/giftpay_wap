package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.AdvertiseRecordModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public interface IAdvertiseRecordService {
    List<AdvertiseRecordModel> queryAdvertiseRecordList(AdvertiseRecordModel advertiseRecordModel);
    int addAdvertiseRecordModel(AdvertiseRecordModel advertiseRecordModel);
}

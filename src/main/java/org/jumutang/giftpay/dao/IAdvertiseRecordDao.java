package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.AdvertiseRecordModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public interface IAdvertiseRecordDao {
    List<AdvertiseRecordModel> queryAdvertiseRecordList(AdvertiseRecordModel advertiseRecordModel);
    int addAdvertiseRecordModel(AdvertiseRecordModel advertiseRecordModel);
}

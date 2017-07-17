package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.PVUVRecordModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/3/28.
 */
public interface IPVUVRecordDao {
    List<PVUVRecordModel> queryAllPVUVRecord();
}

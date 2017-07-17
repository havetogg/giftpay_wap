package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.PVUVRecordModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/3/28.
 */
public interface IPVUVRecordService {
    List<PVUVRecordModel> queryAllPVUVRecord();
}

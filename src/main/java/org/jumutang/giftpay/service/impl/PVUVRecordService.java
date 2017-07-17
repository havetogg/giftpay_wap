package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IPVUVRecordDao;
import org.jumutang.giftpay.entity.PVUVRecordModel;
import org.jumutang.giftpay.service.IPVUVRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/3/28.
 */
@Service("PVUVRecordService")
public class PVUVRecordService implements IPVUVRecordService {

    @Autowired
    private IPVUVRecordDao ipvuvRecordDaoImpl;

    @Override
    public List<PVUVRecordModel> queryAllPVUVRecord() {
        return this.ipvuvRecordDaoImpl.queryAllPVUVRecord();
    }
}

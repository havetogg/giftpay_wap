package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IManagerCountDao;
import org.jumutang.giftpay.entity.ManagerCountModel;
import org.jumutang.giftpay.service.IManagerCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/26.
 */
@Service("managerCountService")
public class ManagerCountServiceImpl implements IManagerCountService {

    @Autowired
    private IManagerCountDao managerCountDao;

    @Override
    public List<ManagerCountModel> queryManagerCountUserList(ManagerCountModel managerCountModel) {
        return managerCountDao.queryManagerCountUserList(managerCountModel);
    }
}

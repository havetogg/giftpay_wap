package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.ManagerCountModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/26.
 */
public interface IManagerCountService {

    List<ManagerCountModel> queryManagerCountUserList(ManagerCountModel managerCountModel);
}

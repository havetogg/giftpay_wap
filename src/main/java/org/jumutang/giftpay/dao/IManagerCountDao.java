package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.ManagerCountModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/26.
 */
public interface IManagerCountDao {
    List<ManagerCountModel> queryManagerCountUserList(ManagerCountModel managerCountModel);
}

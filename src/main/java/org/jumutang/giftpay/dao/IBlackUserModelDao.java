package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.BlackUserModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/7.
 */
public interface IBlackUserModelDao {
    List<BlackUserModel> queryBlackUserList(BlackUserModel blackUserModel);
    int addBlackUserModel(BlackUserModel blackUserModel);
}

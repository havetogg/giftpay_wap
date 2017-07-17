package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.BlackUserModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/7.
 */
public interface IBlackUserService {
    List<BlackUserModel> queryBlackUserList(BlackUserModel blackUserModel);
    int addBlackUserModel(BlackUserModel blackUserModel);
}

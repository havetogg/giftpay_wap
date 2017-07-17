package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.liftPaytment.RechargeAccountModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/4/8.
 */
public interface IRechargeAccountService {
    List<RechargeAccountModel> queryRechargeAccountList(RechargeAccountModel rechargeAccountModel);
    int addRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
    int updateRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
    int removeRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
}

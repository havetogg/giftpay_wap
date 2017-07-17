package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.liftPaytment.RechargeAccountModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/4/8.
 */
public interface IRechargeAccountDao {
    List<RechargeAccountModel> queryRechargeAccountList(RechargeAccountModel rechargeAccountModel);
    int addRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
    int updateRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
    int removeRechargeAccountModel(RechargeAccountModel rechargeAccountModel);
}

package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IRechargeAccountDao;
import org.jumutang.giftpay.model.liftPaytment.RechargeAccountModel;
import org.jumutang.giftpay.service.IFamilyService;
import org.jumutang.giftpay.service.IRechargeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by RuanYJ on 2017/4/8.
 */
@Service("rechargeAccountService")
public class RechargeAccountServiceImpl implements IRechargeAccountService {

    @Autowired
    private IRechargeAccountDao rechargeAccountDaoImpl;

    @Override
    public List<RechargeAccountModel> queryRechargeAccountList(RechargeAccountModel rechargeAccountModel) {
        if(StringUtils.isEmpty(rechargeAccountModel.getStatus())){
            rechargeAccountModel.setStatus("0");
        }
        return rechargeAccountDaoImpl.queryRechargeAccountList(rechargeAccountModel);
    }

    @Override
    public int addRechargeAccountModel(RechargeAccountModel rechargeAccountModel) {
        return rechargeAccountDaoImpl.addRechargeAccountModel(rechargeAccountModel);
    }

    @Override
    public int updateRechargeAccountModel(RechargeAccountModel rechargeAccountModel) {
        return rechargeAccountDaoImpl.updateRechargeAccountModel(rechargeAccountModel);
    }

    @Override
    public int removeRechargeAccountModel(RechargeAccountModel rechargeAccountModel) {
        return rechargeAccountDaoImpl.removeRechargeAccountModel(rechargeAccountModel);
    }
}

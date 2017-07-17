package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.PaBankLoanDao;
import org.jumutang.giftpay.entity.PaBankLoanModel;
import org.jumutang.giftpay.service.PaBankLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/30.
 */

@Service
public class PaBankLoanServiceImpl implements PaBankLoanService {


    @Autowired
    private PaBankLoanDao paBankLoanDao;

    @Override
    public int PaBankLoanAdd(PaBankLoanModel paBankLoanModel) {
        return paBankLoanDao.PaBankLoanAdd(paBankLoanModel);
    }

    @Override
    public ArrayList<PaBankLoanModel> PaBankLoanList(PaBankLoanModel paBankLoanModel) {
        return paBankLoanDao.PaBankLoanList(paBankLoanModel);
    }

    @Override
    public int PaBankLoanUpdate(PaBankLoanModel paBankLoanModel) {
        return paBankLoanDao.PaBankLoanUpdate(paBankLoanModel);
    }
}

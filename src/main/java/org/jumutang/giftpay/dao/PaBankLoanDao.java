package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.PaBankLoanModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/30.
 */
public interface PaBankLoanDao {

        //新增
        int PaBankLoanAdd(PaBankLoanModel paBankLoanModel);

        //查询
        ArrayList<PaBankLoanModel> PaBankLoanList(PaBankLoanModel paBankLoanModel);

        //修改
        int PaBankLoanUpdate(PaBankLoanModel paBankLoanModel);

}

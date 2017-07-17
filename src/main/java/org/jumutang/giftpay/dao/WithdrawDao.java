package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.WithdrawModel;

import javax.annotation.Resource;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 9:53 2017/6/8
 * @Modified By:
 */
@Resource
public interface WithdrawDao {

    int saveWithdraw(WithdrawModel withdrawModel);

    int updateWithdraw(WithdrawModel withdrawModel);

    WithdrawModel getWithdraw(WithdrawModel withdrawModel);
}

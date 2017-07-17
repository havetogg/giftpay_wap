package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.WithdrawModel;

/**
 * @Auther: Tinny.liang
 * @Description: 提现service
 * @Date: Create in 10:26 2017/6/8
 * @Modified By:
 */
public interface WithdrawService {

    int saveWithdraw(WithdrawModel withdrawModel);

    int updateWithdraw(WithdrawModel withdrawModel);

    WithdrawModel getWithdraw(WithdrawModel withdrawModel);
}

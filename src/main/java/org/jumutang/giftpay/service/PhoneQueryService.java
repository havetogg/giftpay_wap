package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.PhoneModel;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 15:06 2017/6/12
 * @Modified By:
 */
public interface PhoneQueryService {
    PhoneModel getPhone(String phone);

    int savePhone(PhoneModel phoneModel);
}

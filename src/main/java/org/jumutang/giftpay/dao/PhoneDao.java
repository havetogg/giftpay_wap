package org.jumutang.giftpay.dao;

import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.model.PhoneModel;

/**
 * @Auther: Tinny.liang
 * @Description: 手机号码归属地Dao
 * @Date: Create in 14:31 2017/6/12
 * @Modified By:
 */
public interface PhoneDao {

    PhoneModel getPhone(@Param(value="phone")String phone);

    int savePhone(PhoneModel phoneModel);
}

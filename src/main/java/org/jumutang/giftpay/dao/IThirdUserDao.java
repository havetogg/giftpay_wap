package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.ThirdUserModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/3/8.
 */
public interface IThirdUserDao  {
    List<ThirdUserModel> queryThirdUserList(ThirdUserModel thirdUserModel);
    int addThirdUserModel(ThirdUserModel thirdUserModel);
    int updateThirdUserModel(ThirdUserModel thirdUserModel);
}

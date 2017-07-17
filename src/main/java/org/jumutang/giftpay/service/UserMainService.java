package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.UserMainModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/2/8.
 */
public interface UserMainService {
    List<UserMainModel> queryUserMainModel(UserMainModel userMainModel);
    void addUserMainModel(UserMainModel userMainModel);
    int updateUserMainModel(UserMainModel userMainModel);
    List<UserMainModel> queryUserCount(UserMainModel userMainModel);
}

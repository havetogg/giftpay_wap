package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IUserMainDao;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.service.UserMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/2/8.
 */
@Service("userMainService")
public class UserMainServiceImpl implements UserMainService {

    @Autowired
    private IUserMainDao userMainDaoImpl;

    @Override
    public List<UserMainModel> queryUserMainModel(UserMainModel userMainModel) {
        return userMainDaoImpl.queryUserMainModel(userMainModel);
    }

    @Override
    public void addUserMainModel(UserMainModel userMainModel) {
        userMainDaoImpl.addUserMainModel(userMainModel);
    }

    @Override
    public int updateUserMainModel(UserMainModel userMainModel) {
       return userMainDaoImpl.updateUserMainModel(userMainModel);
    }

    @Override
    public List<UserMainModel> queryUserCount(UserMainModel userMainModel) {
        return this.userMainDaoImpl.queryUserCount(userMainModel);
    }
}

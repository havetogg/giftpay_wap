package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IUserSubDao;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.UserSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/2/8.
 */
@Service("userSubService")
public class UserSubServiceImpl implements UserSubService{

    @Autowired
    private IUserSubDao userSubDaoImpl;

    @Override
    public List<UserSubModel> queryUserSubModel(UserSubModel userSubModel) {
        return userSubDaoImpl.queryUserSubModel(userSubModel);
    }

    @Override
    public void addUserSubModel(UserSubModel userSubModel) {
        userSubDaoImpl.addUserSubModel(userSubModel);
    }

    @Override
    public int updateUserSubModel(UserSubModel userSubModel) {
       return userSubDaoImpl.updateUserSubModel(userSubModel);
    }

    @Override
    public int queryAddCountByToday() {
        return this.userSubDaoImpl.queryAddCountByToday();
    }

    @Override
    public int queryTotalAddCount() {
        return this.userSubDaoImpl.queryTotalAddCount();
    }

    @Override
    public int queryPhoneUserCountByToday() {
        return this.userSubDaoImpl.queryPhoneUserCountByToday();
    }

    @Override
    public int queryPhoneUserAllCount() {
        return this.userSubDaoImpl.queryPhoneUserAllCount();
    }

    @Override
    public int queryPhoneUserCountByZshToday() {
        return this.userSubDaoImpl.queryPhoneUserCountByZshToday();
    }

    @Override
    public int queryAllZshUserCount() {
        return this.userSubDaoImpl.queryAllZshUserCount();
    }

    @Override
    public int queryOldUserCount() {
        return this.userSubDaoImpl.queryOldUserCount();
    }


}

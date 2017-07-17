package org.jumutang.giftpay.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.model.UserSubModel;

public interface IUserSubDao {
    List<UserSubModel> queryUserSubModel(UserSubModel userSubModel);
    void addUserSubModel(UserSubModel userSubModel);
    int updateUserSubModel(UserSubModel userSubModel);


    int queryAddCountByToday();//今日新增
    int queryTotalAddCount();//累计新增
    int queryPhoneUserCountByToday();//今日新增登记手机用户
    int queryPhoneUserAllCount();//累计登记手机用户
    int queryPhoneUserCountByZshToday();//今日中石化登记用户
    int queryAllZshUserCount();//今日中石化参与人数
    int queryOldUserCount();//老用户表人数
}
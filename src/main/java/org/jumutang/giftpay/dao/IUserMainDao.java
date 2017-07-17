package org.jumutang.giftpay.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.model.UserMainModel;

public interface IUserMainDao {
    List<UserMainModel> queryUserMainModel(UserMainModel userMainModel);
    void addUserMainModel(UserMainModel userMainModel);
    int updateUserMainModel(UserMainModel userMainModel);
    List<UserMainModel> queryUserCount(UserMainModel userMainModel);
}
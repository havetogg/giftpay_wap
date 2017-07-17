package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.PaBankUser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */
public interface PaBankUserDao {

    //增加
    int PaUserAdd(PaBankUser paBankUser);

    //修改
    int PaUserUpdate(PaBankUser paBankUser);

    //查询
    ArrayList<PaBankUser> PaUserList (PaBankUser paBankUser);

}

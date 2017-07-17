package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.PaBankUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */

public interface PaBankUserService {


    //增加
    int PaUserAdd(PaBankUser paBankUser);

    //修改
    int PaUserUpdate(PaBankUser paBankUser);

    //查询
    ArrayList<PaBankUser> PaUserList (PaBankUser paBankUser);


}

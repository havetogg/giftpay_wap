package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.PaBankUserDao;
import org.jumutang.giftpay.entity.PaBankOrderinfo;
import org.jumutang.giftpay.entity.PaBankUser;
import org.jumutang.giftpay.service.PaBankOrderinfoService;
import org.jumutang.giftpay.service.PaBankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */
@Service("paBankUserService")
public class PaBankUserServiceImpl implements PaBankUserService {

    @Autowired
    private PaBankUserDao paBankUserDao;

    @Override
    public int PaUserAdd(PaBankUser paBankUser)
    {

        return paBankUserDao.PaUserAdd(paBankUser);
    }

    @Override
    public int PaUserUpdate(PaBankUser paBankUser)
    {
        return paBankUserDao.PaUserUpdate(paBankUser);
    }

    @Override
    public ArrayList<PaBankUser> PaUserList(PaBankUser paBankUser) {
        return paBankUserDao.PaUserList(paBankUser);
    }




}

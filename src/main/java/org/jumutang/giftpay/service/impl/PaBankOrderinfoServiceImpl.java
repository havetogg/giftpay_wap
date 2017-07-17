package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.PaBankOrderinfoDao;
import org.jumutang.giftpay.dao.PaBankUserDao;
import org.jumutang.giftpay.entity.PaBankOrderinfo;
import org.jumutang.giftpay.service.PaBankOrderinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */

@Service("paBankOrderinfoService")
public class PaBankOrderinfoServiceImpl implements PaBankOrderinfoService {



    @Autowired
    private PaBankOrderinfoDao paBankOrderinfoDao;

    @Override
    public int PaBankOrderinfoAdd(PaBankOrderinfo paBankOrderinfo) {
        return paBankOrderinfoDao.PaBankOrderinfoAdd (paBankOrderinfo);
    }

    @Override
    public int PaBankOrderinfoUpdate(PaBankOrderinfo paBankOrderinfo) {
        return paBankOrderinfoDao.PaBankOrderinfoUpdate(paBankOrderinfo);
    }

    @Override
    public ArrayList<PaBankOrderinfo> PaBankOrderList(PaBankOrderinfo paBankOrderinfo) {
        return paBankOrderinfoDao.PaBankOrderList( paBankOrderinfo);
    }
}

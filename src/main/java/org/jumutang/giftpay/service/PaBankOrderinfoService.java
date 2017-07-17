package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.PaBankOrderinfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */
public interface PaBankOrderinfoService {


    //添加
    int PaBankOrderinfoAdd(PaBankOrderinfo paBankOrderinfo);

    //修改
    int PaBankOrderinfoUpdate(PaBankOrderinfo paBankOrderinfo);

    //查询
    ArrayList<PaBankOrderinfo> PaBankOrderList (PaBankOrderinfo paBankOrderinfo);


}

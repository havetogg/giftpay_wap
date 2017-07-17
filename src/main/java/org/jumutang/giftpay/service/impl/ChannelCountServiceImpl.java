package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.ChannelCountDao;
import org.jumutang.giftpay.entity.ChannelCountModel;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.service.ChannelCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/22.
 */
@Service
public class ChannelCountServiceImpl implements ChannelCountService {

    @Autowired
    private ChannelCountDao channelCountDao;

    @Override
    public int channelIsExits(ChannelCountModel channelCountModel) {
        return channelCountDao.channelIsExits(channelCountModel);
    }

    @Override
    public int channelAdd(ChannelCountModel channelCountModel) {
        return channelCountDao.channelAdd(channelCountModel);
    }

    @Override
    public int channelUpdateClickNum(ChannelCountModel channelCountModel) {
        return channelCountDao.channelUpdateClickNum(channelCountModel);
    }

    @Override
    public ArrayList<ChannelCountModel> channelList(ChannelCountModel channelCountModel) {
        return channelCountDao.channelList(channelCountModel);
    }

    @Override
    public ArrayList<ThirdUserModel> searchThirdUserList(ThirdUserModel thirdUserModel) {
        return channelCountDao.searchThirdUserList( thirdUserModel);
    }

    @Override
    public ArrayList<ChannelCountModel> searchAllData() {
        return channelCountDao.searchAllData();
    }
}

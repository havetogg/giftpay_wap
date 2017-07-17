package org.jumutang.giftpay.service;

import org.jumutang.giftpay.entity.ChannelCountModel;
import org.jumutang.giftpay.entity.ThirdUserModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/22.
 */
public interface ChannelCountService {

    //查询当前渠道码是否存在
    int channelIsExits(ChannelCountModel channelCountModel);

    //新增渠道信息
    int channelAdd(ChannelCountModel channelCountModel);

    //更新当前渠道信息点击量
    int channelUpdateClickNum(ChannelCountModel channelCountModel);

    //查询所有渠道数据
    ArrayList<ChannelCountModel> channelList(ChannelCountModel channelCountModel);

    //查询 渠道码,  渠道点击率 ， 办卡数量
    ArrayList<ChannelCountModel> searchAllData();

    ArrayList<ThirdUserModel> searchThirdUserList (ThirdUserModel thirdUserModel);

}

package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.ChannelCountModel;
import org.jumutang.giftpay.entity.ThirdUserModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/22.
 */
public interface ChannelCountDao {

    //查询当前渠道码是否存在 [  //查询当前已存在渠道码数字 [已经建立渠道码用户数量统计]]
    int channelIsExits(ChannelCountModel channelCountModel);

    //新增渠道信息
    int channelAdd(ChannelCountModel channelCountModel);

    //更新当前渠道信息点击量
    int channelUpdateClickNum(ChannelCountModel channelCountModel);

    //查询所有渠道数据
    ArrayList<ChannelCountModel> channelList(ChannelCountModel channelCountModel);


    //查询 渠道码,  渠道点击率 ， 办卡数量
    ArrayList<ChannelCountModel> searchAllData();

    //查询 成功渠道下办卡用户的信息 [条件:name 用户openId，third_name 渠道码 ,phone 手机号码]
    ArrayList<ThirdUserModel> searchThirdUserList (ThirdUserModel thirdUserModel);





}

package org.jumutang.caranswer.wechat.dao;

import java.util.List;

import org.jumutang.caranswer.model.UserInfo;

/**
 * 用户信息接口
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IUserInfoDao
{
    /**
     * 查询用户信息
     * @param userInfo
     * @return UserInfo
     */
    public UserInfo queryUserInfo(UserInfo userInfo);
    
    /**
     * 添加用户
     * @param userInfo
     */
    public void addUserInfo(UserInfo userInfo);
    
    /**
     * 查询出  当日可返款的  红包信息  （20号）
     * @return
     */
    public List<UserInfo> queryRefundRecords();
    
    
    /**
     * 根据订单id查询出用户手机号码
     * @param orderID
     * @return
     */
    public String queryUserTel(String orderID);
    
}

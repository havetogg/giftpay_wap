package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.model.UserLoginRecord;

/**
 * 用户登录记录
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月1日
 * @see
 * @since gframe-v100
 */
public interface IUserLoginRecordService
{
    /**
     * 添加用户登录记录
     * @param userLogin
     */
    public void addloginRecord(UserLoginRecord userLogin);
}

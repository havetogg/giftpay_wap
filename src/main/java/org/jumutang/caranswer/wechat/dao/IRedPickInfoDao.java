package org.jumutang.caranswer.wechat.dao;

import java.util.List;

import org.jumutang.caranswer.model.RedPickInfo;

/**
 * 加油红包提现
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
public interface IRedPickInfoDao
{
    /**
     * 加油红包提现记录
     * @param userID
     * @return
     */
    public List<RedPickInfo> getRecords(String userID);
    
    /**
     * 添加红包提取信息
     * @param redPickInfo
     */
    public void addRedPickInfo(RedPickInfo redPickInfo);
    
    
    /**
     *  更新红包提取信息 ，更新第三方信息
     * @param redPickInfo
     */
    public void updateRedPickInfo(RedPickInfo redPickInfo);
    
}

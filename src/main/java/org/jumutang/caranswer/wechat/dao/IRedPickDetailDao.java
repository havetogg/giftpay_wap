package org.jumutang.caranswer.wechat.dao;

import org.jumutang.caranswer.model.RedPickDetail;

/**
 * 红包提取详情
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月1日
 * @see
 * @since gframe-v100
 */
public interface IRedPickDetailDao
{
    /**
     * 添加红包提取详情
     * @param redPickDetail
     */
    public void addRedPickDetail(RedPickDetail redPickDetail);
    
    /**
     * 修改红包提取信息
     * @param redPickDetail
     */
    public void updateRedPickDetail(String redpickid);
}

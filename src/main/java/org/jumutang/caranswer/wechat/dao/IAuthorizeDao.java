package org.jumutang.caranswer.wechat.dao;

import org.jumutang.caranswer.model.Authorize;

/**
 * 微信授权信息
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IAuthorizeDao
{
    /**
     * 查询微信授权信息
     * @param userId
     * @return
     */
    public Authorize queryAuthorize(Authorize authorize);
    
    
    public void addAuthorize(Authorize authorize);
}

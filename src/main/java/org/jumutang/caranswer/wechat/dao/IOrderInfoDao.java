package org.jumutang.caranswer.wechat.dao;

import java.util.List;

import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.caranswer.wechat.viewmodel.OrderInfoView;

/**
 * 订单信息详情
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IOrderInfoDao
{
    /**
     * 添加主订单
     * @param orderInfo
     */
    public void insertOrderInfo(OrderInfo orderInfo);
    
    /**
     * 更新订单
     * @param orderInfo
     */
    public void updateOrderInfo(OrderInfo orderInfo);
    
    
    /**
     * 查看加油红包购买记录
     * @param userID
     * @return
     */
    public List<OrderInfoView> queryOrderRecords(OrderInfo orderInfo);
    
    
    
    public OrderInfo queryOrderInfo(OrderInfo orderInfo);
    
    
}

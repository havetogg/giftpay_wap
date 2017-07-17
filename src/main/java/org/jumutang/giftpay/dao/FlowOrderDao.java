package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.FlowOrder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/5.
 */
public interface FlowOrderDao {

       //数据库新增 充值流浪 记录
       int  AddFlowOrder(FlowOrder flowOrder);

       //数据库修改 当前订单的状态
       int updateFlowStatus(String orderId);


       //查询 flowOrder 流量充值记录
       ArrayList<FlowOrder> searchFlowOrderList () ;

}

package org.jumutang.giftpay.service;

import org.jumutang.giftpay.model.OrderInfoModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/4.
 */
public interface IOrderService {
    List<OrderInfoModel> queryAllOrderInfo(OrderInfoModel orderInfo);
    int addOrderInfo(OrderInfoModel orderInfo);
}

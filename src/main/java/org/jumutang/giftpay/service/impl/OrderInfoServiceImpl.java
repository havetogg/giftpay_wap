package org.jumutang.giftpay.service.impl;

import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.giftpay.dao.IOrderModelInfoDao;
import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.service.IOrderService;
import org.jumutang.giftpay.service.IThirdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderInfoService")
public class OrderInfoServiceImpl implements IOrderService {

	@Autowired
	private IOrderModelInfoDao orderInfoDao;

	@Override
	public List<OrderInfoModel> queryAllOrderInfo(OrderInfoModel orderInfo) {
		return orderInfoDao.queryAllOrderInfo(orderInfo);
	}

	@Override
	public int addOrderInfo(OrderInfoModel orderInfo) {
		return orderInfoDao.addOrderInfo(orderInfo);
	}
}

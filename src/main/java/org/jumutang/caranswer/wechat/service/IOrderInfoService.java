package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.compoent.model.EPayPayModel;
import org.jumutang.caranswer.compoent.model.EPayPayNoticeModel;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.caranswer.wechat.viewmodel.OrderInfoView;

/**
 * 订单信息业务接口
 * 
 * @author YuanYu
 *
 */
public interface IOrderInfoService {

	/**
	 * <p>
	 * 申请支付
	 * </p>
	 * <p>
	 * step 1 : 查询当前用户是否还存在尚未完成返款的订单<br>
	 * step 1.1 : 如果存在尚未完成返款的订单，那么返回业务逻辑的异常信息 code：0 ，mess：您当前有套餐处于返款期，占时无法继续加油！
	 * step 1.2 :如果不存在尚未完成返款的订单，那么执行step 2 <br>
	 * step 1.3 : 根绝返还月份  返还金额 取得套餐id<br>
	 * <br>
	 * step 2 ：向订单表插入一条申请支付的订单信息，然后调用易宝支付的申请支付接口，获取到支付的地址信息
	 * </p>
	 * 
	 * @param userID
	 *            用户主键
	 * @param monthCount
	 *            多少个月返还
	 * @param  allMoney 
	 *            套餐总返还金额           
	 * @return 全局结果集 （包含易宝支付返回的相关信息，其中有具体的支付地址）
	 */
	public ContextResult<EPayPayModel> applyPay(String userID, String monthCount, String allMoney);

	/**
	 * <p>
	 * 确认订单具体信息
	 * </p>
	 * <p>
	 * step 1 : 当易宝支付返回的通知信息为订单失败时，直接将本地订单表的状态更新为失败即可<br>
	 * step 2 : 当易宝支付返回的通知信息为订单成功时<br>
	 * step 2.1 : 去除活动id
	 * step 2.1 : 更新订单表状态为成功，并且记录下易宝支付的订单流水号<br>
	 * step 2.2 : 向用户的红包信息表中生成当前套餐应有的红包数量，并且合理计算出红包的有效期
	 * </p>
	 * 
	 * @param userID
	 *            用户主键
	 * @param activityID
	 *            活动主键
	 * @param payNoticeModel
	 *            EPayPayNoticeModel 易宝支付确认订单信息的javaBean
	 * @return 全局结果集 （包含本地订单信息）
	 */
	public ContextResult<OrderInfo> confirmOrder(EPayPayNoticeModel payNoticeModel);
	
	
	/**
	 * 加油红包购买记录
	 * @param userID
	 * @return
	 */
	public ContextResult<OrderInfoView> queryOrderRecords(OrderInfo orderInfo);
}

package org.jumutang.caranswer.compoent;

import org.jumutang.caranswer.compoent.model.EPayPayModel;
import org.jumutang.caranswer.compoent.model.EPayPayOrderModel;
import org.jumutang.caranswer.framework.ContextResult;



/**
 * 易宝支付接口
 * @author YuanYu
 *
 */
public interface IEPayComponent {

	/**
	 * 易宝支付接口
	 * 
	 * @param produceName
	 *            产品名称
	 * @param iOrderID
	 *            订单号
	 * @param amount
	 *            支付金额
	 * @return EPayPayResult
	 */
	public ContextResult<EPayPayModel> pay(String produceName, String iOrderID, String amount);
	
	/**
	 * 订单查询接口
	 * @param iOrderID	订单编号
	 * @return EPayPayOrderModel
	 */
	public ContextResult<EPayPayOrderModel> payOrder(String iOrderID);
	
}

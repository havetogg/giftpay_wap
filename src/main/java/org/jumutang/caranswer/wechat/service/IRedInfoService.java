package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.compoent.model.OFPayOrderInfo;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.model.RedPickInfo;
import org.jumutang.caranswer.wechat.viewmodel.MyRedInfoView;
import org.jumutang.caranswer.wechat.viewmodel.RedInfoView;

/**
 * 红包信息业务接口
 * 
 * @author YuanYu
 *
 */
public interface IRedInfoService {

	/**
	 * <p>
	 * 查询红包领取信息
	 * </p>
	 * <p>
	 * 根据用户主键查询出当前用户领取过的所有红包信息<br>
	 * 注意：请勿查询出还未到有效期开始时间的红包信息
	 * </p>
	 * 
	 * @param userID
	 *            用户主键
	 * @return 全局结果集
	 */
	public ContextResult<RedInfoView> loadRedGetInfo(String userID);

	/**
	 * <p>
	 * 申请提现红包
	 * </p>
	 * <p>
	 * step 1 : 查询出所有可提取的红包信息<br>
	 * step 2 : 生成提取订单信息<br>
	 * step 3 : 生成提取订单详情信息<br>
	 * step 4 : 向欧飞发送申请充值加油卡信息
	 * </p>
	 * 
	 * @param redInfoList
	 *            红包信息集合(主要只包含了红包信息主键)
	 * @return 全局结果集
	 */
	public ContextResult<OFPayOrderInfo> applyPickRedInfo(String userID , String[] redids );
	
	/**
	 * 这里确认提取订单业务接口， 自己写一下  参照org.jumutang.caranswer.wechat.service.IOrderInfoService
	 * @param iOrderInfo
	 * @return
	 */
	public ContextResult<RedPickInfo> confirmPickRedInfo(String iOrderInfo);
	
	
	/**
	 * 我的加油红包首页  待发放 已获得 累计优惠
	 * @param userID
	 * @return
	 */
	public ContextResult<MyRedInfoView> queryAllRedMoney(String userID);
	
	
	/**
	 * 查询可提现的加油卡
	 * @param userId
	 * @return
	 */
	public ContextResult<RedInfo> canExtractRed(String userId);
	
	
	/**
	 * 加油支付回调
	 * @param ret_code
	 * @param sporder_id
	 * @param ordersuccesstime
	 * @param err_msg
	 * @param gascard_code
	 */
	public void ofCallBack(String ret_code , String sporder_id , String ordersuccesstime , String err_msg ,
            String gascard_code);
	
	
	/**
	 * 加油红包购买记录详情
	 * @param orderID 主订单id
	 * @return
	 */
	public ContextResult<RedInfo> queryOrderDetail(String orderID);
}

package org.jumutang.caranswer.wechat.dao;

import java.util.List;

import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.wechat.viewmodel.RedInfoView;

/**
 * 红包信息 [一句话功能简述]
 * <p>
 * [功能详细描述]
 * <p>
 * 
 * @author YeFei
 * @version 1.0, 2015年11月27日
 * @see
 * @since gframe-v100
 */
public interface IRedInfoDao {
	/**
	 * 查询红包领取信息
	 * 
	 * @param userID
	 * @return
	 */
	public List<RedInfoView> loadRedGetInfo(String userID);

	/**
	 * 添加红包
	 * 
	 * @param redInfo
	 */
	public void insertRedInfo(RedInfo redInfo);

	/**
	 * 待发放加油红包
	 * 
	 * @param userId
	 * @return
	 */
	public Double getNotReturnMoney(RedInfo redInfo);

	/**
	 * 累计获得的加油红包
	 * 
	 * @param userId
	 * @return
	 */
	public Double getAllRedMoney(String userId);

	/**
	 * 获得最后一次的加油红包
	 * 
	 * @param userId
	 * @return
	 */
	public Double getLastRedMoney(String userId);

	/**
	 * 获得累计享受优惠
	 * 
	 * @param userId
	 * @return
	 */
	public Double allPreferential(String userId);

	/**
	 * 查询可提现的红包
	 * 
	 * @param userid
	 * @return List<RedInfo> 红包信息
	 */
	public List<RedInfo> canExtractRed(String userId);

	/**
	 * 根绝红包主键 取得红包总金额
	 * 
	 * @param redids
	 * @return
	 */
	public Double getSumRedById(String[] redids);

	/**
	 * 更新红包信息表 如果油卡支付成功 设置deletetime
	 * 
	 * @param redInfo
	 */
	public void updateRedInfoByredpickid(String redpickid);

	/**
	 * 根据主订单查询 子订单的返还状态
	 * 
	 * @param orderID
	 * @return
	 */
	public List<RedInfo> queryOrderDetail(String orderID);

	/**
	 * 根据主订单 查找是否有子订单
	 * 
	 * @param orderID
	 * @return
	 */
	public int vaildOrderCount(String orderID);

}

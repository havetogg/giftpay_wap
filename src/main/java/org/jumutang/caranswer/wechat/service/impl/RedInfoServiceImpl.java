
package org.jumutang.caranswer.wechat.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jumutang.caranswer.compoent.IOFPayCompoent;
import org.jumutang.caranswer.compoent.model.OFPayOrderInfo;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.UniqueX;
import org.jumutang.caranswer.model.GasCard;
import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.model.RedPickDetail;
import org.jumutang.caranswer.model.RedPickInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.dao.IGasCardDao;
import org.jumutang.caranswer.wechat.dao.IRedInfoDao;
import org.jumutang.caranswer.wechat.dao.IRedPickDetailDao;
import org.jumutang.caranswer.wechat.dao.IRedPickInfoDao;
import org.jumutang.caranswer.wechat.service.IRedInfoService;
import org.jumutang.caranswer.wechat.viewmodel.MyRedInfoView;
import org.jumutang.caranswer.wechat.viewmodel.RedInfoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedInfoServiceImpl implements IRedInfoService {

	@Autowired
	private IRedInfoDao redInfoDao;

	@Autowired
	private IRedPickInfoDao redPickInfoDao;

	@Autowired
	private IRedPickDetailDao redPickDetailDao;

	@Autowired
	private IGasCardDao gasCardDao;

	@Autowired
	private IOFPayCompoent ofPayCompoent;

	private static final Logger _LOGGER = LoggerFactory.getLogger(RedInfoServiceImpl.class);

	/*
	 * 查询出用户已领取的所有红包
	 * 
	 * @see
	 * org.jumutang.caranswer.wechat.service.IRedInfoService#loadRedGetInfo(java
	 * .lang.String)
	 */
	@Override
	public ContextResult<RedInfoView> loadRedGetInfo(String userID) {

		ContextResult<RedInfoView> crv = new ContextResult<RedInfoView>();

		_LOGGER.info(StringX.stringFormat("查询用户已领取的所有红包=[0]", userID));

		List<RedInfoView> redInfoView = redInfoDao.loadRedGetInfo(userID);

		_LOGGER.info(StringX.stringFormat("用户领取红包记录条数=[0]", redInfoView.size()));
		crv.setCode(ContextContast._TRUE_CODE).setMess("查询已领取红包成功!").setResultList(redInfoView);

		return crv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.wechat.service.IRedInfoService#confirmPickRedInfo(
	 * java.lang.String)
	 */
	@Override
	public ContextResult<RedPickInfo> confirmPickRedInfo(String iOrderInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jumutang.caranswer.wechat.service.IRedInfoService#applyPickRedInfo(
	 * java.lang.String, java.util.List)
	 */
	@Override
	@Transactional
	public ContextResult<OFPayOrderInfo> applyPickRedInfo(String userID, String[] redids) {

		// 根据红包id 取得 返款的总金额
		Double redMoney = redInfoDao.getSumRedById(redids);

		_LOGGER.info(StringX.stringFormat("提现金额:[0]", redMoney.intValue()));

		// 先添加红包提取信息表
		String uuidID = UniqueX.new32UUIDUpperCaseString();
		RedPickInfo redPickInfo = new RedPickInfo().setRedpickID(uuidID).setUserID(userID).setRedNumber(redids.length)
				.setRedMoney(redMoney.intValue()).setPickMeno("油卡提现");
		_LOGGER.info(StringX.stringFormat("红包提取信息:[0]", redPickInfo.toJsonString()));
		redPickInfoDao.addRedPickInfo(redPickInfo);

		// 添加红包提取详情
		for (String redId : redids) {
			RedPickDetail redPickDetail = new RedPickDetail().setRedPickDetailID(UniqueX.new32UUIDUpperCaseString())
					.setRedPickID(uuidID).setRedID(redId);
			_LOGGER.info(StringX.stringFormat("添加红包提取详情信息:[0]", redPickDetail.toJsonString()));
			redPickDetailDao.addRedPickDetail(redPickDetail);
		}

		// 获取用户加油卡信息
		GasCard gasCard = gasCardDao.queryGasCard(new GasCard().setUserID(userID));
		_LOGGER.info(StringX.stringFormat("用户油卡信息:[0]", gasCard.toJsonString()));

		// 调用支付油卡接口
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		ContextResult<OFPayOrderInfo> oFPayOrderInfo = ofPayCompoent.applyRechargeFuelCard(redMoney.intValue() + "", uuidID, dateformat.format(new Date()),
                gasCard.getGasCardNumber(), gasCard.getBuildTel(), gasCard.getBuildUname());
		
		//判断申请欧飞油卡支付 申请订单状态   1 为成功    其他为失败,失败时修改 主子 订单状态为   申请失败  更新deletetime
		if(!"1".equals(oFPayOrderInfo.getResultObject().getRetCode()))
		{
		    redPickInfo.setStatus("0");
		    _LOGGER.info(StringX.stringFormat("申请欧飞油卡支付接口失败!更改主订单状态0:[0]", redPickInfo.toJsonString()));
		    redPickInfoDao.updateRedPickInfo(redPickInfo);
		    
		    _LOGGER.info(StringX.stringFormat("修改子订单的deletetime时间:主订单id:[0]", uuidID));
		    redPickDetailDao.updateRedPickDetail(uuidID);
		    
		}
		return oFPayOrderInfo;

	}

	/**
	 * 我的加油红包首页
	 * 
	 * @param userID
	 *            用户id
	 */
	@Override
	public ContextResult<MyRedInfoView> queryAllRedMoney(String userID) {
		ContextResult<MyRedInfoView> mycr = new ContextResult<MyRedInfoView>();
		MyRedInfoView myRedInfoView = new MyRedInfoView();
		try {
			_LOGGER.info(StringX.stringFormat("查看加油红包汇总=[0]", userID));

			// 待发放的加油红包
			Double allReturnMoney = redInfoDao.getNotReturnMoney(new RedInfo().setUserID(userID));
			// 累计获得加油红包
			Double allGetRedMoney = redInfoDao.getAllRedMoney(userID);
			// 最后一次获得的红包
			Double getLastRedMoney = redInfoDao.getLastRedMoney(userID);
			// 累计享受优惠
			Double allPreferential = redInfoDao.allPreferential(userID);

			// 待发放加油红包 减去 最后一次获得的红包
			double money = new BigDecimal(allGetRedMoney == null ? 0 : allGetRedMoney)
					.subtract(new BigDecimal(getLastRedMoney == null ? 0 : getLastRedMoney)).doubleValue();
			_LOGGER.info("money=" + money);

			myRedInfoView.setAllGetRedMoney(money).setAllPreferential(allPreferential).setAllReturnMoney(allReturnMoney)
					.setGetLastRedMoney(getLastRedMoney);
			_LOGGER.info(StringX.stringFormat("查询我的加油红包首页=[0]", myRedInfoView.toJsonString()));

			if (gasCardDao.queryGasCard(new GasCard().setUserID(userID)) != null)
				myRedInfoView.setIsBind("1");

			mycr.setCode("1").setMess("查看加油红包成功！").setResultObject(myRedInfoView);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			mycr.setCode(ContextContast._FALSE_CODE).setMess("查看加油红包失败!");
		}
		return mycr;
	}

	@Override
	public ContextResult<RedInfo> canExtractRed(String userId) {
		ContextResult<RedInfo> cr = new ContextResult<RedInfo>();
		_LOGGER.info(StringX.stringFormat("查看可提现的红包列表:[0]", userId));
		// 查询出未提现 或 提现中的红包 过滤掉提现中的红包
		List<RedInfo> list = redInfoDao.canExtractRed(userId);

		// 过滤掉提现中的红包
		//List<RedInfo> redInfoList = redInfoDao.filterPickingRedInfo(list);

		cr.setCode(ContextContast._FALSE_CODE).setMess("操作成功!").setResultList(list);

		return cr;
	}

	@Override
	@Transactional
	public void ofCallBack(String ret_code, String sporder_id, String ordersuccesstime, String err_msg,
			String gascard_code) {
		String reult = "ret_code=" + ret_code + " , sporder_id=" + sporder_id + " ,ordersuccesstime= "
				+ ordersuccesstime + " ,err_msg=" + err_msg + " ,gascard_code=" + gascard_code;

		// 根据 回调返回订单状态 更新 红包提取信息主订单的状态 并填入第三方信息
		RedPickInfo redPickInfo = new RedPickInfo().setRedpickID(sporder_id)
				.setStatus("1".equals(ret_code) ? ret_code : "0").setResult(reult).setBusinessOrderID(gascard_code);
		_LOGGER.info(StringX.stringFormat("更新红包提取信息:[0]", redPickInfo.toJsonString()));
		redPickInfoDao.updateRedPickInfo(redPickInfo);

		// 如果支付成功 取得红包信息 并设置删除时间
		if ("1".equals(ret_code)) {
			redInfoDao.updateRedInfoByredpickid(sporder_id);
		}
	}

	@Override
	public ContextResult<RedInfo> queryOrderDetail(String orderID) {
		ContextResult<RedInfo> cr = new ContextResult<RedInfo>();

		_LOGGER.info(StringX.stringFormat("查询加油红包购买记录详情,订单id:[0]", orderID));
		List<RedInfo> list = redInfoDao.queryOrderDetail(orderID);

		return cr.setCodeWithMess(ErrorCodePools._SUCCESS_1).setResultList(list);

	}

	public static void main(String[] args) {
		Double b = 1.0;

		System.out.println(b.intValue());
	}

}

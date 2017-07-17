package org.jumutang.giftpay.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jumutang.giftpay.dao.PayInfoDaoI;
import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.service.PayInfoServiceI;
import org.jumutang.giftpay.tools.DateFormatUtil;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.jumutang.giftpay.tools.UniqueX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service("/payInfoServiceI")
public class PayInfoServiceImpl implements PayInfoServiceI {
	
	Logger logger = LoggerFactory.getLogger(PayInfoServiceImpl.class);
	
	@Autowired
	private PayInfoDaoI payInfoDaoI;

	/**
	 * 查询支付信息
	 * @param infoModel
	 * @return
	 */
	public List<PayInfoModel> queryPayInfos(PayInfoModel infoModel) {
		
		return payInfoDaoI.queryPayInfos(infoModel);
	}

	@Override
	public List<PayInfoModel> queryAllPayInfos(PayInfoModel infoModel) {
		return this.payInfoDaoI.queryAllPayInfos(infoModel);
	}

	@Override
	public List<PayInfoModel> queryPayInfosForOil(PayInfoModel infoModel) {
		return this.payInfoDaoI.queryPayInfosForOil(infoModel);
	}

	/**
	 * 更新支付信息
	 * @param payInfoModel
	 * @return
	 */
	public int updatePayInfo(PayInfoModel payInfoModel) {
	
		return payInfoDaoI.updatePayInfo(payInfoModel);
	}

	/**
	 * 生成支付信息
	 * @param payInfoModel
	 * @return
	 */
	public int insertPayInfo(PayInfoModel payInfoModel) {
		payInfoModel.setDealId(UUIDUtil.getUUID());
		if(payInfoModel.getOrderNo()==null){
			payInfoModel.setOrderNo(UniqueX.randomUnique());
		}
		if(payInfoModel.getDealState()==null){
			payInfoModel.setDealState(new Short("1"));
		}
		if(payInfoModel.getDealTime()==null){
			payInfoModel.setDealTime(DateFormatUtil.formateString());
		}
		
		return payInfoDaoI.insertPayInfo(payInfoModel);
	}

	@Override
	public ArrayList<String> historyYear(PayInfoModel payInfoModel) {
		return payInfoDaoI.historyYear(payInfoModel);
	}

	@Override
	public ArrayList<PayInfoModel> historyPayInfo(PayInfoModel payInfoModel) {
		return payInfoDaoI.historyPayInfo(payInfoModel);
	}

//	@Override
//	public ArrayList<PayInfoModel> HisFlowOrderRecord(PayInfoModel payInfoModel) {
//		return payInfoDaoI.HisFlowOrderRecord(payInfoModel);
//	}

	@Override
	public ArrayList<OrderInfoModel> HisFlowOrderRecord(OrderInfoModel orderInfoModel) {
		return payInfoDaoI.HisFlowOrderRecord(orderInfoModel);
	}



	//查询订单的 预支付 金额
	@Override
	public int selDealmoneyByOrderNo(String orderNo) {
		return payInfoDaoI.selDealmoneyByOrderNo(orderNo);
	}

	/*//更新 订单的状态和实付款
	@Override
	public int UpdatePayinfoByOrderNo(String orderNo, int deal_realMoney) {
		return payInfoDaoI.UpdatePayinfoByOrderNo(orderNo,deal_realMoney);
	}
*/


	@Override
	public String fuelCardHistorySearch(String userId) {
		PayInfoModel payInfoModel = new PayInfoModel();
		payInfoModel.setAccountId(userId);
		List<String> groupTimes = payInfoDaoI.fuelCardHistoryYear(payInfoModel);
		if(groupTimes.size() == 0) {
			logger.error("===========查询油卡充值记录结果为空  groupTimes：" + groupTimes);
			return "";
		}
		JSONArray jsonArr = new JSONArray();
		Calendar calendar = Calendar.getInstance();
		String thisYear = calendar.get(Calendar.YEAR) + "";
		String thisMonth = calendar.get(Calendar.MONTH) + 1 + "月";
		for(String groupTime :groupTimes) {
			payInfoModel.setDealTime(groupTime);
			List<PayInfoModel> payInfoByTime = payInfoDaoI.fuelCardHistoryPayInfo(payInfoModel);
			String month = "";
			if(groupTime.substring(0,4).equals(thisYear)){
				month = groupTime.substring(5);//2017年04月
				if(!month.equals("10月")) {
					month = month.replace("0", "");
				}
				if(month.equals(thisMonth)) {
					month = "本月";
				}
			}else {
				month = groupTime;
			}
			JSONObject jsonObjMonth = new JSONObject();
			jsonObjMonth.put("month", month);
			JSONArray payInfoMonthJsonArray = (JSONArray) JSONArray.toJSON(payInfoByTime);
			jsonObjMonth.put("payInfo", payInfoMonthJsonArray);
			jsonArr.add(jsonObjMonth);
		}
		return jsonArr.toJSONString();
	}

	@Override
	public int addUserRecordPayInfo(String dealId, String userId, String orderNo, String businessInfo, String dealInfo, String Time, int dealType, int dealState, double dealMoney, String mobile, int dealReaMoney) {
			PayInfoModel p  = new PayInfoModel();

			p.setAccountId(userId);
			p.setDealId(dealId);
			p.setOrderNo(orderNo);
			p.setBusinessInfo(businessInfo);
			p.setDealInfo(dealInfo);
			p.setDealTime(Time);
			p.setDealType((short) dealType);
			p.setDealState((short) dealState);
			p.setDealMoney((double)dealMoney);
			p.setDealRealMoney((double)dealReaMoney);
			p.setDealRemark(mobile);


			return payInfoDaoI.insertPayInfo(p);
	}


	@Override
	public OrderInfoModel searchFlowStatus(String orderNo) {
		return payInfoDaoI.searchFlowStatus(orderNo);
	}
}

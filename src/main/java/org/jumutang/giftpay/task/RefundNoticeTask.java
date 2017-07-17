package org.jumutang.giftpay.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jumutang.giftpay.dao.IOilRecordDao;
import org.jumutang.giftpay.dao.IOilSubRecordDao;
import org.jumutang.giftpay.dao.IUserOilInfoDao;
import org.jumutang.giftpay.dao.IUserSubDao;
import org.jumutang.giftpay.model.OilRecordModel;
import org.jumutang.giftpay.model.OilSubRecordModel;
import org.jumutang.giftpay.model.UserOilInfoModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.tools.DateFormatUtil;
import org.jumutang.giftpay.tools.TemplateMessageUtil;
import org.jumutang.giftpay.tools.UniqueX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 返款通知 [一句话功能简述]
 * <p>
 * [功能详细描述]
 * <p>
 * 
 * @author YeFei
 * @version 1.0, 2015年12月4日
 * @see
 * @since gframe-v100
 */
@Component
public class RefundNoticeTask {

	private static final Logger _LOGGER = LoggerFactory.getLogger(RefundNoticeTask.class);

	@Autowired
	private IUserOilInfoDao userOilInfoDao;

	@Autowired
	private IOilRecordDao oilRecordDao;

	@Autowired
	private IOilSubRecordDao oilSubRecordDao;

	@Autowired
	private IUserSubDao iUserSubDao;

	/**
	 * 每月20号 返款通知 cron表达式：* * * * * *（共6位，使用空格隔开，具体如下） cron表达式：*(秒0-59)
	 * *(分钟0-59) *(小时0-23) *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT)
	 * 0 0 0 20 * *
	 */
	@Scheduled(cron = "0 0 0 20 * *")
	public void messageNotice() throws Exception{
		_LOGGER.info("每月20号 返款扫描!");
		List<OilRecordModel> list = oilRecordDao.queryOilRecordModelListForTask();
		Map<String,Double> returnMap = new HashMap<>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				OilRecordModel temp = list.get(i);
				int nowYear = Integer.parseInt(DateFormatUtil.formateString().substring(0, 4));
				int createYear = Integer.parseInt(temp.getCreatetime().substring(0, 4));
				int nowMonth = Integer.parseInt(DateFormatUtil.formateString().substring(5, 7));
				int createMonth = Integer.parseInt(temp.getCreatetime().substring(5, 7));
				if (nowYear > createYear||nowMonth > createMonth) {// 如果不是当月购买的加油记录，则进行返款，否则直接跳过
					UserOilInfoModel userOilInfoModel = new UserOilInfoModel();
					userOilInfoModel.setUserid(temp.getUserid());
					userOilInfoModel = userOilInfoDao.queryUserOilInfo(userOilInfoModel);
					if (userOilInfoModel == null) {
						_LOGGER.info("查无数据");
						return;
					} else {
						OilSubRecordModel oilSubRecordModel = new OilSubRecordModel();
						oilSubRecordModel.setId(UniqueX.randomUUID());
						oilSubRecordModel.setUserid(temp.getUserid());
						oilSubRecordModel.setRecordid(temp.getId());
						oilSubRecordModel.setAmount(temp.getMonthamount());
						_LOGGER.info("新增加油记录子表信息：" + oilSubRecordModel.toString());
						oilSubRecordDao.
								insertOilSubRecordModel(oilSubRecordModel);

						_LOGGER.info("更新用户加油信息before：" + userOilInfoModel.toString());
						userOilInfoModel.setBalance(Double.parseDouble(userOilInfoModel.getBalance()) + Double.parseDouble(temp.getMonthamount()) + "");// 新增用户加油信息余额
						userOilInfoModel
								.setWaitsendmoney(Double.parseDouble(userOilInfoModel.getWaitsendmoney()) - Double.parseDouble(temp.getMonthamount()) + "");
						userOilInfoModel
								.setTotalgetmoney(Double.parseDouble(userOilInfoModel.getTotalgetmoney()) + Double.parseDouble(temp.getMonthamount()) + "");
						_LOGGER.info("更新用户加油信息after：" + userOilInfoModel.toString());
						userOilInfoDao.updateUserOilInfo(userOilInfoModel);

						temp.setTermsurplus(Integer.parseInt(temp.getTermsurplus()) - 1 + "");
						if (temp.getTermsurplus().equals("0")) {
							temp.setStatus("1");
						}
						_LOGGER.info("更新加油记录信息：" + temp.toString());
						oilRecordDao.updateOilRecordModel(temp);
						String openId = temp.getOpenid();
						if(returnMap.containsKey(openId)){
							double returnMoney = returnMap.get(openId);
							returnMap.put(openId,returnMoney+Double.parseDouble(temp.getMonthamount()));
						}else{
							returnMap.put(openId,Double.parseDouble(temp.getMonthamount()));
						}
					}
				}
			}
			Iterator<Map.Entry<String, Double>> entries = returnMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Double> entry = entries.next();
				UserSubModel userSubModel = new UserSubModel();
				userSubModel.setOpenId(entry.getKey());
				UserSubModel queryModel = iUserSubDao.queryUserSubModel(userSubModel).get(0);
				String phone = queryModel.getPhone();
				TemplateMessageUtil.returnSuccess(entry.getKey(),String.valueOf(entry.getValue()),phone);
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
		}else {
			_LOGGER.info("查无数据");
		}
	}

	public static void main(String[] args) {
		int now = Integer.parseInt(DateFormatUtil.formateString().substring(5, 7));
		int createTime = Integer.parseInt("2017-03-12 00:00:00".substring(5, 7));
		System.out.println(now == createTime);
		System.out.println(createTime);
	}
}
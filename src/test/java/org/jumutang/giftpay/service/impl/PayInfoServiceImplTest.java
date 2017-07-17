package org.jumutang.giftpay.service.impl;


import org.jumutang.giftpay.service.PayInfoServiceI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/jumutang/giftpay/conf/spring/spring-core.xml" })
public class PayInfoServiceImplTest {

	@Autowired
	private PayInfoServiceI payInfoServiceImpl;
	
	/**
	 * 测试 油卡充值记录查询
	 * @author luyaunwen 
	 * @data 2017年4月11日上午11:00:55
	 */
	@Test
	public void fuelCardHistorySearch() {
		payInfoServiceImpl.fuelCardHistorySearch("testYOKA");
	}

}

package org.jumutang.caranswer.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/jumutang/caranswer/conf/spring/spring-core.xml" })
public class DemoSpringJunit4 {

	
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemoSpringJunit4.class);
	
	
	@Test
	public void test(){
		_LOGGER.info("========>>>");
	}
	
}

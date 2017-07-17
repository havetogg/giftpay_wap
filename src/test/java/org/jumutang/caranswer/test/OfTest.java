package org.jumutang.caranswer.test;

import org.jumutang.caranswer.compoent.IJuHeComponent;
import org.jumutang.caranswer.compoent.IOFPayCompoent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/jumutang/caranswer/conf/spring/spring-core.xml" })
public class OfTest
{
    @Autowired
    private IOFPayCompoent of;
    
    @Autowired
    private IJuHeComponent jh;
    
    @Test
    public void iotest()
    {
        of.applyRechargeFuelCard("499", "123000004411222", "20070323140219", "1000113200012579795", "13952635166", "王群");
        
        //jh.sendSMSrechargeNotice("13813919921");
        
        
    }
}

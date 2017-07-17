package org.jumutang.caranswer.task;

import java.util.List;

import org.jumutang.caranswer.compoent.IJuHeComponent;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.dao.IUserInfoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 返款通知 
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月4日
 * @see
 * @since gframe-v100
 */
@Component
public class RefundNoticeTask
{
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(RefundNoticeTask.class);
    
    @Autowired
    private IJuHeComponent jhComponent;
    
    @Autowired
    private IUserInfoDao userInfoDao;
    
    
    
    /**
     * 每月20号 返款通知
     * cron表达式：* * * * * *（共6位，使用空格隔开，具体如下） cron表达式：*(秒0-59) *(分钟0-59) *(小时0-23)
     * *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT)
     
     */
    @Scheduled(cron = "0 0 9 20 * *")
    public void messageNotice()
    {
        try
        {
            _LOGGER.info("每月20号 返款扫描!");
           //获取 有返款信息的所有用户 
           List<UserInfo> userInfos = userInfoDao.queryRefundRecords();
           for(UserInfo userInfo : userInfos)
           {
               _LOGGER.info(StringX.stringFormat("返款客户信息：[0]", userInfo.toJsonString()));
               jhComponent.sendReturnmoneyNotice(userInfo.getUserTel());
           }
            
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(),e);
        }
        
    }
}

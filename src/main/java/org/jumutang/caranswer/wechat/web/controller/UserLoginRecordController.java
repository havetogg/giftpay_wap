package org.jumutang.caranswer.wechat.web.controller;

import org.jumutang.caranswer.model.UserLoginRecord;
import org.jumutang.caranswer.wechat.service.IUserLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月1日
 * @see
 * @since gframe-v100
 */
@Controller
@RequestMapping(value = "/wechat/UserLoginRecordC" , method = {RequestMethod.GET , RequestMethod.POST})
public class UserLoginRecordController
{
    @Autowired
    private IUserLoginRecordService service;
    
    @RequestMapping(value ="/add" , method = {RequestMethod.GET , RequestMethod.POST})
    public void add()
    {
        System.out.println("1111111111111111111111111111111111111");
        service.addloginRecord( new UserLoginRecord().setLoginRecordID("1").setUserID("2"));
    }
}

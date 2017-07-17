package org.jumutang.caranswer.wechat.service.impl;

import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.UserLoginRecord;
import org.jumutang.caranswer.wechat.dao.IUserLoginRecordDao;
import org.jumutang.caranswer.wechat.service.IUserLoginRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginRecordServiceImpl implements IUserLoginRecordService
{
    private static final Logger _LOGGER = LoggerFactory.getLogger(UserLoginRecordServiceImpl.class);
    
    @Autowired
    private IUserLoginRecordDao userLoginRecordDao;
    
    @Override
    public void addloginRecord(UserLoginRecord userLogin)
    {
        _LOGGER.info(StringX.stringFormat("添加用户登录记录:[0]", userLogin.toJsonString()));
        userLoginRecordDao.addloginRecord(userLogin);
    }

}

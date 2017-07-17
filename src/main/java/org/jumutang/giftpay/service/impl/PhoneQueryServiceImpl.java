package org.jumutang.giftpay.service.impl;

import org.apache.commons.lang.StringUtils;
import org.jumutang.giftpay.dao.PhoneDao;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.tools.PhoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 15:08 2017/6/12
 * @Modified By:
 */
@Service
public class PhoneQueryServiceImpl implements PhoneQueryService{

    private Logger logger = LoggerFactory.getLogger(PhoneQueryServiceImpl.class);

    @Autowired
    private PhoneDao phoneDao;

    @Override
    public PhoneModel getPhone(String phone) {
        PhoneModel phoneModel = phoneDao.getPhone(phone);
        if(phoneModel == null){
            phoneModel = PhoneUtil.getPhone(phone);
            if(phoneModel!=null){
                savePhone(phoneModel);
            }
        }
        return phoneModel;
    }


    @Override
    public int savePhone(PhoneModel phoneModel) {
        return phoneDao.savePhone(phoneModel);
    }
}

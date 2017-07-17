package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IUserSubDao;
import org.jumutang.giftpay.dao.WithdrawDao;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.model.WithdrawModel;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.service.WithdrawService;
import org.jumutang.giftpay.tools.TemplateMessageUtil;
import org.jumutang.giftpay.tools.UniqueX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 10:27 2017/6/8
 * @Modified By:
 */
@Service("WithdrawService")
public class WithdrawServiceImpl implements WithdrawService{

    @Resource
    private WithdrawDao withdrawDao;

    @Autowired
    private PhoneQueryService phoneQueryService;

    @Override
    public int saveWithdraw(WithdrawModel withdrawModel) {
        withdrawModel.setWithdrawId(UniqueX.randomUnique());
        withdrawModel.setStatus((short)0);
        Date currentTime = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        withdrawModel.setCreateTime(dateTimeFormat.format(currentTime));
        try {
            PhoneModel phoneModel = phoneQueryService.getPhone(withdrawModel.getPhone());
            TemplateMessageUtil.sendWithdrawOrder(withdrawModel,phoneModel);
        }catch (Exception e){
            e.printStackTrace();
        }
        return withdrawDao.saveWithdraw(withdrawModel);
    }

    @Override
    public int updateWithdraw(WithdrawModel withdrawModel) {
        Date currentTime = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        withdrawModel.setAuditTime(dateTimeFormat.format(currentTime));
        return withdrawDao.updateWithdraw(withdrawModel);
    }

    @Override
    public WithdrawModel getWithdraw(WithdrawModel withdrawModel) {
        return withdrawDao.getWithdraw(withdrawModel);
    }
}

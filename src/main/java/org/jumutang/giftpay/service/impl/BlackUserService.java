package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IBlackUserModelDao;
import org.jumutang.giftpay.model.BlackUserModel;
import org.jumutang.giftpay.service.IBlackUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by RuanYJ on 2017/6/7.
 */
@Service("blackUserService")
public class BlackUserService implements IBlackUserService {
    @Autowired
    private IBlackUserModelDao blackUserModelDao;

    @Override
    public List<BlackUserModel> queryBlackUserList(BlackUserModel blackUserModel) {
        return blackUserModelDao.queryBlackUserList(blackUserModel);
    }

    @Override
    public int addBlackUserModel(BlackUserModel blackUserModel) {
        return blackUserModelDao.addBlackUserModel(blackUserModel);
    }
}

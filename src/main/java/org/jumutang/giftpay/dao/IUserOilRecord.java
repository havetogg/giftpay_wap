package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.entity.UserOilRecord;

import java.util.List;

/**
 * Created by RuanYJ on 2017/8/11.
 */
public interface IUserOilRecord {
    List<UserOilRecord> queryUserOilRecordList(UserOilRecord userOilRecord);
    int addUserOilRecord(UserOilRecord userOilRecord);
}

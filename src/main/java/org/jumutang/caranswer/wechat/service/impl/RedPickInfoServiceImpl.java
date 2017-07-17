package org.jumutang.caranswer.wechat.service.impl;

import java.util.List;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.model.RedPickInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.dao.IRedPickInfoDao;
import org.jumutang.caranswer.wechat.service.IRedPickInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedPickInfoServiceImpl implements IRedPickInfoService {

    @Autowired
	private IRedPickInfoDao redPickInfoDao;
    
	/*
	 * (non-Javadoc)
	 * @see org.jumutang.caranswer.wechat.service.IRedPickInfoService#loadRedPickInfo(java.lang.String)
	 */
	@Override
	public ContextResult<RedPickInfo> loadRedPickInfo(String userID) {
	    ContextResult<RedPickInfo> cr =  new ContextResult<RedPickInfo>();
	    List<RedPickInfo> list = redPickInfoDao.getRecords(userID);
	    cr.setCodeWithMess(ErrorCodePools._SUCCESS_1).setResultList(list);
		return cr;
	}

}

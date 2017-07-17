
package org.jumutang.caranswer.wechat.service.impl;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.UniqueX;
import org.jumutang.caranswer.model.GasCard;
import org.jumutang.caranswer.model.GasCardChangeRecord;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.dao.IGasCardChangeRecordDao;
import org.jumutang.caranswer.wechat.dao.IGasCardDao;
import org.jumutang.caranswer.wechat.service.IGasCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GasCardServiceImpl implements IGasCardService
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(IGasCardService.class);

    @Autowired
    private IGasCardDao gasCardDao;

    @Autowired
    private IGasCardChangeRecordDao changeGasCardDao;

    /*
     * 用户绑定油卡  绑定前判断油卡是否绑定
     * @see org.jumutang.caranswer.wechat.service.IGasCardService#buildGasCard(java.lang.String, org.jumutang.caranswer.model.GasCard)
     */
    @Override
    public ContextResult<GasCard> buildGasCard(GasCard gasCard)
    {
        ContextResult<GasCard> cr = new ContextResult<GasCard>();

        _LOGGER.info(StringX.stringFormat(StringX.stringFormat("油卡绑定请求参数bean=[0]", gasCard.toJsonString())));

        String userId = gasCard.getUserID();
        String gasCardNumber = gasCard.getGasCardNumber();

        if (gasCardDao.vaildGasCard(gasCard.setUserID(null))  > 0)
        {
            cr.setCode(ErrorCodePools._FAIL_0).setMess("油卡已经被绑定!");
            return cr;
        }

        if (gasCardDao.vaildGasCard(gasCard.setUserID(userId).setGasCardNumber(null)) > 0)
        {
            cr.setCode(ErrorCodePools._FAIL_0).setMess("该用户已经绑定过油卡!");
            return cr;
        }
        gasCard = gasCard.setGascardID(UniqueX.new32UUIDUpperCaseString()).setGasCardNumber(gasCardNumber);
        gasCardDao.bindingUserCard(gasCard);
        cr.setCode(ErrorCodePools._SUCCESS_1).setMess("油卡绑定成功!");
        _LOGGER.info(StringX.stringFormat("油卡绑定成功![0]", gasCard.toJsonString()));

        return cr;
    }

    /**
     * 油卡信息变更
     * @param gasCard  用户油卡信息
     * @return  ContextResult 结果集
     */
    @Override
    @Transactional
    public ContextResult<GasCard> changeGasCard(GasCard gasCard)
    {
        ContextResult<GasCard> cr = new ContextResult<GasCard>();
        
        String userId = gasCard.getUserID();
        String gasCardNumber = gasCard.getGasCardNumber();

        if (gasCardDao.vaildGasCard(gasCard.setUserID(null)) > 0)
        {
            cr.setCode(ErrorCodePools._FAIL_0).setMess("油卡已经被绑定!");
            return cr;
        }
        
        GasCard OldgasCard = gasCardDao.queryGasCard(gasCard.setUserID(userId).setGasCardNumber(null));
        _LOGGER.info(StringX.stringFormat("根据油卡主键查询原油卡信息,信息变更前=[0]", OldgasCard.toJsonString()));
        _LOGGER.info(StringX.stringFormat("油卡信息变更为=[0]", gasCard.setGasCardNumber(gasCardNumber).toJsonString()));
        gasCardDao.changeGasCard(gasCard);

        //插入油卡变更记录
        GasCardChangeRecord gasCardChangeRecord = new GasCardChangeRecord();
        gasCardChangeRecord.setChangeID(UniqueX.new32UUIDUpperCaseString()).setGascardID(OldgasCard.getGascardID())
                .setOldGasCard(OldgasCard.getGasCardNumber()).setNewGasCard(gasCard.getGasCardNumber());

        _LOGGER.info(StringX.stringFormat("插入油卡变更记录=[0]", gasCardChangeRecord.toJsonString()));
        changeGasCardDao.addChangeGasCardInfo(gasCardChangeRecord);
        cr.setCode(ErrorCodePools._SUCCESS_1).setMess("用户油卡信息变更成功！");
        _LOGGER.info("用户油卡信息变更成功！");

        return cr;
    }

    /**
     * 加油卡信息查询 
     * @param gasCard
     * @return gasCard
     */
    @Override
    public ContextResult<GasCard> queryGasCard(GasCard gasCard)
    {
        ContextResult<GasCard> cr = new ContextResult<GasCard>();

        _LOGGER.info(StringX.stringFormat("加油卡信息查询:[0]", gasCard.toJsonString()));
        gasCard = gasCardDao.queryGasCard(gasCard);
        cr.setCode(ErrorCodePools._SUCCESS_1).setMess("加油卡信息查询成功!").setResultObject(gasCard);

        return cr;
    }

}

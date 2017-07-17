
package org.jumutang.caranswer.wechat.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jumutang.caranswer.compoent.IEPayComponent;
import org.jumutang.caranswer.compoent.IJuHeComponent;
import org.jumutang.caranswer.compoent.model.EPayPayModel;
import org.jumutang.caranswer.compoent.model.EPayPayNoticeModel;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.UniqueX;
import org.jumutang.caranswer.model.GasPackages;
import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.dao.IGasPackagesDao;
import org.jumutang.caranswer.wechat.dao.IOrderInfoDao;
import org.jumutang.caranswer.wechat.dao.IRedInfoDao;
import org.jumutang.caranswer.wechat.dao.IUserInfoDao;
import org.jumutang.caranswer.wechat.service.IOrderInfoService;
import org.jumutang.caranswer.wechat.viewmodel.OrderInfoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInfoServiceImpl implements IOrderInfoService
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private IOrderInfoDao orderInfoDao;

    @Autowired
    private IRedInfoDao redInfoDao;

    @Autowired
    private IGasPackagesDao gasPackDao;

    @Autowired
    private IEPayComponent payComponent;
    
    @Autowired
    private IJuHeComponent juHeComponent;
    
    @Autowired
    private IUserInfoDao userInfoDao;

    /*
     * (non-Javadoc)
     * @see org.jumutang.caranswer.wechat.service.IOrderInfoService#applyPay(java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public synchronized ContextResult<EPayPayModel> applyPay(String userID , String monthCount , String allMoney)
    {

        ContextResult<EPayPayModel> cr = new ContextResult<EPayPayModel>();

        _LOGGER.info(StringX.stringFormat("查询当前用户有没有未返还订单userid:[0]", userID));
        //根据userid 和活动主键 值查找加油卡活动的 如果有未返还的 return
        Double returnMoney = redInfoDao.getNotReturnMoney(new RedInfo().setUserID(userID));
        if (null != returnMoney && returnMoney > 0)
        {
            cr.setCode(ContextContast._FALSE_CODE).setMess("您当前有套餐处于返款期，暂时无法继续加油！");
            return cr;
        }

        //查询该用户有没有已申请支付的订单  如果没有继续走  ，如果有 取得payurl直接返回   并更新该订单的deletetime
        OrderInfo info1 = orderInfoDao.queryOrderInfo(new OrderInfo().setUserID(userID).setStatus("2"));

        if (info1 != null)
        {
            _LOGGER.info(StringX.stringFormat("有申请未支付的订单:[0]", info1.toJsonString()));
            //判断订单时间 是否超过24小时   如果超过24小时  更新该订单 deletetime 继续创建新订单，如果不超过24小时，直接返回该订单的payurl
            if (new Date().getTime()  - info1.getCreateTime().getTime() > 23 * 60 * 60 * 1000 )
            {
                _LOGGER.info("原订单失效，更改状态 失败0 创建新订单！");
                orderInfoDao.updateOrderInfo(info1.setStatus("0"));
            }else
            {
                cr.setCode(ErrorCodePools._SUCCESS_1).setMess("有存在申请未支付的订单，且创建时间小于24小时!").setResultObject(new EPayPayModel().setPayUrl(info1.getPayurl()));
                return cr;
            }
            
        }

        //查询 套餐 id，根绝返还月份 总返还金额
        GasPackages gasPackage = gasPackDao.queryGasPackage(new GasPackages().setAllMoney(Double.parseDouble(allMoney))
                .setMonthCount(Integer.parseInt(monthCount)));
        if (gasPackage == null)
        {
            cr.setCode(ContextContast._FALSE_CODE).setMess("套餐不存在！");
            return cr;
        }
        _LOGGER.info(StringX.stringFormat("取得套餐信息=[0]", gasPackage.toJsonString()));
        //生成主订单
        String uuidID = UniqueX.new32UUIDUpperCaseString();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderID(uuidID).setGasPackagesID(gasPackage.getGasPackagesID()).setUserID(userID)
                .setPrice(gasPackage.getPrice());
        _LOGGER.info(StringX.stringFormat("插入主订单信息:[0]", orderInfo.toJsonString()));
        orderInfoDao.insertOrderInfo(orderInfo);

        //调用支付接口
        cr = payComponent.pay(gasPackage.getGasPackagesName(), uuidID, gasPackage.getPrice() + "");

        //将支付接口 返回的参数  payurl 更新到刚刚新建的主订单里
        orderInfo.setStatus(!"1".equals(cr.getCode()) ? "0":null);
        orderInfo.setPayurl(cr.getResultObject().getPayUrl());
        orderInfoDao.updateOrderInfo(orderInfo);

        return cr;
    }

    /*
     * (non-Javadoc)
     * @see org.jumutang.caranswer.wechat.service.IOrderInfoService#confirmOrder(java.lang.String, java.lang.String, org.jumutang.caranswer.compoent.model.EPayPayNoticeModel)
     */
    @Override
    @Transactional
    public ContextResult<OrderInfo> confirmOrder(EPayPayNoticeModel payNoticeModel)
    {
        try
        {
            // _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
            _LOGGER.info(StringX.stringFormat("回调bean对象:[0]", payNoticeModel.toJsonString()));

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderID(payNoticeModel.getiOrderID()).setBusinessOrderID(payNoticeModel.getExternalID())
                    .setResult(payNoticeModel.toJsonString());
            // .setUserID(userInfo.getUserID());
            //判断返回状态 更改主订单状态  1为成功
            if ("1".equals(payNoticeModel.getCode()))
                orderInfo.setStatus("1");
            if (!"1".equals(payNoticeModel.getCode()))
                orderInfo.setStatus("0");

            _LOGGER.info(StringX.stringFormat("支付回调 更新主订单bean:[0]", orderInfo.toJsonString()));
            orderInfoDao.updateOrderInfo(orderInfo);

            //如果充值成功  需添加红包信息  失败直接返回
            if (!"1".equals(payNoticeModel.getCode()))
            {
                _LOGGER.info("支付失败!");
                return null;
            }

            if (redInfoDao.vaildOrderCount(orderInfo.getOrderID()) > 0)
            {
                _LOGGER.info("该主订单已经创建过!");
                return null;
            }

            //根据订单查询  套餐信息
            orderInfo = orderInfoDao.queryOrderInfo(orderInfo);
            GasPackages gasPackage = gasPackDao.queryGasPackage(new GasPackages().setGasPackagesID(orderInfo
                    .getGasPackagesID()));

            double money = new BigDecimal(gasPackage.getAllMoney().doubleValue()).subtract(
                    new BigDecimal(gasPackage.getPrice().doubleValue())).doubleValue();

            //每月返还
            double monthReturnRed = new BigDecimal(gasPackage.getAllMoney().doubleValue()).divide(
                    new BigDecimal(gasPackage.getMonthCount()), 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

            _LOGGER.info(StringX.stringFormat("该订单套餐信息:[0]", gasPackage.toJsonString()));

            //String monthReturnRed = (gasPackage.getAllMoney()/gasPackage.getMonthCount()) + "";  //每月返还红包金额

            //每月利润
            String monthpreferential = new BigDecimal(money).divide(new BigDecimal(gasPackage.getMonthCount()), 2,
                    BigDecimal.ROUND_HALF_EVEN).doubleValue()
                    + "";

            RedInfo redInfo = null;
            //设置失效时间为永久
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2099);
            //计算生效时间
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DATE, 20);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);

            for (int i = 0; i < gasPackage.getMonthCount(); i++)
            {
                if (cal1.get(Calendar.MONTH) == 11)
                {
                    cal1.set(Calendar.YEAR, cal1.get(Calendar.YEAR) + 1);
                    cal1.set(Calendar.MONTH, 0);
                }
                else
                {
                    cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) + 1);
                }

                redInfo = new RedInfo();
                redInfo.setRedID(UniqueX.new32UUIDUpperCaseString()).setUserID(orderInfo.getUserID())
                        .setOrderID(orderInfo.getOrderID()).setRedName(gasPackage.getGasPackagesName())
                        .setRedMoney(monthReturnRed + "").setStartTime(cal1.getTime()).setEndTime(cal.getTime())
                        .setPreferential(monthpreferential);
                _LOGGER.info(StringX.stringFormat("添加红包信息:[0]", redInfo.toJsonString()));
                redInfoDao.insertRedInfo(redInfo);
            }
            
            //支付成功发送通知短信 
            String useTel = userInfoDao.queryUserTel(orderInfo.getOrderID());
            _LOGGER.info(StringX.stringFormat("该订单用户手机号:[0]", useTel));
            juHeComponent.sendSMSrechargeNotice(useTel);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 加油红包购买记录
     */
    @Override
    public ContextResult<OrderInfoView> queryOrderRecords(OrderInfo orderInfo)
    {
        ContextResult<OrderInfoView> cr = new ContextResult<OrderInfoView>();
        _LOGGER.info(StringX.stringFormat("查看加油红包购买记录=[0]", orderInfo.toJsonString()));

        List<OrderInfoView> list = orderInfoDao.queryOrderRecords(orderInfo);
        cr.setCode(ContextContast._TRUE_CODE).setMess("查看加油红包购买记录成功！").setResultList(list);

        return cr;
    }

}

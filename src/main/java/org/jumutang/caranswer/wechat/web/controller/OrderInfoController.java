
package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.compoent.model.EPayPayModel;
import org.jumutang.caranswer.compoent.model.EPayPayNoticeModel;
import org.jumutang.caranswer.compoent.util.AESUtil;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.caranswer.model.RedInfo;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IOrderInfoService;
import org.jumutang.caranswer.wechat.service.IRedInfoService;
import org.jumutang.caranswer.wechat.viewmodel.OrderInfoView;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 订单详情
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
@RequestMapping(value = "/wechat/orderInfoC", method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class OrderInfoController
{
    private static final Logger _LOGGER = LoggerFactory.getLogger(OrderInfoController.class);

    @Autowired
    private IOrderInfoService orderInfoService;
    
    @Autowired
    private IRedInfoService redInfoService;

    // 商户密钥
    @Value(value = "#{propertyFactoryBean['epay.keyValue']}")
    private String keyValue;

    /**
     * 加油支付
     * @param session
     * @param monthCount 多少个月
     * @param allMoney 套餐总返还金额
     * @return
     */
    @RequestMapping(value = "/buyOrder", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String buyOrder(HttpSession session , @RequestParam(value = "monthCount") String monthCount ,
            @RequestParam(value = "allMoney") String allMoney)
    {
        ContextResult<EPayPayModel> cr = new ContextResult<EPayPayModel>();
        try
        {
            
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);

            _LOGGER.info(StringX.stringFormat("申请加油支付,[0]:[1]", monthCount, allMoney));

            cr = orderInfoService.applyPay(userInfo.getUserID(), monthCount, allMoney);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
            cr.setCode(ContextContast._FALSE_CODE).setMess("加油支付申请报错!");
        }

        return cr.toJsonString();
    }

    
    /**
     * 支付接口回调地址
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payCallBack" , method = {RequestMethod.GET , RequestMethod.POST})
    public String payCallBack(HttpSession session,@RequestParam(value = "data" , required = true) String data)
    {
        try {
			String decrptData = AESUtil.decrypt(data, keyValue.substring(0, 16));
			EPayPayNoticeModel payNoticeModel = JSONObject.parseObject(decrptData, EPayPayNoticeModel.class);
			orderInfoService.confirmOrder(payNoticeModel);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			return "FAIL";
		}
        return "SUCCESS";
    }
    
    
    /**
     * 加油红包购买记录
     * @param session
     * @param orderID 订单主键 可为空，若不为空 ，则取订单详情
     * @return
     */
    @RequestMapping(value = "/queryOrderRecords" , method ={RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public String queryOrderRecords(HttpSession session,
            @RequestParam(value ="orderID" , required = false) String orderID)
    {
        ContextResult<OrderInfoView> cr = new ContextResult<OrderInfoView>();
        try
        {
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
            _LOGGER.info(StringX.stringFormat("当前登录用户:[0]", userInfo.toJsonString()));
            OrderInfo orderInfo = new OrderInfo().setUserID(userInfo.getUserID());
            if(orderID != null) orderInfo.setOrderID(orderID);
            cr = orderInfoService.queryOrderRecords(orderInfo);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
            cr.setCode(ContextContast._FALSE_CODE).setMess("加油红包购买记录查询失败!");
        }
        
        return cr.toJsonString();
    }
    
    
    /**
     * 加油红包购买记录 (返还信息)
     * @param session
     * @return
     */
    @RequestMapping(value = "/queryOrderDetail" , method ={RequestMethod.GET , RequestMethod.POST})
    @ResponseBody
    public String queryOrderDetail(HttpSession session,
            @RequestParam(value = "orderID") String orderID)
    {
        ContextResult<RedInfo> cr = new ContextResult<RedInfo>();
        try
        {
            cr = redInfoService.queryOrderDetail(orderID);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(),e);
            cr.setCodeWithMess(ErrorCodePools._FAIL_0);
        }
        return cr.toJsonString();
    }
    
}  

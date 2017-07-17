package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.GasCard;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IGasCardService;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 油卡信息
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
@Controller
@RequestMapping(value = "/wechat/gasCard" , method = {RequestMethod.GET , RequestMethod.POST})
public class GasCardController
{
    private static final Logger _LOGGER = LoggerFactory.getLogger(GasCardController.class);
    
    @Autowired
    private IGasCardService gasCardService;
    
    /**
     * 绑定油卡信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/buildGasCard" , method = {RequestMethod.GET , RequestMethod.POST})
    public String buildGasCard(HttpSession session,
            @RequestParam(value = "gasCardNumber" , required = true) String gasCardNumber,
            @RequestParam(value = "buildTel" , required = true) String buildTel,
            @RequestParam(value = "buildUname" , required = true) String buildUname)
    {
        ContextResult<GasCard> cr =new ContextResult<GasCard>();
        
        try
        {
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
            _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
            
            GasCard gasCard = new GasCard()
                    .setUserID(userInfo.getUserID())
                    .setGasCardNumber(gasCardNumber)
                    .setBuildTel(buildTel)
                    .setBuildUname(buildUname);
            _LOGGER.info(StringX.stringFormat("油卡信息绑定:[0]", gasCard.toJsonString()));
             cr = gasCardService.buildGasCard(gasCard);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
            cr.setCode(ContextContast._FALSE_CODE).setMess("绑定油卡信息失败!");
        }
        return cr.toJsonString();
    }
    
    
    /**
     * 查询油卡信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryGasCard" , method = {RequestMethod.GET , RequestMethod.POST})
    public String queryGasCard(HttpSession session)
    {
        ContextResult<GasCard> cr =new ContextResult<GasCard>();
        
        try
        {
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
            _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
            cr = gasCardService.queryGasCard(new GasCard().setUserID(userInfo.getUserID()));
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage() , e);
            cr.setCodeWithMess(ErrorCodePools._FAIL_0);
        }
        
        return cr.toJsonString();
    }
    
    
    /**
     * 油卡信息更改
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeGasCard" , method = {RequestMethod.GET , RequestMethod.POST})
    public String changeGasCard(HttpSession session,
            @RequestParam(value = "gasCardNumber" , required = true) String gasCardNumber,
            @RequestParam(value = "buildTel" , required = true) String buildTel,
            @RequestParam(value = "buildUname" , required = true) String buildUname)
    {
        ContextResult<GasCard> cr =new ContextResult<GasCard>();
        
        try
        {
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
            _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
            
            GasCard gasCard = new GasCard()
                    .setUserID(userInfo.getUserID())
                    .setGasCardNumber(gasCardNumber)
                    .setBuildTel(buildTel)
                    .setBuildUname(buildUname);
             cr = gasCardService.changeGasCard(gasCard);
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
            cr.setCode(ContextContast._FALSE_CODE).setMess("油卡信息变更失败!");
        }
        return cr.toJsonString();
    }
}

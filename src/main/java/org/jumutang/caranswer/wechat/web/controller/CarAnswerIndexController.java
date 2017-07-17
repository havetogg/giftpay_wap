
package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IRedInfoService;
import org.jumutang.caranswer.wechat.service.IUserInfoService;
import org.jumutang.caranswer.wechat.viewmodel.MyRedInfoView;
import org.jumutang.caranswer.wechat.web.controller.util.SessionCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 车答应首页
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月30日
 * @see
 * @since gframe-v100
 */
@RequestMapping(value = "/wechat/carAnswerIndexC", method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class CarAnswerIndexController
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(CarAnswerIndexController.class);

    @Autowired
    private IRedInfoService redInfoService;
    
    @Autowired
    private IUserInfoService userInfoServce;

    /**
     * 车答应首页  ，获取待发放加油红包金额
     * @param session
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/getAllReturnRed" , method = {RequestMethod.GET , RequestMethod.POST})
    public String getAllReturnRed(HttpSession session)
    {
        ContextResult<MyRedInfoView> cr = new ContextResult<MyRedInfoView>();
        try
        {
            UserInfo userInfo = SessionCacheUtil.loadCurrentLoginUserInfo(session);
            if(userInfo == null)
            {
                WeChatUserInfo wechatUserInfo = SessionCacheUtil.loadWechatUserInfo(session);
                _LOGGER.info(StringX.stringFormat("微信授权信息:[0]", wechatUserInfo.toJsonString()));
              //判断登录用户是否注册   未注册提示前台
                if(userInfoServce.loginValidation(wechatUserInfo.getOpenID()).getResultObject() == null)
                {
                    cr.setCodeWithMess(ErrorCodePools._FAIL_10001);
                    return cr.toJsonString();
                }
            }
            
            _LOGGER.info(StringX.stringFormat("当前登录信息:[0]", userInfo.toJsonString()));
            //取待发放红包总金额
           
            cr = redInfoService.queryAllRedMoney(userInfo.getUserID());
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage(), e);
            cr.setCode(ContextContast._FALSE_CODE).setMess("系统错误!");
        }
        return cr.toJsonString();
    }

}

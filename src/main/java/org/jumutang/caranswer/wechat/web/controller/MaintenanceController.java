package org.jumutang.caranswer.wechat.web.controller;

import javax.servlet.http.HttpSession;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.UniqueX;
import org.jumutang.caranswer.model.BusinessInfo;
import org.jumutang.caranswer.model.MaintenanceInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.service.IMaintenanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月9日
 * @see
 * @since gframe-v100
 */
@Controller
@RequestMapping(value = "/wechat", method = { RequestMethod.GET, RequestMethod.POST })
public class MaintenanceController
{
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(MaintenanceController.class);
    
    @Autowired
    private IMaintenanceService mainService;
    
    @ResponseBody
    @RequestMapping(value = "/addMaintenance" , method = {RequestMethod.GET , RequestMethod.POST})
    public String addMaintenance(HttpSession session ,
                                @RequestParam(value = "userTel" , required = true) String userTel,
                                @RequestParam(value = "carType" , required = true) String carType,
                                @RequestParam(value = "serviceType" , required = true) String serviceType)
    {
        ContextResult<MaintenanceInfo> cr = new ContextResult<MaintenanceInfo>();
        _LOGGER.info(StringX.stringFormat("serviceType:[0]", serviceType));
        try
        {
            MaintenanceInfo info = new MaintenanceInfo()
                                    .setUserTel(userTel)
                                    .setCarType(carType)
                                    .setServiceType(serviceType)
                                    .setMaintenanceID(UniqueX.new32UUIDUpperCaseString());
                _LOGGER.info(StringX.stringFormat("申请代办保养:[0]", info.toJsonString()));
                mainService.addMaintenance(info);
            cr.setCode(ErrorCodePools._SUCCESS_1).setMess("申请代办保养成功!");
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage() , e);
            cr.setCodeWithMess(ErrorCodePools._FAIL_0);
        }
        
        return cr.toJsonString();
    }
    
    
    /**
     * 添加商务合作
     * @param session
     * @param userTel
     * @param userName
     * @param context
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBusiness" , method = {RequestMethod.GET , RequestMethod.POST})
    public String addBusiness(HttpSession session,
                              @RequestParam(value = "userTel" , required = true) String userTel,
                              @RequestParam(value = "userName" , required = true) String userName,
                              @RequestParam(value = "context" , required = true) String context)
    {
        ContextResult<Object> cr = new ContextResult<Object>();
        try
        {
            BusinessInfo info = new BusinessInfo()
                                .setBusinessID(UniqueX.new32UUIDUpperCaseString())
                                .setUserName(userName)
                                .setUserTel(userTel)
                                .setContext(context);
          _LOGGER.info(StringX.stringFormat("添加商务合作:[0]", info.toJsonString()));  
          mainService.addBusiness(info);
            
        }
        catch (Exception e)
        {
            _LOGGER.error(e.getMessage() , e);
            cr.setCodeWithMess(ErrorCodePools._FAIL_0);
        }
        return cr.toJsonString();
    }
    
    
    /*
     * XSS跨站脚本注入过滤 Lijinyang 20090723
     */
    public static String xss_inj(String str) {
        String safeString = str;
        if (safeString == null) {
            safeString = "";
        }
        safeString = safeString.replaceAll("", "&#x26;");
        safeString = safeString.replaceAll("<", "&#x3C;");
        safeString = safeString.replaceAll(">", "&#x3E;");
        safeString = safeString.replaceAll("'", "&#x34;");
        safeString = safeString.replaceAll("\"", "&#x39;");
        safeString = safeString.replaceAll("javascript:", "javascript：");
        safeString = safeString.replaceAll("jscript:", "jscript：");
        safeString = safeString.replaceAll("vbscript", "vbscript：");
        safeString = safeString.replaceAll("    ", "&nbsp;&nbsp;");
        safeString = safeString.replaceAll(" ", "&nbsp;");
        safeString = safeString.replaceAll("/\\*\\*/", "注释:");

        return safeString;
    }
}

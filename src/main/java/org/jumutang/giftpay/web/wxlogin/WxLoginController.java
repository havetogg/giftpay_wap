package org.jumutang.giftpay.web.wxlogin;

/**
 * Created by RuanYJ on 2017/1/22.
 */

import com.alibaba.fastjson.JSONObject;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.UserMainService;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户基本等
 */
@Controller
@RequestMapping(value = "/giftpay/wap")
public class WxLoginController extends BaseController {

    private static final String INDEX_HTML="/giftpay/wap/index.html";
    private static final String SETTING_HTML="/giftpay/wap/setting.html";


    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLogin")
    public void wxLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {

        UserModel userInfo= null;
        session.setAttribute("userInfo",userInfo);
        //开始获取有礼付go授权 获取用户openId等信息
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String random=UUIDUtil.getUUID();
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode( this.getBaseUrl(request, response).replace(":80","") + "giftpay/wap/initUserInfo.htm?entryUrl="+request.getParameter("entryUrl")+"&random="+random);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:"+targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @RequestMapping(value = "/initUserInfo")
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                             String code, ModelMap modelMap) throws UnsupportedEncodingException {
        logger.info("进入获取用户微信信息");
        String openId = request.getParameter("openid");
        if(StringUtils.isEmpty(openId)){
            //未查询到openId
            modelMap.put("errMsg","参数错误");
            return new ModelAndView("/error.jsp");
        }
        String headImgUrl = request.getParameter("headimgurl");
        logger.info("头像--------------地址"+headImgUrl);
        String entryUrl=request.getParameter("entryUrl");
        logger.info("==========原始nickName:"+request.getParameter("nickname"));
        String nickName = filterNickName(request);
        UserModel userModel = new UserModel();
        userModel.setHeadImgUrl(headImgUrl);
        userModel.setWechatNikeName(nickName);
        userModel.setOpenId(openId);
        logger.info("==========nickName:"+nickName);
        logger.info("用户OpenId:"+openId);
        UserSubModel userSubModel=new UserSubModel();
        userSubModel.setOpenId(openId);
        List<UserSubModel> subList=this.userSubService.queryUserSubModel(userSubModel);
        logger.info("查询是否存在用户中心表:"+subList.size());
        String userId="";
        if(subList==null||subList.size()==0){
            logger.info("不存在用户中心 创建用户 获取userId");
            UserMainModel userMainModel=new UserMainModel();
            userMainModel.setId( UUIDUtil.getUUID());
            logger.info("创建UserId："+userMainModel.getId());
            userMainModel.setPhone("");
            userMainModel.setStatus("0");
            this.userMainService.addUserMainModel(userMainModel);

            userSubModel.setId(UUIDUtil.getUUID());
            userSubModel.setType("1");
            userSubModel.setStatus("0");
            userSubModel.setPhone("");
            userSubModel.setUserId(userMainModel.getId());
            this.userSubService.addUserSubModel(userSubModel);

            userId=userMainModel.getId();
        }else{
            logger.info("存在 获取userId");
            userId=subList.get(0).getUserId();
        }

        //生成余额账户
        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setAccountId(userId);
        //判断用户余额账户是否存在
        List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
        logger.info("用户余额账户List:"+list);
        if(list.size()==0){
            int result = balanceServiceI.insertBalace(balanceModel);
            if(result==0){
                return new ModelAndView("redirect:/wxLogin.htm");
            }
        }

        session.setAttribute("userId",userId);
        session.setAttribute("openId",openId);
        session.setAttribute("wxUser",userModel);
        session.setAttribute("entryType",0);
        logger.error("userId:"+session.getAttribute("userId")+"==============================");
        return new ModelAndView(INDEX_HTML);
    }

    @RequestMapping(value = "/isRegister")
    @ResponseBody
    public String isRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        CodeMess codeMess;
        UserSubModel userSubModel=new UserSubModel();
        userSubModel.setOpenId((String) session.getAttribute("openId"));
        System.out.println(session.getAttribute("openId"));
        List<UserSubModel> subList=this.userSubService.queryUserSubModel(userSubModel);
        logger.info("判断是否存在用户信息:"+subList.size());
        if (subList.size()==0){
            logger.info("不存在用户信息");
            codeMess=new CodeMess("20000","unbind");
        }else{
            userSubModel=subList.get(0);
            if(userSubModel.getPhone()==null||userSubModel.getPhone().equals("")){
                logger.info("不存在用户信息");
                codeMess=new CodeMess("20000","unbind");
            }else{
                codeMess=new CodeMess("10000","bind");
            }
        }
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 微信端登录授权
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/redPkgGate")
    public void redPkgGate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("跳转到红包中心的静默授权");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","") +
                "giftpay/wap/loginRedPkg.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/redPkgGateByBase")
    public void redPkgGateByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("跳转到红包中心的手动授权");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","") +
                "giftpay/wap/loginRedPkg.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }
    @RequestMapping(value = "/loginRedPkg")
    public void loginRedPkg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("进入获取用户微信信息");
        String openId = request.getParameter("openid");
        if(openIdISNull(openId)){
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl="http://www.linkgift/giftpay_wap/giftpay/wap/redPkgGateByBase.htm";
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        String headImgUrl = request.getParameter("headimgurl");
        logger.info("头像--------------地址"+headImgUrl);
        String nickName = filterNickName(request);
        UserModel userModel = new UserModel();
        userModel.setHeadImgUrl(headImgUrl);
        userModel.setWechatNikeName(nickName);

        logger.info("用户OpenId:"+openId);
        UserSubModel userSubModel=new UserSubModel();
        userSubModel.setOpenId(openId);
        List<UserSubModel> subList=this.userSubService.queryUserSubModel(userSubModel);
        logger.info("查询是否存在用户中心表:"+subList.size());
        String userId="";
        if(subList==null||subList.size()==0){
            logger.info("不存在用户中心 创建用户 获取userId");
            userId="0";
        }else{
            logger.info("存在 获取userId");
            userId=subList.get(0).getUserId();
        }
        response.sendRedirect(response.encodeRedirectURL(myRedListUrl.replace("USERID",userId)));
    }

    /**
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/queryUserInfo")
    @ResponseBody
    public String queryUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String userId= (String) session.getAttribute("userId");
        String openId= (String) session.getAttribute("openId");
        JSONObject object=new JSONObject();
        if(userId==null||openId==null){
            logger.info("未获取到用户信息");
            object.put("success",false);
        }else{
            object.put("success",true);
            object.put("userId",userId);
            object.put("openId",openId);
        }
        return object.toJSONString();
    }



    @RequestMapping(value = "/shareFriend", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String shareFriend(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws Exception {
        logger.error("------------------------------进入分享方法-----[当前分享url:"+request.getParameter("url")+"]----------------------------------------------------------------------");
        String config = null;
        WXShareUtil shareUtil = WXShareUtil.getInstance(this.appId,this.appSecret);
        String url = request.getParameter("url");
        try {
            config = shareUtil.genJSSDKConf(url);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }



    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/thirdLogin")
    public void thirdLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        String entryUrl=request.getParameter("entryUrl");
        String scope=request.getParameter("scope");
        //开始获取有礼付go授权 获取用户openId等信息
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String random=UUIDUtil.getUUID();
        String targetUrl = openIdUrl + "&redirect_client_url="+entryUrl;
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope="+scope+"&state=STATE#wechat_redirect";
        logger.info("targetUrl:"+targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

}

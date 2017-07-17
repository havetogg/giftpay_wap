package org.jumutang.giftpay.web.withdraw;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.dao.IUserSubDao;
import org.jumutang.giftpay.entity.WCSendMoney;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.BlackUserModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.model.WithdrawModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.service.WithdrawService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: Tinny.liang
 * @Description:提现控制层
 * @Date: Create in 11:33 2017/6/7
 * @Modified By:
 */

@Controller
@RequestMapping(value = "/giftpay/withdraw")
public class WithdrawController extends BaseController{

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private BalanceServiceI balanceServiceI;

    @Autowired
    private PhoneQueryService phoneQueryService;

    @Autowired
    private UserSubService userSubService;

    @Value("#{propertyFactoryBean['white_open_id']}")
    private String whiteOpenId;

    @Value("#{propertyFactoryBean['fee']}")
    private String fee;


    //提现后台手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/login")
    public void wxRedPkgByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("进入手动授权方法");
        Long timestamp = System.currentTimeMillis();
        String withdrawId = request.getParameter("withdrawId");
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/withdraw/initUserInfo.htm?withdrawId="+withdrawId);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/initUserInfo")
    public String reviewOrderInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openid");
        String withdrawId = request.getParameter("withdrawId");
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl = "http://www.linkgift.cn/giftpay_wap/withdraw/review/login.htm";
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        session.setAttribute("openId",openId);
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.equals(openId)) {
                res = true;
            }
        }
        if (res) {
            request.setAttribute("withdrawId",withdrawId);
            return "forward:/giftpay/withdraw/initWithdrawInfo.htm";
        }else{
            return "/giftpay/404.jsp";
        }
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/initWithdrawInfo")
    public  String initWithdrawInfo(HttpServletRequest request,Model model){
        //先验证是否有权限
        HttpSession session = request.getSession();
        String openId = session.getAttribute("openId").toString();
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.equals(openId)) {
                res = true;
            }
        }
        if(!res){
            return "/giftpay/404.jsp";
        }
        logger.info("拥有审核权限-----openId:"+openId);
        Object withdrawIdObj = request.getAttribute("withdrawId");
        if(withdrawIdObj != null){
            logger.info("正在查询提现信息withdrawId:"+withdrawIdObj.toString());
            String withdrawId = withdrawIdObj.toString();
            WithdrawModel withdrawModel = new WithdrawModel();
            withdrawModel.setWithdrawId(withdrawId);
            WithdrawModel queryWithdrawModel = withdrawService.getWithdraw(withdrawModel);
            if(queryWithdrawModel != null){
                BalanceModel balanceModel = new BalanceModel();
                balanceModel.setAccountId(queryWithdrawModel.getUserId());
                List<BalanceModel> balanceModelList = balanceServiceI.queryBalances(balanceModel);
                if(balanceModelList==null||balanceModelList.size()<=0){
                    logger.info("没有查询到用户账户表中的零钱信息");
                    return "/giftpay/404.jsp";
                }
                BalanceModel queryBalanceModel = balanceModelList.get(0);

                BlackUserModel blackUserModel = new BlackUserModel();
                blackUserModel.setOpenId(queryWithdrawModel.getOpenId());
                List<BlackUserModel> blackUserModels = blackUserService.queryBlackUserList(blackUserModel);

                if(blackUserModels.size()>0){
                    model.addAttribute("isBlack",true);
                }else{
                    model.addAttribute("isBlack",false);
                }

                logger.info("查询到withdrawModel并放到Model中");
                model.addAttribute("withdrawModel",queryWithdrawModel);
                logger.info("查询到balanceModel并放到Model中");
                model.addAttribute("balanceModel",queryBalanceModel);
                session.setAttribute("withdrawId",withdrawId);
            }else{
                logger.info("没有在提现表中查询到对应信息");
                return "/giftpay/404.jsp";
            }
            return "/giftpay/withdraw/review.jsp";
        }else{
            return "/giftpay/404.jsp";
        }
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/allowWithdraw")
    @Transactional
    public  String allowWithdraw(HttpServletRequest request,Model model){
        //先验证是否有权限
        HttpSession session = request.getSession();
        String openId = session.getAttribute("openId").toString();
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.equals(openId)) {
                res = true;
            }
        }
        if(!res){
            return "/giftpay/404.jsp";
        }
        Object withdrawIdObj = session.getAttribute("withdrawId");
        if(withdrawIdObj != null){
            String withdrawId = withdrawIdObj.toString();
            WithdrawModel withdrawModel = new WithdrawModel();
            withdrawModel.setWithdrawId(withdrawId);
            WithdrawModel queryWithdrawModel = withdrawService.getWithdraw(withdrawModel);
            if(queryWithdrawModel != null){
                BalanceModel balanceModel = new BalanceModel();
                balanceModel.setAccountId(queryWithdrawModel.getUserId());
                BalanceModel queryBalanceModel = balanceServiceI.queryBalances(balanceModel).get(0);
                if(queryWithdrawModel.getWithdrawAmount()<=0){
                    throw new RuntimeException("提现金额必须大于0");
                }
                if(queryWithdrawModel.getWithdrawAmount()>queryBalanceModel.getBalanceNumber()){
                    throw new RuntimeException("提现金额超出范围");
                }
                BlackUserModel blackUserModel = new BlackUserModel();
                blackUserModel.setOpenId(queryWithdrawModel.getOpenId());
                List<BlackUserModel> blackUserModels = blackUserService.queryBlackUserList(blackUserModel);
                if(blackUserModels!=null&&blackUserModels.size()>0){
                    throw new RuntimeException("黑名单用户无法提现");
                }
                //调用微信接口
                BigDecimal getMoney = new BigDecimal("1").subtract(new BigDecimal(fee));
                BigDecimal withDraw = new BigDecimal(String.valueOf(queryWithdrawModel.getWithdrawAmount()));
                int wxAmount = (getMoney.multiply(withDraw).multiply(new BigDecimal("100"))).intValue();
                WCSendMoney wcSendMoney = new WCSendMoney("wxee9d9ad775f75311","1426694102",queryWithdrawModel.getWithdrawId(),queryWithdrawModel.getOpenId(),wxAmount,"有礼付提现","180.110.16.82");


                queryWithdrawModel.setStatus((short)1);
                withdrawService.updateWithdraw(queryWithdrawModel);
                model.addAttribute("withdrawModel",queryWithdrawModel);
                queryBalanceModel.setBalanceNumber(queryBalanceModel.getBalanceNumber()-queryWithdrawModel.getWithdrawAmount());
                balanceServiceI.updateBalance(queryBalanceModel);
                model.addAttribute("balanceModel",queryBalanceModel);
            }
            return "/giftpay/withdraw/review.jsp";
        }else{
            return "/giftpay/404.jsp";
        }
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/saveWithdraw")
    @ResponseBody
    public String saveWithdraw(HttpServletRequest request, String amount) throws Exception{
        logger.info("开始插入提现记录表-----------------------------");
        Map<String,Object> returnMap = new HashMap<>();
        HttpSession session = request.getSession();
        if(session.getAttribute("openId")!=null&&session.getAttribute("userId")!=null){
            logger.info("获取到openId和userId");
            String openId = (String)session.getAttribute("openId");
            String userId = (String)session.getAttribute("userId");
            if(Double.parseDouble(amount)<=0){
                BlackUserModel blackUserModel=new BlackUserModel();
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setRechargeNum("");
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeName("有礼付提现申请");
                blackUserModel.setRechargePrice(amount);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setRechargeDesc("有礼付易提现接口金额被篡改");
                addBlackUser(blackUserModel);
                returnMap.put("result",false);
                returnMap.put("msg","提现金额大于用户余额");
                return JSONObject.toJSONString(returnMap);
            }
            BalanceModel balanceModel = new BalanceModel();
            balanceModel.setAccountId(userId);
            BalanceModel queryBalanceModel = balanceServiceI.queryBalances(balanceModel).get(0);
            if(queryBalanceModel!=null&&queryBalanceModel.getBalanceNumber()>=Double.parseDouble(amount)){
                UserSubModel subModel = new UserSubModel();
                subModel.setOpenId(openId);
                WithdrawModel withdrawModel = new WithdrawModel();
                withdrawModel.setOpenId(openId);
                withdrawModel.setUserId(userId);
                withdrawModel.setWithdrawAmount(Double.parseDouble(amount));
                withdrawModel.setPhone(this.userSubService.queryUserSubModel(subModel).get(0).getPhone());
                withdrawModel.setIpAddress(IPUtil.getIpAddr(request));
                withdrawService.saveWithdraw(withdrawModel);
                returnMap.put("result",true);
                returnMap.put("msg","提现需要审核，请耐心等待1-7个工作日");
            }else{
                returnMap.put("result",false);
                returnMap.put("msg","提现金额大于用户余额");
            }
        }else{
            returnMap.put("result",false);
            returnMap.put("msg","用户信息校验失败");
        }
        return JSONObject.toJSONString(returnMap);
    }

    private void addBlackUser(BlackUserModel blackUserModel) {
        BlackUserModel blackUser=new BlackUserModel();
        blackUser.setOpenId(blackUserModel.getOpenId());
        List<BlackUserModel> blackUserModels=blackUserService.queryBlackUserList(blackUser);
        if(blackUserModels.size()==0){
            blackUserService.addBlackUserModel(blackUserModel);
        }
    }
}

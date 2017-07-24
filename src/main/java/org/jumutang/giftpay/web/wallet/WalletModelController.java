package org.jumutang.giftpay.web.wallet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreOrderResponse;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.tools.*;
import org.jumutang.giftpay.web.wxlogin.WxLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by RuanYJ on 2017/1/14.
 */

/**
 * 钱包模块
 */
@Controller
@RequestMapping(value = "/giftpay/wallet", method = {RequestMethod.GET, RequestMethod.POST})
public class WalletModelController extends BaseController {

    //跳转线下支付页面
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxRedPkg")
    public ModelAndView wxRedPkg(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
        logger.error("跳转下线支付成功页面");
        String thirdOpenId = request.getParameter("thirdOpenId");
        thirdOpenId = thirdOpenId == null ? "" : thirdOpenId;
        String channelId = request.getParameter("channelId");
        channelId = channelId == null ? "" : channelId;
        String amount = request.getParameter("amount");
        amount = amount == null ? "" : amount;
        String hasRed = request.getParameter("hasRed");
        hasRed = hasRed == null ? "" : hasRed;
        String redId = request.getParameter("redId");
        redId = redId == null ? "" : redId;
        String orderId = request.getParameter("orderId");
        orderId = orderId == null ? "" : orderId;
        String gunNo = request.getParameter("gunNo");
        gunNo = gunNo == null ? "" : gunNo;
        String empno = request.getParameter("empno");
        empno = empno == null ? "" : empno;
        String redirectURL=request.getParameter("redirectURL");
        redirectURL = redirectURL == null ? "" : redirectURL;
        modelMap.put("gunNo", gunNo);
        modelMap.put("timestamp", DateFormatUtil.formateString());
        modelMap.put("realAmount", amount);
        modelMap.put("hasRed", hasRed);
        modelMap.put("channelId", channelId);
        modelMap.put("thirdOpenId", thirdOpenId);
        modelMap.put("orderId", orderId);
        modelMap.put("redId", redId);
        modelMap.put("empno", empno);
        modelMap.put("redirectURL", redirectURL);
        logger.error("跳转支付结果员工号:"+empno);
        if (redId.equals("80")) {
            modelMap.put("redAmount", "50");
        } else if (redId.equals("90")) {
            modelMap.put("redAmount", "10");
        }
        return new ModelAndView("/giftpay/wallet/paySuccessNoLineBalance.jsp");
    }


    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxRedPkgBase")
    public void wxRedPkgBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("进入静默授权方法");
        String thirdOpenId = request.getParameter("thirdOpenId");
        thirdOpenId = thirdOpenId == null ? "" : thirdOpenId;
        String channelId = request.getParameter("channelId");
        channelId = channelId == null ? "" : channelId;
        String amount = request.getParameter("amount");
        amount = amount == null ? "" : amount;
        String hasRed = request.getParameter("hasRed");
        hasRed = hasRed == null ? "" : hasRed;
        String redId = request.getParameter("redId");
        redId = redId == null ? "" : redId;
        String orderId = request.getParameter("orderId");
        orderId = orderId == null ? "" : orderId;
        String gunNo = request.getParameter("gunNo");
        gunNo = gunNo == null ? "" : gunNo;
        String time = request.getParameter("timestamp");
        time = time == null ? "" : time;
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/wallet/initUserInfo.htm?entryUrl=" + request.getParameter("entryUrl") + "&thirdOpenId=" +
                thirdOpenId + "&channelId=" + channelId + "&hasRed=" + hasRed + "&redId=" + redId + "&amount=" +
                amount + "&orderId=" + orderId + "&gunNo=" + gunNo + "&time=" + time);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxRedPkgByBase")
    public void wxRedPkgByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("进入手动授权方法");
        String thirdOpenId = request.getParameter("thirdOpenId");
        thirdOpenId = thirdOpenId == null ? "" : thirdOpenId;
        String channelId = request.getParameter("channelId");
        channelId = channelId == null ? "" : channelId;
        String amount = request.getParameter("amount");
        amount = amount == null ? "" : amount;
        String hasRed = request.getParameter("hasRed");
        hasRed = hasRed == null ? "" : hasRed;
        String redId = request.getParameter("redId");
        redId = redId == null ? "" : redId;
        String orderId = request.getParameter("orderId");
        orderId = orderId == null ? "" : orderId;
        String gunNo = request.getParameter("gunNo");
        gunNo = gunNo == null ? "" : gunNo;
        String time = request.getParameter("time");
        time = time == null ? "" : time;
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/wallet/initUserInfo.htm?entryUrl=" + request.getParameter("entryUrl") + "&thirdOpenId=" + thirdOpenId +
                "&channelId=" + channelId + "&hasRed=" + hasRed + "&redId=" + redId + "&amount=" + amount + "&orderId=" + orderId + "&gunNo=" + gunNo + "&time=" + time);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @RequestMapping(value = "/initUserInfo")
    public void initUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                             String code, ModelMap modelMap) throws IOException {
        logger.info("从红包进入获取用户信息方法");
        String openId = request.getParameter("openid");
        String channelId = request.getParameter("channelId");
        String thirdOpenId = request.getParameter("thirdOpenId");
        String hasRed = request.getParameter("hasRed");
        String amount = request.getParameter("amount");
        String redId = request.getParameter("redId");
        String orderId = request.getParameter("orderId");
        String gunNo = request.getParameter("gunNo");
        String time = request.getParameter("time");
        String targetUrl = "";
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/wallet/wxRedPkgByBase.htm?thirdOpenId=" +
                    thirdOpenId + "&channelId=" + channelId + "&hasRed=" + hasRed + "&redId=" + redId + "&amount=" + amount + "&orderId=" + orderId + "&gunNo=" + gunNo + "&time=" + time;
        } else {
            session.setAttribute("thirdOpenId", thirdOpenId);
            session.setAttribute("zshGiftOpenId", openId);
            logger.info("参数:redId:" + redId + " openId: " + openId + " channelId:" + channelId + " thirdOpenId:" + thirdOpenId + " hasRed:" + hasRed + " amount:" + amount);
            logger.info("查询是否有红包");
            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(thirdOpenId);
            List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);
            if (subList.size() == 0) {
                //不存在用户表 查询openId是否存在子表
                UserSubModel subModel2 = new UserSubModel();
                subModel2.setOpenId(openId);
                subList = this.userSubService.queryUserSubModel(subModel2);
                if (subList.size() == 0) {
                    //不存在子表
                    UserMainModel userMainModel = new UserMainModel();
                    userMainModel.setId(UUIDUtil.getUUID());
                    userMainModel.setPhone("");
                    userMainModel.setStatus("0");
                    this.userMainService.addUserMainModel(userMainModel);
                    subModel.setStatus("0");
                    subModel.setType("2");
                    subModel.setPhone("");
                    subModel.setId(UUIDUtil.getUUID());
                    subModel.setUserId(userMainModel.getId());
                    subModel.setOpenId(thirdOpenId);
                    this.userSubService.addUserSubModel(subModel);
                    subModel.setId(UUIDUtil.getUUID());
                    subModel.setOpenId(openId);
                    this.userSubService.addUserSubModel(subModel);
                } else {
                    subModel.setStatus("0");
                    subModel.setType("2");
                    subModel.setPhone("");
                    subModel.setId(UUIDUtil.getUUID());
                    subModel.setOpenId(thirdOpenId);
                    subModel.setUserId(subList.get(0).getUserId());
                    this.userSubService.addUserSubModel(subModel);
                }
            } else {
                subModel = subList.get(0);
                UserSubModel subModel2 = new UserSubModel();
                subModel2.setOpenId(openId);
                subList = this.userSubService.queryUserSubModel(subModel2);
                if (subList.size() == 0) {
                    subModel2.setStatus("0");
                    subModel2.setType("2");
                    subModel2.setPhone("");
                    subModel2.setId(UUIDUtil.getUUID());
                    subModel2.setUserId(subModel.getUserId());
                    subModel2.setOpenId(openId);
                    this.userSubService.addUserSubModel(subModel2);
                }
            }
            session.setAttribute("redPkgUserId", subModel.getUserId());
            logger.info("发送红包查询接口");
            String redpkgResult = HttpUtil.sendGet(queryRedUrl + "?openId=" + thirdOpenId + "&channelId=" + channelId, "utf-8", null);
            if (redpkgResult == null) {
                //无返回
                logger.info("查询红包接口失败");
            }
            JSONObject redp = (JSONObject) JSONObject.parse(redpkgResult);
            logger.info("查询红包接口数据:" + redp);
            //1.没有该用户 创建用户并新增红包   2.有红包 但是未激活  提示激活红包   3.使用红包   4.之前已使用过红包 跳转支付成功
            if (hasRed.equals("0")) {
                logger.info("没 调用红包接口创建红包 查询data是否为null");
                if (redp == null || redp.getString("data") == null) {
                    logger.info("没有红包记录 添加红包");
                    redpkgResult = HttpUtil.sendGet(addRedUrl + "?openId=" + thirdOpenId + "&channelId=" + channelId + "&userId=" + subModel.getUserId(), "utf-8", null);
                    logger.info("添加红包返回结果:" + redpkgResult);
                    JSONObject addResult = (JSONObject) JSONObject.parse(redpkgResult);
                }
            }
            targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/third/addThirdUserIndex.htm?thirdName=zsh";
        }
        response.sendRedirect(response.encodeRedirectURL(targetUrl));

    }

    /**
     * 设置用户信息
     */
    @Transactional
    @RequestMapping(value = "/settingUserMobile")
    @ResponseBody
    public String settingUserMobile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String userId = (String) session.getAttribute("redPkgUserId");
        String mobile = request.getParameter("mobile");
        String valNum = request.getParameter("valNum");
        String redPkgMsgNum = (String) session.getAttribute("redPkgMsgNum");
        String thirdOpenId = (String) session.getAttribute("thirdOpenId");
        String openId = (String) session.getAttribute("zshGiftOpenId");
        CodeMess codeMess;
        if (!valNum.equals(redPkgMsgNum)) {
            codeMess = new CodeMess("20000", "验证码错误！");
            return JSONObject.toJSONString(codeMess);
        }
        UserSubModel userSubModel = new UserSubModel();
        userSubModel.setUserId(userId);
        userSubModel.setPhone(mobile);
        userSubModel.setOpenId(thirdOpenId);
        this.userSubService.updateUserSubModel(userSubModel);
        userSubModel.setOpenId(openId);
        this.userSubService.updateUserSubModel(userSubModel);
        UserMainModel mainModel = new UserMainModel();
        mainModel.setPhone(mobile);
        mainModel.setId(userId);
        this.userMainService.updateUserMainModel(mainModel);
        String redpkgId = (String) session.getAttribute("redpkgId");
        //修改红包状态
        String redpkgResult = HttpUtil.sendGet(updateRedUrl + "?redpkgId=" + redpkgId + "&openId=" + thirdOpenId + "&phone=" + mobile + "&state=4", "utf-8", null);
        logger.info("redpkgResult:" + redpkgResult);
        codeMess = new CodeMess("10000", "");
        return JSON.toJSONString(codeMess);
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void sendMsg(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        String phone = request.getParameter("phone");
        Random random = new Random();
        int valNum = random.nextInt(899999);
        valNum = valNum + 100000;//随机六位数
        String msgContent = "【有礼付】您的验证码是" + valNum + "，如非本人操作请忽略该短信。";

        //验证码存入session中
        session.setAttribute("valNum",valNum);

        logger.info("发送短信验证码为：" + valNum);
        net.sf.json.JSONObject jsonObject = MobileMessageUtil.sendMessage(phone, msgContent);
        logger.info("发送短信结果返回：" + jsonObject);
        session.setAttribute("redPkgMsgNum", String.valueOf(valNum));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxRedPkgOL")
    public void toRedPkgOLPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String realAmount = request.getParameter("realAmount");//实付金额
        String redAmount = request.getParameter("redAmount");//红包抵扣金额
        String redirectURL=request.getParameter("redirectURL");//重定向地址

        String targetUrl = "";
        if (StringUtils.isEmpty(redAmount) || redAmount.equals("0")) {
            //无红包
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftpay/wallet/paySuccessOLNoRed.html?redirectURL="+redirectURL;
        } else {
            //有红包
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftpay/wallet/paySuccessNew1.html?realAmount=" + realAmount + "&redAmount=" + redAmount+"&redirectURL="+redirectURL;
        }
        logger.error("OL跳转链接=================" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/payBase")
    public void payBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("进入静默授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/wallet/initDemoUserInfo.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/payUser")
    public void payUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("进入手动授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/wallet/initDemoUserInfo.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @RequestMapping(value = "/initDemoUserInfo")
    public ModelAndView initDemoUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                         String code, ModelMap modelMap) throws IOException {
        logger.info("从红包进入获取用户信息方法");
        String openId = request.getParameter("openid");
        if (StringUtils.isEmpty(openId) || openId.equals("") || openId == null || openId.equals("null")) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl =this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/wallet/payUser.htm";
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        ModelAndView modelAndView = new ModelAndView("/giftpay/wallet/payDemeIndex.jsp");
        modelAndView.addObject("openId", openId);
        return modelAndView;
    }


    /**
     * 执行当前业务方法(加入微信签名认证)
     */
    @RequestMapping(value = "/preOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String preOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model,
                           HttpSession session) throws Exception {
        CodeMess codeMess;
        String paymoney = request.getParameter("money");
        String goodsName = "中石化微信支付线上DEMO";
        String openId = request.getParameter("openId");

        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setAccountId(openId);
        payInfoModel.setOrderNo(UniqueX.randomUnique());
        payInfoModel.setBusinessInfo("中石化微信支付线上DEMO");
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealInfo(goodsName);
        payInfoModel.setDealState(new Short("2"));
        payInfoModel.setDealType(new Short("2"));
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setDealMoney(Double.parseDouble(paymoney));
        payInfoModel.setDealRealMoney(Double.parseDouble(paymoney));
        int result = payInfoServiceI.insertPayInfo(payInfoModel);
        //微信统一下单接口\
        DecimalFormat df = new DecimalFormat("######0");
        String responseXml = WXPayUtil.preOrder(new WCPreOrderRequest(appId, mchID, key, openId, String.valueOf(df.format(payInfoModel.getDealMoney() * 100)),
                IPUtil.getIpAddr(request), oilPayRedirect, payInfoModel.getOrderNo(), payInfoModel.getDealInfo()));
        logger.info("responseXml=" + responseXml);
        XStream stream = new XStream();
        stream.alias("xml", WCPreOrderResponse.class);
        String redUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftpay/demopay/receivePreOrder.htm";
        WCPreOrderResponse wcPreOrderResponse = (WCPreOrderResponse) stream.fromXML(responseXml);
        if (wcPreOrderResponse.getReturn_code().equals("SUCCESS")) {
            if (wcPreOrderResponse.getResult_code().equals("SUCCESS")) {
                Map<String, String> map = WXPayUtil.loadJavaScriptPayConfig(appId, wcPreOrderResponse.getPrepay_id(),
                        key, payInfoModel.getOrderNo(), redUrl, String.valueOf(df.format(payInfoModel.getDealMoney() * 100)));
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map);
                codeMess = new CodeMess("10000", jsonObject.toString());
            } else {
                codeMess = new CodeMess("20000", "业务失败");
            }
        } else {
            codeMess = new CodeMess("20001", "通信失败");
        }
        return JSON.toJSONString(codeMess);
    }

    /**
     * 支付回调方法
     */
    @RequestMapping(value = "/receivePreOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String receivePreOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session)
            throws Exception {
        logger.info("=================进入支付回调方法");
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(this.getResponseMap(request));
        logger.info("===========-=====回调支付信息:" + jsonObject);
        if (jsonObject.getString("return_code") != null && jsonObject.getString("return_code").equals("SUCCESS")) {
            if (jsonObject.getString("result_code") != null && jsonObject.getString("result_code").equals("SUCCESS")) {
                PayInfoModel payInfoModel = new PayInfoModel();
                payInfoModel.setOrderNo(jsonObject.getString("out_trade_no"));
                logger.info("订单信息:" + payInfoModel);
                List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
                logger.info("支付信息对象:" + list);
                //如果支付状态等于1，相当于已记录
                if (list.size() != 0 && list.get(0).getWxOrder() == null) {
                    logger.info("查询订单信息:" + list.get(0));
                    if (2 == list.get(0).getDealState()) {
                        //更新支付记录表
                        payInfoModel.setDealState(new Short("1"));
                        payInfoModel.setWxOrder(jsonObject.getString("transaction_id"));//订单ID
                        int result1 = payInfoServiceI.updatePayInfo(payInfoModel);
                        CodeMess codeMess = new CodeMess("10000", payInfoModel.getOrderNo());
                        return JSON.toJSONString(codeMess);
                    }
                }
            }
        }
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/launchPay")
    public String launchPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        CodeMess codeMess = null;
        String paymoney = request.getParameter("money");
        String goodsName = "中石化微信支付线上DEMO";
        String openId = request.getParameter("openId");

        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setAccountId(openId);
        payInfoModel.setBusinessInfo("中石化微信支付线上DEMO");
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealInfo(goodsName);
        payInfoModel.setDealState(new Short("2"));
        payInfoModel.setDealType(new Short("2"));
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setDealMoney(Double.parseDouble(paymoney));
        payInfoModel.setDealRealMoney(Double.parseDouble(paymoney));
        payInfoModel.setOrderNo(UUIDUtil.getUUID());
        payInfoServiceI.insertPayInfo(payInfoModel);
        String redirectUrl = this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/wallet/cmbcpayRed.htm";//回调地址
        String timestamp=DateFormatUtil.formateString();
        String md5=MD5Util.MD5Encode("cmbcpay"+openId+paymoney+timestamp).toUpperCase();
        JSONObject object=new JSONObject();
        object.put("openId",openId);
        object.put("money",paymoney);
        object.put("goodsName",goodsName);
        object.put("redirectUrl",redirectUrl);
        object.put("orderNo",payInfoModel.getOrderNo());
        object.put("remark","");
        object.put("fromName","giftpay");
        object.put("payType","");
        object.put("timestamp",timestamp);
        object.put("md5",md5);
        object.put("backUrl","");
        codeMess = new CodeMess("10000", object.toString());
        return JSONObject.toJSONString(codeMess);
    }


    /**
     * 支付回调方法
     */
    @RequestMapping(value = "/cmbcpayRed", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String cmbcpayRed(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session)
            throws Exception {
        logger.info("=================进入支付回调方法");
        String orderNo=request.getParameter("orderNo");
        String bizOrderNumber=request.getParameter("bizOrderNumber");
        logger.error("订单参数："+orderNo+",民生参数："+bizOrderNumber);
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setOrderNo(orderNo);
        logger.info("订单信息:" + payInfoModel);
        List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
        logger.info("支付信息对象:" + list);
        //如果支付状态等于1，相当于已记录
        if (list.size() != 0 && list.get(0).getWxOrder() == null) {
            logger.info("查询订单信息:" + list.get(0));
            if (2 == list.get(0).getDealState()) {
                //更新支付记录表
                payInfoModel.setDealState(new Short("1"));
                payInfoModel.setWxOrder(bizOrderNumber);//订单ID
                int result1 = payInfoServiceI.updatePayInfo(payInfoModel);
                CodeMess codeMess = new CodeMess("10000", payInfoModel.getOrderNo());
                return JSON.toJSONString(codeMess);
            }
        }
        return null;
    }

    private static Map<String, String> getResponseMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>();
        InputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e : elementList)
            map.put(e.getName(), e.getText());
        inputStream.close();
        inputStream = null;
        return map;
    }


}

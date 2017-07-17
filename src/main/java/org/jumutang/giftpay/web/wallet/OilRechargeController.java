package org.jumutang.giftpay.web.wallet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreOrderResponse;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.OilServiceService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RuanYJ on 2017/2/24.
 */
@Controller
@RequestMapping(value = "/giftpay/oilRecharge", method = {RequestMethod.GET, RequestMethod.POST})
public class OilRechargeController extends BaseController {

    @Autowired
    private OilServiceService oilServiceService;

    @Autowired
    private UserSubService userSubService;

    @Autowired
    private PhoneQueryService phoneQueryService;

    /**
     * 九五折加油预支付订单
     */
    @RequestMapping(value = "/preOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String preOrder(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = (String)session.getAttribute("userId");
        String openId = (String)session.getAttribute("openId");
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            codeMess = new CodeMess("0", "重新登录");
            return JSON.toJSONString(codeMess);
        }
        if(request.getAttribute("oilPayMoney") == null||request.getAttribute("oilDealMoney") == null||request.getAttribute("num")==null){
            codeMess = new CodeMess("0", "获取付款信息失败");
            return JSON.toJSONString(codeMess);
        }
        double oilDealMoney = (double)request.getAttribute("oilDealMoney");
        double oilPayMoney = (double)request.getAttribute("oilPayMoney");
        int oilPayNum = (int)request.getAttribute("num");
        OilServiceModel oilServiceModel = oilServiceService.queryOilService();
        if(oilServiceModel == null){
            logger.info("查无当前油价信息");
            codeMess = new CodeMess("0", "获取付款信息失败");
            return JSON.toJSONString(codeMess);
        }

        if(oilPayMoney <= 0||oilPayMoney != oilServiceModel.getPayAmount()*oilPayNum||oilDealMoney != oilServiceModel.getPayAmount()*oilPayNum){
            codeMess = new CodeMess("0", "价格被篡改");
            BlackUserModel blackUserModel=new BlackUserModel();
            blackUserModel.setOpenId(openId);
            blackUserModel.setUserId(userId);
            blackUserModel.setRechargeNum("");
            blackUserModel.setIp(IPUtil.getIpAddr(request));
            blackUserModel.setRechargeName("有礼付易加油预支付");
            blackUserModel.setRechargePrice(String.valueOf(oilPayMoney));
            blackUserModel.setCreateTime(DateFormatUtil.formateString());
            blackUserModel.setRechargeDesc("有礼付易加油预支付接口价格被篡改");
            addBlackUser(blackUserModel);
            return JSON.toJSONString(codeMess);
        }
        String goodsName = "有礼付易加油充值";

        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setAccountId(userId);
        payInfoModel.setOrderNo(UniqueX.randomUnique());
        if(session.getAttribute("entryType")!=null){
            if((int)session.getAttribute("entryType")==1){
                payInfoModel.setBusinessInfo("有礼付易加油-光大途径");
            }else if((int)session.getAttribute("entryType")==0){
                payInfoModel.setBusinessInfo("有礼付易加油");
            }else if((int)session.getAttribute("entryType")==2){
                payInfoModel.setBusinessInfo("有礼付易加油-招行途径");
            }
        }else{
            codeMess = new CodeMess("30000", "未知推广途径");
            return JSON.toJSONString(codeMess);
        }
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealInfo(goodsName);
        payInfoModel.setDealState(new Short("2"));
        payInfoModel.setDealType(new Short("2"));
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setDealMoney(oilDealMoney);
        payInfoModel.setDealRealMoney(oilPayMoney);
        logger.info("需要付款信息"+oilPayMoney);
        payInfoServiceI.insertPayInfo(payInfoModel);

        String redirectUrl = this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/oilRecharge/receivePreOrder.htm";//回调地址

        String timestamp=DateFormatUtil.formateString();
        String oilPayMoneyMultiply = String.valueOf(Arith.mul(oilPayMoney,100));
        logger.info("MD5------------付款金钱"+oilPayMoneyMultiply);
        String md5=MD5Util.MD5Encode("cmbcpay"+openId+oilPayMoneyMultiply+timestamp).toUpperCase();
        JSONObject object=new JSONObject();
        object.put("openId",openId);
        object.put("money",oilPayMoneyMultiply);
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
     * 九五折加油回调方法
     */
    @RequestMapping(value = "/receivePreOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String receivePreOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session)
            throws Exception {
        logger.info("=================进入支付回调方法");
        String orderNo=request.getParameter("orderNo");
        String bizOrderNumber=request.getParameter("bizOrderNumber");
        logger.error("订单参数："+orderNo+",民生参数："+bizOrderNumber+",openId："+request.getParameter("openId"));
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setOrderNo(orderNo);
        logger.info("订单信息:" + payInfoModel);
        List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
        logger.info("支付信息对象:" + list);
        //如果支付状态等于1，相当于已记录
        if (list.size() != 0) {
            logger.info("查询订单信息:" + list.get(0));
            if (2 == list.get(0).getDealState()) {
                //更新前先判断
                String srcAmt = request.getParameter("srcAmt");
                if(Double.parseDouble(srcAmt) != list.get(0).getDealRealMoney()){
                    BlackUserModel blackUserModel=new BlackUserModel();
                    blackUserModel.setOpenId(request.getParameter("openId"));
                    blackUserModel.setUserId(list.get(0).getAccountId());
                    blackUserModel.setRechargeNum("");
                    blackUserModel.setIp("");
                    blackUserModel.setRechargeName("有礼付易加油支付回调");
                    blackUserModel.setRechargePrice(srcAmt);
                    blackUserModel.setCreateTime(DateFormatUtil.formateString());
                    blackUserModel.setRechargeDesc("有礼付易加油支付回调接口价格被篡改");
                    addBlackUser(blackUserModel);
                    CodeMess codeMess = new CodeMess("20000", payInfoModel.getOrderNo());
                    logger.info("回调支付金额不匹配");
                    return JSON.toJSONString(codeMess);
                }
                logger.info("回调支付金额匹配");
                //更新支付记录表
                payInfoModel.setDealState(new Short("1"));
                payInfoModel.setDealInfo("有礼付易加油充值");
                payInfoModel.setWxOrder(bizOrderNumber);//订单ID
                int result1 = payInfoServiceI.updatePayInfo(payInfoModel);
                logger.info("修改订单结果" + result1);
                logger.info("修改订单状态时，查询订单" + payInfoModel);

                //更新用户加油信息表
                logger.info("更新用户加油信息表");
                UserOilInfoModel userOilInfoModel = new UserOilInfoModel();
                userOilInfoModel.setUserid(list.get(0).getAccountId());
                userOilInfoModel = userOilInfoService.queryUserOilInfo(userOilInfoModel);
                if (userOilInfoModel == null) {
                    logger.info("查无该用户信息：" + userOilInfoModel.toString());
                }
                OilServiceModel oilServiceModel = oilServiceService.queryOilService();
                if(oilServiceModel == null){
                    logger.info("查无当前油价信息");
                }
                int number=  (int)(list.get(0).getDealMoney()/oilServiceModel.getPayAmount());
                logger.info("num为============:"+number);
                double tAmount = oilServiceModel.getTotalAmount()*number;
                double pAmount = oilServiceModel.getPayAmount()*number;

                userOilInfoModel.setWaitsendmoney(Double.parseDouble(userOilInfoModel.getWaitsendmoney())
                        + tAmount + "");// 更新待发送总金额
                userOilInfoModel.setTotalsavemoey(Double.parseDouble(userOilInfoModel.getTotalsavemoey())
                        + tAmount
                        - pAmount + "");// 更新累计优惠金额
                logger.info("更新用户加油信息：" + userOilInfoModel.toString());
                userOilInfoService.updateUserOilInfo(userOilInfoModel);

                //oil表添加充值记录
                logger.info("充值期数============:"+number);
                for(int i=0;i<number;i++){
                    OilRecordModel oilRecordModel = new OilRecordModel();
                    oilRecordModel.setUserid(list.get(0).getAccountId());
                    oilRecordModel.setDiscount(String.valueOf(oilServiceModel.getDiscount()));
                    oilRecordModel.setId(UUIDUtil.getUUID());
                    oilRecordModel.setPayamount(String.valueOf(oilServiceModel.getPayAmount()));
                    oilRecordModel.setOpenid(request.getParameter("openId"));
                    oilRecordModel.setStatus("0");
                    oilRecordModel.setCreatetime(DateFormatUtil.formateString());
                    oilRecordModel.setCycle(String.valueOf(oilServiceModel.getCycle()));
                    oilRecordModel.setTermsurplus(String.valueOf(oilServiceModel.getCycle()));
                    oilRecordModel.setTotalamount(String.valueOf(oilServiceModel.getTotalAmount()));
                    oilRecordModel.setMonthamount(String.valueOf(oilServiceModel.getMonthAmount()));
                    this.oilRecordService.insertOilRecord(oilRecordModel);
                    logger.info("插入加油记录表" + oilRecordModel.getId());
                }
                //更新oil_service表库存记录
                Map<String,Object> param = new HashMap<>();
                param.put("num",number);
                oilServiceService.updateOilService(param);

                //用户微信推送消息
                TemplateMessageUtil.sendOilPaySuccess(request.getParameter("openId"),"有礼付易加油",srcAmt);

                //向管理员推送购买成功消息
                int entryType = 0;
                if(list.get(0).getBusinessInfo().indexOf("光大途径")>=0){
                    entryType = 1;
                }else if(list.get(0).getBusinessInfo().indexOf("招行途径")>=0){
                    entryType = 2;
                }
                UserSubModel userSubModel = new UserSubModel();
                userSubModel.setUserId(list.get(0).getAccountId());
                List<UserSubModel> userSubModels = userSubService.queryUserSubModel(userSubModel);
                if(userSubModels != null&&userSubModels.size()>0){
                    String phone = userSubModels.get(0).getPhone();
                    logger.info("查询的手机号码为:"+phone);
                    PhoneModel phoneModel = phoneQueryService.getPhone(phone);
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TemplateMessageUtil.sendOilPayNotify(orderNo,phone,phoneModel,dateTimeFormat.format(new Date()),"有礼付易加油",srcAmt,entryType);
                }

                CodeMess codeMess = new CodeMess("10000", payInfoModel.getOrderNo());
                return JSON.toJSONString(codeMess);
            }
        }
        return null;
    }

    //九五折加油静默授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxOilByBase")
    public void wxOilByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("进入静默授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","") +
                "giftpay/oilRecharge/initUserInfo.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //九五折加油手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxOilByInfo")
    public void wxOilByInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("进入手动授权方法");
        HttpSession session = request.getSession();
        String entryType = request.getParameter("entryType");
            //默认为光大银行
        if(org.apache.commons.lang.StringUtils.isBlank(entryType)){
            session.setAttribute("entryType",1);
            //类型2为招行
        }else if (Integer.parseInt(entryType) == 2){
            session.setAttribute("entryType",2);
        }
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/oilRecharge/initUserInfo.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户信息初始化
    @RequestMapping(value = "/initUserInfo")
    public void initUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                             String code, ModelMap modelMap) throws IOException {
        logger.info("进入获取用户信息方法");
        String openId = request.getParameter("openid");
        String targetUrl="";
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftpay/oilRecharge/wxOilByInfo.htm";
        }else{
            session.setAttribute("openId", openId);
            logger.info("参数:openId = "+openId);
            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(openId);
            List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);
            String userId = "";
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
                userId = userMainModel.getId();
                subModel.setOpenId(openId);
                this.userSubService.addUserSubModel(subModel);
                targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"rechargeoil/bindCellPhone.html?userId="+userId+"&openId="+openId;
            } else {
                logger.info("存在 获取userId");
                userId = subList.get(0).getUserId();
                if(org.apache.commons.lang.StringUtils.isBlank(subList.get(0).getPhone())){
                    targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"rechargeoil/bindCellPhone.html?userId="+userId+"&openId="+openId;
                }else{
                    targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"rechargeoil/index.html?userId="+userId+"&openId="+openId;
                }
            }
            session.setAttribute("userId",userId);

            //生成余额账户
            BalanceModel balanceModel = new BalanceModel();
            balanceModel.setAccountId(userId);
            //判断用户余额账户是否存在
            List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
            logger.info("用户余额账户List:"+list);
            if(list.size()==0){
                balanceServiceI.insertBalace(balanceModel);
            }
        }
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //增加黑名单用户方法
    private void addBlackUser(BlackUserModel blackUserModel) {
        BlackUserModel blackUser=new BlackUserModel();
        blackUser.setOpenId(blackUserModel.getOpenId());
        List<BlackUserModel> blackUserModels=blackUserService.queryBlackUserList(blackUser);
        if(blackUserModels.size()==0){
            blackUserService.addBlackUserModel(blackUserModel);
        }
    }
}

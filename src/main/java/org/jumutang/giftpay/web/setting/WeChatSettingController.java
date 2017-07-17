package org.jumutang.giftpay.web.setting;

/**
 * Created by RuanYJ on 2017/1/14.
 */

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
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreOrderResponse;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.*;
import org.jumutang.giftpay.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 微信相关设置
 */
@Controller
public class WeChatSettingController {

    Logger logger = LoggerFactory.getLogger(WeChatSettingController.class);

    //修改手机号
    @Value("#{propertyFactoryBean['modify_mobile']}")
    private String modifyMobileUrl;
    //重置用户密码
    @Value("#{propertyFactoryBean['reset_pwd']}")
    private String resetPwdUrl;
    //修改用户登录密码
    @Value("#{propertyFactoryBean['modify_pwd']}")
    private String modifyPwdUrl;
    //设置支付密码
    @Value("#{propertyFactoryBean['set_pay_pwd']}")
    private String setPayPwdUrl;
    //修改支付密码
    @Value("#{propertyFactoryBean['modify_pay_pwd']}")
    private String modifyPayPwd;
    //appid
    @Value("#{propertyFactoryBean['app_id']}")
    private String appId;
    //key
    @Value("#{propertyFactoryBean['key']}")
    private String key;
    //mchID
    @Value("#{propertyFactoryBean['mchID']}")
    private String mchID;
    //支付回调链接
    @Value("#{propertyFactoryBean['pay_redirect']}")
    private String payRedirect;
    //发送短信链接
    @Value("#{propertyFactoryBean['send_msg_url']}")
    private String sendMsgUrl;

    @Value("#{propertyFactoryBean['white_open_id']}")
    private String whiteOpenId;
    @Value("#{propertyFactoryBean['review_url']}")
    protected String reviewUrl;

    @Autowired
    private UserMainService userMainService;
    @Autowired
    private UserSubService userSubService;
    //余额账户生成服务层
    @Autowired
    private BalanceServiceI balanceServiceI;
    @Autowired
    private PayInfoServiceI payInfoServiceI;
    @Autowired
    private PhoneQueryService phoneQueryService;

    /**
     * 修改手机号
     */
    @RequestMapping(value = "/modifyMobile")
    public void modifyMobile(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 重置用户密码
     */
    @RequestMapping(value = "/resetPwd")
    public void resetPwd(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 修改用户登录密码
     */
    @RequestMapping(value = "/modifyPwd")
    public void modifyPwd(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     *设置支付密码
     */
//    @RequestMapping(value = "/setPayPwd")
//    public void setPayPwd(HttpServletRequest request, HttpServletResponse response) {
//
//    }

    /**
     * 修改支付密码
     */
    @RequestMapping(value = "/modify_pay_pwd")
    public void modifyPayPwd(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 设置用户信息
     */
    @Transactional
    @RequestMapping(value = "/settingUserMobile")
    @ResponseBody
    public String settingUserMobile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String mobile = request.getParameter("mobile");
        String code = request.getParameter("code");
        String msgCode= (String) session.getAttribute("msgNum");
        String openId = (String) session.getAttribute("userId");
        String entryUrl = request.getParameter("entryUrl");//入口URL
        CodeMess codeMess;
        logger.info("用户输入验证码为："+code+",系统获取验证码为："+msgCode);

        if(!code.equals(msgCode)){
            codeMess=new CodeMess("20001","验证码不匹配!");
            return JSONObject.toJSONString(codeMess);
        }
        //绑定手机
        UserMainModel userMainModel = new UserMainModel();
        userMainModel.setId(openId);
        if (mobile != null) {
            userMainModel.setPhone(mobile);
        }


        logger.info("绑定手机-------------");
        int result = userMainService.updateUserMainModel(userMainModel);
        if (result == 0) {
            codeMess = new CodeMess("20000", "绑定失败");
            return JSONObject.toJSONString(codeMess);
        } else {
            UserSubModel userSubModel = new UserSubModel();
            userSubModel.setUserId(openId);
//        	if(loginPwd !=null){
//        		userSubModel.setLoginPwd(loginPwd);
//        	}
            if (mobile != null) {
                userSubModel.setPhone(mobile);
            }
            int result1 = userSubService.updateUserSubModel(userSubModel);
            if (result1 > 0) {
                codeMess = new CodeMess("10000", entryUrl);
            } else {
                logger.error("绑定失败，信息回滚");
                throw new RuntimeException("绑定失败，信息回滚");
            }
        }
        return JSONObject.toJSONString(codeMess);
    }


    /**
     * 执行当前业务方法(加入微信签名认证)
     */
    @RequestMapping(value = "/preOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String preOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model,
                           HttpSession session) throws Exception {
        CodeMess codeMess;
        String openId = (String) session.getAttribute("openId");
        String money = request.getParameter("dealMoney");
        String userId = (String) session.getAttribute("userId");
        if (StringUtils.isEmpty(userId)) {
            codeMess = new CodeMess("30000", "重新登录");
            return JSON.toJSONString(codeMess);
        }
        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setAccountId(userId);
        List<BalanceModel> lit = balanceServiceI.queryBalances(balanceModel);
        if (lit.size() == 0) {
            codeMess = new CodeMess("30000", "重新登录");
            return JSON.toJSONString(codeMess);
        }
        String dateTime = (String) session.getAttribute("BalanceTime");
        if (StringUtils.isEmpty(dateTime) || dateTime == null) {
            session.setAttribute("BalanceTime", DateFormatUtil.formateString());
        } else {
            String now = DateFormatUtil.formateString();
            if (DateFormatUtil.compare_date(dateTime, now)) {
                //超过2分钟
                session.setAttribute("BalanceTime", now);
            } else {
                codeMess = new CodeMess("20000", "订单提交过快，请稍后再试");
                return JSON.toJSONString(codeMess);
            }
        }

        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setAccountId(userId);
        payInfoModel.setOrderNo(UniqueX.randomUnique());
        payInfoModel.setBusinessInfo("有礼付缴费充值");
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealInfo("有礼付充值-余额(微信支付)");
        payInfoModel.setDealState(new Short("2"));
        payInfoModel.setDealType(new Short("2"));
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setDealMoney(Double.parseDouble(money));
        payInfoModel.setDealRealMoney(Double.parseDouble(money));
        int result = payInfoServiceI.insertPayInfo(payInfoModel);
        if (result == 0) {
            codeMess = new CodeMess("20000", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        String redirectUrl = this.getBaseUrl(request, response).replace(":80", "") +
                "receivePreOrder.htm";//回调地址
        String timestamp=DateFormatUtil.formateString();
        DecimalFormat df = new DecimalFormat("######0");
        String md5=MD5Util.MD5Encode("cmbcpay"+openId+String.valueOf(df.format(payInfoModel.getDealRealMoney() * 100)+timestamp)).toUpperCase();
        JSONObject object=new JSONObject();
        object.put("openId",openId);
        object.put("money",Double.parseDouble(money)*100);
        object.put("goodsName","有礼付充值-余额(微信支付)");
        object.put("redirectUrl",redirectUrl);
        object.put("orderNo",payInfoModel.getOrderNo());
        object.put("remark","");
        object.put("fromName","giftpay");
        object.put("payType","");
        object.put("timestamp",timestamp);
        object.put("md5",md5);
        String backUrl=this.getBaseUrl(request, response).replace(":80","") + "giftpay/wap/rechargeResult.html?orderId="+payInfoModel.getOrderNo();
        object.put("backUrl",backUrl);
        codeMess = new CodeMess("10000", object.toString());
        //微信统一下单接口\
        /*String responseXml = WXPayUtil.preOrder(new WCPreOrderRequest(appId, mchID, key, openId, String.valueOf(stringToInt(String.valueOf(payInfoModel.getDealMoney()*10))),
                IPUtil.getIpAddr(request), payRedirect, payInfoModel.getOrderNo(), payInfoModel.getDealInfo()));
        logger.info("responseXml=" + responseXml);
        XStream stream = new XStream();
        stream.alias("xml", WCPreOrderResponse.class);
        WCPreOrderResponse wcPreOrderResponse = (WCPreOrderResponse) stream.fromXML(responseXml);
        if (wcPreOrderResponse.getReturn_code().equals("SUCCESS")) {
            if (wcPreOrderResponse.getResult_code().equals("SUCCESS")) {
                Map<String, String> map = WXPayUtil.loadJavaScriptPayConfig(appId, wcPreOrderResponse.getPrepay_id(),
                        key, payInfoModel.getOrderNo());
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map);
                jsonObject.put("paymoney",  String.valueOf(stringToInt(String.valueOf(payInfoModel.getDealMoney()*10))));
                codeMess = new CodeMess("10000", jsonObject.toString());
            } else {
                codeMess = new CodeMess("20000", "业务失败");
            }
        } else {
            codeMess = new CodeMess("20001", "通信失败");

        }*/
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
        String orderNo=request.getParameter("orderNo");
        String bizOrderNumber=request.getParameter("bizOrderNumber");
        String srcAmt=request.getParameter("srcAmt");
        String openId=request.getParameter("openId");
        logger.error("订单参数："+orderNo+",民生参数："+bizOrderNumber);
                PayInfoModel payInfoModel = new PayInfoModel();
                payInfoModel.setOrderNo(orderNo);
                logger.info("订单信息:" + payInfoModel);
                List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
                logger.info("获取订单信息失败");
                //如果支付状态等于1，相当于已记录
                if (list.size() != 0) {
                    logger.info("查询订单信息:" + list.get(0));
                    if (2 == list.get(0).getDealState()) {
                        //更新支付记录表
                        UserMainModel subModel=new UserMainModel();
                        subModel.setId(payInfoModel.getAccountId());
                        List<UserMainModel> userSubModels=this.userMainService.queryUserMainModel(subModel);
                        if(userSubModels.size()==0){
                            logger.error("通过用户充值userid未查询到用户");
                            return null;
                        }
                        if(srcAmt.equals(list.get(0).getDealRealMoney())){
                            logger.error("充值金额错误");
                            BlackUserModel blackUserModel=new BlackUserModel();
                            blackUserModel.setOpenId(openId);
                            blackUserModel.setUserId(list.get(0).getAccountId());
                            blackUserModel.setCreateTime(DateFormatUtil.formateString());
                            blackUserModel.setIp("一码付IP");
                            blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                            blackUserModel.setRechargeName("余额充值"+payInfoModel.getDealRealMoney()+"元");
                            blackUserModel.setRechargeNum(list.get(0).getOrderNo());
                            blackUserModel.setRechargePrice(list.get(0).getDealRealMoney().toString());
                            return null;
                        }
                        payInfoModel.setDealState(new Short("1"));
                        payInfoModel.setWxOrder(bizOrderNumber);
                        int result1 = payInfoServiceI.updatePayInfo(payInfoModel);
                        logger.info("修改订单结果" + result1);
                        logger.info("修改订单状态时，查询订单" + payInfoModel);
                        logger.error("审核信息用户信息"+JSONObject.toJSONString(userSubModels.get(0)));
                        String[] whitelist=whiteOpenId.split(",");
                        PhoneModel phoneModel = phoneQueryService.getPhone(userSubModels.get(0).getPhone());
                        for(String str:whitelist){
                            TemplateMessageUtil.sendPaySuccessByBalanceRecharge(payInfoModel.getOrderNo(),String.valueOf(list.get(0).getDealRealMoney()),
                                    payInfoModel.getDealTime(),userSubModels.get(0).getPhone(),phoneModel,str,srcAmt,reviewUrl.replace("NUM",payInfoModel.getOrderNo()).replace("DEFAULT","BALANCE"));
                        }
/*
                        BalanceModel balanceModelParam = new BalanceModel();
                        balanceModelParam.setAccountId(list.get(0).getAccountId());
                        logger.info("获取用户id" + list.get(0).getAccountId());
                        //根据userId获取余额信息
                        List<BalanceModel> resultList = balanceServiceI.queryBalances(balanceModelParam);
                        Double money = 0D;
                        if (resultList.size() != 0) {
                            logger.info("根据用户id获取余额信息:" + resultList.get(0));
                            money = resultList.get(0).getBalanceNumber() + list.get(0).getDealRealMoney();
                            logger.info("余额信息" + money);
                        }
                        //更新用户累计金额
                        balanceModelParam.setBalanceNumber(money);
                        balanceModelParam.setChangeTime(DateFormatUtil.formateString());
                        int result = balanceServiceI.updateBalance(balanceModelParam);
                        logger.info("用户金额累计" + balanceModelParam.getBalanceNumber());
                        if (result == 0) {
                            CodeMess codeMess = new CodeMess("20000", "余额更改失败");
                            String path = "c:\\giftpay_wap\\saveFailInfo\\";
                            net.sf.json.JSONObject jsonObject2 = new net.sf.json.JSONObject();
                            jsonObject2.put("订单号", payInfoModel.getOrderNo());// 订单号
                            jsonObject2.put("微信订单号", payInfoModel.getWxOrder());// 微信订单号
                            jsonObject2.put("商户信息", payInfoModel.getBusinessInfo());// 商户信息
                            jsonObject2.put("交易信息", payInfoModel.getDealInfo());// 交易信息
                            jsonObject2.put("支付金额", payInfoModel.getDealRealMoney());// 支付金额
                            jsonObject2.put("支付时间", payInfoModel.getDealTime());// 支付时间
                            jsonObject2.put("支付结果", "成功");// 支付结果
                            jsonObject2.put("与余额信息交互是否失败", "是");// 是否失败
                            jsonObject2.put("解决方式 ", "人工修改");// 解决方式
                            createFile(path, payInfoModel.getOrderNo() + ".txt");
                            WriterOrReaderTxt.writerTxt(path + payInfoModel.getOrderNo() + ".txt", jsonObject2.toString());
                            return JSON.toJSONString(codeMess);
                        } else {
                            CodeMess codeMess = new CodeMess("10000", payInfoModel.getOrderNo());
                            return JSON.toJSONString(codeMess);
                        }*/
                        return "1";
                    }
        }
        return null;
    }

    /**
     * 修改支付密码
     */
    @RequestMapping(value = "/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void sendMsg(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException {
        String phone=request.getParameter("phone");
        Random random = new Random();
        int valNum = random.nextInt(899999);
        valNum = valNum + 100000;//随机六位数
        String msgContent="【有礼付】您的验证码是"+valNum+"，如非本人操作请忽略该短信。";
        logger.info("发送短信验证码为："+valNum);
        net.sf.json.JSONObject jsonObject=MobileMessageUtil.sendMessage(phone,msgContent);
        logger.info("发送短信结果返回："+jsonObject);
        session.setAttribute("msgNum",String.valueOf(valNum));
    }

    public static Map<String, String> getResponseMap(HttpServletRequest request) throws IOException, DocumentException {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            return null;
        }

        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    public static void createFile(String path, String fileName) {

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    protected String getBaseUrl(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
                + "/";
        return basePath;
    }

    protected int stringToInt(String string){
        int j = 0;
        String str = string.substring(0, string.indexOf(".")) + string.substring(string.indexOf(".") + 1);
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    public static void main(String[] args) {
    }
}

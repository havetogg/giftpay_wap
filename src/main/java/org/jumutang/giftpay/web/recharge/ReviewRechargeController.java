
package org.jumutang.giftpay.web.recharge;

/**
 * Created by RuanYJ on 2017/4/5.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.controller.FlowOrderController;
import org.jumutang.giftpay.entity.WCOrderQuery;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * 生活缴费
 */
@Controller
@RequestMapping(value = "/giftpay/review")
public class ReviewRechargeController extends BaseController {


    private static final String SNSAPI_BASE = "snsapi_base";//静默授权
    private static final String SNSAPI_USERINFO = "snsapi_userinfo";//手动授权

    @Value("#{propertyFactoryBean['white_open_id']}")
    private String whiteOpenId;
    @Autowired
    UserNumRecordService userNumRecordServiceI;

    /**
     * 商户编号
     */
    @Value("#{propertyFactoryBean['oufei.yoka.userid']}")
    private String oiluserid;

    /**
     * 商户密码
     */
    @Value("#{propertyFactoryBean['oufei.yoka.userpws']}")
    private String oiluserpws;

    @Value("#{propertyFactoryBean['oufei.yoka.KeyStr']}")
    private String oilkeystr;
    @Value("#{propertyFactoryBean['URL_common']}")
    private String URLCommon;


    /**
     * 微信端登录授权
     *
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/reviewChareListIndex")
    public void reviewChareListIndex(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("进入查看订单方法");
        String url = getSendWXTokenTypeFunc(request, SNSAPI_BASE, response);
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/reviewChareListIndexUserinfo")
    public void reviewChareListIndexBase(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("进入手动授权方法");
        String url = getSendWXTokenTypeFunc(request, SNSAPI_USERINFO, response);
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/reviewOrderInfo")
    public ModelAndView reviewOrderInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openid");
        String orderNo = request.getParameter("orderNo");
        String rechargeType=request.getParameter("rechargeType");
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/review/reviewChareListIndexUserinfo.htm?orderNo=" + orderNo+"&rechargeType="+rechargeType;
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.contains(openId)) {
                res = true;
            }
        }
        if (!res) {
            ModelAndView modelAndView = new ModelAndView("/giftpay/404.jsp");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/giftpay/review/review.jsp");
            modelAndView.addObject("orderNo", orderNo);
            modelAndView.addObject("rechargeType", rechargeType);
            session.setAttribute("rechargeType",rechargeType);
            session.setAttribute("reviewOpenId", openId);
            return modelAndView;
        }
    }

    /**
     * 查询订单详情
     *
     * @param request
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryOrderInfo")
    @ResponseBody
    public String queryOrderInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        CodeMess codeMess = null;
        String openId = (String) session.getAttribute("reviewOpenId");
        String orderNo = request.getParameter("orderNo");
        String rechargeType= (String) session.getAttribute("rechargeType");
        if (StringUtils.isEmpty(openId)) {
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("reviewOpenId", openId);
        }
        if (StringUtils.isEmpty(rechargeType)) {
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("rechargeType", rechargeType);
        }
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.contains(openId)) {
                res = true;
            }
        }
        if (res) {
            PayInfoModel payInfoModel = new PayInfoModel();
            payInfoModel.setOrderNo(orderNo);
            List<PayInfoModel> list = this.payInfoServiceI.queryPayInfos(payInfoModel);
            if (list.size() == 0) {
                codeMess = new CodeMess("0", "用户数据异常");
            }else{
                if(rechargeType.equals("BALANCE")){
                    //余额显示
                    JSONObject object = new JSONObject();
                    object.put("balance",JSONArray.toJSONString(list));
                    codeMess = new CodeMess("2", object.toString());
                    //添加一马付查询结果
                    payInfoModel=list.get(0);
                    if(StringUtils.isEmpty(payInfoModel.getOfOrder())){

                    }
                }else{
                    OrderInfoModel orderInfoModel = new OrderInfoModel();
                    orderInfoModel.setOrderNo(orderNo);
                    List<OrderInfoModel> orderInfoModels = this.orderService.queryAllOrderInfo(orderInfoModel);
                    JSONObject object = new JSONObject();
                    if (orderInfoModels.size() == 0) {
                        object.put("orderInfo", "");
                    } else {
                        object.put("orderInfo", JSONArray.toJSONString(orderInfoModels));
                    }
                    if(!StringUtils.isEmpty(list.get(0).getOfOrder())) {
                        object.put("wxMap","");
                    }else{
                        Map<String, Object> wxMap = WXPayUtil.orderQuery(new WCOrderQuery(appId, mchID, list.get(0).getWxOrder()), key);
                        object.put("wxMap", JSONObject.toJSONString(wxMap));
                    }
                    BlackUserModel blackUserModel=new BlackUserModel();
                    blackUserModel.setOpenId(orderInfoModels.get(0).getOpenId());
                    List<BlackUserModel> blackUserModels=blackUserService.queryBlackUserList(blackUserModel);
                    if(blackUserModels.size()==0){
                        //不存在黑名单
                        object.put("black", false);
                    }else{
                        //存在黑名单
                        object.put("black", true);
                        object.put("blackUser",JSONArray.toJSONString(blackUserModels));
                    }
                    object.put("payInfo", JSONArray.toJSONString(list));
                    codeMess = new CodeMess("1", object.toString());
                }
            }
        } else {
            codeMess = new CodeMess("0", "查询用户非法");
        }
        return JSONObject.toJSONString(codeMess);
    }

    @RequestMapping(value = "/queryUserInfoByAccountId")
    @ResponseBody
    public String queryUserInfoByAccountId(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        CodeMess codeMess = null;
        String openId = (String) session.getAttribute("reviewOpenId");
        String accountId = request.getParameter("accountId");
        if (StringUtils.isEmpty(openId)) {
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("reviewOpenId", openId);
        }
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.contains(openId)) {
                res = true;
            }
        }
        if (res) {
            UserSubModel userSubModel = new UserSubModel();
            userSubModel.setOpenId(accountId);
            List<UserSubModel> subModels = this.userSubService.queryUserSubModel(userSubModel);
            if (subModels.size() == 0) {
                codeMess = new CodeMess("0", "用户数据异常");
            } else {
                codeMess = new CodeMess("1", JSONArray.toJSONString(subModels));
            }
        } else {
            codeMess = new CodeMess("0", "查询用户非法");
        }
        return JSONObject.toJSONString(codeMess);
    }


    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/toRechargeGoods")
    @ResponseBody
    public String toRechargeGoods(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, DocumentException {
        logger.error("进入充值订单接口");
        String orderNo = request.getParameter("orderNo");
        CodeMess codeMess = null;
        String openId = (String) session.getAttribute("reviewOpenId");
        String rechargeType= (String) session.getAttribute("rechargeType");
        if (StringUtils.isEmpty(openId)) {
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("reviewOpenId", openId);
        }
        if (StringUtils.isEmpty(rechargeType)) {
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("rechargeType", rechargeType);
        }
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.contains(openId)) {
                res = true;
            }
        }
        if (res) {
            PayInfoModel payInfoModel = new PayInfoModel();
            payInfoModel.setOrderNo(orderNo);
            List<PayInfoModel> list = this.payInfoServiceI.queryPayInfos(payInfoModel);
            if (list.size() == 0) {
                codeMess = new CodeMess("0", "查询数据异常");
                return JSONObject.toJSONString(codeMess);
            } else {
                payInfoModel = list.get(0);
                if (!StringUtils.isEmpty(payInfoModel.getOfOrder())) {
                    codeMess = new CodeMess("0", "订单已审核");
                    return JSONObject.toJSONString(codeMess);
                }
                if(rechargeType.equals("BALANCE")){
                    PayInfoModel payIn=new PayInfoModel();
                    payIn.setOrderNo(payInfoModel.getOrderNo());
                    payIn.setOfOrder(UUIDUtil.getUUID());
                    this.payInfoServiceI.updatePayInfo(payIn);
                    BalanceModel balanceModel=new BalanceModel();
                    balanceModel.setAccountId(payInfoModel.getAccountId());
                    List<BalanceModel> resultList = balanceServiceI.queryBalances(balanceModel);
                    Double money = 0D;
                    if (resultList.size() != 0) {
                        logger.info("根据用户id获取余额信息:" + resultList.get(0));
                        money = resultList.get(0).getBalanceNumber() + payInfoModel.getDealRealMoney();
                        logger.info("余额信息" + money);
                    }
                    //更新用户累计金额
                    balanceModel.setBalanceNumber(money);
                    balanceModel.setChangeTime(DateFormatUtil.formateString());
                    int result = balanceServiceI.updateBalance(balanceModel);
                    if (result == 0) {
                        codeMess = new CodeMess("0", "余额更改失败");
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
                        codeMess = new CodeMess("1", "余额充值成功");
                        return JSON.toJSONString(codeMess);
                    }
                }else{
                    //查询预支付
                    OrderInfoModel orderInfoModel = new OrderInfoModel();
                    orderInfoModel.setOrderNo(orderNo);
                    List<OrderInfoModel> orderInfoModels = this.orderService.queryAllOrderInfo(orderInfoModel);
                    if (orderInfoModels.size() == 0) {
                        codeMess = new CodeMess("0", "查询数据异常");
                        return JSONObject.toJSONString(codeMess);
                    } else {
                        orderInfoModel = orderInfoModels.get(0);
                        codeMess = rechargeOfGoodsByType(payInfoModel,orderInfoModel, codeMess);
                        return JSONObject.toJSONString(codeMess);
                    }
                }
            }
        } else {
            //非审核人员
            codeMess = new CodeMess("0", "查询用户非法");
            return JSONObject.toJSONString(codeMess);
        }
    }


    private  CodeMess rechargeOfGoodsByType(PayInfoModel payinfo,OrderInfoModel order, CodeMess codeMess) throws DocumentException {
        // 渠道 1:话费 2:油卡 3:流量 4:95加油 5余额充值 6生活缴费 99其他
        String type = order.getFromType();
        String params = order.getOrderParams();
        logger.error("充值类型:" + type + ",充值参数:" + params);
        Document doc = null;
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setOrderNo(order.getOrderNo());
        JSONObject parseObject=JSONObject.parseObject(params);
        if (type.equals("1")) {
            String sendReult = HttpUtil.sendPost(this.oufeiTelUrl, "gb2312", parseObject);
            doc = DocumentHelper.parseText(sendReult);
            Element rootElt = doc.getRootElement();
            String retcode = rootElt.elementTextTrim("retcode");
            if ("1".equals(retcode)) {
                logger.info("话费订单支付成功");
                String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
                String sporderId = rootElt.elementTextTrim("sporder_id");//订单编号
                String cardname = rootElt.elementTextTrim("cardname");
                String state = rootElt.elementTextTrim("game_state");
                payInfoModel.setOfOrder(orderid);
                payInfoModel.setDealRemark(parseObject.getString("game_userid"));
                if ("1".equals(state)) {
                    codeMess = new CodeMess("1", "充值成功");
                } else if ("0".equals(state)) {
                    codeMess = new CodeMess("1", "充值中");
                } else {
                    codeMess = new CodeMess("0", "充值失败");
                }
                this.payInfoServiceI.updatePayInfo(payInfoModel);
                UserNumRecord record = new UserNumRecord();
                boolean flag = userNumRecordServiceI.isNumExist(payinfo.getAccountId(), parseObject.getString("game_userid"), 2);
                if (!flag) {
                    //没有相同记录就插入
                    //将详单信息插入对应表
                    record.setId(UUIDUtil.getUUID());
                    record.setUserId(payinfo.getAccountId());
                    record.setNumber(parseObject.getString("game_userid"));
                    record.setNumber_desc(cardname.subSequence(0, 4) + "");
                    record.setNumberType(2);
                    record.setCreateTime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                    userNumRecordServiceI.addUserNumRecord(record);

                } else {
                    //有相同记录则更新时间
                    userNumRecordServiceI.updateCreateTime(payinfo.getAccountId(), parseObject.getString("game_userid"), new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                }

            } else {
                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
            }
        } else if (type.equals("2")) {
            logger.error("开始发送油卡充值接口");
            String sendReult = HttpUtil.sendPost(this.fuelCardRechargeUrl, "gb2312", parseObject);
            logger.error("油卡接口返回数据");
            doc = DocumentHelper.parseText(sendReult);
            Element rootElt = doc.getRootElement();
            String retcode = rootElt.elementTextTrim("retcode");
            if (!"1".equals(retcode)) {
                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                return codeMess;
            }
            String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
            String cardname = rootElt.elementTextTrim("cardname");//充值名称
            String sporderId = rootElt.elementTextTrim("sporderId");//订单编号
            String status = rootElt.elementTextTrim("game_state");
            payInfoModel.setOfOrder(orderid);
            payInfoModel.setDealRemark(parseObject.getString("game_userid"));
            if (status.equals("1")) {
                //充值成功
                codeMess = new CodeMess("1", "充值成功");
            } else if (status.equals("0")) {
                //充值中
                codeMess = new CodeMess("1", "充值中");
            } else {
                //充值失败
                codeMess = new CodeMess("0", "充值失败");
            }
            this.payInfoServiceI.updatePayInfo(payInfoModel);

        } else if (type.equals("3")) {
            FlowOrderController flowOrderController = new FlowOrderController();
            JSONObject data2Map = JSONObject.parseObject(params);
            String mobile = String.valueOf(data2Map.getString("mobile")); //充值手机号码
            String number_desc = String.valueOf(data2Map.getString("number_desc")); //运营商
            String rechargeAmount = String.valueOf(data2Map.getString("rechargeAmount")); //充值套餐
            String packCode = String.valueOf(data2Map.getString("packCode")); //充值套餐 代码.
            String Result = flowOrderController.sendOrder(packCode, mobile, order.getOrderNo());

            if("RechargeSuccess".equals(Result)){

                //更新订单 充值操作 状态
                codeMess = new CodeMess("1", "充值成功");

                payInfoModel.setOrderNo(order.getOrderNo()); //更新当前预支付订单表数据
                payInfoModel.setOfOrder("RechargeSuccess");
                //payInfoModel.setDealRemark(mobile);
                this.payInfoServiceI.updatePayInfo(payInfoModel);

                //添加历史记录 用户 [充值成功，给当前用户下添加历史充值记录]
                boolean isExits = userNumRecordServiceI.isNumExist(payinfo.getAccountId(), mobile, 3);
                logger.error("--------------- 当前查询结果:[" + isExits + "]！-------------------------");
                if (isExits == false) {
                    logger.error("--------------- 当前userId用户  Add 历史充值流量记录 ！-------------------------");
                    //新增数据
                    userNumRecordServiceI.addUserNumRecordByFlow(UUIDUtil.getUUID().toString(), 3, mobile, payinfo.getAccountId(), number_desc, DateFormatUtil.formateString());
                } else {
                    logger.error("--------------- 当前userId用户 更新充值createTime ！-------------------------");
                    //更新数据
                    userNumRecordServiceI.updateCreateTime(payinfo.getAccountId(), mobile, DateFormatUtil.formateString());
                }

                //添加充值历史记录
                //插入支付信息 [充值成功，更新充值状态]
               /* logger.error("----------RechargeController----------初始化插入充值订单信息[ 网迅流量充值 ]----------------- ]");
                payinfo.setDealState((short)0);
                payinfo.setBusinessInfo("有礼付流量充值");
                payinfo.setDealRemark(mobile);
                payinfo.setDealInfo(number_desc + "流量充值" + rechargeAmount);
                payinfo.setDealState((short) 0); //充值中
                int count = this.payInfoServiceI.insertPayInfo(payinfo);*/

            }else{
                codeMess = new CodeMess("0", "充值失败");
            }
        } else if (type.equals("4")) {

        } else if (type.equals("5")) {

        } else if (type.equals("6")) {

        } else {

        }
        return codeMess;
    }


    public String getSendWXTokenTypeFunc(HttpServletRequest request, String type, HttpServletResponse response) {
        Long timestamp = System.currentTimeMillis();
        String orderNo = request.getParameter("orderNo");
        String rechargeType=request.getParameter("type");
        String url = "";
        if (orderNo == null || orderNo.equals("")) {
            url = response.encodeRedirectURL(URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") + "giftpay/review/error.html"));
        } else {
            String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
            String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
            String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                    "giftpay/review/reviewOrderInfo.htm?orderNo=" + orderNo+"&rechargeType="+rechargeType);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope="+type+"&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        return url;
    }
    public String getSendWXTokenByRefund(HttpServletRequest request, String type, HttpServletResponse response) {
        Long timestamp = System.currentTimeMillis();
        String orderNo = request.getParameter("orderNo");
        String rechargeType=request.getParameter("type");
        String url = "";
        if (orderNo == null || orderNo.equals("")) {
            url = response.encodeRedirectURL(URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") + "giftpay/review/error.html"));
        } else {
            String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
            String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
            String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                    "giftpay/review/refundUser.htm?orderNo=" + orderNo+"&rechargeType="+rechargeType);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope="+type+"&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        return url;
    }
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/queryOFOrderInfo")
    @ResponseBody
    public String queryOFOrderInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        String orderNo = request.getParameter("orderNo");
        OrderInfoModel orderInfoModel=new OrderInfoModel();
        List<OrderInfoModel> list=this.orderService.queryAllOrderInfo(orderInfoModel);
        orderInfoModel=list.get(0);
        String userId="";
        String pwds="";
        String keystr="";
        //1:话费 2:油卡 3:流量
        if(orderInfoModel.equals("1")){
            userId=telUserid;
            pwds=telUserpws;
            keystr=telKeystr;
        }else if(orderInfoModel.equals("2")){
            userId=oiluserid;
            pwds=oiluserpws;
            keystr=oilkeystr;
        }
        String md5=MD5Util.MD5Encode(userId+pwds+orderNo+keystr).toUpperCase();
        String href="http://api2.ofpay.com/queryOrderInfo.do?userid="+userId+"&userpws="+pwds+"&sporder_id="+orderNo+"&md5_str="+md5+"&version=6.0";
        return href;
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



    /**
     * 微信端登录授权
     * 一码付授权登录
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/refundIndexBase")
    public void refundIndexBase(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("进入查看一码付退款方法");
        String url = getSendWXTokenByRefund(request, SNSAPI_BASE, response);
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/refundIndexInfo")
    public void refundIndexInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("进入手动授权查看一码付退款方法");
        String url = getSendWXTokenByRefund(request, SNSAPI_USERINFO, response);
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/refundUser")
    public ModelAndView refundUser(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openid");
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/review/refundIndexInfo.htm";
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        String[] whiteList = whiteOpenId.split(",");
        logger.error("获取id白名单:" + JSONArray.toJSONString(whiteList));
        boolean res = false;
        for (String str : whiteList) {
            if (str.contains(openId)) {
                res = true;
            }
        }
        if (!res) {
            ModelAndView modelAndView = new ModelAndView("/giftpay/404.jsp");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/giftpay/wap/refund.jsp");
            session.setAttribute("openId", openId);
            return modelAndView;
        }
    }
    @RequestMapping(value = "/doRefundCmbc")
    @ResponseBody
    public String doRefundCmbc(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String order=request.getParameter("order");
        String srcAmt=request.getParameter("srcAmt");
        Map<String,String> params=new HashMap<String,String>();
        params.put("order",order);
        params.put("srcAmt",srcAmt);
        String result=HttpUtil.sendPost("http://"+URLCommon+"/wxpay_gate/retundCmbcOrder.htm","utf-8",params);
        return result;
    }
}

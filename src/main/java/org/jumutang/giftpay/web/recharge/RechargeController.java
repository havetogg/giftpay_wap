package org.jumutang.giftpay.web.recharge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONArray;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.collections.map.HashedMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.controller.FlowOrderController;
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreOrderResponse;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.IOilRecordService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by RuanYJ on 2017/3/31.
 */

/**
 * 充值缴费
 */
@Controller
@RequestMapping(value = "/giftpay/recharge")
public class RechargeController extends BaseController {

    @Value("#{propertyFactoryBean['URL_common']}")
    private  String URL_common ;

    private  String REDIRECTURL = "http://www.linkgift.cn/giftpay_wap/giftpay/recharge/receivePreOrder.htm";
    private  String RECHARGE_REDIRECTURL = "http://www.linkgift.cn/giftpay_wap/giftpay/recharge/updateAccountPayOrderStatus.htm";
    private static final String YOUKA = "youka";
    private static final String LIULIANG = "liuliang";
    private static final String HUAFEI = "huafei";
    private static final String LIFE = "life";
    private static final String ZSHGAME = "zshgame";
    private static final String SHUI = "001";
    private static final String DIAN = "002";
    private static final String MEI = "003";

    @Autowired
    UserNumRecordService userNumRecordServiceI;

    @Autowired
    IOilRecordService iOilRecordService;

    @Autowired
    PhoneQueryService phoneQueryService;

    @Value("#{propertyFactoryBean['white_open_id']}")
    private String whiteOpenId;

    /**
     * 红包状态 已经使用
     */
    private final int REDPKG_STATE_USED = 2;


    /**
     * 通过余额支付缴费
     */
    @RequestMapping(value = "/payByBanlance", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String payByBanlance(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException, DocumentException, Exception {

        logger.info("进入通过余额支付缴费");
        CodeMess codeMess = null;
        //控制接口调用频率
        if(session.getAttribute("payByBanlanceTime") == null){
            session.setAttribute("payByBanlanceTime", new Date());
        }else {
            Date payByBanlanceDate = (Date) session.getAttribute("payByBanlanceTime");
            Date currentDate = new Date();
            int interval = (int) (currentDate.getTime() - payByBanlanceDate.getTime()) / 1000;
            session.setAttribute("payByBanlanceTime", new Date());
            if (interval < 30) {
                codeMess = new CodeMess("0", "请求次数过多,请稍后重试");
                return JSON.toJSONString(codeMess);
            }
        }

        //通过session获取userId和openId
        String userId = getUserId(session);
        String openId = (String) session.getAttribute("openId");
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            userId = request.getParameter("userId");
            openId = request.getParameter("openId");
            if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
                codeMess = new CodeMess("0", "重新登录");
                return JSON.toJSONString(codeMess);
            }
        }
        //增加payInfo记录
        PayInfoModel payInfoModel = new PayInfoModel();
        String goodsName = request.getParameter("rechargeName").trim();//充值名称(水电煤)
        String dealMoney = request.getParameter("payamount").trim();     //充值金额
        String dealRealMoney = request.getParameter("realmount").trim();//真实付款金额
        String rechargeType = request.getParameter("rechargeType").trim();//充值类型（油卡话费等）
        String redPkgId = request.getParameter("redPkgId").trim();//红包ID 支付成功后更新红包状态
        String redirectUrl = request.getParameter("redirectUrl").trim();
        String payInfo=request.getParameter("payInfo");//md5

        if ((Double.parseDouble(dealMoney)) < 0D) {
            codeMess = new CodeMess("3", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        if ((Double.parseDouble(dealRealMoney)) < 0D) {
            codeMess = new CodeMess("3", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        payInfoModel.setAccountId(userId);
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealState(new Short("2"));//'1.成功 2.未成功 （提现状态）3. 提现中 4. 已提现 5.提现失败',
        payInfoModel.setDealType(new Short("3"));//1.提现 2.充值 3.支付 4.退款',
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setBusinessInfo("有礼付缴费充值");
        payInfoModel.setDealMoney(Double.parseDouble(dealMoney));
        payInfoModel.setDealRealMoney(Double.parseDouble(dealRealMoney));
        payInfoModel.setOrderNo(UUIDUtil.getUUID());
        payInfoModel.setDealInfo(goodsName + "(余额支付)");

        //查询用户余额
        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setAccountId(userId);
        List<BalanceModel> balanceModels = this.balanceServiceI.queryBalances(balanceModel);
        if(!(balanceModels.size()>0)){
            codeMess = new CodeMess("0", "无法查询用用户余额信息");
            return JSON.toJSONString(codeMess);
        }

        //根据充值类型获取充值接口进行充值
        if (rechargeType.equals(ZSHGAME)) {
            //中石化游戏中心
            payInfoModel.setBusinessInfo("中石化游戏中心充值");
            this.payInfoServiceI.insertPayInfo(payInfoModel);
            balanceModel = new BalanceModel();
            balanceModel.setAccountId(userId);
            balanceModel.setChangeTime(DateFormatUtil.formateString());
            balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
            int result = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
            PayInfoModel pay = new PayInfoModel();
            pay.setOrderNo(payInfoModel.getOrderNo());
            pay.setWxOrder(UUIDUtil.getUUID());
            pay.setDealState(new Short("1"));
            this.payInfoServiceI.updatePayInfo(pay);
            Map<String, String> redParams = new HashMap<String, String>();
            if (!StringUtils.isEmpty(redPkgId)) {
                redParams.put("openId", openId);
                redParams.put("redpkgId", redPkgId);
                redParams.put("state", "2");
                String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                logger.error("修改红包返回结果:" + updateRedPkgResult);
            }
            codeMess = new CodeMess("1", pay.getOrderNo());//充值成功
        } else if (rechargeType.equals(YOUKA)) {
            logger.info("进入油卡余额充值代码");

            boolean nineOil = false;
            //95加油用户才能用余额支付
            logger.info("查询用户是否是95折加油用户");
            List<OilRecordModel> oilRecordModelList = iOilRecordService.queryOilRecordModelList(new OilRecordModel());
            for(OilRecordModel oilRecordModel:oilRecordModelList){
                if(oilRecordModel.getOpenid().equals(openId)){
                    //符合购买用户
                    logger.info("用户是购买95折加油用户可以购买");
                    nineOil = true;
                }
            }
            if(!nineOil){
                logger.info("用户不是九五折用户返回");
                codeMess = new CodeMess("0", "不是95折加油用户");
                return JSON.toJSONString(codeMess);
            }

            //判断零钱金额是否低于付款金额
            logger.info("查询零钱金额是否低于付款金额");
            if(balanceModels.get(0).getBalanceNumber()<payInfoModel.getDealRealMoney()){
                logger.info("零钱金额低于付款金额");
                codeMess = new CodeMess("0", "零钱金额低于付款金额");
                return JSON.toJSONString(codeMess);
            }

            //油卡
            Map<String, String> params = (Map<String, String>) session.getAttribute("fuelRechargeParams");
            if (params == null) {
                logger.info("session中没有加油信息");
                codeMess = new CodeMess("0", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            String chargeName=params.get("chargeType");
            if(StringUtils.isEmpty(chargeName)){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }else{
                if(chargeName.equals("1")){
                    chargeName="中石化";
                }else{
                    chargeName="中石油";
                }
            }
            String md5Info=MD5Util.MD5Encode(chargeName+params.get("sporder_id")+payInfoModel.getDealRealMoney()+IPUtil.getIpAddr(request)).toUpperCase();
            logger.error("获取支付页参数MD5校验:"+md5Info+",预支付MD5:"+payInfo);
            if(!md5Info.equals(payInfo)){
                logger.info("MD5校验失败用户加入黑名单");
                codeMess = new CodeMess("30000", "数据异常");
                //数据异常 加黑名单
                BlackUserModel blackUserModel = new BlackUserModel();
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName("有礼付余额油卡充值");
                blackUserModel.setRechargeNum(params.get("game_userid"));
                blackUserModel.setRechargePrice(payInfoModel.getDealRealMoney().toString());
                //判断当前用户是否存在黑名单中，不存在则添加当前用户进入黑名单 [addBlackUser方法在下]
                addBlackUser(blackUserModel);
                return JSON.toJSONString(codeMess);
            }
            logger.info("查询是否有订单信息");
            OrderInfoModel orderInfoModel=new OrderInfoModel();
            orderInfoModel.setMd5Sign(md5Info);
            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
            if(orderInfoModels.size()==0){
                logger.info("无加油订单信息");
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            orderInfoModel = orderInfoModels.get(0);
            logger.info("=====================欧飞接口参数params: " + params);
            String cardValueStr = params.get("cardValueStr");
            String cardnumStr = params.get("cardnum");
            String game_userid = params.get("game_userid");
            PostOrGet postOrGetRed = new PostOrGet("utf-8");

            Double cardValue = Double.valueOf(cardValueStr);
            payInfoModel.setDealMoney(cardValue);
            payInfoModel.setOrderNo(params.get("sporder_id"));
            payInfoModel.setDealRemark(game_userid);
            payInfoServiceI.insertPayInfo(payInfoModel);
            logger.info("=================生成初始订单信息: " + payInfoModel.toString());

            String result = HttpUtil.sendPost(this.fuelCardRechargeUrl, "gb2312", params);
            logger.info("=================油卡充值result" + result);
            if ("".equals(result)) {
                logger.error("=================调用欧飞接口异常");
                codeMess = new CodeMess("-1", "油卡充值失败");
                return JSONObject.toJSONString(codeMess);
            }
            String jsonDataStr = "";
            try {
                jsonDataStr = new XMLSerializer().read(result).toString();
            } catch (Exception e) {
                logger.error("=================xml文件解析异常");
                codeMess = new CodeMess("-1", "油卡充值失败");
                return JSONObject.toJSONString(codeMess);
            }
            JSONObject jsonObj = JSONObject.parseObject(jsonDataStr);
            logger.info("=================油卡充值，欧飞接口调用返回结果：" + jsonDataStr);
            String state = jsonObj.getString("game_state");
            if ("1".equals(jsonObj.getString("retcode"))) {
                payInfoModel.setDealInfo(jsonObj.getString(goodsName + "(余额支付)"));
                payInfoModel.setWxOrder(jsonObj.getString("orderid"));
                if ("1".equals(state)) {
                    //game_state=1表示充值成功，更新订单状态为1，表示成功  0充值中  9充值失败
                    short dealState = 1;//1.成功 2.未成功 （提现状态）3. 提现中 4. 已提现 5.提现失败
                    payInfoModel.setDealState(dealState);

                    //执行扣余额
                    balanceModel = new BalanceModel();
                    balanceModel.setAccountId(userId);
                    balanceModel.setChangeTime(DateFormatUtil.formateString());
                    balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                    int balanceResult = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
                    logger.info("========================更新余额表：" + balanceResult);

                    //加油成功推送消息
                    /*logger.info("开始进入推送消息");
                    String[] whitelist=whiteOpenId.split(",");
                    for(String str:whitelist){
                        logger.info("推送对象:"+str);
                        TemplateMessageUtil.sendPaySuccessByOilRecharge(orderInfoModel.getOrderNo(),orderInfoModel.getGoodsMoney(),
                                orderInfoModel.getCreateTime(),game_userid,str,orderInfoModel.getPayMoney(),"",1);
                    }
                    logger.info("推送消息结束");*/

                    //如果使用了红包，要更新红包状态
                    if (!"".equals(redPkgId)) {
                        Map<String, String> redParams = new HashMap<String, String>();
                        redParams.put("openId", openId);
                        redParams.put("redpkgId", redPkgId);
                        redParams.put("state", "2");
                        String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                        logger.error("修改红包返回结果:" + updateRedPkgResult);
                    }

                    codeMess = new CodeMess("1", payInfoModel.getOrderNo());//充值成功
                    logger.info("=================油卡充值成功，更新油卡欧飞返回订单编号、状态");
                } else if ("0".equals(state)) {
                    //充值中
                    payInfoModel.setDealState(new Short("0"));
                    codeMess = new CodeMess("2", payInfoModel.getOrderNo());//充值中

                    //执行扣余额
                    balanceModel = new BalanceModel();
                    balanceModel.setAccountId(userId);
                    balanceModel.setChangeTime(DateFormatUtil.formateString());
                    balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                    int balanceResult = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
                    logger.info("========================更新余额表：" + balanceResult);

                    //加油成功推送消息
                    /*logger.info("开始进入推送消息");
                    String[] whitelist=whiteOpenId.split(",");
                    for(String str:whitelist){
                        logger.info("推送对象:"+str);
                        TemplateMessageUtil.sendPaySuccessByOilRecharge(orderInfoModel.getOrderNo(),orderInfoModel.getGoodsMoney(),
                                orderInfoModel.getCreateTime(),game_userid,str,orderInfoModel.getPayMoney(),"",1);
                    }
                    logger.info("推送消息结束");*/
                } else if ("9".equals(state)) {
                    //充值失败
                    payInfoModel.setDealState(new Short(state));
                    codeMess = new CodeMess("3", "充值失败");//充值失败
                }
                session.setAttribute("orderPayInfo", payInfoModel);
                //更新订单状态信息
                payInfoServiceI.updatePayInfo(payInfoModel);
                return JSONObject.toJSONString(codeMess);
            } else {
                logger.error("=================油卡充值欧飞接口调用发生错误：" + jsonObj.getString("err_msg"));
                codeMess = new CodeMess(jsonObj.getString("retcode"), jsonObj.getString("err_msg"));
            }
        } else if (rechargeType.equals(HUAFEI)) {

            /***************************话费************************************/

            Map<String, String> params = (Map<String, String>) session.getAttribute("telParams");
            logger.error("话费接口参数获取："+params);
            if (params == null) {
                codeMess = new CodeMess("-1", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            String companyName=(String) session.getAttribute("companyName");
            if(StringUtils.isEmpty(companyName)){
                codeMess = new CodeMess("-1", "重新登录");
                return JSON.toJSONString(codeMess);
            }else{
                session.setAttribute("companyName",companyName);
            }
            String md5Info=MD5Util.MD5Encode(companyName+params.get("sporder_id")+payInfoModel.getDealRealMoney()+IPUtil.getIpAddr(request)).toUpperCase();
            logger.error("获取支付页参数MD5校验:"+md5Info+",预支付MD5:"+payInfo);
           /* BlackUserModel blackUserModel=new BlackUserModel();
            if(!md5Info.equals(payInfo)){
                codeMess = new CodeMess("30000", "数据异常");
                //数据异常 加黑名单
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(companyName+"话费充值"+payInfoModel.getDealRealMoney()+"元");
                blackUserModel.setRechargeNum(params.get("game_userid"));
                blackUserModel.setRechargePrice(payInfoModel.getDealRealMoney().toString());
                return JSON.toJSONString(codeMess);
            }*/
            OrderInfoModel orderInfoModel=new OrderInfoModel();
            orderInfoModel.setMd5Sign(md5Info);
            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
            if(orderInfoModels.size()==0){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            orderInfoModel=orderInfoModels.get(0);
            payInfoModel.setDealInfo("有礼付充值-话费(余额支付)");
            payInfoModel.setDealRealMoney(Double.parseDouble(orderInfoModel.getPayMoney()));
            payInfoModel.setDealMoney(Double.parseDouble(orderInfoModel.getGoodsMoney()));
            payInfoModel.setDealRemark(params.get("game_userid"));
            payInfoModel.setOrderNo(params.get("sporder_id")); //校验成功，更新订单号
            payInfoModel.setDealState((short) 2);
            logger.error("订单参数:" + JSONObject.toJSONString(payInfoModel));
            this.payInfoServiceI.insertPayInfo(payInfoModel);
            if (params == null) {
                codeMess = new CodeMess("0", "重新登录");
                return JSON.toJSONString(codeMess);
            } else {
                session.setAttribute("telParams", params);
            }
            String sendReult = HttpUtil.sendPost(this.oufeiTelUrl, "gb2312", params);
            logger.error("发送欧飞调用支付接口返回信息:" + sendReult);
            Document doc = null;
            doc = DocumentHelper.parseText(sendReult);
            Element rootElt = doc.getRootElement();
            String retcode = rootElt.elementTextTrim("retcode");
            logger.info("开始进入推送消息");
            String[] whitelist=whiteOpenId.split(",");
            PhoneModel phoneModel = phoneQueryService.getPhone(orderInfoModel.getRemark());
            for(String str:whitelist){
                logger.info("推送对象:"+str);
                TemplateMessageUtil.sendPaySuccessByPhoneRecharge(orderInfoModel.getOrderNo(),orderInfoModel.getGoodsMoney(),
                        orderInfoModel.getCreateTime(),orderInfoModel.getRemark(),phoneModel,str,orderInfoModel.getPayMoney(),"",1);
            }
            logger.info("推送消息结束");
            if ("1".equals(retcode)) {
                logger.info("订单支付成功");
                String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
                String cardname = rootElt.elementTextTrim("cardname");//充值名称
                String sporderId = rootElt.elementTextTrim("sporderId");//订单编号
                String state = rootElt.elementTextTrim("game_state"); //如果成功将为1，澈消(充值失败)为9，充值中为0,只能当状态为9时，商户才可以退款给用户。
                String ordercash = rootElt.elementTextTrim("ordercash");//订单金额（面值）
                payInfoModel.setWxOrder(orderid);
                payInfoModel.setDealInfo(cardname);
                payInfoModel.setDealMoney(Double.valueOf(ordercash));
                payInfoModel.setDealRealMoney(Double.valueOf(dealMoney));
                if ("1".equals(state)) {
                    logger.info("话费充值成功");
                    payInfoModel.setDealState((short) 1);
                    int num = this.payInfoServiceI.updatePayInfo(payInfoModel);
                    if (num == 0) {
                        codeMess = new CodeMess("3", "业务失败");
                        return JSON.toJSONString(codeMess);
                    }
                    //执行扣余额
                    balanceModel = new BalanceModel();
                    balanceModel.setAccountId(userId);
                    balanceModel.setChangeTime(DateFormatUtil.formateString());
                    balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                    this.balanceServiceI.updateBalance(balanceModel);

                    if (!StringUtils.isEmpty(redPkgId)) {
                        //如果使用了红包则更新红包状态
                        Map<String, String> redParams = new HashMap<String, String>();
                        redParams.put("openId", openId);
                        redParams.put("redpkgId", redPkgId);
                        redParams.put("state", "2");
                        String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                        logger.error("修改红包返回结果:" + updateRedPkgResult);
                    }
                    codeMess = new CodeMess("1", payInfoModel.getOrderNo());//充值成功

                } else if ("0".equals(state)) {
                    //充值中
                    payInfoModel.setDealState(new Short("0"));
                    this.payInfoServiceI.updatePayInfo(payInfoModel);

                    //执行扣余额
                    balanceModel = new BalanceModel();
                    balanceModel.setAccountId(userId);
                    balanceModel.setChangeTime(DateFormatUtil.formateString());
                    balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                    int result = this.balanceServiceI.updateBalance(balanceModel);//更新余额表

                    codeMess = new CodeMess("2", payInfoModel.getOrderNo());//充值中

                } else if ("9".equals(state)) {
                    //充值失败
                    payInfoModel.setDealState(new Short(state));
                    this.payInfoServiceI.updatePayInfo(payInfoModel);
                    codeMess = new CodeMess("3", "充值失败");//充值失败
                }
                //查询是否有相同的记录
                UserNumRecord record = new UserNumRecord();
                boolean flag = userNumRecordServiceI.isNumExist(userId, params.get("game_userid"), 2);
                if (!flag) {
                    //没有相同记录就插入
                    //将详单信息插入对应表
                    record.setId(UUIDUtil.getUUID());
                    record.setUserId(userId);
                    record.setNumber(params.get("game_userid"));
                    record.setNumber_desc(companyName);
                    record.setNumberType(2);
                    record.setCreateTime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                    userNumRecordServiceI.addUserNumRecord(record);
                } else {
                    //有相同记录则更新时间
                    userNumRecordServiceI.updateCreateTime(userId, params.get("game_userid"), new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                }
            } else {
                logger.info("订单支付失败：" + rootElt.elementTextTrim("err_msg"));
                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                return JSONObject.toJSONString(codeMess);
            }
            //更新订单信息
            payInfoModel.setBusinessInfo("有礼付话费充值");
            int result = this.payInfoServiceI.updatePayInfo(payInfoModel);
        } else if (rechargeType.equals(LIULIANG)) {

            /***************************流量************************************/

            //获取用户点击支付时候，发送的订单到后台的数据data2Map
            Map<String, String> data2Map = (Map<String, String>) session.getAttribute("data2Map");

            if (data2Map == null) {
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }

            String timestamp = data2Map.get("timestamp");
            String md5str = MD5Util.MD5Encode(dealRealMoney + rechargeType + timestamp).toUpperCase();
            String payMd5 = (String) session.getAttribute("payMd5");
            if (!payMd5.equals(md5str)) {
                codeMess = new CodeMess("30000", "业务失败");
                return JSON.toJSONString(codeMess);
            }

            //Map<String, String> data2Map = (Map<String, String>) session.getAttribute("data2Map");
            String mobile = String.valueOf(data2Map.get("mobile")); //充值手机号码
            String number_desc = String.valueOf(data2Map.get("number_desc")); //运营商
            String rechargeAmount = String.valueOf(data2Map.get("rechargeAmount")); //充值套餐
            String packCode = String.valueOf(data2Map.get("packCode")); //充值套餐 代码

            //继承微信支付订单中的相应数据，并对个别参数进行重新赋值操作
            PayInfoModel FlowPayInfo = payInfoModel;

            FlowPayInfo.setBusinessInfo("有礼付流量充值");
            FlowPayInfo.setDealRemark(mobile);
            FlowPayInfo.setDealInfo(number_desc + "流量充值" + rechargeAmount);
            FlowPayInfo.setDealState((short) 0); //充值中

            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(FlowPayInfo);
            logger.error("------------导出当前payInfo中的参数：" + json.toString() + "----------[余额支付]------");

            //插入支付信息 [充值成功，更新充值状态]
            logger.error("----------RechargeController----------初始化插入充值订单信息[ 网迅流量充值 ]--------[余额支付]--------- ]");
            int count = this.payInfoServiceI.insertPayInfo(FlowPayInfo);
            logger.error("----------初始化插入订单信息Count:[" + count + "]---<---[1:成功，0失败]----------------[余额支付]----------");


            logger.error("---------RechargeController-----------发送流量订单请求[网迅流量接口]--------[余额支付]---------");
            FlowOrderController flowOrderController = new FlowOrderController();
            String Result = flowOrderController.sendOrder(packCode, mobile, FlowPayInfo.getOrderNo());
            logger.error("--------充值方法返回参数:[" + Result + "]-------[余额支付]---------");

            if ("RechargeSuccess".equals(Result)) {
                logger.error("---------充值成功！ 返回参数:[" + Result + "]------[余额支付]-----------");

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf2.format(new Date());
                UUID uuid = UUID.randomUUID();

                logger.error("--------------- 充值成功，查询当前用户是否存在该号码历史充值记录，如果不存在则添加该记录到t_RechargeRecord表 ！--------[余额支付]-----------------");
                boolean isExits = userNumRecordServiceI.isNumExist(userId, mobile, 3);
                logger.error("--------------- 当前查询结果:[" + isExits + "]！------------[余额支付]-------------");

                if (isExits == false) {
                    logger.error("--------------- 当前userId用户  Add 历史充值流量记录 ！---------[余额支付]----------------");
                    //新增数据
                    userNumRecordServiceI.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
                } else {
                    logger.error("--------------- 当前userId用户 更新充值createTime ！----------[余额支付]---------------");
                    //更新数据
                    userNumRecordServiceI.updateCreateTime(userId, mobile, time);
                }

                logger.error("----------RechargeController-------判断红包是否存在[ " + redPkgId + " ]----[余额支付]-------");
                if (redPkgId != null && !"".equals(redPkgId)) {
                    //更新红包状态
                    Map<String, String> redParams = new HashMap<String, String>();

                    redParams.put("openId", openId);
                    redParams.put("redpkgId", redPkgId);
                    redParams.put("state", "2");
                    logger.error("---------[ openId:" + openId + " , redpkgId :" + redPkgId + " , state: " + redParams.get("state").toString() + "]----[余额支付]-------");
                    String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                    logger.error("---------RedPkg--------红包使用状态更新:" + updateRedPkgResult + "------[余额支付]-----");
                }

                logger.info("--------执行扣除余额操作!----[余额支付]-------");
                //执行扣余额
                balanceModel = new BalanceModel();
                balanceModel.setAccountId(userId);
                balanceModel.setChangeTime(sdf2.format(new Date()));
                balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                int result = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
                logger.info("----Balance表-----余额更新状态result:" + result + "------[余额支付]-----");

            } else {
                logger.error("----------流量订单充值失败！ 返回异常参数:[" + Result + "]--------更新订单充值statu:[3]-------[余额支付]----------");

                logger.error("----------查询用户是否存在余额表[1.存在余额表，更新数据；2.不存在余额表，插入数据]---------[余额支付]--------");
                //充值失败
//                balanceModel.setAccountId(userId);
//                try {
//                    balanceModel=this.balanceServiceI.queryBalances(balanceModel).get(0);
//                } catch (Exception e) {
//                    logger.error("-----获取用户余额信息失败---[余额支付]---");
//                }
//                logger.error("----------更新用户的余额表数据:[balance:"+balanceModel.getBalanceNumber()+" ; dealRealMoney:"+FlowPayInfo.getDealRealMoney()+"]-----------------");
//                balanceModel.setBalanceNumber(balanceModel.getBalanceNumber()+FlowPayInfo.getDealRealMoney());
//                balanceModel.setChangeTime(DateFormatUtil.formateString());
//                this.balanceServiceI.updateBalance(balanceModel);
//                logger.error("----------充值失败 返回金额到用户账户余额----[余额支付]------");

                logger.info("-------余额充值流量失败，不进行扣款操作！--------");

            }

            logger.error("----------------流量充值流程---已完毕,准备退出当前流程！---------[余额支付]-----------");

            codeMess = new CodeMess("2", "余额支付流量业务，暂时关闭");
            return JSON.toJSONString(codeMess);

        } else if (rechargeType.equals(LIFE)) {
            //生活缴费类型
            String lifeType = request.getParameter("lifeType").trim();
            Map<String, String> params = (Map<String, String>) session.getAttribute("lifeRechargeParams");
            String timestamp = params.get("timestamp");
            String md5str = MD5Util.MD5Encode(dealMoney + rechargeType + timestamp).toUpperCase();
            String payMd5 = (String) session.getAttribute("payMd5");
            if (!payMd5.equals(md5str)) {
                codeMess = new CodeMess("3", "业务失败");
                return JSON.toJSONString(codeMess);
            }
            params.put("actPrice", String.valueOf(payInfoModel.getDealRealMoney()));
            params.put("ret_url", RECHARGE_REDIRECTURL + "?retUrl=" + redirectUrl);
            payInfoModel.setOrderNo(params.get("sporderId"));
            payInfoModel.setDealRemark(params.get("account"));
            logger.error("订单参数:" + JSONObject.toJSONString(params));
            this.payInfoServiceI.insertPayInfo(payInfoModel);
            if (params == null) {
                codeMess = new CodeMess("0", "重新登录");
                return JSON.toJSONString(codeMess);
            } else {
                session.setAttribute("lifeRechargeParams", params);
            }
            String sendReult = HttpUtil.sendPost(this.oufeiUtilityOrderUrl, "gb2312", params);
            logger.error("发送欧飞调用支付接口返回信息:" + sendReult);
            Document doc = null;
            doc = DocumentHelper.parseText(sendReult);
            Element rootElt = doc.getRootElement();
            String retcode = rootElt.elementTextTrim("retcode");
            if (!retcode.equals("1")) {
                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                return JSONObject.toJSONString(codeMess);
            }
            String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
            String cardname = rootElt.elementTextTrim("cardname");//充值名称
            String sporderId = rootElt.elementTextTrim("sporderId");//订单编号
            String status = rootElt.elementTextTrim("status"); //如果成功将为1，澈消(充值失败)为9，充值中为0,只能当状态为9时，商户才可以退款给用户。
            if (status.equals("1") || status.equals("0")) {
                //充值成功
                payInfoModel.setDealState(new Short(status));
                payInfoModel.setWxOrder(orderid);
                this.payInfoServiceI.updatePayInfo(payInfoModel);
                //执行扣余额
                balanceModel = new BalanceModel();
                balanceModel.setAccountId(userId);
                balanceModel.setChangeTime(DateFormatUtil.formateString());
                balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
                int result = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
                if (!StringUtils.isEmpty(redPkgId)) {
                    Map<String, String> redParams = new HashMap<String, String>();
                    redParams.put("openId", openId);
                    redParams.put("redpkgId", redPkgId);
                    redParams.put("state", "2");
                    String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                    logger.error("修改红包返回结果:" + updateRedPkgResult);
                }
                codeMess = new CodeMess("1", payInfoModel.getOrderNo());//充值成功
            } else {
                //充值失败
                PayInfoModel pay = new PayInfoModel();
                pay.setOrderNo(payInfoModel.getOrderNo());
                pay.setDealState(new Short(status));
                this.payInfoServiceI.queryPayInfos(pay);
                codeMess = new CodeMess("3", "充值失败");//充值中
            }
            int result = this.payInfoServiceI.updatePayInfo(payInfoModel);
        }
        if (!StringUtils.isEmpty(redirectUrl)) {
            Map<String, String> redParams = new HashMap<String, String>();
            redParams.put("code", codeMess.getCode());
            redParams.put("mess", codeMess.getMess());
            HttpUtil.sendPost(redirectUrl, "utf-8", redParams);
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
        String userId = getUserId(session);
        String openId = (String) session.getAttribute("openId");
        logger.error("OpenId="+openId+",userId="+userId);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            logger.error("未获取到用户信息 从request重新获取");
            userId = request.getParameter("userId").trim();
            openId = request.getParameter("openId").trim();
            if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
        } else {
            session.setAttribute("userId", userId);
            session.setAttribute("openId", openId);
        }
        String goodsName = request.getParameter("rechargeName").trim();//充值名称(水电煤)
        String dealMoney = request.getParameter("payamount").trim();     //充值金额
        String dealRealMoney = request.getParameter("realmount").trim();//真实付款金额
        String rechargeType = request.getParameter("rechargeType").trim();//充值类型（油卡话费等）
        String redPkgId = request.getParameter("redPkgId").trim();//红包ID 支付成功后更新红包状态
        String lifeType = request.getParameter("lifeType").trim();//生活缴费类型
        String payInfo=request.getParameter("payInfo");//md5
        String payRedirectUrl = request.getParameter("redirectUrl").trim();
        if ((Double.parseDouble(dealMoney)) < 0D) {
            codeMess = new CodeMess("3", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        if ((Double.parseDouble(dealRealMoney)) < 0D) {
            codeMess = new CodeMess("3", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setAccountId(userId);
        payInfoModel.setOrderNo(UniqueX.randomUnique());//订单ID  用商户传
        payInfoModel.setDealInfo(goodsName + "(微信支付)");
        payInfoModel.setDealId(UUIDUtil.getUUID());
        payInfoModel.setDealState(new Short("2"));//'1.成功 2.未成功 （提现状态）3. 提现中 4. 已提现 5.提现失败',
        payInfoModel.setDealType(new Short("2"));//1.提现 2.充值 3.支付 4.退款',
        payInfoModel.setDealTime(DateFormatUtil.formateString());
        payInfoModel.setDealMoney(Double.parseDouble(dealMoney));
        payInfoModel.setDealRealMoney(Double.parseDouble(dealRealMoney));
        payInfoModel.setBusinessInfo("有礼付缴费充值");
        JSONObject object = new JSONObject();
        if (rechargeType.equals(LIFE)) {
            logger.error("进入生活缴费充值");
            //生活缴费
            Map<String, String> params = (Map<String, String>) session.getAttribute("lifeRechargeParams");
            String timestamp = params.get("timestamp");
            String md5str = MD5Util.MD5Encode(dealMoney + rechargeType + timestamp).toUpperCase();
            String payMd5 = (String) session.getAttribute("payMd5");
            if (!payMd5.equals(md5str)) {
                codeMess = new CodeMess("30000", "业务失败");
                return JSON.toJSONString(codeMess);
            }
            if (params == null) {
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            params.put("actPrice", String.valueOf(payInfoModel.getDealRealMoney()));
            params.put("ret_url", RECHARGE_REDIRECTURL + "?retUrl=" + payRedirectUrl);
            object.put("interfaceParams", JSONObject.toJSONString(params));
            payInfoModel.setDealInfo("有礼付充值-"+goodsName+"(微信支付)");
        } else if (rechargeType.equals(ZSHGAME)) {
            logger.error("进入中石化游戏中心充值");
            payInfoModel.setDealInfo("有礼付充值-中石化游戏充值(微信支付)");
            payInfoModel.setBusinessInfo("中石化游戏中心充值");
        } else if (rechargeType.equals(HUAFEI)) {

//*************************************话费***********************************
            logger.error("进入话费充值");
            Map<String, String> params = (Map<String, String>) session.getAttribute("telParams");
            logger.error("话费接口参数获取："+session.getAttribute("telParams"));
            if (params == null) {
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            String companyName=(String) session.getAttribute("companyName");
            if(StringUtils.isEmpty(companyName)){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }else{
                session.setAttribute("companyName",companyName);
            }
            String md5Info=MD5Util.MD5Encode(companyName+params.get("sporder_id")+payInfoModel.getDealRealMoney()+IPUtil.getIpAddr(request)).toUpperCase();
            logger.error("获取支付页参数MD5校验:"+md5Info+",预支付MD5:"+payInfo);
            BlackUserModel blackUserModel=new BlackUserModel();
            if(!md5Info.equals(payInfo)){
                codeMess = new CodeMess("30000", "数据异常");
                //数据异常 加黑名单
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(companyName+"话费充值"+payInfoModel.getDealRealMoney()+"元");
                blackUserModel.setRechargeNum(params.get("game_userid"));
                blackUserModel.setRechargePrice(payInfoModel.getDealRealMoney().toString());
                return JSON.toJSONString(codeMess);
            }
            OrderInfoModel orderInfoModel=new OrderInfoModel();
            orderInfoModel.setMd5Sign(md5Info);
            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
            if(orderInfoModels.size()==0){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            orderInfoModel=orderInfoModels.get(0);
            payInfoModel.setDealInfo("有礼付充值-话费(微信支付)");
            payInfoModel.setDealRealMoney(Double.parseDouble(orderInfoModel.getPayMoney()));
            payInfoModel.setDealMoney(Double.parseDouble(orderInfoModel.getGoodsMoney()));
            payInfoModel.setDealRemark(params.get("game_userid"));
            payInfoModel.setOrderNo(params.get("sporder_id")); //校验成功，更新订单号
            logger.error("订单参数:" + JSONObject.toJSONString(params));
            object.put("interfaceParams", JSONObject.toJSONString(params));
            session.setAttribute("lifeRechargeParams", params);
        } else if (rechargeType.equals(LIULIANG)) {
//*************************************流量***********************************
            logger.error("----进入流量充值---WX支付---");
            //流量 【传递参数，微信支付回调中获取参数，并进行相应的业务逻辑操作】
            //获取当前data2Map的参数 [判断当前参数对象是否为空,当前data2Map为订单发起的时候，组合当前订单数据到data2Map中]
            Map<String, String> params = (Map<String, String>) session.getAttribute("data2Map");
            BlackUserModel blackUserModel=new BlackUserModel();

            if (params == null) {
                logger.error("data2Map为空!,重新登陆");
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            //获取当前从PayInfoModel中的参数，做一次MD5加密校验。和数据库预支付订单中的MD5进行匹配。
            // 【md5Info加密参数 现在有2个参数无法确定，numberDesc 还有payInfoModel.getOrderNo】
            String md5Info=MD5Util.MD5Encode(params.get("packCode")+params.get("mobile")+params.get("number_desc")+params.get("sporder_id")+payInfoModel.getDealRealMoney()+IPUtil.getIpAddr(request)).toUpperCase();
            logger.error("获取支付页参数MD5校验:"+md5Info+",预支付MD5:"+payInfo);
            if(!md5Info.equals(payInfo)){
                //数据异常 加黑名单
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(params.get("number_desc")+"流量充值"+params.get("rechargeM")+"M--"+payInfoModel.getDealRealMoney()+"元");
                blackUserModel.setRechargeNum(params.get("mobile"));
                blackUserModel.setRechargePrice(params.get("realMoney"));
                //判断当前用户是否存在黑名单中，不存在则添加当前用户进入黑名单 [addBlackUser方法在下]
                addBlackUser(blackUserModel);

                logger.error("当前md5校验失败！订单异常");
                codeMess = new CodeMess("30000", "数据异常");
                return JSON.toJSONString(codeMess);
            }

           //重新实例OrderInfoModel对象，将获取的MD5数据，赋值对象中
            OrderInfoModel orderInfoModel=new OrderInfoModel();
            orderInfoModel.setMd5Sign(md5Info);

            //匹配当前和预订单中的md5校验 [如果匹配不成功，退出重新登陆]
            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
            if(orderInfoModels.size()==0){
                logger.error("查询失败，没有当前预订单数据存在！");
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }

            //获取当前预定订单的OrderInfoModel
            orderInfoModel=orderInfoModels.get(0);

            //订单传入金额与session中金额进行匹配
            String realMoney_getGoodsInfo = params.get("realMoney");
            String payMoney = String.valueOf(payInfoModel.getDealRealMoney());
            if(!realMoney_getGoodsInfo.equals(payMoney)){

                //数据异常 加黑名单
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(params.get("number_desc")+"流量充值"+params.get("rechargeM")+"M--"+payInfoModel.getDealRealMoney()+"元");
                blackUserModel.setRechargeNum(params.get("mobile"));
                blackUserModel.setRechargePrice(params.get("realMoney"));
                //判断当前用户是否存在黑名单中，不存在则添加当前用户进入黑名单 [addBlackUser方法在下]
                addBlackUser(blackUserModel);


                logger.error("--当前传入金额["+realMoney_getGoodsInfo+"]和数据库goodsInfo["+payMoney+"]中取出的金额不匹配！订单异常--");
                codeMess = new CodeMess("30000", "订单异常");
                return JSON.toJSONString(codeMess);
            }


            //session中的金额与数据库预订单表的金额进行匹配
            String realMoney_orderInfo  = orderInfoModel.getPayMoney();
            if(!realMoney_orderInfo.equals(params.get("realMoney"))){

                //数据异常 加黑名单
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(params.get("number_desc")+"流量充值"+params.get("rechargeM")+"M--"+payInfoModel.getDealRealMoney()+"元");
                blackUserModel.setRechargeNum(params.get("mobile"));
                blackUserModel.setRechargePrice(params.get("realMoney"));
                //判断当前用户是否存在黑名单中，不存在则添加当前用户进入黑名单 [addBlackUser方法在下]
                addBlackUser(blackUserModel);

                logger.error("--当前session中获得realMoney["+realMoney_getGoodsInfo+"]金额与预订单表取出OrderInfo.realMoney["+realMoney_orderInfo+"]不匹配!--");
                codeMess = new CodeMess("30000", "订单异常");
                return JSON.toJSONString(codeMess);
            }


            //payInfoModel 重新赋值payInfoModel微信订单
            payInfoModel.setDealRealMoney(Double.parseDouble(orderInfoModel.getPayMoney())); //真实支付金额
            payInfoModel.setDealMoney(Double.parseDouble(orderInfoModel.getGoodsMoney()));   //商品金额
            payInfoModel.setDealRemark(params.get("mobile"));    //手机号码 备注
            payInfoModel.setOrderNo(params.get("sporder_id")); //校验成功，更新订单号
            payInfoModel.setDealInfo("有礼付充值-流量(微信支付)");
            logger.error("订单参数:" + JSONObject.toJSONString(params));

            //发送订单数据信息 【FlowController 提交订单，给手机号码充值流量】
            object.put("interfaceParams", JSONObject.toJSONString(params));

        } else if (rechargeType.equals(YOUKA)) {
//*************************************油卡***********************************
            logger.error("进入油卡充值");
            Map<String, String> params = (Map<String, String>) session.getAttribute("fuelRechargeParams");
            if (params == null) {
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            String chargeName=params.get("chargeType");
            if(StringUtils.isEmpty(chargeName)){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }else{
                if(chargeName.equals("1")){
                    chargeName="中石化";
                }else{
                    chargeName="中石油";
                }
            }
            String md5Info=MD5Util.MD5Encode(chargeName+params.get("sporder_id")+payInfoModel.getDealRealMoney()+IPUtil.getIpAddr(request)).toUpperCase();
            logger.error("获取支付页参数MD5校验:"+md5Info+",预支付MD5:"+payInfo);
            if(!md5Info.equals(payInfo)){
                codeMess = new CodeMess("30000", "数据异常");
                //数据异常 加黑名单
                return JSON.toJSONString(codeMess);
            }
            OrderInfoModel orderInfoModel=new OrderInfoModel();
            orderInfoModel.setMd5Sign(md5Info);
            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
            if(orderInfoModels.size()==0){
                codeMess = new CodeMess("30000", "重新登录");
                return JSON.toJSONString(codeMess);
            }
            orderInfoModel=orderInfoModels.get(0);

            payInfoModel.setDealInfo("有礼付充值-油卡(微信支付)");
            payInfoModel.setDealRemark(params.get("game_userid"));
            payInfoModel.setDealRealMoney(Double.parseDouble(orderInfoModel.getPayMoney()));
            payInfoModel.setDealMoney(Double.parseDouble(orderInfoModel.getGoodsMoney()));
            payInfoModel.setOrderNo(params.get("sporder_id")); //校验成功，更新订单号
            params.put("actPrice", String.valueOf(payInfoModel.getDealRealMoney()));
            session.setAttribute("fuelRechargeParams", params);
            object.put("interfaceParams", JSONObject.toJSONString(params));
            logger.info("======油卡============params: " + params);
        }
        //微信统一下单接口
        Map<String, String> payParams = new HashMap<String, String>();
        payParams.put("redPkgId", redPkgId);
        payParams.put("rechargeType", rechargeType);
        payParams.put("userId", userId);
        payParams.put("openId", openId);
        payParams.put("lifeType", lifeType);
        payParams.put("payRedirectUrl", payRedirectUrl);
        object.put("payParams", JSONObject.toJSONString(payParams));
        payInfoModel.setDealRemark(object.toJSONString());
        int result = payInfoServiceI.insertPayInfo(payInfoModel);
        if (result == 0) {
            codeMess = new CodeMess("20000", "业务失败");
            return JSON.toJSONString(codeMess);
        }
        DecimalFormat df = new DecimalFormat("######0");
        String responseXml = WXPayUtil.preOrder(new WCPreOrderRequest(appId, mchID, key, openId, String.valueOf(df.format(payInfoModel.getDealRealMoney() * 100)),
                IPUtil.getIpAddr(request), REDIRECTURL, payInfoModel.getOrderNo(), payInfoModel.getDealInfo()));
        logger.info("responseXml=" + responseXml);
        XStream stream = new XStream();
        stream.alias("xml", WCPreOrderResponse.class);
        WCPreOrderResponse wcPreOrderResponse = (WCPreOrderResponse) stream.fromXML(responseXml);
        logger.info("");
        if (wcPreOrderResponse.getReturn_code().equals("SUCCESS")) {
            if (wcPreOrderResponse.getResult_code().equals("SUCCESS")) {
                Map<String, String> map = WXPayUtil.loadJavaScriptPayConfig(appId, wcPreOrderResponse.getPrepay_id(),
                        key, payInfoModel.getOrderNo(), REDIRECTURL, String.valueOf(df.format(payInfoModel.getDealRealMoney() * 100)));
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
                String wxMoney=jsonObject.getString("total_fee");
                PayInfoModel payInfoModel = new PayInfoModel();
                payInfoModel.setOrderNo(jsonObject.getString("out_trade_no"));
                logger.error("订单信息:" + payInfoModel);
                List<PayInfoModel> list = payInfoServiceI.queryPayInfos(payInfoModel);
                logger.info("支付信息对象:" + list);
                DecimalFormat df = new DecimalFormat("######0");
                //如果支付状态等于1，相当于已记录
                CodeMess codeMess = null;
                if (list.size() != 0 && list.get(0).getWxOrder() == null) {
                    logger.info("查询订单信息:" + list.get(0));
                    if (2 == list.get(0).getDealState()) {
                        //更新微信支付记录表
                        payInfoModel.setDealState(new Short("1"));
                        payInfoModel.setWxOrder(jsonObject.getString("transaction_id"));//订单ID
                        JSONObject object = JSONObject.parseObject(list.get(0).getDealRemark());
                        String para = object.getString("payParams");
                        JSONObject payParams = JSONObject.parseObject(para);
                        if (payParams == null) {
                            logger.error("获取payParams失败");
                            codeMess = new CodeMess("3", "业务失败");
                            return JSON.toJSONString(codeMess);
                        }
                        int result = payInfoServiceI.updatePayInfo(payInfoModel);
                        logger.info("获取用户id：" + list.get(0).getAccountId());
                        logger.error("payParams=========" + payParams);
                        String userId = payParams.getString("userId");
                        String lifeType = payParams.getString("lifeType");
                        String rechargeType = payParams.getString("rechargeType");
                        String openId = payParams.getString("openId");
                        String redPkgId = payParams.getString("redPkgId");
                        String payRedirectUrl = payParams.getString("payRedirectUrl");

                        logger.error("userId====" + userId + "lifeType====" + lifeType + "rechargeType====" + rechargeType + ",redPkgId===========" + redPkgId);
                        PayInfoModel payInfo = new PayInfoModel();
                        payInfo.setAccountId(userId);
                        payInfo.setDealId(UUIDUtil.getUUID());
                        payInfo.setDealState(new Short("2"));//'1.成功 2.未成功 （提现状态）3. 提现中 4. 已提现 5.提现失败',
                        payInfo.setDealType(new Short("3"));//1.提现 2.充值 3.支付 4.退款',
                        payInfo.setDealTime(DateFormatUtil.formateString());
                        payInfo.setBusinessInfo("有礼付缴费充值");
                        payInfo.setDealMoney(list.get(0).getDealMoney());
                        payInfo.setDealRealMoney(list.get(0).getDealRealMoney());
                        payInfo.setOrderNo(UUIDUtil.getUUID());
                        payInfo.setDealInfo(getRechargeNameByType(rechargeType, lifeType) + "(微信支付)");
                        logger.error("充值对象信息:" + JSONObject.toJSONString(payInfo));
                        logger.error("充值类型:" + rechargeType);
                        if (rechargeType.equals(ZSHGAME)) {
                            //中石化游戏中心
                            logger.error("类型为中石化游戏充值");
                            codeMess = new CodeMess("1", payInfoModel.getOrderNo());
                            updateUserRedPkg(redPkgId,openId);
                        } else if (rechargeType.equals(YOUKA)) {
                            //油卡
                            logger.error("进入油卡缴费充值接口调用===");
                            String inter = object.getString("interfaceParams");
                            JSONObject params = JSONObject.parseObject(inter);
                            OrderInfoModel orderInfoModel=new OrderInfoModel();
                            orderInfoModel.setOrderNo(params.getString("sporder_id"));
                            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
                            logger.error("获取预支付订单:"+orderInfoModels.size());
                            if(orderInfoModels.size()==0){
                                codeMess = new CodeMess("3", "充值失败");//
                                return JSONObject.toJSONString(codeMess);
                            }
                            orderInfoModel=orderInfoModels.get(0);
                            logger.error("预支付订单对象信息:"+JSONObject.toJSONString(orderInfoModel));
                            String payMoney=String.valueOf(df.format(Double.parseDouble(orderInfoModel.getPayMoney())* 100));
                            logger.error("微信返回金额："+wxMoney+",预支付订单金额:"+payMoney);
                            if(!wxMoney.equals(payMoney)){
                                codeMess = new CodeMess("3", "充值失败");//
                                return JSONObject.toJSONString(codeMess);
                            }
                            logger.error("开始推送油卡充值提醒");
                            String[] whitelist=whiteOpenId.split(",");
                            for(String str:whitelist){
                                TemplateMessageUtil.sendPaySuccessByOilRecharge(orderInfoModel.getOrderNo(),orderInfoModel.getGoodsMoney(),
                                        orderInfoModel.getCreateTime(),params.getString("sporder_id"),str,orderInfoModel.getPayMoney(),reviewUrl.replace("NUM",orderInfoModel.getOrderNo()),0);
                            }
                            /*int addresult = this.payInfoServiceI.insertPayInfo(payInfo);
                            logger.error("添加订单：" + addresult);
                            String sendReult = HttpUtil.sendPost(this.fuelCardRechargeUrl, "gb2312", params);
                            logger.error("发送欧飞调用支付接口返回信息:" + sendReult);
                            Document doc = null;
                            doc = DocumentHelper.parseText(sendReult);
                            Element rootElt = doc.getRootElement();
                            String retcode = rootElt.elementTextTrim("retcode");
                            if (!"1".equals(retcode)) {
                                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                                return JSONObject.toJSONString(codeMess);
                            }
                            String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
                            String cardname = rootElt.elementTextTrim("cardname");//充值名称
                            String sporderId = rootElt.elementTextTrim("sporderId");//订单编号
                            String status = rootElt.elementTextTrim("game_state"); //如果成功将为1，澈消(充值失败)为9，充值中为0,只能当状态为9时，商户才可以退款给用户。
                            payInfo.setDealRemark(rootElt.elementTextTrim("game_userid"));
                            payInfo.setWxOrder(orderid);
                            if (status.equals("1")) {
                                //充值成功
                                payInfo.setDealState(new Short("1"));
                                //如果使用了红包就更新红包状态
                                updateUserRedPkg(redPkgId,openId);
                            } else if (status.equals("0")) {
                                //充值中
                                payInfo.setDealState(new Short("0"));
                                codeMess = new CodeMess("2", payInfo.getOrderNo());//充值中
                            } else {
                                //充值失败
                                payInfo.setDealState(new Short(status));
                                codeMess = new CodeMess("3", "充值失败");//充值中
                            }
                            this.payInfoServiceI.updatePayInfo(payInfo);*/

                        } else if (rechargeType.equals(HUAFEI)) {
//                            **************************话费***********************************
                            String inter = object.getString("interfaceParams");
                            JSONObject params = JSONObject.parseObject(inter);
                            OrderInfoModel orderInfoModel=new OrderInfoModel();
                            orderInfoModel.setOrderNo(params.getString("sporder_id"));
                            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
                            logger.error("获取预支付订单:"+orderInfoModels.size());
                            if(orderInfoModels.size()==0){
                                codeMess = new CodeMess("3", "充值失败");//
                                return JSONObject.toJSONString(codeMess);
                            }
                            orderInfoModel=orderInfoModels.get(0);
                            logger.error("预支付订单对象信息:"+JSONObject.toJSONString(orderInfoModel));
                            String payMoney=String.valueOf(df.format(Double.parseDouble(orderInfoModel.getPayMoney())* 100));
                            logger.error("微信返回金额："+wxMoney+",预支付订单金额:"+payMoney);
                            if(!wxMoney.equals(payMoney)){
                                codeMess = new CodeMess("3", "充值失败");//
                                return JSONObject.toJSONString(codeMess);
                            }
                            logger.error("开始推送手机充值提醒");

                            String[] whitelist=whiteOpenId.split(",");
                            PhoneModel phoneModel = phoneQueryService.getPhone(params.getString("game_userid"));
                            for(String str:whitelist){
                                TemplateMessageUtil.sendPaySuccessByPhoneRecharge(orderInfoModel.getOrderNo(),orderInfoModel.getGoodsMoney(),
                                        orderInfoModel.getCreateTime(),params.getString("game_userid"),phoneModel,str,orderInfoModel.getPayMoney(),reviewUrl.replace("NUM",params.getString("sporder_id")));
                            }
                            /* payInfoModel.setOrderNo(params.getString("sporder_id"));
                            payInfoModel.setDealRemark(params.getString("game_userid"));
                            payInfoModel.setBusinessInfo("有礼付话费充值");
                            payInfoModel.setDealState((short) 2);
                            int addresult = this.payInfoServiceI.insertPayInfo(payInfo);
                            logger.error("添加订单订单:" + addresult + "=====订单参数：" + JSONObject.toJSONString(payInfo));
                            logger.error("欧飞接口参数信息:" + params);
                            String sendReult = HttpUtil.sendPost(this.oufeiTelUrl, "gb2312", params);
                            logger.error("发送欧飞调用支付接口返回信息:" + sendReult);
                            Document doc = null;
                            doc = DocumentHelper.parseText(sendReult);
                            Element rootElt = doc.getRootElement();
                            String retcode = rootElt.elementTextTrim("retcode");
                            if ("1".equals(retcode)) {
                                logger.info("订单支付成功");
                                String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
                                String cardname = rootElt.elementTextTrim("cardname");//充值名称
                                String sporderId = rootElt.elementTextTrim("sporder_id");//订单编号
                                String state = rootElt.elementTextTrim("game_state"); //如果成功将为1，澈消(充值失败)为9，充值中为0,只能当状态为9时，商户才可以退款给用户。
                                payInfo.setWxOrder(orderid);
                                payInfo.setDealInfo(cardname);
                                payInfo.setOrderNo(sporderId);
                                payInfo.setDealRemark(params.getString("game_userid"));
                                if ("1".equals(state)) {
                                    logger.info("话费充值成功");
                                    payInfo.setDealState((short) 1);
                                    int num = this.payInfoServiceI.updatePayInfo(payInfo);
                                    if (num == 0) {
                                        codeMess = new CodeMess("3", "业务失败");
                                        return JSON.toJSONString(codeMess);
                                    }

                                    if (!StringUtils.isEmpty(redPkgId)) {
                                        //如果使用了红包则更新红包状态
                                        Map<String, String> redParams = new HashMap<String, String>();
                                        redParams.put("openId", openId);
                                        redParams.put("redpkgId", redPkgId);
                                        redParams.put("state", "2");
                                        String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                                        logger.error("修改红包返回结果:" + updateRedPkgResult);
                                    }
                                    codeMess = new CodeMess("1", payInfoModel.getOrderNo());//充值成功

                                } else if ("0".equals(state)) {
                                    //充值中
                                    payInfo.setDealState(new Short("0"));
                                    codeMess = new CodeMess("2", payInfoModel.getOrderNo());//充值中

                                } else if ("9".equals(state)) {
                                    //充值失败
                                    payInfoModel.setDealState(new Short(state));
                                    codeMess = new CodeMess("3", "充值失败");//充值失败
                                }

                                //查询是否有相同的记录
                                UserNumRecord record = new UserNumRecord();
                                boolean flag = userNumRecordServiceI.isNumExist(userId, params.getString("game_userid"), 2);
                                if (!flag) {
                                    //没有相同记录就插入
                                    //将详单信息插入对应表
                                    record.setId(UUIDUtil.getUUID());
                                    record.setUserId(userId);
                                    record.setNumber(params.getString("game_userid"));
                                    record.setNumber_desc(cardname.subSequence(0, 4) + "");
                                    record.setNumberType(2);
                                    record.setCreateTime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                                    userNumRecordServiceI.addUserNumRecord(record);

                                } else {
                                    //有相同记录则更新时间
                                    userNumRecordServiceI.updateCreateTime(userId, params.getString("game_userid"), new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                                }
                            } else {//接口调用失败
                                logger.info("订单支付失败：" + rootElt.elementTextTrim("err_msg"));
                                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                                return JSONObject.toJSONString(codeMess);
                            }
                            //更新订单信息
                            payInfo.setBusinessInfo("有礼付话费充值");
                            payInfo.setDealRemark(params.getString("game_userid"));
                            this.payInfoServiceI.updatePayInfo(payInfo);*/
                        } else if (rechargeType.equals(LIULIANG)) {

                            //***********************流量充值-微信*************************

                            //获取当前流量充值需要的参数
                            String inter = object.getString("interfaceParams");
                            //将当前参数转换为jsonObject类型
                            JSONObject params = JSONObject.parseObject(inter);

                            OrderInfoModel orderInfoModel=new OrderInfoModel();
                            orderInfoModel.setOrderNo(params.getString("sporder_id"));
                            List<OrderInfoModel> orderInfoModels=this.orderService.queryAllOrderInfo(orderInfoModel);
                            logger.error("获取预支付订单:"+orderInfoModels.size());
                            if(orderInfoModels.size()==0){
                                codeMess = new CodeMess("3", "充值失败");//
                                return JSONObject.toJSONString(codeMess);
                            }
                            orderInfoModel=orderInfoModels.get(0);
                            logger.error("预支付订单对象信息:"+JSONObject.toJSONString(orderInfoModel));

                            //金额匹配，预支付订单金额 和 微信回调金额数据
                            String payMoney=String.valueOf(df.format(Double.parseDouble(orderInfoModel.getPayMoney())* 100));
                            logger.error("微信返回金额："+wxMoney+",预支付订单金额:"+payMoney);
                            if(!wxMoney.equals(payMoney)){
                                codeMess = new CodeMess("3", "充值失败");
                                return JSONObject.toJSONString(codeMess);
                            }

                            logger.error("----------开始推送手机流量充值提醒---------");
                            String[] whitelist=whiteOpenId.split(",");
                            PhoneModel phoneModel = phoneQueryService.getPhone(params.getString("mobile"));
                            for(String str:whitelist){
                                TemplateMessageUtil.sendPaySuccessByPhoneFlowRecharge(
                                        orderInfoModel.getOrderNo(),     //预定订单编号
                                        orderInfoModel.getGoodsMoney(),  //预定订单商品金额
                                        orderInfoModel.getCreateTime(),  //预定订单创建时间 OrderCreateTime
                                        params.getString("mobile"),
                                        phoneModel,
                                        str,    //OpenId
                                        orderInfoModel.getPayMoney(), //预支付订单付款金额
                                        reviewUrl.replace("NUM",params.getString("sporder_id"))
                                );
                            }



//                            String inter = object.getString("interfaceParams");
//                            JSONObject data2Map = JSONObject.parseObject(inter);
//                            logger.error("------从data2Map中获取相应的数据，进行业务逻辑操作------");
//                            String mobile = String.valueOf(data2Map.getString("mobile")); //充值手机号码
//                            String number_desc = String.valueOf(data2Map.getString("number_desc")); //运营商
//                            String rechargeAmount = String.valueOf(data2Map.getString("rechargeAmount")); //充值套餐
//                            String packCode = String.valueOf(data2Map.getString("packCode")); //充值套餐 代码
//
//                            //继承微信支付订单中的相应数据，并对个别参数进行重新赋值操作
//                            PayInfoModel FlowPayInfo = payInfo;
//
//                            FlowPayInfo.setBusinessInfo("有礼付流量充值");
//                            FlowPayInfo.setDealRemark(mobile);
//                            FlowPayInfo.setDealInfo(number_desc + "流量充值" + rechargeAmount);
//                            FlowPayInfo.setDealState((short) 0); //充值中
//
//                            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(FlowPayInfo);
//                            logger.error("------------导出当前payInfo中的参数：" + json.toString() + "----------------");
//
//                            //插入支付信息 [充值成功，更新充值状态]
//                            logger.error("----------RechargeController----------初始化插入充值订单信息[ 网迅流量充值 ]----------------- ]");
//                            int count = this.payInfoServiceI.insertPayInfo(FlowPayInfo);
//                            logger.error("---------RechargeController-----------发送流量订单请求[网迅流量接口]-----------------");
//                            FlowOrderController flowOrderController = new FlowOrderController();
//                            String Result = flowOrderController.sendOrder(packCode, mobile, FlowPayInfo.getOrderNo());
//                            logger.error("--------充值方法返回参数:[" + Result + "]----------------");
//
//                            if ("RechargeSuccess".equals(Result)) {
//                                logger.error("---------充值成功！ 返回参数:[" + Result + "]-----------------");
//                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                String time = sdf2.format(new Date());
//                                UUID uuid = UUID.randomUUID();
//                                logger.error("--------------- 充值成功，查询当前用户是否存在该号码历史充值记录，如果不存在则添加该记录到t_RechargeRecord表 ！-------------------------");
//                                boolean isExits = userNumRecordServiceI.isNumExist(userId, mobile, 3);
//                                logger.error("--------------- 当前查询结果:[" + isExits + "]！-------------------------");
//                                if (isExits == false) {
//                                    logger.error("--------------- 当前userId用户  Add 历史充值流量记录 ！-------------------------");
//                                    //新增数据
//                                    userNumRecordServiceI.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
//                                } else {
//                                    logger.error("--------------- 当前userId用户 更新充值createTime ！-------------------------");
//                                    //更新数据
//                                    userNumRecordServiceI.updateCreateTime(userId, mobile, time);
//                                }
//                                logger.error("----------RechargeController-------判断红包是否存在[ " + redPkgId + " ]-----------");
//                                updateUserRedPkg(redPkgId,openId);
//                            } else {
//                                logger.error("----------流量订单充值失败！ 返回异常参数:[" + Result + "]--------更新订单充值statu:[3]-----------------");
//                                logger.error("----------充值失败 返回金额到用户账户余额----------");
//                                codeMess = new CodeMess("3", "充值失败");//充值中
//                            }
//
//                            logger.error("----------------流量充值流程---已完毕,准备退出当前流程！--------------------");

                        } else if (rechargeType.equals(LIFE)) {
                            logger.error("进入生活缴费充值接口调用===");
                            session = request.getSession();
                            String inter = object.getString("interfaceParams");
                            JSONObject params = JSONObject.parseObject(inter);
                            logger.error("订单参数:" + JSONObject.toJSONString(params));
                            payInfo.setOrderNo(params.getString("sporderId"));
                            logger.error("接口订单参数:" + JSONObject.toJSONString(payInfo));
                            String account = payParams.getString("account");
                            payInfo.setDealRemark(account);
                            int addresult = this.payInfoServiceI.insertPayInfo(payInfo);
                            logger.error("添加订单：" + addresult);
                            String sendReult = HttpUtil.sendPost(this.oufeiUtilityOrderUrl, "gb2312", params);
                            logger.error("发送欧飞调用支付接口返回信息:" + sendReult);
                            Document doc = null;
                            doc = DocumentHelper.parseText(sendReult);
                            Element rootElt = doc.getRootElement();
                            String retcode = rootElt.elementTextTrim("retcode");
                            if (!retcode.equals("1")) {
                                codeMess = new CodeMess("0", rootElt.elementTextTrim("err_msg"));
                                return JSONObject.toJSONString(codeMess);
                            }
                            String orderid = rootElt.elementTextTrim("orderid");//欧飞订单号
                            String cardname = rootElt.elementTextTrim("cardname");//充值名称
                            String sporderId = rootElt.elementTextTrim("sporderId");//订单编号
                            String status = rootElt.elementTextTrim("status"); //如果成功将为1，澈消(充值失败)为9，充值中为0,只能当状态为9时，商户才可以退款给用户。
                            if (status.equals("1")) {
                                //充值成功
                                payInfoModel.setDealState(new Short("1"));
                                payInfoModel.setWxOrder(orderid);
                                //执行扣余额
                                updateUserRedPkg(redPkgId,openId);
                                codeMess = new CodeMess("1", payInfoModel.getOrderNo());//充值成功
                            } else if (status.equals("0")) {
                                //充值中
                                payInfoModel.setDealState(new Short("0"));
                                codeMess = new CodeMess("2", payInfoModel.getOrderNo());//充值中
                            } else {
                                logger.error("充值失败 返回金额到用户账户");
                                codeMess = new CodeMess("3", "充值失败");//充值中
                            }
                            int updatePayInfoResult = this.payInfoServiceI.updatePayInfo(payInfoModel);
                        }
                        logger.error("中石化回调参数：" + payRedirectUrl);
                        if (!StringUtils.isEmpty(payRedirectUrl)) {
                            Map<String, String> redParams = new HashMap<String, String>();
                            redParams.put("code", codeMess.getCode());
                            redParams.put("mess", codeMess.getMess());
                            HttpUtil.sendPost(payRedirectUrl, "utf-8", redParams);
                        }
                        session.setAttribute("orderPayInfo", payInfoModel);
                        return JSON.toJSONString(codeMess);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 生活缴费充值回调链接
     */
    @RequestMapping(value = "/getPayOrderInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getPayOrderInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String orderNo = request.getParameter("orderNo");
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setOrderNo(orderNo);
        List<PayInfoModel> list = this.payInfoServiceI.queryPayInfos(payInfoModel);
        if (list.size() == 0) {
            logger.error("生活缴费数据异常");
            CodeMess codemess = new CodeMess("0", "数据异常");
            return JSONObject.toJSONString(codemess);
        } else {
            logger.error("生活缴费回调成功");
            CodeMess codemess = new CodeMess("1", JSONObject.toJSONString(list.get(0)));
            return JSONObject.toJSONString(codemess);
        }
    }


    /**
     * 生活缴费充值回调链接
     */
    @RequestMapping(value = "/updateAccountPayOrderStatus", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void updateAccountPayOrderStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String ret_code = request.getParameter("ret_code");
        String sporder_id = request.getParameter("sporder_id");
        String ordersuccesstime = request.getParameter("ordersuccesstime");
        String err_msg = request.getParameter("err_msg ");
        logger.error("ret_code:" + ret_code);
        logger.error("sporder_id:" + sporder_id);
        logger.error("ordersuccesstime:" + ordersuccesstime);
        logger.error("err_msg:" + err_msg);
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setOrderNo(sporder_id);
        if (ret_code.equals("1")) {
            List<PayInfoModel> list = this.payInfoServiceI.queryPayInfos(payInfoModel);
            //充值成功
            payInfoModel.setDealState(new Short(ret_code));
            payInfoModel.setWxOrder(UUIDUtil.getUUID());
            this.payInfoServiceI.updatePayInfo(payInfoModel);

        } else if (ret_code.equals("9")) {
            //充值失败
            payInfoModel.setDealState(new Short(ret_code));
            BalanceModel balanceModel = new BalanceModel();
            balanceModel.setAccountId(payInfoModel.getAccountId());
            List<BalanceModel> balanceModels = this.balanceServiceI.queryBalances(balanceModel);
            if (balanceModels.size() == 0) {
                logger.error("获取用户余额错误");
            } else {
                balanceModel = balanceModels.get(0);
                balanceModel.setChangeTime(DateFormatUtil.formateString());
                balanceModel.setBalanceNumber(balanceModel.getBalanceNumber() + payInfoModel.getDealRealMoney());
                this.balanceServiceI.updateBalance(balanceModel);
            }
            this.payInfoServiceI.updatePayInfo(payInfoModel);
        }
    }




    /**
     * 测试消息推送
     */
    @RequestMapping(value = "/testSendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void testSendMsg(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String rechargeUrl="http://wwww.baidu.com";
//        TemplateMessageUtil.sendPaySuccessByPhoneRecharge("123456789","100",
//              DateFormatUtil.formateString(),"18652902341","o4FD4v5CJ6Rv3KrGjc40B_gB9sj8","99.8",rechargeUrl);
//        TemplateMessageUtil.sendPaySuccessByOilRecharge("987654321","100",
//                DateFormatUtil.formateString(),"1000111111111","o4FD4v5CJ6Rv3KrGjc40B_gB9sj8","99.9",rechargeUrl);

        //流量
        /*TemplateMessageUtil.sendPaySuccessByPhoneFlowRecharge("987654321","100",
                DateFormatUtil.formateString(),"1000111111111","o4FD4v5CJ6Rv3KrGjc40B_gB9sj8","99.9",rechargeUrl);*/
    }

    /**
     * 判断是否是9折加油用户
     */
    @RequestMapping(value = "/isOilRechargeUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String isOilRechargeUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess=null;
        String userId=request.getParameter("userId");
        if(StringUtils.isEmpty(userId)){
            userId= (String) session.getAttribute("userId");
            if(StringUtils.isEmpty(userId)){
                codeMess=new CodeMess("0","用户ID为空");
                return JSONObject.toJSONString(codeMess);
            }else{
                session.setAttribute("userId", userId);
            }
        }
        OilRecordModel oilRecordModel=new OilRecordModel();
        oilRecordModel.setUserid(userId);
        List<OilRecordModel> oilList=this.oilRecordService.queryOilRecordModelList(oilRecordModel);
        if(oilList.size()==0){
            codeMess=new CodeMess("0","不存在该用户");
            return JSONObject.toJSONString(codeMess);
        }else{
            codeMess=new CodeMess("1","存在该用户");
            return JSONObject.toJSONString(codeMess);
        }
    }

    private static String getRechargeNameByType(String type, String lifeType) {
        String name = "";
        if (type.equals(YOUKA)) {
            name = "油卡充值";
        } else if (type.equals(LIULIANG)) {
            name = "流量充值";
        } else if (type.equals(HUAFEI)) {
            name = "话费充值";
        } else if (type.equals(LIFE)) {
            if (lifeType.equals(SHUI)) {
                name = "水费充值";
            } else if (lifeType.equals(DIAN)) {
                name = "电费充值";
            } else if (lifeType.equals(MEI)) {
                name = "燃气费充值";
            }
        } else if (type.equals(ZSHGAME)) {
            name = "中石化游戏充值";
        }
        return name;
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

    private void updateUserRedPkg(String redPkgId, String openId){
        if (!StringUtils.isEmpty(redPkgId)) {
            Map<String, String> redParams = new HashMap<String, String>();
            redParams.put("openId", openId);
            redParams.put("redpkgId", redPkgId);
            redParams.put("state", "2");
            String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
            logger.error("修改红包返回结果:" + updateRedPkgResult);
        }
    }

    //添加黑名单
    private void addBlackUser(BlackUserModel blackUserModel) {
        BlackUserModel blackUser=new BlackUserModel();
        blackUser.setOpenId(blackUserModel.getOpenId());
        List<BlackUserModel> blackUserModels=blackUserService.queryBlackUserList(blackUser);
        if(blackUserModels.size()==0){
            blackUserService.addBlackUserModel(blackUserModel);
        }
    }

    public static void main(String[] args) {
//        String str = "{\"payParams\":\"{\\\"lifeType\\\":\\\"002\\\",\\\"payRedirectUrl\\\":\\\"\\\",\\\"openId\\\":\\\"oUAows_aIqqIPltI8G6On4XzER2U\\\",\\\"rechargeType\\\":\\\"life\\\",\\\"userId\\\":\\\"77b5e1c004be44b88b13c13f00bf765d\\\",\\\"redPkgId\\\":\\\"\\\"}\",\"interfaceParams\":\"{\\\"payModeId\\\":\\\"\\\",\\\"sporderId\\\":\\\"f333427724ee47b9b04c2574754fe826\\\",\\\"contractNo\\\":\\\"20170424160657744549072017042482643098___0\\\",\\\"contentId\\\":\\\"0\\\",\\\"chargeCompanyCode\\\":\\\"v130431\\\",\\\"cityId\\\":\\\"v1931\\\",\\\"type\\\":\\\"002\\\",\\\"userid\\\":\\\"A08566\\\",\\\"version\\\":\\\"6.0\\\",\\\"cardnum\\\":\\\"1\\\",\\\"md5_str\\\":\\\"1F286AECFEA8B902F2342DA6AA2CEF6E\\\",\\\"cardId\\\":\\\"64376601\\\",\\\"userpws\\\":\\\"4c625b7861a92c7971cd2029c2fd3c4a\\\",\\\"provId\\\":\\\"v1918\\\",\\\"account\\\":\\\"1146501589\\\",\\\"ret_url\\\":\\\"http://tdev.juxinbox.com/giftpay_wap/giftpay/recharge/updateAccountPayOrderStatus.htm?retUrl=\\\",\\\"actPrice\\\":\\\"0.01\\\"}\"}";
//        JSONObject object = JSON.parseObject(str);
//        JSONObject ob = JSONObject.parseObject(object.getString("payParams"));
//        System.out.println(ob.getString("lifeType"));
        //  System.out.println(MD5Util.MD5Encode("A14039209257b32b595e25b955ca052a6624e3c064328001590d0f5a618047f1bc1063cebce5f86ev2056v2058001v23519000811789NJJMT").toUpperCase());
        System.out.println(("江苏移动手机快充50元").subSequence(0, 4));
    }
}

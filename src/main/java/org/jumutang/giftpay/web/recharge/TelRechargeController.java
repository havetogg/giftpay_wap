package org.jumutang.giftpay.web.recharge;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.MD5X;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.PayInfoServiceI;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.jumutang.giftpay.service.impl.BlackUserService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;


/**
 * 话费充值
 *
 * @author oywj
 * @Date 2017/04/05
 */
@Controller
@RequestMapping(value = "/giftpay/telRecharge", method = {RequestMethod.GET, RequestMethod.POST})
public class TelRechargeController extends BaseController {


    //话费充值回调
    public static String telUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/telRecharge/telRet.htm";


    @Autowired
    PayInfoServiceI payInfoServiceI;
    @Autowired
    UserNumRecordService userNumRecordServiceI;


    /**
     * 话费充值回调方法
     *
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "/telRet", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void telRet(HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) {
        String ret_code = request.getParameter("ret_code");
        String sporder_id = request.getParameter("sporder_id");
        String ordersuccesstime = request.getParameter("ordersuccesstime");
        String err_msg = request.getParameter("err_msg ");
        PayInfoModel payInfo = new PayInfoModel();
        if ("1".equals(ret_code)) {
            logger.info("====================================话费充值成功！");
            String userId = getUserId(session);
            payInfo.setAccountId(userId);
            List<PayInfoModel> payInfoList = payInfoServiceI.queryPayInfos(payInfo);
            if (payInfoList.size() == 0 || payInfoList.get(0) == null) {
                logger.info("====================================查不到该用户的充值信息");
            } else {
                if (payInfoList.get(0).getDealState() == 0 || payInfoList.get(0).getDealState() == 2) {
                    logger.info("====================================更新用户手机话费充值状态为成功！");
                    payInfo.setOrderNo(sporder_id);
                    payInfo.setDealState((short) 1);
                    payInfo.setDealTime(ordersuccesstime);
                    payInfoServiceI.updatePayInfo(payInfo);

                }
            }
        } else if ("9".equals(ret_code)) {
            logger.info("====================================ret_code为：" + ret_code + ",该订单已撤销！");
            //充值失败，将支付金额返回到有礼付余额中。
            BalanceModel balanceModel = new BalanceModel();
            balanceModel.setAccountId(payInfo.getAccountId());
            List<BalanceModel> balanceModels = this.balanceServiceI.queryBalances(balanceModel);
            balanceModel.setChangeTime(DateFormatUtil.formateString());
            balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() + payInfo.getDealRealMoney());
            int balanceResult = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
            logger.info("=======================充值失败，将支付金额返还到有礼付余额=更新余额表balanceResult：" + balanceResult);
        }


    }


    /**
     * 查询用户充值的手机号历史
     *
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "/historyNumber", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String historyNumber(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        logger.error("====================================查询用户充值的手机号历史");
        //得到用户的userId
        String userId = getUserId(session);
        List<UserNumRecord> list = userNumRecordServiceI.historyFlowOrder(userId, "2");
        return JSONArray.fromObject(list).toString();
    }


    /**
     * 删除用户充值的单个手机号记录
     *
     * @param request
     * @param response
     * @param session
     * @param number
     * @return
     */
    @RequestMapping(value = "/del", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                      @RequestParam(value = "number", required = true) String number
    ) {
        logger.error("====================================需要删除的手机号为：" + number);
        //得到用户的userId
        String userId = getUserId(session);
        boolean flag = userNumRecordServiceI.delUserNumRecord(userId, number);
        CodeMess code;
        if (flag) {
            code = new CodeMess("1", "删除成功");
        } else {
            code = new CodeMess("2", "删除失败");
        }
        return JSONObject.fromObject(code).toString();
    }

    /**
     * 删除用户充值的所有手机号记录
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/delAll", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String delAll(HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) {
        //得到用户的userId
        String userId = getUserId(session);
        boolean flag = userNumRecordServiceI.delAll(userId);
        CodeMess code;
        if (flag) {
            code = new CodeMess("1", "删除成功");
        } else {
            code = new CodeMess("2", "删除失败");
        }
        logger.error("====================================删除用户充值的所有手机号记录：" + flag);
        return JSONObject.fromObject(code).toString();
    }


    /**
     * 获得用户所有话费充值和流量充值的记录
     *
     * @return
     */
    @RequestMapping(value = "/getRecord", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getRecord(HttpSession session) {
        //得到用户的userId
        String userId = getUserId(session);
        //总数据存储
        List result = new ArrayList<>();

        PayInfoModel payInfo = new PayInfoModel();
        payInfo.setAccountId(userId);
        //获取所有的月份   格式    201704
        ArrayList<String> list = payInfoServiceI.historyYear(payInfo);
        for (String s : list) {
            //当月记录存储
            List resMonList = new ArrayList<>();
            payInfo.setDealTime(s);
            //获得当前月份的充值记录
            ArrayList<PayInfoModel> monList = payInfoServiceI.historyPayInfo(payInfo);
            resMonList.add(0, s);
            resMonList.add(1, monList);
            result.add(resMonList);
        }
        return JSONArray.fromObject(result).toString();
    }


    /**
     * 查询用户的红包记录
     *
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "/getRedPkg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getRedPkg(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        UserModel userModel = (UserModel) session.getAttribute("wxUser");
        String result = "";
        if (StringUtils.isEmpty(userModel) || StringUtils.isEmpty(userModel.getOpenId())) {
            result = "0";
            return result;
        }
        JSONArray jsonArray = new JSONArray();
        PostOrGet post = new PostOrGet("utf-8");
        result = post.sendPost(queryAllRedUrl, ESendHTTPModel._SEND_MODEL_DECODER, new NamedValue("openId", userModel.getOpenId()), new NamedValue("state", "1"));
        logger.info("调用红包接口，结果为：" + result);
            /*if(!StringUtils.isEmpty(result)){
                JSONObject json=JSONObject.fromObject(result);
			    jsonArray=json.getJSONArray("data");
		    }*/
        return result;
    }

    @RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void pay(HttpSession session, HttpServletResponse response, HttpServletRequest request,
                    @RequestParam(value = "realMoney", required = true) String realMoney,//用户付款价格
                    @RequestParam(value = "tel", required = true) String tel,              //手机号
                    @RequestParam(value = "rechargeAmount", required = true) String rechargeAmount,//商品价格
                    @RequestParam(value = "companyName", required = true) String companyName,//移动 联通  电信
                    @RequestParam(value = "ticket", required = true) String ticket,
                    @RequestParam(value = "value", required = true) String value
    ) throws ServletException, IOException {

        //获得系统当前时间，并转换为yyyyMMddHHmmss格式
        String sporder_time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        //随机得到一个订单编号
        String sporder_id = UUIDUtil.getUUID();

        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        }

        session.setAttribute("companyName", companyName);

        //计算得到md5_str检验码
        String str = telUserid + MD5X.getLowerCaseMD5For32(telUserpws) + telCardid + rechargeAmount + sporder_id + sporder_time + tel + telKeystr;
        String md5_str = MD5X.getUpperCaseMD5For32(str);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", telUserid);
        params.put("userpws", MD5X.getLowerCaseMD5For32(telUserpws));
        params.put("cardid", telCardid);
        params.put("cardnum", rechargeAmount);
        params.put("sporder_id", sporder_id);
        params.put("sporder_time", sporder_time);
        params.put("game_userid", tel);
        params.put("md5_str", md5_str);
        params.put("ret_url", telUrl);
        params.put("version", telVersion);
        params.put("timestamp", DateFormatUtil.formateString());
        session.setAttribute("telParams", params);
        String md5str = MD5Util.MD5Encode(realMoney + "huafei" + params.get("timestamp")).toUpperCase();
        session.setAttribute("payMd5", md5str);


        String targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/liftpayment/orderPay.html?payamount=" + realMoney + "&rechargeType=huafei&lifeType=&redPkgId=" + ticket + "&redPkgValue=" + value;
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }


    @RequestMapping(value = "/prePayCell", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String prePayCell(HttpSession session, HttpServletResponse response, HttpServletRequest request,
                             @RequestParam(value = "goodsId", required = true) String goodsId,//用户付款价
                             @RequestParam(value = "realMoney", required = true) String realMoney,//用户付款价格
                             @RequestParam(value = "tel", required = true) String tel,              //手机号
                             @RequestParam(value = "companyName", required = true) String companyName,//移动 联通  电信
                             @RequestParam(value = "ticket", required = true) String ticket,
                             @RequestParam(value = "value", required = true) String value
    ) throws ServletException, IOException {
        logger.error("进入预支付方法");
        JSONObject object = new JSONObject();
        String userId = getUserId(session);
        String openId = (String) session.getAttribute("openId");
        BlackUserModel blackUserModel=new BlackUserModel();
        logger.error("用户OPENID:" + openId + ",USERID:" + userId);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            object.put("success", "0");
            object.put("errMsg", "请求超时,请重新登录!");
            return object.toString();
        } else {
            session.setAttribute("userId", userId);
            session.setAttribute("openId", openId);
        }
        String dateTime = (String) session.getAttribute("TellTime");
        if (StringUtils.isEmpty(dateTime) || dateTime == null) {
            session.setAttribute("TellTime", DateFormatUtil.formateString());
        } else {
            String now = DateFormatUtil.formateString();
            if (DateFormatUtil.compare_date(dateTime, now)) {
                //超过2分钟
                session.setAttribute("TellTime", now);
            } else {
                object.put("success", "0");
                object.put("errMsg", "订单提交过快，请稍后再试");
                return object.toString();
            }
        }
        GoodsListModel goodsListModel = new GoodsListModel();
        goodsListModel.setId(goodsId);
        List<GoodsListModel> goodsListModels = this.goodsListService.queryGoodsList(goodsListModel);
        if (goodsListModels.size() == 0) {
            object.put("success", "0");
            object.put("errMsg", "商品数据异常");
            return object.toString();
        } else {
            goodsListModel = goodsListModels.get(0);
            if (!goodsListModel.getRealPrice().equals(realMoney)){
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                if(StringUtils.isEmpty(companyName.trim())){
                    companyName="未知用户";
                }
                blackUserModel.setRechargeName(companyName+goodsListModel.getRealPrice()+"元");
                blackUserModel.setRechargeNum(tel);
                blackUserModel.setRechargePrice(realMoney);
                addBlackUser(blackUserModel);
                object.put("success", "0");
                object.put("errMsg", "商品数据异常");
                return object.toString();
            }
            goodsListModel.setId(goodsId);
            goodsListModel.setRealPrice(realMoney);
            if(!(makeGoodsMd5Str(goodsListModels.get(0)).equals(makeGoodsMd5Str(goodsListModel)))){
                if (!goodsListModel.getRealPrice().equals(realMoney)){
                    blackUserModel.setOpenId(openId);
                    blackUserModel.setUserId(userId);
                    blackUserModel.setCreateTime(DateFormatUtil.formateString());
                    blackUserModel.setIp(IPUtil.getIpAddr(request));
                    blackUserModel.setRechargeDesc("商品MD5与支付商品MD5不符");
                    blackUserModel.setRechargeName(companyName+goodsListModel.getRealPrice()+"元");
                    blackUserModel.setRechargeNum(tel);
                    blackUserModel.setRechargePrice(realMoney);
                    addBlackUser(blackUserModel);
                    object.put("success", "0");
                    object.put("errMsg", "商品数据异常");
                    return object.toString();
                }
            }
        }
        String sporder_time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", telUserid);
        params.put("userpws", MD5X.getLowerCaseMD5For32(telUserpws));
        params.put("cardid", telCardid);
        params.put("cardnum", goodsListModel.getGoodsPrice());
        params.put("sporder_id", UUIDUtil.getUUID());
        params.put("sporder_time", sporder_time);
        params.put("game_userid", tel);
        params.put("ret_url", telUrl);
        params.put("version", telVersion);
        params.put("timestamp", DateFormatUtil.formateString());
        String str = telUserid + MD5X.getLowerCaseMD5For32(telUserpws) + telCardid + params.get("cardnum") + params.get("sporder_id") + sporder_time + tel + telKeystr;
        String md5_str = MD5X.getUpperCaseMD5For32(str);
        params.put("md5_str", md5_str);

        session.setAttribute("companyName", companyName);
        session.setAttribute("telParams", params);
        logger.error("预支付接口调用参数:"+JSONObject.fromObject(params).toString());

        OrderInfoModel order=new OrderInfoModel();
        order.setId(UUIDUtil.getUUID());
        order.setGoodsName(companyName+params.get("cardnum")+"元");
        order.setCreateTime(DateFormatUtil.formateString());
        order.setOrderNo(params.get("sporder_id"));
        order.setFromType("1");
        order.setOpenId(openId);
        order.setGoodsMoney(params.get("cardnum"));
        order.setPayMoney(realMoney);
        order.setIp(IPUtil.getIpAddr(request));
        order.setMd5Sign(MD5Util.MD5Encode(companyName+order.getOrderNo()+order.getPayMoney()+order.getIp()).toUpperCase());
        order.setOrderParams(JSONObject.fromObject(params).toString());
        order.setPayStatus("0");
        order.setRemark(tel);
        order.setStatus("0");
        order.setWxOrder("");
        order.setGoodsId(goodsId);
        this.orderService.addOrderInfo(order);
        logger.error("预支付订单参数:"+JSONObject.fromObject(order).toString());

        object.put("payamount",realMoney);
        object.put("rechargeType","huafei");
        object.put("lifeType","");
        object.put("redPkgId",ticket);
        object.put("redPkgValue",value);
        object.put("payInfo",order.getMd5Sign());
        object.put("success","1");
        object.put("errMsg","");
        return object.toString();
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

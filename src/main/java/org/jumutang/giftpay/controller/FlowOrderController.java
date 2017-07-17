package org.jumutang.giftpay.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.http.HttpResponse;
import org.apache.xpath.operations.Or;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.*;
import org.jumutang.caranswer.model.OrderInfo;
import org.jumutang.giftpay.base.web.controller.BaseController;

import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.model.BlackUserModel;
import org.jumutang.giftpay.model.OrderInfoModel;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.UserNumRecord;
import org.jumutang.giftpay.service.FlowOrderService;
import org.jumutang.giftpay.service.PayInfoServiceI;
import org.jumutang.giftpay.service.UserNumRecordService;
import org.jumutang.giftpay.tools.*;


import org.jumutang.giftpay.tools.MD5X;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/4/5.
 */

@Controller
@RequestMapping(value = "/flow")
public class FlowOrderController extends BaseController {

    //配置打印日志Logger
    private static final org.slf4j.Logger _LOGGER = LoggerFactory.getLogger(FlowOrderController.class);

    //格式化输出 simpleDateFormat
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //UUID生成随机数
    UUID uuid = UUID.randomUUID();

    //流量请求 URL [聚信api] http://api.juxinbox.com/jxapi/traffic_doTrafficOrder
    private String FlowUrl = "http://api.juxinbox.com/jxapi/traffic_doTrafficOrder";

    //流量 接口所属应用ID 【聚信api 流量接口】
    private String appid = "85e0ca0d62c1debcb0fb456d51b0b167";

    //流量 回调URL 路径  【聚信api 流量接口】
//    private String backUrl = "http://www.amumu.xin/giftpay_wap/flow/backFlowUrl.htm";  //无返回数据 跳转，从Session获取数据
    private String backUrl = "http://www.linkgift.cn/giftpay_wap/flow/backFlowUrl_2.htm";  //返回respondBody数据

    /**
     * 跳转页面本地测试url
     */
    private String redirectUrl = "giftpay/liftpayment/orderPay.html";

    //网迅AppId   流量接口 【网迅 流量接口】
    private final String WxAppId = "sw1457505772";

    //网迅AppScrect 流量接口 【网迅 流量接口】
    private final String WxAppScrect = "jQKUKsaO3E1aqqCCQWpo";

    @Autowired
    private FlowOrderService flowOrderService;

    @Autowired
    private UserNumRecordService userNumRecordService;

    //      md5加密规则
    public static String createSign(SortedMap<String, String> packageParams) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        String str = sb.toString();
        String strec = str.substring(0, str.length() - 1);
        String sign = MobileMessageUtil.md5Encodenew(URLEncoder.encode(strec, "utf-8"));
        return sign;
    }


    //    @Autowired
//    private PayInfoServiceI payInfoServiceI;
    public static String md5Encodenew(String sourceStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = null;
        try {
            byteArray = sourceStr.getBytes("UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    //-----------------------------微聚api 流量充值------------------------------------------

    //流量订单发送请求 从页面获取传递的参数value 【不使用 】
    @RequestMapping(value = "/addFlowSession", method = {RequestMethod.GET, RequestMethod.POST})
    public String addSession(@RequestParam(value = "mobile", required = true) String mobile,
                             @RequestParam(value = "packCode", required = true) String packCode,
                             @RequestParam(value = "number_desc", required = true) String number_desc,
                             @RequestParam(value = "rechargeAmount", required = true) String rechargeAmount,
                             @RequestParam(value = "dealMoney", required = true) String dealMoney,
                             @RequestParam(value = "redPkgId", required = true) String redPkgId,
                             @RequestParam(value = "redPkgValue", required = true) String redPkgValue,
                             @RequestParam(value = "rechargeType", required = true) String rechargeType,
                             HttpSession session,
                             HttpServletRequest request,
                             HttpServletResponse response
    ) {

        //Session中获取相关参数 [userId,wxUser,openId]
        String wxUser = String.valueOf(session.getAttribute("wxUser"));
        String userId = String.valueOf(session.getAttribute("userId"));
        String openId = String.valueOf(session.getAttribute("openId"));

        //订单编号 生成orderNo
        String orderNo = sdf.format(new Date());

        //定义请求 流量接口参数 map
        SortedMap<String, String> map = new TreeMap<String, String>();
        //调用方法 获取请求流量参数的map集合
        map = MadeMap(orderNo, mobile, packCode);

        //订单记录时间 [yyyy-mm-dd hh:mm:ss]
        String time = sdf2.format(new Date());

        _LOGGER.info("--------------- 判断是否存在相同的 number 充值记录，如果存在更行CreateTime，不存在Create当前历史记录 !-------------------------");
        //插入数据到t_userNumRecord表
        boolean isExits = userNumRecordService.isNumExist(userId, mobile, 3);

        if (isExits == false) {
            _LOGGER.info("--------------- 当前userId用户  Add 历史充值流量记录 ！-------------------------");
            //新增数据
            userNumRecordService.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
        } else {
            _LOGGER.info("--------------- 当前userId用户 更新充值createTime ！-------------------------");
            //更新数据
            userNumRecordService.updateCreateTime(userId, mobile, time);
        }

        _LOGGER.info("--------------- 为当前userId ，添加 PayInfo 订单记录 !-------------------------");
        //回调成功之后 插入数据到payInfo表中
        payInfoServiceI.addUserRecordPayInfo(uuid.toString().replaceAll("-", ""), userId, orderNo, "有礼付流量充值", number_desc + "流量充值" + rechargeAmount, sdf2.format(new Date()), 3, 2, Double.parseDouble(dealMoney), mobile, 0);

        //准备跳转支付页面，准备参数
        request.setAttribute("redPkgId", redPkgId);
        request.setAttribute("redPkgValue", redPkgValue);
        request.setAttribute("rechargeType", rechargeType);
        request.setAttribute("payamount", dealMoney);

        //请求流量订单参数存入session
        session.setAttribute("flowMap", map);


         /*

            //正式调用时候，去掉头部 responseBody

            sendFlowOrder.htm 是流量订单发送接口。在订单通过钱包支付成功之后，
            调用sendFlowOrder.htm的controller，获取Session中已经封装好的【flowMap】。
            发起请求，回调URL中获得当前请求流量订单的结果：
                 1 成功 修改payInfo中state状态==1 和 实付款 dealReaMoney
                -1 失败 ，流量请求订单失败

         */

        return "sendFlowOrder.htm";
    }

    //流量订单发送请求 从session中获取数据 【不使用 】
    @RequestMapping(value = "/addSession_2", method = {RequestMethod.GET, RequestMethod.POST})
    public void addSession_2(
            HttpSession session, HttpServletRequest request, HttpServletResponse response) {

        //Session中获取相关参数 [userId,wxUser,openId]
        String wxUser = String.valueOf(session.getAttribute("wxUser"));
        String userId = String.valueOf(session.getAttribute("userId"));
        String openId = String.valueOf(session.getAttribute("openId"));

        Map<String, String> data2Map = (Map<String, String>) session.getAttribute("data2Map");
        String mobile = String.valueOf(data2Map.get("mobile"));
        String packCode = String.valueOf(data2Map.get("packCode"));
        String number_desc = String.valueOf(data2Map.get("number_desc"));
        String dealMoney = String.valueOf(data2Map.get("dealMoney"));
        String rechargeAmount = String.valueOf(data2Map.get("rechargeAmount"));
        _LOGGER.info("--------------- Session获取data2Map[ " + data2Map.toString() + " ] !----------------");


        //订单编号 生成orderNo
        String orderNo = sdf.format(new Date());

        //定义请求 流量接口参数 map
        SortedMap<String, String> map = new TreeMap<String, String>();
        //调用方法 获取请求流量参数的map集合
        map = MadeMap(orderNo, mobile, packCode);

        //订单记录时间 [yyyy-mm-dd hh:mm:ss]
        String time = sdf2.format(new Date());

        _LOGGER.info("--------------- 判断是否存在相同的 number 充值记录，如果存在更行CreateTime，不存在Create当前历史记录 !----------------");
        //插入数据到t_userNumRecord表
        boolean isExits = userNumRecordService.isNumExist(userId, mobile, 3);

        if (isExits == false) {
            _LOGGER.info("--------------- 当前userId用户  Add 历史充值流量记录 ！-------------------------");
            //新增数据
            userNumRecordService.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
        } else {
            _LOGGER.info("--------------- 当前userId用户 更新充值createTime ！-------------------------");
            //更新数据
            userNumRecordService.updateCreateTime(userId, mobile, time);
        }

        _LOGGER.info("--------------- 为当前userId ，添加 PayInfo 订单记录 !-------------------------");
        //回调成功之后 插入数据到payInfo表中
//        payInfoServiceI.addUserRecordPayInfo(uuid.toString().replaceAll("-",""),userId,orderNo,"有礼付流量充值",number_desc+"流量充值"+rechargeAmount,sdf2.format(new Date()),3,2,Double.parseDouble(dealMoney),mobile,0);


        //从Session取出已经封转好的map参数
        String strpost = net.sf.json.JSONObject.fromObject(map).toString();

        // 之前的方法是 通过跳转/sendFlowOrder的controller中 发起请求 聚信流量接口订单
        String strs = PostOrGet.sendPost(FlowUrl, strpost, null);

        session.setAttribute("RESULT", strs);

    }


    //发送流量订单 请求【New】
    @RequestMapping(value = "/sendFlowOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public void sendFlow(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        _LOGGER.info("--------------- 为当前userId ，添加 PayInfo 订单记录 !-------------------------");

        //从Session取出已经封转好的map参数
        String strpost = net.sf.json.JSONObject.fromObject(session.getAttribute("flowMap")).toString();

        _LOGGER.info("--------------- 当前请求订单参数:【" + strpost + "】 !-------------------------");
        //请求执行成功之后，自动跳转回调URL
        PostOrGet.sendPost(FlowUrl, strpost, null);

    }

    //发送流量订单 请求【Old】
    @RequestMapping(value = "/getFlow", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String weijun(
            @RequestParam(value = "appid", required = true) String appid,
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "packCode", required = true) String packCode,
            @RequestParam(value = "backUrl", required = true) String backUrl,
            @RequestParam(value = "number_desc", required = true) String number_desc,
            @RequestParam(value = "rechargeAmount", required = true) String rechargeAmount,
            @RequestParam(value = "dealMoney", required = true) String dealMoney,
            @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "openId", required = true) String openId,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        _LOGGER.info("---------测试 是否从session中获取数据WxUser=[" + session.getAttribute("wxUser") + "] --------------");
        _LOGGER.info("---------测试 是否从session中获取数据userId=[" + session.getAttribute("userId") + "] --------------");
        _LOGGER.info("---------测试 是否从session中获取数据openId=[" + session.getAttribute("openId") + "] --------------");


        _LOGGER.info("进入聚信 流量api，准备受理当前订单。。。。。");

        _LOGGER.info("---------------添加 UserNumRecord 历史记录 !-------------------------");
        UUID uuid = UUID.randomUUID();
        UserNumRecord userNumRecord = new UserNumRecord();
        userNumRecord.setId(uuid.toString().replaceAll("-", ""));
        userNumRecord.setNumber(mobile);
        userNumRecord.setNumberType(3);
        //userNumRecord.setUserId(userId); //从页面传递参数userId 获取数据
        userNumRecord.setUserId(String.valueOf(session.getAttribute("userId"))); //从session获取数据
        userNumRecord.setNumber_desc(number_desc);
        userNumRecord.setCreateTime(sdf2.format(new Date()));

        _LOGGER.info("--------------- 判断UserNumRecord 是否存在，存在则更新CreateTime,不存在则 Create !-------------------------");
        //插入数据到t_userNumRecord表
        boolean isExits = userNumRecordService.isNumExist(userNumRecord.getUserId(), userNumRecord.getNumber(), 3);
        if (isExits == false) {
            //新增数据
            userNumRecordService.addUserNumRecord(userNumRecord);
        } else {
            //更新数据
            userNumRecordService.updateCreateTime(userNumRecord.getUserId(), mobile, userNumRecord.getCreateTime());
        }

        _LOGGER.info("----------------发送 充值流量 请求 ，准备进入 回调函数 !-------------------------");
        String orderNo = sdf.format(new Date());
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appid", appid);
        map.put("mobile", mobile);
        map.put("packCode", packCode);
        map.put("orderId", orderNo);
        map.put("backUrl", backUrl);

        _LOGGER.info("----------------流量订单OrderId:" + orderNo + " !-------------------------");
        //发送流量订单
        String sign = null;
        try {
            sign = FlowOrderController.createSign(map);
            map.put("sign", sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String strpost = net.sf.json.JSONObject.fromObject(map).toString();
        String result = PostOrGet.sendPost(FlowUrl, strpost, null);


        _LOGGER.info("---------------添加 PayInfo 订单记录 !-------------------------");
        //回调成功之后 插入数据到payInfo表中
        //插入数据t_payInfo
        PayInfoModel payInfoModel = new PayInfoModel();
        payInfoModel.setDealId(uuid.toString().replaceAll("-", ""));
        payInfoModel.setAccountId(userId);   //当前订单 用户
        payInfoModel.setOrderNo(orderNo);  // 【当前orderNo 是发送 流量订单 时创建的】
        payInfoModel.setBusinessInfo("有礼付流量充值");  //业务类型
        payInfoModel.setDealInfo(number_desc + "流量充值" + rechargeAmount);  //充值详细信息: 运营商+当前业务类型+商品规格
        payInfoModel.setDealTime(sdf2.format(new Date()));  //订单创建日期
        payInfoModel.setDealType((short) 3);  // 1提现 ，2充值 ，3支付 ，4退款
        payInfoModel.setDealState((short) 2);  //2 充值中 ， 1 充值成功
        payInfoModel.setDealMoney(Double.parseDouble(dealMoney));     //预付款
        payInfoModel.setDealRemark(mobile);   //手机号
        payInfoModel.setDealRealMoney(0.00); //实付款 [支付完成后 ，修改当前价格]

        payInfoServiceI.insertPayInfo(payInfoModel);

        return result;
    }


  /*  // 微聚api 流量接口回调URL地址
    @RequestMapping(value = "/backFlowUrl",method = {RequestMethod.GET,RequestMethod.POST})
    public void getBack(HttpServletRequest request, HttpServletResponse response , HttpSession session){

        _LOGGER.info("----------------已经进入回调URL方法中 !-------------------------");

        try {
            byte[] bytes = new byte[1024 * 1024];
            InputStream is = request.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            String gas_returnstr = new String(bytes, 0, nTotalRead, "utf-8");

            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(gas_returnstr);

            _LOGGER.info("--------------------->当前响应数据result："+jsonObject);

            if(jsonObject.containsKey("status")){
                // 流量的场合
                    String ret_code = jsonObject.getString("status");  //ret_code 充值后状态，1代表成功，-1代表失败
                    String orderNo = jsonObject.getString("orderId");  //订单号

                    //默认设置 rec_code == 1 ， 手动设置当前 充值订单成功
                     ret_code = "1";

                    if("1".equals(ret_code)){
                        //代表充值流量成功，修改payInfo表的state状态 [修改实付款dealReaMoney ， 订单状态 State]
                        int dealMoney = payInfoServiceI.selDealmoneyByOrderNo(orderNo);
                        int updateCount = payInfoServiceI.UpdatePayinfoByOrderNo(orderNo,dealMoney);

                        if(updateCount>0){
                            _LOGGER.info("--------------------------订单充值成功！ 修改订单State==1 -------------------------------");
                        }
                    }else{
                        //充值失败
                        _LOGGER.info("-------------------------- 订单 充值 失败 ！ -------------------------------");
                    }

                _LOGGER.info("-------------------------------流量充值状态:"+ret_code+"----------------------------");
            }

        } catch (Exception e) {
            _LOGGER.info(e.getMessage(), e);
        }
    }*/

    // 流量接口回调地址 url_2 【使用】
    @RequestMapping(value = "/backFlowUrl_2", method = {RequestMethod.GET, RequestMethod.POST})
    public void getBack_2(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        _LOGGER.error("----------------已经进入回调URL方法中 !-------------------------");

        try {
            byte[] bytes = new byte[1024 * 1024];
            InputStream is = request.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            String gas_returnstr = new String(bytes, 0, nTotalRead, "utf-8");

            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(gas_returnstr);

            _LOGGER.error("-----------------当前响应数据result：" + jsonObject.toString() + "------------------------");


            if (jsonObject.containsKey("status")) {
                // 流量的场合
                String ret_code = jsonObject.getString("status");  //ret_code 充值后状态，1代表成功，-1代表失败
                String orderNo = jsonObject.getString("orderId");  //订单号

                //默认设置 rec_code == 1 ， 手动设置当前 充值订单成功

                if ("1".equals(ret_code)) {
                    PayInfoModel payInfoModel = new PayInfoModel();
                    payInfoModel.setDealState((short) 1);
                    payInfoModel.setOrderNo(orderNo);
                    int updateCount = payInfoServiceI.updatePayInfo(payInfoModel);
                    _LOGGER.error("------------PayInfo表---订单状态更新-----------");
                } else {
                    //充值失败
                    _LOGGER.error("-------------------------- 订单 充值 失败 ！ -------------------------------");

                    //1.判断用户是否存在余额表，2.如果没有新建Balance表


                }
                _LOGGER.error("-------------------------------流量充值状态:" + ret_code + "----------------------------");
            }

        } catch (Exception e) {
            _LOGGER.error(e.getMessage(), e);
        }


    }


    //调用红包接口 获取当前用户下红包数量
    @RequestMapping(value = "/redGet")
    @ResponseBody
    public String redGet(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        //获取微信用户登陆的唯一openId
        String openId = String.valueOf(session.getAttribute("openId"));

        _LOGGER.info("通过接口获取当前userId 下的用户红包个数 ---------------");

        String result = new PostOrGet("utf-8").sendPost(queryAllRedUrl, ESendHTTPModel._SEND_MODEL_DECODER,
                new NamedValue("openId", openId),
                new NamedValue("state", "1")
        );

        return result;
    }


    // 微聚api 流量充值记录查询【更新方法】【新版本】
    @RequestMapping(value = "/getFlowOrderRecord_2", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getFlowOrderRecord_2(
            HttpServletRequest request, HttpServletResponse response, HttpSession session,
            @RequestParam(value = "type", required = true) String type
    ) {

        //充值 历史记录
        String openId = String.valueOf(session.getAttribute("openId"));

        OrderInfoModel orderInfo = new OrderInfoModel();
        orderInfo.setOpenId(openId);
        String mapJson = "";
        //判断流量或者话费查询
        if (type.equals("1")) {
            //话费
            orderInfo.setFromType("1");
        } else if (type.equals("2")) {
            //流量
            orderInfo.setFromType("3");
        }

        System.out.println("后台获取数据:userId:" + openId + " ，type:" + type);
        ArrayList<OrderInfoModel> orderInfoModels = payInfoServiceI.HisFlowOrderRecord(orderInfo);


        _LOGGER.info("当前查询出来的payInfoModels数据:" + orderInfo.toString());


        if (orderInfoModels.size() > 0) {

            _LOGGER.info("查询数据 微聚流量api 查询当前userId用户下的 流量充值记录---------------");

            // ------------------------------获取[年月]------------------------------------------------------


            //获取年月[TreeMap 默认排序升序， 定义Comparator 比较规则 ]
            TreeMap<String, ArrayList<OrderInfoModel>> map = new TreeMap<String, ArrayList<OrderInfoModel>>(new Comparator<String>() {
                /*
                 * int compare(Object o1, Object o2) 返回一个基本类型的整型，
                 * 返回负数表示：o1 小于o2，
                 * 返回0 表示：o1和o2相等，
                 * 返回正数表示：o1大于o2。
                 */
                public int compare(String o1, String o2) {
                    //指定排序器按照降序排列
                    return o2.compareTo(o1);
                }
            });

            ArrayList<OrderInfoModel> list = null;

            for (OrderInfoModel p : orderInfoModels) {
                //获取当前数据的年份+月份 进行分组排序 例如:2017-04
                String t = p.getCreateTime().toString().substring(0, 7);

                //判断当前map集合中是否存在key对应的 value
                if (map.get(t) == null) {
                    //如果不存在 ，生成新的list对象
                    list = new ArrayList<OrderInfoModel>();
                    //添加p到当前map集合的value中去
                    list.add(p);
                    //根据t为key，list作为value 存入map集合中
                    map.put(t, list);
                } else {
                    //如果存在和key对应的数据，往list集合中添加数据
                    ArrayList<OrderInfoModel> l = map.get(t);
                    l.add(p);
                }

            }

            //默认map
            mapJson = JSONObject.toJSONString(map);
        } else {
            mapJson = "none";
        }

        return mapJson;

    }

    //以前版本查询历史记录
//    @RequestMapping(value = "/getFlowOrderRecord_2",method = {RequestMethod.GET,RequestMethod.POST})
//    @ResponseBody
//    public String getFlowOrderRecord_2(
//            HttpServletRequest request ,HttpServletResponse response ,HttpSession session,
//            @RequestParam(value = "type",required = true) String type
//    ){
//
//        //充值 历史记录
//        String userId = String .valueOf(session.getAttribute("userId"));
//
//        PayInfoModel payInfoModel = new PayInfoModel();
//        payInfoModel.setAccountId(userId);
//        String mapJson="";
//        //判断流量或者话费查询
//        if(type.equals("1")){
//            //话费
//            payInfoModel.setDealRemark("有礼付充值-话费(微信支付)");
//        }else if(type.equals("2")){
//            //流量
//            payInfoModel.setDealRemark("有礼付充值-流量(微信支付)");
//        }
//
//        System.out.println("后台获取数据:userId:"+userId+" ，type:"+type);
//        ArrayList<PayInfoModel> payInfoModels = payInfoServiceI.HisFlowOrderRecord(payInfoModel);
//
//        _LOGGER.info("当前查询出来的payInfoModels数据:"+payInfoModels.toString());
//
//        for(PayInfoModel payInfoModel1 : payInfoModels){
//            System.out.println(payInfoModel.toString());
//        }
//
//      if(payInfoModels.size()>0){
//
//          _LOGGER.info("查询数据 微聚流量api 查询当前userId用户下的 流量充值记录---------------");
//
//          // ------------------------------获取[年月]------------------------------------------------------
//
//
//          //获取年月[TreeMap 默认排序升序， 定义Comparator 比较规则 ]
//          TreeMap<String,ArrayList<PayInfoModel>> map  = new TreeMap<String,ArrayList<PayInfoModel>>(new Comparator<String>(){
//              /*
//               * int compare(Object o1, Object o2) 返回一个基本类型的整型，
//               * 返回负数表示：o1 小于o2，
//               * 返回0 表示：o1和o2相等，
//               * 返回正数表示：o1大于o2。
//               */
//              public int compare(String o1, String o2) {
//                  //指定排序器按照降序排列
//                  return o2.compareTo(o1);
//              }
//          });
//
//          ArrayList<PayInfoModel> list = null;
//
//          for(PayInfoModel p : payInfoModels){
//              //获取当前数据的年份+月份 进行分组排序 例如:2017-04
//               String t =    p.getDealTime().toString().substring(0,7);
//
//              //判断当前map集合中是否存在key对应的 value
//                if(map.get(t)==null ){
//                    //如果不存在 ，生成新的list对象
//                    list = new ArrayList<PayInfoModel>();
//                    //添加p到当前map集合的value中去
//                    list.add(p);
//                    //根据t为key，list作为value 存入map集合中
//                    map.put(t,list);
//                }  else{
//                    //如果存在和key对应的数据，往list集合中添加数据
//                    ArrayList<PayInfoModel> l = map.get(t);
//                    l.add(p);
//                }
//
//          }
//
//          //默认map
//          mapJson = JSONObject.toJSONString(map);
//      }else{
//          mapJson ="none";
//      }
//
//        return mapJson;
//
//    }


    //获取手机流量 充值记录 5条
    @RequestMapping(value = "/getFlowNumber5", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getFlowNumber5(
            HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) {

        String userId = String.valueOf(session.getAttribute("userId"));

        //查询当前用户下的HistoryNumber [根据用户的userId]
        ArrayList<UserNumRecord> list = userNumRecordService.historyFlowOrder(userId, "3");

        String json = JSONObject.toJSONString(list);

        _LOGGER.info("获取手机历史充值记录 5 条---------------");

        return json;

    }


    //删除指定用户下的手机号码[针对手机号码进行删除]
    @RequestMapping(value = "/delNumber", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String deluserIdNumber(
            HttpServletRequest request, HttpServletResponse response, HttpSession session,
            @RequestParam(value = "number", required = true) String number
    ) {

        String userId = String.valueOf(session.getAttribute("userId"));

        userNumRecordService.delUserNumRecord(userId, number);

        _LOGGER.info("删除指定number的手机号码---------------");

        return "del_ok";
    }

    //删除数据库全部的数据
    @RequestMapping(value = "/delFiveNumber", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String delFiveNumber(
            HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) {

        String userId = String.valueOf(session.getAttribute("userId"));

        //删除数据库 当前userId的所有数据
        userNumRecordService.delAll(userId);

        _LOGGER.info("删除所有userId下的流量充值记录---------------");

        return "del_all_Ok";
    }


    //获取页面传递参数并且存入session中 【data2Map】
    @RequestMapping(value = "/addValueSession", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addValueSession(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "packCode", required = true) String packCode,
            @RequestParam(value = "number_desc", required = true) String number_desc,
            @RequestParam(value = "realDealMoney", required = true) String realDealMoney,
            @RequestParam(value = "redPkgId", required = true) String redPkgId,
            @RequestParam(value = "rechargeAmount", required = true) String rechargeAmount,
            @RequestParam(value = "redPkgValue", required = true) String redPkgValue,
            HttpServletResponse response,
            HttpServletRequest request,
            HttpSession session
    ) {

        //参数组合以map形式存入session中
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile); //手机号码
        map.put("packCode", packCode); //流量代码
        map.put("number_desc", number_desc); //运营商名称
        map.put("dealMoney", realDealMoney); //真实付款金额
        map.put("rechargeAmount", rechargeAmount); //充值套餐兆数量
        map.put("timestamp", DateFormatUtil.formateString()); //传递时间戳 ， 后台判断测试用户是否刷单行为

        //充值套餐类型
        String rechargeType = "liuliang";

        String str = realDealMoney + rechargeType + map.get("timestamp");
        str = MD5Util.MD5Encode(str).toUpperCase(); //加密字符串

        //将当前payMd5加密支付串存入Session中
        session.setAttribute("payMd5", str);

        //将当前data2Map存入Session中。流量订单参数
        session.setAttribute("data2Map", map);

        return "success";
    }


    //制作参数 请求流量接口,返回map类型
    public SortedMap<String, String> MadeMap(String orderNo, String mobile, String packCode) {

        _LOGGER.info("--------------- 进入MadeMap 准备制作SortMap的集合 ！-------------------------");

        SortedMap<String, String> map = new TreeMap<String, String>();

        map.put("appid", appid);       //应用接口辨识编号
        map.put("mobile", mobile);      //充值手机号
        map.put("packCode", packCode);  //充值套餐编号
        map.put("orderId", orderNo);    //订单号
        map.put("backUrl", backUrl);  //回调函数 流量充值回调URL

        String sign = null;
        try {
            sign = createSign(map);
            map.put("sign", sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        _LOGGER.info("--------------- MdaeMap制作完成 ！-------------------------");
        return map;
    }


    //跳转订单支付页面orderPay.html
//    @RequestMapping(value = "/toOrderPay.htm")
//    @ResponseBody
//    public void toOrderPay(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
//
//        //获取页面传递参数
//        String payamount = request.getParameter("payamount"); //应付金额
//        String realmount = request.getParameter("realmount"); //实付金额
//        String rechargeType = request.getParameter("rechargeType"); //充值类型
//        String redPkgId = request.getParameter("redPkgId"); //红包id
//        String redPkgValue = request.getParameter("redPkgValue"); //红包金额
//
//        //参数组合以map形式存入session中
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("timestamp",DateFormatUtil.formateString());
//        params.put("timestamp",DateFormatUtil.formateString());
//
//
//
//        String str=realmount+rechargeType+params.get("timestamp");
//        str= MD5Util.MD5Encode(str).toUpperCase(); //加密字符串
//        session.setAttribute("payMd5",str);
//
//        String targetUrl="";
//        if(redPkgValue.equals("0")){
//            targetUrl = this.getBaseUrl(request, response) + redirectUrl + "?redPkgId=&payamount="+payamount+"&realmount="+realmount+"&rechargeType="+rechargeType;
//        }else{
//            targetUrl = this.getBaseUrl(request, response) + redirectUrl + "?redPkgId="+redPkgId+"&redPkgValue="+redPkgValue+"&payamount="+payamount+"&realmount="+realmount+"&rechargeType="+rechargeType;
//        }
//
//        logger.info("=====================跳转支付页面地址:" + targetUrl);
//
//        response.sendRedirect(response.encodeRedirectURL(targetUrl));
//
//    }

    //--------- 网讯流量--------------------------------------------------------------------------------

    //base64加密
    public String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    //base64解密
    public String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //生成随机字符串
    public String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //时间戳
    public String getTimeStatem() {

        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        return sdf.format(date);

    }


    //网讯流量充值【流量订购】 【成功，接口已经调通】
    public String sendOrder(String PackCode, String Mobile, String OrderId) {

        logger.error("--------------进入充值流量方法SendOrder()-----------------");

        //String Appid = "sw1457505772"; //客户接入ID
        //String AppScret = "jQKUKsaO3E1aqqCCQWpo";
        String Appid = WxAppId;
        String AppScret = WxAppScrect;
        String OrderID = OrderId; //订单编号
        String Echostr = getRandomString(20); //生成随机字符串[长度20]
        String Timestamp = getTimeStatem(); //时间戳

        logger.error("--------------当前充值参数:AppId:[" + Appid + "]---AppScret:[" + AppScret + "]----OrderID:[" + OrderID + "]---Echostr:[" + Echostr + "]---Timestamp:[" + Timestamp + "]--------");

        /*
        * Md5(AppID + OrderID + AppSecret + Echostr + Timestamp)生成32位小写字符串
        * */
        String sign = MD5X.getLowerCaseMD5For32(Appid + OrderID + AppScret + Echostr + Timestamp);  //加密sign签名

        logger.error("----------当前加密sign:[" + sign + "]------Md5(AppID + OrderID + AppSecret + Echostr + Timestamp)生成32位小写字符串--------");


        logger.error("----------充值的packCode:[" + PackCode + "]--------");

        logger.error("--------------发起Http请求，网迅流量订购请求！-----------------");
        String result = new PostOrGet("utf-8").sendPost("http://api.wangxunit.com/v3/order",
                ESendHTTPModel._SEND_MODEL_DECODER,
                new NamedValue("AppID", Appid),
                new NamedValue("OrderID", OrderID),
                new NamedValue("OrderType", "1"),  //订单类型：1 订购
                new NamedValue("Echostr", Echostr),
                new NamedValue("Timestamp", Timestamp),
                new NamedValue("Signature", sign), //加密签名
                new NamedValue("PackCode", String.valueOf(PackCode)), //套餐代码
                new NamedValue("Mobile", String.valueOf(Mobile)), //充值手机号码
                new NamedValue("EffectType", "1") // 生效方式
        );

        //响应数据格式： {"Code":"0009","Msg":"账户不存在！"}

        JSONObject jsonObject1 = (JSONObject) JSONObject.parse(result);

        String CodeMessage = jsonObject1.getString("Code");
        logger.error("--------------响应当前http请求返回JSON数据:[" + CodeMessage + "]-----------------");

        //定义返回参数变量
        String ResultMessage = "";

        //判断是否充值成功,Code:0000 表示充值成功
        if ("0000".equals(CodeMessage)) {
            logger.error("--------------充值成功!返回CodeMessage:" + CodeMessage + "-----------------");
            ResultMessage = "RechargeSuccess";
        }
//        else if ("0023".equals(CodeMessage)){
//            logger.error("--------------余额不足，已缓存！等待对方提交充值订单:"+CodeMessage+"-----------------");
//            ResultMessage = "RechargeSuccess";
//        }
        else {
            logger.error("--------------充值失败!异常CodeMessage:" + CodeMessage + "-----------------");
            ResultMessage = CodeMessage;  //返回异常代码
        }

        return ResultMessage;
    }

    //网迅流量充值 【流量回调】 获取第三方响应数据 [http://www.linkgift.cn/giftpay_wap/flow/getSendOrderStatu.htm]
    @RequestMapping(value = "/getSendOrderStatu", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getUserOrderStatus(
            HttpServletRequest request
    ) {

        _LOGGER.error("-----------------进入回调URL 网迅------------------------");
        try {
            byte[] bytes = new byte[1024 * 1024];
            InputStream is = request.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            String gas_returnstr = new String(bytes, 0, nTotalRead, "utf-8");

            //回调WX获取的数据：{"Signature":"bd43a82e2f7d7e4fb1a9402c2435f5a6","AppID":"sw1457505772","Echostr":"BM8EUEjCVJ3iSokmJ8uJ","Timestamp":"20170505163330","Mobiles":[{"OrderID":"96a72123c68142d8894543852d3555ff","OrderType":1,"EffectType":1,"PackCode":"y100030","Mobile":"15730356136","Code":"9999","Msg":"系统异常！"}]}------------------------
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(gas_returnstr);
            _LOGGER.error("-----------------回调WX获取的数据：" + jsonObject.toString() + "------------------------");

            if (jsonObject.containsKey("Mobiles")) {

                //解析Mobile为JsonObject对象
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
                JSONArray json = JSONArray.fromObject(jsonObject.get("Mobiles"), jsonConfig);
                //{"OrderType":"1","PackCode":"100030","OrderID":"96a72123c68142d8894543852d3555ff","EffectType":"1","Mobile":"15730356136","Code":"9999"}
                _LOGGER.error("--------json.toString().packCode：" + json.get(0).toString());

                _LOGGER.error("-------将jsonArray.toString()转换为jsonObject对象--------");
                JSONObject MobileObject = JSONObject.parseObject(json.get(0).toString());
                _LOGGER.error("------------Code:" + MobileObject.get("Code").toString());

                String orderNo = MobileObject.get("OrderID").toString();
                PayInfoModel payInfoModel = new PayInfoModel();
                payInfoModel.setOrderNo(orderNo);
                _LOGGER.error("--------------准备更新当前流量订单[" + orderNo + "]的状态------------------");

                _LOGGER.error("-----判断当前充值状态是否成功?------");
                if ("0000".equals(MobileObject.get("Code").toString())) {
                    _LOGGER.error("----- 当前流量充值状态Code:" + MobileObject.get("Code").toString() + "----SUCCESS!!!----");
                    // payInfoModel.setDealState((short)1);  //之前正式 充值成功，接受到回调链接，修改订单状态
                    payInfoModel.setOfOrder(UUIDUtil.getUUID());  //2017-06-05 在payInfoModel中赋值欧非值，确定当前订单已经充值成功
                }
//                else if ("0023".equals(MobileObject.get("Code").toString())){
//                    //应该是不会触发当前else if的，余额不足时候，不会发起回执url请求。当余额充足的时候，第三方手动发起当前订单的回调。
//                    _LOGGER.error("----- 余额不足，第三方手动提交订单！充值状态Code:"+MobileObject.get("Code").toString()+"----SUCCESS!!!----");
//                    payInfoModel.setDealState((short)0);
//                }
                else {
                    _LOGGER.error("----- 当前流量充值状态Code:" + MobileObject.get("Code").toString() + "---充值失败！！！---");
                    payInfoModel.setDealState((short) 3);
                }

                int updateCount = payInfoServiceI.updatePayInfo(payInfoModel);
                _LOGGER.error("--------------当前更新是否成功! updateCount:" + updateCount + "------------------");

            }

        } catch (Exception e) {
            _LOGGER.error(e.getMessage(), e);
        }

        net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
        jsonObject.put("Code", "0000");

        _LOGGER.error("回调返回Code:" + jsonObject.toString());
        return jsonObject.toString();

    }

    //【发送post请求】 [参数:key=values&key2=values2]
    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    //【查询订单的状态】 [根据订单号，查询网迅后台充值流量订单状态]
    @RequestMapping(value = "/searchOrderStatusByOrderNoBak")
    @ResponseBody
    public String searchOrderStatus(String orderNo) {

        /**
         * 发送请求遇到json格式:{key1:values1,{key2:values2,key3:values3}}
         * 1.先将string转换为jsonobject
         * 2.jsonArray.parseObject(jsonobject)
         * 3.配置文件 jsonConfig
         * 4.jsonObject.parseObject(jsonArray.get(0).toString())
         * */

        FlowOrderController flow = new FlowOrderController();

        String Appid = WxAppId;
        String AppScret = WxAppScrect;
        String OrderID = orderNo; //订单编号
        String Echostr = getRandomString(20); //生成随机字符串[长度20]
        String Timestamp = getTimeStatem(); //时间戳

        logger.error("AppId:" + Appid + "- AppScret:" + WxAppScrect + "- OrderID:" + OrderID + "- Echostr:" + Echostr + "- Timestamp:" + Timestamp);

        /*
        * Md5(AppID + OrderID + AppSecret + Echostr + Timestamp)生成32位小写字符串
        * */
        String sign = MD5Util.MD5Encode(Appid + Echostr + AppScret + Timestamp).toLowerCase();  //加密sign签名
        logger.error("加密sign:" + sign);

        //AppID=sw1457505772&Echostr=dayUSSF0KJhFZdWJEojT&Signature=2359cc653cc40ce7ece0d6e55faf166b&OrderID=36c0c3d439734d628e18bff873bfa8d4&Timestamp=20170608145305

        //请求参数
        String param = "AppID=" + Appid + "&Echostr=" + Echostr + "&Signature=" + sign + "&OrderID=" + orderNo + "&Timestamp=" + Timestamp;
        //请求url
        String url = "http://api.wangxunit.com/v3/order/query";

        //当前请求返回数据:{"Code":"0000","Msg":null,"Count":1,"Items":[{"OrderID":"2017060407335113815138","Mobile":"18078782682","OrderType":1,"EffectType":1,"PackCode":"100500","Size":500,"RealPrice":24.0000,"OrigPrice":30.0000,"Discount":0.8000,"OrderDate":"2017-06-04 07:33:51","OrderStatus":3,"CallbackStatus":3,"Code":"29"}]}
        String jsonResult = flow.sendPost(url, param);
        logger.error("当前请求返回数据:"+jsonResult);

        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonResult);
        logger.error("将当前jsonResult转换为jsonObject类型.");

        String orderStatus = "";

        if (jsonObject.containsKey("Items")) {

            //解析配置文件
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

            //将jsonObject类型转换为jsonArray类型
            JSONArray json = JSONArray.fromObject(jsonObject.get("Items"), jsonConfig);
            _LOGGER.error("------------Json.Items.toString:" + json.get(0).toString());

            //将jsonArray数组转换为jsonObject类型
            JSONObject jsonObect_Itesm = JSONObject.parseObject(json.get(0).toString());
            _LOGGER.error("------------OrderStatus:" + jsonObect_Itesm.get("OrderStatus").toString());

            orderStatus=jsonObect_Itesm.get("OrderStatus").toString();

        }

        return orderStatus;
    }

    //网讯流量充值【客户余额查询接口】
    public  String getBalanceUser(){

        FlowOrderController flowOrderController = new FlowOrderController();

        String Appid = WxAppId; //客户接入ID
        String AppScret = WxAppScrect;
        String Echostr = getRandomString(20); //生成随机字符串
        String Timestamp = getTimeStatem(); //时间戳

        /*
        * Md5(AppID  + AppSecret + Echostr + Timestamp)生成32位小写字符串
        * */
        String sign =  MD5X.getLowerCaseMD5For32(Appid+Echostr+AppScret+Timestamp);  //加密sign签名

        //调用网讯接口V2版本查询账户余额
        String url = "http://api.wangxunit.com/v2/customer/balance";
        String params = "AppID=" + Appid + "&Echostr=" + Echostr + "&Timestamp=" + Timestamp +  "&Signature=" + sign;

        logger.error("参数:"+params);

        String resultJson =  flowOrderController.sendPost(url,params);
        logger.error("查询余额响应数据:"+resultJson);

        JSONObject jsonObject = JSONObject.parseObject(resultJson);

        String balance = String.valueOf(jsonObject.get("Balance"));
        balance = balance.substring(0,balance.indexOf('.'));

        int Balance_new = 0;
        String strs = "";

        Balance_new= Integer.parseInt(balance.replace("-",""));

        strs= Balance_new/100 + "."+Balance_new%100;

        if(balance.indexOf('-')!=-1){
            logger.error("当前余额结果为负数!");
            strs = "-"+strs;
        }

        logger.error("当前余额:"+strs);

        return strs;

    }


    //【流量预订单 [暂时停止] 】
    @RequestMapping(value = "/TestPreOrderInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String TestSenPreOrderInfo (
            @RequestParam(value = "mobile",required = true) String mobile,
            @RequestParam(value = "packCode",required = true) String packCode,
            @RequestParam(value = "number_desc",required = true) String number_desc,
            @RequestParam(value = "rechargeM",required = true) String rechargeM,
            @RequestParam(value = "dealMoney",required = true) String dealMoney,
            @RequestParam(value = "realDealMoney",required = true) String realDealMoney,
            @RequestParam(value = "redPkgId",required = true) String redPkgId,
            @RequestParam(value = "redPkgValue",required = true) String redPkgValue,
            HttpServletResponse response,
            HttpServletRequest request,
            HttpSession session
    ){

        logger.error("进入预支付方法-----流量");
        net.sf.json.JSONObject object=new net.sf.json.JSONObject();

        //从Session中获取到用户UserId和OpenId
        String userId = getUserId(session);
        String openId = (String) session.getAttribute("openId");

        //判断当前用户是否存在
        logger.error("用户OPENID:"+openId+",USERID:"+userId);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            object.put("success","0");
            object.put("errMsg","请求超时,请重新登录!");
            return object.toString();
        } else {
            session.setAttribute("userId", userId);
            session.setAttribute("openId", openId);
        }

        //防止用户短时间重复点击立即支付生成预订单
        String dateTime= (String) session.getAttribute("FlowTime");
        if(StringUtils.isEmpty(dateTime)||dateTime==null){
            session.setAttribute("FlowTime",DateFormatUtil.formateString());
        }else{
            String now=DateFormatUtil.formateString();
            if(DateFormatUtil.compare_date(dateTime,now)){
                //超过2分钟
                session.setAttribute("FlowTime",now);
            }else{
                object.put("success","0");
                object.put("errMsg","订单提交过快，请稍后再试");
                return object.toString();
            }
        }


        //获取时间戳
        String sporder_time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());

        //组合Map集合的参数params【发送流量订单需要参数】
        Map<String, String> params = new HashMap<String, String>();
        params.put("sporder_id", UUIDUtil.getUUID());
        params.put("sporder_time", sporder_time);
        //params.put("WxAppId", WxAppId);  //网讯用户id
        //params.put("WxAppScrect", WxAppScrect); //网讯用户密码password
        //params.put("ret_url", "流量充值url");  //流量充值
        //params.put("timestamp", DateFormatUtil.formateString());



        //充值参数【充值参数校验】
        params.put("mobile",mobile); //手机号码
        params.put("packCode",packCode); //流量代码
        params.put("number_desc",number_desc); //运营商名称
        if(Double.parseDouble(realDealMoney)<0D) {
            object.put("success","0");
            object.put("errMsg","当前金额不能为负数，小于0!");
            logger.error("真实付款金额不能为负数！");
            return object.toString();
        }
        params.put("dealMoney",realDealMoney); //真实付款金额
        params.put("rechargeAmount",rechargeM); //充值套餐兆数量
        //params.put("timestamp",DateFormatUtil.formateString()); //传递时间戳 ， 后台判断测试用户是否刷单行为

        //String str=realDealMoney+"liuliang"+params.get("timestamp");
        //str= MD5Util.MD5Encode(str).toUpperCase(); //加密字符串

        //将当前payMd5加密支付串存入Session中
        //session.setAttribute("payMd5",str);

        //将当前data2Map存入Session中。流量订单参数
        session.setAttribute("data2Map",params);

        logger.error("预支付接口调用参数:"+ net.sf.json.JSONObject.fromObject(params).toString());

        OrderInfoModel order=new OrderInfoModel();
        order.setId(UUIDUtil.getUUID());  //预支付OrderId
        order.setOrderNo(params.get("sporder_id")); //预支付订单OrderNo 订单号 唯一
        order.setGoodsName(number_desc+"流量充值"+rechargeM+"M"); //商品名GoodsName
        order.setCreateTime(DateFormatUtil.formateString()); //预支付订单创建时间
        order.setFromType("3");  //流量类型 3
        order.setOpenId(openId);  //用户的OpenId
        order.setRemark(mobile);  //手机号码
        if(Double.parseDouble(dealMoney)<0D){
            object.put("success","0");
            object.put("errMsg","当前金额不能为负数，小于0!");
            logger.error("当前金额不能为负数！");
            return object.toString();
        }
        order.setGoodsMoney(dealMoney); //商品金额
        order.setPayMoney(realDealMoney); //真实付款金额
        order.setIp(IPUtil.getIpAddr(request));  //获取用户发起请求的IP地址
        order.setMd5Sign(MD5Util.MD5Encode(number_desc+order.getOrderNo()+order.getPayMoney()+order.getIp()).toUpperCase()); //加密参数 MD5
        order.setOrderParams(net.sf.json.JSONObject.fromObject(params).toString()); //预支付订单参数
        order.setPayStatus("0");
        order.setRemark("");
        order.setStatus("0");
        order.setWxOrder("");
        order.setGoodsId(realDealMoney);
        this.orderService.addOrderInfo(order); //添加预支付订单 作为后期匹配依据
        logger.error("预支付订单参数:"+ net.sf.json.JSONObject.fromObject(order).toString());

        //预支付订单 返回数据
        object.put("payamount",realDealMoney); //真实付款金额
        object.put("rechargeType","liuliang"); //充值类型
        object.put("lifeType","");
        object.put("redPkgId",redPkgId);  //红包Id
        object.put("redPkgValue",redPkgValue); //红包Value
        object.put("payInfo",order.getMd5Sign()); //预支付订单MD5加密
        object.put("success","1"); //当前状态 1 ，成功
        object.put("errMsg","");   //错误信息

        return object.toString();

    }

    //【流量预支付】 【正在使用】
    @RequestMapping(value = "/prePayFlow", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String prePayCell(HttpSession session, HttpServletResponse response, HttpServletRequest request,
                             @RequestParam(value = "goodsId", required = true) String goodsId,//商品id
                             @RequestParam(value = "mobile", required = true) String mobile,   //手机号
                             @RequestParam(value = "number_desc", required = true) String number_desc,//移动 联通  电信
                             @RequestParam(value = "packCode", required = true) String packCode,//流量套餐充值代码
                             @RequestParam(value = "rechargeM", required = true) String rechargeM,//当前流量充值M数量
                             @RequestParam(value = "realMoney", required = true) String realMoney,//用户付款价格
                             @RequestParam(value = "redPkgId", required = true) String redPkgId,     //红包编号
                             @RequestParam(value = "redPkgValue", required = true) String redPkgValue    //红包金额
    ) throws ServletException, IOException {
        logger.error("进入预支付方法-----流量------>");
        net.sf.json.JSONObject object=new net.sf.json.JSONObject();

        //从Session中获取到用户UserId和OpenId
        String userId = getUserId(session);
        String openId = (String) session.getAttribute("openId");
        //黑名单 实例化获取blackUserModel对象
        BlackUserModel blackUserModel=new BlackUserModel();
        //获取时间戳
        String sporder_time = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());


        //判断当前用户是否存在
        logger.error("用户OPENID:"+openId+",USERID:"+userId);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(openId)) {
            object.put("success","0");
            object.put("errMsg","请求超时,请重新登录!");
            return object.toString();
        } else {
            session.setAttribute("userId", userId);
            session.setAttribute("openId", openId);
        }

        //防止订单提交过快[防止用户多次重复提交立即支付请求]
        String dateTime = (String) session.getAttribute("FlowTime");
        if (StringUtils.isEmpty(dateTime) || dateTime == null) {
            session.setAttribute("FlowTime", DateFormatUtil.formateString());
        } else {
            String now = DateFormatUtil.formateString();
            if (DateFormatUtil.compare_date(dateTime, now)) {
                //超过2分钟
                session.setAttribute("FlowTime", now);
            } else {
                object.put("success", "0");
                object.put("errMsg", "订单提交过快，请稍后再试");
                return object.toString();
            }
        }

        //根据提交商品编号，从数据库取出当前数据进行认证匹配
        GoodsListModel goodsListModel = new GoodsListModel();
        goodsListModel.setId(goodsId);
        List<GoodsListModel> goodsListModels = this.goodsListService.queryGoodsList(goodsListModel);
        logger.error("根据goodId:"+goodsId+" ，查询当前商品id下是否存在商品于数据库中Count:"+goodsListModels.size());
        if (goodsListModels.size() == 0) {
            object.put("success", "0");
            object.put("errMsg", "商品数据异常");
            logger.error("没有查询到当前goodsId的商品数据从数据库中");
            return object.toString();
        } else {
            goodsListModel = goodsListModels.get(0);
            logger.error("存在当前goodsId的商品于数据库中，现在与数据库商品的金额["+goodsListModel.getRealPrice()+"]和传入金额["+realMoney+"]进行匹配。");
            if (!goodsListModel.getRealPrice().equals(realMoney)){
                logger.error("当前商品金额与数据库商品金额不匹配，将此用户拉入黑名单中。");
                if(StringUtils.isEmpty(number_desc.trim())){
                    number_desc="未知用户";
                }
                blackUserModel.setOpenId(openId);
                blackUserModel.setUserId(userId);
                blackUserModel.setCreateTime(DateFormatUtil.formateString());
                blackUserModel.setIp(IPUtil.getIpAddr(request));
                blackUserModel.setRechargeDesc("商品金额与支付金额不符");
                blackUserModel.setRechargeName(number_desc+"流量充值"+rechargeM+"M--"+goodsListModel.getRealPrice()+"元");
                blackUserModel.setRechargeNum(mobile);
                blackUserModel.setRechargePrice(realMoney);
                //判断当前用户是否存在黑名单中，不存在则添加当前用户进入黑名单 [addBlackUser方法在下]
                addBlackUser(blackUserModel);

                object.put("success", "0");
                object.put("errMsg", "商品数据异常");
                logger.error("当前支付金额与数据库中商品金额不匹配,订单异常!");
                return object.toString();
            }
            goodsListModel.setId(goodsId);
            goodsListModel.setRealPrice(realMoney);
            if(!(makeGoodsMd5Str(goodsListModels.get(0)).equals(makeGoodsMd5Str(goodsListModel)))){
                //if (!goodsListModel.getRealPrice().equals(realMoney)){  // 感觉此处校验金额 不恰当。因为此方法之上已经校验了金额匹配，如果金额不正确是不会进入到当前方法中的。此处假设md5校验不正确，而金额校验正确。不会执行黑名单添加操作

                    blackUserModel.setOpenId(openId);
                    blackUserModel.setUserId(userId);
                    blackUserModel.setCreateTime(DateFormatUtil.formateString());
                    blackUserModel.setIp(IPUtil.getIpAddr(request));
                    blackUserModel.setRechargeDesc("商品MD5与支付商品MD5不符");
                    blackUserModel.setRechargeName(number_desc+"流量充值"+rechargeM+"M--"+goodsListModel.getRealPrice()+"元");
                    blackUserModel.setRechargeNum(mobile);
                    blackUserModel.setRechargePrice(realMoney);
                    addBlackUser(blackUserModel);

                    object.put("success", "0");
                    object.put("errMsg", "商品数据异常");
                    logger.error("当前查询出goodsInfoModel做MD5加密校验,出现异常!");
                    return object.toString();
                //}
            }
            //从数据库goodsInfo取出数据，按照规则定义packCode，和传入的packCode进行匹配规则校验
            String taocan =goodsListModel.getGoodsPrice(); //[数据库中的goodsPrice代表流量的充值M数量]
            logger.error("数据库中取出当前goodsId下的goodsPrice[流量充值套餐M数量]:"+taocan);
            String new_taocan = taocan.length()>3?(taocan):(taocan.length()==3?("0"+taocan):("00"+taocan));
            new_taocan="10"+new_taocan;
            logger.error("获取当前组合packCode_get从数据库中取出数据按规则组合:"+new_taocan);

            if(!new_taocan.equals(packCode)){
                object.put("success", "0");
                object.put("errMsg", "商品数据packCode异常");
                logger.error("当前充值商品Id下充值packCode["+new_taocan+"]和输入的packCode["+packCode+"]不一致!");
                return object.toString();
            }

        }

        //组合Map集合的参数params[封装参数data2Map,流量充值到时候从当前data2Map中取出需要到参数数据]
        Map<String, String> params = new HashMap<String, String>();
        params.put("sporder_id", UUIDUtil.getUUID());
        params.put("sporder_time", sporder_time);
        params.put("game_userid", mobile);
        params.put("mobile", mobile);
        params.put("rechargeM", rechargeM);
        params.put("packCode",packCode);
        params.put("realMoney",goodsListModel.getRealPrice());
        params.put("number_desc",number_desc);
        params.put("timestamp", DateFormatUtil.formateString());
        session.setAttribute("data2Map",params);//将参数存入session中 data2Map

        logger.error("预支付接口调用参数:"+ net.sf.json.JSONObject.fromObject(params).toString());

        if(Double.parseDouble(realMoney)<0D){
            object.put("success","0");
            object.put("errMsg","当前金额不能为负数，小于0!");
            logger.error("当前金额不能为负数！");
            return object.toString();
        }

        //以上校验完毕，将此订单信息存入预订单表，预订单表中数据作为初始订单号后期支付与当前订单进行校验比较
        OrderInfoModel order=new OrderInfoModel();
        order.setId(UUIDUtil.getUUID());  //预支付OrderId
        order.setOrderNo(params.get("sporder_id")); //预支付订单OrderNo 订单号 唯一
        order.setGoodsName(number_desc+"流量充值"+goodsListModel.getGoodsPrice()+"M"); //商品名GoodsName
        order.setCreateTime(DateFormatUtil.formateString()); //预支付订单创建时间
        order.setFromType("3");  //流量类型 3
        order.setOpenId(openId);  //用户的OpenId
        order.setGoodsMoney(goodsListModel.getRealPrice()); //商品金额
        order.setPayMoney(goodsListModel.getRealPrice()); //真实付款金额
        order.setRemark(mobile); //手机号码
        order.setGoodsId(goodsId); //商品Id
        order.setIp(IPUtil.getIpAddr(request));  //获取用户发起请求的IP地址
        order.setMd5Sign(MD5Util.MD5Encode(packCode+mobile+number_desc+order.getOrderNo()+order.getPayMoney()+order.getIp()).toUpperCase()); //加密参数 MD5
        order.setOrderParams(net.sf.json.JSONObject.fromObject(params).toString()); //预支付订单参数
        order.setPayStatus("0"); //支付状态 0 [默认]
        order.setStatus("0");  //充值状态 0 [默认]
        order.setWxOrder(""); //微信订单号

        this.orderService.addOrderInfo(order); //添加预支付订单 作为后期匹配依据
        logger.error("预支付订单参数:"+ net.sf.json.JSONObject.fromObject(order).toString());

        //预支付订单 返回数据
        object.put("payamount",goodsListModel.getRealPrice()); //真实付款金额
        object.put("rechargeType","liuliang"); //充值类型
        object.put("lifeType","");
        object.put("redPkgId",redPkgId);  //红包Id
        object.put("redPkgValue",redPkgValue); //红包Value
        object.put("payInfo",order.getMd5Sign()); //预支付订单MD5加密
        object.put("success","1"); //当前状态 1 ，成功
        object.put("errMsg","");   //错误信息


        return object.toString();
    }



    //发起请求，订购流量 【流量订购】
    @RequestMapping(value = "/wxFlowController")
    @ResponseBody
    public String sendWXController(
           @RequestParam(value = "PackCode",required = true) String PackCode ,
           @RequestParam(value = "Mobile",required = true) String Mobile ,
           @RequestParam(value = "OrderId",required = true) String OrderId
    ){
                String  strs = "";

                strs = sendOrder(PackCode,Mobile,OrderId);

                if(strs.equals("RechargeSuccess")){
                    strs="true";
                    logger.error("当前流量订购成功!");

                }else{
                    strs="false";
                    logger.error("流量订购失败");

                }

                return strs;
    }


    //【查询流量订单是否充值成功】 订单审核成功之后，展示当前订单是否成功. [订单发送Code:0000 成功]
    @RequestMapping(value = "/searchOrderStatusByOrderNo")
    @ResponseBody
    public String searchPayInfoByOrderNo(
            @RequestParam(value = "orderNo",required = true)String orderNo
    ){
//        PayInfoModel payInfoModel = payInfoServiceI.searchFlowStatus(orderNo);

        OrderInfoModel orderInfoModel = payInfoServiceI.searchFlowStatus(orderNo);

        String json = JSONObject.toJSONString(orderInfoModel);

        logger.error("获取当前查询payInfo数据:"+json);
        return json;
    }


    //【添加黑名单】 [拉黑用户，恶意刷单串改商品金额]
    private void addBlackUser(BlackUserModel blackUserModel) {
        BlackUserModel blackUser=new BlackUserModel();
        blackUser.setOpenId(blackUserModel.getOpenId());
        List<BlackUserModel> blackUserModels=blackUserService.queryBlackUserList(blackUser);
        logger.error("查询当前用户是否已经添加黑名单中。。。");
        if(blackUserModels.size()==0){
            logger.error("当前用户不存在黑名单中，添加当前用户进入黑名单.");
            blackUserService.addBlackUserModel(blackUserModel);
        }
    }


    //测试Main方法
    public static void main(String[] args) {

         FlowOrderController flow = new FlowOrderController();

         //String strs =  flow.sendOrder("100010","17782359491","20170505133622");
        // http://api.wangxunit.com/v3/order/query?AppID=sw1457505772&OrderID=36c0c3d439734d628e18bff873bfa8d4&Echostr=J01oIVzbxPMbuzxcY32g&Timestamp=20170605225557&Signature=451086815339e3f7a388e8b30a2d72c9

        //测试POST发送数据接口
//        String result =    flow.sendPost("http://api.wangxunit.com/v3/order/query","AppID=sw1457505772&Echostr=dayUSSF0KJhFZdWJEojT&Signature=2359cc653cc40ce7ece0d6e55faf166b&OrderID=36c0c3d439734d628e18bff873bfa8d4&Timestamp=20170608145305");
//        System.out.println(result);

         //flow.TestUrl();  //本地测试 回执URL，测试是否正确响应数据---{Code:0000}
//        String orderStatus =  flow.searchOrderStatus("36c0c3d439734d628e18bff873bfa8d4");
//        System.out.println(orderStatus);


        //查询余额
        String result = flow.getBalanceUser();
        System.out.println(result);



    }


}

//微信支付
/*
*                           //从Session获取data2Map对象数据的集合
                            String inter = object.getString("interfaceParams");
                            JSONObject data2Map = JSONObject.parseObject(inter);
//                            Map<String,String > data2Map = (Map<String, String>) session.getAttribute("data2Map");
                            String mobile = String.valueOf(data2Map.getString("mobile"));
                            String number_desc = String.valueOf(data2Map.getString("number_desc"));
                            String rechargeAmount = String.valueOf(data2Map.getString("rechargeAmount"));
                            String packCode = String.valueOf(data2Map.getString("packCode"));
                            payInfoModel.setBusinessInfo("有礼付流量充值"); // 交易信息
                            payInfoModel.setDealRemark(mobile); //手机号码
                            payInfoModel.setDealInfo(number_desc + "流量充值" + rechargeAmount); //商品详情

                            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(payInfoModel);
                            logger.info("导出当前payInfo中的参数：" + json.toString());

                            //插入支付信息
                            logger.info("-------RechargeController----------初始化插入充值订单信息[ 有礼付流量充值 ]----------------- ]");
                            this.payInfoServiceI.insertPayInfo(payInfoModel);

                            logger.info("------RechargeController-----------发送流量订单请求  聚信流量接口-----------------");

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = sdf2.format(new Date());
                            UUID uuid = UUID.randomUUID();

                            boolean isExits = userNumRecordServiceI.isNumExist(userId, mobile);

                            if (isExits == false) {
                                logger.info("--------------- 当前userId用户  Add 历史充值流量记录 ！-------------------------");
                                //新增数据
                                userNumRecordServiceI.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
                            } else {
                                logger.info("--------------- 当前userId用户 更新充值createTime ！-------------------------");
                                //更新数据
                                userNumRecordServiceI.updateCreateTime(userId, mobile, time);
                            }


//                            String BackUrl = "http://www.amumu.xin/giftpay_wap/flow/backFlowUrl_2.htm";
                            String BackUrl = "http://www.linkgift.cn/giftpay_wap/flow/backFlowUrl_2.htm";
                            //www.linkgift.cn 正式地址

                            logger.info("---------------制作sign签名，请求流量接口 聚信api！-------------------------");
                            SortedMap<String, String> map = new TreeMap<String, String>();
                            map.put("appid", "85e0ca0d62c1debcb0fb456d51b0b167");       //应用接口辨识编号
                            map.put("mobile", mobile);      //充值手机号
                            map.put("packCode", packCode);  //充值套餐编号
                            map.put("orderId", payInfoModel.getOrderNo());    //订单号
                            map.put("backUrl", BackUrl);  //回调函数 流量充值回调URL

                            String sign = null;
                            try {
                                sign = FlowOrderController.createSign(map);
                                map.put("sign", sign);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            String strpost = net.sf.json.JSONObject.fromObject(map).toString();
                            logger.info("------------[ 请求流量订单受理结果: " + strpost + "]---------------------");

                            //跳转回调函数backUrl
                            String strs = PostOrGet.sendPost("http://api.juxinbox.com/jxapi/traffic_doTrafficOrder", strpost, null);
                            logger.info("------------[ 当前请求状态：:" + strs + " ]-------------------");

                            logger.info("----RechargeController-------判断红包是否存在[ " + redPkgId + " ]-----------");
                            if (redPkgId != null && !"".equals(redPkgId)) {
                                //更新红包状态
                                Map<String, String> redParams = new HashMap<String, String>();

                                redParams.put("openId", openId);
                                redParams.put("redpkgId", redPkgId);
                                redParams.put("state", "2");
                                logger.info("----[ openId:" + openId + " , redpkgId :" + redPkgId + " , state: " + redParams.get("state").toString() + "]-----------");
                                String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
                                logger.info("----RedPkg--------红包使用状态更新:" + updateRedPkgResult + "-----------");
                            }

*
* */


/**  余额支付
 *
 *

//从Session获取data2Map对象数据的集合
Map<String, String> data2Map = (Map<String, String>) session.getAttribute("data2Map");
    String mobile = String.valueOf(data2Map.get("mobile"));
    String number_desc = String.valueOf(data2Map.get("number_desc"));
    String rechargeAmount = String.valueOf(data2Map.get("rechargeAmount"));
    String packCode = String.valueOf(data2Map.get("packCode"));
            payInfoModel.setBusinessInfo("有礼付流量充值"); // 交易信息
                    payInfoModel.setDealRemark(mobile); //手机号码
                    payInfoModel.setDealInfo(number_desc + "流量充值" + rechargeAmount); //商品详情

                    //插入支付信息
                    logger.info("-------RechargeController----------初始化插入充值订单信息[ 有礼付流量充值 ]----------------- ]");
                    this.payInfoServiceI.insertPayInfo(payInfoModel);

                    logger.info("------RechargeController-----------发送流量订单请求 据悉聚信流量接口-----------------");

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf2.format(new Date());
                    UUID uuid = UUID.randomUUID();

                    boolean isExits = userNumRecordServiceI.isNumExist(userId, mobile);

                    if (isExits == false) {
                    logger.info("--------------- 当前userId用户："+userId+"  Add 历史充值流量记录 ！-------------------------");
                    //新增数据
                    userNumRecordServiceI.addUserNumRecordByFlow(uuid.toString(), 3, mobile, userId, number_desc, time);
                    } else {
                    logger.info("--------------- 当前userId用户："+userId+"  更新充值createTime ！-------------------------");
                    //更新数据
                    userNumRecordServiceI.updateCreateTime(userId, mobile, time);
                    }

//            String BackUrl = "http://www.amumu.xin/giftpay_wap/flow/backFlowUrl_2.htm";
                    String BackUrl = "http://wwww.linkgift.cn/giftpay_wap/flow/backFlowUrl_2.htm";
                    logger.info("---------------制作sign签名，请求流量接口 聚信api！-------------------------");
                    SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appid", "85e0ca0d62c1debcb0fb456d51b0b167");       //应用接口辨识编号
        map.put("mobile", mobile);      //充值手机号
        map.put("packCode", packCode);  //充值套餐编号
        map.put("orderId", payInfoModel.getOrderNo());    //订单号
        map.put("backUrl", BackUrl);  //回调函数 流量充值回调URL

        String sign = null;
        try {
        sign = FlowOrderController.createSign(map);
        map.put("sign", sign);
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        }

        String strpost = net.sf.json.JSONObject.fromObject(map).toString();
        logger.info("------------[ 请求流量订单发送参数: " + strpost + "]---------------------");


        //跳转回调函数backUrl
        String strs = PostOrGet.sendPost("http://api.juxinbox.com/jxapi/traffic_doTrafficOrder", strpost, null);
        logger.info("------------[ 当前请求结果：:" + strs + " ]-------------------");

        //执行扣余额
        balanceModel.setChangeTime(sdf2.format(new Date()));
        balanceModel.setBalanceNumber(balanceModels.get(0).getBalanceNumber() - payInfoModel.getDealRealMoney());
        int result = this.balanceServiceI.updateBalance(balanceModel);//更新余额表
        logger.info("----Balance表-----余额更新状态result:" + result + "-----------");


        logger.info("----RechargeController-------判断红包是否存在[ " + redPkgId + " ]-----------");
        if (redPkgId != null && !"".equals(redPkgId)) {
        //更新红包状态j
        Map<String, String> redParams = new HashMap<String, String>();

        redParams.put("openId", openId);
        redParams.put("redpkgId", redPkgId);
        redParams.put("state", "2");
        logger.info("----[ openId:" + openId + " , redpkgId :" + redPkgId + " , state: " + redParams.get("state").toString() + "]-----------");
        String updateRedPkgResult = HttpUtil.sendPost(this.updateRedOtherUrl, "utf-8", redParams);
        logger.info("----RedPkg--------红包使用状态更新:" + updateRedPkgResult + "-----------");
        }
 * */

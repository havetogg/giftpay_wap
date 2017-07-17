package org.jumutang.giftpay.tools;

import net.sf.json.JSONObject;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.model.WithdrawModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 10:40 2017/6/2
 * @Modified By:
 */
public class TemplateMessageUtil {

    /**
     * 调用接口地址
     */
    private interface IWxapiURL {
        String URL_ACCESS_TOCKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        String SEND_template = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    }

    private interface IInvalidListener {
        JSONObject handle() throws Exception;
    }

    private static final String REQUEST_ENCODE = "UTF-8";
    //region 错误码对应中文解释，造福广大码农❤
    private static Map<String, String> code_info = null;

    public static List<String> notifyManager = null;

    static {
        code_info = new HashMap<String, String>();
        code_info.put("-1", "系统繁忙，此时请开发者稍候再试");
        code_info.put("0", "请求成功");
        code_info.put("40001", "获取access_token时AppSecret错误，或者access_token无效。请开发者认真比对AppSecret的正确性，或查看是否正在为恰当的公众号调用接口");
        code_info.put("40002", "不合法的凭证类型");
        code_info.put("40003", "不合法的OpenID，请开发者确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID");
        code_info.put("40004", "不合法的媒体文件类型");
        code_info.put("40005", "不合法的文件类型");
        code_info.put("40006", "不合法的文件大小");
        code_info.put("40007", "不合法的媒体文件id");
        code_info.put("40008", "不合法的消息类型");
        code_info.put("40009", "不合法的图片文件大小");
        code_info.put("40010", "不合法的语音文件大小");
        code_info.put("40011", "不合法的视频文件大小");
        code_info.put("40012", "不合法的缩略图文件大小");
        code_info.put("40013", "不合法的AppID，请开发者检查AppID的正确性，避免异常字符，注意大小写");
        code_info.put("40014", "不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口");
        code_info.put("40015", "不合法的菜单类型");
        code_info.put("40016", "不合法的按钮个数");
        code_info.put("40017", "不合法的按钮个数");
        code_info.put("40018", "不合法的按钮名字长度");
        code_info.put("40019", "不合法的按钮KEY长度");
        code_info.put("40020", "不合法的按钮URL长度");
        code_info.put("40021", "不合法的菜单版本号");
        code_info.put("40022", "不合法的子菜单级数");
        code_info.put("40023", "不合法的子菜单按钮个数");
        code_info.put("40024", "不合法的子菜单按钮类型");
        code_info.put("40025", "不合法的子菜单按钮名字长度");
        code_info.put("40026", "不合法的子菜单按钮KEY长度");
        code_info.put("40027", "不合法的子菜单按钮URL长度");
        code_info.put("40028", "不合法的自定义菜单使用用户");
        code_info.put("40029", "不合法的oauth_code");
        code_info.put("40030", "不合法的refresh_token");
        code_info.put("40031", "不合法的openid列表");
        code_info.put("40032", "不合法的openid列表长度");
        code_info.put("40033", "不合法的请求字符，不能包含\\uxxxx格式的字符");
        code_info.put("40035", "不合法的参数");
        code_info.put("40038", "不合法的请求格式");
        code_info.put("40039", "不合法的URL长度");
        code_info.put("40050", "不合法的分组id");
        code_info.put("40051", "分组名字不合法");
        code_info.put("41001", "缺少access_token参数");
        code_info.put("41002", "缺少appid参数");
        code_info.put("41003", "缺少refresh_token参数");
        code_info.put("41004", "缺少secret参数");
        code_info.put("41005", "缺少多媒体文件数据");
        code_info.put("41006", "缺少media_id参数");
        code_info.put("41007", "缺少子菜单数据");
        code_info.put("41008", "缺少oauth code");
        code_info.put("41009", "缺少openid");
        code_info.put("42001", "access_token超时，请检查access_token的有效期，请参考基础支持-获取access_token中，对access_token的详细机制说明");
        code_info.put("42002", "refresh_token超时");
        code_info.put("42003", "oauth_code超时");
        code_info.put("43001", "需要GET请求");
        code_info.put("43002", "需要POST请求");
        code_info.put("43003", "需要HTTPS请求");
        code_info.put("43004", "需要接收者关注");
        code_info.put("43005", "需要好友关系");
        code_info.put("44001", "多媒体文件为空");
        code_info.put("44002", "POST的数据包为空");
        code_info.put("44003", "图文消息内容为空");
        code_info.put("44004", "文本消息内容为空");
        code_info.put("45001", "多媒体文件大小超过限制");
        code_info.put("45002", "消息内容超过限制");
        code_info.put("45003", "标题字段超过限制");
        code_info.put("45004", "描述字段超过限制");
        code_info.put("45005", "链接字段超过限制");
        code_info.put("45006", "图片链接字段超过限制");
        code_info.put("45007", "语音播放时间超过限制");
        code_info.put("45008", "图文消息超过限制");
        code_info.put("45009", "接口调用超过限制");
        code_info.put("45010", "创建菜单个数超过限制");
        code_info.put("45015", "回复时间超过限制");
        code_info.put("45016", "系统分组，不允许修改");
        code_info.put("45017", "分组名字过长");
        code_info.put("45018", "分组数量超过上限");
        code_info.put("46001", "不存在媒体数据");
        code_info.put("46002", "不存在的菜单版本");
        code_info.put("46003", "不存在的菜单数据");
        code_info.put("46004", "不存在的用户");
        code_info.put("47001", "解析JSON/XML内容错误");
        code_info.put("48001", "api功能未授权，请确认公众号已获得该接口，可以在公众平台官网-开发者中心页中查看接口权限");
        code_info.put("50001", "用户未授权该api");
        code_info.put("61451", "参数错误(invalid parameter)");
        code_info.put("61452", "无效客服账号(invalid kf_account)");
        code_info.put("61453", "客服帐号已存在(kf_account exsited)");
        code_info.put("61454", "客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length)");
        code_info.put("61455", "客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account)");
        code_info.put("61456", "客服帐号个数超过限制(10个客服账号)(kf_account count exceeded)");
        code_info.put("61457", "无效头像文件类型(invalid file type)");
        code_info.put("61450", "系统错误(system error)");
        code_info.put("61500", "日期格式错误");
        code_info.put("61501", "日期范围错误");
        notifyManager  = new ArrayList<>();
        notifyManager.add("o4FD4v5CJ6Rv3KrGjc40B_gB9sj8");
        notifyManager.add("o4FD4v_VSL8r87LR713h4s_ywo1Y");
    }
    //endregion

    //公众号appid
    private static final String appId = "wxee9d9ad775f75311";
    //private static final String testAppId = "wx3b30b09d7587ec94";
    //公众号appSecret
    private static final String appSecret = "ed0d0d8cd9187b71a1ad951414d3a5a2";
    //private static final String testAppSecret = "10e159057de1f226804fe850a8ec16ae";

    //加油订单支付成功通知模板id
    private static final String paySuccess = "DbXEezsdR-3OG_jgh5lIV_SVUOYsW3S8Lh1x6P3PQTQ";

    //实时交易提醒
    private static final String paySuccessByRecharge = "zM68eh5Qfgb-Db5clgBLEcQ8551fDhKfSSSy-E-_2H0";

    //充值成功通知
    private static final String returnSuccess = "pl4Wy071ClapaIrdxZh0xDUidnqfb0Wc0CUekT7Szkk";

    //提现申请通知
    private static final String withdrawOrder = "cPz9L1FHP2dXTyyc8t_NTes9Iqeq222k7mIrxm6xoLQ";

    //加油跳转url
    private static final String paySuccessUrl = "www.linkgift.cn/giftpay_wap/giftpay/oilRecharge/wxOilByInfo.htm";

    //提现跳转url
    private static final String withdrawUrl = "www.linkgift.cn/giftpay_wap/giftpay/withdraw/login.htm?withdrawId=%s";
    private static final String testWithdrawUrl = "www.linkgift.cn/giftpay_wap_test/giftpay/withdraw/login.htm?withdrawId=%s";




    private static String access_token = null;
    private static long accesstocken_timestamp = 0;
    private static long accesstocken_expiresIn = 0;

    /**
     * 获取access_tocken
     * @throws Exception
     */
    private static void getAccessToken() throws Exception {
        String url = String.format(TemplateMessageUtil.IWxapiURL.URL_ACCESS_TOCKEN, appId, appSecret);
        //String url = String.format(TemplateMessageUtil.IWxapiURL.URL_ACCESS_TOCKEN, testAppId, testAppSecret);
        JSONObject respJson = requestWXAPI(url, null);
        access_token = respJson.getString("access_token");
        accesstocken_timestamp = System.currentTimeMillis();
        accesstocken_expiresIn = respJson.getInt("expires_in") * 1000;
    }
    //加油服务购买成功
    public static void sendOilPaySuccess(String openId,String itemName,String payAmount) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccess);
        postMap.put("url",paySuccessUrl);

        JSONObject first = new JSONObject();
        first.put("value","您好，您的订单已支付成功！");
        first.put("color","#173177");

        JSONObject orderMoneySum = new JSONObject();
        orderMoneySum.put("value",payAmount+"元");
        orderMoneySum.put("color","#173177");

        JSONObject orderProductName = new JSONObject();
        orderProductName.put("value",itemName);
        orderProductName.put("color","#173177");

        JSONObject Remark = new JSONObject();
        Remark.put("value","感谢您的光临");
        Remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("orderMoneySum",orderMoneySum.toString());
        data.put("orderProductName",orderProductName.toString());
        data.put("Remark",Remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }
    //加油服务推送消息
    public static void sendOilPayNotify(String orderNo, String phone, PhoneModel phoneModel, String endTime, String itemName, String payAmount, int entryType) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        for(String notifyOpenId:notifyManager){
            String url = String.format(IWxapiURL.SEND_template, access_token);
            JSONObject postMap = new JSONObject();
            postMap.put("touser",notifyOpenId);
            postMap.put("template_id",paySuccessByRecharge);
            //postMap.put("url",successUrl);

            JSONObject first = new JSONObject();
            first.put("value",orderNo+"订单已成功付款,手机号为:"+phone+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
            first.put("color","#173177");

            JSONObject tradeDateTime = new JSONObject();
            tradeDateTime.put("value",endTime);
            tradeDateTime.put("color","#173177");

            JSONObject tradeType = new JSONObject();
            if(entryType == 0){
                tradeType.put("value",itemName);
            }else if(entryType == 1){
                tradeType.put("value",itemName+"-光大途径");
            }else if(entryType == 2){
                tradeType.put("value",itemName+"-招行途径");
            }
            tradeType.put("color","#173177");

            JSONObject curAmount = new JSONObject();
            curAmount.put("value",payAmount+"元");
            curAmount.put("color","#173177");

            JSONObject remark = new JSONObject();
            remark.put("value","请尽快审核");
            remark.put("color","#173177");

            JSONObject data = new JSONObject();
            data.put("first",first.toString());
            data.put("tradeDateTime",tradeDateTime.toString());
            data.put("tradeType",tradeType.toString());
            data.put("curAmount",curAmount.toString());
            //data.put("remark",remark.toString());
            postMap.put("data",data.toString());
            String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
        }

    }
    //加油服务每月还款通知
    public static void returnSuccess(String openId,String returnAmount,String phone) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",returnSuccess);
        postMap.put("url",paySuccessUrl);

        JSONObject first = new JSONObject();
        first.put("value","每月加油红包已充值到您的有礼付账户");
        first.put("color","#173177");

        JSONObject accountType = new JSONObject();
        accountType.put("value","手机号");
        accountType.put("color","#173177");

        JSONObject account = new JSONObject();
        account.put("value",phone);
        account.put("color","#173177");

        JSONObject amount = new JSONObject();
        amount.put("value",returnAmount+"元");
        amount.put("color","#173177");

        JSONObject result = new JSONObject();
        result.put("value","充值成功");
        result.put("color","#173177");

        JSONObject remark = new JSONObject();
        remark.put("value","感谢您的光临");
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("accountType",accountType.toString());
        data.put("account",account.toString());
        data.put("amount",amount.toString());
        data.put("result",result.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String resultStr = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }

    public static void sendPaySuccessByPhoneRecharge(String orderNo,String goodsMoney,String dateTime,String phone,PhoneModel phoneModel,String openId,String payAmount,String successUrl) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccessByRecharge);
        postMap.put("url",successUrl);

        JSONObject first = new JSONObject();
        first.put("value",orderNo+"订单已成功付款,手机号为:"+phone+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
        first.put("color","#173177");

        JSONObject tradeDateTime = new JSONObject();
        tradeDateTime.put("value",dateTime);
        tradeDateTime.put("color","#173177");

        JSONObject tradeType = new JSONObject();
        tradeType.put("value","手机话费充值");
        tradeType.put("color","#173177");

        JSONObject curAmount = new JSONObject();
        curAmount.put("value","商品订单金额:"+goodsMoney+",实际支付金额:"+payAmount);
        curAmount.put("color","#173177");

        JSONObject remark = new JSONObject();
        remark.put("value","请尽快审核");
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("tradeDateTime",tradeDateTime.toString());
        data.put("tradeType",tradeType.toString());
        data.put("curAmount",curAmount.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }
    public static void sendPaySuccessByBalanceRecharge(String orderNo,String goodsMoney,String dateTime,String phone,PhoneModel phoneModel,String openId,String payAmount,String successUrl) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccessByRecharge);
        postMap.put("url",successUrl);

        JSONObject first = new JSONObject();
        first.put("value",orderNo+"订单已成功付款,手机号为:"+phone+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
        first.put("color","#173177");

        JSONObject tradeDateTime = new JSONObject();
        tradeDateTime.put("value",dateTime);
        tradeDateTime.put("color","#173177");

        JSONObject tradeType = new JSONObject();
        tradeType.put("value","用户余额充值");
        tradeType.put("color","#173177");

        JSONObject curAmount = new JSONObject();
        curAmount.put("value","用户充值金额:"+goodsMoney+",实际支付金额:"+payAmount);
        curAmount.put("color","#173177");

        JSONObject remark = new JSONObject();
        remark.put("value","请尽快审核");
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("tradeDateTime",tradeDateTime.toString());
        data.put("tradeType",tradeType.toString());
        data.put("curAmount",curAmount.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }
    public static void sendPaySuccessByPhoneRecharge(String orderNo,String goodsMoney,String dateTime,String phone,PhoneModel phoneModel,String openId,String payAmount,String successUrl,int type) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccessByRecharge);
        postMap.put("url",successUrl);

        JSONObject first = new JSONObject();
        first.put("value",orderNo+"订单已成功付款,手机号为:"+phone+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
        first.put("color","#173177");

        JSONObject tradeDateTime = new JSONObject();
        tradeDateTime.put("value",dateTime);
        tradeDateTime.put("color","#173177");

        JSONObject tradeType = new JSONObject();
        tradeType.put("value","话费充值");
        tradeType.put("color","#173177");

        JSONObject curAmount = new JSONObject();
        curAmount.put("value","商品订单金额:"+goodsMoney+",实际支付金额:"+payAmount);
        curAmount.put("color","#173177");
        JSONObject remark = new JSONObject();
        if(type == 0){
            remark.put("value","请尽快审核");
        }else if(type ==1){
            remark.put("value","有礼付九五折加油余额充值无需审核");
        }
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("tradeDateTime",tradeDateTime.toString());
        data.put("tradeType",tradeType.toString());
        data.put("curAmount",curAmount.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }
    public static void sendPaySuccessByOilRecharge(String orderNo,String goodsMoney,String dateTime,String phone,String openId,String payAmount,String successUrl,int type) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccessByRecharge);
        postMap.put("url",successUrl);

        JSONObject first = new JSONObject();
        first.put("value",orderNo+"订单已成功付款,油卡卡号为:"+phone);
        first.put("color","#173177");

        JSONObject tradeDateTime = new JSONObject();
        tradeDateTime.put("value",dateTime);
        tradeDateTime.put("color","#173177");

        JSONObject tradeType = new JSONObject();
        tradeType.put("value","加油卡充值");
        tradeType.put("color","#173177");

        JSONObject curAmount = new JSONObject();
        curAmount.put("value","商品订单金额:"+goodsMoney+",实际支付金额:"+payAmount);
        curAmount.put("color","#173177");

        JSONObject remark = new JSONObject();
        if(type == 0){
            remark.put("value","请尽快审核");
        }else if(type ==1){
            remark.put("value","有礼付九五折加油余额充值无需审核");
        }
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("tradeDateTime",tradeDateTime.toString());
        data.put("tradeType",tradeType.toString());
        data.put("curAmount",curAmount.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }
    public static void sendPaySuccessByPhoneFlowRecharge(String orderNo,String goodsMoney,String dateTime,String phone,PhoneModel phoneModel,String openId,String payAmount,String successUrl) throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        String url = String.format(IWxapiURL.SEND_template, access_token);
        JSONObject postMap = new JSONObject();
        postMap.put("touser",openId);
        postMap.put("template_id",paySuccessByRecharge);
        postMap.put("url",successUrl);

        JSONObject first = new JSONObject();
        first.put("value",orderNo+"订单已成功付款,手机号为:"+phone+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
        first.put("color","#173177");

        JSONObject tradeDateTime = new JSONObject();
        tradeDateTime.put("value",dateTime);
        tradeDateTime.put("color","#173177");

        JSONObject tradeType = new JSONObject();
        tradeType.put("value","手机流量充值");
        tradeType.put("color","#173177");

        JSONObject curAmount = new JSONObject();
        curAmount.put("value","商品订单金额:"+goodsMoney+",实际支付金额:"+payAmount);
        curAmount.put("color","#173177");

        JSONObject remark = new JSONObject();
        remark.put("value","请尽快审核");
        remark.put("color","#173177");

        JSONObject data = new JSONObject();
        data.put("first",first.toString());
        data.put("tradeDateTime",tradeDateTime.toString());
        data.put("tradeType",tradeType.toString());
        data.put("curAmount",curAmount.toString());
        data.put("remark",remark.toString());
        postMap.put("data",data.toString());
        String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
    }

    //发送提现申请
    public static void sendWithdrawOrder(WithdrawModel withdrawModel,PhoneModel phoneModel)throws Exception{
        //获取access_token
        if (null == access_token || System.currentTimeMillis() >= accesstocken_timestamp + accesstocken_expiresIn) {
            getAccessToken();
            //刷新access_tocken后ticket跟着立即刷新
        }
        for(String notifyOpenId:notifyManager){
            String url = String.format(IWxapiURL.SEND_template, access_token);
            JSONObject postMap = new JSONObject();
            postMap.put("touser",notifyOpenId);
            postMap.put("template_id",withdrawOrder);
            postMap.put("url",String.format(withdrawUrl,withdrawModel.getWithdrawId()));
            //postMap.put("url",String.format(testWithdrawUrl,withdrawModel.getWithdrawId()));

            JSONObject first = new JSONObject();
            first.put("value","手机号为:"+withdrawModel.getPhone()+"的用户申请提现"+" "+phoneModel.getProvince()+phoneModel.getCity()+" "+phoneModel.getCard());
            first.put("color","#173177");

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value",withdrawModel.getCreateTime());
            keyword1.put("color","#173177");

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value",withdrawModel.getWithdrawAmount()+"元");
            keyword2.put("color","#173177");

            JSONObject remark = new JSONObject();
            remark.put("value","请尽快审核");
            remark.put("color","#173177");

            JSONObject data = new JSONObject();
            data.put("first",first.toString());
            data.put("keyword1",keyword1);
            data.put("keyword2",keyword2);
            data.put("remark",remark);
            postMap.put("data",data.toString());
            String result = HttpUtil.sendPost(url,"utf-8",postMap.toString());
        }
    }


    /**
     * 访问目标url并返回响应内容
     * @param strURL 目标url
     * @return 响应的内容
     */
    private static JSONObject requestWXAPI(String strURL, TemplateMessageUtil.IInvalidListener listener) throws Exception {
        InputStream inputStream = null;
        JSONObject respJson = null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置通信头
            connection.setRequestProperty("content-type", "textml; charset="+TemplateMessageUtil.REQUEST_ENCODE);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            //开始读取响应内容
            if (200 == connection.getResponseCode()) {
                //读取响应数据
                inputStream = connection.getInputStream();
                ByteArrayOutputStream memory = new ByteArrayOutputStream();
                // 读取数据
                byte[] buffer = new byte[2048];
                int readSize = inputStream.read(buffer, 0, 2048);
                while(readSize > 0) {
                    memory.write(buffer, 0 ,readSize);
                    readSize = inputStream.read(buffer, 0, 2048);
                }
                String respStr = new String(memory.toByteArray(), TemplateMessageUtil.REQUEST_ENCODE);
                respJson = JSONObject.fromObject(respStr);

                String error_code = respJson.containsKey("errcode") ? respJson.getString("errcode") : null;
                //错误信息处理
                if(null != error_code && !"0".equals(error_code)){
                    //如果返回码为40014（不合法的access_token）并且有对应处理监听则处理，否则报错
                    if ("40014".equals(error_code) && null != listener) {
                        respJson = listener.handle();
                    } else {
                        throw new Exception("调用微信API错误！返回信息：error_code:" + error_code + ", error_msg:"+code_info.get(error_code));
                    }
                } else {
                    return respJson;
                }
            } else {
                throw new Exception("访问微信时发生错误！错误信息：respCode:"+connection.getResponseCode()+", respMsg:"+connection.getResponseMessage());
            }
        } finally {
            if (null != inputStream) inputStream.close();
        }
        return respJson;
    }

    public static void main(String[] args) throws Exception{
        //TemplateMessageUtil.returnSuccess("o4FD4v_VSL8r87LR713h4s_ywo1Y","250","18094233235");
        //TemplateMessageUtil.sendOilPaySuccess("o4FD4v_VSL8r87LR713h4s_ywo1Y","有礼付易加油","2850");
        //SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //TemplateMessageUtil.sendOilPayNotify("456789","123",dateTimeFormat.format(new Date()),"有礼付易加油","2850",1);
        /*String whiteOpenId="o4FD4v5CJ6Rv3KrGjc40B_gB9sj8,o4FD4v6oEoNMA9Fbggg4mS-HKg5U,o4FD4v_VSL8r87LR713h4s_ywo1Y";
        String[] whitelist=whiteOpenId.split(",");
        for(String str:whitelist){
            TemplateMessageUtil.sendPaySuccessByOilRecharge("413dc57ce2e5495b80fc6a0467fed5dd","100",
                    "2017-06-19 20:25:04","1000113200016737817",str,"99.8","",1);
        }*/
    }
}

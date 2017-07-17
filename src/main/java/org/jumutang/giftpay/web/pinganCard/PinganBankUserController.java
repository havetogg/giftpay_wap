package org.jumutang.giftpay.web.pinganCard;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.dao.PaBankLoanDao;
import org.jumutang.giftpay.entity.PaBankLoanModel;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.PaBankLoanService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.tools.GetRequestIpAddress;
import org.jumutang.giftpay.tools.HttpUtil;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

@Controller
@RequestMapping(value = "/pinganBankGo",method = {RequestMethod.GET,RequestMethod.POST})
public class PinganBankUserController extends BaseController {

    //访问路劲 http://域名地址/项目名打包名/pinganBankGo/wxLogin.htm [静默授权]

    @Autowired
    private PhoneQueryService phoneQueryService;

    @Autowired
    private PaBankLoanService paBankLoanService;

    GetRequestIpAddress getIpAddress = new GetRequestIpAddress();

    //淘宝 ip地址查询接口 [bug：部分ip查询不到城市，只有省份]
    public final static  String urlPath = "http://ip.taobao.com/service/getIpInfo.php";

//    //聚合数据 流量接口 appKey
//    private final String juhe_appKey = "f5a829f8b2e30e65243b611450c31bb3";
//    //聚合数据url请求
//    private final String juhe_url ="http://apis.juhe.cn/ip/ip2addr";

    @Value("#{propertyFactoryBean['juhe_flowUrl']}")
    protected String juheUrl_flow;
    @Value("#{propertyFactoryBean['juhe_flowAppKey']}")
    protected String juhe_flowAppKey;


    //有礼付用户静默授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLogin")
    public void wxRedPkgBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("--pankBankCard---进入静默授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","")
                +"pinganBankGo/init.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLoginByHands")
    public void wxRedPkgByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("---pinganBankController---进入手动授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "pinganBankGo/init.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户信息初始化
    @RequestMapping(value = "/init")
    public void initUserInfo(
            HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) throws IOException {
        logger.info("---giftpay_UserController--init---进入获取用户信息方法");
        String openId = request.getParameter("openid");

        String targetUrl="";
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"pinganBankGo/wxLoginByHands.htm";
        }else if (openId.length()<=4){
            logger.error("静默授权获取openId["+openId+"],长度不正确。跳转手动授权！");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"pinganBankGo/wxLoginByHands.htm";
        }else{
            session.setAttribute("openId", openId);
            logger.info("获取到当前授权用户的openId = "+openId);
            //查询是否存在当前用户数据
            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(openId);
            List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);

            String phone = "";
            String userId = "";
            if (subList.size() != 0) {
                phone = subList.get(0).getPhone();
                userId = subList.get(0).getUserId();
                session.setAttribute("userId",userId);
                session.setAttribute("phone",phone);
                logger.info("存在 当前用户,当前用户手机号码["+phone+"]!");
           }else if(subList.size() == 0){
                logger.info("--giftpay_UserController---当前openId:["+openId+"]用户不存在!");

                //生成用户主表
                UserMainModel userMainModel = new UserMainModel();
                userMainModel.setId(UUIDUtil.getUUID());
                userMainModel.setPhone("");
                userMainModel.setStatus("0");
                this.userMainService.addUserMainModel(userMainModel);
                //生成用户详细表
                subModel.setStatus("0");
                subModel.setType("2");
                subModel.setPhone("");
                subModel.setId(UUIDUtil.getUUID());
                subModel.setUserId(userMainModel.getId());
                userId = userMainModel.getId();
                subModel.setOpenId(openId);
                this.userSubService.addUserSubModel(subModel);

                session.setAttribute("userId",userId);
                session.setAttribute("phone",phone);

                logger.info("用户授权成功！获取到当前用户的openId:["+openId+"]");

            }

            targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/pingan.jsp?phone="+phone+"&openId="+openId;

        }

        /*
        * Session中传入参数:
        * openId 微信用户唯一id
        * phone 手机号码
        * third_name 渠道号码
        * */

        logger.info("giftpay_UserController--用户跳转办卡页面url地址:"+targetUrl);
        //重定向跳转链接
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //立即办卡 操作
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/doPaBankLoan")
    @ResponseBody
    public String  doBankCard(
            HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) throws IOException {

        JSONObject jsonObject = new JSONObject();

        // 手机号码是否已经存在[0,不存在;1,存在]
//        String status ="1"; //从Session中获取手机号码
         String status ="0"; //请求中获取手机号码 需要进行验证码那校验操作
        //是否检验验证码成功
        int optionStatus = 0 ;

        //手机号码
        String phone=request.getParameter("phone"); //从请求中获取手机号码
        logger.info("从request请求中获取当前手机号码为:"+phone);
        //String phone = String.valueOf(session.getAttribute("phone")); //从Session中获取手机号码
        //logger.info("从session中获取当前手机号码为:"+phone);

        //验证码 session
        String valNum = String.valueOf(session.getAttribute("valNum"));
        logger.info("从Session中获取手机验证码:"+valNum);

        //验证码 request
        String PhoneCard =request.getParameter("code");
        logger.info("请求中获取手机验证码:"+PhoneCard);

        //openId
        String openId = String.valueOf(session.getAttribute("openId"));
        logger.info("获取用户openId:"+openId);

        //UserId
        String userId =String.valueOf( session.getAttribute("userId"));
        logger.info("获取用户UserId:"+userId);

        //获取用户username
        String username = request.getParameter("username");
        logger.info("从请求request获取当前用户姓名username:"+username);

        //获取渠道码
        String third_code = request.getParameter("third_code");
        logger.info("从请求request获取当前渠道码third_code:"+third_code);

        if(openIdISNull(third_code)){
            jsonObject.put("status", "false");
            jsonObject.put("mess", "当前渠道码获取异常,重新授权登陆获取！");
            logger.info("当前渠道码获取异常!");
            return jsonObject.toString();
        }

//        if(openIdISNull(phone)){
//            phone = request.getParameter("phone");
//            logger.info("当前Session中获取手机号码为空，从页面请求request中获取用户手机号码:"+phone);
//
//            if("".equals(phone)){
//                logger.info("页面获取手机号码为空！[phone],session中获取手机号码也为空!");
//                jsonObject.put("status", "false");
//                jsonObject.put("mess", "手机号码获取异常，重新授权登陆获取！");
//                logger.info("当前手机号码获取异常!");
//                return jsonObject.toString();
//            }
//
//            logger.info("session中取出手机号码为空，从页面获取手机号码为:"+phone);
//            status ="0";
//        }

        String ipAddress = getIpAddress.getIpAddress(request);
        logger.info("获取当前请求发送的ip地址:"+ipAddress);

        //验证手机号码是否为空
        if(openIdISNull(phone)){
            logger.info("当前获取用户userId为空！");
            jsonObject.put("status", "false");
            jsonObject.put("mess", "当前手机号码输入为空!系统异常");
            logger.info("当前用户openId:【"+openId+"】，当前手机号码输入为空!重新授权登陆！");
            return jsonObject.toString();
        }


        if(openIdISNull(userId)){
            logger.info("当前获取用户userId为空！");
            jsonObject.put("status", "false");
            jsonObject.put("mess", "当前用户userId为空!系统异常");
            logger.info("当前用户openId:【"+openId+"】，未获取用户userId。重新授权登陆！");
            return jsonObject.toString();
        }

        if(openIdISNull(username)){
            logger.info("当前获取用户username为空！");
            jsonObject.put("status", "false");
            jsonObject.put("mess", "获取用户姓名为空,请重新登陆！");
            logger.info("用户姓名获取为空，系统异常!");
            return jsonObject.toString();
        }

        if(openIdISNull(openId)){
            logger.info("当前获取用户openId为空！");
            jsonObject.put("status", "false");
            jsonObject.put("mess", "当前获取用户openId为空,请重新登陆！");
            logger.info("用户姓名获取为空，系统异常!");
            return jsonObject.toString();
        }

        if(status=="0"){
            logger.info("当前用户openId[" + openId + "],手机号码不存在，准备绑卡操作.....");
            if (valNum.equals(PhoneCard)) {
                logger.info("验证码验证成功!!");
            } else {
                jsonObject.put("status", "false");
                jsonObject.put("mess", "验证码输入错误！");
                logger.info("当前验证码错误!");
                return jsonObject.toString();
            }

            logger.info("当前用户不存在手机号码openId["+openId+"],更新用户手机号码:"+phone);
            UserSubModel userSubModel = new UserSubModel();
            userSubModel.setOpenId(openId);
            userSubModel.setUserId(userId);
            userSubModel.setPhone(phone);
            userSubService.updateUserSubModel(userSubModel);

            optionStatus=1;

        }else {//绑卡操作
            logger.info("当前用户openId[" + openId + "],不需要绑定手机号.....");
            optionStatus=1;
        }

        if(optionStatus==1){
            logger.info(",验证码校验正确!.....");

            logger.info("插入当前获取手机号到t_phone表中....");
           PhoneModel phoneModel = phoneQueryService.getPhone(phone);

            PaBankLoanModel paBankLoanModel = new PaBankLoanModel();
            paBankLoanModel.setOpenid(openId);
            logger.info("查询当前openId用户是否存在paBankLoan表中......");
            ArrayList<PaBankLoanModel> list = paBankLoanService.PaBankLoanList(paBankLoanModel);
            if(list.size()>0){
                logger.info("当前用户，已存在paBankLoan表中，不添加！");
            }else{
                logger.info("当前openId["+openId+"]用户不存在paBankLoan表中，准备添加！");
                paBankLoanModel.setPhone(phone);
                paBankLoanModel.setUsername(username);
                paBankLoanModel.setThird_code(third_code);
                paBankLoanModel.setAddress(phoneModel.getProvince()+phoneModel.getCity()); //手机号码归属地
                paBankLoanModel.setIpaddress(ipAddress); //当前ip地址

                if("".equals(ipAddress) || ipAddress==null){
                    paBankLoanModel.setIpaddress_true("IpAddress LOST");
                }else{
                    paBankLoanModel.setIpaddress_true(getIpAddressTrue(ipAddress)); //当前ip地址真实用户地址
                }

                paBankLoanService.PaBankLoanAdd(paBankLoanModel);
            }

        }

        jsonObject.put("status","success");
        jsonObject.put("mess","信息添加成功!");
        logger.info("当前返回jsonobject:"+jsonObject.toString());

        return jsonObject.toString();
    }


    //查询所有数据 [贷款用户信息]
    @SuppressWarnings("deprecation")
    @RequestMapping("/searchLoanUserList")
    @ResponseBody
    public String getPaLoanUserList(HttpServletRequest request, HttpServletResponse response, HttpSession session){

        PaBankLoanModel paBankLoanModel = new PaBankLoanModel();

        String phone = request.getParameter("phone");
        String openId = request.getParameter("openid");
        String name = request.getParameter("userid");

        logger.info("查询当前贷款用户信息.........");

        if(openIdISNull(phone)){
            paBankLoanModel.setPhone(phone);
        }

        if(openIdISNull(openId)){
            paBankLoanModel.setOpenid(openId);
        }

        if(openIdISNull(name)){
            paBankLoanModel.setUsername(name);
        }
        logger.info("查询条件:phone["+phone+"]--openid["+openId+"]--username["+name+"].........");

        ArrayList<PaBankLoanModel> list = paBankLoanService.PaBankLoanList(paBankLoanModel);

        String json = JSONObject.toJSONString(list);

        logger.info("当前查询结果为:"+json);

        return json;

    }


    //更新数据库已经存在ip地址，更新真实ip地址
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/updateIpAddressTrue")
    public void updateAddressTue(){

        logger.info("update更新ip地址的真实地址信息");
        updateIpAddressTrue();

    }

    // -------------------------------------------------------------------------------------------

    //方法循环 update数据库存在ip地址用户的真实地址
    public void updateIpAddressTrue(){

        ArrayList<PaBankLoanModel> list = paBankLoanService.PaBankLoanList(null);

        for(PaBankLoanModel p:list){

            if(!"".equals(p.getIpaddress())){

                String ipadressTrue = getIpAddressTrue(p.getIpaddress());

                PaBankLoanModel p_new = new PaBankLoanModel();
                p_new.setIpaddress_true(ipadressTrue);
                p_new.setOpenid(p.getOpenid());
                paBankLoanService.PaBankLoanUpdate(p_new);
            }

        }

    }

    //根据ip地址解析真实地址
    public String getIpAddressTrue(String ipaddress){


        //发送请求返回结果
        String result = sendGet(juheUrl_flow+"?ip="+ipaddress+"&key="+juhe_flowAppKey,"utf-8");

        //返回结果转换jsonobject
        JSONObject jsonObject = JSONObject.parseObject(result);

        logger.info("当前ip地址:"+ipaddress+"--真实地址:"+jsonObject.toString());

        //解析jsonobject集合
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray json = JSONArray.fromObject(jsonObject.get("result"), jsonConfig);

        JSONObject IpAddress = JSONObject.parseObject(json.get(0).toString());

        return IpAddress.get("area").toString();

    }

    //转换编码
    public  String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed  encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    //发送请求
    public  String sendPost(String url, String param) {
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

    public  String sendGet(String url, String encode) {

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时�?
        get.setConfig(requestConfig);
        get.setHeader("Content-Type", "text/html;charset=UTF-8");

        try {
            HttpResponse httpResponse = closeableHttpClient.execute(get);
            HttpEntity responceEntity = httpResponse.getEntity();
            System.out.println("status:" + httpResponse.getStatusLine());
            return EntityUtils.toString(responceEntity, encode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                closeableHttpClient.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


}

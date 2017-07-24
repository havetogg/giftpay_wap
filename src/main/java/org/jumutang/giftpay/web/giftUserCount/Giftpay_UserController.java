package org.jumutang.giftpay.web.giftUserCount;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.IThirdUserService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

@Controller
@RequestMapping(value = "/giftUserGo",method = {RequestMethod.GET,RequestMethod.POST})
public class Giftpay_UserController extends BaseController {

    //访问路劲 http://域名地址/项目名打包名/giftUserGo/wxLogin.htm [静默授权]

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IThirdUserService iThirdUserService;

    @Autowired
    private PhoneQueryService phoneQueryService;

    //有礼付用户静默授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLogin")
    public void wxRedPkgBase(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取渠道名称
        String third_name = request.getParameter("third_name");
        logger.info("---giftpay_UserController--进入静默授权方法--当前获取渠道名称[third_name:"+third_name+"]");

        logger.error("进入静默授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","")
                +"giftUserGo/init.htm?third_name="+third_name);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLoginByHands")
    public void wxRedPkgByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("进入手动授权方法");

        // 获取渠道名称
        String third_name = request.getParameter("third_name");
        logger.info("---giftpay_UserController--进入手动授权方法--当前获取渠道名称[third_name:"+third_name+"]");

        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftUserGo/init.htm?third_name="+third_name);
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

        //获取当前用户访问渠道
        String third_name = request.getParameter("third_name");
        logger.info("当前用户访问渠道:"+third_name);
        session.setAttribute("third_name",third_name);

        String targetUrl="";
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftUserGo/wxLoginByHands.htm?third_name="+third_name;
        }else if (openId.length()<=4){
            logger.error("静默授权获取openId["+openId+"],长度不正确。跳转手动授权！");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftUserGo/wxLoginByHands.htm?third_name="+third_name;
        }else{
            session.setAttribute("openId", openId);
            logger.info("参数:openId = "+openId);

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
                targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/index.jsp?phone="+phone+"&openId="+openId+"&third_name="+third_name;
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
                targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/index.jsp?phone="+phone+"&openId="+openId;

            }

        }

        /*
        * Session中传入参数:
        * userId 用户id
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
    @RequestMapping(value = "/doBankCard")
    @ResponseBody
    public String  doBankCard(
            HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) throws IOException {

        JSONObject jsonObject = new JSONObject();

        // 手机号码是否已经存在[0,不存在;1,存在]
        String status ="1";
        //是否检验验证码成功
        int optionStatus = 0 ;

        //手机号码
        String phone = String.valueOf(session.getAttribute("phone"));
        logger.info("从session中获取当前手机号码为:"+phone);
//        if( phone.equals("")){ //原始代码
        if(openIdISNull(phone)){
            //校验手机号是否为空，从session中取出，如果为空。则从请求中获取当前传递手机号码
            phone = request.getParameter("phone");
            if("".equals(phone)){
                logger.info("页面获取手机号码为空！[phone],session中获取手机号码也为空!");
                jsonObject.put("code", "5000");
                jsonObject.put("status", "false");
                jsonObject.put("mess", "手机号码获取异常，重新授权登陆获取！");
                logger.info("当前手机号码获取异常!");
                return jsonObject.toString();
            }
            //从页面获取当前手机号码，数据库不存在此用户手机号码。更新用户手机号码
            logger.info("session中取出手机号码为空，从页面获取手机号码为:"+phone);
            status ="0";
        }

        //验证码 session
        String valNum = String.valueOf(session.getAttribute("valNum"));
        logger.info("[giftUserCount-UserController-session]Session中获取手机验证码:"+valNum);
        //验证码 request
        String PhoneCard =request.getParameter("code");
        logger.info("[giftUserCount-UserController-request]请求中获取手机验证码:"+PhoneCard);

        //openId
        String openId = String.valueOf(session.getAttribute("openId"));
        logger.info("获取用户openId:"+openId);

        //UserId
        String userId =String.valueOf( session.getAttribute("userId"));
        logger.info("获取用户UserId:"+userId);

        //办卡操作时，判断当前用户openId是否存在
        logger.info("判断当前openId或者UserId是否为空!");
        if(openIdISNull(openId) || openIdISNull(userId)){
            jsonObject.put("status","false");
            jsonObject.put("code", "1199");
            jsonObject.put("mess","数据异常！未正确获取到openId["+openId+"],userId["+userId+"]！");
            logger.info("数据异常!未正确获取到openId["+openId+"],userId["+userId+"]");
            return jsonObject.toString();
        }

        if(status=="0"){   //update更新用户的手机号码
            logger.info("当前用户openId[" + openId + "],手机号码不存在，准备绑卡操作.....");
            if (valNum.equals(PhoneCard)) {
                logger.info("验证码验证成功!!");
            } else {
                jsonObject.put("code", "3000");
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

            jsonObject.put("status","success");
            jsonObject.put("mess","更新当前用户手机号码,办卡发起成功！");
            optionStatus=1;

            //获取当前手机号码，获取手机号码归属地
            phoneQueryService.getPhone(phone);

        }else {//绑卡操作
            logger.info("当前用户openId[" + openId + "],存在手机号,添加third_user表记录.....");
            //页面传递验证码和后台session中验证码进行匹配
            optionStatus=1;
        }


        if(optionStatus==1){
            logger.info(",验证码校验正确!插入数据到临时表中.....");
            String third_name = String.valueOf(session.getAttribute("third_name"));

            //如果当前渠道码获取为空，
//            if("".equals(third_name) && third_name!=null){
            if(openIdISNull(third_name)){
//                jsonObject.put("code","9999");
//                jsonObject.put("status","false");
//                jsonObject.put("mess","渠道码获取为空！请重新进入此页面办卡！");
//                return jsonObject.toString();

                //当前正在进行次操作的用户
                String openId_request = String.valueOf(session.getAttribute("openId"));
                logger.info("渠道为null，当前访问用户--openid：["+openId_request+"]");
                //渠道码重新赋值[公司]
                third_name="zfwcy";
                logger.info("渠道为null，将null渠道码赋值zfwcy");
            }

            logger.info("获取当前用户渠道:"+third_name);

            Date date = new Date();
            //插入数据到临时表
            ThirdUserModel thirdUserModel = new ThirdUserModel();

            //获取用户办卡-手机号码城市地区
            thirdUserModel.setPhone(phoneQueryService.getPhone(phone).getPhone());
            thirdUserModel.setName(openId);
            thirdUserModel.setThirdName(third_name);//渠道名称

            //查询当前用户是否存在
            List<ThirdUserModel> list = iThirdUserService.queryThirdUserList(thirdUserModel);
            if(list.size()>0){
                logger.info("当前用户，已存在临时表中，不添加！");
            }else{
                thirdUserModel.settId(UUIDUtil.getUUID());
                thirdUserModel.setStatus("0");
                thirdUserModel.setCreateTime(sdf.format(date));
                logger.info("添加当前用户到临时表中!");
                iThirdUserService.addThirdUserModel(thirdUserModel);
            }

            String params = JSONObject.toJSONString(thirdUserModel);
            logger.info("当前数据third_model:"+params);
        }

        jsonObject.put("status","success");
        jsonObject.put("mess","绑卡成功，临时表添加数据成功！");
        logger.info("当前返回jsonobject:"+jsonObject.toString());

        return jsonObject.toString();
    }


    //配置PC端，访问当前办卡页面地址
    @RequestMapping(value = "/pabankGoPc")
    public ModelAndView PaBankGoPcController(HttpServletRequest request ,HttpServletResponse response,HttpSession session){
        logger.info("进入PC端平安办卡通道......");
        ModelAndView modelAndView = new ModelAndView();

        String third_name = request.getParameter("third_name");
        logger.info("获取当前third_namer:"+third_name);

        //将当前third_name  存入session中
        session.setAttribute("third_name_pc",third_name);

        modelAndView.addObject("third_name",third_name);
        modelAndView.addObject("phone","");
        modelAndView.addObject("mode","pc");
        modelAndView.setViewName("redirect:/giftpay/giftUserCount/index.jsp");

        return modelAndView;
    }


    //PC端，用户办卡
    @RequestMapping(value = "/doBankPc")
    @ResponseBody
    public String doBankPc(HttpServletRequest request ,HttpServletResponse response ,HttpSession session){
        JSONObject jsonObject = new JSONObject();

        //验证码 session
        String valNum = String.valueOf(session.getAttribute("valNum"));
        logger.info("[giftUserCount-UserController-session]Session中获取手机验证码:"+valNum);
        //验证码 request
        String PhoneCard =request.getParameter("code");
        logger.info("[giftUserCount-UserController-request]请求中获取手机验证码:"+PhoneCard);
        //手机号
        String phone = request.getParameter("phone");
        logger.info("请求中获取手机号码:"+phone);

        //判断当前手机号输入验证码是否正确
        if (valNum.equals(PhoneCard)) {
            logger.info("验证码验证成功!!");
        } else {
            jsonObject.put("code", "3000");
            jsonObject.put("status", "false");
            jsonObject.put("mess", "验证码输入错误！");
            logger.info("当前验证码错误!");
            return jsonObject.toString();
        }

        Date date = new Date();
        //插入数据到临时表
        ThirdUserModel thirdUserModel = new ThirdUserModel();

        //获取用户办卡-手机号码城市地区
        thirdUserModel.setPhone(phoneQueryService.getPhone(phone).getPhone());
        thirdUserModel.setName("非微信访问方式");

        String third_name = String.valueOf(session.getAttribute("third_name_pc"));
        if(openIdISNull(third_name)){
            third_name = "lost_thirdName";
        }

        thirdUserModel.setThirdName(third_name);//渠道名称
        thirdUserModel.settId(UUIDUtil.getUUID());
        thirdUserModel.setStatus("0");
        thirdUserModel.setCreateTime(sdf.format(date));
        logger.info("---pc端----添加当前用户到临时表中!");
        iThirdUserService.addThirdUserModel(thirdUserModel);

        jsonObject.put("code", "3000");
        jsonObject.put("status", "success");
        jsonObject.put("mess", "添加成功！");
        logger.info("当前验证码错误!");
        return jsonObject.toString();

    }


}

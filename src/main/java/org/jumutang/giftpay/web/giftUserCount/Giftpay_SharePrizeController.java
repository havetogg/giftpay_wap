package org.jumutang.giftpay.web.giftUserCount;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.*;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.ChannelCountService;
import org.jumutang.giftpay.service.PaBankOrderinfoService;
import org.jumutang.giftpay.service.PaBankUserService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mumu on 17/6/22.
 */


@Controller
@RequestMapping(value = "/pinganShare",method = {RequestMethod.POST,RequestMethod.GET})
public class Giftpay_SharePrizeController extends BaseController{


    @Autowired
    private PaBankUserService paBankUserService;

    @Autowired
    private ChannelCountService channelCountService;

    @Autowired
    private PaBankOrderinfoService paBankOrderinfoService;

    @Autowired
    private PhoneQueryService phoneQueryService;

    //有礼付用户静默授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxlogin_pa")
    public void wxRedPkgBase(HttpServletRequest request, HttpServletResponse response) throws IOException {


        logger.error("----Giftpay_SharePrizeController--进入静默授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","")
                +"pinganShare/init.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("----Giftpay_SharePrizeController--targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxLoginByHands_pa")
    public void wxRedPkgByBase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("--Giftpay_SharePrizeController--进入手动授权方法");

        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "pinganShare/init.htm");
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
        logger.info("--Giftpay_SharePrizeController----进入获取用户信息方法---");
        String openId = request.getParameter("openid");

        PaBankUser paBankUser = new PaBankUser();

        String targetUrl="";
        if (openIdISNull(openId)) {
            logger.error("--Giftpay_SharePrizeController----静默授权未获取到openId，跳转到手动授权");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"pinganShare/wxLoginByHands_pa.htm";
        }else if (openId.length()<=4){ //之前t_paBankUser表获取到null的openId,所以做一次验证,openId的长度必须>4.
            logger.error("静默授权获取openId["+openId+"],长度不正确。跳转手动授权！");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"pinganShare/wxLoginByHands_pa.htm";
        }else{
            session.setAttribute("openId", openId);
            logger.info("参数存入session中:openId = "+openId);

            //查询是否存在当前用户数据
            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(openId);
            List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);
            logger.info("查询当前用户openId["+openId+"]是否存在:"+subList.size());

            String phone = "";
            String userId = "";
            //验证用户是否存在，
            if (subList.size() != 0) {
                phone = subList.get(0).getPhone();
                session.setAttribute("phone",phone);
                logger.info("存在 当前用户,获取当前用户手机号码["+phone+"]!");

                //将获取到的数据存入数据表中paBankUser,paBankUser表插入数据只需要openId用户id,和phone手机号
                paBankUser.setOpenid(openId);
                paBankUser.setPhone(phone);

               // targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/recommend.jsp?openId="+openId+"&phone="+phone;
            }else if(subList.size() == 0){
                logger.info("当前openId:["+openId+"]用户不存在!");
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

                session.setAttribute("phone",phone);
                //创建用户生成之后，将当前openId用户创建生成paBankUser
                paBankUser.setOpenid(openId);
               // targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/recommend.jsp?openId="+openId+"&phone="+phone;
            }

            logger.info("当前初始化init跳转地址:"+targetUrl);
            logger.info("添加当前授权用户到paBankUser表中[插入数据:openId:"+openId+",phone:"+phone+"]......");
            logger.error("当前初始化init跳转地址:"+targetUrl);
            logger.error("添加当前授权用户到paBankUser表中[插入数据:openId:"+openId+",phone:"+phone+"]......");


            //判断当前openId是否为空
            if(openIdISNull(openId)){
                logger.info("当前插入渠道码openId：["+openId+"],跳转手动授权");
                logger.info("当前渠道码从paBankUser取：["+paBankUser.getOpenid()+"]");
                //当前openId为空,跳转手动授权
                targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"pinganShare/wxLoginByHands_pa.htm";
            }else {
                logger.info("获取当前渠道码openId["+openId+"]--准备查询当前用户是否存在paBankUser表中.....");
                //判断当前paBankUser是否存在
                PaBankUser p2 = new PaBankUser();
                p2.setOpenid(openId);
                ArrayList<PaBankUser> p2_list = paBankUserService.PaUserList(p2);
                logger.info("查询当前渠道码是否存在paBankUser表[count:"+p2_list.size()+"].....");
                if (p2_list.size() > 0) {
                    logger.info("当前用户已经存在:openId:[" + openId + "]");
                } else {
                    logger.info("不存在当前openId用户，创建当前用户openId:[" + openId + "]");
                    paBankUserService.PaUserAdd(paBankUser);
                }

                //查询当前渠道码是否存在点击率
                logger.info("当前渠道码["+openId+"]是否存在点击率，查询t_channel_count表......");
                ChannelCountModel channelCountModel = new ChannelCountModel();
                channelCountModel.setChannel_code(openId);
                ArrayList<ChannelCountModel> list = channelCountService.channelList(channelCountModel);
                logger.info("当前渠道码是否存在t_channel_count表，查询结果[count:"+list.size()+"]");
                if (list.size() > 0) {
                    logger.info("当前已经存在！以当前用户openId作为渠道码");
                } else {
                    logger.info("不存在，创建以用户openId为渠道码的数据");
                    channelCountService.channelAdd(channelCountModel);
                }

                //最终确认url地址
                targetUrl=this.getBaseUrl(request, response).replace(":80", "")+"/giftpay/giftUserCount/recommend.jsp?openId="+openId+"&phone="+phone;
            }

        }
        logger.info("---giftpay_SharePrizeController-重定向跳转地址--Url:"+targetUrl);
        //重定向跳转链接
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }


    //查询PaBankUser表的数据
    @RequestMapping(value = "/searchPaBankUser",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String searchPaBankUserList(HttpServletRequest request ,HttpServletResponse response){

        PaBankUser paBankUser =  new PaBankUser();

        String openId = request.getParameter("openid");

        logger.info("准备查询用户openId作为渠道码的成功办卡数据........[当前请求用户openId:"+openId+"]");

        if(!"".equals(openId)){
            paBankUser.setOpenid(openId);
        }else{
            logger.info("----Giftpay_SharePrizeController-----当前openid为空，查询指定openId用户数据失败！全查数据");
        }

        ArrayList<PaBankUser> list = paBankUserService.PaUserList(paBankUser);

        String json = JSONObject.toJSONString(list);
        logger.info("查询PaBankUser表的数据:"+json);

        return json;
    }


    //用户发起提现申请
    @RequestMapping(value = "/doMoney",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String doMoney(HttpServletRequest request ,HttpServletResponse response){

        JSONObject jsonObject = new JSONObject();

        String openId = request.getParameter("openid");
        logger.info("获取发起提现申请用户的openid:["+openId+"]");

        UserSubModel subModel = new UserSubModel();
        subModel.setOpenId(openId);
        List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);

        String phone = subList.get(0).getPhone();
        logger.info("获取当前用户下手机号码:"+phone);

        if("".equals(phone)){
            phone = request.getParameter("phone");
            logger.info("当前用户下手机号码为空!从请求中获取当前手机号码:"+phone);

            subModel.setPhone(phone);
            subModel.setUserId(subList.get(0).getUserId());
            this.userSubService.updateUserSubModel(subModel);
            logger.info("为当前openId用户更新手机号码[userId:"+subList.get(0).getUserId()+"]");


            //获取当前手机号码，获取手机号码归属地
            phoneQueryService.getPhone(phone);

        }

        //查询当前用户的人数 成功办卡
        PaBankUser paBankUser = new PaBankUser();
        paBankUser.setOpenid(openId);
        ArrayList<PaBankUser> list = paBankUserService.PaUserList(paBankUser);



        if(list.size()>0){

            //判断当前用户下是否存在成功办卡人数
            if(list.get(0).getGet_bankuser()<=0){
                logger.info("当前用户下不存在成功办卡用户!请求驳回");
                jsonObject.put("code","9999");
                jsonObject.put("status","false");
                jsonObject.put("mess","当前用户下不存在成功办卡用户");
                return  jsonObject.toString();
            }else{

                PaBankOrderinfo paBankOrderinfo = new PaBankOrderinfo();
                paBankOrderinfo.setOpenid(openId);
                paBankOrderinfo.setPhone(phone);
                paBankOrderinfo.setUsercount(list.get(0).getGet_bankuser());
                paBankOrderinfo.setMoney(70*paBankOrderinfo.getUsercount());
                paBankOrderinfoService.PaBankOrderinfoAdd(paBankOrderinfo);
                logger.info("添加用户提现订单记录........");

                //修改用户表中的数据
                PaBankUser p1 = new PaBankUser();
                p1.setOpenid(openId);  //openId作为条件
                p1.setGet_bankuser(0); //将办卡成功基数 修改为默认0
                p1.setPhone(phone); //更新当前用户的手机号码
                p1.setOld_bankuser(list.get(0).getGet_bankuser()); //获取之前查询出来当前用户 成功办卡 人数
                paBankUserService.PaUserUpdate(p1);
                logger.info("修改用户表中的数据paBankUser........");

                jsonObject.put("code","1000");
                jsonObject.put("status","success");

            }
        }


        return jsonObject.toString();
    }

}

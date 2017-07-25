package org.jumutang.giftpay.web.wxlogin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.tools.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by RuanYJ on 2017/3/8.
 */
@Controller
@RequestMapping(value = "/giftpay/third")
public class ThirdLoginController extends BaseController {

    /**
     * 微信端登录授权
     *
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/login")
    public void redPkgGate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/third/loginThirdFunc.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    @RequestMapping(value = "/loginThirdFunc")
    @ResponseBody
    public ModelAndView loginRedPkg(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.info("进入获取用户微信信息");
        String openId = request.getParameter("openid");
        if (StringUtils.isEmpty(openId)) {
            logger.info("参数错误");
        }
        logger.info("用户OpenId:" + openId);
        session.setAttribute("thirdUserOpenId", openId);
        //response.sendRedirect(response.encodeRedirectURL(checkRedPhoneUrl.replace("OPENID",openId)));
        String redpkgResult= HttpUtil.sendGet(queryRedUrl + "?openId=" + openId + "&channelId=0001", "utf-8", null);
        return new ModelAndView("/giftpay/third/search.html");
    }

    @RequestMapping(value = "/checkPhone")
    @ResponseBody
    public String checkPhone(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String phone = request.getParameter("phone");
        ThirdUserModel thirdUserModel = new ThirdUserModel();
        thirdUserModel.setPhone(phone);
        List<ThirdUserModel> list = this.thirdUserService.queryThirdUserList(thirdUserModel);
        CodeMess codeMess;
        if (list.size() == 0) {
            codeMess = new CodeMess("20000", "无可用红包");
        } else {
            if(list.get(0).getStatus().equals("1")){
                codeMess = new CodeMess("10000", "可用红包");
            }else{
                codeMess = new CodeMess("20000", "无可用红包");
            }
        }
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void sendMsg(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        String phone = request.getParameter("phone");
        Random random = new Random();
        int valNum = random.nextInt(899999);
        valNum = valNum + 100000;//随机六位数
        String msgContent = "【有礼付】您的验证码是" + valNum + "，如非本人操作请忽略该短信。";
        logger.info("发送短信验证码为：" + valNum);
        net.sf.json.JSONObject jsonObject = MobileMessageUtil.sendMessage(phone, msgContent);
        logger.info("发送短信结果返回：" + jsonObject);
        session.setAttribute(phone, String.valueOf(valNum));
    }

    /**
     * 设置用户信息
     */
    @Transactional
    @RequestMapping(value = "/valiThirdUserInfo")
    @ResponseBody
    public String valiThirdUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        String openId= (String) session.getAttribute("thirdUserOpenId");
        String mobile = request.getParameter("phone");
        String valNum = request.getParameter("valNum");
        String redPkgMsgNum = (String) session.getAttribute(mobile);
        CodeMess codeMess = null;
        if (!valNum.equals(redPkgMsgNum)) {
            codeMess = new CodeMess("20000", "验证码错误！");
            return JSONObject.toJSONString(codeMess);
        } else {
            codeMess = new CodeMess("10000", "验证通过");
            session.setAttribute("thirdPhone", mobile);
        }
        return JSON.toJSONString(codeMess);
    }

    @Transactional
    @RequestMapping(value = "/getZshOpenId")
    @ResponseBody
    public void getZshOpenId(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(getZshOpenIdUrl));
    }

    @Transactional
    @RequestMapping(value = "/addThirdUserInfo")
    @ResponseBody
    public void addThirdUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String openId = (String) session.getAttribute("thirdUserOpenId");//有礼付openId
        String userOpenid = request.getParameter("userOpenid");           //中石化openId
        String mobile = (String) session.getAttribute("thirdPhone");
        ThirdUserModel thirdUserModel = new ThirdUserModel();
        thirdUserModel.setPhone(mobile);
        List<ThirdUserModel> list = this.thirdUserService.queryThirdUserList(thirdUserModel);
        String tName = list.get(0).getThirdName();
        UserSubModel subModel = new UserSubModel();
        subModel.setOpenId(openId);
        subModel.setPhone(mobile);
        subModel.setType("3");
        this.addThirdUser(subModel);
        subModel.setOpenId(userOpenid);
        String userId = this.addThirdUser(subModel);
        String redpkgResult ="";
        if(tName.equals("zsh")){
            redpkgResult= HttpUtil.sendGet(queryRedUrl + "?openId=" + userOpenid + "&channelId=0001", "utf-8", null);
        }else{
            //如果是第三方用有礼付openid
            redpkgResult= HttpUtil.sendGet(queryRedUrl + "?openId=" + userOpenid + "&channelId=0001", "utf-8", null);
        }
        if (redpkgResult == null) {
            //无返回
            logger.info("查询红包接口失败");
        }
        JSONObject redp = (JSONObject) JSONObject.parse(redpkgResult);
        if (redp == null || redp.getString("data") == null) {
            logger.info("没有红包记录 添加红包");
            if(tName.equals("zsh")){
                redpkgResult = HttpUtil.sendGet(addRedUrl + "?openId=" + userOpenid + "&channelId=0001&userId=" + userId, "utf-8", null);
                logger.info("添加红包返回结果:" + redpkgResult);
                redpkgResult = HttpUtil.sendGet(queryRedUrl + "?openId=" + userOpenid + "&channelId=0001", "utf-8", null);
            }else{
                redpkgResult = HttpUtil.sendGet(addRedUrl + "?openId=" + userOpenid + "&channelId=0001&userId=" + userId, "utf-8", null);
                logger.info("添加红包返回结果:" + redpkgResult);
                redpkgResult = HttpUtil.sendGet(queryRedUrl + "?openId=" + userOpenid + "&channelId=0001", "utf-8", null);
            }
            JSONObject redResult = (JSONObject) JSONObject.parse(redpkgResult);
            JSONObject object = redResult.getJSONObject("data");
            if (object.getString("state").equals("2")) {
                //已添加可使用红包
                logger.info("已添加可使用红包");
            }
//            if (tName.equals("zsh")) {
               /* logger.info("第三方ZSH");
                String redId = object.getString("redpkgId");
                String uptUrl = updateRedUrl + "ByThird?redpkgId=" + redId + "&openId=" + userOpenid + "&phone=" + mobile + "&state=1";
                logger.info("==========uptUrl:" + uptUrl);
                redpkgResult = HttpUtil.sendGet(uptUrl, "utf-8", null);
                logger.info("=============" + redpkgResult);
            }else{*/
                logger.info("第三方"+tName);
                String redId = object.getString("redpkgId");
                String uptUrl = updateRedUrl + "ByThird?redpkgId=" + redId + "&openId=" + userOpenid + "&phone=" + mobile + "&state=1";
                logger.info("==========uptUrl:" + uptUrl);
                redpkgResult = HttpUtil.sendGet(uptUrl, "utf-8", null);
                logger.info("=============" + redpkgResult);
//            }
        }
        response.sendRedirect(response.encodeRedirectURL(checkRedHomePage));
    }



    private String addThirdUser(UserSubModel userSubModel) {
        CodeMess codeMess = null;
        List<UserSubModel> subList;
        try {
            subList = this.userSubService.queryUserSubModel(userSubModel);
        } catch (Exception e) {
            logger.info("未获取到数据");
            subList = new ArrayList<UserSubModel>();
        }
        String userId = "";
        if (subList.size() == 0) {
            //不存在 查找是否在主表
            UserMainModel userMainModel = new UserMainModel();
            userMainModel.setPhone(userSubModel.getPhone());
            List<UserMainModel> mainList = this.userMainService.queryUserMainModel(userMainModel);
            if (mainList.size() == 0) {
                //主表不存在 添加主表和字表
                String id = UUIDUtil.getUUID();
                userMainModel.setId(id);
                userMainModel.setStatus("0");
                this.userMainService.addUserMainModel(userMainModel);
                userSubModel.setUserId(id);
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess = new CodeMess("10000", "添加成功");
                userId = id;
            } else {
                //主表存在
                userMainModel = mainList.get(0);
                userSubModel.setUserId(userMainModel.getId());
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess = new CodeMess("10001", "添加子关联表成功");
                userId = userMainModel.getId();
            }
        } else {
            //已存在
            userId = subList.get(0).getUserId();
            codeMess = new CodeMess("10002", "已存在相关联表");
        }
        return userId;
    }

    /**
     * 微信端登录授权
     *
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/addThirdUserIndex")
    public void addThirdUserIndex(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("添加兴业红包，静默授权");
        Long timestamp = System.currentTimeMillis();
        String thirdName = request.getParameter("thirdName");
        String url = "";
        if (thirdName == null || thirdName.equals("")) {
            url = response.encodeRedirectURL(URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") + "giftpay/third/error.html"));
        } else {
            session.setAttribute("thirdName", thirdName);//第三方平台名称
            String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
            String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
            String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                    "giftpay/third/toAddThirdUser.htm?thirdName="+thirdName);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        response.sendRedirect(response.encodeRedirectURL(url));
    }
    /**
     * 微信端登录授权
     *
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/addThirdUserIndexUserInfo")
    public void addThirdUserIndexUserInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        logger.error("添加兴业红包，手动授权");
        Long timestamp = System.currentTimeMillis();
        String thirdName = request.getParameter("thirdName");
        String url = "";
        if (thirdName == null || thirdName.equals("")) {
            url = response.encodeRedirectURL(URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") + "giftpay/third/error.html"));
        } else {
            session.setAttribute("thirdName", thirdName);//第三方平台名称
            String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
            String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
            String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                    "giftpay/third/toAddThirdUser.htm?thirdName="+thirdName);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        response.sendRedirect(response.encodeRedirectURL(url));
    }
    @RequestMapping(value = "/toAddThirdUser")
    @ResponseBody
    public ModelAndView toAddThirdUser(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openid");
        String thirdName=request.getParameter("thirdName");
        if(openIdISNull(openId)){
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl="http://www.linkgift.cn/giftpay_wap/giftpay/third/addThirdUserIndexUserInfo.htm?thirdName="+thirdName;
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        session.setAttribute("thirdUserOpenId", openId);
        UserSubModel subModel = new UserSubModel();
        subModel.setOpenId(openId);
        ModelAndView modelAndView=new ModelAndView("/giftpay/third/activateMain.jsp");
        modelAndView.addObject("thirdName",thirdName);
        return modelAndView;
    }
    @RequestMapping(value = "/addThirdUser")
    @ResponseBody
    public String addThirdUser(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String mobile = request.getParameter("mobile");
        String valNum = request.getParameter("valNum");
        String thirdName=request.getParameter("thirdName");
        String redPkgMsgNum = (String) session.getAttribute(mobile);
        CodeMess codeMess = null;
        ThirdUserModel u = new ThirdUserModel();
        u.setPhone(mobile);
        u.setThirdName(thirdName);
        List<ThirdUserModel> list = thirdUserService.queryThirdUserList(u);
        if (list.size() == 0) {
            if (!valNum.equals(redPkgMsgNum)) {
                codeMess = new CodeMess("20000", "验证码错误！");
                return JSONObject.toJSONString(codeMess);
            } else {
                codeMess = new CodeMess("10000", "验证通过");
                session.setAttribute("thirdPhone", mobile);
                ThirdUserModel thirdUserModel = new ThirdUserModel();
                thirdUserModel.setPhone(mobile);
                thirdUserModel.setThirdName((String) session.getAttribute("thirdName"));
                thirdUserModel.setStatus("0");
                thirdUserModel.setName("");
                this.thirdUserService.addThirdUserModel(thirdUserModel);
                return JSONObject.toJSONString(codeMess);
            }
        } else {
            codeMess = new CodeMess("20000", "您的号码已存在！");
            return JSONObject.toJSONString(codeMess);
        }
    }


    /**
     * 微信端登录授权
     *
     * @param request
     * @param response
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/addThirdUserOther")
    public void addThirdUserOther(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        Long timestamp = System.currentTimeMillis();
        String thirdName = request.getParameter("thirdName");
        String url = "";
        if (thirdName == null || thirdName.equals("")) {
            url = response.encodeRedirectURL(URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") + "giftpay/third/error.html"));
        } else {
            session.setAttribute("thirdName", thirdName);//第三方平台名称
            String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
            String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
            String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                    "giftpay/third/toAddThirdUserOther.htm?thirdName="+thirdName);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    @RequestMapping(value = "/toAddThirdUserOther")
    @ResponseBody
    public ModelAndView toAddThirdUserOther(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openid");
        String thirdName=request.getParameter("thirdName");
        session.setAttribute("thirdUserOpenId", openId);
        ModelAndView modelAndView=new ModelAndView("/giftpay/third/activateMain2.jsp");
        modelAndView.addObject("thirdName",thirdName);
        return modelAndView;
    }

    /**
     * 统计通过第三方添加号码的数量
     * @param request
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/thirdCount")
    @ResponseBody
    public String thirdCount(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<ThirdUserModel> xingyeAllList=new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> pinganAllList=new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> fuliAllList=new ArrayList<ThirdUserModel>();

        List<ThirdUserModel> xingyeTodayList=new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> pinganTodayList=new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> fuliTodayList=new ArrayList<ThirdUserModel>();
        ThirdUserModel th=new ThirdUserModel();
        List<ThirdUserModel> list=this.thirdUserService.queryThirdUserList(th);
        th.setCreateTime("today");
        List<ThirdUserModel> todayList=this.thirdUserService.queryThirdUserList(th);
        for(ThirdUserModel thirdUserModel :list){
            if("pingan".equals(thirdUserModel.getThirdName())){
                pinganAllList.add(thirdUserModel);
            }else if("other".equals(thirdUserModel.getThirdName())){
                xingyeAllList.add(thirdUserModel);
            }else if("fuli".equals(thirdUserModel.getThirdName())){
                fuliAllList.add(thirdUserModel);
            }
        }
        for(ThirdUserModel thirdUserModel :todayList){
            if("pingan".equals(thirdUserModel.getThirdName())){
                pinganTodayList.add(thirdUserModel);
            }else if("other".equals(thirdUserModel.getThirdName())){
                xingyeTodayList.add(thirdUserModel);
            }else if("fuli".equals(thirdUserModel.getThirdName())){
                fuliTodayList.add(thirdUserModel);
            }
        }
        JSONObject object=new JSONObject();
        object.put("xingye", JSONArray.toJSONString(xingyeAllList));
        object.put("pingan",JSONArray.toJSONString(pinganAllList));
        object.put("fuli",JSONArray.toJSONString(pinganAllList));
        object.put("xingyeToday", JSONArray.toJSONString(xingyeTodayList));
        object.put("pinganToday",JSONArray.toJSONString(pinganTodayList));
        object.put("fuliToday", JSONArray.toJSONString(xingyeTodayList));
        return object.toJSONString();
    }
}

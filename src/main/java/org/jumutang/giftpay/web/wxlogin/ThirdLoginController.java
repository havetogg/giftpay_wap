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
import org.jumutang.giftpay.service.UserMainService;
import org.jumutang.giftpay.tools.*;
import org.jumutang.giftpay.web.user.UserOperationController;
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
import java.util.*;

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
        String redpkgResult = HttpUtil.sendGet(queryRedUrl + "?openId=" + openId + "&channelId=0001", "utf-8", null);
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
    /*    if (list.size() == 0) {
            codeMess = new CodeMess("20000", "无可用红包");
        } else {
            if(!list.get(0).getStatus().equals("0")){
                codeMess = new CodeMess("10000", "可用红包");
            }else{
                codeMess = new CodeMess("20000", "无可用红包");
            }
        }*/
        if (list.size() == 0) {
            codeMess = new CodeMess("20000", "无可用红包");
        } else {
            boolean isExist = false;
            for (ThirdUserModel tUser : list) {
                if (!tUser.getStatus().equals("0")) {
                    //有红包
                    isExist = true;
                }
            }
            if (isExist) {
                codeMess = new CodeMess("10000", "可用红包");
            } else {
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
        synchronized (ThirdLoginController.class) {
            String ADDREDPKG = "http://www.linkgift.cn/giftcenter/saveFuliCenterRedPkg";
            String UPDATEREDPKG = "http://www.linkgift.cn/giftcenter/updateRedpkgStateByThird";
            String openId = (String) session.getAttribute("thirdUserOpenId");//有礼付openId
            String userOpenid = request.getParameter("userOpenid");           //中石化openId
            String mobile = (String) session.getAttribute("thirdPhone");
            ThirdUserModel thirdUserModel = new ThirdUserModel();
            thirdUserModel.setPhone(mobile);
            List<ThirdUserModel> list = this.thirdUserService.queryThirdUserList(thirdUserModel);
            for (ThirdUserModel t : list) {
                if (!t.getStatus().equals("0")) {
                    thirdUserModel = t;
                    break;
                }
            }
            String status = thirdUserModel.getStatus();
            logger.error("激活红包的状态为:"+status);
            String redpkgId = "";
            String channelId="";
            if (status.equals("1")) {
                redpkgId = "80";
                channelId="0001";
            } else if (status.equals("2")) {
                redpkgId = "90";
                channelId="0002";
            }
            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(openId);
            subModel.setPhone(mobile);
            subModel.setType("3");
            String userId=this.addThirdUser(subModel);
            subModel.setOpenId(userOpenid);
            this.addThirdUser(subModel);
            String redpkgResult = "";

            Map<String, String> updateParams = new HashMap<String, String>();
            updateParams.put("redpkgId", redpkgId);
            updateParams.put("openId", userOpenid);
            updateParams.put("phone", mobile);
            updateParams.put("state", "1");
            updateParams.put("reState", "4");
            updateParams.put("userId", userId);
            updateParams.put("channelId", channelId);
            logger.error("添加激活红包参数:"+updateParams);
            redpkgResult = HttpUtil.sendPost(ADDREDPKG, HttpUtil.UTF8, updateParams);
            logger.error("查询添加红包返回信息:" + redpkgResult);
            redpkgResult = HttpUtil.sendPost(UPDATEREDPKG, HttpUtil.UTF8, updateParams);
            logger.error("查询更新红包返回信息:" + redpkgResult);
            JSONObject redResult = (JSONObject) JSONObject.parse(redpkgResult);
            logger.info("=============" + redResult);

            ThirdUserModel third = new ThirdUserModel();
            third.setStatus("0");
            third.settId(thirdUserModel.gettId());
            third.setThirdName(thirdUserModel.getThirdName() + "(activate)");
            int result = this.thirdUserService.updateThirdUserModel(third);
            logger.error("使用已激活 修改第三方表记录状态:" + result);
            response.sendRedirect(response.encodeRedirectURL(checkRedHomePage));
        }
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
                    "giftpay/third/toAddThirdUser.htm?thirdName=" + thirdName);
            targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                    + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
            logger.info("targetUrl:" + targetUrl);
            url = targetUrl;
        }
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    private String addThirdUser(UserSubModel userSubModel) {
        CodeMess codeMess = null;
        String userId="";
        UserSubModel userSub = new UserSubModel();
        userSub.setOpenId(userSubModel.getOpenId());
        List<UserSubModel> subList;
        subList = this.userSubService.queryUserSubModel(userSub);
        if (subList.size() == 0) {
            //不存在 查找是否在主表
            UserMainModel userMainModel = new UserMainModel();
            userMainModel.setPhone(userSubModel.getPhone());
            List<UserMainModel> mainList = this.userMainService.queryUserMainModel(userMainModel);
            if (mainList.size() == 0) {
                //主表不存在 添加主表和字表
                String id = UUIDUtil.getUUID();
                id = getRandomUUUID(id, userMainService);
                userMainModel.setId(id);
                userMainModel.setStatus("0");
                this.userMainService.addUserMainModel(userMainModel);
                userSubModel.setUserId(id);
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess = new CodeMess("10000", "添加成功");
                userId=id;
            } else {
                //主表存在 子表不存在 判断手机是否为空
                userMainModel = mainList.get(0);
                if (StringUtils.isEmpty(userMainModel.getPhone())) {
                    userMainModel.setPhone(userSubModel.getPhone());
                    //手机号为空更新手机号
                    this.userMainService.updateUserMainModel(userMainModel);
                }
                userSubModel.setUserId(userMainModel.getId());
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess = new CodeMess("10001", "添加子关联表成功");
                userId=userSubModel.getUserId();
            }
        } else {
            //已存在 判断手机号是否为空
            codeMess = new CodeMess("10002", "已存在相关联表");
            for (UserSubModel subModel : subList) {
                //循环所有子表 手机号为空的用户更新手机号
                //if(StringUtils.isEmpty(subModel.getPhone())){
                subModel.setPhone(userSubModel.getPhone());
                logger.info("-------手机号为：" + userSubModel.getPhone());
                this.userSubService.updateUserSubModel(subModel);
                //}
            }
            UserMainModel userMainModel = new UserMainModel();
            userMainModel.setPhone(userSubModel.getPhone());
            userMainModel.setId(subList.get(0).getUserId());
            this.userMainService.updateUserMainModel(userMainModel);
            userId=userMainModel.getId();
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
                    "giftpay/third/toAddThirdUser.htm?thirdName=" + thirdName);
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
        String thirdName = request.getParameter("thirdName");
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            String targetUrl = "http://www.linkgift.cn/giftpay_wap/giftpay/third/addThirdUserIndexUserInfo.htm?thirdName=" + thirdName;
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        }
        session.setAttribute("thirdUserOpenId", openId);
        UserSubModel subModel = new UserSubModel();
        subModel.setOpenId(openId);
        ModelAndView modelAndView = new ModelAndView("/giftpay/third/activateMain.jsp");
        modelAndView.addObject("thirdName", thirdName);
        return modelAndView;
    }

    @RequestMapping(value = "/addThirdUser")
    @ResponseBody
    public String addThirdUser(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String mobile = request.getParameter("mobile");
        String valNum = request.getParameter("valNum");
        String thirdName = request.getParameter("thirdName");
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
                    "giftpay/third/toAddThirdUserOther.htm?thirdName=" + thirdName);
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
        String thirdName = request.getParameter("thirdName");
        session.setAttribute("thirdUserOpenId", openId);
        ModelAndView modelAndView = new ModelAndView("/giftpay/third/activateMain2.jsp");
        modelAndView.addObject("thirdName", thirdName);
        return modelAndView;
    }

    /**
     * 统计通过第三方添加号码的数量
     *
     * @param request
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/thirdCount")
    @ResponseBody
    public String thirdCount(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<ThirdUserModel> xingyeAllList = new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> pinganAllList = new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> fuliAllList = new ArrayList<ThirdUserModel>();

        List<ThirdUserModel> xingyeTodayList = new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> pinganTodayList = new ArrayList<ThirdUserModel>();
        List<ThirdUserModel> fuliTodayList = new ArrayList<ThirdUserModel>();
        ThirdUserModel th = new ThirdUserModel();
        List<ThirdUserModel> list = this.thirdUserService.queryThirdUserList(th);
        th.setCreateTime("today");
        List<ThirdUserModel> todayList = this.thirdUserService.queryThirdUserList(th);
        for (ThirdUserModel thirdUserModel : list) {
            if ("pingan".equals(thirdUserModel.getThirdName())) {
                pinganAllList.add(thirdUserModel);
            } else if ("other".equals(thirdUserModel.getThirdName())) {
                xingyeAllList.add(thirdUserModel);
            } else if ("fuli".equals(thirdUserModel.getThirdName())) {
                fuliAllList.add(thirdUserModel);
            }
        }
        for (ThirdUserModel thirdUserModel : todayList) {
            if ("pingan".equals(thirdUserModel.getThirdName())) {
                pinganTodayList.add(thirdUserModel);
            } else if ("other".equals(thirdUserModel.getThirdName())) {
                xingyeTodayList.add(thirdUserModel);
            } else if ("fuli".equals(thirdUserModel.getThirdName())) {
                fuliTodayList.add(thirdUserModel);
            }
        }
        JSONObject object = new JSONObject();
        object.put("xingye", JSONArray.toJSONString(xingyeAllList));
        object.put("pingan", JSONArray.toJSONString(pinganAllList));
        object.put("fuli", JSONArray.toJSONString(pinganAllList));
        object.put("xingyeToday", JSONArray.toJSONString(xingyeTodayList));
        object.put("pinganToday", JSONArray.toJSONString(pinganTodayList));
        object.put("fuliToday", JSONArray.toJSONString(xingyeTodayList));
        return object.toJSONString();
    }


    public String getRandomUUUID(String uuuid, UserMainService service) {
        UserMainModel mainModel = new UserMainModel();
        mainModel.setId(uuuid);
        List<UserMainModel> list = service.queryUserMainModel(mainModel);
        if (list.size() > 0) {
            String uid = UUIDUtil.getUUID();
            getRandomUUUID(uid, service);
        }
        return uuuid;
    }
}

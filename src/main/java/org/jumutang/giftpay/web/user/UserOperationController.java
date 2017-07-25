package org.jumutang.giftpay.web.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.service.BalanceServiceI;
import org.jumutang.giftpay.service.UserMainService;
import org.jumutang.giftpay.service.UserSubService;
import org.jumutang.giftpay.tools.HttpUtil;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.jumutang.giftpay.web.wxlogin.WxLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import java.util.UUID;

/**
 * Created by RuanYJ on 2017/2/8.
 */
@Controller
@RequestMapping(value = "/userCenter", method = {RequestMethod.GET, RequestMethod.POST})
public class UserOperationController extends BaseController {

    Logger logger = LoggerFactory.getLogger(UserOperationController.class);


    @Autowired
    private UserMainService userMainService;
    @Autowired
    private UserSubService userSubService;
    //余额账户生成服务层
    @Autowired
    private BalanceServiceI balanceServiceI;

    @RequestMapping(value = "/getAllUser")
    @ResponseBody
    public String queryAllUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session, UserSubModel userSubModel){
        CodeMess codeMess;
        if(StringUtils.isEmpty(userSubModel.getOpenId())||userSubModel.getOpenId()==null){
            codeMess=new CodeMess("20000","openId参数不正确");
            return JSONObject.toJSONString(codeMess);
        }
        if(StringUtils.isEmpty(userSubModel.getPhone())||userSubModel.getPhone()==null){
            codeMess=new CodeMess("20000","phone参数不正确");
            return JSONObject.toJSONString(codeMess);
        }
        JSONObject object=new JSONObject();
        //获取字表数据
        List<UserSubModel> subList=this.userSubService.queryUserSubModel(userSubModel);
        if(subList.size()==0){
            //字表数据不存在
            //查找主表
            UserMainModel userMainModel=new UserMainModel();
            if(StringUtils.isEmpty(userSubModel.getPhone())){
                codeMess=new CodeMess("10003","数据不存在");
                object= (JSONObject) JSONObject.toJSON(codeMess);
            }else{
                userMainModel.setPhone(userSubModel.getPhone());
                List<UserMainModel> mainList=this.userMainService.queryUserMainModel(userMainModel);
                if(mainList.size()==0){
                    //子主表数据不存在
                    codeMess=new CodeMess("10001","主表数据不存在");
                    object= (JSONObject) JSONObject.toJSON(codeMess);
                    object.put("mainUser",new JSONObject());
                    object.put("subUser",new JSONObject());
                }else{
                    //存在主表数据 不存在子表数据
                    codeMess=new CodeMess("10002","子表数据不存在");
                    object= (JSONObject) JSONObject.toJSON(codeMess);
                    object.put("mainUser",JSONObject.toJSON(mainList.get(0)));
                    object.put("subUser",new JSONObject());
                }
            }

        }else {
            //存在子表数据
            //查找主表
            UserMainModel userMainModel=new UserMainModel();
            userMainModel.setPhone(userSubModel.getPhone());
            List<UserMainModel> mainList=this.userMainService.queryUserMainModel(userMainModel);
            codeMess=new CodeMess("10000","存在该数据");
            object= (JSONObject) JSONObject.toJSON(codeMess);
            object.put("mainUser",JSONObject.toJSON(mainList.get(0)));
            object.put("subUser",JSONObject.toJSON(subList.get(0)));
        }
        return object.toJSONString();
    }
    @RequestMapping(value = "/addAllUser")
    @ResponseBody
    public String addAllUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, UserSubModel userSubModel){
        CodeMess codeMess;
        if(StringUtils.isEmpty(userSubModel.getType())||userSubModel.getType()==null){
            codeMess=new CodeMess("20000","type参数不正确");
            return JSONObject.toJSONString(codeMess);
        }
        if(StringUtils.isEmpty(userSubModel.getOpenId())||userSubModel.getOpenId()==null){
            codeMess=new CodeMess("20000","openId参数不正确");
            return JSONObject.toJSONString(codeMess);
        }
        if(StringUtils.isEmpty(userSubModel.getPhone())||userSubModel.getPhone()==null){
            codeMess=new CodeMess("20000","phone参数不正确");
            return JSONObject.toJSONString(codeMess);
        }
        UserSubModel userSub=new UserSubModel();
        userSub.setOpenId(userSubModel.getOpenId());
        List<UserSubModel> subList;
        try {
            subList=this.userSubService.queryUserSubModel(userSub);
        } catch (Exception e) {
            codeMess=new CodeMess("20001","接口异常");
            subList=new ArrayList<UserSubModel>();
            return JSONObject.toJSONString(codeMess);
        }
        if(subList.size()==0){
            //不存在 查找是否在主表
            UserMainModel userMainModel=new UserMainModel();
            userMainModel.setPhone(userSubModel.getPhone());
            List<UserMainModel> mainList=this.userMainService.queryUserMainModel(userMainModel);
            if(mainList.size()==0){
                //主表不存在 添加主表和字表
                String id=UUIDUtil.getUUID();
                id=getRandomUUUID(id,userMainService);
                userMainModel.setId(id);
                userMainModel.setStatus("0");
                this.userMainService.addUserMainModel(userMainModel);
                userSubModel.setUserId(id);
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess=new CodeMess("10000","添加成功");
            }else{
                //主表存在 子表不存在 判断手机是否为空
                userMainModel=mainList.get(0);
                if(StringUtils.isEmpty(userMainModel.getPhone())){
                    userMainModel.setPhone(userSubModel.getPhone());
                    //手机号为空更新手机号
                    this.userMainService.updateUserMainModel(userMainModel);
                }
                userSubModel.setUserId(userMainModel.getId());
                userSubModel.setId(UUIDUtil.getUUID());
                this.userSubService.addUserSubModel(userSubModel);
                codeMess=new CodeMess("10001","添加子关联表成功");
            }
        }else{
            //已存在 判断手机号是否为空
            codeMess=new CodeMess("10002","已存在相关联表");
            for(UserSubModel subModel:subList){
                //循环所有子表 手机号为空的用户更新手机号
                if(StringUtils.isEmpty(subModel.getPhone())){
                    subModel.setPhone(userSubModel.getPhone());
                    this.userSubService.updateUserSubModel(subModel);
                }
            }
        }
        //判断用户是否有钱包 没有添加钱包模块
        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setAccountId(userSubModel.getOpenId());
        //判断用户余额账户是否存在
        List<BalanceModel> list = balanceServiceI.queryBalances(balanceModel);
        logger.info("判断用户是否存在钱包余额:"+list);
        if(list.size()==0){
            int result = balanceServiceI.insertBalace(balanceModel);
            if(result==0){
                codeMess=new CodeMess("10003","添加用户钱包功能失败");
            }
        }
        return JSONObject.toJSONString(codeMess);
    }

/*
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/giftUserInfo")
    public void wxRedPkg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phone=request.getParameter("phone");//用户手机号
        phone=phone==null?"":phone;

        String thirdOpenId=request.getParameter("openId");//用户openId
        thirdOpenId=thirdOpenId==null?"":thirdOpenId;

        String rediectUrl=request.getParameter("rediectUrl");//回调Url
        rediectUrl=rediectUrl==null?"":rediectUrl;

        String operation=request.getParameter("operation");//操作：添加 查询 修改
        operation=operation==null?"":operation;

        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80","") +
                "giftpay/userCenter/giftUserHome.htm?entryUrl=" + rediectUrl+"&phone="+phone+"&thirdOpenId="+thirdOpenId+"&operation="+operation);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }
    @RequestMapping(value = "/giftUserHome")
    public void initUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                     String code, ModelMap modelMap) throws UnsupportedEncodingException {
        logger.info("从红包进入获取用户信息方法");
        String openId = request.getParameter("openid");//有礼付openid
        String phone=request.getParameter("phone");//用户手机号
        String thirdOpenId=request.getParameter("thirdOpenId");//用户openId
        String rediectUrl=request.getParameter("entryUrl");//回调Url
        String operation=request.getParameter("operation");//操作：添加 查询 修改

    }*/
    public String getRandomUUUID(String uuuid,UserMainService service){
        UserMainModel mainModel=new UserMainModel();
        mainModel.setId(uuuid);
        List<UserMainModel> list=service.queryUserMainModel(mainModel);
        if(list.size()>0){
            String uid=UUIDUtil.getUUID();
            getRandomUUUID(uid,service);
        }
        return uuuid;
    }

}


package org.jumutang.giftpay.web.recharge;

/**
 * Created by RuanYJ on 2017/4/5.
 */

import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.model.PayInfoModel;
import org.jumutang.giftpay.model.liftPaytment.FamilyModel;
import org.jumutang.giftpay.model.liftPaytment.RechargeAccountModel;
import org.jumutang.giftpay.tools.DateFormatUtil;
import org.jumutang.giftpay.tools.HttpUtil;
import org.jumutang.giftpay.tools.MD5Util;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.stereotype.Controller;
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
 * 生活缴费
 */
@Controller
@RequestMapping(value = "/giftpay/liftpayment")
public class LiftPaytmentController extends BaseController {


    /**
     * 获取用户充值缴费信息
     */
    @RequestMapping(value = "/rechargeIndex", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ModelAndView rechargeIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
       String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        FamilyModel familyModel = new FamilyModel();
        familyModel.setUserId(userId);
        familyModel.setStatus("0");
        List<FamilyModel> familyModelList = this.familyService.queryFamilyList(familyModel);
        if (familyModelList.size() == 0) {
            logger.error("不存在家庭账户 跳转到家庭账户添加页面 ");
            modelAndView.setViewName("/giftpay/liftpayment/addFamily.html");
            return modelAndView;
        }
        modelAndView.setViewName("/giftpay/liftpayment/index.jsp");
        return modelAndView;
    }

    /**
     * 添加用户缴费家庭
     */
    @RequestMapping(value = "/addFamily", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addFamily(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String fname = request.getParameter("name");
        String cityName = request.getParameter("city");
        String provName = request.getParameter("prov");
        provName=provName.substring(0,provName.length()-1);
        cityName=cityName.substring(0,cityName.length()-1);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", oufeiLifeUserId);
        params.put("userpws", oufeiLifeUserPws);
        params.put("version", oufeiProvinceListVersion);
        String provinceResult = HttpUtil.sendPost(this.oufeiProvinceListUrl, "gb2312", params);
        Document doc = null;
        doc = DocumentHelper.parseText(provinceResult);
        Element rootElt = doc.getRootElement();
        Iterator iter = rootElt.elementIterator("provinces");
        String provId = "";
        String cityId = "";
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("province");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                String id = prov.elementTextTrim("provinceId");
                String name = prov.elementTextTrim("provinceName");
                logger.error("省："+name+",参数省："+name);
                if (name.equals(provName)) {
                    provId = id;
                }
            }
        }
        if (provId.equals("")) {
            logger.error("未获取到省ID");
            codeMess = new CodeMess("0", "暂未支持该省市");
            return JSONObject.toJSONString(codeMess);
        }
        params.put("provId",provId);
        String cityResult=HttpUtil.sendPost(this.oufeiCityListUrl,"gb2312",params);
        System.out.println(cityResult);
        doc = DocumentHelper.parseText(cityResult);
        rootElt = doc.getRootElement();
        iter = rootElt.elementIterator("citys");
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("city");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                String id=prov.elementTextTrim("cityId");
                String name=prov.elementTextTrim("cityName");
                logger.error("市："+name+",参数市:"+cityName);
                if(name.equals(cityName)){
                    cityId=id;
                }
            }
        }
        if (cityId.equals("")) {
            logger.error("未获取到市ID");
            codeMess = new CodeMess("0", "暂未支持该省市");
            return JSONObject.toJSONString(codeMess);
        }
        FamilyModel familyModel = new FamilyModel();
        familyModel.setId(UUIDUtil.getUUID());
        familyModel.setUserId(userId);
        familyModel.setCityId(cityId);
        familyModel.setProvinceId(provId);
        familyModel.setCityName(cityName);
        familyModel.setProvinceName(provName);
        familyModel.setStatus("0");
        familyModel.setFamilyName(fname);
        int res = this.familyService.addFamilyModel(familyModel);
        if(res==0){
            codeMess = new CodeMess("0", "添加失败");
            return JSONObject.toJSONString(codeMess);
        }else{
            codeMess = new CodeMess(String.valueOf(res), "添加成功");
            return JSONObject.toJSONString(codeMess);
        }
    }


    /**
     * 查询用户基本信息
     */
    @RequestMapping(value = "/queryFamilyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryFamilyInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }

        FamilyModel familyModel = new FamilyModel();
        familyModel.setUserId(userId);
        familyModel.setStatus("0");
        String fid=request.getParameter("fid");
        if(StringUtils.isNotEmpty(fid)){
            familyModel.setId(fid);
        }
        List<FamilyModel> familyModelList = this.familyService.queryFamilyList(familyModel);
        if (familyModelList.size() == 0) {
            codeMess = new CodeMess("0", "获取用户信息失败（用户访问超时）");
            return JSONObject.toJSONString(codeMess);
        }
        codeMess = new CodeMess("1", JSONArray.fromObject(familyModelList).toString());
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 查询用户基本信息
     */
    @RequestMapping(value = "/queryAccountInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryAccountInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String fId = request.getParameter("fId");
        String accNo=request.getParameter("accNo");
        RechargeAccountModel rechargeAccountModel=new RechargeAccountModel();
        if(StringUtils.isNotEmpty(fId)){
            rechargeAccountModel.setFamilyId(fId);
        }
        if(StringUtils.isNotEmpty(accNo)){
            rechargeAccountModel.setId(accNo);
        }
        rechargeAccountModel.setStatus("0");
        List<RechargeAccountModel> list=this.rechargeAccountService.queryRechargeAccountList(rechargeAccountModel);
        if(list.size()==0){
            codeMess=new CodeMess("1","");
            return JSONObject.toJSONString(codeMess);
        }else{
            codeMess=new CodeMess("2",JSONArray.fromObject(list).toString());
            return JSONObject.toJSONString(codeMess);
        }
    }

    /**
     * 查询缴费单位
     */
    @RequestMapping(value = "/queryRechargeUnit", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryRechargeUnit(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String fId = request.getParameter("fId");
        String accountType=request.getParameter("type");//水电煤类型
        if(accountType.equals("shui")){
            accountType="水费";
        }else if(accountType.equals("dian")){
            accountType="电费";
        }else if(accountType.equals("mei")){
            accountType="燃气费";
        }
        FamilyModel familyModel=new FamilyModel();
        familyModel.setId(fId);
        List<FamilyModel> list=this.familyService.queryFamilyList(familyModel);
        if(list.size()==0){
            logger.error("获取家庭信息参数错误");
        }
        familyModel=list.get(0);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", oufeiLifeUserId);
        params.put("userpws", oufeiLifeUserPws);
        params.put("version", oufeiProvinceListVersion);
        params.put("provId", familyModel.getProvinceId());
        params.put("cityId", familyModel.getCityId());
        String payProjectResult = HttpUtil.sendPost(this.oufeiPayProjectListUrl, "gb2312", params);
        Document doc = null;
        doc = DocumentHelper.parseText(payProjectResult);
        Element rootElt = doc.getRootElement();
        Iterator iter = rootElt.elementIterator("payProjects");
        String projectId="";
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("payProject");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                String payProjectName=prov.elementTextTrim("payProjectName");
                logger.error("获取充值类型名称："+payProjectName);
                if(accountType.trim().equals(payProjectName.trim())){
                    projectId=prov.elementTextTrim("payProjectId");
                }
            }
        }
        logger.error("充值类型ID:"+projectId);
        params.put("type", projectId);
        payProjectResult = HttpUtil.sendPost(this.oufeiPayUnitListUrl, "gb2312", params);
        logger.error("缴费单位字符串："+payProjectResult);
        doc = DocumentHelper.parseText(payProjectResult);
        rootElt = doc.getRootElement();
        iter = rootElt.elementIterator("payUnits");
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("payUnit");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                String payUnitId = prov.elementTextTrim("payUnitId");
                String payUnitName = prov.elementTextTrim("payUnitName");
                object.put("payUnitId", payUnitId);
                object.put("payUnitName", payUnitName);
                array.add(object);
            }
        }
        session.setAttribute("rechargeParam",params);
        return array.toString();
    }



    /**
     * 添加缴费账号
     */
    @RequestMapping(value = "/addRechargeAccount", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addRechargeAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException, DocumentException {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String account=request.getParameter("account");
        String fid=request.getParameter("fid");
        String accountInt=request.getParameter("accountInt");
        String accountUnit=request.getParameter("accountUnit");
        String accountUnitName=request.getParameter("accountUnitName");
        String modelName="";
        String modelId="";
        String productId="";
        String productName="";
        String operationType=request.getParameter("operationType");
        FamilyModel familyModel=new FamilyModel();
        familyModel.setId(fid);
        familyModel.setStatus("0");
        List<FamilyModel> familyModelList=this.familyService.queryFamilyList(familyModel);
        if(familyModelList.size()==0){
            logger.error("获取家庭信息失败");
        }
        familyModel=familyModelList.get(0);
        Map<String, String> params = (Map<String, String>) session.getAttribute("rechargeParam");
        params.put("chargeCompanyCode",accountUnit);
        String payProjectResult = HttpUtil.sendPost(this.oufeiPayModeListUrl, "gb2312", params);
        Document doc = null;
        doc = DocumentHelper.parseText(payProjectResult);
        Element rootElt = doc.getRootElement();
        Iterator iter = rootElt.elementIterator("payModes");
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("payMode");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                String payModeId=prov.elementTextTrim("payModeId");
                String payModeName=prov.elementTextTrim("payModeName");
                if(payModeName.trim().equals("户号")){
                    modelName=payModeName;
                    modelId=payModeId;
                }
            }
        }
        payProjectResult = HttpUtil.sendPost(this.oufeiQueryClassIdUrl, "gb2312", params);
        doc = DocumentHelper.parseText(payProjectResult);
        rootElt = doc.getRootElement();
        iter = rootElt.elementIterator("cards");
        while (iter.hasNext()) {
            Element recordEle = (Element) iter.next();
            Iterator iterch = recordEle.elementIterator("card");
            while (iterch.hasNext()) {
                Element prov = (Element) iterch.next();
                productId=prov.elementTextTrim("productId");
                productName=prov.elementTextTrim("productName");
            }
        }
        Map<String, String> sendParams= new HashMap<String, String>();
        sendParams.put("userid", oufeiLifeUserId);
        sendParams.put("userpws", oufeiLifeUserPws);
        sendParams.put("version", oufeiProvinceListVersion);
        sendParams.put("provName", URLEncoder.encode(familyModel.getProvinceName(),"GBK"));
        sendParams.put("cityName", URLEncoder.encode(familyModel.getCityName(),"GBK"));
        sendParams.put("type",accountInt);
        sendParams.put("chargeCompanyCode",accountUnit);
        sendParams.put("chargeCompanyName",URLEncoder.encode(accountUnitName,"GBK"));
        sendParams.put("account",account);
        sendParams.put("cardId",productId);
        sendParams.put("amount","");
        sendParams.put("counts","");
        sendParams.put("payModeId","2");
        /* logger.error("查询用户账号参数:"+JSONObject.toJSONString(sendParams));
        payProjectResult = HttpUtil.sendPost(this.oufeiBalanceUrl, "gb2312", sendParams);
        logger.error("查询账户信息："+payProjectResult);
        doc = DocumentHelper.parseText(payProjectResult);
        rootElt = doc.getRootElement();
       String code = rootElt.elementTextTrim("retcode");
        if(!code.trim().equals("1")){
            codeMess=new CodeMess(code, rootElt.elementTextTrim("err_msg"));
            return JSONObject.toJSONString(codeMess);
        }*/
//        iter = rootElt.elementIterator("balances");
//        while (iter.hasNext()) {
//            Element recordEle = (Element) iter.next();
//            Iterator iterch = recordEle.elementIterator("balance");
//            while (iterch.hasNext()) {
//                Element prov = (Element) iterch.next();
//
//            }
//        }
        int result=0;
        if(operationType.equals("save")){
            RechargeAccountModel rechargeAccountModel=new RechargeAccountModel();
            rechargeAccountModel.setId(UUIDUtil.getUUID());
            rechargeAccountModel.setFamilyId(fid);
            rechargeAccountModel.setStatus("0");
            rechargeAccountModel.setAccountType(accountInt);
            rechargeAccountModel.setPayAccount(account);
            rechargeAccountModel.setPayUnitId(accountUnit);
            rechargeAccountModel.setPayUnitName(accountUnitName);
            rechargeAccountModel.setCardId(productId);
            rechargeAccountModel.setQueryParams(JSONObject.toJSONString(sendParams));
            result=this.rechargeAccountService.addRechargeAccountModel(rechargeAccountModel);
        }else{
            String accountId=request.getParameter("accountId");
            RechargeAccountModel rechargeAccountModel=new RechargeAccountModel();
            rechargeAccountModel.setId(accountId);
            rechargeAccountModel.setFamilyId(fid);
            rechargeAccountModel.setStatus("0");
            rechargeAccountModel.setAccountType(accountInt);
            rechargeAccountModel.setPayAccount(account);
            rechargeAccountModel.setPayUnitId(accountUnit);
            rechargeAccountModel.setPayUnitName(accountUnitName);
            rechargeAccountModel.setCardId(productId);
            rechargeAccountModel.setQueryParams(JSONObject.toJSONString(sendParams));
            result=this.rechargeAccountService.updateRechargeAccountModel(rechargeAccountModel);
        }
        codeMess=new CodeMess("1",String.valueOf(result));
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 移除用户基本信息
     */
    @RequestMapping(value = "/removeFamily", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String removeFamily(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String fId = request.getParameter("fId");
        if(StringUtils.isNotEmpty(fId)){
            FamilyModel familyModel=new FamilyModel();
            familyModel.setId(fId);
            familyModel.setStatus("1");
            int res=this.familyService.updateFamilyModel(familyModel);
            if(res==0){
                codeMess=new CodeMess("0","删除失败，请联系客服!");
                return JSONObject.toJSONString(codeMess);
            }
        }
        String accountId=request.getParameter("accountId");
        if(StringUtils.isNotEmpty(accountId)){
            RechargeAccountModel rechargeAccountModel=new RechargeAccountModel();
            rechargeAccountModel.setId(accountId);
            rechargeAccountModel.setStatus("1");
            int res=this.rechargeAccountService.updateRechargeAccountModel(rechargeAccountModel);
            if(res==0){
                codeMess=new CodeMess("0","删除失败，请联系客服!");
                return JSONObject.toJSONString(codeMess);
            }
        }
        codeMess=new CodeMess("1","删除成功");
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 查询用户账户余额信息
     */
    @RequestMapping(value = "/queryAccountBalance", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryAccountBalance(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String accNo=request.getParameter("accNo");
        RechargeAccountModel rechargeAccountModel=new RechargeAccountModel();
        rechargeAccountModel.setId(accNo);
        List<RechargeAccountModel> list=this.rechargeAccountService.queryRechargeAccountList(rechargeAccountModel);
        if(list.size()==0){
            logger.error("获取用户账户信息失败（用户访问超时）");
            codeMess=new CodeMess("0","获取用户账户信息失败（用户访问超时）");
            return JSONObject.toJSONString(codeMess);
        }else{
            Map<String,String> map= (Map<String, String>) net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(list.get(0).getQueryParams()),HashMap.class);
            Document doc = null;
            String payProjectResult = HttpUtil.sendPost(this.oufeiBalanceUrl, "gb2312", map);
            logger.error("获取用户充值账户余额信息："+payProjectResult);
            doc = DocumentHelper.parseText(payProjectResult);
            Element rootElt = doc.getRootElement();
            String retcode=rootElt.elementTextTrim("retcode");
            if(!retcode.equals("1")){
                codeMess=new CodeMess("0",rootElt.elementTextTrim("err_msg"));
                return JSONObject.toJSONString(codeMess);
            }
            Iterator iter = rootElt.elementIterator("balances");
            JSONObject object=new JSONObject();
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                Iterator iterch = recordEle.elementIterator("balance");
                while (iterch.hasNext()) {
                    Element prov = (Element) iterch.next();
                    String accountName=prov.elementTextTrim("accountName");
                    String balance=prov.elementTextTrim("balance");
                    String balanceAmount=prov.elementTextTrim("balanceAmount");
                    String contractNo=prov.elementTextTrim("contractNo");
                    String contentId=prov.elementTextTrim("contentId");
                    String prepaidFlag=prov.elementTextTrim("prepaidFlag");
                    String payMentDay=prov.elementTextTrim("payMentDay");
                    String address=prov.elementTextTrim("address");
                    String param1=prov.elementTextTrim("param1");
                    object.put("accountName",accountName);
                    object.put("balance",balance);
                    object.put("balanceAmount",balanceAmount);
                    object.put("contractNo",contractNo);
                    object.put("payMentDay",payMentDay);
                    object.put("contentId",contentId);
                    object.put("address",address);
                    object.put("param1",param1);
                    object.put("prepaidFlag",prepaidFlag);
                }
            }
            session.setAttribute("queryBalanceParams",object);
            codeMess=new CodeMess("1",object.toString());
            return JSONObject.toJSONString(codeMess);
        }
    }
    /**
     * 修改用户家庭信息
     */
    @RequestMapping(value = "/updateFamily", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String updateFamily(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
        }
        String fId = request.getParameter("fId");
        String fName=request.getParameter("fName");
        FamilyModel familyModel=new FamilyModel();
        familyModel.setId(fId);
        familyModel.setFamilyName(fName);
        int result=this.familyService.updateFamilyModel(familyModel);
        codeMess=new CodeMess(String.valueOf(result),"");
        return  JSONObject.toJSONString(codeMess);
    }

    /**
     * 查询生活缴费账单
     */
    @RequestMapping(value = "/queryPayList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryPayList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        CodeMess codeMess;
        String userId = getUserId(session);
        if (userId == null) {
            logger.error("获取用户信息失败（用户访问超时）");
            codeMess=new CodeMess("3","获取用户信息失败（用户访问超时）");
            return JSONObject.toJSONString(codeMess);
        } else {
            session.setAttribute("userId", userId);
        }
        PayInfoModel payInfoModel=new PayInfoModel();
        payInfoModel.setAccountId(userId);
        List<PayInfoModel> list=this.payInfoServiceI.queryPayInfos(payInfoModel);
        if(list.size()==0){
            codeMess=new CodeMess("2","");
        }else{
            List<PayInfoModel> payList=new ArrayList<PayInfoModel>();
            for(PayInfoModel pay:list){
                String payName=pay.getDealInfo();
                try {
                    payName=payName.substring(0,payName.indexOf("("));
                } catch (Exception e) {
                    payName=payName;
                }
                if(payName.equals("电费充值")||payName.equals("水费充值")||payName.equals("燃气费充值")){
                    payList.add(pay);
                }
            }
            if(payList.size()==0){
                codeMess=new CodeMess("2","");
            }else{
                codeMess=new CodeMess("1",JSONArray.fromObject(payList).toString());
            }
        }
        return  JSONObject.toJSONString(codeMess);
    }
    /**
     * 查询用户的红包记录
     *
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "/queryRedPkgList",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String getRedPkg(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        JSONArray jsonArray=new JSONArray();
        CodeMess codeMess;
        String userId = getUserId(session);
        String openId= (String) session.getAttribute("openId");
        if (userId == null||openId==null) {
            logger.error("获取用户信息失败（用户访问超时）");
        } else {
            session.setAttribute("userId", userId);
            session.setAttribute("openId", openId);
        }
        PostOrGet post =new PostOrGet("utf-8");
        String result=post.sendPost(queryAllRedUrl, ESendHTTPModel._SEND_MODEL_DECODER, new NamedValue("openId",openId),new NamedValue("state","1"));
        logger.info("调用红包接口，结果为："+result);
        if(!org.springframework.util.StringUtils.isEmpty(result)){
            net.sf.json.JSONObject json= net.sf.json.JSONObject.fromObject(result);
            try {
                jsonArray=json.getJSONArray("data");
            } catch (Exception e) {
                logger.error("未获取到红包列表");
                jsonArray=new JSONArray();
            }
        }
        return jsonArray.toString();
    }

    /**
     * 查询用户的红包记录
     *
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "/redirectPayHome",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void redirectPayHome(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
       /* String userId = getUserId(session);
        if (userId == null) {
                logger.error("获取用户信息失败（用户访问超时）");
        }*/
        String payamount=request.getParameter("payamount");
        String rechargeType=request.getParameter("rechargeType");
        String lifeType=request.getParameter("lifeType");
        String redPkgId=request.getParameter("redPkgId");
        String redPkgValue=request.getParameter("redPkgValue");
        String accNo=request.getParameter("accNo");//账户ID
        RechargeAccountModel recharge=new RechargeAccountModel();
        FamilyModel familyModel=new FamilyModel();
        recharge.setId(accNo);
        List<RechargeAccountModel> rechargeList=this.rechargeAccountService.queryRechargeAccountList(recharge);
        if(rechargeList.size()==0){
            logger.error("获取用户信息超时（未查询到用户账户信息）");
        }
        recharge=rechargeList.get(0);
        familyModel.setId(recharge.getFamilyId());
        List<FamilyModel> familyModels=this.familyService.queryFamilyList(familyModel);
        if(familyModels.size()==0){
            logger.error("获取用户信息超时（未查询到用户家庭信息）");
        }
        familyModel=familyModels.get(0);

        JSONObject object= (JSONObject) session.getAttribute("queryBalanceParams");
        String sporderId=UUIDUtil.getUUID();
        StringBuffer md5Sign=new StringBuffer();
        md5Sign.append(oufeiLifeUserId);
        md5Sign.append(oufeiLifeUserPws);
        md5Sign.append(recharge.getCardId());
        md5Sign.append("1");
        md5Sign.append(sporderId);
        md5Sign.append(familyModel.getProvinceId());
        md5Sign.append(familyModel.getCityId());
        md5Sign.append(recharge.getAccountType());
        md5Sign.append(recharge.getPayUnitId());
        md5Sign.append(recharge.getPayAccount());
        md5Sign.append("NJJMTJMT");
        logger.error("md5=="+md5Sign.toString());
        String md5Str=MD5Util.MD5Encode(md5Sign.toString()).toUpperCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", oufeiLifeUserId);
        params.put("userpws", oufeiLifeUserPws);
        params.put("version", oufeiProvinceListVersion);
        params.put("provId",familyModel.getProvinceId());
        params.put("cityId",familyModel.getCityId());
        params.put("type",recharge.getAccountType());
        params.put("chargeCompanyCode",recharge.getPayUnitId());
        params.put("payModeId","");
        params.put("cardId",recharge.getCardId());
        params.put("cardnum","1");
        params.put("account",recharge.getPayAccount());
        params.put("ret_url","");//回调地址
        params.put("md5_str",md5Str);
        params.put("sporderId",sporderId);//测试欧飞单号
        params.put("actPrice","1.00");//金额
        params.put("payMentDay",getJsonOjectValues("payMentDay",object));
        params.put("param1",getJsonOjectValues("param1",object));
        params.put("contentId",getJsonOjectValues("contentId",object));
        try {
            params.put("contractNo",URLEncoder.encode(getJsonOjectValues("contractNo",object),"GBK"));
        } catch (Exception e) {
            logger.error("获取参数出错");
            params.put("contractNo","");
        }
        params.put("timestamp", DateFormatUtil.formateString());
//        String sendReult = HttpUtil.sendPost(this.oufeiUtilityOrderUrl, "gb2312", params);
        logger.error("充值参数:"+JSONObject.toJSONString(params));
        session.setAttribute("lifeRechargeParams",params);
        String md5str=MD5Util.MD5Encode(payamount+rechargeType+params.get("timestamp")).toUpperCase();
        session.setAttribute("payMd5",md5str);
        String targetUrl=orderPayUrl+"?payamount="+payamount+"&rechargeType="+rechargeType+"&lifeType="+lifeType+"&redPkgId="+redPkgId+"&redPkgValue="+redPkgValue;
        logger.error("跳转支付页面地址:"+targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    private String getJsonOjectValues(String key,JSONObject object){
        String val="";
        try {
            val=object.getString(val);
        } catch (Exception e) {
            val="";
            logger.error(key+"获取数据为空");
        }
        return  val;

    }

}

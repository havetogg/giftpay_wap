package org.jumutang.giftpay.web.count;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.model.SellCarInfo;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.CountModel;
import org.jumutang.giftpay.entity.ManagerCountModel;
import org.jumutang.giftpay.entity.PVUVRecordModel;
import org.jumutang.giftpay.model.*;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.jumutang.giftpay.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by RuanYJ on 2017/3/16.
 */
@Controller
@RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
public class CountController extends BaseController {

    @Autowired
    private PhoneQueryService phoneQueryService;

    private final static String RedPkgList = "http://www.linkgift.cn/giftcenter/queryAllRedpkgInfo";

    private final static String QUERYZSHBASEINFO="http://pay-wx.jspec.cn/zshwx_task/api/v1/day/report.do";

    private final static String GAMEUSERPRICEURL="https://prodone.juxinbox.com/sinopecGameCt/weixinMng/api/getAllUser.htm";


    /**
     * 统计平台登录
     * @param req
     * @param res
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
        CodeMess resResult =null;
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        if (StringUtils.isEmpty(userName)) {
            resResult=new CodeMess("0","用户名不能为空");
            return JSONObject.toJSONString(resResult);
        }
        if (StringUtils.isEmpty(userPassword)) {
            resResult=new CodeMess("0","密码不能为空");
            return JSONObject.toJSONString(resResult);
        }
        ManagerCountModel countModel=new ManagerCountModel();
        countModel.setUserName(MD5Util.MD5Encode(userName).toUpperCase());
        countModel.setUserPwd(MD5Util.MD5Encode(userPassword).toUpperCase());
        List<ManagerCountModel> list=this.managerCountService.queryManagerCountUserList(countModel);
        if (list.size() == 0) {
            resResult=new CodeMess("0","用户名密码不一致");
            return JSONObject.toJSONString(resResult);
        } else {
            session.setAttribute("loginInfo", list.get(0));
            resResult=new CodeMess("1",JSONObject.toJSONString(list.get(0)));
            return JSONObject.toJSONString(resResult);
        }
    }

    /**
     * 检查是否登录
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/isLogin")
    public String isLogin(HttpServletRequest req, HttpServletResponse res) throws Exception {
        CodeMess resResult =null;
        HttpSession session=req.getSession();
        ManagerCountModel userLoginModel= (ManagerCountModel) session.getAttribute("loginInfo");
        if(userLoginModel==null){
            resResult=new CodeMess("0","请先登录管理平台");
            return JSONObject.toJSONString(resResult);
        }else{
            resResult=new CodeMess("1",JSONObject.toJSONString(userLoginModel));
            session.setAttribute("loginInfo", userLoginModel);
            return JSONObject.toJSONString(resResult);
        }
    }

    /**
     * 注销用户
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/logOn")
    public String logOn(HttpServletRequest req, HttpServletResponse res) throws Exception {
        CodeMess resResult =null;
        HttpSession session=req.getSession();
        session.removeAttribute("loginInfo");
        resResult=new CodeMess("1","注销成功");
        return JSONObject.toJSONString(resResult);
    }



    @RequestMapping(value = "/countAllUserData")
    @ResponseBody
    public String countAllUserData(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        int countA = this.userSubService.queryAddCountByToday();
        int countB = this.userSubService.queryTotalAddCount();
        int countC = this.userSubService.queryPhoneUserCountByToday();
        int countD = this.userSubService.queryPhoneUserAllCount();
        int countE = this.userSubService.queryPhoneUserCountByZshToday();
        int countF = this.userSubService.queryAllZshUserCount();
        int countG = this.userSubService.queryOldUserCount();
        List<PVUVRecordModel> list = this.PVUVRecordService.queryAllPVUVRecord();

        JSONObject object = new JSONObject();
        object.put("dataA", countA);
        object.put("dataB", countB);
        object.put("dataC", countC);
        object.put("dataD", countD);
        object.put("dataE", countE);
        object.put("dataF", countF);
        object.put("dataG", countG);
        object.put("dataH", JSONArray.toJSONString(list));
        return object.toJSONString();
    }

    //九五折加油总数据
    @RequestMapping(value = "/countOilRecordTotalInfo")
    @ResponseBody
    public String countOilRecordTotalInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        JSONObject returnObj = new JSONObject();
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());

        //获取总数据
        int totalNum = recordModels.size(); //总份数
        double payAmount = 0;
        double totalAmount = 0;
        double totalAmountReturn = 0;
        for(OilRecordModel oilRecordModel:recordModels){
            payAmount = Double.parseDouble(oilRecordModel.getPayamount())+payAmount;
            totalAmount = Double.parseDouble(oilRecordModel.getTotalamount())+totalAmount;
            totalAmountReturn = Integer.parseInt(oilRecordModel.getTermsurplus())*Double.parseDouble(oilRecordModel.getMonthamount())+totalAmountReturn;
        }
        JSONObject total = new JSONObject();
        total.put("totalNum",totalNum);
        total.put("payAmount",payAmount);
        total.put("totalAmount",totalAmount);
        total.put("totalAmountReturn",totalAmountReturn);

        returnObj.put("total",total);
        return JSONArray.toJSONString(returnObj);
    }

    //九五折加油当天数据
    @RequestMapping(value = "/countOilRecordTodayInfo")
    @ResponseBody
    public String countOilRecordTodayInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        JSONObject returnObj = new JSONObject();
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());

        //获得当天数据
        int todayNum = 0;
        double todayPay = 0;
        double todayAmount = 0;
        Date date = new Date();
        SimpleDateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String TodayStr = todayFormatter.format(date);
        for(OilRecordModel oilRecordModel:recordModels){
            Date createDate = todayFormatter.parse(oilRecordModel.getCreatetime());
            String createStr = todayFormatter.format(createDate);
            if(TodayStr.equals(createStr)){
                todayNum++;
                todayPay += Double.parseDouble(oilRecordModel.getPayamount());
                todayAmount +=Double.parseDouble(oilRecordModel.getTotalamount());
            }
        }
        JSONObject today = new JSONObject();
        today.put("todayNum",todayNum);
        today.put("todayPay",todayPay);
        today.put("todayAmount",todayAmount);

        returnObj.put("today",today);

        return JSONArray.toJSONString(returnObj);
    }

    //九五折加油当月数据
    @RequestMapping(value = "/countOilRecordMonthInfo")
    @ResponseBody
    public String countOilRecordMonthInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        JSONObject returnObj = new JSONObject();
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());

        Date date = new Date();
        //获得当月数据
        int monthNum = 0;
        double monthPay = 0;
        double monthReturn = 0;
        SimpleDateFormat monthFormatter = new SimpleDateFormat("yyyy-MM");
        String monthStr = monthFormatter.format(date);
        for(OilRecordModel oilRecordModel:recordModels) {
            Date createDate = monthFormatter.parse(oilRecordModel.getCreatetime());
            String createStr = monthFormatter.format(createDate);
            if (monthStr.equals(createStr)) {
                monthNum++;
                monthPay += Double.parseDouble(oilRecordModel.getPayamount());
            }else{
                if(oilRecordModel.getStatus().equals("0")){
                    monthReturn+=Double.parseDouble(oilRecordModel.getMonthamount());
                }
            }
        }
        JSONObject month = new JSONObject();
        month.put("monthNum",monthNum);
        month.put("monthPay",monthPay);
        month.put("monthReturn",monthReturn);

        returnObj.put("month",month);

        return JSONArray.toJSONString(returnObj);
    }

    //九五折加油折线图数据
    @RequestMapping(value = "/countOilRecordLineInfo")
    @ResponseBody
    public String countOilRecordLineInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        JSONObject returnObj = new JSONObject();
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());

        SimpleDateFormat todayFormatter = new SimpleDateFormat("yyyy-MM-dd");

        //折线图数据
        List<String> dateList = DateFormatUtil.queryDaysList(15);
        Collections.reverse(dateList);
        Object[] dates =  dateList.toArray();
        int[] lineNum = new int[dates.length];
        for(int i = 0;i < dates.length;i++){
            int num = 0;
            for(OilRecordModel oilRecordModel:recordModels){
                Date createDate = todayFormatter.parse(oilRecordModel.getCreatetime());
                String createStr = todayFormatter.format(createDate);
                if(dates[i].toString().equals(createStr)){
                    num++;
                }
            }
            lineNum[i] = num;
        }
        JSONObject line = new JSONObject();
        line.put("dates",dates);
        line.put("lineNum",lineNum);

        returnObj.put("line",line);
        return JSONArray.toJSONString(returnObj);
    }

    //九五折加油表格数据
    @RequestMapping(value = "/countOilRecordRecordInfo")
    @ResponseBody
    public String countOilRecordRecordInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        JSONObject returnObj = new JSONObject();
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());

        //加油记录表
        List<String> userOpenIds = new ArrayList<>();
        Map<String,String> phoneMap = new HashMap<>();
        Map<String,String> AttributionMap = new HashMap<>();
        Map<String,String> numMap = new HashMap<>();
        Map<String,String> payMap = new HashMap<>();
        Map<String,String> amountMap = new HashMap<>();
        Map<String,String> returnMap = new HashMap<>();
        Map<String,String> createMap = new HashMap<>();
        Map<String,String> lastMap = new HashMap<>();
        for(OilRecordModel oilRecordModel:recordModels){
            if(!userOpenIds.contains(oilRecordModel.getOpenid())){
                userOpenIds.add(oilRecordModel.getOpenid());
                UserSubModel userSubModel = new UserSubModel();
                userSubModel.setOpenId(oilRecordModel.getOpenid());
                phoneMap.put(oilRecordModel.getOpenid(),userSubService.queryUserSubModel(userSubModel).get(0).getPhone());
                PhoneModel phoneModel = phoneQueryService.getPhone(userSubService.queryUserSubModel(userSubModel).get(0).getPhone());
                AttributionMap.put(oilRecordModel.getOpenid(),phoneModel.getProvince()+phoneModel.getCity());
                numMap.put(oilRecordModel.getOpenid(),"1");
                payMap.put(oilRecordModel.getOpenid(),oilRecordModel.getPayamount());
                amountMap.put(oilRecordModel.getOpenid(),oilRecordModel.getTotalamount());
                returnMap.put(oilRecordModel.getOpenid(),String.valueOf(Integer.parseInt(oilRecordModel.getTermsurplus())*Double.parseDouble(oilRecordModel.getMonthamount())));
                createMap.put(oilRecordModel.getOpenid(),oilRecordModel.getCreatetime());
                lastMap.put(oilRecordModel.getOpenid(),oilRecordModel.getCreatetime());
            }else{
                numMap.put(oilRecordModel.getOpenid(),String.valueOf((Integer.parseInt(numMap.get(oilRecordModel.getOpenid()))+1)));
                payMap.put(oilRecordModel.getOpenid(),String.valueOf(Double.parseDouble(payMap.get(oilRecordModel.getOpenid()))+Double.parseDouble(oilRecordModel.getPayamount())));
                amountMap.put(oilRecordModel.getOpenid(),String.valueOf(Double.parseDouble(amountMap.get(oilRecordModel.getOpenid()))+Double.parseDouble(oilRecordModel.getTotalamount())));
                returnMap.put(oilRecordModel.getOpenid(),String.valueOf(Double.parseDouble(returnMap.get(oilRecordModel.getOpenid()))+Integer.parseInt(oilRecordModel.getTermsurplus())*Double.parseDouble(oilRecordModel.getMonthamount())));
                createMap.put(oilRecordModel.getOpenid(),oilRecordModel.getCreatetime());
            }
        }
        JSONArray oilRecord = new JSONArray();
        for(String openId:userOpenIds){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("openId",openId);
            jsonObject.put("phone",phoneMap.get(openId));
            jsonObject.put("attribution",AttributionMap.get(openId));
            jsonObject.put("num",numMap.get(openId));
            jsonObject.put("pay",payMap.get(openId));
            jsonObject.put("amount",amountMap.get(openId));
            jsonObject.put("return",returnMap.get(openId));
            jsonObject.put("createTime",createMap.get(openId));
            jsonObject.put("lastTime",lastMap.get(openId));
            oilRecord.add(jsonObject);
        }
        returnObj.put("oilRecord",oilRecord);
        return JSONArray.toJSONString(returnObj);
    }

    @RequestMapping(value = "/countBalanceAllInfo")
    @ResponseBody
    public String countBalanceAllInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<BalanceModel> balanceModels = this.balanceServiceI.queryBalancesNoNull(new BalanceModel());
        return JSONArray.toJSONString(balanceModels);
    }

    @RequestMapping(value = "/countSellCarAllInfo")
    @ResponseBody
    public String countSellCarAllInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<SellCarInfo> sellCarInfos = this.sellCarInfoService.queryAllSellCarInfo(new SellCarInfo());
        return JSONArray.toJSONString(sellCarInfos);
    }

    @RequestMapping(value = "/countPayAllInfo")
    @ResponseBody
    public String countPayAllInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<PayInfoModel> payInfoModels = this.payInfoServiceI.queryAllPayInfos(new PayInfoModel());
        return JSONArray.toJSONString(payInfoModels);
    }

    @RequestMapping(value = "/countRedPkgAllInfo")
    @ResponseBody
    public String countRedPkgAllInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String state = request.getParameter("state");
        Map<String, String> map = new HashMap<String, String>();
        map.put("state", state);
        String result = HttpUtil.sendPost(RedPkgList, "utf-8", map);
        return result;
    }

    @RequestMapping(value = "/countAllUserBaseInfo")
    @ResponseBody
    public String countAllUserBaseInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<UserMainModel> list = this.userMainService.queryUserCount(new UserMainModel());
        List<UserMainModel> newList=new ArrayList<>();
        for(UserMainModel user:list){
            PhoneModel model=this.phoneQueryService.getPhone(user.getPhone());
            if(model==null){
                user.setCity("未知");
            }else{
                user.setCity(model.getProvince()+model.getCity());
            }
            newList.add(user);
        }
        return JSONArray.toJSONString(newList);
    }


    //获取中石化线上线下基本数据
    @RequestMapping(value = "/queryZshBaseInfo")
    @ResponseBody
    public String queryZshBaseInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        CodeMess codeMess=null;
        String date=DateFormatUtil.queryDaysObject(1);//查询前一天的数据
        Map<String,String> params=new HashMap<>();
        params.put("queryDate",date);
        String result= HttpClientUtil.doHttpPost(QUERYZSHBASEINFO,params);
        logger.error("链接:"+QUERYZSHBASEINFO+",参数:"+JSONArray.toJSONString(params));
        logger.error("查询中石化数据返回："+result);
        if(result!=null&& StringUtils.isNotEmpty(result)){
            codeMess=new CodeMess("0",result);
        }else{
            codeMess=new CodeMess("1","查询失败");
        }
     return JSONObject.toJSONString(codeMess);
    }




    //获取中石化线上线下基本数据图表
    @RequestMapping(value = "/queryZshBaseInfoEchart")
    @ResponseBody
    public String queryZshBaseInfoEchart(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        CodeMess codeMess=null;
        String startTime=DateFormatUtil.queryDaysObject(30);//查询前一天的数据
        String endTime=DateFormatUtil.queryDaysObject(0);
        Map<String,String> params=new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        System.out.println(JSONArray.toJSONString(params));
        String name="http://pay-wx.jspec.cn/zshwx_task/api/v1/report/list.do";
        String result= HttpClientUtil.doHttpPost(name,params);
        logger.error("查询中石化数据返回："+result);
        if(result!=null&& StringUtils.isNotEmpty(result)){
            codeMess=new CodeMess("0",result);
        }else{
            codeMess=new CodeMess("1","查询失败");
        }
        return JSONObject.toJSONString(codeMess);
    }


    @RequestMapping(value = "/countEchartData")
    @ResponseBody
    public String countEchartData(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        CountModel countModel = new CountModel();
        //统计天数
        List<String> dateList = DateFormatUtil.queryDaysList(15);
        Map<String, List<CountModel>> maps = new HashMap<String, List<CountModel>>();
        Map<String, List<CountModel>> newMaps = new HashMap<String, List<CountModel>>();
        countModel.setStartTime(dateList.get(dateList.size()-1));
        countModel.setEndTime(dateList.get(0));
        countModel.setCountType(CountModel.NEWUSER);
        maps.put(countModel.getCountType(), this.countService.queryCountModelByType(countModel));
        countModel.setCountType(CountModel.REGISTERUSER);
        maps.put(countModel.getCountType(), this.countService.queryCountModelByType(countModel));
        countModel.setCountType(CountModel.ZSHUSER);
        maps.put(countModel.getCountType(), this.countService.queryCountModelByType(countModel));
        countModel.setCountType(CountModel.FULIUSER);
        maps.put(countModel.getCountType(), this.countService.queryCountModelByType(countModel));
        countModel.setCountType(CountModel.OTHERUSER);
        maps.put(countModel.getCountType(), this.countService.queryCountModelByType(countModel));

        for(String key:maps.keySet()){
            List<CountModel> cc=regexCountTime(maps.get(key),dateList);
            ListSort(cc);
            newMaps.put(key,cc);
        }
        return JSONArray.toJSONString(newMaps);
    }
    @RequestMapping(value = "/countOilRecordAllInfo")
    @ResponseBody
    public String countOilRecordAllInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        List<OilRecordModel> recordModels = this.oilRecordService.queryOilRecordModelListMobile(new OilRecordModel());
        return JSONArray.toJSONString(recordModels);
    }


    @RequestMapping(value = "/countGameUserPrice")
    @ResponseBody
    public String countGameUserPrice(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, URISyntaxException {
        Map<String, String> paramMap = new HashMap<String, String>();
        String result=HttpClientUtil.doHttpPost(GAMEUSERPRICEURL,paramMap);
        logger.error("result");
        return result;
    }
    public List<CountModel> regexCountTime(List<CountModel> newCount,List<String> ss) {
        List<String> dateList=new ArrayList<>();
        dateList.addAll(ss);
        List<String> dataTimeList=new ArrayList<>();
        for(CountModel cc:newCount){
            dataTimeList.add(cc.getAddTime());
        }
        dateList.removeAll(dataTimeList);
        logger.error("不存在的日期:"+dateList+",数据日期:"+dataTimeList+",查询日期:"+dateList);
        if(dateList.size()>0){
            //存在差集 遍历
            List<CountModel> cc=new ArrayList<>();
            for(String str:dateList){
                CountModel newC=new CountModel();
                newC.setAddTime(str);
                newC.setNum("0");
                cc.add(newC);
            }
            newCount.addAll(cc);
        }
        return newCount;
    }

    private static void ListSort(List<CountModel> list) {
        Collections.sort(list, new Comparator<CountModel>() {
            @Override
            public int compare(CountModel o1, CountModel o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(o1.getAddTime());
                    Date dt2 = format.parse(o2.getAddTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        String startTime=DateFormatUtil.queryDaysObject(30);//查询前一天的数据
        String endTime=DateFormatUtil.queryDaysObject(0);
        Map<String,String> params=new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        System.out.println(JSONArray.toJSONString(params));
        String name="http://pay-wx.jspec.cn/zshwx_task/api/v1/report/list.do";
        String result= HttpClientUtil.doHttpPost(name,params);
    }
}

package org.jumutang.giftpay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.model.CarValuation;
import org.jumutang.caranswer.model.SellCarInfo;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.service.CarValuationService;
import org.jumutang.caranswer.service.SellCarInfoService;
import org.jumutang.caranswer.service.impl.SellCarInfoServiceImpl;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.tools.MobileMessageUtil;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import ch.qos.logback.core.net.SyslogOutputStream;
import net.sf.json.JSONObject;

/**
 * 汽车快速估值
 *
 * @author oywj
 */
@Controller
@RequestMapping(value = "/wechat/carValuation", method = {RequestMethod.GET, RequestMethod.POST})
public class CarValuationController {

    //配置打印日志Logger
    private static final org.slf4j.Logger _LOGGER = LoggerFactory.getLogger(CarValuationController.class);

    @Value("#{propertyFactoryBean['token']}")
    protected String token_car;
    @Value("#{propertyFactoryBean['car_valuation_url']}")
    protected String car_valuation_url;
    @Value("#{propertyFactoryBean['car_sell_url']}")
    protected String car_sell_url;
    @Value("#{propertyFactoryBean['car_city_url']}")
    protected String car_city_url;
    @Value("#{propertyFactoryBean['car_all_city']}")
    protected String car_all_city;


    @Autowired
    private SellCarInfoService sellCarInfoService;

    @Autowired
    private CarValuationService carValuationService;
    
    
    /**
     * 快速估值汽车价格
     *
     * @param modelId
     * @param regDate
     * @param mile
     * @param zone
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/valuation", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String valuation(HttpSession session,
            @RequestParam(value = "modelId", required = true) String modelId,
            @RequestParam(value = "regDate", required = true) String regDate,
            @RequestParam(value = "mile", required = true) String mile,
            @RequestParam(value = "tel", required = true) String tel,
            @RequestParam(value = "city", required = true) String zone,
            @RequestParam(value = "msgCode" , required = true) String msgCode
    ) throws IOException {
    	   
    	
    	    JSONObject object = new JSONObject();
        	UserModel userModel = (UserModel) session.getAttribute("wxUser");
    	      if (userModel == null || StringUtils.isEmpty(userModel.getOpenId())) {
    	    	 object.put("code", "1");
    	    	 object.put("msg", "请求超时，请重新登录");
    	    	 return object.toString();
              }  
    	      
    	      
    	   CarValuation  carValuation =new CarValuation();
    	   carValuation.setOpenId(userModel.getOpenId());//
    	   carValuation.setTel(tel);
    	   
    	   List<CarValuation> list = carValuationService.isExist(carValuation);
    	   
    	   if(list.size()==0){
    		   _LOGGER.error("插入一条记录,carValuation参数为"+carValuation.toString());
    		   int num = carValuationService.addValuationInfo(carValuation);
        	   if(num!=1){
        		   object.put("code", "2");
      	    	   object.put("msg", "业务失败");
      	    	   return object.toString();
        	   }
    	   }
    	
    	   //校验手机验证码是否正确
    	   String phoneMsgCode = (String) session.getAttribute("phoneMsgCode");
    	   _LOGGER.error("短信发送的验证码："+phoneMsgCode+",用户输入的验证码："+msgCode);
    	   if(!msgCode.equals(phoneMsgCode)){
    		   //校验不通过
    		   object.put("code", "3");
    		   object.put("msg", "验证码输入错误");
    		   return object.toString();
    	   }
    	   
        PostOrGet post = new PostOrGet("utf-8");
        //String targetUrl="http://testapi.che300.com/service/getUsedCarPrice";
        String jsonResult = new PostOrGet("utf-8").sendGet(car_valuation_url,
                ESendHTTPModel._SEND_MODEL_ENCODE,
                new NamedValue("token", token_car),
                new NamedValue("modelId", modelId),
                new NamedValue("regDate", regDate),
                new NamedValue("mile", mile),
                new NamedValue("zone", zone)
        );
       
        String msg = "";
        JSONObject json = JSONObject.fromObject(jsonResult);
        System.out.println(json.toString());
        String status = (String) json.get("status");
      
        if ("0".equals(status)) {
            msg = (String) json.get("eval_price");
        } else {
            msg = json.get("dealer_buy_price") + "";
        }
        object.put("code", "0");
        object.put("msg", msg);
        return object.toString();
    }

    /**
     * 提交卖车申请
     *
     * @param session
     * @param model
     * @param brand
     * @param series
     * @param regDate
     * @param city
     * @param tel
     * @return
     */
    @RequestMapping(value = "/sell", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String sell(HttpSession session,
                       @RequestParam(value = "model", required = true) String model,
                       @RequestParam(value = "brand", required = true) String brand,
                       @RequestParam(value = "series", required = true) String series,
                       @RequestParam(value = "regDate", required = true) String regDate,
                       @RequestParam(value = "city", required = true) String city,
                       @RequestParam(value = "tel", required = true) String tel,
                       @RequestParam(value = "status", required = true) String status
    ) {

        //调用接口获得当前城市帮卖平台列表
        String result = new PostOrGet("utf-8").sendGet(car_city_url, ESendHTTPModel._SEND_MODEL_ENCODE,
                new NamedValue("token", token_car), new NamedValue("city", city));
        JSONObject json = JSONObject.fromObject(result);
        net.sf.json.JSONArray array = json.getJSONArray("channels");
        String channelStatus = json.getString("status");
        if (channelStatus.equals("0")) {
            JSONObject object = new JSONObject();
            object.put("status", "1");
            object.put("error_msg", "获取帮买城市平台数据异常");
            return object.toString();
        }
        //遍历json数组，得到帮卖平台名称
        String channelName = "";
        for (int i = 0; i < array.size(); i++) {
            //把每一个对象转化为json对象
            JSONObject object = (JSONObject) array.get(i);
            String chaName = object.getString("channel_name");
            if (i == array.size() - 1) {
                channelName += chaName;
            } else {
                channelName += chaName + ",";
            }
        }
        String[] channelList = channelName.split(",");
        int index = (int) (Math.random() * channelList.length);
        channelName = channelList[index];
        //从session中获得用户信息
//		UserModel user=(UserModel) session.getAttribute("userInfo");
//		String sellerName=user.getWechatNikeName();
//		String openid=user.getOpenId();
        UserModel userModel = (UserModel) session.getAttribute("wxUser");
        NamedValue namedValue;
        if (userModel == null) {
            namedValue = new NamedValue("sellerName", "giftpay");
        } else {
            namedValue = new NamedValue("sellerName", userModel.getWechatNikeName());
        }
        SellCarInfo carInfoModel = new SellCarInfo();
        if (status.equals("1")) {
            //先将卖车信息存入数据库
            if (userModel == null) {
                carInfoModel.setOpenId("giftpay_wap");
                carInfoModel.setSellerName("giftpay_wap");
            } else {
                carInfoModel.setOpenId(userModel.getOpenId());
                carInfoModel.setSellerName(userModel.getWechatNikeName());
            }
        } else {
            carInfoModel.setOpenId("中石化福利中心");
            carInfoModel.setSellerName("中石化福利中心");
        }
        carInfoModel.setId(UUIDUtil.getUUID());
        carInfoModel.setBrand(brand);
        carInfoModel.setSeries(series);
        carInfoModel.setModel(model);
        carInfoModel.setLocation(city);
        carInfoModel.setTel(tel);
        carInfoModel.setSellChannel(channelName);
        carInfoModel.setRegDate(regDate);
        carInfoModel.setClue_id("");
        int num = sellCarInfoService.addCarInfo(carInfoModel);
        _LOGGER.error("将卖车信息存入数据库，carInfoModel参数为：" + carInfoModel.toString());
        if (num == 1) {
           /* // 提交帮卖信息
            String jsonResult = new PostOrGet("utf-8").sendGet(car_sell_url,
                    ESendHTTPModel._SEND_MODEL_ENCODE,
                    new NamedValue("token", token_car),
                    new NamedValue("brand", brand),
                    new NamedValue("series", series),
                    new NamedValue("model", model),
                    new NamedValue("location", city),
                    new NamedValue("tel", tel),
                    namedValue,
                    new NamedValue("sellChannel", channelName),
                    new NamedValue("regDate", regDate)
            );
            JSONObject object = JSONObject.fromObject(jsonResult);
            //如果帮卖信息提交成功，则将更新数据库中的卖车信息
            if ("1".equals(object.getString("status"))) {
                SellCarInfo carInfo = new SellCarInfo();
                carInfo.setOpenId(userModel.getOpenId());
                carInfo.setModel(model);
                carInfo.setTel(tel);
                carInfo.setRegDate(regDate);
                carInfo.setClue_id(object.getString("clue_id"));
                sellCarInfoService.updateCarInfo(carInfo);
                _LOGGER.error("更新卖车信息，将提交成功后返回的车源线索clue_id=" + object.getString("clue_id") + "存入数据库");
            }*/
            JSONObject object = new JSONObject();
            object.put("status", "1");
            return object.toString();
        } else {
            JSONObject object = new JSONObject();
            object.put("status", "2");
            object.put("error_msg", "业务失败");
            _LOGGER.error("卖车信息存储失败");
            return object.toString();
        }
    }

    /**
     * 查询帮卖城市信息
     *
     * @param balanceModel
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllCity")
    public String getAllCity(BalanceModel balanceModel, HttpServletRequest request) {
        String jsonResult = new PostOrGet("utf-8").sendGet(car_all_city,
                ESendHTTPModel._SEND_MODEL_ENCODE,
                new NamedValue("token", token_car)
        );
        return jsonResult;
    }
    
    /**
     * 发送手机验证码
     * @param request
     * @param session
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping("/sendPhoneMsg")
    public String  sendMessage(HttpServletRequest request ,HttpSession session) throws UnsupportedEncodingException{
    	   String tel = request.getParameter("tel");
           Random random = new Random();
           int valNum = random.nextInt(899999);
           valNum = valNum + 100000;//随机六位数
           String msgContent = "【有礼付】您的验证码是" + valNum + "，如非本人操作请忽略该短信。";
           _LOGGER.error("发送短信验证码为：" + valNum);
           net.sf.json.JSONObject jsonObject = MobileMessageUtil.sendMessage(tel, msgContent);
           _LOGGER.error("发送短信结果返回：" + jsonObject);
           session.setAttribute("phoneMsgCode", String.valueOf(valNum));
         //TimerTask实现1分钟后从session中删除phoneMsgCode  
           final Timer timer=new Timer();  
           timer.schedule(new TimerTask() {  
               @Override  
               public void run() {  
                   session.removeAttribute("phoneMsgCode");  
                   _LOGGER.error("phoneMsgCode删除成功");  
                   timer.cancel();  
               }  
           },5*60*1000);
		return jsonObject.toString();
    }
    
    
}

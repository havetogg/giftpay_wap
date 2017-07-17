package org.jumutang.giftpay.web.giftUserCount;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.ChannelCountModel;
import org.jumutang.giftpay.entity.ThirdUserModel;
import org.jumutang.giftpay.model.PhoneModel;
import org.jumutang.giftpay.service.ChannelCountService;
import org.jumutang.giftpay.service.PhoneQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/19.
 */

@Controller
@RequestMapping(value = "/giftGO",method = {RequestMethod.POST,RequestMethod.GET})
public class Giftpay_ChannelController extends BaseController {


    @Autowired
    private PhoneQueryService phoneQueryService;

    @Autowired
    private ChannelCountService channelCountService;

    //添加渠道数据 [存在渠道数据，更新当前渠道码点击率]
    @RequestMapping(value = "/addChannelData",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String addChannelData(HttpServletRequest request , HttpServletResponse response , HttpSession session){

        JSONObject jsonObject = new JSONObject();

        String channel_code = request.getParameter("third_name");
        logger.info("添加渠道码数据中从请求中获取当前渠道码:[channel_code:"+channel_code+"]");

        //判断当前渠道号码是否为空，
//        if("".equals(channel_code) ){
        if(openIdISNull(channel_code)){
            logger.info("---当前渠道码获取为空！添加或更新渠道数据异常---");
//            jsonObject.put("result","false");
//            jsonObject.put("code","9999");
//            jsonObject.put("mess","渠道码为空！添加异常");
//            return jsonObject.toString();

            //当前正在进行次操作的用户
            String openId = String.valueOf(session.getAttribute("openId"));
            logger.info("渠道为null，当前访问用户--openid：["+openId+"]");
            //渠道码重新赋值[公司]
            channel_code="zfwcy";
            logger.info("渠道为null，将null渠道码重新赋值zfwcy");
        }

//        String third_name =String .valueOf( session.getAttribute("third_name"));
//        if(third_name.equals(channel_code)){
//            logger.info("当前页面中获取渠道码与session中获取渠道码一致!");
//        }else{
//            logger.info("渠道码获取不一致，当前系统异常!");
//            jsonObject.put("result","false");
//            jsonObject.put("code","9989");
//            jsonObject.put("mess","渠道码获取不一致，当前系统异常");
//            return  jsonObject.toString();
//        }

        logger.info("当前渠道码["+channel_code+"]！查询当前是否存在，准备更新或者不操作.....");
        ChannelCountModel cm = new ChannelCountModel();
        cm.setChannel_code(channel_code);
        cm.setExist(1);
        int count = channelCountService.channelIsExits(cm);

        if(count>0){
            logger.info("当前渠道码已经存在！更新当前点击率");
            int count2 = channelCountService.channelUpdateClickNum(cm);

            switch (count2){
                case 1:
                    logger.info("更新当前渠道码点击率成功!");
                    jsonObject.put("code","0010");
                    jsonObject.put("result","success");
                    jsonObject.put("mess","更新当前渠道码点击率成功");
                    break;
                default :
                    logger.info("渠道码更新失败，当前渠道码为["+channel_code+"]!");
                    jsonObject.put("code","0100");
                    jsonObject.put("result","false");
                    jsonObject.put("mess","渠道码更新失败");
                    break;
            }

            return jsonObject.toString();

        }else{
            logger.info("当前渠道码不存在，创建渠道数据");
            int count3 = channelCountService.channelAdd(cm);
            switch (count3){
                case 1:
                    logger.info("渠道码数据创建成功!");
                    jsonObject.put("code","0020");
                    jsonObject.put("result","success");
                    jsonObject.put("mess","渠道码数据创建成功");
                    break;
                default :
                    logger.info("渠道码数据创建失败，当前渠道码为["+channel_code+"]!");
                    jsonObject.put("code","0200");
                    jsonObject.put("result","false");
                    jsonObject.put("mess","渠道码数据创建失败");
                    break;
            }

            return jsonObject.toString();
        }

    }


    //查询渠道所有数据 [条件:渠道码channle_code，渠道描述channel_desc，渠道状态exist ]
    @RequestMapping(value = "/searchChannelList",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String searchChannelModelList (HttpServletRequest request , HttpServletResponse response){

        logger.info("查询当前渠道所有数据.....");

        ChannelCountModel cm = new ChannelCountModel();

        String channel_code = request.getParameter("channel_code");
        if(channel_code!=null && !"".equals(channel_code)){
            logger.info("渠道码channel_code.....["+channel_code+"]");
            cm.setChannel_code(channel_code);
        }

        String channel_desc = request.getParameter("channel_desc");
        if(channel_desc!=null && !"".equals(channel_desc)){
            logger.info("渠道描述channel_desc.....["+channel_desc+"]");
            cm.setChannel_desc(channel_desc);
        }

        String exits = request.getParameter("exits");
        if(exits!=null && !"".equals(exits)){
            logger.info("渠道码是否存在exits.....["+exits+"]");
            cm.setExist(Integer.parseInt(exits));
        }

        ArrayList<ChannelCountModel> list = channelCountService.channelList(cm);

        String json  = JSONObject.toJSONString(list);
        logger.info("当前返回查询渠道所有数据:"+json.toString());

        return json;
    }

    //查询渠道数据[两表联查，和thirdUser表。查询成功点击办卡用户和当前渠道号点击率]
    @RequestMapping(value = "/searchDataAll",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String searchDataAll (){

        logger.info("--[两表联查]--当前查询渠道码的点击率，点击办卡次数.......");

        ArrayList<ChannelCountModel> list = channelCountService.searchAllData();

        String json = JSONObject.toJSONString(list);

        logger.info("当前查询数据为:"+json);

        return json;
    }

    @RequestMapping(value = "/searchThirdUserList",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String searchThirdUserList (HttpServletRequest request,HttpServletResponse response){

        logger.info("查询当前thirdUser表办卡信息数据.......");

        String phone =request.getParameter("phone") ; //手机号
        String third_name = request.getParameter("third_name") ;//渠道码
        String  openId = request.getParameter("openId") ; //用户openId

        ThirdUserModel th  = new ThirdUserModel();

        if(!"".equals(phone) && phone!=null){
            th.setPhone(phone);
        }
        if(!"".equals(third_name) && third_name!=null){
            th.setThirdName(third_name);
        }
        if(!"".equals(openId) && openId!=null){
            th.setName(openId);
        }

        logger.info("当前参数状态:[phone="+phone+"],[third_name="+third_name+"],[openId="+openId+"]");

        ArrayList<ThirdUserModel> list = channelCountService.searchThirdUserList(th);

        int i = 0;
        for(ThirdUserModel thirdUserModel:list){
            PhoneModel phoneModel = phoneQueryService.getPhone(list.get(i).getPhone());
            list.get(i).setPhone(phoneModel.getPhone()+"("+phoneModel.getProvince()+phoneModel.getCity()+")");
            i++;
        }

        String json = JSONObject.toJSONString(list);

        logger.info("当前查询thirdUser表的数据为:"+json);

        return json;
    }

    //当前渠道码建立用户总数
    @RequestMapping(value = "/searchChannelCount")
    @ResponseBody
    public String searchChannelCount (){

        ChannelCountModel channelCountModel =  new ChannelCountModel();
        int count = channelCountService.channelIsExits(channelCountModel);

        logger.info("查询当前已经建立渠道号用户数量:"+count);
        return String.valueOf(count);
    }


}

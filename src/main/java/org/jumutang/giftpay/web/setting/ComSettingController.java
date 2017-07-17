package org.jumutang.giftpay.web.setting;

/**
 * Created by RuanYJ on 2017/1/14.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jumutang.caranswer.wechat.CodeMess;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.model.AdvertiseModel;
import org.jumutang.giftpay.model.AdvertiseRecordModel;
import org.jumutang.giftpay.tools.IPUtil;
import org.jumutang.giftpay.tools.MD5Util;
import org.jumutang.giftpay.tools.MD5X;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 用户基本设置
 * 判断用户超时，是否绑定手机等信息
 */
@Controller
@RequestMapping(value = "/giftpay/commonSetting")
public class ComSettingController extends BaseController{

    @RequestMapping(value = "/queryAdvertiseList")
    @ResponseBody
    public String queryAdvertise(HttpServletRequest request, HttpServletResponse response){
        String name=request.getParameter("name");
        AdvertiseModel advertiseModel=new AdvertiseModel();
        if(!StringUtils.isEmpty(name)){
            advertiseModel.setAdvertiseName(name);
        }
        List<AdvertiseModel> list=this.advertiseService.queryAdvertiseList(advertiseModel);
        return JSONArray.toJSONString(list);
    }

    @RequestMapping(value = "/queryRandomAdvertise")
    @ResponseBody
    public String queryRandomAdvertise(HttpServletRequest request, HttpServletResponse response){
        AdvertiseModel advertiseModel=new AdvertiseModel();
        List<AdvertiseModel> list=this.advertiseService.queryAdvertiseList(advertiseModel);
        int min = 1;// 随机数最小值
        int max = 10000;// 随机数最大值
        int limit1 = Integer.parseInt(list.get(0).getRate()) * 100;
        int limit2 = Integer.parseInt(list.get(1).getRate()) * 100;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        logger.error("几率:"+s);
        List<AdvertiseModel> arrList=new ArrayList<>();
        if (s <= limit1) {// 80几率
            arrList.add(list.get(0));
        }else{
            arrList.add(list.get(1));
        }
        return JSONArray.toJSONString(arrList);
    }

    //线上点击广告数更新统计
    @RequestMapping(value = "/updateAdverClickNumOL")
    @ResponseBody
    public String updateAdverClickNumOL(HttpServletRequest request, HttpServletResponse response){
        String id=request.getParameter("id");
        AdvertiseModel advertiseModel=new AdvertiseModel();
        advertiseModel.setAdvertiseId(id);
        int result=this.advertiseService.updateAdverClickNum(advertiseModel);
        CodeMess codeMess=new CodeMess(String.valueOf(result),"");
        return JSONObject.toJSONString(codeMess);
    }

    /**
     * 线下点击广告数更新统计
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateAdverClickNum")
    @ResponseBody
    public String updateAdverClickNum(HttpServletRequest request, HttpServletResponse response){
        String id=request.getParameter("id");
        String empno=request.getParameter("empno");
        //判断是否存在点击广告记录表
        AdvertiseRecordModel advertiseRecordModel=new AdvertiseRecordModel();
        advertiseRecordModel.setIp(empno);
        List<AdvertiseRecordModel> list=this.advertiseRecordService.queryAdvertiseRecordList(advertiseRecordModel);
        logger.error("是否点击过广告："+list.size());
        if(list.size()==0){
            //添加点击广告记录表
            advertiseRecordModel.setIp(IPUtil.getIpAddr(request));
            advertiseRecordModel.setAdvertiseType(id);
            this.advertiseRecordService.addAdvertiseRecordModel(advertiseRecordModel);
        }
        //更新广告点击数
        AdvertiseModel advertiseModel=new AdvertiseModel();
        advertiseModel.setAdvertiseId(id);
        int result=this.advertiseService.updateAdverClickNum(advertiseModel);
        CodeMess codeMess=new CodeMess(String.valueOf(result),"");
        return JSONObject.toJSONString(codeMess);
    }

    @RequestMapping(value = "/queryAllGoodsList")
    @ResponseBody
    public String queryAllGoodsList(HttpServletRequest request, HttpServletResponse response){
        CodeMess codeMess=null;
        String goodsType=request.getParameter("goodsType");//商品大类 1加油卡充值  2话费充值  3流量充值
        String goodsSubType=request.getParameter("goodsSubType");//商品小类
        String status=request.getParameter("status");//商品状态
        String path =request.getParameter("page");//商品状态
        logger.error("获取商品信息页面地址:"+path);
        if(path.equals("cellPhone_recharge")){
            if(!goodsType.equals("2")){
                codeMess=new CodeMess("0","商品数据异常");
                logger.error("未输入商品查询大类");
                return JSONObject.toJSONString(codeMess);
            }
        }else if(path.equals("fuelCard_recharge_new")){
            if(!goodsType.equals("1")){
                codeMess=new CodeMess("0","商品数据异常");
                logger.error("未输入商品查询大类");
                return JSONObject.toJSONString(codeMess);
            }
        }else if(path.equals("flow_recharge")){
            if(!goodsType.equals("3")){
                codeMess=new CodeMess("0","商品数据异常");
                logger.error("未输入商品查询大类");
                return JSONObject.toJSONString(codeMess);
            }
        }else{
            codeMess=new CodeMess("0","商品数据异常");
            logger.error("未输入商品查询大类");
            return JSONObject.toJSONString(codeMess);
        }

        GoodsListModel goods=new GoodsListModel();
        if(StringUtils.isNotEmpty(goodsType)){
            goods.setGoodsType(goodsType);
        }else{
            codeMess=new CodeMess("0","商品数据异常");
            logger.error("未输入商品查询大类");
            return JSONObject.toJSONString(codeMess);
        }
        if(StringUtils.isNotEmpty(goodsSubType)){
            goods.setGoodsSubType(goodsSubType);
        }
        if(StringUtils.isNotEmpty(status)){
            goods.setStatus(status);
        }else{
            goods.setStatus("0");
        }
        List<GoodsListModel> goodsList=this.goodsListService.queryGoodsList(goods);
        if(goodsList.size()==0){
            codeMess=new CodeMess("0","商品数据异常");
            logger.error("未查询到商品信息列表");
            return JSONObject.toJSONString(codeMess);
        }else{
            codeMess=new CodeMess("1",JSONArray.toJSONString(goodsList));
            return JSONObject.toJSONString(codeMess);
        }
    }


}

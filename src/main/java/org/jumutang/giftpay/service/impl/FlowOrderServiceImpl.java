package org.jumutang.giftpay.service.impl;

import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.jumutang.giftpay.dao.FlowOrderDao;
import org.jumutang.giftpay.entity.FlowOrder;
import org.jumutang.giftpay.entity.Traffic;
import org.jumutang.giftpay.service.FlowOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/5.
 */

@Service("flowOrderService")
public class FlowOrderServiceImpl implements FlowOrderService {

    @Autowired
    private FlowOrderDao flowOrderDao;

    @Override
    public int AddFlowOrder(FlowOrder flowOrder) {
        return flowOrderDao.AddFlowOrder(flowOrder);
    }

    @Override
    public int updateFlowStatus(String orderId) {
        return flowOrderDao.updateFlowStatus(orderId);
    }

    @Override
    public ArrayList<FlowOrder> searchFlowOrderList() {
        return flowOrderDao.searchFlowOrderList();
    }

    private JSONArray trafficOrder(Traffic traffic){
        String[] packCodeArr = traffic.getPackCode().split("\\|");
        JSONArray jsonArr = new JSONArray();
        JSONObject json = new JSONObject();
        for(int i = 0;i <packCodeArr.length;i++){
            String packCode = packCodeArr[i];
            String mobile = traffic.getMobile().split("\\|")[i];
            json.put("PackCode", packCode);
            json.put("Mobile", mobile);
            json.put("EffectType", Traffic.EFFECT_TYPE_BUY);
            jsonArr.add(json);
        }
        return jsonArr;
    }


}


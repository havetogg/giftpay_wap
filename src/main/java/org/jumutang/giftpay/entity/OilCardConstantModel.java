package org.jumutang.giftpay.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by RuanYJ on 2017/6/8.
 */
public class OilCardConstantModel {

    public static final String CARDNUM(String oilId){
        if(oilId.equals("1")){
            return JSONObject.toJSONString(new OilCardModel("64157005","50","全国中石化加油卡直充50元"));
        }
        if(oilId.equals("6")){
            return JSONObject.toJSONString(new OilCardModel("64157004","100","全国中石化加油卡直充100元"));
        }
        if(oilId.equals("7")){
            return JSONObject.toJSONString(new OilCardModel("64157003","200","全国中石化加油卡直充200元"));
        }
        if(oilId.equals("8")){
            return JSONObject.toJSONString(new OilCardModel("64157002","500","全国中石化加油卡直充500元"));
        }
        if(oilId.equals("9")){
            return JSONObject.toJSONString(new OilCardModel("64157001","1000","全国中石化加油卡直充1000元"));
        }



        if(oilId.equals("10")){
            return JSONObject.toJSONString(new OilCardModel("64349107","50","全国中石油加油卡直充50元"));
        }
        if(oilId.equals("11")){
            return JSONObject.toJSONString(new OilCardModel("64349106","100","全国中石油加油卡直充100元"));
        }
        if(oilId.equals("12")){
            return JSONObject.toJSONString(new OilCardModel("64349105","200","全国中石油加油卡直充200元"));
        }
        if(oilId.equals("13")){
            return JSONObject.toJSONString(new OilCardModel("64349103","500","全国中石油加油卡直充500元"));
        }
        if(oilId.equals("14")){
            return JSONObject.toJSONString(new OilCardModel("64349101","1000","全国中石油加油卡直充1000元"));
        }

      /*  if(oilId.equals("10")||oilId.equals("11")||oilId.equals("12")||oilId.equals("13")){
            return JSONObject.toJSONString(new OilCardModel("64349102","1","中石油任意充"));
        }*/
        return  null;
    }



    static class OilCardModel{
        private String cardNum;
        private String cardPrice;
        private String cardDesc;

        public OilCardModel(String cardNum, String cardPrice, String cardDesc) {
            this.cardNum = cardNum;
            this.cardPrice = cardPrice;
            this.cardDesc = cardDesc;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getCardPrice() {
            return cardPrice;
        }

        public void setCardPrice(String cardPrice) {
            this.cardPrice = cardPrice;
        }

        public String getCardDesc() {
            return cardDesc;
        }

        public void setCardDesc(String cardDesc) {
            this.cardDesc = cardDesc;
        }
    }
}

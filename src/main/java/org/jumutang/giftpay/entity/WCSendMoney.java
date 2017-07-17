package org.jumutang.giftpay.entity;

import java.util.Random;

/**
 * @Auther: Tinny.liang
 * @Description: 提现实体类
 * @Date: Create in 11:49 2017/5/18
 * @Modified By:
 */
public class WCSendMoney {
    private String mch_appid;

    private String mchid;

    private String nonce_str;

    private String sign;

    private String partner_trade_no;

    private String openid;

    private String check_name;

    private int amount;

    private String desc;

    private String spbill_create_ip;

    public WCSendMoney(String mch_appid, String mchid, String partner_trade_no, String openid, int amount, String desc, String spbill_create_ip) {
        this.mch_appid = mch_appid;
        this.mchid = mchid;
        this.nonce_str = getRandomStringByLength(32);
        this.partner_trade_no = partner_trade_no;
        this.openid = openid;
        this.check_name = "NO_CHECK";
        this.amount = amount;
        this.desc = desc;
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

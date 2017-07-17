package org.jumutang.giftpay.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Random;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 16:29 2017/6/5
 * @Modified By:
 */
@XStreamAlias("xml")
public class WCOrderQuery {
    private String appid;
    private String mch_id;
    private String transaction_id;
    private String nonce_str;
    private String sign;

    public WCOrderQuery(String appid, String mch_id, String transaction_id) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.transaction_id = transaction_id;
        this.nonce_str = WCOrderQuery.getRandomStringByLength(32);
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
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

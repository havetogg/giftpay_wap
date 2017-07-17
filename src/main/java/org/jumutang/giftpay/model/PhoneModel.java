package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 10:35 2017/6/12
 * @Modified By:
 */
public class PhoneModel implements Serializable{
    //电话号码
    private String phone;
    //省份
    private String province;
    //城市
    private String city;
    //卡类型
    private String card;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}

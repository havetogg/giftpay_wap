package org.jumutang.giftpay.entity;

/**
 * Created by Administrator on 2017/6/30.
 */
public class PaBankLoanModel {

    private int id;

    private String username;

    private String openid;

    private String phone;

    private String createtime;

    private String third_code;

    private String address;

    private String ipaddress;

    private String ipaddress_true;

    public String getIpaddress_true() {
        return ipaddress_true;
    }

    public void setIpaddress_true(String ipaddress_true) {
        this.ipaddress_true = ipaddress_true;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThird_code() {
        return third_code;
    }

    public void setThird_code(String third_code) {
        this.third_code = third_code;
    }
}

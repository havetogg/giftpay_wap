package org.jumutang.giftpay.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class PaBankUser {

    private int id;

    private String openid;

    private String phone ;

    private String createtime;

    private int get_bankuser;

    private int old_bankuser;

    private int total_count;

    private int exits;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getGet_bankuser() {
        return get_bankuser;
    }

    public void setGet_bankuser(int get_bankuser) {
        this.get_bankuser = get_bankuser;
    }

    public int getOld_bankuser() {
        return old_bankuser;
    }

    public void setOld_bankuser(int old_bankuser) {
        this.old_bankuser = old_bankuser;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getExits() {
        return exits;
    }

    public void setExits(int exits) {
        this.exits = exits;
    }
}

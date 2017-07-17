package org.jumutang.giftpay.entity;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/3/8.
 */
public class ThirdUserModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String tId;
    private String phone;
    private String status;
    private String name;
    private String createTime;
    private String thirdName;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }
}

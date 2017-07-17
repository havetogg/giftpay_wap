package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/2/8.
 * 用户主表 用于获取用户关键表主要信息
 */
public class UserMainModel implements Serializable {
    private String id;
    private String phone;
    private String status;
    private String createTime;
    private String valNum;

    private String balanceNumber;

    //身份证号
    private String idCard;
    //姓名
    private String userName;
    //登录密码
    private String loginPwd;

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBalanceNumber() {
        return balanceNumber;
    }

    public void setBalanceNumber(String balanceNumber) {
        this.balanceNumber = balanceNumber;
    }

    public String getValNum() {
        return valNum;
    }

    public void setValNum(String valNum) {
        this.valNum = valNum;
    }

    public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

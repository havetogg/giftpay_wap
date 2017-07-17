package org.jumutang.giftpay.model;

import java.io.Serializable;

/**
 * @Auther: Tinny.liang
 * @Description: 提现记录实体类
 * @Date: Create in 9:32 2017/6/8
 * @Modified By:
 */
public class WithdrawModel implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //提现Id
    private String withdrawId;
    //提现人的openId
    private String openId;
    //userId
    private String userId;
    //手机号码
    private String phone;
    //提现金额
    private Double withdrawAmount;
    //提现状态 0 未审核 1 审核通过 2 审核不通过
    private Short status;
    //创建时间
    private String createTime;
    //审核时间
    private String auditTime;
    //提现人的ip地址
    private String ipAddress;

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
}

package org.jumutang.giftpay.model.liftPaytment;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/4/8.
 */
public class RechargeAccountModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String accountType;
    private String familyId;
    private String payUnitId;
    private String payUnitName;
    private String payAccount;
    private String status;
    private String createTime;
    private String cardId;
    private String queryParams;

    @Override
    public String toString() {
        return "RechargeAccountModel{" +
                "id='" + id + '\'' +
                ", accountType='" + accountType + '\'' +
                ", familyId='" + familyId + '\'' +
                ", payUnitId='" + payUnitId + '\'' +
                ", payUnitName='" + payUnitName + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                ", cardId='" + cardId + '\'' +
                ", queryParams='" + queryParams + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getPayUnitId() {
        return payUnitId;
    }

    public void setPayUnitId(String payUnitId) {
        this.payUnitId = payUnitId;
    }

    public String getPayUnitName() {
        return payUnitName;
    }

    public void setPayUnitName(String payUnitName) {
        this.payUnitName = payUnitName;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }
}

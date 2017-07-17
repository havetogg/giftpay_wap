package org.jumutang.caranswer.wechat.viewmodel;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.jumutang.caranswer.model.BaseModel;

public class MyRedInfoView extends BaseModel implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 7150254198781532420L;
    
    DecimalFormat df = new DecimalFormat("######0.00");
    
    //待发放的加油红包
    private Double allReturnMoney;
    //累计获得加油红包
    private Double allGetRedMoney;
    //最后一次获得的红包
    private Double getLastRedMoney;
    //累计享受优惠
    private Double allPreferential;
    //是否绑定油卡 1是  0否
    private String isBind = "0";
    //是否在还款期  1是  0否
    private String isReimbursement = "0";

    public String getIsReimbursement()
    {
        return isReimbursement;
    }
    public void setIsReimbursement(String isReimbursement)
    {
        this.isReimbursement = isReimbursement;
    }
    public Double getAllReturnMoney()
    {
        return allReturnMoney;
    }
    public MyRedInfoView setAllReturnMoney(Double allReturnMoney)
    {
        this.allReturnMoney = allReturnMoney == null ? 0 : allReturnMoney;
        this.allReturnMoney = Double.valueOf(df.format(this.allReturnMoney));
        return this;
    }
    public Double getAllGetRedMoney()
    {
        return allGetRedMoney;
    }
    public MyRedInfoView setAllGetRedMoney(Double allGetRedMoney)
    {
        this.allGetRedMoney = allGetRedMoney == null ? 0 : allGetRedMoney;
        this.allGetRedMoney = Double.valueOf(df.format(this.allGetRedMoney));
        return this;
    }
    public Double getGetLastRedMoney()
    {
        return getLastRedMoney;
    }
    public MyRedInfoView setGetLastRedMoney(Double getLastRedMoney)
    {
        this.getLastRedMoney = getLastRedMoney == null ? 0 : getLastRedMoney;
        this.getLastRedMoney = Double.valueOf(df.format(this.getLastRedMoney));
        return this;
    }
    public Double getAllPreferential()
    {
        return allPreferential;
    }
    public MyRedInfoView setAllPreferential(Double allPreferential)
    {
        this.allPreferential = allPreferential == null ? 0 : allPreferential;
        this.allPreferential = Double.valueOf(df.format(this.allPreferential));
        return this;
    }
    public String getIsBind()
    {
        return isBind;
    }
    public MyRedInfoView setIsBind(String isBind)
    {
        this.isBind = isBind;
        return this;
    } 
    
    

}

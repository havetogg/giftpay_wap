package org.jumutang.caranswer.wechat.viewmodel;

import java.io.Serializable;
import java.util.Date;

import org.jumutang.caranswer.model.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
public class RedInfoView extends BaseModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 5757964096540329465L;
    // 红包主键
    private String redID;
    // 活动主键
    private String activityID;
    // 用户主键
    private String userID;
    // 订单主键
    private String orderID;
    // 红包名称
    private String redName;
    // 红包金额
    private String redMoney;
    // 红包有效期开始时间
    @JSONField(format="yyyy-MM-dd")
    private Date startTime;
    // 红包有效期结束时间
    private Date endTime;
    // 红包创建时间
    private Date createTime;
    // 红包修改时间
    private Date updateTime;
    // 红包删除时间
    private Date deleteTime;
    //红包优惠
    private String preferential;
    //活动名称
    private String activityName;
    //是否使用
    private String redInfoState;
    //是否提取 1已发放  0未发放
    private String isTrue ;
    
    private String month;
    

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getIsTrue()
    {
        return isTrue;
    }

    public void setIsTrue(String isTrue)
    {
        this.isTrue = isTrue;
    }

    public String getActivityName()
    {
        return activityName;
    }

    public RedInfoView setActivityName(String activityName)
    {
        this.activityName = activityName;
        return this;
    }

    public String getRedInfoState()
    {
        return redInfoState;
    }

    public RedInfoView setRedInfoState(String redInfoState)
    {
        this.redInfoState = redInfoState;
        return this;
    }

    public String getPreferential()
    {
        return preferential;
    }

    public RedInfoView setPreferential(String preferential)
    {
        this.preferential = preferential;
        return this;
    }

    public String getRedID() {
        return redID;
    }

    public RedInfoView setRedID(String redID) {
        this.redID = redID;
        return this;
    }

    public String getActivityID() {
        return activityID;
    }

    public RedInfoView setActivityID(String activityID) {
        this.activityID = activityID;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public RedInfoView setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getOrderID() {
        return orderID;
    }

    public RedInfoView setOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public String getRedName() {
        return redName;
    }

    public RedInfoView setRedName(String redName) {
        this.redName = redName;
        return this;
    }

    public String getRedMoney() {
        return redMoney;
    }

    public RedInfoView setRedMoney(String redMoney) {
        this.redMoney = redMoney;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public RedInfoView setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public RedInfoView setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public RedInfoView setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public RedInfoView setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public RedInfoView setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
        return this;
    }
}

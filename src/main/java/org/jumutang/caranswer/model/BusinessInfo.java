package org.jumutang.caranswer.model;

import java.io.Serializable;

/**
 * 商务合作信息表
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月9日
 * @see
 * @since gframe-v100
 */
public class BusinessInfo extends BaseModel implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 8951900668948078120L;
    //主键id
    private String businessID;
    //联系电话
    private String userTel;
    //联系人
    private String userName;
    //添加时间
    private String addTime;
    //合作内容
    private String context;
    public String getBusinessID()
    {
        return businessID;
    }
    public BusinessInfo setBusinessID(String businessID)
    {
        this.businessID = businessID;
        return this;
    }
    public String getUserTel()
    {
        return userTel;
    }
    public BusinessInfo setUserTel(String userTel)
    {
        this.userTel = userTel;
        return this;
    }
    public String getUserName()
    {
        return userName;
    }
    public BusinessInfo setUserName(String userName)
    {
        this.userName = userName;
        return this;
    }
    public String getAddTime()
    {
        return addTime;
    }
    public BusinessInfo setAddTime(String addTime)
    {
        this.addTime = addTime;
        return this;
    }
    public String getContext()
    {
        return context;
    }
    public BusinessInfo setContext(String context)
    {
        this.context = context;
        return this;
    }
    
    
    
}

package org.jumutang.caranswer.model;

import java.io.Serializable;

/**
 * 代办保养model
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年12月9日
 * @see
 * @since gframe-v100
 */
public class MaintenanceInfo extends BaseModel implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 7141195579689011235L;

    //主键id
    private String maintenanceID;
    //用户手机号
    private String userTel;
    //车辆类型
    private String carType;
    //服务类型 
    private String serviceType;
    //添加时间
    private String addtime;
    
    public String getMaintenanceID()
    {
        return maintenanceID;
    }
    public MaintenanceInfo setMaintenanceID(String maintenanceID)
    {
        this.maintenanceID = maintenanceID;
        return this;
    }
    public String getUserTel()
    {
        return userTel;
    }
    public MaintenanceInfo setUserTel(String userTel)
    {
        this.userTel = userTel;
        return this;
    }
    public String getCarType()
    {
        return carType;
    }
    public MaintenanceInfo setCarType(String carType)
    {
        this.carType = carType;
        return this;
    }
    public String getServiceType()
    {
        return serviceType;
    }
    public MaintenanceInfo setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
        return this;
    }
    public String getAddtime()
    {
        return addtime;
    }
    public MaintenanceInfo setAddtime(String addtime)
    {
        this.addtime = addtime;
        return this;
    }
    
}

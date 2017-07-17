package org.jumutang.giftpay.model.liftPaytment;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/4/8.
 */
public class FamilyModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String familyName;
    private String provinceName;
    private String provinceId;
    private String cityName;
    private String cityId;
    private String status;
    private String createTime;

    @Override
    public String toString() {
        return "FamilyModel{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", familyName='" + familyName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

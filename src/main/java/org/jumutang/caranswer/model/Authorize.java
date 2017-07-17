package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户授权信息
 * 
 * @author YuanYu
 *
 */
public class Authorize extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8064340953882540238L;
	// 授权主键
	private String authorizeID;
	// 用户主键
	private String userID;
	// 授权昵称
	private String nikeName;
	// 授权性别
	private String sex;
	// 授权省份
	private String province;
	// 授权地市
	private String city;
	// 授权国家
	private String country;
	// 授权头像
	private String headimgurl;
	// 特权信息
	private String privilege;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 删除时间
	private Date deleteTime;
	//wechatOpenID
	private String weChatCode;
	

	public String getWeChatCode()
    {
        return weChatCode;
    }

    public Authorize setWeChatCode(String weChatCode)
    {
        this.weChatCode = weChatCode;
        return this;
    }

    public String getAuthorizeID() {
		return authorizeID;
	}

	public Authorize setAuthorizeID(String authorizeID) {
		this.authorizeID = authorizeID;
		return this;
	}

	public String getUserID() {
		return userID;
	}

	public Authorize setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getNikeName() {
		return nikeName;
	}

	public Authorize setNikeName(String nikeName) {
		this.nikeName = nikeName;
		return this;
	}

	public String getSex() {
		return sex;
	}

	public Authorize setSex(String sex) {
		this.sex = sex;
		return this;
	}

	public String getProvince() {
		return province;
	}

	public Authorize setProvince(String province) {
		this.province = province;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Authorize setCity(String city) {
		this.city = city;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public Authorize setCountry(String country) {
		this.country = country;
		return this;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public Authorize setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
		return this;
	}

	public String getPrivilege() {
		return privilege;
	}

	public Authorize setPrivilege(String privilege) {
		this.privilege = privilege;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Authorize setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

package org.jumutang.caranswer.framework.wechat.model;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信用户
 * 
 * @author YuanYu
 *
 */
public class WeChatUserInfo {

	/**
	 * 用户的唯一标识
	 */
	@JSONField(name = "openid")
	private String openID;
	/**
	 * 用户昵称
	 */
	@JSONField(name = "nickname")
	private String nickName;
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	@JSONField(name = "sex")
	private String sex;
	/**
	 * 用户个人资料填写的省份
	 */
	@JSONField(name = "province")
	private String province;
	/**
	 * 普通用户个人资料填写的城市
	 */
	@JSONField(name = "city")
	private String city;
	/**
	 * 国家，如中国为CN
	 */
	@JSONField(name = "country")
	private String country;
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）， 用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 */
	@JSONField(name = "headimgurl")
	private String headimgurl;
	/**
	 * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	 */
	@JSONField(name = "privilege")
	private String[] privilege;
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	@JSONField(name = "unionid")
	private String unionid;
	/**
	 * 错误码
	 */
	@JSONField(name = "errcode")
	private String errorCode;
	/**
	 * 错误信息
	 */
	@JSONField(name = "errmsg")
	private String errorMess;
	
	/**
	 * 手机号码
	 */
	private String uTel;
	

	public String getuTel()
    {
        return uTel;
    }

    public void setuTel(String uTel)
    {
        this.uTel = uTel;
    }

    /**
	 * 用户的唯一标识
	 */
	public String getOpenID() {
		return openID;
	}

	/**
	 * 用户的唯一标识
	 */
	public void setOpenID(String openID) {
		this.openID = openID;
	}

	/**
	 * 用户昵称
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * 用户昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 用户个人资料填写的省份
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 用户个人资料填写的省份
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 普通用户个人资料填写的城市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 普通用户个人资料填写的城市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 国家，如中国为CN
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 国家，如中国为CN
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）， 用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）， 用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	/**
	 * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	 */
	public String[] getPrivilege() {
		return privilege;
	}

	/**
	 * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	 */
	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	public String getUnionid() {
		return unionid;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	/**
	 * 错误码
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 错误码
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 错误信息
	 */
	public String getErrorMess() {
		return errorMess;
	}

	/**
	 * 错误信息
	 */
	public void setErrorMess(String errorMess) {
		this.errorMess = errorMess;
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
}

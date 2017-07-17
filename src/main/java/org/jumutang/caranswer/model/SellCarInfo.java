package org.jumutang.caranswer.model;

import java.io.Serializable;

/**
 * 
 * @author oywj
 * 卖车信息记录表
 *
 */
public class SellCarInfo extends BaseModel implements Serializable{
	//主键     
	private String id;   
	//用户唯一标识
	private String openId;
	//车主姓名
	private String sellerName;
	//车品牌id
	private String brand;
	//车系列id
	private String series;
	//型号id
	private String model;
	//城市
	private String location;
	//手机号
	private String tel;
	//帮卖平台
	private String sellChannel;
	//创建时间
	private String ct_date;
	//车首次上牌时间
	private String regDate;
	//提交成功返回的车源线索在车300的唯一性标识
	private String clue_id;
	
	public String getClue_id() {
		return clue_id;
	}
	public void setClue_id(String clue_id) {
		this.clue_id = clue_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellName) {
		this.sellerName = sellName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSellChannel() {
		return sellChannel;
	}
	public void setSellChannel(String sellChannel) {
		this.sellChannel = sellChannel;
	}
	
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getCt_date() {
		return ct_date;
	}
	public void setCt_date(String ct_date) {
		this.ct_date = ct_date;
	}
	
	
	
}

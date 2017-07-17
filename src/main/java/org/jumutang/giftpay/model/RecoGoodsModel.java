package org.jumutang.giftpay.model;

/**
 * 
 * @author oywj
 *
 * @Date 2017年7月6日 下午4:17:05
 */
public class RecoGoodsModel {

	private int id;
	private String name;
	private String imgUrl;
	private String linkUrl;
	private String originalCost;
	private String discountCost;
	private String discount;
	private int clickNum;
	private int state;
	private String createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getOriginalCost() {
		return originalCost;
	}
	public void setOriginalCost(String originalCost) {
		this.originalCost = originalCost;
	}
	public String getDiscountCost() {
		return discountCost;
	}
	public void setDiscountCost(String discountCost) {
		this.discountCost = discountCost;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	
	
	
}

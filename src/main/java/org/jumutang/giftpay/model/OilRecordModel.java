package org.jumutang.giftpay.model;

public class OilRecordModel {
	private String id;
	private String userid;
	private String openid;
	private String payamount;
	private String totalamount;
	private String monthamount;
	private String termsurplus;
	private String cycle;
	private String discount;
	private String status;
	private String createtime;
	private String updatetime;
	private String url;
	private String number;
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getTermsurplus() {
		return termsurplus;
	}

	public void setTermsurplus(String termsurplus) {
		this.termsurplus = termsurplus;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getMonthamount() {
		return monthamount;
	}

	public void setMonthamount(String monthamount) {
		this.monthamount = monthamount;
	}

	@Override
	public String toString() {
		return "OilRecordModel{" +
				"id='" + id + '\'' +
				", userid='" + userid + '\'' +
				", openid='" + openid + '\'' +
				", payamount='" + payamount + '\'' +
				", totalamount='" + totalamount + '\'' +
				", termsurplus='" + termsurplus + '\'' +
				", cycle='" + cycle + '\'' +
				", discount='" + discount + '\'' +
				", status='" + status + '\'' +
				", createtime='" + createtime + '\'' +
				", updatetime='" + updatetime + '\'' +
				", url='" + url + '\'' +
				", number='" + number + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}
}

package org.jumutang.giftpay.model;

public class UserOilInfoModel {
	private String userid;
	private String openid;
	private String balance;
	private String waitsendmoney;
	private String totalgetmoney;
	private String totalsavemoey;
	private String status;
	private String createtime;
	private String updatetime;

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

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getWaitsendmoney() {
		return waitsendmoney;
	}

	public void setWaitsendmoney(String waitsendmoney) {
		this.waitsendmoney = waitsendmoney;
	}

	public String getTotalgetmoney() {
		return totalgetmoney;
	}

	public void setTotalgetmoney(String totalgetmoney) {
		this.totalgetmoney = totalgetmoney;
	}

	public String getTotalsavemoey() {
		return totalsavemoey;
	}

	public void setTotalsavemoey(String totalsavemoey) {
		this.totalsavemoey = totalsavemoey;
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

	@Override
	public String toString() {
		return "UserinfoOilModel [userid=" + userid + ", openid=" + openid + ", balance=" + balance + ", waitsendmoney="
				+ waitsendmoney + ", totalgetmoney=" + totalgetmoney + ", totalsavemoey=" + totalsavemoey + ", status="
				+ status + ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
	}

}

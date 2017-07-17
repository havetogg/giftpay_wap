package org.jumutang.giftpay.model;

public class OilSubRecordModel {
	private String id;
	private String recordid;
	private String userid;
	private String amount;
	private String status;
	private String createtime;
	private String updatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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
		return "OilSubRecordModel [id=" + id + ", recordid=" + recordid + ", userid=" + userid + ", amount=" + amount
				+ ", status=" + status + ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
	}

}

package org.jumutang.giftpay.entity;

public class WCPreOrderRequest {
	public String appID;
	public String mchID;
	public String key;
	public String openID;
	public String money;
	public String ip;
	public String notify_url;
	public String out_trade_no;
	public String body;

	public WCPreOrderRequest(String appID, String mchID, String key, String openID, String money, String ip,
							 String notify_url, String out_trade_no, String body) {
		this.appID = appID;
		this.mchID = mchID;
		this.key = key;
		this.openID = openID;
		this.money = money;
		this.ip = ip;
		this.notify_url = notify_url;
		this.out_trade_no = out_trade_no;
		this.body = body;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getMchID() {
		return mchID;
	}

	public void setMchID(String mchID) {
		this.mchID = mchID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}

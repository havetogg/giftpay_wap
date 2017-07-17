package org.jumutang.caranswer.framework.wechat.e;

public enum EAuthorizeScope {

	_SNSAPI_BASE("snsapi_base", "snsapi_base", "授权基础信息"), 
	_SNSAPI_USERINFO("snsapi_userinfo", "snsapi_userinfo", "授权详细信息");

	private final String java_code;
	private final String data_save;
	private final String view_show;

	private EAuthorizeScope(String java_code, String data_save, String view_show) {
		this.java_code = java_code;
		this.data_save = data_save;
		this.view_show = view_show;
	}

	public String getJava_code() {
		return java_code;
	}

	public String getData_save() {
		return data_save;
	}

	public String getView_show() {
		return view_show;
	}

}

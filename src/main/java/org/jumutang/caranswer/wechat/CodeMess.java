package org.jumutang.caranswer.wechat;

public class CodeMess {

	private final String code;
	private final String mess;

	public CodeMess(String code, String mess) {
		super();
		this.code = code;
		this.mess = mess;
	}

	public String getCode() {
		return code;
	}

	public String getMess() {
		return mess;
	}

}

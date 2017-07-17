package org.jumutang.giftpay.base.entity;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CodeMess {

	private final String code;
	private final String mess;
	private final String data;



	public CodeMess(String code, String mess, Object data) {
		super();
		this.code = code;
		this.mess = mess;
		if(data!=null&!data.equals("")){
			this.data = JSONObject.fromObject(data).toString();
		}else{
			this.data = "";
		}

	}
	public CodeMess(String code, String mess, List<Object> data) {
		super();
		this.code = code;
		this.mess = mess;
		if(data!=null&data.size()!=0){
			this.data = JSONArray.fromObject(data).toString();
		}else{
			this.data ="";
		}
	}

	public String getData() {
		return data;
	}

	public String getCode() {
		return code;
	}

	public String getMess() {
		return mess;
	}

}

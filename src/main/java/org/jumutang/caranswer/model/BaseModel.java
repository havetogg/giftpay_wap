package org.jumutang.caranswer.model;

import com.alibaba.fastjson.JSONObject;

public class BaseModel {

	
	public String toJsonString(){
		return JSONObject.toJSONString(this);
	}
	
}

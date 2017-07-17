package org.jumutang.giftpay.tools;

import java.io.Serializable;
import java.util.Date;

/**
 * 默认json返回实体类
 * 
 * @author 鲁雨
 * @since 20161223
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 * 
 */
public class ResResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;
	private String describe;
	private long time;
	private Object data;
	
	public ResResult(int status){
		this.status = status;
		this.time=new Date().getTime();
	}
	
	public ResResult(int status,String describe){
		this.status = status;
		this.time=new Date().getTime();
		this.describe = describe;
	}
	
	public ResResult(int status,Object data){
		this.status = status;
		this.data = data;
		this.time=new Date().getTime();
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}

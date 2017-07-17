package org.jumutang.giftpay.tools;

/**
 * 返回操作类
 * 
 * @author ³��
 * @since 20161223
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public class ResponseModel {
	
	/**
	 * ʧ����Ϣ
	 * @param describe
	 * @return
	 */
	public static ResResult errorActive(String describe){
		return new ResResult(1,describe);
	}
	
	/**
	 * �ɹ���Ϣ
	 * @param describe
	 * @return
	 */
	public static ResResult successActive(){
		return new ResResult(0);
	}
	
	/**
	 * ���ݷ���
	 * 
	 * @param data
	 * @param status
	 */
	public static ResResult returnData(Object data){
		return new ResResult(0,data);
	}
}

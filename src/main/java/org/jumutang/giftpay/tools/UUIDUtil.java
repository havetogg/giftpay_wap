package org.jumutang.giftpay.tools;

import java.util.UUID;

/**
 * 创建id工具类
 * 
 * @author 鲁雨
 * @since 20170119
 * @version v1.0
 * 
 * copyright Luyu(18994139782@163.com)
 *
 */
public class UUIDUtil {

	/**
	 * 获取36不唯一id字符串
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		id = id.replace("-", "");
		return id;
	}
	
	public static void main(String[] args) {
		String url = "http://ityuany.com:8080/archives/queryArchives";
		String identity = "5c8b58d99aee469d96a9e315f5ac4c2b";
		String result = HttpUtil.sendGet(url,"UTF-8",identity);
		System.out.println(result);
	}
}

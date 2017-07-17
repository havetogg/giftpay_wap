package org.jumutang.caranswer.framework.x;

import java.util.UUID;

/**
 * 唯一标识工具类
 * @author YuanYu
 *
 */
public final class UniqueX {

	private UniqueX(){
	}
	/**
	 * 32位全大写uuid
	 * @return
	 */
	public static String new32UUIDUpperCaseString(){
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	/**
	 * 36位全小写uuid
	 * @return
	 */
	public static String new36UUIDUpperCaseString(){
		return UUID.randomUUID().toString().toUpperCase();
	}
}

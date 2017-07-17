package org.jumutang.caranswer.compoent;

import org.jumutang.caranswer.framework.ContextResult;

/**
 * 聚合接口
 * @author YuanYu
 *
 */
public interface IJuHeComponent {

	
	
	/**
	 * 发送短信验证码接口
	 * 
	 * @param userTel
	 *            用户手机号
	 * @param securityCode
	 *            验证码
	 * @return {"reason":"操作成功","result":{"sid":"1511222021416780","fee":1,
	 *         "count":1},"error_code":0}
	 */
	public ContextResult<String> sendSMSSecurityCode(String userTel, String securityCode);
	
	
	/**
	 * 发送充值成功通知
	 * @param userTel
	 * @return
	 */
	public ContextResult<String> sendSMSrechargeNotice(String userTel);
	
	/**
	 * 返款通知
	 * @param userTel
	 * @return
	 */
	public ContextResult<String> sendReturnmoneyNotice(String userTel);
}

package org.jumutang.caranswer.service;

import org.jumutang.caranswer.framework.ContextResult;

/**
 * 第三方接口
 * 
 * @author YuanYu
 *
 */
public interface IThirdPartyService {

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
	public ContextResult<Object> getSMSSecurityCode(String userTel, String securityCode);

	/**
	 * 申请加油卡充值接口
	 * 
	 * @param cardNum
	 *            充值数量 任意充 （整数（元）），其余面值固定值为1
	 * @param iOrderID
	 *            商家订单号，8-32位字母数字组合
	 * @param gameUserID
	 *            加油卡卡号，中石化：以100011开头的卡号、中石油：以9开头的卡号
	 * @param gasCardTel
	 *            持卡人手机号码
	 * @param gasCardName
	 *            持卡人姓名
	 * @return { "code":"0", "mess":"提交充值成功", "resultObject": { "result": {
	 *         "cardnum":"充值数量", "uorderid":"商户自定的订单号", "game_state":
	 *         "充值状态:0充值中 1成功 9撤销", "cardname":"充值名称", "cardid":"",
	 *         "sporder_id":"商家订单号", "ordercash":"进货价格", "game_userid":"加油卡卡号"
	 *         }, "reason":"提交充值成功", "error_code":0 } }
	 */
	public ContextResult<Object> applyRechargeFuelCard(String cardNum, String iOrderID, String gameUserID,
			String gasCardTel, String gasCardName);

	/**
	 * 
	 * @param iOrderid
	 *            商家订单号，8-32位字母数字组合
	 * @return { "code":"1", "mess":"操作成功！", "resultObject": { "result": {
	 *         "game_state":"状态 1:成功 9:失败 0：充值中", "sporder_id":"聚合订单号",
	 *         "uordercash":"订单扣除金额", "gascard_code":"1000025362697" },
	 *         "reason":"查询成功", "error_code":0 } }
	 */
	public ContextResult<Object> queryFuelCardOrderStatus(String iOrderid);

	/**
	 * 
	 * @param produceName
	 *            产品名称
	 * @param iOrderID
	 *            自家的订单号
	 * @param rechargeMoney
	 *            充值金额
	 * @param callbackurl
	 *            后台回调地址
	 * @param webcallbackurl
	 *            前台回调地址
	 * @return {"amount":"1.0","bankcode":"","bindid":"","cardno":"","cardtype":
	 *         "","code":"1","customernumber":"10012434318","externalid":
	 *         "2a0ba0a62fc4-876147458ZGT","hmac":
	 *         "e0cfcc554ab68f698e247ee07e5e857c","payurl":
	 *         "https://ok.yeepay.com/zhangguitong/api/pay/request?merchantaccount=10012434318&data=D25dCOoOslE5szHVRLL6po5FxVJrJhtlzBn0kq1TBLzkTAOrl2GFnyXhvHpcTDo20b47Wd%2BOIZdBYKko1vmZSvccxez3%2FkDYWN1EqhSBXN53Xm140RFLXwxfPZfDkCvrC3YJSzB%2BkHpFs41vYS%2BlJEVBYJ9zms7t%2B7CB6VaJ2J%2BR5hSNxHdqjcCNkV8owHulqKQSquhAxeDDTHUO4cKBzCIcl00lvh2lrUCBMaDlGtdsnXJMWvCKmRkeNX47Xn7vng4NKbESfjy5Ti4cafNeG1XNgtG6t%2FIVakfaOkp5b%2BK%2BHxe2fvJ9Vs6IY4a0FILXtuXUQ%2BKKmHpg7azndTePHPAUuFfPMnwoio9CbnFPi2MpVPhsIeEixLPRKlAekER5yABOrNkutmASFR6wu6uaO6ZfYIqj14Uy6DZxxgsh5nPuklhFuETTZZNKXTEyLhWcpYgzPex9UcdOZ5y2s%2B3AHH09pBseMLqQQdR2v6wTLxcDSKx76RvaqqSmBcO%2Fdxft4XmGTiWS7xJEwNtTY2bmoaFnk4wrXSyZK1cbectLUu%2Foqws5veSSy%2FN1O8gyIQUQaIA1PNVZqHmycWV4cqwRT0MUgHghvNwuco%2BdFt30jZ0PoHd%2FaucgzisoBMAlAo%2FfJvXxiQNIZ2ixPq1LJfD%2Fg4xksEUceESyRd5iATXDmBY%3D&encryptkey=bveJOoHg6raEz2kchOpnKY5xdFErTlzXdlyx6Zt%2B%2Be62AZ0FAIqJfhNRkOTDFmtv0XEouprjLy%2Bl8QwKfts6%2F4NXna0r7eh4w9Fi%2FlLRtl6wPTz%2F7FpsHfNSNILnnMxwoebFIxibcV9tEE2ToQWm5GuLl7ChIunLrk2CPlPti3M%3D"
	 *         ,"requestid":"4BA9A1E85D9D4941ABD8A2EC5C4F9DBD"}
	 */
	public ContextResult<Object> epay(String produceName, String iOrderID, String rechargeMoney, String callbackurl,
			String webcallbackurl);
}

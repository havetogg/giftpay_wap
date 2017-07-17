
package org.jumutang.caranswer.compoent;

import org.jumutang.caranswer.compoent.model.OFPayOrderInfo;
import org.jumutang.caranswer.framework.ContextResult;

/**
 * 欧飞接口
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月24日
 * @see
 * @since gframe-v100
 */
public interface IOFPayCompoent
{
    /**
     * 申请加油卡充值接口
     * 
     * @param cardnum
     *            充值数量 任意充 （整数（元）），其余面值固定值为1
     * @param sporder_id
     *            商家订单号，  商户传给欧飞的唯一订单号
     * @param sporder_time
     *            订单时间   yyyyMMddHHmmss 如：20070323140214
     * @param gasCardTel
     *            持卡人手机号码
     * @param gasCardName
     *            持卡人姓名
     * @param game_userid
     *            加油卡号 
     *            
     * @return { "code":"0", "mess":"提交充值成功", "resultObject": { "result": {
     *         "cardnum":"充值数量", "uorderid":"商户自定的订单号", "game_state":
     *         "充值状态:0充值中 1成功 9撤销", "cardname":"充值名称", "cardid":"",
     *         "sporder_id":"商家订单号", "ordercash":"进货价格", "game_userid":"加油卡卡号"
     *         }, "reason":"提交充值成功", "error_code":0 } }
     */
    public ContextResult<OFPayOrderInfo> applyRechargeFuelCard(String cardnum , String sporder_id , String sporder_time , String game_userid ,
            String gasCardTel , String gasCardName);
}

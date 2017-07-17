
package org.jumutang.caranswer.compoent.impl;

import java.util.regex.Pattern;

import net.sf.json.xml.XMLSerializer;

import org.jumutang.caranswer.compoent.IOFPayCompoent;
import org.jumutang.caranswer.compoent.model.OFPayOrderInfo;
import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.http.PostOrGet;
import org.jumutang.caranswer.framework.http.web.e.ESendHTTPModel;
import org.jumutang.caranswer.framework.model.NamedValue;
import org.jumutang.caranswer.framework.x.MD5X;
import org.jumutang.caranswer.framework.x.StringX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class OFPayCompoentImpl implements IOFPayCompoent
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(OFPayCompoentImpl.class);

    /**
     * 欧飞充值油卡接口url
     */
    @Value(value = "#{propertyFactoryBean['oufei.fuelCard.recharge.URL']}")
    private String ouFeiFuelCardRechargeUrl;

    /**
     * 欧飞用户密码 需要md5加密  转小写
     */
    @Value(value = "#{propertyFactoryBean['oufei.ouFeiUserPws']}")
    private String userpws;

    /**
     * 欧飞用户id
     */
    @Value(value = "#{propertyFactoryBean['oufei.ouFeiFUserId']}")
    private String userid;

    /**
     * 是否需要发票 1:是 、0:否
     */
    @Value(value = "#{propertyFactoryBean['oufei.invoiceFlag']}")
    private String invoiceFlag;

    /**
     * 固定值为6.0
     */
    @Value(value = "#{propertyFactoryBean['oufei.version']}")
    private String version;

    /**
     * 任意冲 编码
     */
    private String cardid;

    /**
     * 加油卡类型 （1:中石化、2:中石油)
     */
    private String chargeType;

    /**
     * 中石化任意冲编码
     */
    @Value(value = "#{propertyFactoryBean['oufei.shihua.cardid']}")
    private String shiHuaCardid;

    /**
     * 中石油任意冲编码
     */
    @Value(value = "#{propertyFactoryBean['oufei.shiyou.cardid']}")
    private String shiYouCardid;
    
    /**
     * 回调地址
     */
    @Value(value = "#{propertyFactoryBean['oufei.callback.url']}" )
    private String callBackUrl;

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
     * @param  game_userid
     *            加油卡号           
     * @return {"err_msg":[],"retcode":"欧飞订单状态","orderid":"欧飞订单号",
     * "cardid":"油卡任意冲编码","cardnum":"充值金额","ordercash":"实际扣款金额",
     * "cardname":"订单标题","sporder_id":"车答应订单","game_userid":"油卡号","game_state":"充值状态"}
     */
    @Override
    public ContextResult<OFPayOrderInfo> applyRechargeFuelCard(String cardnum , String sporder_id , String sporder_time ,
            String game_userid , String gasCardTel , String gasCardName)
    {
        _LOGGER.info("用户请求申请加油卡充值接口！");
        ContextResult<OFPayOrderInfo> cr = new ContextResult<OFPayOrderInfo>();
        _LOGGER.info("-----------密码md5加密！32位小写,加密前=" + userpws);
        String jiami = MD5X.getLowerCaseMD5For32(userpws);
        _LOGGER.info("-----------密码md5加密！32位小写,加密后=" + jiami);
        _LOGGER.info(game_userid + " 验证油卡类型!");

        cardid = this.validateFuelCard(game_userid);
        if (null == cardid)
        {
            cr.setCode(ContextContast._FALSE_CODE).setMess("加油卡号不正确！");
        }
        else
        {
            String md5_str = MD5X.getUpperCaseMD5For32((new StringBuffer(this.userid).append(jiami).append(this.cardid)
                    .append(cardnum).append(sporder_id).append(sporder_time).append(game_userid).append("OFCARD")
                    .toString()));
            _LOGGER.info("向欧飞发送申请加油卡充值请求！");
            PostOrGet pg = new PostOrGet("gb2312");
            String result = pg.sendGet(this.ouFeiFuelCardRechargeUrl, ESendHTTPModel._SEND_MODEL_DECODER,
                    new NamedValue("userid", userid), 
                    new NamedValue("userpws", jiami),
                    new NamedValue("cardid", cardid), 
                    new NamedValue("cardnum", cardnum), 
                    new NamedValue("sporder_id",sporder_id), 
                    new NamedValue("sporder_time", sporder_time), 
                    new NamedValue("game_userid",game_userid), 
                    new NamedValue("chargeType", chargeType), 
                    new NamedValue("gasCardTel", gasCardTel),
                    new NamedValue("gasCardName", gasCardName), 
                    new NamedValue("invoiceFlag", invoiceFlag),
                    new NamedValue("md5_str", md5_str), 
                    new NamedValue("ret_url",callBackUrl), 
                    new NamedValue("version", version));
            _LOGGER.info(StringX.stringFormat("欧飞申请加油卡充值接口的返回结果为xml：[0]", result));
            try
            {
                String jsonData = new XMLSerializer().read(result).toString();
                _LOGGER.info(StringX.stringFormat("欧飞申请加油卡充值接口的返回结果json为：[0]", jsonData));

                OFPayOrderInfo model = JSONObject.parseObject(jsonData, OFPayOrderInfo.class);

                _LOGGER.info(StringX.stringFormat("欧飞申请加油卡充值接口的返回结果封装bean为：[0]", model.toString()));
                
                if ("1".equals(String.valueOf(model.getRetCode())))
                {
                    cr.setCode(ContextContast._TRUE_CODE);
                }
                else
                {
                    cr.setCode(ContextContast._FALSE_CODE);
                }
                cr.setMess(String.valueOf(model.getErrMsg())).setResultObject(model);
            }
            catch (Exception e)
            {
                _LOGGER.error("欧飞返回结果解析xml失败！");
                _LOGGER.error(e.getMessage(), e);
                cr.setCode(ContextContast._FALSE_CODE).setMess(ContextContast._OPERATION_FALSE_MESS);
            }
        }

        return cr;
    }

    /**
     * 校验加油卡为中石油还是中石化
     * 
     * @param gameUserID
     * @return
     */
    private String validateFuelCard(String gameUserID)
    {
        _LOGGER.info("开始校验加油卡号信息！");
        // 中石化
        String shiHuaregEx = "100011[0-9]+";
        // 中石油
        String shiYouregEx = "9[0-9]+";
        Pattern shiHuaPattern = Pattern.compile(shiHuaregEx);
        Pattern shiYouPattern = Pattern.compile(shiYouregEx);
        if (shiHuaPattern.matcher(gameUserID).matches())
        {
            this.chargeType = "1";
            return shiHuaCardid;
        }

        if (shiYouPattern.matcher(gameUserID).matches())
        {
            this.chargeType = "2";
            return shiYouCardid;
        }
        return null;
    }
    

}

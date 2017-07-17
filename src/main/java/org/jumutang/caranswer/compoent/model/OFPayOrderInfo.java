package org.jumutang.caranswer.compoent.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *  欧飞油卡支付 返回model
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author YeFei
 * @version 1.0, 2015年11月24日
 * @see
 * @since gframe-v100
 */
public class OFPayOrderInfo implements Serializable
{
	public static void main(String[] args) {
		BigDecimal decimal = new BigDecimal(0.8);
		BigDecimal decinal2 = new BigDecimal(0.2);
		System.out.println(decimal.subtract(decinal2));
	}
    /**
     * 
     */
    private static final long serialVersionUID = 4450582924314602167L;

    /**
     * 错误信息
     */
    @JSONField(name = "err_msg")
    private String errMsg;
    
    /**
     * 返回状态
     */
    @JSONField(name = "retcode")
    private String retCode;
    
    /**
     * 欧飞订单
     */
    @JSONField(name = "orderid")
    private String orderID;
    
    /**
     * 任意冲编码
     */
    @JSONField(name = "cardid")
    private String cardID;
    
    /**
     * 充值数量
     */
    @JSONField(name = "cardnum")
    private String cardNum;
    
    /**
     * 实际扣款
     */
    @JSONField(name = "ordercash")
    private String orderCash;
    
    /**
     * 充值标题
     */
    @JSONField(name = "cardname")
    private String cardName;
    
    /**
     * 提交订单
     */
    @JSONField(name = "sporder_id")
    private String sporderID;
    
    /**
     * 充值卡号
     */
    @JSONField(name = "game_userid")
    private String gameUserID;
    
    /**
     * 成功将为1，澈消(充值失败)为9，充值中为0
     */
    @JSONField(name = "game_state")
    private String gameState;

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public String getRetCode()
    {
        return retCode;
    }

    public void setRetCode(String retCode)
    {
        this.retCode = retCode;
    }

    public String getOrderID()
    {
        return orderID;
    }

    public void setOrderID(String orderID)
    {
        this.orderID = orderID;
    }

    public String getCardID()
    {
        return cardID;
    }

    public void setCardID(String cardID)
    {
        this.cardID = cardID;
    }

    public String getCardNum()
    {
        return cardNum;
    }

    public void setCardNum(String cardNum)
    {
        this.cardNum = cardNum;
    }

    public String getOrderCash()
    {
        return orderCash;
    }

    public void setOrderCash(String orderCash)
    {
        this.orderCash = orderCash;
    }

    public String getCardName()
    {
        return cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public String getSporderID()
    {
        return sporderID;
    }

    public void setSporderID(String sporderID)
    {
        this.sporderID = sporderID;
    }

    public String getGameUserID()
    {
        return gameUserID;
    }

    public void setGameUserID(String gameUserID)
    {
        this.gameUserID = gameUserID;
    }

    public String getGameState()
    {
        return gameState;
    }

    public void setGameState(String gameState)
    {
        this.gameState = gameState;
    }

    @Override
    public String toString()
    {
        return "OFPayOrderInfo [errMsg=" + errMsg + ", retCode=" + retCode + ", orderID=" + orderID + ", cardID="
                + cardID + ", cardNum=" + cardNum + ", orderCash=" + orderCash + ", cardName=" + cardName
                + ", sporderID=" + sporderID + ", gameUserID=" + gameUserID + ", gameState=" + gameState + "]";
    }
    
    
}

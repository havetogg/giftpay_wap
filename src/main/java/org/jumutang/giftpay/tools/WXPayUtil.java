package org.jumutang.giftpay.tools;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jumutang.giftpay.entity.WCOrderQuery;
import org.jumutang.giftpay.entity.WCPreOrderRequest;
import org.jumutang.giftpay.entity.WCPreorder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class WXPayUtil {
	// 申请支付
	public static final String _LAUNCH_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 订单查询
	public static final String _ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单
	public static final String _CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 退款
	public static final String _REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 退款查询
	public static final String _REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 下载对账单
	public static final String _DOWNLOAD_BILL = "https://api.mch.weixin.qq.com/pay/downloadbill";

	public static String preOrder(WCPreOrderRequest wcPreOrderRequest) {
		WCPreorder preorder = new WCPreorder();
		preorder.setAppid(wcPreOrderRequest.getAppID());
		preorder.setAttach("paytest");
		preorder.setBody(wcPreOrderRequest.getBody());
		preorder.setMch_id(wcPreOrderRequest.getMchID());
		preorder.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		preorder.setNotify_url(wcPreOrderRequest.getNotify_url());
		preorder.setOpenid(wcPreOrderRequest.getOpenID());
		preorder.setOut_trade_no(wcPreOrderRequest.getOut_trade_no());
		preorder.setSpbill_create_ip(wcPreOrderRequest.getIp());
		preorder.setTotal_fee(wcPreOrderRequest.getMoney());
		preorder.setTrade_type("JSAPI");

		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", preorder.getAppid());
		parameters.put("attach", preorder.getAttach());
		parameters.put("body", preorder.getBody());
		parameters.put("mch_id", preorder.getMch_id());
		parameters.put("nonce_str", preorder.getNonce_str());
		parameters.put("notify_url", preorder.getNotify_url());
		parameters.put("openid", preorder.getOpenid());
		parameters.put("out_trade_no", preorder.getOut_trade_no());
		parameters.put("spbill_create_ip", preorder.getSpbill_create_ip());
		parameters.put("total_fee", preorder.getTotal_fee());
		parameters.put("trade_type", preorder.getTrade_type());

		preorder.setSign(createSign(parameters, wcPreOrderRequest.getKey()));

		XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xStream.autodetectAnnotations(true);
		String xml = xStream.toXML(preorder);
		System.out.println(xml);
		return HttpUtil.sendPost(_LAUNCH_PAY_URL, "utf-8", xml);
	}

	public static Map<String,Object> orderQuery(WCOrderQuery wcOrderQuery,String key){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", wcOrderQuery.getAppid());
		parameters.put("mch_id", wcOrderQuery.getMch_id());
		parameters.put("nonce_str", wcOrderQuery.getNonce_str());
		parameters.put("transaction_id", wcOrderQuery.getTransaction_id());
		wcOrderQuery.setSign(createSign(parameters, key));
		XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xStream.autodetectAnnotations(true);
		String xml = xStream.toXML(wcOrderQuery);
		System.out.println(xml);
		String returnXml =  HttpUtil.sendPost(_ORDER_QUERY_URL, "utf-8", xml);
		Map<String,Object> returnMap = new HashMap<>();
		if(returnXml.contains("<return_code><![CDATA[SUCCESS]]></return_code>")&&returnXml.contains("<result_code><![CDATA[SUCCESS]]></result_code>")&&returnXml.contains("<trade_state><![CDATA[SUCCESS]]></trade_state>")){
			String timeEnd = returnXml.substring(returnXml.indexOf("<time_end><![CDATA[")+19,returnXml.indexOf("]]></time_end>"));
			String cashFee = returnXml.substring(returnXml.indexOf("<cash_fee>")+10,returnXml.indexOf("</cash_fee>"));
			String transactionId = returnXml.substring(returnXml.indexOf("<transaction_id><![CDATA[")+25,returnXml.indexOf("]]></transaction_id>"));
			String openId = returnXml.substring(returnXml.indexOf("<openid><![CDATA[")+17,returnXml.indexOf("]]></openid>"));
			returnMap.put("result",true);
			returnMap.put("timeEnd",timeEnd);
			returnMap.put("cashFee",cashFee);
			returnMap.put("transactionId",transactionId);
			returnMap.put("openId",openId);
		}else{
			String errMsg = null;
			try {
				errMsg = returnXml.substring(returnXml.indexOf("<trade_state_desc>")+18,returnXml.indexOf("</trade_state_desc>"));
			} catch (Exception e) {
				errMsg="<[CDATA[用户微信订单错误]]>";
			}
			returnMap.put("result",false);
			returnMap.put("errMsg",errMsg);
		}
		return returnMap;
	}

	public static Map<String, String> loadJavaScriptPayConfig(String appID, String prePayId, String key, String orderId,String url,String paymoney) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		String timestamp = simpleDateFormat.format(new Date());
		String nonceStr = UUID.randomUUID().toString().replace("-", "");
		String packageStr = prePayId;
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appId", appID);
		parameters.put("timeStamp", timestamp);
		parameters.put("nonceStr", nonceStr);
		parameters.put("package", "prepay_id=" + packageStr);
		parameters.put("signType", "MD5");
		parameters.put("paySign", WXPayUtil.createSign(parameters, key));
		parameters.put("timestamp", timestamp);
		parameters.put("orderId", orderId);
		parameters.put("url",url);
		parameters.put("paymoney",paymoney);
		return parameters;
	}

	public static Map<String, String> loadJavaScriptPayConfig(String appID, String prePayId, String key, String orderId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		String timestamp = simpleDateFormat.format(new Date());
		String nonceStr = UUID.randomUUID().toString().replace("-", "");
		String packageStr = prePayId;
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appId", appID);
		parameters.put("timeStamp", timestamp);
		parameters.put("nonceStr", nonceStr);
		parameters.put("package", "prepay_id=" + packageStr);
		parameters.put("signType", "MD5");
		parameters.put("paySign", WXPayUtil.createSign(parameters, key));
		parameters.put("timestamp", timestamp);
		parameters.put("orderId", orderId);
		return parameters;
	}

	public static Map<String, String> loadJavaScriptPayConfigByCmbc(String appID, String prePayId, String timestamp, String nonceStr, String paySign, String key) {
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appId", appID);
		parameters.put("timeStamp", timestamp);
		parameters.put("nonceStr", nonceStr);
		parameters.put("package", "prepay_id=" + prePayId);
		parameters.put("signType", "MD5");
		parameters.put("paySign", paySign);
		parameters.put("timestamp", timestamp);
		return parameters;
	}

	private static String createSign(SortedMap<String, String> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		sb.append("key=" + key);
		System.out.println(sb);
		String sign = MD5X.getUpperCaseMD5For32(sb.toString());
		return sign;
	}

	public static void main(String[] args) {
		System.out.println(JSONObject.fromObject(WXPayUtil.orderQuery(new WCOrderQuery("wxee9d9ad775f75311","1426694102","4004762001201706064480651892"),"DF780A97B7D6A8F779F14728BCCD3C4C")).toString());
	}
}

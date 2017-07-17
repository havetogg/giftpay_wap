package org.jumutang.giftpay.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

public class CmbcPayUtil {
	
	static Logger logger = LoggerFactory.getLogger(CmbcPayUtil.class);


	// private final static String key = "bda32d325a3a9b8e75f2f623f2fe4497";
	public final static String key = "36eb4d2b5de48d30a02f2be4d977e661";

	public final static String encryptId = "000010089000001";

	public final static Integer apiVersion = 1;

	public static final String serverUrl = "https://www.njcmbc.com/oss-transaction/gateway/";

	/*public static void main(String[] args) throws Exception {
		String result = sendHttpsPost(serverUrl+"jsprecreate", jsprecreate("oUAowszSYSkRsU-Ljd6aTSbSFZ3U", "1"));
		JSONObject jsonObject = JSONObject.parseObject(result);
		if (jsonObject != null && JSONObject.parseObject(jsonObject.get("result").toString()).get("code").toString().equals("000000")) {
			JSONObject jsonObject2 = JSONObject.parseObject(jsonObject.get("result").toString());
			JSONObject jsonObject3 = JSONObject.parseObject(jsonObject2.get("data").toString());
			JSONObject jsonObject4 = JSONObject.parseObject(jsonObject3.get("sdkParam").toString());
			String prepay_id = jsonObject4.get("package").toString();
			System.out.println(prepay_id.substring(10));
		}else{
			System.out.println("fail");
		}
	}*/

	/**
	 * 2.二维码生成接口
	 * @return
	 */
	public static String precreate() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("mid", "000010000000001");
		obj.put("srcAmt", 0.01);
		obj.put("walletType", 1);
		obj.put("method", "precreate");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 3.被扫接口
	 * @return
	 */
	public static String createAndPay() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "createAndPay");
		obj.put("mid", "000010000000001");
		obj.put("qrcode", "https://qr.alipay.com/bax08019qjk88e2dg0zt0042");
		obj.put("srcAmt", 0.01);
		obj.put("isAsynchronous", "0");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 4.查询订单状态接口
	 * @return
	 */
	public static String query() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "query");
		obj.put("mid", "000010000000001");
		obj.put("bizOrderNumber", "34604868170111165350");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 5.撤销订单接口
	 * @return
	 */
	public static String cancel() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "cancel");
		obj.put("mid", "000010000000001");
		obj.put("bizOrderNumber", "34604868170111165350");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 6.退款接口
	 * @return
	 */
	public static String refund() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "refund");
		obj.put("mid", "000010000000001");
		obj.put("bizOrderNumber", "63785786170111171119");
		obj.put("srcAmt", "0.01");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 7.清结算情况查询接口
	 * @return
	 */
	public static String getMerchantSettlementInfo() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "getMerchantSettlementInfo");
		obj.put("mid", "000010000000001");
		obj.put("txnSubmittedDate", "2016-12-01");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 8.对账单接口
	 * @return
	 */
	public static String downloadbill() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("method", "downloadbill");
		obj.put("mid", "000010000000001");
		obj.put("txnSubmittedDate", "2017-01-10");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 9.获取商户一码付路径 接口
	 * @return
	 */
	public static String getMerchantQrcode() {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("apiVersion", apiVersion);
		obj.put("method", "getMerchantQrcode");
		obj.put("mid", "000010000000001");
		obj.put("txnSubmittedDate", "2017-01-10");
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		System.out.println("signStr==" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}

	/**
	 * 10.一码付预下单接口
	 * @return
	 */
	public static String jsprecreate(String openId, String money,String notifyUrl,String goods_desc) {
		JSONObject content = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("encryptId", encryptId);
		obj.put("apiVersion", apiVersion);
		obj.put("mid", "000010089000001");
		obj.put("walletType", "2");
		obj.put("srcAmt", Double.parseDouble(money)/100);
		obj.put("walletType", "2");
		obj.put("goods_desc", goods_desc);
		obj.put("walletType", "2");
		obj.put("userId", openId);
		obj.put("method", "jsprecreate");
		obj.put("subAppid", "wx3b30b09d7587ec94");
		obj.put("notifyUrl", notifyUrl);
		obj.put("txnDate", Calendar.getInstance().getTimeInMillis());
		content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		content.put("key", key);
		String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
		logger.info("加密参数：" + signStr);
		String sign = MD5X.getLowerCaseMD5For32(signStr);
		logger.info("sign：" + sign);
		content.remove("key");
		content.put("sign", sign);
		return JSON.toJSONString(content);
	}
	
	/**
	 * 发送HTTPS的post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendHttpsPost(String url, String params) {
		DataOutputStream out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		URL u = null;
		HttpsURLConnection con = null;
		// 尝试发送请求
		try {
			logger.info("请求参数："+params);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			u = new URL(url);
			// 打开和URL之间的连接
			con = (HttpsURLConnection) u.openConnection();
			// 设置通用的请求属性
			con.setSSLSocketFactory(sc.getSocketFactory());
			con.setHostnameVerifier(new TrustAnyHostnameVerifier());
			// con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json"); //
			con.setUseCaches(false);
			// 发送POST请求必须设置如下两行
			con.setDoOutput(true);
			con.setDoInput(true);

			con.connect();
			out = new DataOutputStream(con.getOutputStream());
			out.write(params.getBytes("utf-8"));
			// 刷新、关闭
			out.flush();
			out.close();
			// 读取返回内容
			// InputStream is = con.getInputStream();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line).append(System.lineSeparator());
			}
			System.out.println(result);
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}

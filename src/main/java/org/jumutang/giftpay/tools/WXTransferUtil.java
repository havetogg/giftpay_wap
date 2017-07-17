package org.jumutang.giftpay.tools;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.jumutang.giftpay.entity.WCSendMoney;

import java.io.*;
import java.security.KeyStore;
import java.util.*;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 13:43 2017/5/18
 * @Modified By:
 */
public class WXTransferUtil {
    //付款API
    public static String _TRANSFER_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    public static String transfer(WCSendMoney wcSendMoney,String key){

        SortedMap<String, Object> parameters = new TreeMap<>();
        parameters.put("amount", wcSendMoney.getAmount());
        parameters.put("check_name", wcSendMoney.getCheck_name());
        parameters.put("desc", wcSendMoney.getDesc());
        parameters.put("mch_appid", wcSendMoney.getMch_appid());
        parameters.put("mchid", wcSendMoney.getMchid());
        parameters.put("nonce_str", wcSendMoney.getNonce_str());
        parameters.put("openid", wcSendMoney.getOpenid());
        parameters.put("partner_trade_no", wcSendMoney.getPartner_trade_no());
        parameters.put("spbill_create_ip", wcSendMoney.getSpbill_create_ip());

        wcSendMoney.setSign(createSign(parameters, key));
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.autodetectAnnotations(true);
        String xml = xStream.toXML(wcSendMoney);
        System.out.println(xml);
        return ssl(_TRANSFER_URL,xml,wcSendMoney.getMchid());
    }

    private static String ssl(String url,String data,String mchId){
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File("D:/certs/apiclient_cert.p12"));
            keyStore.load(instream, mchId.toCharArray());
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mchId.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpost = new HttpPost(url);

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }

                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return message.toString();
    }

    private static String createSign(SortedMap<String, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String k = entry.getKey();
            String v = entry.getValue().toString();
            sb.append(k + "=" + v + "&");
        }
        sb.append("key=" + key);
        System.out.println(sb);
        String sign = MD5X.getUpperCaseMD5For32(sb.toString());
        return sign;
    }

    public static void main(String[] args) {
        //测试1分钱企业付款
        WCSendMoney wcSendMoney = new WCSendMoney("wxee9d9ad775f75311","1426694102","test002","o4FD4v_VSL8r87LR713h4s_ywo1Y",1,"测试微信企业付款","180.110.16.82");
        System.out.println(WXTransferUtil.transfer(wcSendMoney,"DF780A97B7D6A8F779F14728BCCD3C4C"));
    }
}

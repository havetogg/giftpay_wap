package org.jumutang.giftpay.web.pinganCard;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jumutang.giftpay.tools.HttpUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/7/6.
 */
public class tests {


    //http://ip.taobao.com/service/getIpInfo.php?ip=117.136.45.41

    public final static  String urlPath = "http://ip.taobao.com/service/getIpInfo.php";

    private final static String juhe_appKey = "f5a829f8b2e30e65243b611450c31bb3";


    //转换编码
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed  encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    //发送请求
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    //聚合数据 免费接口[免费会员500次]
    //http://apis.juhe.cn/ip/ip2addr?ip=www.juhe.cn&key=appkey


    public static   String sendGet(String url, String encode) {

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时�?
        get.setConfig(requestConfig);
        get.setHeader("Content-Type", "text/html;charset=UTF-8");

        try {
            HttpResponse httpResponse = closeableHttpClient.execute(get);
            HttpEntity responceEntity = httpResponse.getEntity();
            System.out.println("status:" + httpResponse.getStatusLine());
            return EntityUtils.toString(responceEntity, encode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                closeableHttpClient.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String args []){

//        String params = "ip=117.136.45.41&key="+juhe_appKey;
//
//        String result = sendPost("http://apis.juhe.cn/ip/ip2addr",params);
//
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        System.out.println(jsonObject.toString());
//
//        JsonConfig jsonConfig = new JsonConfig();
//        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//        JSONArray json = JSONArray.fromObject(jsonObject.get("result"), jsonConfig);
//
//        JSONObject IpAddress = JSONObject.parseObject(json.get(0).toString());
//        System.out.println(IpAddress.get("area"));

        String result = sendGet("http://apis.juhe.cn/ip/ip2addr?ip=117.136.45.41&key="+juhe_appKey,"utf-8");

        System.out.println(result);

    }



}

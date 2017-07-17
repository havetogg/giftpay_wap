package org.jumutang.giftpay.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http请求apache工具类
 * @date 2017-06-12
 * @author chencq
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int timeout = 5000;

    /**
     * http GET请求
     *
     * @param url      请求地址
     * @param paramMap 请参数
     * @return 响应结果
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doHttpGet(String url, Map<String, String> paramMap)
            throws URISyntaxException, ClientProtocolException, IOException {
        return doGet(url, paramMap, null);
    }

    /**
     * https GET请求
     *
     * @param url      请求地址
     * @param paramMap 请参数
     * @return 响应结果
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doHttpsGet(String url, Map<String, String> paramMap)
            throws URISyntaxException, ClientProtocolException, IOException {
        return doGet(url, paramMap, createSSLClientDefault());
    }

    /**
     * get 请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     * @throws ClientProtocolException
     */
    private static String doGet(String url, Map<String, String> paramMap, CloseableHttpClient httpClient)
            throws IOException, UnsupportedEncodingException, URISyntaxException, ClientProtocolException {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        if (paramMap != null) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(paramMap.size());
            for (Entry<String, String> entry : paramMap.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            logger.debug("url-->" + url);
        }
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).build();
        httpGet.setConfig(requestConfig);

        logger.info("向  " + httpGet.getURI() + " 发起GET请求");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        String rs = null;
        logger.info("GET请求返回状态码：" + statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            logger.info("GET请求发送成功");
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            rs = EntityUtils.toString(entity, charset);
        } else {
            httpGet.abort();
            logger.error(statusCode + "：GET请求发送失败");
        }

        response.close();
        return rs;
    }

    /**
     * http POST请求
     *
     * @param url      请求地址
     * @param paramMap 请参数
     * @return 响应结果
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doHttpPost(String url, Map<String, String> paramMap)
            throws URISyntaxException, ClientProtocolException, IOException {
        return doPost(url, paramMap, null);
    }

    /**
     * https POST请求
     *
     * @param url      请求地址
     * @param paramMap 请参数
     * @return 响应结果
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doHttpsPost(String url, Map<String, String> paramMap)
            throws URISyntaxException, ClientProtocolException, IOException {
        return doPost(url, paramMap, createSSLClientDefault());
    }

    /**
     * post请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    private static String doPost(String url, Map<String, String> paramMap, CloseableHttpClient httpClient)
            throws URISyntaxException, IOException, ClientProtocolException {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }

        URIBuilder uriBuilder = new URIBuilder(url);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).build();
        httpPost.setConfig(requestConfig);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        // logger.info("请求数据 is ------>"+paramList.toString());
        httpPost.setEntity(new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8")));

        logger.info("向  " + httpPost.getURI() + " 发起POST请求");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String rs = null;
        logger.info("POST请求返回状态码：" + statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            logger.info("POST请求发送成功");
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            rs = EntityUtils.toString(entity, "UTF-8");
            logger.info("请求返回数据 is ----->" + rs);
        } else {
            httpPost.abort();
            logger.error(statusCode + "：POST请求发送失败");
        }

        response.close();
        return rs;
    }


    /**
     * 向云平台发送请求
     *
     * @param url      请求地址
     * @param json 请参数
     * @return 响应结果
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doAcReq(String url, String json)
            throws URISyntaxException, ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).build();
        httpPost.setConfig(requestConfig);

        httpPost.setEntity(new StringEntity(json));
        logger.info("向  " + httpPost.getURI() + " 发起POST请求");
        // logger.info("请求数据  " + json );
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String rs = null;
        logger.info("POST请求返回状态码：" + statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            logger.info("POST请求发送成功");
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            rs = EntityUtils.toString(entity, charset);
        } else {
            httpPost.abort();
            logger.error(statusCode + "：POST请求发送失败");
        }

        response.close();
        return rs;
    }

    /**
     * 下载文件
     *
     * @param url          http://www.xxx.com/img/333.jpg
     * @param destFileName xxx.jpg/xxx.png/xxx.txt
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void getFile(String url, String destFileName) throws ClientProtocolException, IOException {
        // 生成一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        File file = new File(destFileName);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
            fout.close();
        } finally {
            // 关闭低层流。
            in.close();
        }
        httpclient.close();
    }

    /**
     * 获取发送https请求的httpclient
     *
     * @return
     */
    private static CloseableHttpClient createSSLClientDefault() {
        try {
            /*SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();*/

            SSLContext sslContext =
                    new org.apache.http.ssl.SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient build = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            return build;
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("startPage","1");
        paramMap.put("count","10");
        //成功
        String resul=HttpClientUtil.doHttpsPost("https://prod1.juxinbox.com/njvote_spring/workList.do", paramMap);
        //失败
        //HttpClientUtil.doHttpPost("https://prod1.juxinbox.com/njvote_spring/workList.do", paramMap);

    }
}

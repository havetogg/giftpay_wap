package org.jumutang.giftpay.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jumutang.giftpay.model.PhoneModel;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 10:29 2017/6/12
 * @Modified By:
 */
public class PhoneUtil {
    //请求接口地址
    private static final String getUrl = "http://jshmgsdmfb.market.alicloudapi.com/shouji/query?shouji=%s";
    //key
    private static final String key = "627d9ac19c3c4ef0a72211ddd712eccc";
    private static final String key2 = "22cac6176f614fb08d1e1ca63123fa5c";
    private static final String key3 = "b087d6a29364453097a0d60cbfcc8d71";

    public static PhoneModel getPhone(String phone) {
        String url = String.format(getUrl, phone);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时�?
        get.setConfig(requestConfig);
        get.setHeader("Content-Type", "text/html;charset=UTF-8");
        get.setHeader("Authorization", "APPCODE " + key);
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(get);
            HttpEntity responceEntity = httpResponse.getEntity();
            System.out.println("status:" + httpResponse.getStatusLine());
            JSONObject returnObj = JSONObject.parseObject(EntityUtils.toString(responceEntity, "utf-8"));
            PhoneModel phoneModel = null;
            if (returnObj.getString("status").equals("0")) {
                JSONObject result = returnObj.getJSONObject("result");
                phoneModel = new PhoneModel();
                phoneModel.setPhone(phone);
                phoneModel.setCard(result.getString("company"));
                phoneModel.setCity(result.getString("city"));
                phoneModel.setProvince(result.getString("province"));
            }
            return phoneModel;
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
}

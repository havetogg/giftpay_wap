package org.jumutang.giftpay.plugin;

import org.jumutang.giftpay.tools.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/***
 * 加密配置文件插件
 * @author chencq
 * @date 2017/7/26
 **/
public class EncryptConfigurer extends PreferencesPlaceholderConfigurer {

    private static final Logger _LOGGER = LoggerFactory.getLogger(EncryptConfigurer.class);

    //待解密的字符数组
    private String[] propertyNames = {
            "db.password"
    };

    /**
     * 解密指定propertyName的加密属性值
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        for (String p : propertyNames) {
            if (p.equalsIgnoreCase(propertyName)) {
                String  decodePropertyValue = AESUtil.AESDecode(propertyValue);
                _LOGGER.info("配置文件属性：" + propertyName + "，值（密文）：" + propertyValue + "，经过AES解密后字符串：" + decodePropertyValue);
                return decodePropertyValue;
            }
        }
        return super.convertProperty(propertyName, propertyValue);
    }
}

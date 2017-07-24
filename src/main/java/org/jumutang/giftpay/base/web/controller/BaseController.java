package org.jumutang.giftpay.base.web.controller;


import org.jumutang.caranswer.service.SellCarInfoService;
import org.jumutang.giftpay.entity.GoodsListModel;
import org.jumutang.giftpay.entity.UserModel;
import org.jumutang.giftpay.model.BlackUserModel;
import org.jumutang.giftpay.service.*;
import org.jumutang.giftpay.service.impl.BlackUserService;
import org.jumutang.giftpay.service.impl.OilRecordServiceImpl;
import org.jumutang.giftpay.service.impl.ThirdUserServiceImpl;
import org.jumutang.giftpay.service.impl.UserOilInfoServiceImpl;
import org.jumutang.giftpay.tools.EmojiFilter;
import org.jumutang.giftpay.tools.MD5Util;
import org.jumutang.giftpay.tools.MD5X;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by RuanYJ on 2017/1/4.
 */

public class BaseController {


    //主用户服务
    @Autowired
    protected UserMainService userMainService;
    //子用户服务
    @Autowired
    protected UserSubService userSubService;
    //余额账户生成服务层
    @Autowired
    protected BalanceServiceI balanceServiceI;
    //订单/账单服务层
    @Autowired
    protected PayInfoServiceI payInfoServiceI;
    //加油记录服务层
    @Autowired
    protected OilRecordServiceImpl oilRecordService;
    //第三方服务层
    @Autowired
    protected ThirdUserServiceImpl thirdUserService;
    //用户加油信息服务层
    @Autowired
    protected UserOilInfoServiceImpl userOilInfoService;
    //第三方pvuv统计服务层
    @Autowired
    protected IPVUVRecordService PVUVRecordService;
    //用户家庭管理服务层
    @Autowired
    protected IFamilyService familyService;
    //用户账户服务管理层
    @Autowired
    protected IRechargeAccountService rechargeAccountService;
    @Autowired
    protected SellCarInfoService sellCarInfoService;

    @Autowired
    protected IAdvertiseService advertiseService;
    @Autowired
	protected IRecoGoodsService recoGoodsService;
    @Autowired
    protected ICardPrizeService cardPrizeService;
    @Autowired
    protected IGoodsListService goodsListService;
    @Autowired
    protected IBlackUserService blackUserService;
    @Autowired
    protected IAdvertiseRecordService advertiseRecordService;
    @Autowired
    protected ICountService countService;
    @Autowired
    protected IManagerCountService managerCountService;

    /**
     *  预支付订单服务层
     */
    @Autowired
    protected  IOrderService orderService;

    //微信端微信登录接口
    @Value("#{propertyFactoryBean['wx_login']}")
    protected String wxLoginUrl;
    @Value("#{propertyFactoryBean['app_id']}")
    protected String appId;
    @Value("#{propertyFactoryBean['app_secret']}")
    protected String appSecret;
    @Value("#{propertyFactoryBean['wx_authorize']}")
    protected String wxAuthorize;
    @Value("#{propertyFactoryBean['gate_user_info']}")
    protected String gateUserInfo;
    @Value("#{propertyFactoryBean['query_red_url']}")
    protected String queryRedUrl;
    @Value("#{propertyFactoryBean['add_red_url']}")
    protected String addRedUrl;
    @Value("#{propertyFactoryBean['update_red_url']}")
    protected String updateRedUrl;
    /**
     * 缴费充值更新红包状态
     */
    @Value("#{propertyFactoryBean['update_red_url_other']}")
    protected String updateRedOtherUrl;
    //key
    @Value("#{propertyFactoryBean['key']}")
    protected String key;
    //mchID
    @Value("#{propertyFactoryBean['mchID']}")
    protected String mchID;
    //支付回调链接
    @Value("#{propertyFactoryBean['oil_pay_redirect']}")
    protected String oilPayRedirect;
    //直接查看红包链接
    @Value("#{propertyFactoryBean['my_red_list']}")
    protected String myRedListUrl;
    //直接查询手机是否有红包
    @Value("#{propertyFactoryBean['check_red_byPhone']}")
    protected String checkRedPhoneUrl;
    //获取中石化openId
    @Value("#{propertyFactoryBean['get_zsh_openId']}")
    protected String getZshOpenIdUrl;
    //红包地址主页入口
    @Value("#{propertyFactoryBean['check_red_home_page']}")
    protected String checkRedHomePage;
    //查询用户所有红包
    @Value("#{propertyFactoryBean['query_all_red_url']}")
    protected String queryAllRedUrl;
    //充值缴费支付页面
    @Value("#{propertyFactoryBean['order_pay_url']}")
    protected String orderPayUrl;

    /**
     * 欧飞接口
     */
    //欧飞接口查询省
    @Value("#{propertyFactoryBean['oufei.provinceList.url']}")
    protected String oufeiProvinceListUrl;
    //欧飞接口水电煤用户id
    @Value("#{propertyFactoryBean['oufei.provinceList.userid']}")
    protected String oufeiLifeUserId;
    //欧飞接口版本号
    @Value("#{propertyFactoryBean['oufei.provinceList.version']}")
    protected String oufeiProvinceListVersion;
    //欧飞接口查询城市
    @Value("#{propertyFactoryBean['oufei.cityList.url']}")
    protected String oufeiCityListUrl;
    //欧飞接口秘钥
    @Value("#{propertyFactoryBean['oufei.provinceList.userpws']}")
    protected String oufeiLifeUserPws;
    //欧飞接口查询缴费类型
    @Value("#{propertyFactoryBean['oufei.payProjectList.url']}")
    protected String oufeiPayProjectListUrl;
    //欧飞接口查询缴费单位
    @Value("#{propertyFactoryBean['oufei.payPayUnitList.url']}")
    protected String oufeiPayUnitListUrl;
    //欧飞接口查询缴费单位
    @Value("#{propertyFactoryBean['oufei.payModeList.url']}")
    protected String oufeiPayModeListUrl;
    //公共事业商品信息查询接口
    @Value("#{propertyFactoryBean['oufei.queryClassId.url']}")
    protected String oufeiQueryClassIdUrl;
    //生活缴费充值
    @Value("#{propertyFactoryBean['oufei.utilityOrder.url']}")
    protected String oufeiUtilityOrderUrl;
    //账单查询
    @Value("#{propertyFactoryBean['oufei.queryBalance.url']}")
    protected String oufeiBalanceUrl;
    //手机话费充值
    @Value("#{propertyFactoryBean['oufei.tel.url']}")
    protected String oufeiTelUrl;
    //欧飞油卡充值
    @Value("#{propertyFactoryBean['oufei.fuelCardRecharge.url']}")
    protected String fuelCardRechargeUrl;


    //话费欧飞用户账号
    @Value("#{propertyFactoryBean['oufei.tel.userid']}")
    protected String telUserid;
    //话费欧飞用户密码
    @Value("#{propertyFactoryBean['oufei.tel.userpws']}")
    protected String telUserpws;
    //话费欧飞密钥
    @Value("#{propertyFactoryBean['oufei.tel.keystr']}")
    protected String telKeystr;
    //话费欧飞版本
    @Value("#{propertyFactoryBean['oufei.tel.version']}")
    protected String telVersion;
    //话费欧飞用户密码
    @Value("#{propertyFactoryBean['oufei.tel.cardid']}")
    protected String telCardid;

    /**
     * 审核链接
     */
    @Value("#{propertyFactoryBean['review_url']}")
    protected String reviewUrl;

    /**
     * 日志方法
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 过滤用户微信昵称
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String filterNickName(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String nickName = request.getParameter("nickname");
        //仍有乱码加以下代码
//        nickName = new String(nickName.getBytes("ISO-8859-1"), "UTF-8");
        nickName = EmojiFilter.filterEmoji(nickName);
        return nickName;
    }

    /**
     * 获取当前服务器项目地址
     *
     * @param request
     * @param response
     * @return
     */
    protected String getBaseUrl(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
                + "/";
        return basePath;
    }

    /**
     * 直接引用微信授权
     *
     * @param request
     * @param response
     * @param functionName
     * @throws IOException
     */
    public void loginSendRedirect(HttpServletRequest request, HttpServletResponse response, String functionName) throws IOException {
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                functionName);
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    /**
     * 获取session中的user信息
     *
     * @param session
     * @return
     */
    protected String getUserId(HttpSession session) {
        return (String) session.getAttribute("userId");
    }

    /**
     * double转string类型
     *
     * @param string
     * @return
     */
    public int stringToInt(String string) {
        int j = 0;
        String str = string.substring(0, string.indexOf(".")) + string.substring(string.indexOf(".") + 1);
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    public boolean openIdISNull(String openId) {
        if (StringUtils.isEmpty(openId) || openId.equals("") || openId == null || openId.equals("null")) {
            return true;
        }
        return false;
    }
    protected void showParams(HttpServletRequest request) {
        logger.error("打印参数方法-----");
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }

        Set<Map.Entry<String, String>> set = map.entrySet();
        logger.error("------------------------------");
        for (Map.Entry entry : set) {
            logger.error(entry.getKey() + ":" + entry.getValue());
        }
        logger.error("------------------------------");
    }
    protected String makeGoodsMd5Str(GoodsListModel goodsListModel){
        String md5Str= MD5Util.MD5Encode(goodsListModel.getId()+goodsListModel.getGoodsType()+goodsListModel.getGoodsSubType()+goodsListModel.getRealPrice()+goodsListModel.getGoodsPrice());
        return md5Str;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, Exception {
        DecimalFormat df = new DecimalFormat("######0");
        System.out.println(String.valueOf(df.format(1 * 100)));
    }


}

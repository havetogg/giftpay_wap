package org.jumutang.giftpay.web.mall;

import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.model.BalanceModel;
import org.jumutang.giftpay.model.UserMainModel;
import org.jumutang.giftpay.model.UserSubModel;
import org.jumutang.giftpay.tools.MD5X;
import org.jumutang.giftpay.tools.UUIDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Auther: Tinny.liang
 * @Description:
 * @Date: Create in 15:09 2017/7/5
 * @Modified By:
 */
@Controller
@RequestMapping(value = "/giftpay/mall", method = {RequestMethod.GET, RequestMethod.POST})
public class WXLoginController extends BaseController{

    //积分商城手动授权
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/wxMallByInfo")
    public void wxMallByInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("进入积分商城手动授权方法");
        Long timestamp = System.currentTimeMillis();
        String md5 = MD5X.getLowerCaseMD5For32(appId + appSecret + timestamp);
        String openIdUrl = gateUserInfo.replace("_TIMESTAMP", String.valueOf(timestamp)).replace("_MD5", md5).replace("_APPID", appId);
        String targetUrl = openIdUrl + "&redirect_client_url=" + URLEncoder.encode(this.getBaseUrl(request, response).replace(":80", "") +
                "giftpay/mall/initUserInfo.htm");
        targetUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
                + URLEncoder.encode(targetUrl) + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.info("targetUrl:" + targetUrl);
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }

    //用户信息初始化
    @RequestMapping(value = "/initUserInfo")
    public void initUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        logger.info("进入获取用户信息方法");
        String openId = request.getParameter("openid");
        String targetUrl="";
        if (openIdISNull(openId)) {
            logger.error("静默授权未获取到openId，跳转到手动授权");
            targetUrl = this.getBaseUrl(request, response).replace(":80", "")+"giftpay/mall/wxMallByInfo.htm";
        }else{
            session.setAttribute("openId", openId);
            logger.info("参数:openId = "+openId);

            UserSubModel subModel = new UserSubModel();
            subModel.setOpenId(openId);
            List<UserSubModel> subList = this.userSubService.queryUserSubModel(subModel);
            String userId = "";
            if (subList.size() == 0) {
                //不存在子表
                UserMainModel userMainModel = new UserMainModel();
                userMainModel.setId(UUIDUtil.getUUID());
                userMainModel.setPhone("");
                userMainModel.setStatus("0");
                this.userMainService.addUserMainModel(userMainModel);
                subModel.setStatus("0");
                subModel.setType("2");
                subModel.setPhone("");
                subModel.setId(UUIDUtil.getUUID());
                subModel.setUserId(userMainModel.getId());
                userId = userMainModel.getId();
                subModel.setOpenId(openId);
                this.userSubService.addUserSubModel(subModel);
            } else {
                logger.info("存在 获取userId");
                userId = subList.get(0).getUserId();
            }
            session.setAttribute("userId",userId);


            String nickName = request.getParameter("nickname");
            String headImgUrl = request.getParameter("headimgurl");

        }
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
    }
}

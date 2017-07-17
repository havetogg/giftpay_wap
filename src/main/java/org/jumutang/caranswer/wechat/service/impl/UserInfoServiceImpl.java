
package org.jumutang.caranswer.wechat.service.impl;

import org.jumutang.caranswer.framework.ContextContast;
import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.framework.x.StringX;
import org.jumutang.caranswer.framework.x.UniqueX;
import org.jumutang.caranswer.model.Authorize;
import org.jumutang.caranswer.model.UserInfo;
import org.jumutang.caranswer.wechat.ErrorCodePools;
import org.jumutang.caranswer.wechat.dao.IAuthorizeDao;
import org.jumutang.caranswer.wechat.dao.IUserInfoDao;
import org.jumutang.caranswer.wechat.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoServiceImpl implements IUserInfoService
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private IAuthorizeDao authorizeDao;

    @Autowired
    private IUserInfoDao userInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jumutang.caranswer.wechat.service.IUserInfoService#loginValidation(
     * java.lang.String)
     */
    @Override
    public ContextResult<UserInfo> loginValidation(String wechatOpenID)
    {
        ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
        _LOGGER.info(StringX.stringFormat("登录微信openid=[0],准备查询授权信息", wechatOpenID));
        Authorize authorize = authorizeDao.queryAuthorize(new Authorize().setWeChatCode(wechatOpenID));
        if (authorize == null)
        {
            cr.setCode(ErrorCodePools._FAIL_10001).setMess(ErrorCodePools.getMess(ErrorCodePools._FAIL_10001))
                    .setResultObject(null);
            return cr;
        }
        _LOGGER.info(StringX.stringFormat("通过wechatOpenID查询到的用户授权信息为：[0]", authorize.toJsonString()));
        UserInfo userInfo = userInfoDao.queryUserInfo(new UserInfo().setUserID(authorize.getUserID()));
        _LOGGER.info(StringX.stringFormat("查询到的用户信息为：[0]", userInfo.toJsonString()));
        cr.setCode(ErrorCodePools._SUCCESS_1).setMess(ErrorCodePools.getMess(ErrorCodePools._SUCCESS_1))
                .setResultObject(userInfo);
        return cr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jumutang.caranswer.wechat.service.IUserInfoService#activateUserInfo(
     * java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public ContextResult<UserInfo> activateUserInfo(WeChatUserInfo wechatUserInfo , String tel)
    {
        ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
        _LOGGER.info(StringX.stringFormat("激活用户=[0]", tel));
        UserInfo userInfo = userInfoDao.queryUserInfo(new UserInfo().setUserTel(tel));
        // 判断会员是否在数据库中存在
        if (userInfo == null)
        {
            cr.setCode("0").setMess("不存在该会员用户，无法进行绑定操作！");
            return cr;
        }
        // 判断微信用户是否用其他账户绑定过
        // 根据openid 取微信授权信息 如果不为null，用userid取userinfo信息，判断 表里的tel和传来的tel是否一直
        Authorize authorize1 = authorizeDao.queryAuthorize(new Authorize().setWeChatCode(wechatUserInfo.getOpenID()));
        if (authorize1 != null)
        {
            UserInfo userInfo1 = userInfoDao.queryUserInfo(new UserInfo().setUserID(authorize1.getUserID()));
            if (userInfo1 != null && !userInfo1.getUserTel().equals("tel"))
            {
                cr.setCode("0").setMess("该微信账户已注册").setResultObject(userInfo1);
                return cr;
            }
        }
        _LOGGER.info("开始激活...");
        Authorize authorize = new Authorize();
        authorize.setAuthorizeID(UniqueX.new32UUIDUpperCaseString()).setUserID(userInfo.getUserID())
                .setNikeName(wechatUserInfo.getNickName()).setSex(wechatUserInfo.getSex())
                .setProvince(wechatUserInfo.getProvince()).setCity(wechatUserInfo.getCity())
                .setCountry(wechatUserInfo.getCountry()).setHeadimgurl(wechatUserInfo.getHeadimgurl())
                .setWeChatCode(wechatUserInfo.getOpenID());
        _LOGGER.info(StringX.stringFormat("开始更新微信授权信息=[0]", authorize.toJsonString()));
        authorizeDao.addAuthorize(authorize);
        _LOGGER.info("激活成功...");
        cr.setCode("1").setMess("用户激活成功!").setResultObject(userInfoDao.queryUserInfo(new UserInfo().setUserTel(tel)));
        return cr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jumutang.caranswer.wechat.service.IUserInfoService#registerUser(org.
     * jumutang.caranswer.model.Authorize,
     * org.jumutang.caranswer.model.UserInfo)
     */
    @Override
    @Transactional
    public ContextResult<UserInfo> registerUser(WeChatUserInfo wechatUserInfo , UserInfo userInfo)
    {
        ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
        _LOGGER.info(StringX.stringFormat("用户注册信息=[0]", userInfo.toJsonString()));
        if (userInfoDao.queryUserInfo(userInfo) != null)
        {
            cr.setCode(ContextContast._FALSE_CODE).setMess("当前手机号已被注册！");
            return cr;
        }
        if (StringX.isNotEmpty(userInfo.getRecommendCode()))
        {
        	UserInfo recommendUserInfo = userInfoDao.queryUserInfo(new UserInfo().setUserTel(userInfo.getRecommendCode()));
        	if (null == recommendUserInfo) {
        		cr.setCode(ContextContast._FALSE_CODE).setMess("推荐码不存在！");
                return cr;
			}
        }
        _LOGGER.info("开始注册...");
        userInfo.setUserID(UniqueX.new32UUIDUpperCaseString());
        userInfoDao.addUserInfo(userInfo);
        Authorize authorize = new Authorize();
        authorize.setAuthorizeID(UniqueX.new32UUIDUpperCaseString()).setUserID(userInfo.getUserID())
                .setNikeName(wechatUserInfo.getNickName()).setSex(wechatUserInfo.getSex())
                .setProvince(wechatUserInfo.getProvince()).setCity(wechatUserInfo.getCity())
                .setCountry(wechatUserInfo.getCountry()).setHeadimgurl(wechatUserInfo.getHeadimgurl())
                .setWeChatCode(wechatUserInfo.getOpenID());
        _LOGGER.info(StringX.stringFormat("插入微信授权信息=[0]", authorize.toJsonString()));
        authorizeDao.addAuthorize(authorize);
        _LOGGER.info("注册成功...");
        return cr.setCode(ContextContast._TRUE_CODE).setMess("注册成功!").setResultObject(userInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jumutang.caranswer.wechat.service.IUserInfoService#loadUserInfo(java.
     * lang.String)
     */
    @Override
    public ContextResult<UserInfo> loadUserInfo(String userID)
    {
        ContextResult<UserInfo> cr = new ContextResult<UserInfo>();
        _LOGGER.info(StringX.stringFormat("查询用户信息，主键=[0]", userID));
        UserInfo userInfo = userInfoDao.queryUserInfo(new UserInfo().setUserID(userID));
        _LOGGER.info(StringX.stringFormat("用户信息=[0]", userInfo.toJsonString()));
        _LOGGER.info("开始查询第三方授权信息!");
        Authorize authorize = authorizeDao.queryAuthorize(new Authorize().setUserID(userID));
        _LOGGER.info(StringX.stringFormat("微信授权信息=[0]", authorize.toJsonString()));
        if (userInfo != null && authorize != null)
            userInfo.setAuthorize(authorize);
        cr.setCode("0").setMess("用户信息查询成功！").setResultObject(userInfo);
        return cr;
    }
}

package org.jumutang.caranswer.wechat.service;

import org.jumutang.caranswer.framework.ContextResult;
import org.jumutang.caranswer.framework.wechat.model.WeChatUserInfo;
import org.jumutang.caranswer.model.UserInfo;

/**
 * 用户信息接口
 * 
 * @author YuanYu
 *
 */
public interface IUserInfoService {

	/**
	 * <p>
	 * 登陆验证方法
	 * </p>
	 * <p>
	 * 根据微信登陆获取到的wechatOpenID信息去用户授权表查询当前用户是否为已注册用户<br>
	 * 为注册用户：全局结果集中包含用户的对象信息<br>
	 * 为非注册用户：返回的全局结果集对象中的object对象为null
	 * </p>
	 * 
	 * @param wechatOpenID
	 *            微信openID
	 * 
	 * @return 全局结果集
	 */
	public ContextResult<UserInfo> loginValidation(String wechatOpenID);

	/**
	 * <p>
	 * 激活线下开卡会员用户
	 * </p>
	 * <p>
	 * 根据用户传入的tel信息查询出当前数据库中是否存在注册手机号为该手机号的注册会员<br>
	 * 如存在：与当前登陆的  绑定 , 全局结果集中包含用户对象信息<br>
	 * 如不存在：全局结果集的code信息为：0 ， mess信息为：不存在该会员用户，无法进行绑定操作！
	 * </p>
	 * 
	 * @param wechatOpenID
	 *            微信openID
	 * @param tel
	 *            用户手机号
	 * 
	 * @return 全局结果集
	 */
	public ContextResult<UserInfo> activateUserInfo(WeChatUserInfo wechatUserInfo , String tel);

	/**
	 * <p>
	 * 用户注册方法
	 * </p>
	 * <p>
	 * step 1 ： 根据传入的UserInfo中的TEL信息去数据库中分析当前数据库中是否已经存在注册手机号相同的用户
	 * step 1.1 ：如果存在，返回业务逻辑的错误信息（全局结果集的code为：0 ， 全局结果集的mess：当前手机号已被注册！）
	 * step 1.2 :  如果不存在，执行step 2 操作
	 * step 2 : 将UserInfo入库 , 然后将 Authorize 入库
	 * </p>
	 * @param authorize 用户授权信息
	 * @param userInfo 用户信息
	 * @return 全局结果集信息
	 */
	public ContextResult<UserInfo> registerUser(WeChatUserInfo wechatUserInfo, UserInfo userInfo);
	
	/**
	 * <p>查询用户信息</p>
	 * @param userID 用户主键
	 * @return 全局结果集
	 */
	public ContextResult<UserInfo> loadUserInfo(String userID);
	
}

/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IVerifyCodeRedisDAO
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.redis
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:45:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.redis;

/**
 * 验证码DAO
 * @author SunChengfei
 *
 */
public interface IVerifyCodeRedisDAO {
	/**
	 * 手机注册验证
	 */
	public String MOBILE_REGISTER = "mobileRegister";
	
	/**
	 * 手机绑定验证
	 */
	public String MOBILE_BOUND = "mobileBound";
	
	/**
	 * 手机找回密码
	 */
	public String MOBILE_BACK_PD = "mobileBackPassword";
	
	/**
	 * 邮箱找回密码
	 */
	public String EMAIL_BACK_PD = "emailBackPassword";
	
	/**
	 * 保存注册手机验证码
	 * @param mobilePhone
	 * @param verifyCode
	 */
	public void saveMobileRegisVfCode(String mobilePhone, String verifyCode);
	
	/**
	 * 获取注册手机验证码
	 * @param mobilePhone
	 * @return
	 */
	public String getMobileRegisVfCode(String mobilePhone);
	
	/**
	 * 保存绑定手机验证码
	 * @param mobilePhone
	 * @param verifyCode
	 */
	public void saveMobileBoundVfCode(String mobilePhone, String verifyCode);
	
	/**
	 * 获取绑定手机验证码
	 * @param mobilePhone
	 * @return
	 */
	public String getMobileBoundVfCode(String mobilePhone);
	
	/**
	 * 保存手机找回密码验证码
	 * @param mobilePhone
	 * @param verifyCode
	 */
	public void saveMobileBackPdToken(String mobilePhone, String verifyCode);
	
	/**
	 * 获取手机找回密码验证码
	 * @param mobilePhone
	 * @return
	 */
	public String getMobileBackPdToken(String mobilePhone);
	
	/**
	 * 保存邮箱找回密码验证码
	 * @param tokenId
	 * @param loginAccount
	 */
	public void saveEmailBackPdToken(String tokenId, String loginAccount);
	
	/**
	 * 根据tokenId获取账号
	 * @param loginAccount
	 * @return
	 */
	public String getAccountByToken(String tokenId);
	
}

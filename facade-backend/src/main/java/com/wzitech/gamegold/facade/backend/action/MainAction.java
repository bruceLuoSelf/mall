/************************************************************************************
 *  Copyright 2012 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountServiceImpl
 *	包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class MainAction extends AbstractAction {

	private UserInfoEO currentUser;

	private String oldPassword;

	private String newPassword;

	@Autowired
	IUserInfoManager userInfoManager;

	@Autowired
	IFileManager fileManager;

	private Map<String, String> imageSizeMap;
	
	/**
	 * 订单自动刷新时间（秒）
	 */
	@Value("${order.autoload.time}")
	private int orderAutoLoadTime = 30;
	
	/**
	 * 公司得到拥金的比例
	 */
	@Value("${sub_commission.base}")
	private double subCommissionBase = 0.08;
	
	/**
	 * 
	 * 得到系统的配置信息
	 * @author ztjie
	 * @date 2013-11-26 下午5:10:36
	 * @return
	 * @see
	 */
	public String sysConfig(){
		orderAutoLoadTime = orderAutoLoadTime*1000;
		StringBuilder sb = new StringBuilder();
		imageSizeMap = new HashMap<String, String>();
		String[] strs = fileManager.getUserAvatarSizeList().split("[,:;]");
		imageSizeMap.put("userAvatarSize", sb.append("_").append(strs[0]).toString());
		sb = new StringBuilder();
		strs = fileManager.getImageGoodsSizeList().split("[,:;]");
		imageSizeMap.put("imageGoodsFileSize", sb.append("_").append(strs[0]).toString());
		sb = new StringBuilder();
		strs = fileManager.getImageTradePlaceSizeList().split("[,:;]");
		imageSizeMap.put("imageTradePlaceSizeList", sb.append("_").append(strs[0]).toString());
		return returnSuccess();
	}

	/**
	 * 得到当前登录用户信息
	 * 
	 * @return
	 */
	public String currentLoginUserInfo() {
		currentUser = (UserInfoEO)CurrentUserContext.getUserForAction();
		return this.returnSuccess();
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String changePassword() {
		UserInfoEO currentUser = (UserInfoEO)CurrentUserContext.getUserForAction();
		try {
			currentUser = userInfoManager.modifyCurrentUserPassword(
					currentUser, oldPassword, newPassword);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public UserInfoEO getCurrentUser() {
		return currentUser;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Map<String, String> getImageSizeMap() {
		return imageSizeMap;
	}

	public int getOrderAutoLoadTime() {
		return orderAutoLoadTime;
	}

	public double getSubCommissionBase() {
		return subCommissionBase;
	}
	
}

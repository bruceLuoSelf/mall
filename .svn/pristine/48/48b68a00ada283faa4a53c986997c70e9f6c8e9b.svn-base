/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		SessionManagerImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.business.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午6:07:44
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author SunChengfei
 *
 */
@Component
public class SessionManagerImpl extends AbstractBusinessObject implements ISessionManager {
	@Autowired
	IUserInfoRedisDAO userInfoRedisDAO;
	
	@Autowired
	IUserInfoManager useInfoManager;

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.shuabao.usermgmt.business.ISessionManager#createSession(java.lang.String)
	 */
	@Override
	public String createSession(String uid) {
		// 生成新的authkey
		String authkey = UUID.randomUUID().toString().replace("-", "");
		// 保存authkey
		userInfoRedisDAO.setUserAuthkey(uid, authkey);
		return authkey;
	}

    @Override
    public String createSession(String uid, int timeout) {
        // 生成新的authkey
        String authkey = UUID.randomUUID().toString().replace("-", "");
        // 保存authkey
        userInfoRedisDAO.setUserAuthkey(uid, authkey, timeout);
        return authkey;
    }

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.shuabao.usermgmt.business.ISessionManager#removeSession(java.lang.String)
	 */
	@Override
	public void removeSession(String uid) {
		userInfoRedisDAO.removeUserAuthkey(uid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.shuabao.usermgmt.business.ISessionManager#getUserByAuthkey(java.lang.String)
	 */
	@Override
	public UserInfoEO getUserByAuthkey(String authkey) {
		String uid = userInfoRedisDAO.getUidByAuthkey(authkey);
		if(StringUtils.isEmpty(uid)){
			return null;
		}
		return useInfoManager.findUserByUid(uid);
	}
}

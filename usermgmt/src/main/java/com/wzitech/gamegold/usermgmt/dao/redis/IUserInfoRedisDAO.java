/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IUserInfoRedisDAO
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.redis
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:41:00
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.redis;

import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SunChengfei
 *
 */
public interface IUserInfoRedisDAO {
	/**
	 * 通过Uid查找用户
	 * @param uid
	 * @return
	 */
	public UserInfoEO findUserByUid(String uid);
	
	/**
	 * 通过帐号名查找用户
	 * @param loginAccount
	 * @return
	 */
	public UserInfoEO findUserByLoginAccount(String loginAccount);
	
	/**
	 * 保存用户信息至Redis
	 * @param userInfo
	 */
	public void saveUser(UserInfoEO userInfo);
	
	/**
	 * 设置用户Session
	 * @param uid
	 * @param authkey
	 */
	public void setUserAuthkey(String uid, String authkey);

    /**
     * 设置用户Session
     * @param uid
     * @param authkey
     */
    public void setUserAuthkey(String uid, String authkey, int timeout);
	
	/**
	 * 移除用户Session
	 * @param uid
	 */
	public void removeUserAuthkey(String uid);
	
	/**
	 * 通过Session Authkey获取Uid
	 * @param authkey
	 * @return
	 */
	public String getUidByAuthkey(String authkey);
	
	/**
	 * 删除用户
	 * @param uid
	 */
	public void deleteUser(String uid);

	/**
	 * 获取所有的客服
	 * @return
	 */
	List<UserInfoEO> findAllService();

	/**
	 * 查询指定游戏担保客服ID列表，待发货订单数量少的客服靠前排序
	 */
	List<String> findAssureServiceByGame(String gameName);

	/**
	 * 保存担保客服ID，按忙碌程度
	 * @param gameName
	 * @param users
	 */
	void saveAssureService(String gameName, List<UserInfoEO> users);

	/**
	 * 清除前台订单页担保客服ID排序
	 */
	void removeAssureService();

	/**
	 * 锁
	 */
	boolean lock(long timeout, TimeUnit timeUnit,String lockKey);

	/**
	 * 关锁
     */
	boolean unlock(String unlockKey );
}

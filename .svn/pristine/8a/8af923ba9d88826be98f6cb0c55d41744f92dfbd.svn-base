/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IServicerOrderManager
 *	包	名：		com.wzitech.gamegold.common.servicer
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午11:20:57
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.servicer;

import java.util.List;

/**
 * 客服处理订单数管理
 * @author SunChengfei
 *
 */
public interface IServicerOrderManager {
	/**
	 * 该客服处理订单数是否被初始化
	 * @return
	 */
	public boolean isInitOrderNum(String gameName, String region, String server, 
			String gameRace, Long servicerId);
	
	/**
	 * 初始化客服处理订单数为0
	 * @param servicerId
	 */
	public void initOrderNum(String gameName, String region, String server, 
			String gameRace, Long servicerId);
	
	/**
	 * 初始化客服处理订单数
	 * @param servicerId
	 */
	public void initOrderNum(Long servicerId);
	
	/**
	 * 获取该客服处理订单数
	 * @return
	 */
	public Double getOrderNum(String gameName, String region, String server, 
			String gameRace, Long servicerId);
	
	/**
	 * 客服处理订单数+1
	 * @param servicerId
	 */
	public void addOrderNum(String gameName, String region, String server, 
			String gameRace, Long servicerId);
	
	/**
	 * 客服处理订单数-1
	 * @param servicerId
	 */
	public void subOrderNum(String gameName, String region, String server, 
			String gameRace, Long servicerId);
	
	/**
	 * 按照处理订单数从小到大排序客服Id
	 * @return
	 */
	public List<String> sortServicer(String gameName, String region, String server, String gameRace);
	
	/**
	 * 按照处理订单数从小到大排序客服Id
	 * @return
	 */
	public List<String> sortServicer();
	/**
	 * 查询客服当前处理的订单数
	 * @param servicerId
	 * @param orderNum
	 */
	public void serviceOrderNum(Long servicerId,int orderNum);

	/**
	 * 初始化客服处理卖家入驻数
	 * @param servicerId
	 */
	public void initEnterNum(Long servicerId);

	/**
	 * 更新卖家入驻客服当前处理的入驻数
	 * @param servicerId
	 * @param num
	 */
	public void updateServiceEnterNum(Long servicerId, int num);

	/**
	 * 按照处理卖家入驻数从小到大排序客服Id
	 * @return
	 */
	public List<String> sortEnterServicer();

	/**
	 * 客服处理卖家入驻数+1
	 * @param servicerId
	 */
	public void addEnterNum(Long servicerId);

	/**
	 * 客服处理卖家入驻数-1
	 */
	public void subEnterNum(Long servicerId);

	/**
	 * 客服已处理的订单数量+1
	 * @param servicerId
	 */
	void addOrderProcessedCount(Long servicerId);

	/**
	 * 初始化客服已处理的订单数量
	 * @param servicerId
	 */
	void initOrderProcessedCount(Long servicerId);

	/**
	 * 根据用户ID获取分数
	 * @param userId
	 * @return
	 */
	double getScoreByUserId(Long userId);

	/**
	 * 获取客服已处理的订单数量
	 * @param servicerId
	 * @return
	 */
	int getOrderProcessedCount(Long servicerId);
}

/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ITradePlaceDBDAO
 *	包	名：		com.wzitech.gamegold.order.dao
 *	项目名称：	gamegold-order
 *	作	者：		Wengwei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-2-21 下午1:23:34
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;

/**
 * 订单配置结果数据访问
 * @author ztjie
 *
 */
public interface IConfigResultInfoDBDAO extends IMyBatisBaseDAO<ConfigResultInfoEO,Long>{

	/**
	 * 
	 * <p>通过订单号删除之前配置过的配置信息</p> 
	 * @author Think
	 * @date 2014-3-1 下午2:07:33
	 * @param orderId
	 * @see
	 */
	void deleteByOrderId(String orderId);

	/**
	 * 
	 * <p>通过订单号查询订单库存配置信息</p> 
	 * @author Think
	 * @date 2014-3-1 下午2:39:26
	 * @param orderId
	 * @see
	 */
	List<ConfigResultInfoEO> selectByOrderId(String orderId);

	/**
	 * 
	 * <p>通过订单ID，更新订单配置信息状态</p> 
	 * @author Think
	 * @date 2014-3-16 下午5:33:56
	 * @param orderId
	 * @param state
	 * @see
	 */
	void updateStateByOrderId(String orderId, int state);

	/**
	 * 
	 * <p>通过ID，删除配置信息</p> 
	 * @author Think
	 * @date 2014-3-16 下午9:58:52
	 * @param configId
	 * @see
	 */
	void deleteConfigById(Long configId);

	/**
	 * 
	 * <p>通过配置信息ID,更新配置信息状态</p> 
	 * @author Think
	 * @date 2014-3-16 下午10:44:08
	 * @param configId
	 * @param state
	 * @see
	 */
	void updateStateById(Long configId, int state);
	/**
	 * 通过卖家UId查询订单列表
	 * @param accountUId
	 * @return
	 */
	public GenericPage<ConfigResultInfoEO> selectSellerOrder(Map<String, Object> queryParam, String orderBy, boolean isAsc, int pageSize, int start);

	/**
	 * 通过卖家UId查询订单列表(优化)
	 * @param accountUId
	 * @return
	 */
	GenericPage<ConfigResultInfoEO> selectSellerOrderForOptimization(Map<String, Object> queryParam, String orderBy, boolean isAsc, int pageSize, int start);

	/**
	 * 通过卖家查询订单列表(导出订单)
	 * @param accountUId
	 * @return
	 */
	public List<ConfigResultInfoEO> exprotSellerOrder(Map<String, Object> queryParam, String orderBy, boolean isAsc);

    public ConfigResultInfoEO selectByIdForUpdate(Long id);

	/**
	 * 查询客服当前待发货订单数量和最后分配的时间
	 * @param queryParams
	 * @return
	 */
	Map<String, Object> selectCurrWaitDeliveryData(Map<String, Object> queryParams);

	/**
	 * 子订单移交
	 * @param orderId 主订单号
	 * @param id 子订单号
	 * @param account 卖家账号
	 * @param uid 卖家ID
	 */
	void transfer(String orderId, Long id, String account, String uid);

	/**
	 * 从寄售全自动机器人退回，安排寄售物服
	 * @param params
	 */
	int sendbackFromJsRobot(Map<String, Object> params);

	/**
	 * 查询指定的卖家游戏账号下等待寄售物服发货的订单笔数
	 * @param repositoryId 库存ID
	 * @return
	 */
	long selectCountByGameAccount(Long repositoryId);

	Integer selectConfigResultWithGameRole(String gameName,String region,String server,String gameRace,String gameRole,String loginAccount,String gameAccount);
}

/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradePlaceDBDAOImpl
 *	包	名：		com.wzitech.gamegold.order.dao.impl
 *	项目名称：	gamegold-order
 *	作	者：		Wengwei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-2-21 下午1:28:08
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.gamegold.common.enums.RefundReason;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;

/**
 * @author ztjie
 *
 */
@Repository
public class ConfigResultInfoDBDAOImpl extends AbstractMyBatisDAO<ConfigResultInfoEO, Long> implements IConfigResultInfoDBDAO{

	@Override
	public void deleteByOrderId(String orderId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDeleted", true);
		paramMap.put("orderId", orderId);
		this.getSqlSession().update(getMapperNamespace()+".delete", paramMap);
	}

	@Override
	public Integer selectConfigResultWithGameRole(String gameName, String region, String server, String gameRace, String gameRole,String loginAccount,String gameAccount) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("gameName",gameName);
		paramMap.put("region",region);
		paramMap.put("server",server);
		paramMap.put("gameRace",gameRace);
		paramMap.put("gameRole",gameRole);
		paramMap.put("loginAccount",loginAccount);
		paramMap.put("gameAccount",gameAccount);
		return (Integer)this.getSqlSession().selectOne(getMapperNamespace()+".selectByGameRole",paramMap);
	}

	@Override
	public void deleteConfigById(Long configId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDeleted", true);
		paramMap.put("configId", configId);
		this.getSqlSession().update(getMapperNamespace()+".delete", paramMap);
	}

	@Override
	public List<ConfigResultInfoEO> selectByOrderId(String orderId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDeleted", false);
		paramMap.put("orderId", orderId);
		return this.selectByMap(paramMap, "ID", false);
	}

	@Override
	public void updateStateByOrderId(String orderId, int state) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("state", state);
		paramMap.put("isDeleted", false);
		paramMap.put("orderId", orderId);
		this.getSqlSession().update(getMapperNamespace() + ".updateState", paramMap);
	}

	@Override
	public void updateStateById(Long configId, int state) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("state", state);
		paramMap.put("isDeleted", false);
		paramMap.put("configId", configId);
		this.getSqlSession().update(getMapperNamespace()+".updateState", paramMap);
	}

	@Override
	public GenericPage<ConfigResultInfoEO> selectSellerOrder(Map<String, Object> queryParam, String orderBy, boolean isAsc, int pageSize, int start) {
		if (queryParam == null) {
			queryParam = new HashMap<String, Object>();
		}
		queryParam.put("isDeleted", new Boolean(false));
		
		if(orderBy!=null){
			queryParam.put("ORDERBY", orderBy);
		}
		
		//检查分页参数
		if(pageSize < 1){
			throw new IllegalArgumentException("分页pageSize参数必须大于1");
		}
		
		if(start < 0){
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		}
		
		if(isAsc){
			queryParam.put("ORDER", "ASC");
		}else{
			queryParam.put("ORDER", "DESC");
		}
		
		int totalSize = (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".countBySeller", queryParam);
		
		// 如果数据Count为0，则直接返回
		if(totalSize == 0){
			return new GenericPage<ConfigResultInfoEO>(start, totalSize, pageSize, null);
		}
		List<ConfigResultInfoEO> pagedData =  this.getSqlSession().selectList(this.mapperNamespace + ".selectSellerMap", 
				queryParam, new RowBounds(start, pageSize));

		return new GenericPage<ConfigResultInfoEO>(start, totalSize, pageSize, pagedData);
	}

	/**
	 * 通过卖家UId查询订单列表(优化)
	 * @param
	 * @return
	 */
	@Override
	public GenericPage<ConfigResultInfoEO> selectSellerOrderForOptimization(Map<String, Object> queryParam, String orderBy, boolean isAsc, int pageSize, int start) {
		if (queryParam == null) {
			queryParam = new HashMap<String, Object>();
		}
		queryParam.put("isDeleted", new Boolean(false));

		if(orderBy!=null){
			queryParam.put("ORDERBY", orderBy);
		}

		//检查分页参数
		if(pageSize < 1){
			throw new IllegalArgumentException("分页pageSize参数必须大于1");
		}

		if(start < 0){
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		}

		if(isAsc){
			queryParam.put("ORDER", "ASC");
		}else{
			queryParam.put("ORDER", "DESC");
		}

		int totalSize = (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".countBySellerForOptimization", queryParam);

		// 如果数据Count为0，则直接返回
		if(totalSize == 0){
			return new GenericPage<ConfigResultInfoEO>(start, totalSize, pageSize, null);
		}
		List<ConfigResultInfoEO> pagedData =  this.getSqlSession().selectList(this.mapperNamespace + ".selectSellerMap",
				queryParam, new RowBounds(start, pageSize));

		return new GenericPage<ConfigResultInfoEO>(start, totalSize, pageSize, pagedData);
	}

	/**
	 * 通过卖家查询订单列表(导出订单)
	 * @return
	 */
	@Override
	public List<ConfigResultInfoEO> exprotSellerOrder(
			Map<String, Object> queryParam, String orderBy, boolean isAsc) {
		if (queryParam == null) {
			queryParam = new HashMap<String, Object>();
		}
		queryParam.put("isDeleted", new Boolean(false));
		
		if(orderBy!=null){
			queryParam.put("ORDERBY", orderBy);
		}
		if(isAsc){
			queryParam.put("ORDER", "ASC");
		}else{
			queryParam.put("ORDER", "DESC");
		}
		List<ConfigResultInfoEO> pagedData =  this.getSqlSession().selectList(this.mapperNamespace + ".selectSellerMap", 
				queryParam);
		return pagedData;
	}


    public ConfigResultInfoEO selectByIdForUpdate(Long id) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".selectByIdForUpdate", id);
    }

	/**
	 * 查询客服当前待发货订单数量和最后分配的时间
	 * @param queryParams
	 * @return
	 */
	public Map<String, Object> selectCurrWaitDeliveryData(Map<String, Object> queryParams) {
		return this.getSqlSession().selectOne(this.mapperNamespace + ".selectCurrWaitDeliveryData", queryParams);
	}

	/**
	 * 子订单移交
	 *
	 * @param orderId 主订单号
	 * @param id      子订单号
	 * @param account 卖家账号
	 * @param uid     卖家ID
	 */
	@Override
	public void transfer(String orderId, Long id, String account, String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("id", id);
		params.put("uid", uid);
		params.put("account", account);
		this.getSqlSession().update(this.mapperNamespace + ".transfer", params);
	}

	/**
	 * 从寄售全自动机器人退回，安排寄售物服
	 * @param params
	 */
	public int sendbackFromJsRobot(Map<String, Object> params) {
		return this.getSqlSession().update(this.mapperNamespace + ".sendbackFromJsRobot", params);
	}

	/**
	 * 查询指定的卖家游戏账号下等待寄售物服发货的订单笔数
	 *
	 * @param repositoryId 库存ID
	 * @return
	 */
	@Override
	public long selectCountByGameAccount(Long repositoryId) {
		return getSqlSession().selectOne(this.getMapperNamespace() + ".selectCountByGameAccount", repositoryId);
	}
}

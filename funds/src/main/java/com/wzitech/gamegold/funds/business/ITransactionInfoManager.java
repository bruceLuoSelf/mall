/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderInfoManager
 *	包	名：		com.wzitech.gamegold.order.business
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-17
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-17 下午3:32:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.funds.business;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.funds.entity.TransactionInfo;

/**
 * 交易流水管理接口
 * @author ztjie
 *
 */
public interface ITransactionInfoManager {

	/**
	 * 根据条件分页查询交易流水
	 * @param queryMap
	 * @param orderBy
	 * @param isAsc
	 * @param pageSize
	 * @param start
	 * @return
	 * @throws SystemException
	 */
	GenericPage<TransactionInfo> queryTransactionList(Map<String, Object> queryMap, String orderBy, boolean isAsc, int pageSize, int start) throws SystemException;

	/**
	 * 
	 * <p>根据条件查询交易流水,用于导出的数据</p> 
	 * @author Think
	 * @date 2014-2-25 下午7:52:44
	 * @param queryMap
	 * @return
	 * @see
	 */
	List<TransactionInfo> queryTransactionList(Map<String, Object> queryMap, String orderBy, boolean isAsc);
	
}

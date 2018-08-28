/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IDiscountInfoManager
 *	包	名：		com.wzitech.gamegold.goods.business
 *	项目名称：	gamegold-goods
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午3:15:22
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.business;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.DiscountInfo;

/**
 * 商品对应折扣管理接口
 * @author HeJian
 *
 */
public interface IDiscountInfoManager {
	/**
	 * 添加折扣
	 * @param DiscountInfo
	 * @return
	 * @throws SystemException;
	 */
	DiscountInfo addDiscount(DiscountInfo discountInfo)throws SystemException;
	
	/**
	 * 修改折扣
	 * @param discountInfo
	 * @return
	 * @throws SystemException
	 */
	DiscountInfo modifyDiscount(DiscountInfo discountInfo) throws SystemException;
	
	/**
	 * 删除折扣
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	DiscountInfo deleteDiscount(Long id) throws SystemException;
	
	/**
	 * 根据条件分页查询商品对应折扣
	 * @param queryMap
	 * @param pageSize
	 * @param pubSize
	 * @param sortBy
	 * @param isAsc
	 * @return
	 * @throws SystemException
	 */
	GenericPage<DiscountInfo> queryDiscount(Map<String, Object> queryMap, int pageSize, int pubSize,
			String sortBy, boolean isAsc) throws SystemException;

	/**
	 * 根据条件查询该商品对应所有折扣
	 * @param goodsId
	 * @return
	 * @throws SystemException
	 */
	List<DiscountInfo> queryDiscountInfos(Long goodsId) throws SystemException;

	/**
	 * 
	 * <p>通过商品ID删除商品的折扣信息</p> 
	 * @author ztjie
	 * @date 2014-2-23 下午9:53:39
	 * @param id
	 * @see
	 */
	void deleteByGoodsId(Long id);
}

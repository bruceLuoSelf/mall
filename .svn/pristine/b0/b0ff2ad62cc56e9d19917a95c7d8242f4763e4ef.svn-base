/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountInfoManagerImpl
 *	包	名：		com.wzitech.gamegold.goods.business.impl
 *	项目名称：	gamegold-goods
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午3:19:40
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IDiscountInfoManager;
import com.wzitech.gamegold.goods.dao.IDiscountInfoDBDAO;
import com.wzitech.gamegold.goods.entity.DiscountInfo;

/**
 * 商品对应折扣管理接口实现
 * @author HeJian
 *
 */
@Component
public class DiscountInfoManagerImpl extends AbstractBusinessObject implements IDiscountInfoManager{
	@Autowired
	IDiscountInfoDBDAO discountInfoDBDAO;
	
	@Override
	@Transactional
	public DiscountInfo addDiscount(DiscountInfo discountInfo)
			throws SystemException {
		if (discountInfo == null){
			throw new SystemException(
					ResponseCodes.EmptyDiscountInfo.getCode(), ResponseCodes.EmptyDiscountInfo.getMessage());
		}
		if (discountInfo.getGoldCount() == null){
			throw new SystemException(
					ResponseCodes.EmptyGoodsCount.getCode(), ResponseCodes.EmptyGoodsCount.getMessage());
		}
		if (discountInfo.getDiscount() == null){
			throw new SystemException(
					ResponseCodes.EmptyDiscount.getCode(), ResponseCodes.EmptyDiscount.getMessage());
		}
		if (discountInfo.getGoodsId() == null){
			throw new SystemException(
					ResponseCodes.EmptyGoodsId.getCode(), ResponseCodes.EmptyGoodsId.getMessage());
		}
		discountInfo.setIsDeleted(false);
		discountInfoDBDAO.insert(discountInfo);
		return discountInfo;
	}

	@Override
	@Transactional
	public DiscountInfo modifyDiscount(DiscountInfo discountInfo)
			throws SystemException {
		if (discountInfo.getId() == null){
			throw new SystemException(
					ResponseCodes.EmptyDiscountId.getCode(), ResponseCodes.EmptyDiscountId.getMessage());
		}
		discountInfo.setIsDeleted(false);
		discountInfoDBDAO.update(discountInfo);
		return discountInfo;
	}

	@Override
	@Transactional
	public DiscountInfo deleteDiscount(Long id) throws SystemException {
		if (id==null) {
			throw new SystemException(
					ResponseCodes.EmptyDiscountId.getCode(),ResponseCodes.EmptyDiscountId.getMessage());
		}
		DiscountInfo discountInfo = new DiscountInfo();
		discountInfo.setId(id);
		discountInfo.setIsDeleted(true);
		return discountInfo;
	}

	@Override
	public GenericPage<DiscountInfo> queryDiscount(
			Map<String, Object> queryMap, int pageSize, int pubSize,
			String sortBy, boolean isAsc) throws SystemException {
		queryMap.put("isDeleted", false);
		return discountInfoDBDAO.selectByMap(queryMap, pageSize,(pubSize-1)*pageSize, sortBy, isAsc);
	}

	@Override
	public List<DiscountInfo> queryDiscountInfos(Long goodsId) throws SystemException {
		if(goodsId == null) {
			throw new SystemException(
					ResponseCodes.EmptyGoodsId.getCode(),ResponseCodes.EmptyGoodsId.getMessage());
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("goodsId", goodsId);
		queryMap.put("isDeleted", false);
		List<DiscountInfo> discountInfos = discountInfoDBDAO.selectByMap(queryMap, "GOLD_COUNT", true);
		return discountInfos;
	}

	@Override
	@Transactional
	public void deleteByGoodsId(Long goodsId) {
		if(goodsId == null) {
			throw new SystemException(
					ResponseCodes.EmptyGoodsId.getCode(),ResponseCodes.EmptyGoodsId.getMessage());
		}
		discountInfoDBDAO.deleteByGoodsId(goodsId);
	}
}

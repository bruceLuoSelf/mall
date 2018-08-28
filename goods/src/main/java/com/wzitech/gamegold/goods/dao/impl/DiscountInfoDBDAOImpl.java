/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.goods.dao.impl
 *	项目名称：	gamegold-goods
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午3:13:27
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IDiscountInfoDBDAO;
import com.wzitech.gamegold.goods.entity.DiscountInfo;

/**
 * @author HeJian
 *
 */
@Repository
public class DiscountInfoDBDAOImpl extends AbstractMyBatisDAO<DiscountInfo, Long> implements IDiscountInfoDBDAO{

	@Override
	public void deleteByGoodsId(Long goodsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsId", goodsId);
		map.put("isDeleted", true);
		this.getSqlSession().update(getMapperNamespace() + ".deleteByGoodsId", map);
	}

}

/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GoodsInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.goodsmgmt.dao.impl
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:42:01
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IGoodsInfoDBDAO;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

/**
 * @author SunChengfei
 *
 */
@Repository
public class GoodsInfoDBDAOImpl extends AbstractMyBatisDAO<GoodsInfo, Long> implements IGoodsInfoDBDAO{

	@Override
	public boolean checkGoodsCatExist(Map<String, Object> map) {
		Long count = this.getSqlSession().selectOne(this.getMapperNamespace() + ".checkGoodsCatExist", map);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void updatePrice(Map<String, Object> paramMap) {
		this.getSqlSession().update(this.getMapperNamespace()+".updatePrice", paramMap);
	}

}

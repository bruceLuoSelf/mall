/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IDiscountInfoDBDAO
 *	包	名：		com.wzitech.gamegold.goods.dao
 *	项目名称：	gamegold-goods
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午3:12:11
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.goods.entity.DiscountInfo;

/**
 * 商品对应折扣DB
 * @author HeJian
 *
 */
public interface IDiscountInfoDBDAO extends IMyBatisBaseDAO<DiscountInfo, Long>{

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

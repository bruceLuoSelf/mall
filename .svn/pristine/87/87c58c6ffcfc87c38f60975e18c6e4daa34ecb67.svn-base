/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGoodsInfoDBDAO
 *	包	名：		com.wzitech.gamegold.goodsmgmt.dao
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:40:09
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao;

import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

/**
 * 商品DB
 * @author SunChengfei
 *
 */
public interface IGoodsInfoDBDAO extends IMyBatisBaseDAO<GoodsInfo, Long>{

	/**
	 * 
	 * <p>判断商品的类目是否已经存在</p> 
	 * @author Think
	 * @date 2014-2-22 上午10:06:17
	 * @param map
	 * @return
	 * @see
	 */
	public boolean checkGoodsCatExist(Map<String, Object> map);

	/**
	 * 
	 * <p>更新商品的价格</p> 
	 * @author ztjie
	 * @date 2014-3-27 下午8:06:57
	 * @param paramMap
	 * @see
	 */
	public void updatePrice(Map<String, Object> paramMap);

}

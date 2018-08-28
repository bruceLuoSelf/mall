/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGoodsInfoRedisDAO
 *	包	名：		com.wzitech.gamegold.goods.dao
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午6:54:57
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao;

import com.wzitech.gamegold.goods.entity.GoodsInfo;

/**
 * @author SunChengfei
 **         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */
public interface IGoodsInfoRedisDAO {
	/**
	 * 保存商品信息
	 * @param goodsInfo
	 */
	public void saveGoodsInfo(GoodsInfo goodsInfo);
	
	/**
	 * 删除商品信息
	 * @param goodsInfo
	 */
	public void deleteGoods(GoodsInfo goodsInfo);
	
	/**
	 * 条件查询指定栏目商品
	 * @param gameName
	 * @param region
	 * @param server
	 * @param gameRace
	 * @goodsCat 商品栏目
	 * @return
	 */
	public GoodsInfo getMaxSaleGoods(String gameName,String goodsTypeName, String region,
			String server, String gameRace, int goodsCat);/**ZW_C_JB_00008_20170512 add**/
}

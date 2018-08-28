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

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.GameConfigEO;

import java.util.List;

/**
 * 游戏交易地点DB
 * @author Wengwei
 *
 */
public interface IGameConfigDBDAO extends IMyBatisBaseDAO<GameConfigEO,Long>{
    /**
     * 获取商城的所有游戏名称和ID
     * @return
     */
    List<GameConfigEO> queryGameNameIdList();
}

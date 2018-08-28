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

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.IGameConfigDBDAO;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wengwei
 *
 */
@Repository
public class GameConfigDBDAOImpl extends AbstractMyBatisDAO<GameConfigEO, Long> implements IGameConfigDBDAO{

    /**
     * 获取商城的所有游戏名称和ID
     *
     * @return
     */
    @Override
    public List<GameConfigEO> queryGameNameIdList() {
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".queryGameNameIdList");
    }
}

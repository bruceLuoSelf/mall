/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *
 *	模	块：		ITradePlaceEOManager
 *	包	名：		com.wzitech.gamegold.goodsmgmt.business
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:48:26
 *
 ************************************************************************************/
package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.GameConfigEO;

import java.util.List;
import java.util.Map;

/**
 * 游戏配置管理接口
 *
 * @author SunChengfei
 */
public interface IGameConfigManager {

    /**
     * 获取商城的所有游戏名称和ID
     *
     * @return
     */
    List<GameConfigEO> queryGameNameIdList();

    /**
     * 添加游戏配置
     *
     * @param TradePlaceEO
     * @return
     * @throws BusinessException
     */
    GameConfigEO addGameConfig(GameConfigEO TradePlaceEO) throws SystemException;

    /**
     * 修改游戏配置
     *
     * @param TradePlaceEO
     * @return
     * @throws BusinessException
     */
    GameConfigEO modifyGameConfig(GameConfigEO TradePlaceEO) throws SystemException;

    /**
     * 删除游戏配置
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    void deleteGameConfig(Long id) throws SystemException;

    /**
     * 根据条件分页查询游戏配置
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws BusinessException
     */
    GenericPage<GameConfigEO> queryGameConfig(Map<String, Object> queryMap, int limit, int start,
                                              String sortBy, boolean isAsc);

    /**
     * 根据游戏名称显示配置
     *
     * @param gameName
     * @return
     * @throws SystemException
     */
    GameConfigEO selectGameConfig(String gameName,String goodsTypeName);

    GameConfigEO selectById(Long id);

}

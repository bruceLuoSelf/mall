/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *
 *	模	块：		GoodsInfoManagerImpl
 *	包	名：		com.wzitech.gamegold.goodsmgmt.business.impl
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:52:17
 *
 ************************************************************************************/
package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.dao.IGameConfigDBDAO;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏交易地管理接口实现
 * @author SunChengfei
 *Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/24  wubiao           ZW_C_JB_00008 商城增加通货
 */
@Component
public class GameConfigManagerImpl extends AbstractBusinessObject implements IGameConfigManager{

	@Autowired
	IGameConfigDBDAO tradePlaceDBDAO;

    /**
     * 获取商城的所有游戏名称和ID
     *
     * @return
     */
    @Override
    public List<GameConfigEO> queryGameNameIdList() {
        return tradePlaceDBDAO.queryGameNameIdList();
    }

    @Override
	@Transactional
	public GameConfigEO addGameConfig(GameConfigEO tradePlace) throws SystemException {
		if (tradePlace == null) {
			throw new SystemException(
					ResponseCodes.EmptyGoodsInfo.getCode(),ResponseCodes.EmptyGoodsInfo.getMessage());
		}
		if (StringUtils.isEmpty(tradePlace.getGameName())) {
			throw new SystemException(
					ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getCode());
		}
		if (StringUtils.isEmpty(tradePlace.getPlaceName())) {
			throw new SystemException(
					ResponseCodes.EmptyPlaceName.getCode(),ResponseCodes.EmptyPlaceName.getCode());
		}
		/**************ZW_C_JB_00008_20170524 ADD 商城增加通货 START**************************/
		if (StringUtils.isEmpty(tradePlace.getGoodsTypeName())) {
			throw new SystemException(
					ResponseCodes.EmptyCategoryValue.getCode(),ResponseCodes.EmptyCategoryValue.getCode());
		}
		/**************ZW_C_JB_00008_20170524 ADD 商城增加通货 END**************************/
		if (tradePlace.getMailTime()==null) {
			throw new SystemException(
					ResponseCodes.EmptyMailTime.getCode(),ResponseCodes.EmptyMailTime.getCode());
		}
		if (tradePlace.getAutoPlayTime()==null) {
			throw new SystemException(
					ResponseCodes.EmptyAutoPlayTime.getCode(),ResponseCodes.EmptyAutoPlayTime.getCode());
		}
		tradePlace.setIsDeleted(false);
		tradePlace.setLastUpdateTime(new Date());
		tradePlace.setCreateTime(new Date());
		/**************ZW_C_JB_00008_20170526 ADD 商城增加通货 START**************************/
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("gameName", tradePlace.getGameName());
		queryMap.put("goodsTypeName", tradePlace.getGoodsTypeName());
		List<GameConfigEO> list = tradePlaceDBDAO.selectByMap(queryMap, "ID", true);
		if (list != null && list.size() > 0) {
			throw new SystemException(
					ResponseCodes.NoCategoryrepeat.getCode(), ResponseCodes.NoCategoryrepeat.getCode());
		}
		tradePlaceDBDAO.insert(tradePlace);
		/**************ZW_C_JB_00008_20170526 ADD 商城增加通货 END**************************/
		return tradePlace;
	}

	@Override
	@Transactional
	public GameConfigEO modifyGameConfig(GameConfigEO tradePlace) throws SystemException {
		if (tradePlace==null) {
			throw new SystemException(
					ResponseCodes.EmptyTradePlace.getCode(),ResponseCodes.EmptyTradePlace.getMessage());
		}
		if (tradePlace.getId()==null) {
			throw new SystemException(
					ResponseCodes.EmptyTradePlaceId.getCode(),ResponseCodes.EmptyTradePlaceId.getMessage());
		}
		GameConfigEO dbTradePlace = tradePlaceDBDAO.selectById((Long)(tradePlace.getId()));
		if (StringUtils.isEmpty(tradePlace.getGameName())) {
			throw new SystemException(
					ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getCode());
		}
		dbTradePlace.setGameName(tradePlace.getGameName());
		/**************ZW_C_JB_00008_20170524 ADD 商城增加通货 START**************************/
		if (StringUtils.isEmpty(tradePlace.getGoodsTypeName())) {
			//之前旧数据没有商品类型,为空的情况下默认为'游戏币'
			dbTradePlace.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
		}
		/**************ZW_C_JB_00008_20170524 ADD 商城增加通货 START**************************/
		if (StringUtils.isEmpty(tradePlace.getPlaceName())) {
			throw new SystemException(
					ResponseCodes.EmptyPlaceName.getCode(),ResponseCodes.EmptyPlaceName.getCode());
		}
		dbTradePlace.setPlaceName(tradePlace.getPlaceName());
		if (tradePlace.getMailTime()==null) {
			throw new SystemException(
					ResponseCodes.EmptyMailTime.getCode(),ResponseCodes.EmptyMailTime.getCode());
		}
		dbTradePlace.setMailTime(tradePlace.getMailTime());
		if (tradePlace.getAutoPlayTime()==null) {
			throw new SystemException(
					ResponseCodes.EmptyAutoPlayTime.getCode(),ResponseCodes.EmptyAutoPlayTime.getCode());
		}
		dbTradePlace.setAutoPlayTime(tradePlace.getAutoPlayTime());
		if(StringUtils.isNotEmpty(tradePlace.getPlaceImage())){
			dbTradePlace.setPlaceImage(tradePlace.getPlaceImage());
		}
		if(StringUtils.isNotEmpty(tradePlace.getGameImage())){
			dbTradePlace.setGameImage(tradePlace.getGameImage());
		}
		dbTradePlace.setIsDeleted(false);
		dbTradePlace.setLastUpdateTime(new Date());
		dbTradePlace.setCommision(tradePlace.getCommision());
		tradePlaceDBDAO.update(dbTradePlace);
		return dbTradePlace;
	}

    @Override
    @Transactional
    public void deleteGameConfig(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(
                    ResponseCodes.EmptyTradePlaceId.getCode(), ResponseCodes.EmptyTradePlaceId.getMessage());
        }
        GameConfigEO tradePlace = new GameConfigEO();
        tradePlace.setId(id);
        tradePlace.setLastUpdateTime(new Date());
        tradePlace.setIsDeleted(true);
        tradePlaceDBDAO.update(tradePlace);
    }

    @Override
    public GenericPage<GameConfigEO> queryGameConfig(Map<String, Object> queryMap, int limit, int start,
                                                     String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
//        queryMap.put("isDeleted", false);
        return tradePlaceDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    @Override
    public GameConfigEO selectGameConfig(String gameName, String goodsTypeName) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("goodsTypeName", goodsTypeName);
        List<GameConfigEO> list = tradePlaceDBDAO.selectByMap(queryMap, "ID", true);
        if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
        }
		return null;
    }

	@Override
	public GameConfigEO selectById(Long id) {
		return tradePlaceDBDAO.selectById(id);
	}
}

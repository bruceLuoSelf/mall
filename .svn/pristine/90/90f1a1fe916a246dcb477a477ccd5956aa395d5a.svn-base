/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderConfigQueryImpl
 *	包	名：		com.wzitech.gamegold.order.business.impl
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-25 下午7:32:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.gamegold.common.constants.ServicesContants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;

/**
 * @author SunChengfei
 *
 */
@Component
public class OrderConfigManagerImpl extends AbstractBusinessObject implements IOrderConfigManager {
	@Autowired
	IConfigResultInfoDBDAO configResultInfoDBDAO;
	
	@Autowired
	IRepositoryManager repositoryManager;
	
	/**
	 * 根据订单号查询配置库存
	 * @param orderId
	 * @return 返回库存信息，及关联的订单信息
	 */
	@Override
	public List<ConfigResultInfoEO> orderConfigList(String orderId) {
		if (orderId == null) {
			throw new SystemException(
					ResponseCodes.EmptyOrderId.getCode(),ResponseCodes.EmptyOrderId.getMessage());
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderId", orderId);
		paramMap.put("isDeleted", false);
        paramMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
		List<ConfigResultInfoEO> list = configResultInfoDBDAO.selectByMap(paramMap,"ID",true);
		return list;
	}

    /**
     * 根据订单号查询配置库存
     * @param orderId
     * @return 返回库存信息，及关联的订单信息
     */
    public List<ConfigResultInfoEO> orderConfigList(String orderId, boolean lockMode) {
        if (orderId == null) {
            throw new SystemException(
                    ResponseCodes.EmptyOrderId.getCode(),ResponseCodes.EmptyOrderId.getMessage());
        }
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("orderId", orderId);
        paramMap.put("isDeleted", false);
        paramMap.put("lockMode", lockMode);
        paramMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<ConfigResultInfoEO> list = configResultInfoDBDAO.selectByMap(paramMap,"ID",true);
        return list;
    }

    @Override
    @Transactional
    public void updateConfigState(Long configId, Integer state) {
        if (configId == null) {
            throw new SystemException(
                    ResponseCodes.EmptyConfigResultId.getCode(),
                    ResponseCodes.EmptyConfigResultId.getMessage());
        }
        if (state == null) {
            throw new SystemException(
                    ResponseCodes.EmptyConfigState.getCode(),
                    ResponseCodes.EmptyConfigState.getMessage());
        }

        //如果子订单取消，那么就要把取消的配置库存数加回到之前的库存中
        if (OrderState.Cancelled.getCode() == state.intValue()) {
            //ConfigResultInfoEO config = this.orderConfigById(configId);
            ConfigResultInfoEO config = configResultInfoDBDAO.selectByIdForUpdate(configId);

            // 如果已经发货的不能取消
            if (OrderState.Delivery.getCode() == config.getState().intValue()) {
                throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
            }

            //判断查询出来的子订单是否已经取消，只有没有取消的订单才把配置的库存加回到之前的库存
            if (config.getState() != OrderState.Cancelled.getCode()) {
                RepositoryInfo repositoryInfo = config.getRepositoryInfo();
                if (repositoryInfo != null) {
                    /*repositoryInfo.setGoldCount(repositoryInfo.getGoldCount() + config.getConfigGoldCount());
                    repositoryInfo.setSellableCount(repositoryInfo.getSellableCount() + config.getConfigGoldCount());
                    repositoryManager.modifyRepository(repositoryInfo);*/
                    repositoryManager.incrRepositoryCount(repositoryInfo, config.getConfigGoldCount().longValue(), config.getOrderId());
                }
            }
        }
        configResultInfoDBDAO.updateStateById(Long.valueOf(configId), state);
    }

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.order.business.IOrderConfigManager#orderConfigById(java.lang.Long)
	 */
	@Override
	public ConfigResultInfoEO orderConfigById(Long configId) {
		return configResultInfoDBDAO.selectById(configId);
	}

	@Override
	public void deleteConfig(Long id) {
		configResultInfoDBDAO.deleteConfigById(id);		
	}
}

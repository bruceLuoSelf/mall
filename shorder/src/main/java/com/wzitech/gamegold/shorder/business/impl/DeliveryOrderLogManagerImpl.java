package com.wzitech.gamegold.shorder.business.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderLogDao;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货单日志管理
 *
 * @author yemq
 */
@Component
public class DeliveryOrderLogManagerImpl implements IDeliveryOrderLogManager {
    @Autowired
    private IDeliveryOrderLogDao orderLogDao;
    @Autowired
    private ISystemConfigManager systemConfigManager;

    /**
     * 写日志
     *
     * @param orderLog
     */
    @Override
    @Transactional
    public void writeLog(OrderLog orderLog) {
        if (orderLog == null)
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        if (orderLog.getOrderId() == null)
            throw new SystemException(ResponseCodes.EmptyChId.getCode(), ResponseCodes.EmptyChId.getMessage());
        if (StringUtils.isBlank(orderLog.getLog()))
            throw new SystemException(ResponseCodes.EmptyLogOperateInfo.getCode(),
                    ResponseCodes.EmptyLogOperateInfo.getMessage());
        if (orderLog.getType() == null) {
            orderLog.setType(OrderLog.TYPE_NORMAL);
        }
        orderLog.setOperator(CurrentUserContext.getUserLoginAccount());
        orderLog.setCreateTime(new Date());
        orderLogDao.insert(orderLog);
    }

    /**
     * 根据出货单ID查询所有的日志
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLog> getByOrderId(String orderId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orderId", orderId);
        return orderLogDao.selectByMap(params, "id", true);
    }

    /**
     * 根据出货单ID查询所有的日志
     *
     * @param orderId
     * @param type
     * @return
     */
    public List<OrderLog> getByOrderId(String orderId, int type) {
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (StringUtils.isBlank(loginAccount)) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(),
                    ResponseCodes.InvalidAuthkey.getMessage());
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("orderId", orderId);
        params.put("operator", loginAccount);
        params.put("type", type);
        return orderLogDao.selectByMap(params, "id", true);
    }

    /**
     * 根据日志ID查询
     *
     * @param id
     * @return
     */
    @Override
    public OrderLog getById(Long id) {
        return orderLogDao.selectById(id);
    }

    @Override
    public GenericPage<OrderLog> queryByMap(Map<String, Object> map, int pageSize, int startIndex, String orderBy,
                                            Boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (startIndex < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        return orderLogDao.selectByMap(map, pageSize, startIndex, orderBy, isAsc);
    }

    @Override
    public List<OrderLog> queryAllByMap(Map<String, Object> map) {
        return orderLogDao.selectByMap(map);
    }

    /**
     * 是否存在邮寄自动化返回的日志信息
     *
     * @param subId
     * @return
     */
    @Override
    public boolean isHaveLog(Long subId) {
        int sellerDeliveryTimeOut = systemConfigManager.sellerDeliveryTimeOut();
        Map<String, Object> params = Maps.newHashMap();
        params.put("subId", subId);
        params.put("sellerDeliveryTimeOut", sellerDeliveryTimeOut + " min");
        return orderLogDao.selectSellerDeliveryTimeOut(params);
    }
}

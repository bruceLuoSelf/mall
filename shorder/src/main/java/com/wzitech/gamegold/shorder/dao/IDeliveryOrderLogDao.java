package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.OrderLog;

import java.util.Map;

/**
 * 出货单日志DAO
 */
public interface IDeliveryOrderLogDao extends IMyBatisBaseDAO<OrderLog, Long> {

    boolean selectSellerDeliveryTimeOut(Map<String,Object> selectMap);
}

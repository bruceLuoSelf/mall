package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;

/**
 * 订单日志接口
 * @author yemq
 */
public interface IOrderLogDao extends IMyBatisBaseDAO<OrderLogInfo, Long> {
}

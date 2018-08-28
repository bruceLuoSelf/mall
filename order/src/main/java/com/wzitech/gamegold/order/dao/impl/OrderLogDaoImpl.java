package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.dao.IOrderLogDao;
import org.springframework.stereotype.Repository;

/**
 * 订单日志实现类
 * @author yemq
 */
@Repository
public class OrderLogDaoImpl extends AbstractMyBatisDAO<OrderLogInfo, Long> implements IOrderLogDao {
}

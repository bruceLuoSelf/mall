package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IOrderUserLogDao;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.stereotype.Repository;

/**sunyang
 * Created by Administrator on 2017/2/17.
 */
@Repository
public class OrderUserLogDaoImpl extends AbstractMyBatisDAO<OrderLog,Long> implements IOrderUserLogDao {
}

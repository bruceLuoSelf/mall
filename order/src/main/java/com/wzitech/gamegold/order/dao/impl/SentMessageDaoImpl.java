package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.ISentMessageDao;
import com.wzitech.gamegold.order.entity.SentMessage;
import org.springframework.stereotype.Repository;

/**
 * 已发送的短信dao
 *
 * @author yemq
 */
@Repository
public class SentMessageDaoImpl extends AbstractMyBatisDAO<SentMessage, Long> implements ISentMessageDao {
}

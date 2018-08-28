package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import com.wzitech.gamegold.repository.dao.IRepositoryLogDao;
import org.springframework.stereotype.Repository;

/**
 * 库存日志dao实现类
 *
 * @author yemq
 */
@Repository
public class RepositoryLogDaoImpl extends AbstractMyBatisDAO<RepositoryLogInfo, Long> implements IRepositoryLogDao {
}

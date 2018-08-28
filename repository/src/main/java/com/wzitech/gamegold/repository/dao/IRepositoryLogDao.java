package com.wzitech.gamegold.repository.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import org.springframework.stereotype.Repository;

/**
 * 库存日志dao
 *
 * @author yemq
 */
@Repository
public interface IRepositoryLogDao extends IMyBatisBaseDAO<RepositoryLogInfo, Long> {
}

package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.usermgmt.dao.rdb.IServicerVoteConfigDBDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteConfigEO;
import org.springframework.stereotype.Repository;

/**
 * 客服投票配置dao
 * @author yemq
 */
@Repository
public class ServicerVoteConfigDBDAO extends AbstractMyBatisDAO<ServicerVoteConfigEO, Long>  implements IServicerVoteConfigDBDAO {
}

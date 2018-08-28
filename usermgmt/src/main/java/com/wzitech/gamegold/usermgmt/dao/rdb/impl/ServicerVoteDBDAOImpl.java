package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.usermgmt.dao.rdb.IServicerVoteDBDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteRecordEO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 客服投票DAO
 *
 * @author yemq
 */
@Repository
public class ServicerVoteDBDAOImpl extends AbstractMyBatisDAO<ServicerVoteRecordEO, Long> implements IServicerVoteDBDAO {

    /**
     * 查询是否已经投票
     *
     * @param operatorId
     * @param ip
     * @return boolean
     */
    public boolean hasVoted(String operatorId, String ip) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("operatorId", operatorId);
        queryMap.put("ip", ip);
        int count = getSqlSession().selectOne(getMapperNamespace() + ".hasVoted", queryMap);
        return (count > 0) ? true : false;
    }

    /**
     * 增加客服投票数
     *
     * @param uid 客服ID
     * @return 返回投票数
     */
    @Override
    public void incrVote(Long uid) {
        this.getSqlSession().update(this.mapperNamespace + ".incrVote", uid);
    }

    /**
     * 查询客服投票数
     *
     * @param uid 客服ID
     * @return 投票数
     */
    @Override
    public int queryServicerVote(Long uid) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".queryServicerVote", uid);
    }
}

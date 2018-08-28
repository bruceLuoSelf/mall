package com.wzitech.gamegold.usermgmt.dao.rdb;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteRecordEO;

/**
 * 客服投票DAO
 * @author yemq
 */
public interface IServicerVoteDBDAO extends IMyBatisBaseDAO<ServicerVoteRecordEO, Long> {

    /**
     * 查询是否已经投票
     *
     * @param operatorId
     * @param ip
     * @return boolean
     */
    public boolean hasVoted(String operatorId, String ip);

    /**
     * 增加客服投票数
     * @param uid 客服ID
     * @return 返回投票数
     */
    void incrVote(Long uid);

    /**
     * 查询客服投票数
     * @param uid 客服ID
     * @return 投票数
     */
    int queryServicerVote(Long uid);
}

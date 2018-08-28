package com.wzitech.gamegold.usermgmt.business;

/**
 * 对客服投票管理
 *
 * @author yemq
 */
public interface IServicerVoteManager {
    /**
     * 投票
     *
     * @param servicerId   客服ID
     * @param operatorId   投票者5173注册账号
     * @param operatorName 投票者5713注册账户名
     * @param ip           投票者IP地址
     * @return 客服投票数
     */
    public int incrVote(Long servicerId, String operatorId, String operatorName, String ip);

    /**
     * 查询是否已经投票
     *
     * @param operatorId
     * @param ip
     * @return boolean
     */
    boolean hasVoted(String operatorId, String ip);

    /**
     * 增加客服投票数
     *
     * @param uid 客服ID
     * @return 返回投票数
     */
    void incrVote(Long uid);

    /**
     * 查询客服投票数
     *
     * @param uid 客服ID
     * @return 投票数
     */
    int queryServicerVote(Long uid);
}

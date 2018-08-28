package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IServicerVoteConfigManager;
import com.wzitech.gamegold.usermgmt.business.IServicerVoteManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IServicerVoteDBDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteConfigEO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteRecordEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.cookie.DateParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 客服投票管理
 *
 * @author yemq
 */
@Component
public class ServicerVoteManagerImpl extends AbstractBusinessObject implements IServicerVoteManager {
    @Autowired
    private IServicerVoteDBDAO serviceVoteDBDAO;
    @Autowired
    private IServicerVoteConfigManager servicerVoteConfigManager;

    /**
     * 投票
     *
     * @param servicerId   客服ID
     * @param operatorId   投票者5173注册账号
     * @param operatorName 投票者5713注册账户名
     * @param ip           投票者IP地址
     * @return 客服投票数
     */
    @Override
    @Transactional
    public int incrVote(Long servicerId, String operatorId, String operatorName, String ip) {
        if (servicerId == null) {
            throw new SystemException(ResponseCodes.EmptyServicerId.getCode(), ResponseCodes.EmptyServicerId.getMessage());
        }

        if (StringUtils.isBlank(ip)) {
            throw new SystemException(ResponseCodes.EmptyIpAddress.getCode(), ResponseCodes.EmptyIpAddress.getMessage());
        }

        // 判断投票时间是否有效
        ServicerVoteConfigEO config = servicerVoteConfigManager.loadConfig();
        long now = new Date().getTime();
        if (now < config.getStartTime().getTime()) {
            // 投票还没有开始
            throw new SystemException(ResponseCodes.VotingHasNotStarted.getCode(), ResponseCodes.VotingHasNotStarted.getMessage());
        } else if (now > config.getEndTime().getTime()) {
            // 投票已经结束
            throw new SystemException(ResponseCodes.VotingHasEnded.getCode(), ResponseCodes.VotingHasEnded.getMessage());
        }

        // 判断该用户是否已经投过票
        if (hasVoted(operatorId, ip)) {
            throw new SystemException(ResponseCodes.VotedUserId.getCode(), ResponseCodes.VotedUserId.getMessage());
        }

        // 添加客服投票记录
        ServicerVoteRecordEO vote = new ServicerVoteRecordEO();
        vote.setServicerId(servicerId);
        vote.setOperatorId(operatorId);
        vote.setOperatorName(operatorName);
        vote.setIp(ip);
        vote.setCreateTime(new Date());
        serviceVoteDBDAO.insert(vote);

        // 增加客服投票数
        this.incrVote(servicerId);

        return queryServicerVote(servicerId);
    }

    /**
     * 查询是否已经投票
     *
     * @param operatorId
     * @param ip
     * @return boolean
     */
    public boolean hasVoted(String operatorId, String ip) {
        return serviceVoteDBDAO.hasVoted(operatorId, ip);
    }

    /**
     * 增加客服投票数
     *
     * @param uid 客服ID
     * @return 返回投票数
     */
    @Override
    @Transactional
    public void incrVote(Long uid) {
        serviceVoteDBDAO.incrVote(uid);
    }

    /**
     * 查询客服投票数
     *
     * @param uid 客服ID
     * @return 投票数
     */
    @Override
    @Transactional(readOnly = true)
    public int queryServicerVote(Long uid) {
        return serviceVoteDBDAO.queryServicerVote(uid);
    }


}

package com.wzitech.gamegold.facade.frontend.service.vote.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.List;

/**
 * 对客服投票响应
 * @author yemq
 */
public class ServicerVoteResponse extends AbstractServiceResponse {
    /**
     * 客服投票数
     */
    private int vote;

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}

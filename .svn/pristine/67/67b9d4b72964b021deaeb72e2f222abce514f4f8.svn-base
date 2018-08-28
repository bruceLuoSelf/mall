package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;

import java.util.List;

/**
 * Created by 339928 on 2018/6/19.
 */
public interface ISplitRepositorySubRequestDao extends IMyBatisBaseDAO<SplitRepositorySubRequest,Long> {

    /**
     * 根据主订单号获取子订单列表
     * @param orderId
     * @return
     */
    List<SplitRepositorySubRequest> getSubOrderList(String orderId);

    /**
     * 顶号的情况下 要把根据ID获取的主订单下所有子订单实际数量都设置为0 且都为失败
     * @param id
     */
    List<SplitRepositorySubRequest> selectRealCountZeroWithId(Long id);

}

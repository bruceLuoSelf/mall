package com.wzitech.gamegold.shorder.dao;

/**
 * Created by 339928 on 2018/6/14.
 */
public interface IRepositorySplitRequestManager {
    /**
     *  创建分仓单
     * @param orderId 收货主订单号
     * @param id 收货子订单id
     */
    void createRepositorySplitOrder(String orderId,Long id);

    /**
     * 分仓单完单
     * @param subSplitId  分仓单id
     * @param realCount 分仓单收到的实际数量
     * @param splitRepositoryReason 自动化执行状态
     * @param useRepertoryCount 使用金币仓库数量
     * @param useRepertoryCount 使用金币仓库数量
     */
    void finishSplitRepositoryOrder(Long subSplitId,Long realCount,int splitRepositoryReason,Long useRepertoryCount,String robotOtherReason);
}

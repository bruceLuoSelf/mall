package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.GameAccount;

import java.io.IOException;

/**
 * 出货单交易完成manager
 */
public interface IDeliveryOrderFinishManager {
//    /**
//     * 订单完成接口，供收货机器人调用
//     *
//     * @param mainOrderId  主订单号
//     * @param subOrderId   子订单号
//     * @param type         类型
//     *                     <ul>
//     *                     <li>100：交易完成</li>
//     *                     <li>200：需人工介入</li>
//     *                     <li>300：其他情况</li>
//     *                     <li>301：交易超时</li>
//     *                     <li>302：背包已满</li>
//     *                     <li>303：不是附魔师</li>
//     *                     </ul>
//     * @param receiveCount 总共收到数量
//     * @param remark       备注
//     */
//    void finish(String mainOrderId, Long subOrderId, Integer type, Long receiveCount, String remark) throws IOException;
//
//    /**
//     * 交易完成
//     * @param mainOrderId
//     * @param subOrderId
//     * @param gtrType
//     * @param receiveCount
//     * @param remark
//     */
//    GameAccount complete(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) throws IOException;

    /**
     * 更新前台数量
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param receiveCount
     */
     void chanageReceiveCount(String mainOrderId, Long subOrderId, String gtrType, Long receiveCount,Boolean offline);

    /**
     * 人工介入
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param backType
     * @param receiveCount
     * @param remark
     */
    GameAccount manual(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark,Boolean offline);

    /**
     * 交易取消
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param backType
     * @param remark
     */
     void cancel(String mainOrderId, Long subOrderId, String gtrType,Integer backType, String remark,Boolean offline);
}

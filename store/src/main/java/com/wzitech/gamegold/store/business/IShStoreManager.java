package com.wzitech.gamegold.store.business;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;

import java.io.IOException;
import java.util.Map;

/**
 * 库存管理(收货发起)
 * Created by 汪俊杰 on 2017/1/8.
 */
public interface IShStoreManager {
//    /**
//     * 交易完成接口，供收货机器人调用
//     *
//     * @param mainOrderId  主订单号
//     * @param subOrderId   子订单号
//     * @param gtrType      类型
//     *                     <ul>
//     *                     <li>100：交易完成</li>
//     *                     <li>101：修改收货数量</li>
//     *                     <li>200：需人工介入</li>
//     *                     <li>300：其他情况</li>
//     *                     <li>301：交易超时</li>
//     *                     <li>302：背包已满</li>
//     *                     <li>303：不是附魔师</li>
//     *                     </ul>
//     * @param receiveCount 总共收到数量
//     */
//     void complete(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) throws IOException;

    /**
     * 人工介入
     *
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param backType
     * @param receiveCount
     * @param remark
     */
     void manual(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline);

    /**
     * 人工完单
     *
     * @param mainOrderId             出货主订单号
     * @param subOrderReceiveCountMap 每个子订单收到的实际数量
     *                                <p>格式：</p>
     *                                <li>key=子订单ID</li>
     *                                <li>value=实际收到的数量</li>
     */
     void manualFinishOrder(String mainOrderId, Map<Long, Long> subOrderReceiveCountMap) throws IOException;

    /**
     * 分仓结果
     *
     * @param orderId
     * @param repsitoryCount
     * @param level
     * @param fmRepsitoryCount
     * @param fmLevel
     */
     void spliteResult(String orderId, Long repsitoryCount, Integer level, Long fmRepsitoryCount, Integer fmLevel);


    /**
     * 自动分配物服
     * @param orderId
     */
//    void voluntarilyRC(String orderId,Long id,String serviceId,String remark, Boolean offline,Integer type,Long receiveCount);
}

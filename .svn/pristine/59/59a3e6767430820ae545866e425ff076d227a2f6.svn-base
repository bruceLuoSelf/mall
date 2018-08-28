package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.common.enums.UserLogType;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.OrderLog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/17.
 */
public interface IOrderUserLogManager {

    /**
     * 保存聊天记录
     *
     * @param orderId
     * @param chattingRecords
     */
    void saveChattingRecords(String orderId, String chattingRecords);

    /**
     * 查询聊天记录
     *
     * @param orderId
     * @return
     */
    List<OrderLog> selectChattingRecords(String orderId);

    /**
     * 写入聊天记录
     *
     * @param sellerLog
     * @param buyerLog
     * @param order
     */
    Map<String,String> saveChatLog(String sellerLog, String buyerLog, UserLogType userLogType, DeliveryOrder order);

    /**
     * 根据查询聊天记录
     * @param orderId
     * @return
     */
     List<OrderLog> selectChattingRecordsByOrderId(String orderId);
}

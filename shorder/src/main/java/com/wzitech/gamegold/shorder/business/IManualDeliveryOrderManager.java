package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.ImageUploadElements;

import java.io.IOException;
import java.util.Map;

/**
 * 出货子订单管理
 */
public interface IManualDeliveryOrderManager {

    /**
     * 手动收货，分配收货角色
     * @param orderId
     * @param roleName
     * @param count
     * @return
     */
    DeliverySubOrder assignGameAccount(String orderId, String roleName, Long count);

    /**
     * 手动收货，子订单确认收货
     * @param subOrderId
     * @return
     */
    boolean confirmReceived(Long subOrderId) throws IOException;

    /**
     * 手动收货，已发货状态，收货商取消子订单
     * @param subOrderId
     * @return
     */
    boolean cancelSubOrder(Long subOrderId);

    /**
     * 出货商取消子订单
     * @param subOrderId
     */
    boolean cancelSubOrderBySeller(Long subOrderId) throws IOException;

    /**
     * 手动收货，待发货状态，出货商确认发货
     * @param subOrderId
     * @param realCount
     * @return
     */
    boolean confirmDelivery(Long subOrderId, Long realCount);

    /**
     * 开始交易(我已登录游戏)，for手动模式
     *
     * @param id 出货单ID
     */
    void startTradingForManual(long id);

    /**
     * 保存图片的路径
     * @param id   子订单唯一标识id
     * @param orderId 主订单号
     * @param imageJsonString  参数封装的uploadfilerequest类。tostring之后的String字符串
     */
    ImageUploadElements saveImageUrlForSubOrder(Long id, String imageJsonString, String orderId, int picSource,int viewRight) throws IOException;

}

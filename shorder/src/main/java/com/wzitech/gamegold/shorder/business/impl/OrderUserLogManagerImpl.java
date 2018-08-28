package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserLogType;
import com.wzitech.gamegold.shorder.business.IOrderUserLogManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderLogDao;
import com.wzitech.gamegold.shorder.dao.IOrderLogRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sunyang
 * Created by Administrator on 2017/2/17.
 */
@Component
public class OrderUserLogManagerImpl implements IOrderUserLogManager {

    @Autowired
    IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    IDeliveryOrderLogDao deliveryOrderLogDao;

    @Autowired
    IOrderLogRedisDao orderLogRedisDao;

    /**
     * 保存聊天记录
     * @param orderId
     * @param chattingRecords
     */
    @Override
    public void saveChattingRecords(String orderId, String chattingRecords) {
        if (orderId == null)
            throw new SystemException(ResponseCodes.OrderLogId.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());
        DeliveryOrder deliveryOrder = deliveryOrderDao.getByOrderId(orderId);
        if (deliveryOrder == null)
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());
        String userAccount = CurrentUserContext.getUserLoginAccount();
        if (!deliveryOrder.getBuyerAccount().equals(userAccount) && !deliveryOrder.getSellerAccount().equals(userAccount))
            throw new SystemException(ResponseCodes.userTypeError.getCode(), ResponseCodes.userTypeError.getMessage());

        //userType：当前log是谁发的
        UserLogType userLogType;
        if (deliveryOrder.getBuyerAccount().equals(userAccount)) {
            userLogType = UserLogType.BuyerLogType;
        } else if (deliveryOrder.getSellerAccount().equals(userAccount)) {
            userLogType = UserLogType.SellerLogType;
        } else {
            userLogType = UserLogType.SystemLogType;
        }

//        //出货商 收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(deliveryOrder.getBuyerHxAccount(),chattingRecords);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(deliveryOrder.getSellerHxAccount(),chattingRecords);

        saveChatLog(chattingRecords, chattingRecords, userLogType, deliveryOrder);
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(orderId);
//        orderLog.setLog(chattingRecords);
//        orderLog.setCreateTime(new Date());
//        orderLog.setUserAccount(deliveryOrder.getBuyerAccount());
//        orderLog.setUserType(userType);
//        deliveryOrderLogDao.insert(orderLog);
//
//        OrderLog orderLog1 = new OrderLog();
//        orderLog1.setOrderId(orderId);
//        orderLog1.setLog(chattingRecords);
//        orderLog1.setCreateTime(new Date());
//        orderLog1.setUserType(userType);
//        orderLog1.setUserAccount(deliveryOrder.getSellerAccount());
//        deliveryOrderLogDao.insert(orderLog1);
//
//        orderLogRedisDao.save(orderLog, orderId, deliveryOrder.getBuyerAccount());
//        orderLogRedisDao.save(orderLog1, orderId, deliveryOrder.getSellerAccount());

    }

    /**
     * 查询聊天记录
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLog> selectChattingRecords(String orderId) {
        if (orderId == null)
            throw new SystemException(ResponseCodes.OrderLogId.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());
        DeliveryOrder deliveryOrder = deliveryOrderDao.getByOrderId(orderId);
        if (deliveryOrder == null)
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());
        String userAccount = CurrentUserContext.getUserLoginAccount();
//        Long userId = CurrentUserContext.getUser().getId();
        if (!deliveryOrder.getBuyerAccount().equals(userAccount) && !deliveryOrder.getSellerAccount().equals(userAccount))
            throw new SystemException(ResponseCodes.userTypeError.getCode(), ResponseCodes.userTypeError.getMessage());

        List<OrderLog> listOrderListRedis = orderLogRedisDao.getByOrderLog("gamegold:shorder:OrderLog:"+orderId + ":" + userAccount);
        if (listOrderListRedis != null && listOrderListRedis.size() >= 0) {
            return listOrderListRedis;
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", orderId);
        queryMap.put("userAccount", userAccount);
        List<OrderLog> listOrderLogDb = deliveryOrderLogDao.selectByMap(queryMap);
        return listOrderLogDb;
    }

    /**
     * 根据查询聊天记录
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLog> selectChattingRecordsByOrderId(String orderId) {
        if (orderId == null)
            throw new SystemException(ResponseCodes.OrderLogId.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());
        DeliveryOrder deliveryOrder = deliveryOrderDao.getByOrderId(orderId);
        if (deliveryOrder == null)
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(), ResponseCodes.OrderLogIdInvalid.getMessage());

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", orderId);
        List<OrderLog> list = deliveryOrderLogDao.selectByMap(queryMap);
        return list;
    }


    @Override
    public Map<String,String> saveChatLog(String sellerLog, String buyerLog, UserLogType userLogType, DeliveryOrder order) {
        //同一笔log,收货商、出货商都各自存一笔；获取的时候根据订单号、登录帐号获取自己的log，根据userType来判断当前log是谁发的
        //出货商日志
        Map<String, String> message = new HashMap<String, String>();
        if(sellerLog!=null) {
            OrderLog orderLogSeller = new OrderLog();
            orderLogSeller.setOrderId(order.getOrderId());
            orderLogSeller.setLog(sellerLog);
            orderLogSeller.setUserAccount(order.getSellerAccount());
            orderLogSeller.setUserType(userLogType.getCode());
            orderLogSeller.setCreateTime(new Date());
            deliveryOrderLogDao.insert(orderLogSeller);
            message.put("sellerId",orderLogSeller.getId()+"");
            //订单中聊天记录存于redis中
            orderLogRedisDao.save(orderLogSeller, order.getOrderId(), order.getSellerAccount());
        }
        if(buyerLog!=null) {
            //收货商日志
            OrderLog orderLogBuyer = new OrderLog();
            orderLogBuyer.setOrderId(order.getOrderId());
            orderLogBuyer.setLog(buyerLog);
            orderLogBuyer.setUserAccount(order.getBuyerAccount());
            orderLogBuyer.setUserType(userLogType.getCode());
            orderLogBuyer.setCreateTime(new Date());
            deliveryOrderLogDao.insert(orderLogBuyer);
            message.put("buyerId",orderLogBuyer.getId()+"");
            orderLogRedisDao.save(orderLogBuyer, order.getOrderId(), order.getBuyerAccount());
        }
        return message;
    }
}

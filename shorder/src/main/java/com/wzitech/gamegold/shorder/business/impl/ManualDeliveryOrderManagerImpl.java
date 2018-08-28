package com.wzitech.gamegold.shorder.business.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.dao.impl.HxChatroomNetWorkDaoImpl;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.PicSourceEnum;
import com.wzitech.gamegold.shorder.utils.HttpToConn;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出货子订单管理
 *
 * @author yemq
 */
@Component
public class ManualDeliveryOrderManagerImpl extends AbstractBusinessObject implements IManualDeliveryOrderManager {
    @Autowired
    IMobileMsgManager mobileMsgManager;
    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;
    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private IOrderUserLogManager orderUserLogManager;

    @Autowired
    private IHxChatroomNetWorkDao hxChatroomNetWorkDao;

    @Autowired
    private IOrderLogRedisDao orderLogRedisDao;


    @Autowired
    private AsyncPushToMainMethodsImple asyncPushToMainMehtods;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;
    @Autowired
    private IRobotImgDAO robotImgDAO;

    @Value("${huanxin.5173.url}")
    private String huanXinUrl;

    @Value("${methodName}")
    private String methodName;

    @Value("${UpLoadFileUserName}")
    private String userName;

    @Value("${passWord}")
    private String passWord;

    @Autowired
    private IHxChatroomNetWorkDao huanXinManager;

    @Autowired
    HttpToConn httpToConn;


    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 手动收货，收货商分配收货角色
     *
     * @param orderId
     * @param roleName
     * @param count
     * @return
     */
    @Override
    @Transactional
    public DeliverySubOrder assignGameAccount(String orderId, String roleName, Long count) {
        logger.info("手动收货，收货商分配收货角色开始，参数：orderId:{},roleName:{},count:{}", new Object[]{orderId, roleName, count});

        if (orderId == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }
        if (StringUtils.isBlank(roleName)) {
            throw new SystemException(ResponseCodes.EmptyShRoleName.getCode(), ResponseCodes.EmptyShRoleName.getMessage());
        }
        if (count == null || count <= 0) {
            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(), ResponseCodes.EmptyReceivingCount.getMessage());
        }
        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
        if (order == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getCode(), ResponseCodes.NoDeliveryOrder.getMessage());
        }
        //校验是否自己的订单
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!order.getBuyerAccount().equals(loginAccount)) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }
        //校验订单状态，只有排队中，交易中的可以分配角色
        if (order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode() && order.getStatus() != DeliveryOrderStatus
                .TRADING.getCode() && DeliveryOrderStatus.WAIT_RECEIVE.getCode() != order.getStatus()) {
            String msg = "该订单状态是：" + DeliveryOrderStatus.getTypeByCode(order.getStatus()).getName() + "，不能分配收货角色";
            throw new SystemException(msg, msg);
        }
        //校验收货数量不能超过未收货数量
        Long restCount = order.getCount() - order.getRealCount();
        if (count > restCount) {
            String msg = "收货数量不能超过" + restCount;
            throw new SystemException(msg, msg);
        }
        //校验之前的子订单，是否有未完成的，否则不能操作
        Map<String, Object> params = Maps.newHashMap();
        params.put("orderId", orderId);
        params.put("notInStatus", DeliveryOrderStatus.COMPLETE.getCode() + "," + DeliveryOrderStatus.COMPLETE_PART.getCode() + "," + DeliveryOrderStatus.CANCEL.getCode());
        int result = deliverySubOrderDao.countByMap(params);
        if (result > 0) {
            throw new SystemException(ResponseCodes.SubOrderNotFinishCannotAssignRole.getCode(), ResponseCodes.SubOrderNotFinishCannotAssignRole.getMessage());
        }
        //保存子订单
        DeliverySubOrder subOrder = new DeliverySubOrder();
        subOrder.setChId(order.getId());
        subOrder.setOrderId(order.getOrderId());
        subOrder.setSellerAccount(order.getSellerAccount());
        subOrder.setSellerUid(order.getSellerUid());
        subOrder.setBuyerAccount(order.getBuyerAccount());
        subOrder.setBuyerUid(order.getBuyerUid());
        subOrder.setGameName(order.getGameName());
        subOrder.setRegion(order.getRegion());
        subOrder.setServer(order.getServer());
        subOrder.setGameRace(order.getGameRace());
        subOrder.setWords(order.getWords());
        subOrder.setAddress(order.getAddress());
        subOrder.setGameRole(roleName);
        subOrder.setCount(count);
        subOrder.setStatus(DeliveryOrderStatus.WAIT_DELIVERY.getCode());
        Date now = new Date();
        subOrder.setCreateTime(now);
        subOrder.setUpdateTime(now);
        subOrder.setSellerRoleName(order.getRoleName());
        subOrder.setDeliveryType(order.getDeliveryType());
        subOrder.setTradeType(order.getTradeType());
        subOrder.setMoneyName(order.getMoneyName());
        deliverySubOrderDao.insert(subOrder);

        //把主订单状态更改为交易中
        if (order.getStatus() == DeliveryOrderStatus.INQUEUE.getCode()) {
            order.setStatus(DeliveryOrderStatus.TRADING.getCode());
            order.setTradeStartTime(now);
            deliveryOrderDao.update(order);

            int orderCenterStatus = OrderCenterOrderStatus.WAIT_SEND.getCode();
            asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);
        }

        // 写入订单日志
        String sellerLog = "收货商已经分配游戏角色名：" + roleName + "，该角色收货数量" + count + "万金，请您仔细核对角色名后交易，务必由你邀请收货商交易并录制交易视频。";
        String buyerLog = "您已经分配游戏角色名：" + roleName + "，该角色收货数量" + count + "万金。";

        orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);

//        //TODo 出货商id
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, ids.get("sellerId"), HxChatroomNetWorkDaoImpl.SELLER);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, ids.get("buyerId"), HxChatroomNetWorkDaoImpl.BUYER);

        logger.info("手动收货，收货商分配收货角色结束，生成子订单：{}", ToStringBuilder.reflectionToString(subOrder));
        return subOrder;
    }

    /**
     * 手动收货，子订单收货商确认收货
     *
     * @param subOrderId
     * @return
     */
    @Override
    @Transactional
    public boolean confirmReceived(Long subOrderId) throws IOException {
        logger.info("手动收货，子订单收货商确认收货开始，参数：subOrderId:{}", subOrderId);
        if (subOrderId == null) {
            throw new SystemException(ResponseCodes.EmptySubOrderId.getCode(), ResponseCodes.EmptySubOrderId.getMessage());
        }
        //查询子订单
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
        if (subOrder == null) {
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
        }
        //校验是否自己的订单
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!subOrder.getBuyerAccount().equals(loginAccount)) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }
        //查询主订单
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(subOrder.getChId());
        if (order == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getCode(), ResponseCodes.NoDeliveryOrder.getMessage());
        }
//        //主订单状态为待确认收货的才可以确认收货
//        if (order.getStatus() != DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
//            throw new SystemException(ResponseCodes.DeliveryOrderNotWAIT_RECEIVENotConfirm.getCode(),
//                    ResponseCodes.DeliveryOrderNotWAIT_RECEIVENotConfirm.getMessage());
//        }
        //校验订子单状态，只有已发货或者待发货才可以确认收货
        if (subOrder.getStatus() != DeliveryOrderStatus.DELIVERY_FINISH.getCode() && subOrder.getStatus() != DeliveryOrderStatus.
                WAIT_DELIVERY.getCode()) {
            throw new SystemException(ResponseCodes.SubOrderNotDelivery.getCode(), ResponseCodes.SubOrderNotDelivery.getMessage());
        }
        //如果是代发货状态确认已经收货，则默认已全部收货
        if (subOrder.getStatus() == DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
            subOrder.setRealCount(subOrder.getCount());
        }
        if (subOrder.getRealCount() == null || subOrder.getRealCount() <= 0) {
            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(), ResponseCodes.EmptyReceivingCount.getMessage());
        }


        //更新子订单状态
        Date now = new Date();
        boolean isReAssignRemind = false; //是否需要提示，再次分配角色

        if (subOrder.getRealCount().longValue() < subOrder.getCount().longValue()) {
            subOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            //子订单部分完单，则更新主订单为部分完单
            order.setRealCount(order.getRealCount() == null ? 0 : order.getRealCount() + subOrder.getRealCount());
            BigDecimal realAmount = order.getPrice().multiply(new BigDecimal(order.getRealCount())).setScale(2, RoundingMode.DOWN);
            if (realAmount.compareTo(order.getAmount()) == 1) {
                realAmount = order.getAmount();
            }
//            order.setRealAmount(order.getPrice().multiply(new BigDecimal(order.getRealCount())).setScale(2, RoundingMode.DOWN));
            order.setRealAmount(realAmount);
            order.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            order.setTradeEndTime(now);
            deliveryOrderDao.update(order);
            //订单状态改变 同步通知主站订单中心
            int orderCenterOrderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            asyncPushToMainMehtods.orderPushToMain(order, orderCenterOrderStatus);

            //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
//            //更新采购单库存，把未发货的加回去
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount() - order.getRealCount());
            //end by 20170728 汪俊杰 解决线上数据堵塞等待问题

//            //结算
//            deliveryOrderManager.settlement(order.getOrderId());

            //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
            //更新采购单库存，把未发货的加回去
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount() - order.getRealCount());
            purchaseOrderManager.updatePurchaseOrderCountById(order.getCgId(),order.getCount() - order.getRealCount());
            //end by 20170728 汪俊杰 解决线上数据堵塞等待问题

            //删除聊天室id
            if (StringUtils.isNotBlank(order.getChatroomId())) {
                hxChatroomNetWorkDao.deleteChatroom(order.getChatroomId());
            }

            //先查询到聊天记录
//            String loginAccount = userInfo.getLoginAccount();
            String orderId = order.getOrderId();
            List<OrderLog> list = orderUserLogManager.selectChattingRecords(orderId);
            //再对聊天记录循环删除
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    orderLogRedisDao.deleteChatRecord(orderId, loginAccount, list.get(i));
                }
            }
        } else {
            subOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());

            //如果收货完成，则更新主订单为交易完成
            order.setRealCount(order.getRealCount() == null ? 0 : order.getRealCount() + subOrder.getRealCount());
            BigDecimal realAmount = order.getPrice().multiply(new BigDecimal(order.getRealCount())).setScale(2, RoundingMode.DOWN);
            if (realAmount.compareTo(order.getAmount()) == 1) {
                realAmount = order.getAmount();
            }
            order.setRealAmount(realAmount);
//            order.setRealAmount(order.getPrice().multiply(new BigDecimal(order.getRealCount())).setScale(2, RoundingMode.DOWN));
            if (order.getRealCount().longValue() == order.getCount().longValue()) {
                order.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
                order.setTradeEndTime(now);
                deliveryOrderDao.update(order);
                //订单状态改变 同步通知主站订单中心
                int orderCenterOrderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
                asyncPushToMainMehtods.orderPushToMain(order, orderCenterOrderStatus);

//                //结算
//                deliveryOrderManager.settlement(order.getOrderId());

                // 删除聊天室
                if (order.getChatroomId() != null)
                    hxChatroomNetWorkDao.deleteChatroom(order.getChatroomId());

                //先查询到聊天记录
//                String loginAccount = userInfo.getLoginAccount();
                String orderId = order.getOrderId();
                List<OrderLog> list = orderUserLogManager.selectChattingRecords(orderId);
                //再对聊天记录循环删除
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        orderLogRedisDao.deleteChatRecord(orderId, loginAccount, list.get(i));
                    }
                }
            } else {
                isReAssignRemind = true;
                //更新主订单状态待分配角色
                order.setStatus(DeliveryOrderStatus.INQUEUE.getCode());
                deliveryOrderDao.update(order);
                int orderCenterStatus = OrderCenterOrderStatus.WAIT_SEND.getCode();
                asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);
            }
        }

        subOrder.setUpdateTime(now);
        subOrder.setFinishTime(now);
        deliverySubOrderDao.update(subOrder);

        // 写入订单日志
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(order.getOrderId());
//        orderLog.setLog("系统信息：收货商确认收货，本次收货数量为" + subOrder.getRealCount());
//        deliveryOrderLogManager.writeLog(orderLog);
//
//        orderLog.setLog("系统信息：订单数量" + order.getCount() + "，已收货数量" + order.getRealCount() + "，未交易数量" + (order.getCount() - order.getRealCount()));
//        deliveryOrderLogManager.writeLog(orderLog);

        //资金结算
        if (subOrder.getRealCount().longValue() < subOrder.getCount().longValue()) {
            //结算
            deliveryOrderManager.settlement(order.getOrderId());
        } else {
            if (order.getRealCount().longValue() == order.getCount().longValue()) {
                //结算
                deliveryOrderManager.settlement(order.getOrderId());
            }
        }

        try {
            String sellerLog = "收货商确认收货，本次收货数量为" + subOrder.getRealCount();
            String buyerLog = "您确认收货，本次收货数量为" + subOrder.getRealCount();

//            //TODo 出货商id
//            hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, HxChatroomNetWorkDaoImpl.SELLER);
//            //收货商发送请求
//            hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, HxChatroomNetWorkDaoImpl.BUYER);

            orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);

            String sellerLog2 = "订单数量" + order.getCount() + "，已收货数量" + order.getRealCount() + "，未交易数量" + (order.getCount() - order.getRealCount());
            String buyerLog2 = "订单数量" + order.getCount() + "，已收货数量" + order.getRealCount() + "，未交易数量" + (order.getCount() - order.getRealCount());

            orderUserLogManager.saveChatLog(sellerLog2, buyerLog2, UserLogType.SystemLogType, order);

//            //TODo 出货商id
//            hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog2, HxChatroomNetWorkDaoImpl.SELLER);
//            //收货商发送请求
//            hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog2, HxChatroomNetWorkDaoImpl.BUYER);

            if (isReAssignRemind) {
//            orderLog.setLog("我们正在为您再次分配收货角色");
//            deliveryOrderLogManager.writeLog(orderLog);
                String sellerLog1 = "收货商正在为您再次分配收货角色";
                String buyerLog1 = "请您再次分配收货角色";

                orderUserLogManager.saveChatLog(sellerLog1, buyerLog1, UserLogType.SystemLogType, order);

//                //TODo 出货商id
//                hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog1, sellerAndBuyerIds.get("sellerId"), HxChatroomNetWorkDaoImpl.SELLER);
//                //收货商发送请求
//                hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog1, sellerAndBuyerIds.get("buyerId"), HxChatroomNetWorkDaoImpl.BUYER);
            }
        } catch (Exception ex) {
            logger.error("confirmReceived推送环信：" + ex.toString());
        }

        logger.info("手动收货，子订单收货商确认收货结束");
        return true;
    }

    /**
     * 手动收货，收货商取消子订单
     *
     * @param subOrderId
     * @return
     */
    @Override
    @Transactional
    public boolean cancelSubOrder(Long subOrderId) {
        logger.info("手动收货，已发货状态，收货商取消子订单开始，参数：subOrderId:{}", subOrderId);
        if (subOrderId == null) {
            throw new SystemException(ResponseCodes.EmptySubOrderId.getCode(), ResponseCodes.EmptySubOrderId.getMessage());
        }
        //查询子订单
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
        if (subOrder == null) {
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
        }
        //校验子订单状态，只有已发货的才可以取消
        if (subOrder.getStatus() != DeliveryOrderStatus.WAIT_DELIVERY.getCode() && subOrder.getStatus() != DeliveryOrderStatus.DELIVERY_FINISH.getCode()) {
            throw new SystemException(ResponseCodes.SubOrderNotDeliveryNotCancel.getCode(), ResponseCodes.SubOrderNotDeliveryNotCancel.getMessage());
        }
        //未发货状态，校验是否超过30分钟
        Date now = new Date();
        if (subOrder.getStatus() == DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
            long timeDiff = now.getTime() - subOrder.getCreateTime().getTime();
            if (timeDiff < 30 * 60 * 1000) {
                throw new SystemException(ResponseCodes.SubOrderNotOvertimeNotCancel.getCode(), ResponseCodes.SubOrderNotOvertimeNotCancel.getMessage());
            }
        }

        //校验是否自己的订单
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!subOrder.getBuyerAccount().equals(loginAccount)) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }

        //查询主订单
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(subOrder.getChId());
        if (order == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getCode(), ResponseCodes.NoDeliveryOrder.getMessage());
        }

        //收货商取消子订单，更新订单状态为人工介入
        subOrder.setStatus(DeliveryOrderStatus.MANUAL_INTERVENTION.getCode());
        subOrder.setUpdateTime(now);
        deliverySubOrderDao.update(subOrder);

        order.setStatus(DeliveryOrderStatus.MANUAL_INTERVENTION.getCode());
        deliveryOrderDao.update(order);
        //订单状态改变 需同步通知主站订单中心
        int centerOrderStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
        asyncPushToMainMehtods.orderPushToMain(order, centerOrderStatus);

        // 写入订单日志
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(order.getOrderId());
//        orderLog.setLog("收货商发起撤单申请，系统为您分配客服介入，您可以联系客服咨询！");
//        deliveryOrderLogManager.writeLog(orderLog);
        String sellerLog = "收货商发起撤单申请，系统为您分配客服介入，您可以联系客服咨询！";
        String buyerLog = "您已经发起撤单申请，系统为您分配客服介入，您可以联系客服咨询！";

        orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);

//        //TODo 出货商id
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, sellerAndBuyerIds.get("sellerId"), HxChatroomNetWorkDaoImpl.SELLER);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, sellerAndBuyerIds.get("buyerId"), HxChatroomNetWorkDaoImpl.BUYER);

        logger.info("手动收货，已发货状态，收货商取消子订单结束");
        return true;
    }

    /**
     * 手动收货，待发货状态，出货商确认发货
     *
     * @param subOrderId
     * @param realCount
     * @return
     */
    @Override
    @Transactional
    public boolean confirmDelivery(Long subOrderId, Long realCount) {
        logger.info("手动收货，待发货状态，出货商确认发货开始，参数：subOrderId:{},realCount:{}", new Object[]{subOrderId, realCount});
        if (subOrderId == null) {
            throw new SystemException(ResponseCodes.EmptySubOrderId.getCode(), ResponseCodes.EmptySubOrderId.getMessage());
        }
        if (realCount == null || realCount <= 0) {
            throw new SystemException(ResponseCodes.EmptyDeliveryCount.getCode(), ResponseCodes.EmptyDeliveryCount.getMessage());
        }
        //查询子订单
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
        if (subOrder == null) {
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
        }
        if (realCount > subOrder.getCount()) {
            throw new SystemException(ResponseCodes.DeliveryCountGtPurchaseCount.getCode(), ResponseCodes.DeliveryCountGtPurchaseCount.getMessage());
        }
        //校验子订单状态，只有待发货状态的才可以确认发货
        if (subOrder.getStatus() != DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
            throw new SystemException(ResponseCodes.SubOrderNotWaitDeliveryNotConfirm.getCode(), ResponseCodes.SubOrderNotWaitDeliveryNotConfirm.getMessage());
        }
        //校验是否自己的订单
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!subOrder.getSellerAccount().equals(loginAccount)) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }

        //更新子订单状态为已发货
        subOrder.setStatus(DeliveryOrderStatus.DELIVERY_FINISH.getCode());
        Date now = new Date();
        subOrder.setDeliveryTime(now);
        subOrder.setRealCount(realCount);
        subOrder.setUpdateTime(now);
        deliverySubOrderDao.update(subOrder);

        //主订单状态为交易中则更新主订单状态为待确认收货
        DeliveryOrder order = new DeliveryOrder();
        order = deliveryOrderDao.selectById(subOrder.getChId());
        if (DeliveryOrderStatus.TRADING.getCode() == order.getStatus()) {
            order.setStatus(DeliveryOrderStatus.WAIT_RECEIVE.getCode());
            deliveryOrderDao.update(order);
            //订单状态改变 同步通知主站订单中心
            int centerOrderStatus = OrderCenterOrderStatus.ALREADY_SEND.getCode();
            asyncPushToMainMehtods.orderPushToMain(order, centerOrderStatus);
        }

        // 写入订单日志
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(subOrder.getOrderId());
//        orderLog.setLog("您已发货，请等待收货商确认，如有问题您可以联系客服！");
//        deliveryOrderLogManager.writeLog(orderLog);
        String sellerLog = "您已发货，请等待收货商确认，如有问题您可以联系客服！";
        String buyerLog = "出货商已经发货，请您尽快确认收货，如有问题您可以联系客服！";

//        //TODo 出货商id
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, HxChatroomNetWorkDaoImpl.SELLER);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, HxChatroomNetWorkDaoImpl.BUYER);

        orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);


        logger.info("手动收货，待发货状态，出货商确认发货结束");
        return true;
    }

    /**
     * 出货商取消子订单
     *
     * @param subOrderId
     */
    @Override
    @Transactional
    public boolean cancelSubOrderBySeller(Long subOrderId) throws IOException {
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);

        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!StringUtils.equals(loginAccount, subOrder.getSellerAccount())) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),
                    ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }
        if (subOrder.getStatus() != DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
            throw new SystemException(ResponseCodes.ErrorOrderStatusCantCancelOrder.getCode(),
                    ResponseCodes.ErrorOrderStatusCantCancelOrder.getMessage());
        }
        if (subOrder.getRealCount() != null && subOrder.getRealCount().longValue() > 0) {
            throw new SystemException(ResponseCodes.CannotCancelOrderRealCountGtZero.getCode(),
                    ResponseCodes.CannotCancelOrderRealCountGtZero.getMessage());
        }

        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(subOrder.getChId());
        if (order == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getCode(), ResponseCodes.NoDeliveryOrder.getMessage());
        }

        //如果之前有子订单成功，则部分完单并转账，否则主订单撤单
        if (order.getRealCount() == null || order.getRealCount() == 0) {
            //取消主订单
            deliveryOrderManager.cancelOrder(order.getId(), DeliveryOrder.OHTER_REASON, "出货商取消子订单");
        } else if (order.getRealCount() > 0 && order.getRealCount().longValue() < order.getCount().longValue()) {
            subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
            subOrder.setOtherReason("出货商取消子订单");
            subOrder.setUpdateTime(new Date());
            deliverySubOrderDao.update(subOrder);

            //主订单变成部分完单
            order.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            order.setOtherReason("出货商取消子订单");
            order.setTradeEndTime(new Date());
            deliveryOrderDao.update(order);
            //订单状态改变 同步通知主站订单中心
            int orderCenterOrderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            asyncPushToMainMehtods.orderPushToMain(order, orderCenterOrderStatus);

            //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
//            //更新采购单库存，把未发货的加回去
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount() - order.getRealCount());
            //end by 20170728 汪俊杰 解决线上数据堵塞等待问题

//            //结算
//            deliveryOrderManager.settlement(order.getOrderId());

            //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
            //更新采购单库存，把未发货的加回去
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount() - order.getRealCount());
            purchaseOrderManager.updatePurchaseOrderCountById(order.getCgId(),order.getCount() - order.getRealCount());
            //end by 20170728 汪俊杰 解决线上数据堵塞等待问题
        }

        //先查询到聊天记录
//        String loginAccount = userInfo.getLoginAccount();
        String orderId = order.getOrderId();
        List<OrderLog> list = orderUserLogManager.selectChattingRecords(orderId);
        //再对聊天记录循环删除
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                orderLogRedisDao.deleteChatRecord(orderId, loginAccount, list.get(i));
            }
        }

        //写入日志
//        OrderLog log = new OrderLog();
//        log.setOrderId(subOrder.getOrderId());
//        log.setLog("您已取消订单");
//        deliveryOrderLogManager.writeLog(log);
        String sellerLog = "您已取消订单";
        String buyerLog = "出货商主动取消订单";


        orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);

        // 删除群组
        if (StringUtils.isNotBlank(order.getChatroomId())) {
            hxChatroomNetWorkDao.deleteChatroom(order.getChatroomId());
        }

        if (order.getRealCount() > 0 && order.getRealCount().longValue() < order.getCount().longValue()) {
            //结算
            deliveryOrderManager.settlement(order.getOrderId());
        }

//        //出货商id
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, ids.get("sellerId"), HxChatroomNetWorkDaoImpl.SELLER);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, ids.get("buyerId"), HxChatroomNetWorkDaoImpl.BUYER);

        return true;
    }

    /**
     * 开始交易(我已登录游戏)，for手动模式
     *
     * @param id 出货单ID
     */
    @Transactional
    public void startTradingForManual(long id) {
        logger.info("我已登录for手动模式开始，参数：id:{}", id);
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        if (order.getStatus().intValue() != DeliveryOrderStatus.WAIT_TRADE.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        order.setStatus(DeliveryOrderStatus.INQUEUE.getCode());
        order.setQueueStartTime(new Date());

        deliveryOrderDao.update(order);

        int orderCenterStatus = OrderCenterOrderStatus.WAIT_SEND.getCode();
        asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);

        //发短信
        sendMessage(order.getBuyerPhone(), String.format("订单：%s, 出货商游戏角色：%s已登录游戏，交易方式：%s，请及时上号交易", order.getOrderId(), order.getRoleName(), order.getTradeTypeName()));

        // 写入订单日志
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(order.getOrderId());
//        orderLog.setLog("收货商已经接到订单信息，正在为您分配收货角色，请等待");
//        deliveryOrderLogManager.writeLog(orderLog);
        String sellerLog = "收货商已经接到订单信息，正在为您分配收货角色，请等待";
        String buyerLog = "出货商已经登录游戏，请您尽快分配角色并登录游戏收货";


        orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);

//        //TODo 出货商id
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), sellerLog, messag.get("sellerId"), HxChatroomNetWorkDaoImpl.SELLER);
//        //收货商发送请求
//        hxChatroomNetWorkDao.sendHxSystemMessage(order.getChatroomId(), buyerLog, messag.get("buyerId"), HxChatroomNetWorkDaoImpl.BUYER);
        logger.info("我已登录for手动模式结束:order:{}", order);
    }


    /**
     * 送短信
     *
     * @param mobilePhone
     * @return
     * @ 发param content
     */
    private boolean sendMessage(String mobilePhone, String content) {
        try {
            mobileMsgManager.sendMessage(mobilePhone, content);
        } catch (Exception e) {
            logger.error("发送短信出错了", e);
            return false;
        }
        return true;
    }


    @Override
    public ImageUploadElements saveImageUrlForSubOrder(Long id, String param, String orderId, int picSource, int viewRight) throws IOException {
        long time = System.currentTimeMillis();
        String sign = userName + time + methodName + param + passWord;
        sign = EncryptHelper.md5(sign, "UTF-8");
        String paramForUrl = "method=" + methodName + "&user=" + userName + "&sign=" + sign + "&time=" + time;
        String url = String.format("http://tradeservice.5173.com/tradeservicecomm.ashx?%s", paramForUrl);
        JSONObject resultJson = httpToConn.httpPost(url, param);
        ImageUploadElements imageUploadElements = jsonMapper.fromJson(resultJson.toString(), ImageUploadElements.class);
        if (imageUploadElements.isSucceed()) {
//            responseMap.put("OriginalUrl", resultJson.get("OriginalUrl").toString());
            logger.info("上传图片成功,访问路径为" + resultJson.get("OriginalUrl").toString());
            RobotImgEO robotImgEO = new RobotImgEO();
            robotImgEO.setImgSrc(resultJson.get("OriginalUrl").toString());
            robotImgEO.setCreateTime(new Date());
            robotImgEO.setOrderId(orderId);
            if (id != null) {
                robotImgEO.setSubOrderId(id);
            }
            robotImgEO.setImgSource(picSource);
            robotImgEO.setType(viewRight);
            robotImgDAO.insert(robotImgEO);
            DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(orderId);
            //人工的、邮寄的、用户自上传的 加一笔日志记录
            if (deliveryOrder.getDeliveryType() == DeliveryTypeEnum.Manual.getCode()
                    && deliveryOrder.getTradeLogo() == TradeLogoEnum.AuctionTrade.getCode()
                    && picSource == PicSourceEnum.CUSTOMER.getCode()) {
                OrderLog orderLog = new OrderLog();
                orderLog.setOrderId(orderId);
                StringBuffer sb = new StringBuffer();
                sb.append("收货商已上传游戏截图。").append("<a href=" + resultJson.get("OriginalUrl").toString() + " target=" + "_Blank" + ">查看游戏截图</a>");
                orderLog.setLog(sb.toString());
                orderLog.setType(OrderLog.TYPE_NORMAL);
                //属于系统的日志
                orderLog.setUserType(UserLogType.SystemLogType.getCode());
                orderLog.setCreateTime(new Date());
                orderLog.setUserAccount(deliveryOrder.getBuyerAccount());
                if (id != null) {
                    orderLog.setSubId(id);
                }
                orderUserLogManager.saveChatLog(sb.toString(),sb.toString(),UserLogType.SystemLogType,deliveryOrder);
            }
        }
        return imageUploadElements;
    }
}

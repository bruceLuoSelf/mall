package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.dao.ISellerDTOdao;
import com.wzitech.gamegold.shorder.dto.SellerDTO;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import com.wzitech.gamegold.shorder.enums.OrderPrefix;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货单交易完成manager实现类
 *
 * @author yemq
 */
@Component
public class DeliveryOrderFinishManagerImpl implements IDeliveryOrderFinishManager {
    public static final String RCNAME = "全自动物服";
    protected static final Logger logger = LoggerFactory.getLogger(DeliveryOrderFinishManagerImpl.class);
    @Autowired
    private IDeliveryOrderAutoConfigManager autoConfigManager;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private AsyncPushToMainMethodsImple asyncPushToMainMehtods;

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private IFundManager fundManager;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    private ISplitRepositoryRequestManager splitRepositoryRequestManager;

    @Autowired
    private IGameAccountManager gameAccountManager;

    @Autowired
    private ISellerDTOdao sellerDTOdao;

//    /**
//     * 订单完成接口，供收货机器人调用
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
//     * @param remark       备注
//     */
//    @Override
//    @Transactional
//    public void finish(String mainOrderId, Long subOrderId, Integer gtrType, Long receiveCount, String remark) throws IOException {
//        if (StringUtils.isBlank(mainOrderId) || subOrderId == null)
//            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
//
//        if (gtrType == null)
//            throw new SystemException(ResponseCodes.EmptyType.getCode(), ResponseCodes.EmptyType.getMessage());
//
//        if (receiveCount == null || receiveCount.longValue() < 0)
//            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(),
//                    ResponseCodes.EmptyReceivingCount.getMessage());
//
//
//        logger.info("调用订单完成接口,orderId:{}, subOrderId:{}, type:{}, receiveCount:{}, remark:{}",
//                new Object[]{mainOrderId, subOrderId, gtrType, receiveCount, remark});
//
//        DeliveryOrderGTRType type = DeliveryOrderGTRType.getTypeByCode(gtrType);
//
//        if (type == DeliveryOrderGTRType.CHANGE_RECEIVE_COUNT) {
//            // 只更新收货数量
//            deliveryOrderManager.changeReceiveCount(mainOrderId, subOrderId, receiveCount);
//            return;
//        }
//
//        // 更新子订单数据
//        //DeliverySubOrder dbSubOrder = updateSubOrderInfo(subOrderId, type, receiveCount, remark);
//        DeliverySubOrder dbSubOrder = null;
//        // 更新主订单，有需要再生成配单
//        DeliveryOrder mainOrder = updateMainOrder(dbSubOrder, receiveCount, type, remark);
//
//        // 写入订单日志
//        String log = "";
//        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
//            log = "交易成功";
//        } else if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
//            log = "交易结束，已部分完单";
//        } else if (mainOrder.getStatus() == DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
//            log = "交易结束，需人工介入";
//        } else if (mainOrder.getStatus().intValue() == DeliveryOrderStatus.CANCEL.getCode()) {
//            log = "交易取消";
//        } else {
//            remark = StringUtils.isBlank(remark) ? "游戏异常" : remark;
//            log = remark;
//        }
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(mainOrder.getOrderId());
//        orderLog.setLog(log);
//        orderLog.setType(OrderLog.TYPE_NORMAL);
//        deliveryOrderLogManager.writeLog(orderLog);
//
//        // 写入日志
//        StringBuilder sb = new StringBuilder("系统信息：订单数量").append(mainOrder.getCount());
//        sb.append("，已收货数量").append(mainOrder.getRealCount()).append("，未交易数量");
//        sb.append(mainOrder.getCount() - mainOrder.getRealCount());
//        orderLog = new OrderLog();
//        orderLog.setOrderId(dbSubOrder.getOrderId());
//        orderLog.setLog(sb.toString());
//        orderLog.setType(OrderLog.TYPE_NORMAL);
//        deliveryOrderLogManager.writeLog(orderLog);
//
//        // 更新游戏账号信息
//        deliveryOrderManager.updateGameAccountInfo(dbSubOrder, type, false);
//
//        if (receiveCount > 0) {
//            //更新帐号库存
//            gameAccountManager.addRepositoryCount(dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameRace(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole(), receiveCount, true);
//        }
//
//        // 交易完成或部分完单的，生成分仓请求，资金结算
//        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()
//                || mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
//
////            // 查询该订单下的账号信息
////            List<DeliverySubOrder> list = deliverySubOrderDao.queryAllByOrderId(mainOrderId);
////            for (DeliverySubOrder subOrder : list) {
////                // 交易完成后，生成分仓请求
////                splitRepositoryRequestManager.create(subOrder);
////            }
//
//            // 资金结算
//            deliveryOrderManager.settlement(mainOrder.getOrderId());
//        } else if (mainOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
//            // 交易取消，解冻资金
//            //如果开通了新资金，还需解冻7bao的资金  ZW_C_JB_00021 340096-20170825
//            String buyerAccount = mainOrder.getBuyerAccount();
//            String uid = mainOrder.getBuyerUid();
//            SellerDTO seller = sellerDTOdao.findByAccountAndUid(buyerAccount, uid);
//            if (mainOrderId.contains(OrderPrefix.NEWORDERID.getName())){
//                DeliveryOrder deliveryOrder = deliveryOrderManager.getByOrderId(mainOrderId);
//                fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER,deliveryOrder,mainOrder.getAmount());
//            }else {
//                //未开通新资金继续走原有资金
//                fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, mainOrder.getBuyerAccount(),
//                        mainOrder.getOrderId(), mainOrder.getAmount());
//            }
//        }
//    }

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
//    @Override
//    @Transactional
//    public GameAccount complete(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) throws IOException {
//        if (StringUtils.isBlank(mainOrderId) || subOrderId == null)
//            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
//        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(mainOrderId);
//        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode() && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
//            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
//        }
//        if (gtrType == null)
//            throw new SystemException(ResponseCodes.EmptyType.getCode(), ResponseCodes.EmptyType.getMessage());
//
//        if (receiveCount == null || receiveCount.longValue() < 0)
//            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(),
//                    ResponseCodes.EmptyReceivingCount.getMessage());
//
//        //设置物服姓名为全自动物服
////        order.setTakeOverSubjectId(RCNAME);
//
//        logger.info("调用订单完成接口,orderId:{}, subOrderId:{}, type:{}, receiveCount:{}, remark:{}",
//                new Object[]{mainOrderId, subOrderId, gtrType, receiveCount, remark});
//
//        DeliveryOrderGTRType type = DeliveryOrderGTRType.getTypeByCode(backType);
//        // 更新子订单数据
//        DeliverySubOrder dbSubOrder = updateSubOrderInfo(subOrderId, type, gtrType, receiveCount, remark);
//
//        // 更新主订单，有需要再生成配单
//        DeliveryOrder mainOrder = updateMainOrder(dbSubOrder, receiveCount, type, remark);
//
//        // 写入订单日志
//        String log = "";
//        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
//            log = "交易成功";
//        } else if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
//            log = "交易结束，已部分完单";
//        } else {
//            remark = StringUtils.isBlank(remark) ? "游戏异常" : remark;
//            log = remark;
//        }
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(mainOrder.getOrderId());
//        orderLog.setLog(log);
//        orderLog.setType(OrderLog.TYPE_NORMAL);
//        deliveryOrderLogManager.writeLog(orderLog);
//
//        // 写入日志
//        StringBuilder sb = new StringBuilder("系统信息：订单数量").append(mainOrder.getCount());
//        sb.append("，已收货数量").append(mainOrder.getRealCount()).append("，未交易数量");
//        sb.append(mainOrder.getCount() - mainOrder.getRealCount());
//        orderLog = new OrderLog();
//        orderLog.setOrderId(dbSubOrder.getOrderId());
//        orderLog.setLog(sb.toString());
//        orderLog.setType(OrderLog.TYPE_NORMAL);
//        deliveryOrderLogManager.writeLog(orderLog);
//
//        // 更新游戏账号信息
//        deliveryOrderManager.updateGameAccountInfo(dbSubOrder, type, offline);
//
//        GameAccount gameAccount = null;
//        if (receiveCount > 0) {
//            //更新帐号库存
//            gameAccountManager.addRepositoryCount(dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameRace(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole(), receiveCount, true);
//
//            List<GameAccount> gameAccountList = gameAccountManager.queryGameAccount(dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole());
//            if (gameAccountList != null && gameAccountList.size() > 0) {
//                gameAccount = gameAccountList.get(0);
//            }
//        }
//
//        // 交易完成或部分完单的，生成分仓请求，资金结算
//        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode() || mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
////            // 查询该订单下的账号信息
////            List<DeliverySubOrder> list = deliverySubOrderDao.queryAllByOrderId(mainOrderId);
////            for (DeliverySubOrder subOrder : list) {
////                // 交易完成后，生成分仓请求
////                splitRepositoryRequestManager.create(subOrder);
////            }
//
//            // 资金结算
//            deliveryOrderManager.settlement(mainOrder.getOrderId());
//        }
//        return gameAccount;
//    }

    /**
     * 交易取消
     *
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param backType
     * @param remark
     */
    @Override
    @Transactional
    public void cancel(String mainOrderId, Long subOrderId, String gtrType, Integer backType, String remark, Boolean offline) {
        if (StringUtils.isBlank(mainOrderId) || subOrderId == null)
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        DeliveryOrder order = deliveryOrderDao.selectByOrderId(mainOrderId);
        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode() && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
        }
        if (gtrType == null)
            throw new SystemException(ResponseCodes.EmptyType.getCode(), ResponseCodes.EmptyType.getMessage());

        logger.info("调用取消接口,orderId:{}, subOrderId:{}, type:{}, remark:{}, offline:{}",
                new Object[]{mainOrderId, subOrderId, gtrType, remark, offline});

        DeliveryOrderGTRType type = DeliveryOrderGTRType.getTypeByCode(backType);
        // 更新子订单数据
        DeliverySubOrder dbSubOrder = updateSubOrderInfo(subOrderId, type, gtrType, 0L, remark);
        DeliveryOrder mainOrder = updateMainOrder(dbSubOrder, 0L, type, remark);
//        // 更新主订单，有需要再生成配单
//        DeliveryOrder mainOrder = deliveryOrderDao.selectByIdForUpdate(dbSubOrder.getChId());

        // 写入日志
        StringBuilder sb = new StringBuilder("系统信息：交易取消," + remark);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(dbSubOrder.getOrderId());
        orderLog.setLog(sb.toString());
        orderLog.setType(OrderLog.TYPE_NORMAL);
        deliveryOrderLogManager.writeLog(orderLog);

        // 更新游戏账号信息
        deliveryOrderManager.updateGameAccountInfo(dbSubOrder, type, offline);

        // 交易取消，解冻资金
        if (mainOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
            //如果开通了新资金，还需解冻7bao的资金  ZW_C_JB_00021 340096-20170825
            String uid = mainOrder.getBuyerUid();
            String buyerAccount = mainOrder.getBuyerAccount();
            SellerDTO seller = sellerDTOdao.findByAccountAndUid(buyerAccount, uid);
            if (mainOrderId.contains(OrderPrefix.NEWORDERID.getName())){
                DeliveryOrder deliveryOrder = deliveryOrderManager.getByOrderId(mainOrderId);
                fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER,deliveryOrder,mainOrder.getAmount());
            }else {
                //未开通新资金继续走原有资金
                fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, mainOrder.getBuyerAccount(),
                        mainOrder.getOrderId(), mainOrder.getAmount());
            }
        }
    }

    /**
     * 更新前台数量
     *
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param receiveCount
     */
    @Override
    @Transactional
    public void chanageReceiveCount(String mainOrderId, Long subOrderId, String gtrType, Long receiveCount, Boolean offline) {
        if (StringUtils.isBlank(mainOrderId) || subOrderId == null)
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        DeliveryOrder order = deliveryOrderDao.selectByOrderId(mainOrderId);
        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode() && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
        }
        if (gtrType == null)
            throw new SystemException(ResponseCodes.EmptyType.getCode(), ResponseCodes.EmptyType.getMessage());

        if (receiveCount == null || receiveCount.longValue() < 0)
            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(),
                    ResponseCodes.EmptyReceivingCount.getMessage());

        logger.info("调用订单更新数量接口,orderId:{}, subOrderId:{}, type:{}, receiveCount:{}",
                new Object[]{mainOrderId, subOrderId, gtrType, receiveCount});

        if (gtrType.equals(ShServiceType.UPDATE_STORE.getCode())) {
            // 只更新收货数量
            deliveryOrderManager.changeReceiveCount(mainOrderId, subOrderId, receiveCount);
        }
    }

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
    @Override
    @Transactional
    public GameAccount manual(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) {
        if (StringUtils.isBlank(mainOrderId) || subOrderId == null)
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        DeliveryOrder order = deliveryOrderDao.selectByOrderId(mainOrderId);
        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode() && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
        }
        if (gtrType == null)
            throw new SystemException(ResponseCodes.EmptyType.getCode(), ResponseCodes.EmptyType.getMessage());

        if (receiveCount == null || receiveCount.longValue() < 0)
            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(),
                    ResponseCodes.EmptyReceivingCount.getMessage());

        logger.info("调用人工介入接口,orderId:{}, subOrderId:{}, type:{}, receiveCount:{}, remark:{}",
                new Object[]{mainOrderId, subOrderId, gtrType, receiveCount, remark});

        DeliveryOrderGTRType type = DeliveryOrderGTRType.getTypeByCode(backType);
        // 更新子订单数据
        DeliverySubOrder dbSubOrder = updateSubOrderInfo(subOrderId, type, gtrType, receiveCount, remark);

        // 更新主订单，有需要再生成配单
        DeliveryOrder mainOrder = updateMainOrder(dbSubOrder, receiveCount, type, remark);

        // 写入订单日志
        String log = "";
        if (mainOrder.getStatus() == DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
            log = "交易结束，需人工介入";
        } else {
            remark = StringUtils.isBlank(remark) ? "游戏异常" : remark;
            log = remark;
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(mainOrder.getOrderId());
        orderLog.setLog(log);
        orderLog.setType(OrderLog.TYPE_NORMAL);
        deliveryOrderLogManager.writeLog(orderLog);

        // 写入日志
        StringBuilder sb = new StringBuilder("系统信息：订单数量").append(mainOrder.getCount());
        sb.append("，已收货数量").append(mainOrder.getRealCount()).append("，未交易数量");
        sb.append(mainOrder.getCount() - mainOrder.getRealCount());
        orderLog = new OrderLog();
        orderLog.setOrderId(dbSubOrder.getOrderId());
        orderLog.setLog(sb.toString());
        orderLog.setType(OrderLog.TYPE_NORMAL);
        deliveryOrderLogManager.writeLog(orderLog);

        // 更新游戏账号信息
        deliveryOrderManager.updateGameAccountInfo(dbSubOrder, type, offline);

        GameAccount gameAccount = null;
        if (receiveCount > 0) {
            //更新帐号库存
            gameAccountManager.addRepositoryCount(dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameRace(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole(), receiveCount, true);

            List<GameAccount> gameAccountList = gameAccountManager.queryGameAccount(dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole());
            if (gameAccountList != null && gameAccountList.size() > 0) {
                gameAccount = gameAccountList.get(0);
            }
        }
        return gameAccount;
    }

    /**
     * 更新子订单数据
     *
     * @param id
     * @param type
     * @param receiveCount
     * @param remark
     */
    private DeliverySubOrder updateSubOrderInfo(long id, DeliveryOrderGTRType backType, String type, Long receiveCount,
                                                String remark) {
        DeliverySubOrder dbSubOrder = deliverySubOrderDao.selectByIdForUpdate(id);

        if (dbSubOrder == null) {
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                    ResponseCodes.NoSubOrder.getMessage());
        }
        // 只有交易中或申请部分完单的订单，才可以调用
        if (dbSubOrder.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
                && dbSubOrder.getStatus().intValue() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode())
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(),
                    ResponseCodes.OrderStatusHasChangedError.getMessage());

        // 设置订单状态
        if (type.equals(ShServiceType.MANUAL_INTERVENTION.getCode())) {
            // 人工介入
            dbSubOrder.setStatus(DeliveryOrderStatus.MANUAL_INTERVENTION.getCode());
        } else {
            if (receiveCount.longValue() == 0) {
                dbSubOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
            } else if (receiveCount.longValue() >= dbSubOrder.getCount().longValue()) {
                dbSubOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
            } else if (receiveCount.longValue() < dbSubOrder.getCount().longValue()) {
                dbSubOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            }
        }

        // 设置原因
        dbSubOrder.setReason(backType.getCode());
        dbSubOrder.setOtherReason(backType.getMessage());
        dbSubOrder.setRealCount(receiveCount);
        dbSubOrder.setUpdateTime(new Date());

        // 更新子订单数据
        deliverySubOrderDao.update(dbSubOrder);

        // 写入日志
        StringBuilder sb = new StringBuilder();
        sb.append("订单号：").append(dbSubOrder.getOrderId()).append("_").append(dbSubOrder.getId());
        sb.append("，GTR返回：").append(backType.getCode()).append("/").append(backType.getMessage());
        if (dbSubOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE.getCode()) {
            sb.append(",本角色交易完成");
        } else if (dbSubOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
            sb.append(",部分完单");
        } else if (dbSubOrder.getStatus().intValue() == DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
            sb.append(",人工介入");
        } else if (dbSubOrder.getStatus().intValue() == DeliveryOrderStatus.CANCEL.getCode()) {
            sb.append(",撤单");
        } else {
            sb.append(",其他情况");
        }
        sb.append(",收到数量：").append(receiveCount).append(",").append(remark);

        OrderLog log = new OrderLog();
        log.setOrderId(dbSubOrder.getOrderId());
        log.setLog(sb.toString());
        log.setType(OrderLog.TYPE_INNER);
        deliveryOrderLogManager.writeLog(log);

        if (dbSubOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
            // 写入日志
            sb = new StringBuilder("系统信息：本角色已收满，本次收货数量为").append(dbSubOrder.getRealCount());
            log = new OrderLog();
            log.setOrderId(dbSubOrder.getOrderId());
            log.setLog(sb.toString());
            log.setType(OrderLog.TYPE_NORMAL);
            deliveryOrderLogManager.writeLog(log);
        }

        return dbSubOrder;
    }

    /**
     * 更新主订单，生成配单
     *
     * @param deliverySubOrder
     * @param receiveCount
     * @param type
     * @param remark
     */
    private DeliveryOrder updateMainOrder(DeliverySubOrder deliverySubOrder, long receiveCount,
                                          DeliveryOrderGTRType type, String remark) {
        DeliveryOrder mainOrder = deliveryOrderDao.selectByIdForUpdate(deliverySubOrder.getChId());

        // 增加主出货单实际出货数量，计算出货金额
        if (receiveCount > 0) {
            long realCount = 0;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", deliverySubOrder.getOrderId());
            List<DeliverySubOrder> subOrders = deliverySubOrderDao.selectByMap(params);
            for (DeliverySubOrder so : subOrders) {
                realCount += so.getRealCount();
            }

            mainOrder.setRealCount(realCount);
            BigDecimal realAmount = null;
            if (mainOrder.getRealCount().compareTo(mainOrder.getCount()) == 1) {
                realAmount = mainOrder.getPrice().multiply(new BigDecimal(mainOrder.getCount().toString()));
            } else {
                realAmount = mainOrder.getPrice().multiply(new BigDecimal(String.valueOf(realCount)));
            }
            realAmount = realAmount.setScale(2, RoundingMode.DOWN);

            // 实际金额大于原先出货金额的，以原先出货金额为准打款
            if (realAmount.compareTo(mainOrder.getAmount()) == 1) {
                realAmount = mainOrder.getAmount();
            }
            mainOrder.setRealAmount(realAmount);
        }

        // 已经申请部分完单的，立即结束交易
        if (mainOrder.getStatus().intValue() == DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
            // 交易异常结束
            tradeException(mainOrder, deliverySubOrder, type, remark);
        } else {
            /*
            1.如果是需要人工介入，则更新主订单的状态为需人工介入
            2.如果是交易超时，则更新主订单的状态为撤单、部分完单或交易完成
            最后将该主订单下所有交易中的子订单更新为撤单
            */
            switch (type) {
                case MANUAL_INTERVENTION:
                case EXCEPTION_TIMEOUT_MANUAL:
                    //ZW_C_JB_00004 jiyx 当订单是自动化异常的时候直接跑出
                case AUTOMETA_CONTINUE:
                case TRADE_TIMEOUT: {
                    logger.info("交易异常结束,orderId:{}, subOrderId:{}, type:{}",
                            new Object[]{mainOrder.getOrderId(), deliverySubOrder.getId(), type});
                    // 交易异常结束
                    tradeException(mainOrder, deliverySubOrder, type, remark);
                    break;
                }
                default: {
                    logger.info("交易正常结束,orderId:{}, subOrderId:{}, type:{}",
                            new Object[]{mainOrder.getOrderId(), deliverySubOrder.getId(), type});
                    // 交易正常结束
                    tradeComplete(mainOrder, deliverySubOrder);
                }
            }
        }
        return mainOrder;
    }

    /**
     * 交易异常结束
     *
     * @param mainOrder
     * @param deliverySubOrder
     * @param type
     * @param remark
     */
    private void tradeException(DeliveryOrder mainOrder, DeliverySubOrder deliverySubOrder,
                                DeliveryOrderGTRType type, String remark) {
         /*
            1.如果是需要人工介入，则更新主订单的状态为需人工介入
            2.如果是交易超时，则更新主订单的状态为撤单、部分完单或交易完成
            最后将该主订单下所有交易中的子订单更新为撤单
        */

        deliveryOrderManager.cancelAllSubOrder(mainOrder.getOrderId(), deliverySubOrder.getId(), type);
        int orderStatus = 0;
        // 设置订单状态
        if (type == DeliveryOrderGTRType.MANUAL_INTERVENTION || type==DeliveryOrderGTRType.EXCEPTION_TIMEOUT_MANUAL) {
            mainOrder.setStatus(DeliveryOrderStatus.MANUAL_INTERVENTION.getCode());
            orderStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
        } else {
            if (mainOrder.getRealCount().longValue() == 0) {
                mainOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
                orderStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
            } else if (mainOrder.getRealCount().longValue() >= mainOrder.getCount().longValue()) {
                mainOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
                orderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            } else if (mainOrder.getRealCount().longValue() < mainOrder.getCount().longValue()) {
                mainOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
                orderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            }
        }

        mainOrder.setReason(type.getCode());
        mainOrder.setOtherReason(remark);
        mainOrder.setTradeEndTime(new Date());
        deliveryOrderDao.update(mainOrder);
        //订单状态改变同步通知主站订单中心
        asyncPushToMainMehtods.orderPushToMain(mainOrder,orderStatus);
    }

    /**
     * 交易正常结束
     *
     * @param mainOrder
     * @param deliverySubOrder
     */
    private void tradeComplete(DeliveryOrder mainOrder, DeliverySubOrder deliverySubOrder) {
        int orderCenterStatus = 0;
        // 检查子订单是否已经全部交易完成
        if (checkSubOrderIsAllComplete(deliverySubOrder.getChId())) {
            // 判断还需不需要进行配单，不需要配单或配不出单了的则认为该订单交易结束了
            boolean isAllComplete = true;
            long needCount = mainOrder.getCount() - mainOrder.getRealCount(); // 需要配单的数量
            if (needCount > 0) {
                // 开始配单
                long createTotalCount = autoConfigManager.configOrder(mainOrder, needCount);
                if (createTotalCount > 0) {
                    // 有创建配单记录，则继续进行交易
                    isAllComplete = false;

                    StringBuilder sb = new StringBuilder("系统信息：收货商正在为您再次分配收货角色");
                    OrderLog log = new OrderLog();
                    log.setOrderId(mainOrder.getOrderId());
                    log.setLog(sb.toString());
                    log.setType(OrderLog.TYPE_NORMAL);
                    deliveryOrderLogManager.writeLog(log);
                }
            }

            if (isAllComplete) {
                int status;
                
                if (mainOrder.getRealCount().longValue() == 0) {
                    status = DeliveryOrderStatus.CANCEL.getCode();
                    orderCenterStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
                }
                else if (mainOrder.getRealCount().longValue() >= mainOrder.getCount().longValue()) {
                    status = DeliveryOrderStatus.COMPLETE.getCode();
                    orderCenterStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
                }
                else {
                    status = DeliveryOrderStatus.COMPLETE_PART.getCode();
                    orderCenterStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
                }
                mainOrder.setStatus(status);
                mainOrder.setTradeEndTime(new Date());
            }
        } else {
            StringBuilder sb = new StringBuilder("系统信息：收货商正在为您再次分配收货角色");
            OrderLog log = new OrderLog();
            log.setOrderId(mainOrder.getOrderId());
            log.setLog(sb.toString());
            log.setType(OrderLog.TYPE_NORMAL);
            deliveryOrderLogManager.writeLog(log);
        }
        deliveryOrderDao.update(mainOrder);
        asyncPushToMainMehtods.orderPushToMain(mainOrder,orderCenterStatus);
        //订单状态改变 同步通知主站订单中心
        int orderCenterOrderStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
        asyncPushToMainMehtods.orderPushToMain(mainOrder,orderCenterOrderStatus);
    }

    /**
     * 检查订单是否还需要进行配单，需要配单则生成配单记录，最终返回这个主订单下的所有子订单是否交易完成
     * <p>规则：只有所有子订单"交易完成"了（包括等待交易和交易中的子订单），才判断是否需要配单
     *
     * @param chId 出货单ID
     * @return boolean 返回这个主订单下的所有子订单是否交易完成
     */
    private boolean checkSubOrderIsAllComplete(Long chId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chId", chId);
        params.put("multiStatus", DeliveryOrderStatus.WAIT_TRADE.getCode() + "," + DeliveryOrderStatus.TRADING.getCode());
        // 查询这笔出货主订单下，还有几笔子订单在交易中
        int count = deliverySubOrderDao.countByMap(params);
        // 如果还有子订单在交易中的，先退出不配单
        if (count != 0) return false;
        return true;
    }


}

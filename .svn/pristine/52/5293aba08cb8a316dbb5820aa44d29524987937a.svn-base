package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 340096
 * @date 2017/12/25.
 */
@Component("autoTimeOutOrderForMachineDeliveryJob")
public class AutoTimeOutOrderForMachineDeliveryJob {

    protected static final Logger logger = LoggerFactory.getLogger(AutoTimeOutOrderForMachineDeliveryJob.class);

    private static final String JOB_ID = "SH_AUTO_TIMEOUT_MACHINE_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IDeliverySubOrderManager deliverySubOrderManager;
    @Autowired
    IDeliveryOrderLogManager orderLogManager;
    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 自动设置机器收货订单超时，每隔1分钟执行一次
     */
    public void autoTimeOutMachineDeliveryOrder() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            return;
        }
        try {
//            // 查询交易超时的订单
//            List<String> orderIds = deliveryOrderManager.queryMachineDeliveryTimeoutOrders();
//            if (!CollectionUtils.isEmpty(orderIds)) {
//                for (String orderId : orderIds) {
//                    try {
//                        //超时转物服处理
//                        int source = 1;
//                        deliveryOrderManager.autoDistributionManager(orderId, source);
//                    } catch (Exception e) {
//                        logger.error("检测机器收货订单，交易中是否超时JOB出错了", e);
//                    }
//                }
//            }
            logger.info("超时转物服订单处理" + new Date());
            List<Long> subIds = deliverySubOrderManager.queryMachineSellerDeliveryTimeoutOrders();
            logger.info("自动化超时准备转人工，订单号：{}", subIds);
            if (!CollectionUtils.isEmpty(subIds)) {
                List<DeliverySubOrder> deliverySubOrderList = new ArrayList<DeliverySubOrder>();
                try {
                    for (Long orderId : subIds) {
                        Boolean flag = orderLogManager.isHaveLog(orderId);
                        if (!flag) {
                            logger.error("检测机器收货订单，该订单无需转人工，{}", orderId);
                            continue;
                        }

                        //邮寄收货自动化超时转物服处理
                        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(orderId);
                        logger.info("自动化超时，当前子订单信息：{}", subOrder);
                        if (subOrder != null) {
                            List<DeliverySubOrder> deliverySubOrderTemp = deliverySubOrderDao.queryAllByOrderForUpdate(subOrder.getOrderId(), subOrder.getBuyerAccount(), subOrder.getGameAccount());
                            logger.info("自动化超时，获取同账号订单,deliverySubOrderTemp：{}", deliverySubOrderTemp);
                            if (deliverySubOrderTemp != null && deliverySubOrderTemp.size() > 0) {
                                deliverySubOrderList.addAll(deliverySubOrderTemp);
                            }
                        }
                    }

                    logger.info("自动化超时,deliverySubOrderList：{}", deliverySubOrderList);
                    if (deliverySubOrderList.size() > 0) {
                        Integer exceptionFromAuto = 2;
                        for (DeliverySubOrder deliverySubOrder : deliverySubOrderList) {
                            try {
                                String otherReason = "由于系统长时间未响应，无法完成自动收货，订单转人工处理。";
                                logger.info("自动化超时开始转人工：{}", deliverySubOrder);
                                deliveryOrderManager.subOrderAutoDistributionManager(deliverySubOrder.getId(), exceptionFromAuto, otherReason);
                            }catch (Exception e){
                                logger.error("检测机器收货自动化响应超时订单，交易中是否超时开始转人工JOB出错了", e);
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("检测机器收货自动化响应超时订单，交易中是否超时JOB出错了", e);
                }
            }

            // 邮寄交易与拍卖交易  30分钟没有操作,转人工
            logger.info("超时转物服订单处理" + new Date());
            List<Long> SubOrders = deliverySubOrderManager.queryAllMachineSellerDeliveryTimeoutOrders();
            logger.info("自动化超时准备转人工，订单号：{}", SubOrders);
            if (!CollectionUtils.isEmpty(SubOrders)) {
                for (long id : SubOrders) {
                    try {
                        //邮寄收货自动化超时转物服处理
                        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(id);
                        //超时转物服处理
                        int source = 1;
                        deliveryOrderManager.autoDistributionManager(subOrder.getOrderId(), source,subOrder.getId());
                    } catch (Exception e) {
                        logger.error("检测机器收货订单，交易中是否超时JOB出错了", e);
                    }
                }
            }
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}

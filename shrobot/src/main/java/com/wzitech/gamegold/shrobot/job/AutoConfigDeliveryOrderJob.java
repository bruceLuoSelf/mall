//package com.wzitech.gamegold.shrobot.job;
//
//import com.wzitech.gamegold.shorder.business.IDeliveryOrderAutoConfigManager;
//import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * 给排队中的出货单自动配单JOB
// *
// * @author yemq
// */
//@Component
//public class AutoConfigDeliveryOrderJob {
//    protected static final Logger logger = LoggerFactory.getLogger(AutoConfigDeliveryOrderJob.class);
//
//    private static final String JOB_ID = "SH_AUTO_CONFIG_DELIVERY_ORDER_JOB";
//
//    @Autowired
//    IDeliveryOrderManager deliveryOrderManager;
//
//    @Autowired
//    IDeliveryOrderAutoConfigManager autoConfigManager;
//
//    @Resource(name = "jobLock")
//    JobLockRedisImpl jobLock;
//
//    /**
//     * 自动配单，每隔10秒执行一次
//     */
//    public void autoConfig() {
////        jobLock.unlock(JOB_ID);
//        Boolean locked = jobLock.lock(JOB_ID);
//        if (!locked) {
//            //logger.info("[给排队中的出货单自动配单JOB]上一个任务还未执行完成。");
//            return;
//        }
//        try {
//            //logger.info("[给排队中的出货单自动配单JOB]任务开始");
//            // 查询等待排队的订单
//            List<Long> orderIds = deliveryOrderManager.queryInQueueOrderIds();
//            //logger.info("[给排队中的出货单自动配单JOB]需要配单的数量：{}", orders == null ? 0 : orders.size());
//            if (!CollectionUtils.isEmpty(orderIds)) {
//                for (Long orderId : orderIds) {
//                    try {
////                        autoConfigManager.autoConfigOrder(orderId);
//                    }/* catch (NotEnoughRepertoryException e){
//                        //撤单
//                        deliveryOrderManager.cancelOrderByNotEnoughRepertory(orderId, DeliveryOrder.OHTER_REASON,
//                                NotEnoughRepertoryException.MESSAGE);
//                        continue;
//                    }*/ catch (Exception e) {
//                        logger.error("给排队中的出货单自动配单JOB出错了", e);
//                    }
//                }
//            }
//            //logger.info("[给排队中的出货单自动配单JOB]任务结束");
//        } finally {
//            jobLock.unlock(JOB_ID);
//        }
//    }
//}

package com.wzitech.gamegold.shrobot.job;

//import com.wzitech.gamegold.common.enums.DeliveryOrderGTRType;
//import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
//import com.wzitech.gamegold.common.enums.ShServiceType;
//import com.wzitech.gamegold.common.enums.SystemConfigEnum;
//import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
//import com.wzitech.gamegold.common.expection.OrderToCompletePartException;
//import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
//import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
//import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
//import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
//import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
//import com.wzitech.gamegold.shorder.entity.SystemConfig;
//import com.wzitech.gamegold.store.business.IShStoreManager;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by 340082 on 2017/5/11.
// *
// * ZW_C_JB_00004 jiyx
// */
//@Component("automateTimeout")
//public class AutomateTimeout {
//    protected static final Log log = LogFactory.getLog(AutomateTimeout.class);
//    //当与配单逻辑并行是有问题
//    private static final String JOB_ID = "AUTOMATE_TIMEOUT";
//    //异常超时部分完单
//    private static String orderToCompletePart = "交易超时部分完单";
//    @Resource(name = "jobLock")
//    JobLockRedisImpl jobLock;
//
//    @Autowired
//    private ISystemConfigManager systemConfigManager;
//
//    @Autowired
//    private IDeliveryOrderManager deliveryOrderManager;
//
//    @Autowired
//    private IShStoreManager shStoreManager;
//
//    @Autowired
//    private IDeliverySubOrderManager deliverySubOrderManager;
//
//    /**
//     * 自动化转人工（每3分钟，执行一次）
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void autoPlay() throws IOException {
////        jobLock.unlock(JOB_ID);  //测试用 其他注释
//        Boolean locked = jobLock.lock(JOB_ID);
//
//        if (!locked) {
//            log.info("上一个定时器还未执行完成。");
//            return;
//        }
//        try {
//            //获取超时配置
//            SystemConfig automateTimeout = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.AUTOMATE_TIMEOUT.getKey());
//            if (automateTimeout == null || !automateTimeout.getEnabled()) {
//                log.info("自动化收货异常转人工超时配置没有配置，或没有启用");
//                return;
//            }
//            String configValue = automateTimeout.getConfigValue();
//            int minute = Integer.parseInt(configValue);
//            //获取超时的游戏
//            List<Long> deliveryOrderIds = deliveryOrderManager.selectByAutometaTimeout(minute);
//            for (int i = 0; i < deliveryOrderIds.size(); i++) {
//                try {
//                    log.info("自动化收货异常转人工超时开始：订单号" + deliveryOrderIds.get(i) + "");
//                    deliveryOrderManager.createSubOrderByAutoMateError(deliveryOrderIds.get(i), minute);
//                } catch (NotEnoughRepertoryException e) {
//                    //撤单
//                    deliveryOrderManager.cancelOrderByNotEnoughRepertory(deliveryOrderIds.get(i), DeliveryOrder.OHTER_REASON,
//                            NotEnoughRepertoryException.MESSAGE);
//                    log.info("自动化收货异常转数量不足撤单开始：+" + deliveryOrderIds.get(i) + "");
//                    continue;
////                } catch (OrderToCompletePartException e) {
////                    //当跑出改错误的时候进行自动化部分完单处理
////                    List<DeliverySubOrder> deliverySubOrders = deliverySubOrderManager.querySubOrdersByOrderId(deliveryOrderIds.get(i));
////                    for (DeliverySubOrder d : deliverySubOrders) {
////                        if (d.getStatus().equals(DeliveryOrderStatus.TRADING.getCode())) {
////                            shStoreManager.complete(d.getOrderId(), d.getId(), ShServiceType.COMPLETE_PART.getCode(), DeliveryOrderGTRType.AUTOMETA_CONTINUE.getCode(), 0L, AutomateTimeout.orderToCompletePart, false);
////                            break;
////                        }
////                    }
////                    continue;
//                }
//            }
//            log.info("自动化订单超时转人工job结束");
//        } finally {
//            jobLock.unlock(JOB_ID);
//        }
//    }
//}

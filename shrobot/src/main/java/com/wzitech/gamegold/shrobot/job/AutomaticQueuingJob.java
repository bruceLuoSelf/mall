package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderAutoConfigManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.impl.DeliveryOrderAutoConfigManagerImpl;
import com.wzitech.gamegold.shorder.dao.IAutomaticQueuingRedis;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.impl.AutomaticQueuingRedisImpl;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 340082 on 2017/12/22.
 * 排队订单生成job
 *
 */
@Component("automaticQueuingJob")
public class AutomaticQueuingJob {
    protected static final Log log = LogFactory.getLog(AutomaticQueuingJob.class);

    //当与配单逻辑并行是有问题
    private static final String JOB_ID = "AUTOMATIC_QUEUING";


    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    private IAutomaticQueuingRedis automaticQueuingRedis;

    @Autowired
    private IDeliveryOrderAutoConfigManager deliveryOrderAutoConfigManager;

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    /**
     * 自动化排队订单生成（每?分钟，执行一次）
     */
    public void autoPlay() {
//        jobLock.unlock(JOB_ID);
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            log.info("上一个自动化排队订单生成定时器还未执行完成。");
            return;
        }
        log.info("自动化排队订单生成开始执行时间:"+new Date());
        try {
            //先将开始标识删除
            automaticQueuingRedis.remOrderid(AutomaticQueuingRedisImpl.START_STRING);
            //添加开始标识
            automaticQueuingRedis.pushOrderId(AutomaticQueuingRedisImpl.START_STRING);
            String orderid = automaticQueuingRedis.getLastOrderid();
            while (orderid != null){
                if(AutomaticQueuingRedisImpl.START_STRING.equals(orderid)){
                    break;
                }
                DeliveryOrder byOrderId = deliveryOrderManager.getByOrderId(orderid);
                try{
                    if(byOrderId == null || byOrderId.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()){
                        orderid = automaticQueuingRedis.getLastOrderid();
                        continue;
                    }
                    // 写入订单日志
                    if(byOrderId.getQueueStartTime() == null){
                        OrderLog orderLog = new OrderLog();
                        orderLog.setOrderId(byOrderId.getOrderId());
                        orderLog.setType(OrderLog.TYPE_NORMAL);
                        orderLog.setLog("当前收货商正在交易中，无空闲帐号。因机器收货暂不支持同时多人交易，等待交易完成。。。。排队结束后，显示收货角色名和等级。");
                        deliveryOrderLogManager.writeLog(orderLog);
                        orderLog.setLog("如要退出排队，请点击左侧【撤单】按钮，订单将撤单。");
                        deliveryOrderLogManager.writeLog(orderLog);
                        byOrderId.setQueueStartTime(new Date());
                        deliveryOrderDao.update(byOrderId);
                    }
                    //对申诉单的配单和正常订单配单分开处理
                    if(StringUtils.isBlank(byOrderId.getAppealOrder())){
                        deliveryOrderAutoConfigManager.autoConfigOrder(byOrderId);
                    }else {
                        deliveryOrderAutoConfigManager.autoConfigAppealOrder(byOrderId);
                    }
                }catch (NotEnoughRepertoryException e) {
                    //撤单
                    deliveryOrderManager.cancelOrderByNotEnoughRepertory(orderid, DeliveryOrder.OHTER_REASON,
                            NotEnoughRepertoryException.MESSAGE);
                    log.info("自动化收货异常转数量不足撤单开始：+" + orderid + "");
                    orderid = automaticQueuingRedis.getLastOrderid();
                    continue;
                }
                orderid = automaticQueuingRedis.getLastOrderid();
            }
        }catch (Exception e){
            log.info("自动化排队订单生成系统异常：{}",e);
        }finally {
            //将排队中删除
            automaticQueuingRedis.remOrderid(AutomaticQueuingRedisImpl.START_STRING);
            jobLock.unlock(JOB_ID);
        }



    }

}

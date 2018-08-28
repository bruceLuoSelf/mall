package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderAutoConfigManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IAutomaticQueuingRedis;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
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
@Component
@JobHandler("automaticQueuingJob")
public class AutomaticQueuingJob extends IJobHandler{
    protected static final Log log = LogFactory.getLog(AutomaticQueuingJob.class);

    //当与配单逻辑并行是有问题
    private static final String JOB_ID = "AUTOMATIC_QUEUING";

    private static final String START_STRING = "订单排队生成开始";

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

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            automaticQueuingRedis.pushOrderId(START_STRING);
            String orderid = automaticQueuingRedis.getLastOrderid();
            while (orderid != null){
                if(START_STRING.equals(orderid)){
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
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("automaticQueuingJob异常:{}",e);
            return FAIL;
        }
    }
}

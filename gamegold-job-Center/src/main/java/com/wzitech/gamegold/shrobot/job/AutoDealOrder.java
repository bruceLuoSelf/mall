package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2018/5/29.
 * 自动完单
 */
@Component
@JobHandler("autoDealOrder")
public class AutoDealOrder extends IJobHandler {

    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            //查询出所有十分钟交易完成的订单进行自动完单
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("waitToConfirm", true);
            //全自动返回信息后10分钟 之后的信息
            queryMap.put("updateTimeAddTenMinute", new Date(System.currentTimeMillis() - 10 * 60 * 1000));
            List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.selectByMap(queryMap);
            for (DeliverySubOrder deliverySubOrder : deliverySubOrders) {
                logger.info("处理未确认订单:"+deliverySubOrder.getId());
                Map<Long, Long> map = new HashMap<Long, Long>();
                map.put(deliverySubOrder.getId(), deliverySubOrder.getRealCount());
                Map<Long, String> cancelReason = new HashMap<Long, String>();
                if (deliverySubOrder.getShInputCount() == 0) {
                    cancelReason.put(deliverySubOrder.getId(), "出货商对订单无异议已逾10分钟");
                }
                deliveryOrderManager.handleOrderForMailDeliveryOrderMax(map, deliverySubOrder.getOrderId(), cancelReason, null,null);
                logger.info("订单:"+deliverySubOrder.getId()+"已自动完单完毕");
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoDealOrder异常:{}",e);
            return FAIL;
        }
    }
}

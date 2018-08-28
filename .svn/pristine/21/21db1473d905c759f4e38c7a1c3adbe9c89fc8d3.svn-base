package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ljn
 * @date 2018/5/29.
 * 自动取消交易超时的订单
 */
@Component
@JobHandler("autoCancelTimeoutOrder")
public class AutoCancelTimeoutOrder extends IJobHandler{

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            // 查询交易超时的订单
            List<Long> ids = deliveryOrderManager.queryTradeTimeoutOrders();
            if (!CollectionUtils.isEmpty(ids)) {
                for (Long id : ids) {
                    try {
                        deliveryOrderManager.cancelTimeoutOrder(id);
                    }catch (Exception e){
                        log.info(id +"：订单号交易超时订单出现异常：{}",e);
                        continue;
                    }
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoCancelTimeoutOrder异常:{}",e);
            return FAIL;
        }
    }
}

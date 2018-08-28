package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IAmoutHttp;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IReceiveMainManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/22.
 */
@Component
public class ReceiveMainManagerImpl implements IReceiveMainManager{
    //日志输出
    private static final Logger logger = LoggerFactory.getLogger(ReceiveMainManagerImpl.class);

    @Autowired
    private IFundManager fundManager;
    
    @Autowired
    private IAmoutHttp amoutHttp;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;
    /**
     * 给此单卖家加钱
     * */
    @Transactional
    public void plusAmount(DeliveryOrder deliveryOrder){
        String orderId = deliveryOrder.getOrderId();
        //新资金：更新此单卖家purchasdata
        boolean flag = fundManager.plusAmount(deliveryOrder);
        if (!flag){
            logger.error("给此单卖家加钱失败{}：",orderId);
            throw new SystemException(String.valueOf(ResponseCodes.FaildPlusAmount));
        }

    }
}

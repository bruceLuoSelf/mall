package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.IDeliveryOrderService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.RefundOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.RemoveDeliveryOrderRequest;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

/**
 * 撤单
 * Created by 340032 on 2017/12/27.
 */

@Service("RemoveDeliveryOrderService")
@Path("/RemoveShOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class RemoveDeliveryOrderImpl extends AbstractBaseService implements IDeliveryOrderService {


    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IDeliveryOrderDao deliveryOrderDao;

    @Override
    @GET
    @Path("/removeShOrder")
    public IServiceResponse removeShOrder(RemoveDeliveryOrderRequest request) {
        RefundOrderResponse response=new RefundOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            DeliverySubOrder subOrder=new DeliverySubOrder();
            if (request.getId()==null){
                responseStatus.setStatus(ResponseCodes.EmptyOrderId.getCode(),ResponseCodes.EmptyOrderId.getMessage());
            }
            if (request.getOrderId()==null){
                responseStatus.setStatus(ResponseCodes.EmptyOrderId.getCode(),ResponseCodes.EmptyOrderId.getMessage());
            }
            if (request.getShInputCount() !=null){
                subOrder.setShInputCount(0L);
            }
//            //查询主订单
//            DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(request.getOrderId());
//            if (!deliveryOrder.getId().equals(request.getId())){
//                responseStatus.setStatus(ResponseCodes.NotMatchError.getCode(),ResponseCodes.NotMatchError.getMessage());
//            }
            Map<Long,Long> subOrdersInfos=new HashMap<Long, Long>();
            subOrdersInfos.put(request.getId(),subOrder.getShInputCount());
            String mainOrderId=request.getOrderId();
            Map<Long,String> appealReason =new HashMap<Long, String>();
            appealReason.put(request.getId(),"出货商撤单!");
            //撤单
            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(subOrdersInfos,mainOrderId,appealReason,null,null);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
            logger.info("撤单成功!:{}",responseStatus);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

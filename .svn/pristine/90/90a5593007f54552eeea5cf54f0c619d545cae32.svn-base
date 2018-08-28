package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

/**
 * Created by chengXY on 2017/8/22.
 * 出货商开通收货时资金接入主站 主站操作后回调到此接口 该接口更新金币商城purchasedata余额
 */

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.frontend.service.shorder.IReceiveMainService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReceivceMainResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReceiveMainRequest;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.IReceiveMainManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;


@Service("ReceiveMain")
@Path("/receiveMain")
@Produces("application/json;charset=UTF-8")
public class ReceiveMainServiceImpl extends AbstractBaseService implements IReceiveMainService{

    //日志记录
    private static final Logger logger = LoggerFactory.getLogger(ReceiveMainServiceImpl.class);

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private IReceiveMainManager receiveMainManager;

    @Value("${receive.main.key}")
    private String receiveMainKey;

    @Path("/reduceAmount")
    @POST
    public HttpServletResponse plusAmount(@FormParam("") ReceiveMainRequest receiveMainRequest , @Context HttpServletRequest request,
                                          @Context HttpServletResponse response){
//        //取出主站传过来的orderId,查出出货单信息
//        String orderId = receiveMainRequest.getOut_trade_no();
//        try {
//            String needSignString = "out_trade_no="+receiveMainRequest.getOut_trade_no()
//                    +"&trading_type="+receiveMainRequest.getTrading_type()+"&total_fee="+
//                    receiveMainRequest.getTotal_fee();
//            String format = String.format("%s%s",needSignString,receiveMainKey);
//
//            logger.info("主站回调给卖家加钱签名:{}",format);
//            String toEncrypt = EncryptHelper.md5(format);
//            if (!StringUtils.equals(toEncrypt,receiveMainRequest.getSign())){
//                logger.error("签名不一致");
//                return response;
//            }
//        }catch (Exception e){
//            throw new SystemException("主站回调出错{}:",e);
//        }
//
//
//        DeliveryOrder deliveryOrder = deliveryOrderManager.selectByOrderIdForUpdate(orderId);
//        try{
//            //新资金：去金币商城purchasedata给此出货单的卖家加钱 新资金
//            receiveMainManager.plusAmount(deliveryOrder);
//            logger.info("给金币商城出货单{}：卖家加款成功",orderId);
//        }catch (Exception e){
//            logger.error("给金币商城出货单{}：卖家加款失败",orderId);
//        }

        try {
            String data = "true";
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] dataByteArr = data.getBytes();
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

package com.wzitech.gamegold.facade.frontend.service.chorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.facade.frontend.service.chorder.IDeliverySubOrderService;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliverySubOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliverySubOrderResponse;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IBalckListDao;
import com.wzitech.gamegold.shorder.dao.IRobotImgDAO;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.ShowUserImgEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jhlcitadmin on 2017/1/6.
 */
@Service("DeliverySubOrderService")
@Path("/deliveryOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class DeliverySubOrderServiceImpl extends AbstractBaseService implements IDeliverySubOrderService {
    @Autowired
    IDeliverySubOrderManager deliverySubOrderManager;
    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IDeliveryOrderLogManager deliveryOrderLogManager;
    @Autowired
    IGameAccountManager gameAccountManager;
    @Autowired
    IShGameConfigManager shGameConfigManager;
    @Autowired
    ISystemConfigManager systemConfigManager;
    @Autowired
    private IBalckListDao balckListDao;
    @Autowired
    private IRobotImgDAO robotImgDAO;
    /**
     * 查询所有数据
     * subOrderList add user input the count which he send to buyer
     *
     * @param orderId
     * @return
     */
    @Override
    @GET
    @Path("/selectDeliverySubOrderData")
    public IServiceResponse findAll(@QueryParam("orderId") String orderId) {
        DeliverySubOrderResponse response = new DeliverySubOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        List<DeliverySubOrderRequest> list = new ArrayList<DeliverySubOrderRequest>();
        try {
            if (null == orderId) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            }
            DeliveryOrder order = deliveryOrderManager.getByOrderId(orderId);
            if (null == order) {
                throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                        ResponseCodes.EmptyOrderInfo.getMessage());
            }
            //获取当前登录用户
            String loginAccount = CurrentUserContext.getUserLoginAccount();
            if (!(StringUtils.equals(loginAccount, order.getSellerAccount()) || StringUtils.equals(loginAccount, order.getBuyerAccount()))) {
                throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),
                        ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
            //获取所有子订单
            List<DeliverySubOrder> OrderList = deliverySubOrderManager.querySubOrdersByOrderId(order.getId());
            if (CollectionUtils.isNotEmpty(OrderList)) {
                Map<String,Object> map = new HashMap<String, Object>();
                for (DeliverySubOrder entity : OrderList) {
                    DeliverySubOrderRequest request = new DeliverySubOrderRequest();
                    map.clear();
                    map.put("subOrderId",entity.getId());
                    map.put("orderId",entity.getOrderId());
                    map.put("type", ShowUserImgEnum.SHOW_IMG.getCode());
                    List<RobotImgEO> robotImgEOs = robotImgDAO.selectByMap(map);
                    request.setRobotImgList(robotImgEOs);
                    request.setId(entity.getId());
                    request.setGameRole(entity.getGameRole());
                    request.setCount(entity.getCount());
                    request.setAfterFour(entity.getAfterFour());
                    if (entity.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()){
                        request.setRealCount(entity.getRealCount());
                    }else {
                        request.setRealCount(entity.getRealCount() == null ? 0 : entity.getRealCount());
                    }
                    request.setMachineArtificialStatus(entity.getMachineArtificialStatus());
                    request.setStatus(DeliveryOrderStatus.getNameByCode(entity.getStatus()));
                    request.setSubOrderStatus(entity.getStatus());
                    request.setSellerInputCount(entity.getShInputCount());
                    request.setRoleLevel(entity.getRoleLevel());
                    request.setAppealOrder(entity.getAppealOrder());
                    request.setWaitToConfirm(entity.getWaitToConfirm());
                    if (StringUtils.isNotBlank(entity.getAppealOrder())){
                        DeliverySubOrder subOrderByAppealOrder = deliverySubOrderManager.findSubOrderByAppealOrder(entity.getAppealOrder());
                        if (subOrderByAppealOrder != null) {
                            request.setSubOrderCorrespondingAppealOrderId(subOrderByAppealOrder.getOrderId());
                        }
                    }
                    if (StringUtils.isBlank(entity.getAppealOrder()) && order.getTradeEndTime() != null) {
                        //is it allowed to operate
                        Boolean whetherOverTime = System.currentTimeMillis() - order.getTradeEndTime().getTime() - systemConfigManager.getOverTime() > 0;
                        if (whetherOverTime) {
                            //overtime forbid appeal
                            request.setAppealOrderStatus(AppealOrderStatus.OVERTIME.getCode());
                        } else {
                            request.setAppealOrderStatus(AppealOrderStatus.ALLOWAPPEAL.getCode());
                        }
                    } else {
                        //entity.getAppealOrder isNotBlank ,representative has already complained, and takes the status from appealOrder directly.
                        request.setAppealOrderStatus(entity.getAppealOrderStatus());
                    }
                    list.add(request);
                }
            }
            //是否黑名单用户
            BlackListEO userByLoginAccount = balckListDao.blackListAccount(loginAccount, false);
            if (userByLoginAccount!=null){
                response.setBlackUser(userByLoginAccount.getLoginAccount());
            }
            //获取收货角色
            Set<String> roleNames = new HashSet<String>();
            roleNames = gameAccountManager.selectRoleNames(order.getBuyerAccount(),
                    order.getGameName(), order.getRegion(), order.getServer());
            if (null == order.getMoneyName()) {
                order.setMoneyName("");
            }
            response.setRoleNames(roleNames);
            response.setSubOrderList(list);
            response.setDeliveryOrder(order);
            response.setOrderStatus(DeliveryOrderStatus.getNameByCode(order.getStatus()));
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("创建出货单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            logger.error("创建出货单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 根据子订单id查询子订单详情
     */
    @Override
    @GET
    @Path("/findSubOrderById")
    public IServiceResponse findSubOrderById(@QueryParam("id") Long id) {
        DeliverySubOrderResponse response = new DeliverySubOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (null == id) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            }
            List<DeliverySubOrderRequest> list = new ArrayList<DeliverySubOrderRequest>();
            DeliverySubOrder entity = deliverySubOrderManager.findSubOrderById(id);
            //获取当前登录用户
            String loginAccount = CurrentUserContext.getUserLoginAccount();
            if (!(StringUtils.equals(loginAccount, entity.getSellerAccount()) || StringUtils.equals(loginAccount, entity.getBuyerAccount()))) {
                throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),
                        ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }

            DeliverySubOrderRequest request = new DeliverySubOrderRequest();
            request.setId(entity.getId());
            request.setGameRole(entity.getGameRole());
            request.setCount(entity.getCount());
            request.setRealCount(entity.getRealCount());
            request.setStatus(DeliveryOrderStatus.getNameByCode(entity.getStatus()));
            request.setSubOrderStatus(entity.getStatus());
            request.setSellerInputCount(entity.getShInputCount());
            request.setRoleLevel(entity.getRoleLevel());
            request.setWaitToConfirm(entity.getWaitToConfirm());
            list.add(request);
            response.setSubOrderList(list);

            //查出子订单对应的主订单
            DeliveryOrder deliveryOrder = deliveryOrderManager.selectByOrderId(entity.getOrderId());
            response.setDeliveryOrder(deliveryOrder);

            //查出最小申请阈值
            BigDecimal mailRobotCriticalValue = systemConfigManager.getMailRobotCriticalValue();
            response.setLeastAmount(mailRobotCriticalValue);

            if (StringUtils.isBlank(entity.getAppealOrder()) && deliveryOrder.getTradeEndTime() != null) {
                //is it allowed to operate
                Boolean isOverTime = System.currentTimeMillis() - deliveryOrder.getTradeEndTime().getTime() > systemConfigManager.getOverTime();
                if (isOverTime) {
                    //overtime forbid appeal
                    response.setAppealOrderStatus(AppealOrderStatus.OVERTIME.getCode());
                } else {
                    response.setAppealOrderStatus(AppealOrderStatus.ALLOWAPPEAL.getCode());
                }
            } else {
                //entity.getAppealOrder isNotBlank ,representative has already complained, and takes the status from appealOrder directly.
                response.setAppealOrderStatus(entity.getAppealOrderStatus());
            }

            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("查询子订单发生异常:{}", ex);
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
        }
        return response;
    }

    @GET
    @Path("/noQuestion")
    @Override
    public IServiceResponse noQuestion(@QueryParam("SubOrderId") Long SubOrderId) {
        DeliverySubOrderResponse deliverySubOrderResponse = new DeliverySubOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        deliverySubOrderResponse.setResponseStatus(responseStatus);
        try {
            DeliverySubOrder deliverySubOrder = deliverySubOrderManager.selectByOrderId(SubOrderId, null);
            if (deliverySubOrder == null || deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()
                    || deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()
                    || deliverySubOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
            }
            if (deliverySubOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                throw new SystemException(ResponseCodes.AllReadyManualOper.getCode(), ResponseCodes.AllReadyManualOper.getMessage());
            }
            StringBuffer sb = new StringBuffer();
            OrderLog orderLog = new OrderLog();
            sb.append("【").append(deliverySubOrder.getId()).append("号机器人】").
            append("【").append(deliverySubOrder.getGameRole()).append("】").
                    append("您已点击【无异议】按钮。。").append("\r\n")
                    .append("【").append(  deliverySubOrder.getGameRole() ).append( "】卖家无异议" +
                    ",该子订单以");
            if (deliverySubOrder.getRealCount() >= deliverySubOrder.getShInputCount() && deliverySubOrder.getRealCount() != 0L) {
                sb.append(deliverySubOrder.getRealCount()).append("完单。");
            } else if (deliverySubOrder.getRealCount() < deliverySubOrder.getShInputCount() && deliverySubOrder.getRealCount() != 0L) {
                sb.append(deliverySubOrder.getRealCount()).append("部分完单。");
            } else if (deliverySubOrder.getRealCount() == 0) {
                sb.append(deliverySubOrder.getRealCount()).append("撤单。");
            }
            orderLog.setLog(sb.toString());
            orderLog.setOrderId(deliverySubOrder.getOrderId());
            orderLog.setType(OrderLog.TYPE_NORMAL);
            deliveryOrderLogManager.writeLog(orderLog);
            Map<Long, Long> dataMap = new HashMap<Long, Long>();
            dataMap.put(deliverySubOrder.getId(), deliverySubOrder.getRealCount());
            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(dataMap, deliverySubOrder.getOrderId(), null, null,null);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("无异议完单发生异常:{}", ex);
        } catch (Exception ex) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("无异议完单发生未知异常:{}", ex);
        }
        return deliverySubOrderResponse;
    }
}

package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.store.business.IShStoreManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340082 on 2018/1/5.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class AppealDeliveryOrderAction extends AbstractAction {
    private DeliveryOrder deliveryOrder;

    private String startTime;

    private String endTime;

    private List<DeliveryOrder> deliveryOrderList;

    private String mainOrderId;

    private String orderId;

    private Long realCount;

    private String otherReason;

    private List<DeliverySubOrder> deliverySubOrderList;

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    IShStoreManager shStoreManager;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String queryAppealDeliveryOrder() throws ParseException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (deliveryOrder != null) {
            if (StringUtils.isNotBlank(deliveryOrder.getBuyerAccount())) {
                queryMap.put("buyerAccount", deliveryOrder.getBuyerAccount().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getSellerAccount())) {
                queryMap.put("sellerAccount", deliveryOrder.getSellerAccount().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getGameName())) {
                queryMap.put("gameName", deliveryOrder.getGameName().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getRegion())) {
                queryMap.put("region", deliveryOrder.getRegion().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getServer())) {
                queryMap.put("server", deliveryOrder.getServer().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getGameRace())) {
                queryMap.put("gameRace", deliveryOrder.getGameRace().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getOrderId())) {
                queryMap.put("orderId", deliveryOrder.getOrderId().trim());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getAppealOrder())) {
                queryMap.put("appealOrder", deliveryOrder.getAppealOrder().trim());
            }
            if (deliveryOrder.getStatus() != null && deliveryOrder.getStatus() != 0) {
                queryMap.put("status", deliveryOrder.getStatus());
            }
            if (deliveryOrder.getTransferStatus() != null && deliveryOrder.getTransferStatus() != -1) {
                queryMap.put("transferStatus", deliveryOrder.getTransferStatus());
            }
            if (deliveryOrder.getDeliveryType() != null && deliveryOrder.getDeliveryType() != 0) {
                queryMap.put("deliveryType", deliveryOrder.getDeliveryType());
            }
            if (StringUtils.isNotBlank(deliveryOrder.getTradeTypeName()) && !deliveryOrder.getTradeTypeName().equals("全部")) {
                queryMap.put("tradeTypeName", deliveryOrder.getTradeTypeName());
            }
            //添加转人工状态
            if (deliveryOrder.getMachineArtificialStatus() != null && deliveryOrder.getMachineArtificialStatus() != -1) {
                queryMap.put("machineArtificialStatus", deliveryOrder.getMachineArtificialStatus());
            }
            queryMap.put("appealOrderNotNull", true);
            queryMap.put("createStartTime", simpleDateFormat.parse(startTime));
            queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(simpleDateFormat.parse(endTime)));
        }
        GenericPage<DeliveryOrder> genericPage = deliveryOrderManager.queryAppealOrderInPage(queryMap, this.start, this.limit, "create_time", false);
        deliveryOrderList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 人工完单 撤单 部分完单接口
     * 传入主订单号 ，完单理由 ，完单数量
     *
     * @return
     */
    public String updateAppealDeliveryOrder() throws IOException {
//        //根据主订单ｉｄ查询，如果主订单的状态不是交易中，不是申请部分完单，不是人工介入，提示前台用户
//        DeliveryOrder order = deliveryOrderManager.getByOrderId(mainOrderId);
//        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
//                && order.getStatus() != DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()
//                && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()
//                && order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()
//                && order.getStatus() != DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
//            return this.returnError("需要人工介入");
//        }
        deliveryOrderManager.appealFinishDeliveryOrder(orderId, realCount, otherReason);
        return this.returnSuccess();
    }


    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<DeliveryOrder> getDeliveryOrderList() {
        return deliveryOrderList;
    }

    public void setDeliveryOrderList(List<DeliveryOrder> deliveryOrderList) {
        this.deliveryOrderList = deliveryOrderList;
    }

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public List<DeliverySubOrder> getDeliverySubOrderList() {
        return deliverySubOrderList;
    }

    public void setDeliverySubOrderList(List<DeliverySubOrder> deliverySubOrderList) {
        this.deliverySubOrderList = deliverySubOrderList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }
}

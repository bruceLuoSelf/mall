package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.enums.DeliveryTypeEnum;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.store.business.IShStoreManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 收货订单
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class DeliveryOrderAction extends AbstractAction {
    private DeliveryOrder deliveryOrder;

    private String startTime;

    private String endTime;

    private List<DeliveryOrder> deliveryOrderList;

    private String mainOrderId;

    private List<DeliverySubOrder> deliverySubOrderList;

    private String isTimeOut;

    private Long id;

    private Long configCount;

    private Integer status;

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    IShStoreManager shStoreManager;

    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询收货订单信息列表
     *
     * @return
     */
    public String queryDeliveryOrder() throws ParseException {
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
            if (isTimeOut != null && !isTimeOut.equals("-1")) {
                queryMap.put("isTimeout", isTimeOut.equals("true"));
            }
            queryMap.put("createStartTime", simpleDateFormat.parse(startTime));
            queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(simpleDateFormat.parse(endTime)));
            queryMap.put("appealOrderNull", true);
        }
        GenericPage<DeliveryOrder> genericPage = deliveryOrderManager.queryListInPage(queryMap, this.start, this.limit, "create_time", false);
        deliveryOrderList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询收货完单统计
     *
     * @return
     */
    public String queryMoneyCount() throws ParseException {
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
            if (isTimeOut != null && !isTimeOut.equals("-1")) {
                queryMap.put("isTimeout", isTimeOut.equals("true"));
            }
            queryMap.put("tradeCountStartTime", simpleDateFormat.parse(startTime));
            queryMap.put("tradeCountEndTime", WebServerUtil.oneDateLastTime(simpleDateFormat.parse(endTime)));
            queryMap.put("statusList", "4,5");
        }
        GenericPage<DeliveryOrder> genericPage = deliveryOrderManager.queryListInPage(queryMap, this.start, this.limit, "create_time", false);
        deliveryOrderList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 人工完单
     *
     * @return
     * @throws IOException
     */
    public String handleDeliverySubOrder() throws IOException {
        logger.info("人工完单,handleDeliverySubOrder,id:" + id + ",configCount:" + configCount + ",status:" + status);
        if (id == null) {
            this.returnError("要结单的子订单号不能为空！");
        }
        if (configCount == null) {
            this.returnError("要结单的子订单数量不能为空！");
        }
        if (status == null) {
            this.returnError("当前子订单状态异常！");
        }
        if (status == DeliveryOrderStatus.COMPLETE.getCode() || status == DeliveryOrderStatus.COMPLETE_PART.getCode() || status == DeliveryOrderStatus.CANCEL.getCode()) {
            this.returnError("当前子订单已结单！");
        }
        DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectById(id);
        DeliveryOrder order = deliveryOrderManager.getByOrderId(deliverySubOrder.getOrderId());
        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
                && order.getStatus() != DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()
                && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()
                && order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()
                && order.getStatus() != DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
            return this.returnError("需要人工介入");
        }
        Map<Long, Long> cancelCount = new HashMap<Long, Long>();
        cancelCount.put(id, configCount);
        if (order.getDeliveryType() == DeliveryTypeEnum.Robot.getCode()) {
            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(cancelCount, deliverySubOrder.getOrderId(), null, null, null);
        } else {
            shStoreManager.manualFinishOrder(deliverySubOrder.getOrderId(), cancelCount);
        }
        return this.returnSuccess();
    }

//    /**
//     * 人工完单--目前不用了
//     *
//     * @return
//     */
//    public String updateDeliverySubOrder() throws IOException {
//        Map<Long, Long> updateMap = new HashMap<Long, Long>();
//        if (deliverySubOrderList != null) {
//            for (DeliverySubOrder list : deliverySubOrderList) {
//                if (list.getId() != null &&
//                        list.getStatus() != DeliveryOrderStatus.COMPLETE.getCode()
//                        && list.getStatus() != DeliveryOrderStatus.COMPLETE_PART.getCode()
//                        && list.getStatus() != DeliveryOrderStatus.CANCEL.getCode()
//                        && list.getRealCount() != 0) {
//                    updateMap.put(list.getId(), list.getRealCount());
//                }
//            }
//        }
//        System.out.print(mainOrderId);
//        //根据主订单ｉｄ查询，如果主订单的状态不是交易中，不是申请部分完单，不是人工介入，提示前台用户
//        DeliveryOrder order = deliveryOrderManager.getByOrderId(mainOrderId);
//        if (order.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
//                && order.getStatus() != DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()
//                && order.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()
//                && order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()
//                && order.getStatus() != DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
//            return this.returnError("需要人工介入");
//        }
//
//        if (order.getDeliveryType() == DeliveryTypeEnum.Robot.getCode()) {
//            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(updateMap, mainOrderId, null, null, null);
//        } else {
//            shStoreManager.manualFinishOrder(mainOrderId, updateMap);
//        }
//        return this.returnSuccess();
//    }

    /**
     * 订单转人工
     */
    public String turnDeliverySubOrder() throws IOException {
        if (id == null) {
            throw new SystemException("要转人工的子订单号不能为空！");
        }
        try {
            deliveryOrderManager.subOrderAutoDistributionManager(id, 2, null);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("转人工失败。" + e.getMessage());
        }
        return this.returnSuccess();
    }


    /**
     * 统计金额汇总
     *
     * @return
     */
    public String statisAmount() throws ParseException {
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
            if (isTimeOut != null && !isTimeOut.equals("-1")) {
                if (isTimeOut.equals("true")) {
                    queryMap.put("isTimeout", true);
                } else {
                    queryMap.put("noTimeoutData", "(is_timeout is false or is_timeout is null)");
                }
            }
            queryMap.put("createStartTime", simpleDateFormat.parse(startTime));
            queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(simpleDateFormat.parse(endTime)));
        }
        deliveryOrder = deliveryOrderManager.statisAmount(queryMap);

        return this.returnSuccess();
    }

    /**
     * 统计完单金额汇总
     *
     * @return
     */
    public String statisAmountTradeCount() throws ParseException {
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
            if (isTimeOut != null && !isTimeOut.equals("-1")) {
                if (isTimeOut.equals("true")) {
                    queryMap.put("isTimeout", true);
                } else {
                    queryMap.put("noTimeoutData", "(is_timeout is false or is_timeout is null)");
                }
            }
            queryMap.put("tradeCountStartTime", simpleDateFormat.parse(startTime));
            queryMap.put("tradeCountEndTime", WebServerUtil.oneDateLastTime(simpleDateFormat.parse(endTime)));
            queryMap.put("statusList", "4,5");
        }
        deliveryOrder = deliveryOrderManager.statisAmount(queryMap);

        return this.returnSuccess();
    }

    /**
     * 手动分配物服
     * ZW_C_JB_00004 mj
     *
     * @return
     */
    public String distributionService() {
        if (StringUtils.isBlank(mainOrderId)) {
            return this.returnError("需要订单id");
        }
        deliveryOrderManager.distributionService(mainOrderId);
        return this.returnSuccess();
    }

    public String getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(String isTimeOut) {
        this.isTimeOut = isTimeOut;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigCount() {
        return configCount;
    }

    public void setConfigCount(Long configCount) {
        this.configCount = configCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

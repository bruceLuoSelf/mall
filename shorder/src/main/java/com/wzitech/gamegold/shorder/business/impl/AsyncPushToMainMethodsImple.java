package com.wzitech.gamegold.shorder.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.dto.GoodsInfo;
import com.wzitech.gamegold.common.dto.OrderDataVO;
import com.wzitech.gamegold.common.dto.OrderOpData;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.main.IGetGameIdFromMain;
import com.wzitech.gamegold.common.main.IGetImageUrlFromMain;
import com.wzitech.gamegold.common.main.IMainGerIdUtil;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.common.utils.RedisDaoUtil;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by 339928 on 2018/6/11.
 */
@Repository
public class AsyncPushToMainMethodsImple extends AbstractBusinessObject {

    @Autowired
    private IMainGerIdUtil mainGerIdUtil;

    @Autowired
    private IGetGameIdFromMain getGameIdFromMain;

    @Autowired
    private IGetImageUrlFromMain getImageUrlFromMain;


    @Autowired
    private RedisDaoUtil redisDaoUtil;


    @Autowired
    ImqUtilForOrderCenterToMain mqUtilForOrderCenterToMain;
    /**
     * 订单信息同步推送主站
     * 参数：1.出货单；2.订单中心所需的订单状态；
     *
     */
    @Async
    public void orderPushToMain(DeliveryOrder deliveryOrder, int orderStatus) {
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getMessage());
        }
        orderPushVo pushVo = new orderPushVo();
        OrderDataVO dataVo = new OrderDataVO();
        OrderOpData opData = new OrderOpData();
        GoodsInfo goodsInfo = new GoodsInfo();

        pushVo.setClientType(ClientTypeEnum.PC_CLIENT.getCode());
        pushVo.setIsPaid(true);
        pushVo.setId(deliveryOrder.getOrderId());
        pushVo.setSellerId(deliveryOrder.getSellerUid());
        pushVo.setBuyerId(deliveryOrder.getBuyerUid());
        //主站接收c#解析 需要加8小时
        Long eightTime = 28800000L;
        pushVo.setOrderCreateDate(deliveryOrder.getCreateTime().getTime() + eightTime);
        pushVo.setOrderSource(OrderSource.GAME_SH_GOLD.getName());

        pushVo.setCqtradingType(OrderType.GAME_GOLD_DELIVERY_ORDER.getCode());
        pushVo.setOriginOderStatus(deliveryOrder.getStatus());

        String mainId = mainGerIdUtil.getMainId(true);
        Boolean isNotTrueMainId = false;
        if ("-1".equals(mainId)) {
            isNotTrueMainId = true;
        }
        Integer versionId = Integer.parseInt(mainId);
        pushVo.setCurrentVersion(versionId);

        dataVo.setBuyerName(deliveryOrder.getBuyerAccount());
        dataVo.setSellerName(deliveryOrder.getSellerAccount());

        goodsInfo.setGameName(deliveryOrder.getGameName());
        goodsInfo.setRegion(deliveryOrder.getRegion());
        goodsInfo.setServer(deliveryOrder.getServer());
        GoodsInfo gameAndServer = getGameIdFromMain.getGameAndServer(goodsInfo);
        pushVo.setGameId(gameAndServer.getGameId());
        pushVo.setGameAreaId(gameAndServer.getRegionId());
        pushVo.setGameServerId(gameAndServer.getServerId());
        //store image address
        pushVo.setGameServerId(gameAndServer.getServerId());
        String image = getImageUrlFromMain.getImage(pushVo.getGameId());
        dataVo.setPicUrlManager(image);

        dataVo.setBizOfferTypeName("游戏币");
        dataVo.setGameName(deliveryOrder.getGameName());
        dataVo.setGameAreaName(deliveryOrder.getRegion());
        dataVo.setGameServerName(deliveryOrder.getServer());
        dataVo.setRealAmount(deliveryOrder.getRealAmount());
        StringBuffer title = new StringBuffer();
        if (StringUtils.isNotBlank(deliveryOrder.getAppealOrder())) {
            title.append("【申诉单】");
        }
        title.append(deliveryOrder.getCount().toString());
        if (StringUtils.isNotBlank(deliveryOrder.getMoneyName())) {
            title.append(deliveryOrder.getMoneyName());
        }
        if (deliveryOrder.getAmount() != null) {
            title.append(" = " + deliveryOrder.getAmount() + "元");
        }
        dataVo.setBizOfferName(title.toString());
        dataVo.setBuyerIp(deliveryOrder.getSellerIp());
        dataVo.setBuyerQQ(deliveryOrder.getQq());
        dataVo.setBuyerMobile(deliveryOrder.getBuyerPhone());
        dataVo.setBuyerGameRole(deliveryOrder.getRoleName());
        if (deliveryOrder.getDeliveryType() == 1) {
            dataVo.setMachineDelivery(MachineOrArtficialEnum.MACHINE.getName());
        }
        if (deliveryOrder.getDeliveryType() == 2) {
            dataVo.setMachineDelivery(MachineOrArtficialEnum.ARTFICIAL.getName());
        }
        BigDecimal amount = deliveryOrder.getPrice();
        pushVo.setPrice(amount);
        pushVo.setPayPrice(deliveryOrder.getAmount());
        pushVo.setGoodsQuantity(deliveryOrder.getCount().toString());
        if (deliveryOrder.getOtherReason() == null) {
            dataVo.setCancelReasons("");
        } else {
            dataVo.setCancelReasons(deliveryOrder.getOtherReason());
        }

        pushVo.setJsonData(dataVo);

        opData.setKefuQQ(deliveryOrder.getServiceQq());
        opData.setKefuName(deliveryOrder.getServiceNickname());
        pushVo.setOpJsonData(opData);

        ObjectMapper mapper = new ObjectMapper();
        String message = null;
        try {
            message = mapper.writeValueAsString(pushVo);
            if (isNotTrueMainId) {
                redisDaoUtil.saveOrder(message);
            }
            mqUtilForOrderCenterToMain.mqPushOrderToMain(message);
            logger.info("调用推送mq方法结束", message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

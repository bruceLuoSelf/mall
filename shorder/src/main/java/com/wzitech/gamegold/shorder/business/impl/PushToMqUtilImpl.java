package com.wzitech.gamegold.shorder.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.common.dto.GoodsInfo;
import com.wzitech.gamegold.common.dto.OrderDataVO;
import com.wzitech.gamegold.common.dto.OrderOpData;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.enums.ClientTypeEnum;
import com.wzitech.gamegold.common.enums.MachineOrArtficialEnum;
import com.wzitech.gamegold.common.enums.OrderSource;
import com.wzitech.gamegold.common.enums.OrderType;
import com.wzitech.gamegold.common.main.IGetGameIdFromMain;
import com.wzitech.gamegold.common.main.IGetImageUrlFromMain;
import com.wzitech.gamegold.common.main.IMainGerIdUtil;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.common.utils.RedisDaoUtil;
import com.wzitech.gamegold.shorder.business.IPushToMqUtil;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by chengXY on 2017/10/30.
 */
@Component
public class PushToMqUtilImpl extends AbstractBusinessObject implements IPushToMqUtil {

    @Autowired
    private ImqUtilForOrderCenterToMain mqUtilForOrderCenterToMain;

    @Autowired
    private IGetGameIdFromMain getGameIdFromMain;

    @Autowired
    private IMainGerIdUtil mainGerIdUtil;

    @Autowired
    private IGetImageUrlFromMain getImageUrlFromMain;

    @Autowired
    private RedisDaoUtil redisDaoUtil;

    @Override
    @Async
    public void pushToMainMq(DeliveryOrder deliveryOrder, int orderStatus){
        orderPushVo pushVo = new orderPushVo();
        OrderDataVO dataVo = new OrderDataVO();
        GoodsInfo goodsInfo = new GoodsInfo();
        OrderOpData opData = new OrderOpData();

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
        Boolean isNotTrueMainId = false;
        String mainId = mainGerIdUtil.getMainId(true);
        if ("-1".equals(mainId)){
            isNotTrueMainId = true;
        }
        Integer versionId = Integer.parseInt(mainId);
        pushVo.setCurrentVersion(versionId);

        dataVo.setBuyerName(deliveryOrder.getBuyerAccount());
        dataVo.setSellerName(deliveryOrder.getSellerAccount());
        if(deliveryOrder.getDeliveryType()==1){
            dataVo.setMachineDelivery(MachineOrArtficialEnum.MACHINE.getName());
        }
        if (deliveryOrder.getDeliveryType()==2) {
            dataVo.setMachineDelivery(MachineOrArtficialEnum.ARTFICIAL.getName());
        }
        goodsInfo.setGameName(deliveryOrder.getGameName());
        goodsInfo.setRegion(deliveryOrder.getRegion());
        goodsInfo.setServer(deliveryOrder.getServer());
        GoodsInfo gameAndServer = getGameIdFromMain.getGameAndServer(goodsInfo);
        pushVo.setGameId(gameAndServer.getGameId());
        pushVo.setGameAreaId(gameAndServer.getRegionId());
        //store image address
        pushVo.setGameServerId(gameAndServer.getServerId());
        String image = getImageUrlFromMain.getImage(pushVo.getGameId());
        dataVo.setPicUrlManager(image);

        dataVo.setBizOfferTypeName("游戏币");
        dataVo.setGameName(deliveryOrder.getGameName());
        dataVo.setGameAreaName(deliveryOrder.getRegion());
        dataVo.setGameServerName(deliveryOrder.getServer());

        dataVo.setBuyerQQ(deliveryOrder.getQq());
        dataVo.setBuyerMobile(deliveryOrder.getBuyerPhone());
        dataVo.setBuyerGameRole(deliveryOrder.getRoleName());
        dataVo.setRealAmount(deliveryOrder.getRealAmount());

        String title = deliveryOrder.getCount().toString();
        if (StringUtils.isNotBlank(deliveryOrder.getMoneyName())){
            title=title+deliveryOrder.getMoneyName();
        }
        if (deliveryOrder.getAmount()!=null){
            title=title+" = "+deliveryOrder.getAmount()+"元";
        }
        dataVo.setBizOfferName(title);
        dataVo.setBuyerIp(deliveryOrder.getSellerIp());
        BigDecimal amount = deliveryOrder.getPrice();
        pushVo.setPrice(amount);
        pushVo.setPayPrice(deliveryOrder.getAmount());
        pushVo.setGoodsQuantity(deliveryOrder.getCount().toString());
        if (deliveryOrder.getOtherReason()==null){
            dataVo.setCancelReasons("");
        }else {
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
            if (isNotTrueMainId){
                redisDaoUtil.saveOrder(message);
            }else {
                mqUtilForOrderCenterToMain.mqPushOrderToMain(message);
            }
            logger.info("调用推送mq方法结束");
        } catch (Exception e) {
            logger.error("推送金币商城订单至队列出错");
            e.printStackTrace();
        }

    }
}

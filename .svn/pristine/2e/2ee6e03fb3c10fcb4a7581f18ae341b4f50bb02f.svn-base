package com.wzitech.gamegold.order.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.dto.OrderDataVO;
import com.wzitech.gamegold.common.dto.OrderOpData;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.main.IGetImageUrlFromMain;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.common.main.MainGerIdUtilEo;
import com.wzitech.gamegold.common.utils.RedisDaoUtil;
import com.wzitech.gamegold.order.business.IDeliveryOrderCenter;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.impl.DeliveryOrderIdRedisDaoImpl;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.usermgmt.dao.redis.impl.UserInfoRedisDAOImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by 339928 on 2017/10/25.
 */
@Component
public class DeliveryOrderCenterManager implements IDeliveryOrderCenter {

    protected static final Logger logger = LoggerFactory.getLogger(DeliveryOrderCenterManager.class);

    @Autowired
    IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    DeliveryOrderIdRedisDaoImpl deliveryOrderIdRedisDao;

    @Autowired
    IGoodsInfoManager goodsInfoManager;


    @Autowired
    MainGerIdUtilEo mainGerIdUtilEo;

    @Autowired
    ImqUtilForOrderCenterToMain imqUtilForOrderCenterToMain;


    @Autowired
    private IGetImageUrlFromMain getImageUrlFromMain;

    @Autowired
    RedisDaoUtil redisDaoUtil;

    @Autowired
    UserInfoRedisDAOImpl userInfoRedisDAO;

    public void autoDeliveryOrderToOrderCenter(Long createStartTime, Long createEndTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>(3);
        queryMap.put("createStartTime", new Date(createStartTime));
        queryMap.put("createEndTime", new Date(createEndTime));
//        String startString = deliveryOrderIdRedisDao.getCount();
//        if (StringUtils.isNotBlank(startString)) {
//            start = Integer.parseInt(startString);
//        }
        Integer start = 0;
        while (true) {
            GenericPage<DeliveryOrder> deliveryOrderGenericPage = deliveryOrderDao.selectByMap(queryMap, 100, start, "id", false);
            List<DeliveryOrder> deliveryOrder = deliveryOrderGenericPage.getData();
            try {
                logger.info(createStartTime+"----"+createEndTime+"已推送" + start + "条");
                if (deliveryOrder == null || deliveryOrder.size() == 0) {
                    break;
                }
                for (DeliveryOrder order : deliveryOrder) {
                    orderPushVo orderPushVo = new orderPushVo();
                    OrderDataVO dataVo = new OrderDataVO();
                    orderPushVo.setId(order.getOrderId());
                    /**
                     *  0 没有
                     *  1 对应1 2 3 9
                     *  2 对应 8 10 11
                     *  3 对应4 5
                     *  4 对应 6  7
                     *  */
                    if (null != order.getStatus()) {
                        if (order.getStatus() == DeliveryOrderStatus.WAIT_TRADE.getCode() || order.getStatus() == DeliveryOrderStatus.INQUEUE.getCode() || order.getStatus() == DeliveryOrderStatus.TRADING.getCode() || order.getStatus() == DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getStatus() == DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode() || order.getStatus() == DeliveryOrderStatus.DELIVERY_FINISH.getCode() || order.getStatus() == DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getStatus() == DeliveryOrderStatus.COMPLETE.getCode() || order.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                            orderPushVo.setIsPaid(false);
                        }
                        if (order.getStatus() == DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                    }
                    orderPushVo.setOriginOderStatus(order.getStatus());
                    orderPushVo.setSellerId(order.getSellerUid());
                    orderPushVo.setClientType(1);
                    GoodsInfo goodsInfo = new GoodsInfo();
                    goodsInfo.setGameName(order.getGameName());
                    goodsInfo.setRegion(order.getRegion());
                    goodsInfo.setServer(order.getServer());
                    goodsInfo = goodsInfoManager.setGoodsGameId(goodsInfo);
                    orderPushVo.setGameId(goodsInfo.getGameId());
                    //store image address
                    String image = getImageUrlFromMain.getImage(goodsInfo.getGameId());
                    dataVo.setPicUrlManager(image);
                    if (order.getDeliveryType() == 1) {
                        dataVo.setMachineDelivery(MachineOrArtficialEnum.MACHINE.getName());
                    }
                    if (order.getDeliveryType() == 2) {
                        dataVo.setMachineDelivery(MachineOrArtficialEnum.ARTFICIAL.getName());
                    }
                    orderPushVo.setGameAreaId(goodsInfo.getRegionId());
                    orderPushVo.setGameServerId(goodsInfo.getServerId());
                    orderPushVo.setCqtradingType(10);
                    if (org.apache.commons.lang3.StringUtils.isBlank(order.getGoodsTypeName()) || order.getGoodsTypeName().equals(ServicesContants.GOODS_TYPE_GOLD)) {
                        dataVo.setBizOfferTypeName(ServicesContants.GOODS_TYPE_GOLD);
                        dataVo.setBizOfferName(ServicesContants.GOODS_TYPE_GOLD);
                    } else {
                        dataVo.setBizOfferTypeName(order.getGoodsTypeName());
                        dataVo.setBizOfferName(order.getGoodsTypeName());
                    }
                    //标题
                    if (StringUtils.isBlank(order.getMoneyName())) {
                        dataVo.setBizOfferName(order.getCount().toString() + "万金" + "=" + order.getAmount() + "元");
                    } else {
                        dataVo.setBizOfferName(order.getCount() + order.getMoneyName() + "=" + order.getAmount() + "元");
                    }
                    if (order.getGoodsType() != null) {
                        orderPushVo.setBizOfferTypeId(order.getGoodsType().toString());
                    } else {
                        orderPushVo.setBizOfferTypeId("47");
                    }
                    orderPushVo.setBuyerId(order.getBuyerUid());
                    Boolean isNullOpenApiId = false;
                    //商品类型为游戏币
                    if (StringUtils.isBlank(order.getGoodsTypeName()) || order.getGoodsTypeName().equals(ServicesContants.GOODS_TYPE_GOLD)) {
                        orderPushVo.setOrderSource(OrderSource.GAME_SH_GOLD.getName());
                        //游戏币的openapi
                        String openAPIIDOfGold = mainGerIdUtilEo.getMainId(true);
                        if ("-1".equals(openAPIIDOfGold)) {
                            isNullOpenApiId = true;
                        }
                        orderPushVo.setCurrentVersion(Integer.parseInt(openAPIIDOfGold));
                    } else {
                        //通货的openapi
                        String openAPIIDOfGoods = mainGerIdUtilEo.getMainId(false);
                        if ("-1".equals(openAPIIDOfGoods)) {
                            isNullOpenApiId = true;
                        }
                        orderPushVo.setOrderSource(OrderSource.GAME_SH_GOLD.getName());
                        orderPushVo.setCurrentVersion(Integer.parseInt(openAPIIDOfGoods));
                    }
                    if (order.getCreateTime() != null) {
                        Long eightTime = 28800000L;
                        orderPushVo.setOrderCreateDate(order.getCreateTime().getTime() + eightTime);
                    }
                    /**
                     * 自定义订单数据
                     */
                    dataVo.setBuyerName(order.getBuyerAccount());
                    dataVo.setGameName(order.getGameName());
                    dataVo.setGameAreaName(order.getRegion());
                    dataVo.setBuyerMobile(order.getBuyerPhone());
                    orderPushVo.setGoodsQuantity(order.getCount().toString());
                    dataVo.setGameServerName(order.getServer());
                    dataVo.setBuyerGameRole(order.getRoleName());
                    dataVo.setSellerName(order.getSellerAccount());
                    if (StringUtils.isBlank(order.getOtherReason())) {
                        dataVo.setCancelReasons("");
                    } else {
                        dataVo.setCancelReasons(order.getOtherReason());
                    }

                    dataVo.setRealAmount(order.getRealAmount());
                    if (order.getPrice() != null) {
                        orderPushVo.setPrice(order.getPrice());
                    }
                    if (order.getAmount() != null) {
                        orderPushVo.setPayPrice(order.getAmount());
                    }
                    /**
                     * 客服信息
                     */
                    OrderOpData orderOpData = new OrderOpData();
                    if(StringUtils.isNotBlank(order.getServiceNickname())){
                        orderOpData.setKefuName(order.getServiceNickname());
                    }else {
                        orderOpData.setKefuName(null);
                    }
                    if(StringUtils.isNotBlank(order.getServiceQq())){
                        orderOpData.setKefuQQ(order.getServiceQq());
                    }else {
                        orderOpData.setKefuQQ(null);
                    }
                    orderPushVo.setJsonData(dataVo);
                    orderPushVo.setOpJsonData(orderOpData);

                    ObjectMapper mapper = new ObjectMapper();
                    String message = null;
                    try {
                        message = mapper.writeValueAsString(orderPushVo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("向订单中心发送收货流程的订单开始");
                    if (isNullOpenApiId) {
                        redisDaoUtil.saveOrder(message);
                    } else {
                        imqUtilForOrderCenterToMain.mqPushOrderToMain(message);
                    }
                }
                start += 100;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userInfoRedisDAO.setCount("已推送完成",createStartTime,createEndTime);
    }
}

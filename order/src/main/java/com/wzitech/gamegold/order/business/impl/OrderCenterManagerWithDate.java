package com.wzitech.gamegold.order.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.dto.BuyerSigns;
import com.wzitech.gamegold.common.dto.OrderDataVO;
import com.wzitech.gamegold.common.dto.OrderOpData;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.main.IGetImageUrlFromMain;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.common.main.MainGerIdUtilEo;
import com.wzitech.gamegold.common.utils.RedisDaoUtil;
import com.wzitech.gamegold.order.business.IOrderCenterWithDate;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.shorder.dao.impl.SystemConfigDaoImpl;
import com.wzitech.gamegold.usermgmt.dao.redis.impl.UserInfoRedisDAOImpl;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 339928 on 2017/12/7.
 */
@Component
public class OrderCenterManagerWithDate implements IOrderCenterWithDate {

    protected static final Logger logger = LoggerFactory.getLogger(OrderCenterManagerWithDate.class);

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    MainGerIdUtilEo mainGerIdUtilEol;

    @Autowired
    UserInfoRedisDAOImpl userInfoRedisDAO;

    @Autowired
    ImqUtilForOrderCenterToMain imqUtilForOrderCenterToMain;

    @Autowired
    SystemConfigDaoImpl systemConfigDao;

    @Autowired
    IOrderPushMainManager orderPushMainManager;

    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    private IGetImageUrlFromMain getImageUrlFromMain;

    @Autowired
    private RedisDaoUtil redisDaoUtil;

    @Override
    public void autoSellOrderToOrderCenterWithDate(Long dateBegin, Long dateEnd) {
        Map<String, Object> queryMap = new HashMap<String, Object>(3);
        queryMap.put("createStartTime", new Date(dateBegin));
        queryMap.put("createEndTime", new Date(dateEnd));
        queryMap.put("goodsTypeName", "全部");
//        String startString = userInfoRedisDAO.getCountWithDate();
//        if (StringUtils.isNotBlank(startString)) {
//            start = Integer.parseInt(startString);
//        }
        Integer start = 0;
        while (true) {
            JSONArray jsonArray = null;
            GenericPage<OrderInfoEO> orderinfoGenericPage = orderInfoDBDAO.selectByMap(queryMap, 100, start, "ID", false);
            logger.info(dateBegin+"----"+dateEnd+"已推送" + start + "条");
            List<OrderInfoEO> orderInfoEO = orderinfoGenericPage.getData();
            try {
                if (orderInfoEO == null || orderInfoEO.size() == 0) {
                    break;
                }
                for (OrderInfoEO order : orderInfoEO) {
                    orderPushVo orderPushVo = new orderPushVo();
                    OrderDataVO orderDataVO = new OrderDataVO();
                    BuyerSigns buyerSigns = new BuyerSigns();
                    JSONArray buyerSign = null;
                    if (order.getInsuranceAmount() != null && order.getInsuranceAmount().compareTo(BigDecimal.ZERO) != 0) {
                        buyerSigns.setPrice(order.getInsuranceAmount());
                        buyerSigns.setName("买家安心买");
                        buyerSigns.setId("buyerrelieved");
                        buyerSign = JSONArray.fromObject(buyerSigns);

                    }
                    if (buyerSign != null) {
                        String buyerEnd = buyerSign.toString();
                        if (buyerEnd.contains("id")) {
                            buyerEnd = buyerEnd.replace("id", "Id");
                        }
                        if (buyerEnd.contains("name")) {
                            buyerEnd = buyerEnd.replace("name", "Name");
                        }
                        if (buyerEnd.contains("price")) {
                            buyerEnd = buyerEnd.replace("price", "Price");
                        }
                        jsonArray = JSONArray.fromObject(buyerEnd);
                    }

                    //推送卖家订单列表 可能一对多
                    List<ConfigResultInfoEO> configResultInfoEOs = configResultInfoDBDAO.selectByOrderId(order.getOrderId());

                    if (configResultInfoEOs != null && configResultInfoEOs.size() != 0) {
                        orderPushMainManager.putMainConfigList(configResultInfoEOs, order);
                    }
                    orderPushVo.setId(order.getOrderId());
                    /**
                     * 1对应0
                     * 23  对应1
                     * 48 对应 2
                     * 5 对应 3
                     * 67 对应4
                     */
                    if (null != order.getOrderState()) {
                        if (order.getOrderState() == OrderState.WaitPayment.getCode()) {
                            orderPushVo.setIsPaid(false);
                        }
                        if (order.getOrderState() == OrderState.Paid.getCode() || order.getOrderState() == OrderState.WaitDelivery.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getOrderState() == OrderState.Delivery.getCode() || order.getOrderState() == OrderState.Receive.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getOrderState() == OrderState.Statement.getCode()) {
                            orderPushVo.setIsPaid(true);
                        }
                        if (order.getOrderState() == OrderState.Refund.getCode() || order.getOrderState() == OrderState.Cancelled.getCode()) {
                            orderPushVo.setIsPaid(false);
                        }
                    }
                    orderPushVo.setOriginOderStatus(order.getOrderState());
                    orderPushVo.setGameId(order.getGameId());
                    //store image address
                    String image = getImageUrlFromMain.getImage(order.getGameId());
                    orderDataVO.setPicUrlManager(image);

                    orderPushVo.setGameAreaId(order.getRegionId());
                    orderPushVo.setBuyerId(order.getUid());
                    orderPushVo.setSellerId("");
                    orderPushVo.setGameServerId(order.getServerId());
                    if (buyerSign != null) {
                        orderPushVo.setNewBuyerSigns(jsonArray);
                    }
                    orderPushVo.setCqtradingType(6);

                    //评价
                    if (order.getIsEvaluate() != null && order.getIsEvaluate()) {
                        orderDataVO.setEvaluation(OrderEvaluation.Evaluation.getCode());
                    } else if (order.getIsReEvaluate() != null && order.getIsReEvaluate()) {
                        orderDataVO.setEvaluation(OrderEvaluation.Append_Evaluation.getCode());
                    } else {
                        orderDataVO.setEvaluation(OrderEvaluation.NO_Evaluation.getCode());
                    }

                    if (order.getGoodsTypeId() != null) {
                        orderPushVo.setBizOfferTypeId(order.getGoodsTypeId().toString());
                    } else {
                        orderPushVo.setBizOfferTypeId("47");
                    }
                    Boolean isNotTrueMainId = false;
                    if (StringUtils.isBlank(order.getGoodsTypeName()) || order.getGoodsTypeName().equals(ServicesContants.GOODS_TYPE_GOLD)) {
                        orderPushVo.setOrderSource(OrderSource.GAME_XS_GOLD.getName());
                        //游戏币的openapi
                        String openAPIIDOfGold = mainGerIdUtilEol.getMainId(true);
                        if ("-1".equals(openAPIIDOfGold)) {
                            isNotTrueMainId = true;
                        }
                        orderPushVo.setCurrentVersion(Integer.parseInt(openAPIIDOfGold));
                    } else {
                        //通货的openapi
                        String openAPIIDOfGoods = mainGerIdUtilEol.getMainId(false);
                        if ("-1".equals(openAPIIDOfGoods)) {
                            isNotTrueMainId = true;
                        }
                        orderPushVo.setOrderSource(OrderSource.GAME_XS_GOODS.getName());
                        orderPushVo.setCurrentVersion(Integer.parseInt(openAPIIDOfGoods));
                    }
                    if (order.getCreateTime() != null) {
                        //主站接收c#解析 需要加8小时
                        Long eightTime = 28800000L;
                        orderPushVo.setOrderCreateDate(order.getCreateTime().getTime() + eightTime);
                    }
                    /**
                     * 1 2 3 都属于pc
                     */
                    if (null == order.getRefererType() || order.getRefererType() == RefererType.CUSTOMERS_SERVICE_CENTER.getCode() || order.getRefererType() == RefererType.InternetBarAlliance.getCode() || order.getRefererType() == RefererType.goldOrder.getCode()) {
                        orderPushVo.setClientType(1);
                    }
                    if (null != order.getRefererType()) {
                        if (order.getRefererType() == RefererType.mOrder.getCode()) {
                            orderPushVo.setClientType(2);
                        }
                        if (order.getRefererType() == RefererType.AppOrder.getCode()) {
                            orderPushVo.setClientType(3);
                        }
                    }
                    /**
                     * 自定义订单数据
                     */
                    orderDataVO.setGameName(order.getGameName());
                    orderDataVO.setGameAreaName(order.getRegion());
                    orderDataVO.setGameServerName(order.getServer());
                    orderDataVO.setBuyerGameRole(order.getReceiver());
                    orderDataVO.setBuyerName(order.getUserAccount());
                    orderPushVo.setGoodsQuantity(order.getGoldCount().toString());
                    if (StringUtils.isNotBlank(order.getCancelReson())) {
                        orderDataVO.setCancelReasons(order.getCancelReson());
                    } else {
                        orderDataVO.setCancelReasons("");
                    }
                    if (org.apache.commons.lang3.StringUtils.isBlank(order.getGoodsTypeName()) || order.getGoodsTypeName().equals(ServicesContants.GOODS_TYPE_GOLD)) {
                        orderDataVO.setBizOfferTypeName(ServicesContants.GOODS_TYPE_GOLD);
                    } else {
                        orderDataVO.setBizOfferTypeName(order.getGoodsTypeName());
                    }
                    //标题
                    BigDecimal realOrderAmount = order.getTotalPrice();
                    if (order.getInsuranceAmount() != null && order.getInsuranceAmount().compareTo(BigDecimal.ZERO) == 1) {
                        realOrderAmount = order.getTotalPrice().subtract(order.getInsuranceAmount());
                    }
                    if (StringUtils.isBlank(order.getMoneyName())) {
                        orderDataVO.setBizOfferName(order.getGoldCount().toString() + "万金" + "=" + realOrderAmount.setScale(2) + "元");
                    } else {
                        orderDataVO.setBizOfferName(order.getGoldCount().toString() + order.getMoneyName() + "=" + realOrderAmount.setScale(2) + "元");
                    }
                    orderDataVO.setSellerName(order.getSellerLoginAccount());
                    if (order.getUnitPrice() != null) {
                        orderPushVo.setPrice(order.getUnitPrice());
                    }
                    if (order.getTotalPrice() != null) {
                        orderPushVo.setPayPrice(order.getTotalPrice()
                        );
                    }
                    /**
                     *客服信息
                     */
                    OrderOpData orderOpData = new OrderOpData();
                    if (order.getServicerInfo() != null) {
                        orderOpData.setKefuLoginId(order.getServicerInfo().getId().toString());
                        orderOpData.setKefuName(order.getServicerInfo().getNickName());
                        orderOpData.setKefuQQ(order.getServicerInfo().getQq());
                    } else {
                        orderOpData.setKefuLoginId(null);
                        orderOpData.setKefuName(null);
                        orderOpData.setKefuQQ(null);
                    }
                    orderPushVo.setJsonData(orderDataVO);
                    orderPushVo.setOpJsonData(orderOpData);
                    ObjectMapper mapper = new ObjectMapper();
                    String message = null;
                    try {
                        message = mapper.writeValueAsString(orderPushVo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    logger.info("向订单中心发送出售流程订单开始");
                    if (isNotTrueMainId) {
                        redisDaoUtil.saveOrder(message);
                    } else {
                        imqUtilForOrderCenterToMain.mqPushOrderToMain(message);
                    }
                    userInfoRedisDAO.setCount(order.getOrderId(),dateBegin,dateEnd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            start += 100;
        }
        userInfoRedisDAO.setCount("已推送完成",dateBegin,dateEnd);
    }

}

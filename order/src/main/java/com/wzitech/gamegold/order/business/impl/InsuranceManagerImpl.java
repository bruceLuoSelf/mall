package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.common.insurance.dto.BQOrderServiceSoap;
import com.wzitech.gamegold.common.insurance.dto.BQType;
import com.wzitech.gamegold.common.insurance.dto.BackDTO;
import com.wzitech.gamegold.common.insurance.dto.OrderDTO;
import com.wzitech.gamegold.common.userinfo.IUserInfoService;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.order.business.IInsuranceManager;
import com.wzitech.gamegold.order.business.IInsuranceOrderManager;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

/**
 * 保险服务
 *
 * @author yemq
 */
@Component
public class InsuranceManagerImpl implements IInsuranceManager {
    protected static final Logger logger = LoggerFactory.getLogger(InsuranceManagerImpl.class);

    /**
     * 保险webservice客户端
     */
    @Autowired
    private BQOrderServiceSoap insuranceService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IInsuranceOrderManager insuranceOrderManager;

    /**
     * 创建保单
     *
     * @param insuranceOrder
     * @return
     */
    public void createBQOrder(InsuranceOrder insuranceOrder) {
        OrderInfoEO order = insuranceOrder.getOrder();
        if (order.getIsBuyInsurance() == null || !order.getIsBuyInsurance())
            return;

        logger.info("开始创建保单:{}", order);
        BackDTO backDTO = null;
        OrderDTO orderDTO = buildOrderDTO(order);
        for (int i = 0; i < 5; i++) {
            backDTO = insuranceService.createBQOrder(orderDTO);
            logger.info("订单:{}创建保单返回:{}/{}", new Object[]{order.getOrderId(), backDTO.getCode(), backDTO.getDetail()});
            if (!isSuccess(backDTO)) {
                logger.info("订单{},创建保单失败，重试第{}次", order.getOrderId(), (i + 1));
                continue;
            } else {
                break;
            }
        }

        if (isSuccess(backDTO)) {
            // 保单创建成功，将状态设置为待结单
            insuranceOrder.setType(InsuranceOrder.TYPE_MODIFY_TRANSFER_TIME);
        }
        insuranceOrder.setDetail(backDTO.getDetail());
        insuranceOrder.setLastUpdateTime(new Date());
        insuranceOrderManager.updateOrder(insuranceOrder);
    }

    /**
     * 转账时间
     *
     * @param insuranceOrder
     */
    public void modifyTransferTime(InsuranceOrder insuranceOrder) {
        OrderInfoEO order = insuranceOrder.getOrder();
        if (order.getIsBuyInsurance() == null || !order.getIsBuyInsurance())
            return;

        if (order.getEndTime() == null)
            return;

        logger.info("开始调用保单转账时间服务,订单：{}", order.getOrderId());
        XMLGregorianCalendar finishTime = DateUtil.convertToXMLGregorianCalendar(order.getEndTime());
        BackDTO backDTO = null;

        for (int i = 0; i < 5; i++) {
            backDTO = insuranceService.modifyTransferTime(order.getOrderId(), finishTime);
            logger.info("订单:{},调用保单转账时间服务返回:{}/{}", new Object[]{order.getOrderId(), backDTO.getCode(), backDTO.getDetail()});
            if (!isSuccess(backDTO)) {
                logger.info("订单{},调用保单转账时间失败，重试第{}次", order.getOrderId(), (i + 1));
                continue;
            } else {
                break;
            }
        }

        if (isSuccess(backDTO)) {
            // 保单结单成功，将状态设置为已结单
            insuranceOrder.setType(InsuranceOrder.TYPE_ALREADY_MODIFY_TRANSFER_TIME);
        }
        insuranceOrder.setDetail(backDTO.getDetail());
        insuranceOrder.setLastUpdateTime(new Date());
        insuranceOrderManager.updateOrder(insuranceOrder);
    }

    /**
     * 判断是否执行成功
     *
     * @param backDTO
     * @return
     */
    protected boolean isSuccess(BackDTO backDTO) {
        if (backDTO == null
                || StringUtils.isBlank(backDTO.getCode())
                || !StringUtils.equals("0000", backDTO.getCode())) {
            return false;
        }
        return true;
    }

    protected OrderDTO buildOrderDTO(OrderInfoEO order) {
        Date payTime = order.getPayTime();
        if (payTime == null)
            payTime = new Date();
        XMLGregorianCalendar orderPayTime = DateUtil.convertToXMLGregorianCalendar(payTime);

        Date sendTime = order.getSendTime();
        if (sendTime == null)
            sendTime = new Date();
        XMLGregorianCalendar orderFinishedDate = DateUtil.convertToXMLGregorianCalendar(sendTime);

        OrderDTO dto = new OrderDTO();
        dto.setOrderID(order.getOrderId());
        dto.setOrderPrice(order.getTotalPrice());
        dto.setRate(order.getInsuranceRate());
        dto.setBQServicePrice(order.getInsuranceAmount());
        dto.setOrderOp(order.getServicerInfo().getRealName());
        dto.setBuyerId(order.getUid());
        dto.setBuyerName(order.getUserAccount());
        dto.setGameId(order.getGameId());
        dto.setGameAreaId(order.getRegionId());
        dto.setGameServerId(order.getServerId());
        dto.setGameAreaName(order.getRegion());
        dto.setGameServerName(order.getServer());
        dto.setGameCampName(order.getGameRace());
        dto.setBuyerPhone(order.getMobileNumber());
        dto.setBuyerQQ(order.getQq());
        dto.setProductName(order.getTitle());
        dto.setTradingType(10);
        dto.setBQType(BQType.BuyerAnXinBuy);
        dto.setIsPicc(false);
        dto.setStartDate(orderPayTime);
        dto.setEndDate(DateUtil.convertToXMLGregorianCalendar(order.getInsuranceExpireTime()));
        dto.setOrderPayTime(orderPayTime);
        dto.setOrderFinishedDate(orderFinishedDate);
        dto.setBuyerIP("127.0.0.1");

        try {
            // 获取5173用户真实姓名和身份证号
            UserInfo userInfo = userInfoService.getUserInfo(order.getUid());
            if (userInfo != null) {
                dto.setInsuredName(userInfo.getRealName());
                dto.setIdentifynumber(userInfo.getIdCard());
            }
        } catch (Exception e) {
            logger.error("获取5173用户信息出错了", e);
        }

        return dto;
    }
}

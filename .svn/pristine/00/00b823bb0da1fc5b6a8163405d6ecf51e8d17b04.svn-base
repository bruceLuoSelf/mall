package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.DefaultIMEnum;
import com.wzitech.gamegold.common.enums.HxUserType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.order.business.IHxDataManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.HxDataEO;
import com.wzitech.gamegold.order.entity.HxOrderInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汪俊杰 on 2017/9/25.
 */
@Component
public class HxDataManagerImpl implements IHxDataManager {
    private static final String orderFrontendIndex = "YX";

    @Autowired
    IOrderInfoManager orderInfoManager;
    @Autowired
    private ISystemConfigManager systemConfigManager;
    @Autowired
    IDeliveryOrderDao deliveryOrderDao;
    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;
    /**
     * 日志输出
     */
    private static final Logger logger = LoggerFactory.getLogger(HxDataManagerImpl.class);


    @Override
    public HxDataEO selectBuyerHxDataByOrderId(String orderId, int userType) {
        HxDataEO hxData = null;
//        if (userType == HxUserType.Buyer.getCode()) {
        hxData = selectBuyerByOrderId(orderId, userType);
//        }
        return hxData;
    }

    /**
     * g根据订单查找对应的环信订单信息
     *
     * @param orderId
     * @return
     */
    private HxDataEO selectBuyerByOrderId(String orderId, int userType) {
        //当前接口只对买家 卖家开放
        if (userType!=HxUserType.Seller.getCode()&&userType!=HxUserType.Buyer.getCode()){
                throw new SystemException(ResponseCodes.NoPermit.getCode(), ResponseCodes.NoPermit.getMessage());
        }
        //获取当前登录用户
        UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
        if (userInfo == null) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }

        if (orderId.startsWith(orderFrontendIndex)) {
            OrderInfoEO orderInfo = orderInfoManager.selectById(orderId);
            //订单是否存在
            if (orderInfo == null) {
                throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
            }
            //判断是否为自己的订单
            //买家 登录号等于useraccount
            if (userType == HxUserType.Buyer.getCode() && !orderInfo.getUserAccount().equals(userInfo.getLoginAccount())) {
                throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
            //卖家一对多 存在于配单表中即表示有此订单
            if (userType == HxUserType.Seller.getCode()) {
                Map<String, Object> queryMap = new HashMap<String, Object>();
                queryMap.put("orderId", orderId);
                queryMap.put("loginAccount", userInfo.getLoginAccount());
                List<ConfigResultInfoEO> configResultInfoEOGenericPage = configResultInfoDBDAO.selectByMap(queryMap,"ID",true);
                if (configResultInfoEOGenericPage == null || configResultInfoEOGenericPage.size() <= 0) {
                    throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
                }
            }
            //获取客服信息
            UserInfoEO customInfo = orderInfo.getServicerInfo();
            if (customInfo == null) {
                throw new SystemException(ResponseCodes.EmptyAlterServiceID.getCode(), ResponseCodes.EmptyAlterServiceID.getMessage());
            }

            Boolean isOpenHx = false;
            /*SystemConfig config = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.HUANXIN_OPEN.getKey());
            if (config != null && config.getEnabled() && userType == HxUserType.Buyer.getCode()) {
                isOpenHx = true;
            }*/

            HxDataEO hxData = new HxDataEO();
            hxData.setOrderId(orderInfo.getOrderId());
            hxData.setUserType(userType);
            hxData.setCustomServiceLoginId(customInfo.getLoginAccount());
            hxData.setCustomServiceName(customInfo.getNickName());
            hxData.setCustomServiceStatus(customInfo.getIsDeleted() ? 2 : 1);

            //zhulf  start
            //添加系统默认通讯方式
            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.DEFAULT_IM.getKey());
            logger.info("系统默认通讯方式配置:{}",systemConfig);
            if (systemConfig!=null&&systemConfig.getConfigValue()!=null){
                Integer defaultIM = Integer.valueOf(systemConfig.getConfigValue());
                hxData.setDefaultIM(defaultIM);
                if (defaultIM.equals(DefaultIMEnum.HX_COMMUNICATION_TOOL.getCode())
                        && userType == HxUserType.Buyer.getCode()) {
                    isOpenHx = true;
                }
                hxData.setIsOpenHX(isOpenHx);
            }
            hxData.setCustomServiceQQ(customInfo.getQq());
            hxData.setHxCustomService(customInfo.getHxAppAccount());
            hxData.setWyCustomService(customInfo.getYxAccount());
            //end

            //获取订单信息list
            List<HxOrderInfoEO> hxOrderInfoList = getHxOrderInf(orderInfo);
            hxData.setOrderInfo(hxOrderInfoList);
            logger.info("hxData属性设置完成:{}",hxData);
            return hxData;
        } else {
            DeliveryOrder deliveryOrder = deliveryOrderDao.getByOrderId(orderId);
            //判断订单是否存在
            if (deliveryOrder == null) {
                throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
            }
            //判断是否为自己的订单
            //买家 登录号要等于buyerAmount
            if (userType == HxUserType.Buyer.getCode() && !deliveryOrder.getBuyerAccount().equals(userInfo.getLoginAccount())) {
                throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
            //买家登录号要等于sellerAmount
            if (userType == HxUserType.Seller.getCode() && !deliveryOrder.getSellerAccount().equals(userInfo.getLoginAccount())) {
                throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
            HxDataEO hxData = new HxDataEO();
            hxData.setOrderId(deliveryOrder.getOrderId());
            hxData.setUserType(userType);
            hxData.setCustomServiceName(deliveryOrder.getServiceNickname());
            hxData.setCustomServiceQQ(deliveryOrder.getServiceQq());
            //出货单  默认通讯方式配置为QQ
            hxData.setDefaultIM(DefaultIMEnum.QQ_COMMUNICATION_TOOL.getCode());
            hxData.setIsOpenHX(false);
            //获取订单信息list
            List<HxOrderInfoEO> hxOrderInfoEOs = getHxDeliveryOrderInf(deliveryOrder);
            hxData.setOrderInfo(hxOrderInfoEOs);
            return hxData;
        }
    }

    /**
     * 拼接订单内容list
     *
     * @param orderInfo
     * @return
     */
    private List<HxOrderInfoEO> getHxOrderInf(OrderInfoEO orderInfo) {
        List<HxOrderInfoEO> dataList = new ArrayList<HxOrderInfoEO>();
        //订单是否存在
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
        }

        //区服信息
        String regionData = "";
        if (StringUtils.isNotBlank(orderInfo.getRegion())) {
            regionData += orderInfo.getRegion();
        }
        if (StringUtils.isNotBlank(orderInfo.getServer())) {
            regionData += "/" + orderInfo.getServer();
        }
        if (StringUtils.isNotBlank(orderInfo.getGameRace())) {
            regionData += "/" + orderInfo.getGameRace();
        }

        //购买数量信息，数量+单位
        String buyGoodsData = "";
        if (orderInfo.getGoldCount() != null) {
            buyGoodsData += orderInfo.getGoldCount();
        }
        if (StringUtils.isNotBlank(orderInfo.getMoneyName())) {
            buyGoodsData += " " + orderInfo.getMoneyName();
        }

        HxOrderInfoEO hxOrderId = new HxOrderInfoEO();
        hxOrderId.setKey("订单号");
        hxOrderId.setValue(orderInfo.getOrderId());
        dataList.add(hxOrderId);

        HxOrderInfoEO hxGoodsName = new HxOrderInfoEO();
        hxGoodsName.setKey("商品名称");
        hxGoodsName.setValue(orderInfo.getTitle());
        dataList.add(hxGoodsName);

        HxOrderInfoEO hxGoodsData = new HxOrderInfoEO();
        hxGoodsData.setKey("购买数量");
        hxGoodsData.setValue(buyGoodsData);
        dataList.add(hxGoodsData);

        HxOrderInfoEO hxGameName = new HxOrderInfoEO();
        hxGameName.setKey("游戏");
        hxGameName.setValue(orderInfo.getGameName());
        dataList.add(hxGameName);

        HxOrderInfoEO hxRegionData = new HxOrderInfoEO();
        hxRegionData.setKey("区服");
        hxRegionData.setValue(regionData);
        dataList.add(hxRegionData);

        HxOrderInfoEO hxGoodsTypeName = new HxOrderInfoEO();
        hxGoodsTypeName.setKey("类型");
        hxGoodsTypeName.setValue(orderInfo.getGoodsTypeName());
        dataList.add(hxGoodsTypeName);

        return dataList;
    }


    private List<HxOrderInfoEO> getHxDeliveryOrderInf(DeliveryOrder deliveryOrder) {
        List<HxOrderInfoEO> dataList = new ArrayList<HxOrderInfoEO>();
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
        }

        //区服信息
        String regionData = "";
        if (StringUtils.isNotBlank(deliveryOrder.getRegion())) {
            regionData += deliveryOrder.getRegion();
        }
        if (StringUtils.isNotBlank(deliveryOrder.getServer())) {
            regionData += deliveryOrder.getServer();
        }
        if (StringUtils.isNotBlank(deliveryOrder.getGameRace())) {
            regionData += deliveryOrder.getGameRace();
        }

        //购买数量信息， 数量+单位
        String buyGoodsData = "";
        if (deliveryOrder.getCount() != null) {
            buyGoodsData += deliveryOrder.getCount();
        }
        if (StringUtils.isNotBlank(deliveryOrder.getMoneyName())) {
            buyGoodsData += deliveryOrder.getMoneyName();
        }

        HxOrderInfoEO hxorderId = new HxOrderInfoEO();
        hxorderId.setKey("订单号");
        hxorderId.setValue(deliveryOrder.getOrderId());
        dataList.add(hxorderId);

        HxOrderInfoEO hxGoodsName = new HxOrderInfoEO();
        hxGoodsName.setKey("商品名称");
        hxGoodsName.setValue(ServicesContants.GOODS_TYPE_GOLD);
        dataList.add(hxGoodsName);

        HxOrderInfoEO hxGoodsData = new HxOrderInfoEO();
        hxGoodsData.setKey("购买数量");
        hxGoodsData.setValue(buyGoodsData);
        dataList.add(hxGoodsData);

        HxOrderInfoEO hxGameName = new HxOrderInfoEO();
        hxGameName.setKey("游戏");
        hxGameName.setValue(deliveryOrder.getGameName());
        dataList.add(hxGameName);

        HxOrderInfoEO hxRegionData = new HxOrderInfoEO();
        hxRegionData.setKey("区服");
        hxRegionData.setValue(regionData);
        dataList.add(hxRegionData);

        HxOrderInfoEO hxGoodsTypeName = new HxOrderInfoEO();
        hxGoodsTypeName.setKey("类型");
        hxGoodsTypeName.setValue(deliveryOrder.getGoodsTypeName());
        dataList.add(hxGoodsTypeName);

        return dataList;
    }

}

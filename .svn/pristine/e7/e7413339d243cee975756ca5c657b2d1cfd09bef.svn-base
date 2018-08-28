package com.wzitech.gamegold.order.business.impl;

import com.google.common.collect.Maps;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.common.main.IMainGerIdUtil;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.IGoodsInfoDBDAO;
import com.wzitech.gamegold.goods.dao.IReferencePriceDao;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.order.dao.*;
import com.wzitech.gamegold.order.dto.SimpleOrderDTO;
import com.wzitech.gamegold.order.entity.*;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.business.ISellerSettingManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单管理接口实现类
 *
 * @author SunChengfei
 *         <p>
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/16    wrf              ZW_C_JB_00008商城增加通货
 */
@Component
public class OrderInfoManagerImpl extends AbstractBusinessObject implements
        IOrderInfoManager {
    protected static final Log log = LogFactory.getLog(OrderInfoManagerImpl.class);

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    IOrderIdRedisDAO orderIdRedisDAO;

    @Autowired
    IOrderInfoRedisDAO orderInfoRedisDAO;

    @Autowired
    IServicerOrderManager servicerOrderManager;

    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    IOrderConfigLockRedisDAO orderConfigLockRedisDAO;

    @Autowired
    IRepositoryManager repositoryManager;

    @Autowired
    IRepositoryConfineManager repositoryConfineManager;

    @Autowired
    IShippingInfoDBDAO shippingInfoDBDAO;

    @Autowired
    IGoodsInfoManager goodsInfoManager;

    /*@Autowired
    ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

    @Autowired
    IAutoPayManager autoPayManager;

    @Autowired
    IOrderStateManager orderStateManager;

    @Autowired
    IOrderConfigManager orderConfigManager;

    @Autowired
    IGameInfoManager gameInfoManager;

    @Autowired
    IConfigPowerDBDAO configPowerDBDAO;

    @Autowired
    private IArrangeTraderManager arrangeTraderManager;

    @Autowired
    private IInsuranceSettingsManager insuranceSettingsManager;

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IServiceSortManager serviceSortManager;

    @Autowired
    ISellerManager sellerManager;

    @Autowired
    ISellerSettingManager sellerSettingManager;

    @Autowired
    IGameConfigManager gameConfigManager;

    @Autowired
    ICommissionManager commissionManager;

    @Autowired
    IDiscountCouponManager discountCouponManager;

    @Autowired
    IReferencePriceDao referencePriceDao;

    @Autowired
    IGoodsInfoDBDAO goodsInfoDBDAO;

    @Autowired
    IShGameConfigManager shGameConfigManager;


    @Autowired
    IMainGerIdUtil mainGerIdUtil;

    @Autowired
    ImqUtilForOrderCenterToMain mqUtilForOrderCenterToMain;

    @Autowired
    IOrderPushMainManager orderPushMainManager;

    @Autowired
    ICurrencyConfigDBDAO currencyConfigDBDAO;



    @Override
    @Transactional
    public boolean autoPay(OrderInfoEO orderInfo) {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }

        //调用自动打款接口
        log.info("调用自动打款接口");
        //修改订单状态为结单，并进行打款
        OrderInfoEO result = orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.Statement);
        if (result != null) {
            log.info("调用自动打款接口成功");
            return true;
        } else {
            log.info("调用自动打款接口失败");
        }
        return false;
    }

    @Override
    @Transactional
    public OrderInfoEO addOrderInfo(OrderInfoEO orderInfo, BigDecimal discount)
            throws Exception {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }

        if (orderInfo.getUid() == null) {
            throw new SystemException(ResponseCodes.EmptyUid.getCode(),
                    ResponseCodes.EmptyUid.getMessage());
        }
        if (orderInfo.getUserAccount() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyLoginAccount.getCode(),
                    ResponseCodes.EmptyLoginAccount.getMessage());
        }
        /*if (orderInfo.getServicerId() == null) {
            throw new SystemException(ResponseCodes.EmptyServicerId.getCode(),
					ResponseCodes.EmptyServicerId.getMessage());
		}*/
        /*if (orderInfo.getGameLevel()<=0) {
            throw new SystemException(ResponseCodes.IllegalGameLevel.getCode(),
					ResponseCodes.IllegalGameLevel.getMessage());
		}*/
        if (StringUtils.isEmpty(orderInfo.getMobileNumber())) {
            throw new SystemException(ResponseCodes.EmptyMobilePhone.getCode(),
                    ResponseCodes.EmptyMobilePhone.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getQq())) {
            throw new SystemException(ResponseCodes.EmptyQQ.getCode(),
                    ResponseCodes.EmptyQQ.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getReceiver())) {
            throw new SystemException(ResponseCodes.EmptyReceiver.getCode(),
                    ResponseCodes.EmptyReceiver.getMessage());
        }
        if (orderInfo.getGoodsId() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsId.getCode(),
                    ResponseCodes.EmptyGoodsId.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(orderInfo.getRegion())) {
            throw new SystemException(ResponseCodes.EmptyRegion.getCode(),
                    ResponseCodes.EmptyRegion.getCode());
        }
        if (StringUtils.isEmpty(orderInfo.getServer())) {
            throw new SystemException(ResponseCodes.EmptyGameServer.getCode(),
                    ResponseCodes.EmptyGameServer.getCode());
        }
        /**
         * 商品类目是否为空判断 ZW_C_JB_00008_20170516
         */
        if (StringUtils.isEmpty(orderInfo.getGoodsTypeName())) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeName.getCode(),
                    ResponseCodes.EmptyGoodsTypeName.getCode());
        }
//        Map<String,Object> currentyMap=new HashMap<String, Object>();
//        currentyMap.put("gameName",orderInfo.getGameName());
//        currentyMap.put("goodsType",orderInfo.getGoodsTypeName());
//        currentyMap.put("enabled",true);
//        List<CurrencyConfigEO> currencyConfigEO=currencyConfigDBDAO.selectByMap(currentyMap);
//        JSONObject jsonObject=JSONObject.fromObject(orderInfo.getField());
//        for(CurrencyConfigEO currencyConfigEO1:currencyConfigEO){
//            if(StringUtils.isBlank(jsonObject.getString(currencyConfigEO1.getFieldMeaning()))){
//                throw new SystemException(ResponseCodes.EmptyCurrencyConfig.getCode(),ResponseCodes.EmptyCurrencyConfig.getMessage());
//            }
//        }
        //地下城与勇士 挑战书时 没有游戏等级 抛异常
        if (orderInfo.getGameName().equals("地下城与勇士") && orderInfo.getGoodsTypeName().equals("挑战书") && (null == orderInfo.getGameLevel() || orderInfo.getGameLevel() <= 0)) {
            throw new SystemException(ResponseCodes.IllegalGameLevel.getCode(), ResponseCodes.IllegalGameLevel.getMessage());
        }
        //新天龙八部 元宝票 没有数字id 抛异常
        if (orderInfo.getGameName().equals("新天龙八部") && orderInfo.getGoodsTypeName().equals("元宝票") && StringUtils.isBlank(orderInfo.getGameNumberId())) {
            throw new SystemException(ResponseCodes.IllegalGameNumberID.getCode(), ResponseCodes.IllegalGameNumberID.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getGameId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(), orderInfo.getGameName().trim(), 1);
            if (gameNameAndId != null) {
                orderInfo.setGameId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyGameId.getCode(),
                        ResponseCodes.EmptyGameId.getCode());
            }
        }
        if (StringUtils.isEmpty(orderInfo.getRegionId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(),
                    orderInfo.getRegion().trim(), 2);
            if (gameNameAndId != null) {
                orderInfo.setRegionId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyRegionId.getCode(),
                        ResponseCodes.EmptyRegionId.getCode());
            }
        }
        if (StringUtils.isEmpty(orderInfo.getServerId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(),
                    orderInfo.getServer().trim(), 3);
            if (gameNameAndId != null) {
                orderInfo.setServerId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyServerId.getCode(),
                        ResponseCodes.EmptyServerId.getCode());
            }
        }
        if (StringUtils.isEmpty(orderInfo.getTitle())) {
            throw new SystemException(ResponseCodes.EmptyGoodsTitle.getCode(),
                    ResponseCodes.EmptyGoodsTitle.getCode());
        }
        if (orderInfo.getUnitPrice() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsPrice.getCode(),
                    ResponseCodes.EmptyGoodsPrice.getMessage());
        }
        if (orderInfo.getMoneyName() == null) {
            throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(),
                    ResponseCodes.EmptyMoneyName.getMessage());
        }
        if (orderInfo.getGoldCount() == null || orderInfo.getGoldCount() <= 0) {
            throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(),
                    ResponseCodes.EmptyGoldCount.getMessage());
        }
        if (orderInfo.getGoodsCat() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsCat.getCode(), ResponseCodes.EmptyGoodsCat.getMessage());
        }

        if (orderInfo.getDeliveryTime() == null) {
            // 发货速度默认20
            orderInfo.setDeliveryTime(20);
            /*throw new SystemException(
                    ResponseCodes.EmptyDeliveryTime.getCode(),
					ResponseCodes.EmptyDeliveryTime.getMessage());*/
        }

        // 根据商品ID获取商品单价
        BigDecimal unitPirce = null;
        if (orderInfo.getGoodsCat() == GoodsCat.Cat2.getCode()) {
            // 栏目2从商品表获取单价
            GoodsInfo goodsInfo = goodsInfoManager.selectById(orderInfo.getGoodsId());
            if (goodsInfo != null && goodsInfo.getUnitPrice() != null) {
                unitPirce = goodsInfo.getUnitPrice();
            }
        } else if (orderInfo.getGoodsCat() == GoodsCat.Cat1.getCode() || orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()) {
            // 栏目1和3，从库存表获取单价
            RepositoryInfo repositoryInfo = repositoryManager.selectById(orderInfo.getGoodsId());
            if (repositoryInfo != null && repositoryInfo.getUnitPrice() != null) {
                unitPirce = repositoryInfo.getUnitPrice();
            }
        }
        if (unitPirce != null) {
            // 更新订单价格为数据库中查询出的单价
            orderInfo.setUnitPrice(unitPirce);
        } else {
            // 未找到单价则以传上来的单价为准
            unitPirce = orderInfo.getUnitPrice();
        }

        // 检查库存数量，如果是栏目1或栏目3的，都需要检查购买的金币数量是否大于库存数量
        if (orderInfo.getGoodsCat() == GoodsCat.Cat1.getCode() || orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()) {
            long maxGoldCount = 0;
            if (orderInfo.getGoodsCat() == GoodsCat.Cat1.getCode()) {
                maxGoldCount = repositoryManager.queryRepositoryMaxCount(orderInfo.getGameName(), orderInfo.getGoodsTypeName(), orderInfo.getRegion(),
                        orderInfo.getServer(), orderInfo.getGameRace(), null, unitPirce);
            } else if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()) {
                if (StringUtils.isBlank(orderInfo.getSellerLoginAccount())) {
                    throw new SystemException(ResponseCodes.EmptyGoodsSeller.getCode(), ResponseCodes.EmptyGoodsSeller.getMessage());
                }
                maxGoldCount = repositoryManager.queryRepositoryMaxCount(orderInfo.getGameName(), orderInfo.getGoodsTypeName(), orderInfo.getRegion(),
                        orderInfo.getServer(), orderInfo.getGameRace(), orderInfo.getSellerLoginAccount(), unitPirce);
            }
            if (maxGoldCount < orderInfo.getGoldCount()) {
                // 库存数量不足
                throw new SystemException(ResponseCodes.LowCapacity.getCode(), ResponseCodes.LowCapacity.getMessage());
            }
        }

        // 计算订单金额
        if (orderInfo.getIsBuyInsurance() == null || !orderInfo.getIsBuyInsurance()) {
            orderInfo.setTotalPrice(orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount())));
        } else if (orderInfo.getIsBuyInsurance()) {
            BigDecimal totalPrice = orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount()));
            BigDecimal insuranceAmount = BigDecimal.ZERO;

            // 查询保险设置，设置保费信息
            InsuranceSettings insuranceSettings = insuranceSettingsManager.queryByGameName(orderInfo.getGameName());
            if (insuranceSettings != null) {
                if (insuranceSettings.getEnabled()) {
                    if (orderInfo.getIsBuyInsurance()) {
                        // 计算保费
                        BigDecimal percent = new BigDecimal("100");
                        percent = insuranceSettings.getRate().divide(percent);
                        insuranceAmount = percent.multiply(totalPrice);
                        insuranceAmount = insuranceAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

                        if (insuranceAmount.compareTo(insuranceSettings.getCeiling()) == 1) {
                            // 不能超过保费上限
                            insuranceAmount = insuranceSettings.getCeiling();
                        } else if (insuranceAmount.compareTo(insuranceSettings.getFloor()) == -1) {
                            // 不能低于保费下限
                            insuranceAmount = insuranceSettings.getFloor();
                        }

                        // 设置保险有效期
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, insuranceSettings.getExpireDay());

                        orderInfo.setInsuranceAmount(insuranceAmount);
                        orderInfo.setInsuranceRate(percent);
                        orderInfo.setInsuranceExpireTime(calendar.getTime());
                    }
                } else {
                    orderInfo.setIsBuyInsurance(false);
                }
            } else {
                orderInfo.setIsBuyInsurance(false);
            }

            totalPrice = totalPrice.add(insuranceAmount);
            orderInfo.setTotalPrice(totalPrice);
        }

        // 购买金额，客服服务费+游戏币金额, 但不包含商品保障险金额
        BigDecimal amount = orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount()));

        // 没有选择客服的，自动分配一个客服，不包括视频客服
        if (orderInfo.getServicerId() == null) {
            //Long serviceId = serviceSortManager.getNextServiceId(UserType.AssureService.getCode(), orderInfo.getGameName());
//            List<UserInfoEO> users = userInfoManager.queryAssureServiceByGameNoCache(orderInfo.getGameName());
//            Long serviceId = null;
//            if (!CollectionUtils.isEmpty(users)) {
//                for (UserInfoEO user : users) {
//                    if (user.getServiceCharge() == 0) {
//                        serviceId = user.getId();
//                        break;
//                    }
//                    continue;
//                }
//            }

            Long serviceId = null;
            UserInfoEO user = userInfoManager.queryAssureServiceIDByGame(orderInfo.getGameName());
            if (user != null) {
                serviceId = user.getId();
            }
            if (serviceId == null) {
                throw new SystemException(ResponseCodes.EmptyServicer.getCode(), ResponseCodes.EmptyServicer.getMessage());
            }
            orderInfo.setServicerId(serviceId);
        } else {
            // 获取客服信息,如果是收费客服，则加上该客服服务费
            UserInfoEO service = userInfoManager.findUserByUid(orderInfo.getServicerId().toString());
            if (service.getServiceCharge() > 0) {
                BigDecimal serviceCharge = new BigDecimal(service.getServiceCharge());
                BigDecimal totalPrice = orderInfo.getTotalPrice().add(serviceCharge);
                orderInfo.setTotalPrice(totalPrice);
                orderInfo.setServiceCharge(service.getServiceCharge());
                amount = amount.add(serviceCharge);
            }
        }
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

        orderInfo.setTotalPrice(orderInfo.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));

        /*****************新增根据游戏名称和商品类型查询最低购买金额_20170607_MODIFY_START***********/
        ShGameConfig config = shGameConfigManager.getConfigByGameName(orderInfo.getGameName(), orderInfo.getGoodsTypeName(), null, null);
        if (config != null && config.getMinBuyAmount() != null) {
            BigDecimal minBuyAmount = config.getMinBuyAmount();
            if (orderInfo.getTotalPrice().compareTo(minBuyAmount) < 0) {
                //如果订单的购买金额<游戏配置最小购买金额
                throw new SystemException(ResponseCodes.notEnoughOrderPrice.getCode(),
                        MessageFormat.format(ResponseCodes.notEnoughOrderPrice.getMessage(), minBuyAmount));
            }
        }
        //增加订单时商品类型id插入
        orderInfo.setGoodsTypeId(config == null ? null : config.getGoodsTypeId());

        /****************新增根据游戏名称和商品ID查询最低购买金额_20170607_MODIFY_END*************/

        //订单总价不能超过999999元
        if (orderInfo.getTotalPrice().intValue() > 999999) {
            throw new SystemException(ResponseCodes.ExceedOrderMaxPrice.getCode(),
                    ResponseCodes.ExceedOrderMaxPrice.getMessage());
        }

        // 添加订单附加信息
        String orderId = orderIdRedisDAO.getOrderId();
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderState(OrderState.WaitPayment.getCode()); // 等待付款
        orderInfo.setCreateTime(new Date()); // 下单时间

        // 验证红包是否有效
        if (StringUtils.isNotBlank(orderInfo.getRedEnvelopeCode())
                && StringUtils.isNotBlank(orderInfo.getRedEnvelopePwd())) {
            DiscountCoupon discountCoupon = discountCouponManager.selectUniqueDisCountCoupon(orderInfo.getRedEnvelopeCode(),
                    orderInfo.getRedEnvelopePwd(), CouponType.Hb.getCode(), amount.doubleValue());

            if (discountCoupon != null) {
                // 更新红包为已使用
                discountCouponManager.UpdateCouponUsed(orderInfo.getRedEnvelopeCode(), orderId, CouponType.Hb.getCode());

                // 记录红包金额
                orderInfo.setRedEnvelope(discountCoupon.getPrice());
                // 订单金额=订单金额-红包金额
                BigDecimal totalPrice = orderInfo.getTotalPrice().subtract(new BigDecimal(orderInfo.getRedEnvelope()));
                orderInfo.setTotalPrice(totalPrice);
            }
        }
        // 卖家店铺商品，验证店铺券是否有效
        if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
                && StringUtils.isNotBlank(orderInfo.getShopCouponCode())
                && StringUtils.isNotBlank(orderInfo.getShopCouponPwd())) {
            DiscountCoupon discountCoupon = discountCouponManager.selectUniqueDisCountCoupon(orderInfo.getShopCouponCode(),
                    orderInfo.getShopCouponPwd(), CouponType.Dp.getCode(), amount.doubleValue());

            if (discountCoupon != null) {
                // 更新店铺券为已使用
                discountCouponManager.UpdateCouponUsed(orderInfo.getShopCouponCode(), orderId, CouponType.Dp.getCode());

                // 记录店铺券金额
                orderInfo.setShopCoupon(discountCoupon.getPrice());
                // 订单金额=订单金额-店铺券金额
                BigDecimal totalPrice = orderInfo.getTotalPrice().subtract(new BigDecimal(orderInfo.getShopCoupon()));
                orderInfo.setTotalPrice(totalPrice);
            }
        }

        // 订单没有设置交易方式的，先更新订单交易方式
        if (orderInfo.getTradeType() == null) {
            ConfigPowerEO configPowerInfo = configPowerDBDAO.getByGameName(orderInfo.getGameName(), orderInfo.getGoodsTypeName());
            if (configPowerInfo != null) {
                if (configPowerInfo.getTradeType() == TradeType.Divided.getCode()) {// 邮寄交易
                    orderInfo.setTradeType(TradeType.Divided.getCode());
                } else if (configPowerInfo.getTradeType() == TradeType.UnionTrading.getCode()) {// 公会交易
                    orderInfo.setTradeType(TradeType.UnionTrading.getCode());
                } else { // 当面交易
                    orderInfo.setTradeType(TradeType.NoDivid.getCode());
                }
            } else {
                orderInfo.setTradeType(TradeType.NoDivid.getCode()); // 默认当面交易
            }
        }
        if (orderInfo.getRefererType() == null) {
            //订单来源为空时默认设置为金币商城内部
            orderInfo.setRefererType(RefererType.goldOrder.getCode());
        }

        // 保存订单
        orderInfoDBDAO.insert(orderInfo);
        //修改订单状态为待付款
        orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.WaitPayment);

        orderInfoRedisDAO.saveOrder(orderInfo);

        // 增加订单新增日志
        StringBuffer sb = new StringBuffer();
        sb.append("创建订单，订单号：").append(orderInfo.getOrderId()).append("，单价：")
                .append(orderInfo.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP)).append("元，总价：")
                .append(orderInfo.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)).append("元");
        //logManager.add(ModuleType.ORDER, sb.toString(),CurrentUserContext.getUser());
        log.info(sb.toString());
        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_CREATE);
        logInfo.setOrderId(orderInfo.getOrderId());
        logInfo.setRemark(sb.toString());
        orderLogManager.add(logInfo);

        return orderInfo;
    }

    @Override
    @Transactional
    public OrderInfoEO addOrderInfoFromM(OrderInfoEO orderInfo) throws SystemException, Exception {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        //因为M站没有客服 这里设置为NULL以便走后面分配客服的逻辑,后面分配客服逻辑不删除 留以后拓展
        orderInfo.setServicerId(null);
        if (StringUtils.isBlank(orderInfo.getUid())) {
            throw new SystemException(ResponseCodes.EmptyUid.getCode(),
                    ResponseCodes.EmptyUid.getMessage());
        }
        if (StringUtils.isBlank(orderInfo.getUserAccount())) {
            throw new SystemException(
                    ResponseCodes.EmptyLoginAccount.getCode(),
                    ResponseCodes.EmptyLoginAccount.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getMobileNumber())) {
            throw new SystemException(ResponseCodes.EmptyMobilePhone.getCode(),
                    ResponseCodes.EmptyMobilePhone.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getQq())) {
            throw new SystemException(ResponseCodes.EmptyQQ.getCode(),
                    ResponseCodes.EmptyQQ.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getReceiver())) {
            throw new SystemException(ResponseCodes.EmptyReceiver.getCode(),
                    ResponseCodes.EmptyReceiver.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(orderInfo.getRegion())) {
            throw new SystemException(ResponseCodes.EmptyRegion.getCode(),
                    ResponseCodes.EmptyRegion.getCode());
        }
        if (StringUtils.isEmpty(orderInfo.getServer())) {
            throw new SystemException(ResponseCodes.EmptyGameServer.getCode(),
                    ResponseCodes.EmptyGameServer.getCode());
        }
        if (StringUtils.isEmpty(orderInfo.getGoodsTypeName())) {
            throw new SystemException(ResponseCodes.EmptyGoodsType.getCode(),
                    ResponseCodes.EmptyGoodsType.getMessage());
        }
        if (StringUtils.isBlank(orderInfo.getGameId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(), orderInfo.getGameName().trim(), 1);
            if (gameNameAndId != null) {
                orderInfo.setGameId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyGameId.getCode(),
                        ResponseCodes.EmptyGameId.getCode());
            }
        }
        if (StringUtils.isBlank(orderInfo.getRegionId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(),
                    orderInfo.getRegion().trim(), 2);
            if (gameNameAndId != null) {
                orderInfo.setRegionId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyRegionId.getCode(),
                        ResponseCodes.EmptyRegionId.getCode());
            }
        }
        if (StringUtils.isBlank(orderInfo.getServerId())) {
            GameNameAndId gameNameAndId = gameInfoManager.getIdByProp(orderInfo.getGameName().trim(),
                    orderInfo.getServer().trim(), 3);
            if (gameNameAndId != null) {
                orderInfo.setServerId(gameNameAndId.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyServerId.getCode(),
                        ResponseCodes.EmptyServerId.getCode());
            }
        }
        if (orderInfo.getUnitPrice() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsPrice.getCode(),
                    ResponseCodes.EmptyGoodsPrice.getMessage());
        }
        if (orderInfo.getMoneyName() == null) {
            throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(),
                    ResponseCodes.EmptyMoneyName.getMessage());
        }
        if (orderInfo.getGoldCount() == null || orderInfo.getGoldCount() <= 0) {
            throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(),
                    ResponseCodes.EmptyGoldCount.getMessage());
        }
        //        Map<String,Object> currentyMap=new HashMap<String, Object>();
//        currentyMap.put("gameName",orderInfo.getGameName());
//        currentyMap.put("goodsType",orderInfo.getGoodsTypeName());
//        currentyMap.put("enabled",true);
//        List<CurrencyConfigEO> currencyConfigEO=currencyConfigDBDAO.selectByMap(currentyMap);
//        JSONObject jsonObject=JSONObject.fromObject(orderInfo.getField());
//        for(CurrencyConfigEO currencyConfigEO1:currencyConfigEO){
//            if(StringUtils.isBlank(jsonObject.getString(currencyConfigEO1.getFieldMeaning()))){
//                throw new SystemException(ResponseCodes.EmptyCurrencyConfig.getCode(),ResponseCodes.EmptyCurrencyConfig.getMessage());
//            }
//        }
        //更新单价
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setGameName(orderInfo.getGameName());
        repositoryInfo.setRegion(orderInfo.getRegion());
        repositoryInfo.setServer(orderInfo.getServer());
        if (StringUtils.isNotBlank(orderInfo.getGameRace())) {
            repositoryInfo.setGameRace(orderInfo.getGameRace());
        }
        repositoryConfineManager.selectByMinUnitPrice(repositoryInfo);
        //检查当前的库存与单价
        ReferencePrice referencePrice = new ReferencePrice();
        referencePrice.setGameName(orderInfo.getGameName());
        referencePrice.setRegionName(orderInfo.getRegion());
        referencePrice.setServerName(orderInfo.getServer());
        referencePrice.setRaceName(orderInfo.getGameRace());
        referencePrice.setGoodsTypeName(orderInfo.getGoodsTypeName());
        ReferencePrice referencePriceFromDB = referencePriceDao.selectByGameNameAndRegionNameAndServerNameAndRaceName(referencePrice);
        if (null == referencePriceFromDB) {
            throw new SystemException(ResponseCodes.IllegalConfigData.getCode(),
                    ResponseCodes.IllegalConfigData.getMessage());
        }
        BigDecimal referenceUnitPrice = referencePriceFromDB.getUnitPrice();
        if (null == referenceUnitPrice) {
            throw new SystemException(ResponseCodes.NullUnitPrice.getCode(),
                    ResponseCodes.NullUnitPrice.getMessage());
        }
        if (null == referencePriceFromDB.getTotalAccount()) {
            throw new SystemException(ResponseCodes.NullTotalCount.getCode(),
                    ResponseCodes.NullTotalCount.getMessage());
        }
        //单价比对
        //如果从数据库里查询的参考价与页面上获取的单价做比较,如果参考价比单价要大就返回错误信息,如果是等于或者小于单价就走正常流程
        if (referenceUnitPrice.compareTo(orderInfo.getUnitPrice()) > 0) {
            //手速太慢单价已变
            throw new SystemException(ResponseCodes.UnitPriceHasChanged.getCode(),
                    ResponseCodes.UnitPriceHasChanged.getMessage());
        }
        BigInteger totalAcount = referencePriceFromDB.getTotalAccount();
        Long goldCount = orderInfo.getGoldCount();
        //九宫格最小购买金额与当前金额下购买金额比对
        //这里利用从数据库里查询的最新单价再次对购买金额与最低购买金额作一个判断
        BigDecimal totalPriceToPay = referenceUnitPrice.multiply(new BigDecimal(goldCount.toString()));
        BigDecimal formatTotalPrice = totalPriceToPay.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (orderInfo.getLimitPrice().compareTo(formatTotalPrice) > 0) {
            throw new SystemException(ResponseCodes.IllegalGoldCount.getCode(),
                    ResponseCodes.IllegalGoldCount.getMessage());
        }
        //库存与下单数量比
        //如果库存比下单数量要少,进入后续比对逻辑,返回错误信息即可
        if (totalAcount.compareTo(new BigInteger(goldCount.toString())) < 0) {
            throw new SystemException(ResponseCodes.RepositoryGoldNotEnough.getCode(),
                    ResponseCodes.RepositoryGoldNotEnough.getMessage());
        }
        //下的商城的订单就是商城最低价商品
        orderInfo.setGoodsCat(GoodsCat.Cat1.getCode());
        if (orderInfo.getDeliveryTime() == null) {
            // 发货速度默认20
            orderInfo.setDeliveryTime(20);
            /*throw new SystemException(
                    ResponseCodes.EmptyDeliveryTime.getCode(),
					ResponseCodes.EmptyDeliveryTime.getMessage());*/
        }
        // 计算订单金额，M站是没有保费的,故不会走else if 但是留着以免以后要用
        if (orderInfo.getIsBuyInsurance() == null || !orderInfo.getIsBuyInsurance()) {
            orderInfo.setTotalPrice(orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount())));
        } else if (orderInfo.getIsBuyInsurance()) {
            BigDecimal totalPrice = orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount()));
            BigDecimal insuranceAmount = BigDecimal.ZERO;

            // 查询保险设置，设置保费信息
            InsuranceSettings insuranceSettings = insuranceSettingsManager.queryByGameName(orderInfo.getGameName());
            if (insuranceSettings != null) {
                if (insuranceSettings.getEnabled()) {
                    if (orderInfo.getIsBuyInsurance()) {
                        // 计算保费
                        BigDecimal percent = new BigDecimal("100");
                        percent = insuranceSettings.getRate().divide(percent);
                        insuranceAmount = percent.multiply(totalPrice);
                        insuranceAmount = insuranceAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

                        if (insuranceAmount.compareTo(insuranceSettings.getCeiling()) == 1) {
                            // 不能超过保费上限
                            insuranceAmount = insuranceSettings.getCeiling();
                        } else if (insuranceAmount.compareTo(insuranceSettings.getFloor()) == -1) {
                            // 不能低于保费下限
                            insuranceAmount = insuranceSettings.getFloor();
                        }

                        // 设置保险有效期
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, insuranceSettings.getExpireDay());

                        orderInfo.setInsuranceAmount(insuranceAmount);
                        orderInfo.setInsuranceRate(percent);
                        orderInfo.setInsuranceExpireTime(calendar.getTime());
                    }
                } else {
                    orderInfo.setIsBuyInsurance(false);
                }
            } else {
                orderInfo.setIsBuyInsurance(false);
            }

            totalPrice = totalPrice.add(insuranceAmount);
            orderInfo.setTotalPrice(totalPrice);
        }

        // 购买金额，客服服务费+游戏币金额, 但不包含商品保障险金额,M站暂时没有客服服务费与保障险
        BigDecimal amount = orderInfo.getUnitPrice().multiply(new BigDecimal(orderInfo.getGoldCount()));

        // 没有选择客服的，自动分配一个客服，不包括视频客服,M站是没有客服的
        if (orderInfo.getServicerId() == null) {
            //Long serviceId = serviceSortManager.getNextServiceId(UserType.AssureService.getCode(), orderInfo.getGameName());
//            List<UserInfoEO> users = userInfoManager.queryAssureServiceByGameNoCache(orderInfo.getGameName());
//            Long serviceId = null;
//            if (!CollectionUtils.isEmpty(users)) {
//                for (UserInfoEO user : users) {
//                    if (user.getServiceCharge() == 0) {
//                        serviceId = user.getId();
//                        break;
//                    }
//                    continue;
//                }
//            }

            Long serviceId = null;
            UserInfoEO user = userInfoManager.queryAssureServiceIDByGame(orderInfo.getGameName());
            if (user != null) {
                serviceId = user.getId();
            }
            if (serviceId == null) {
                throw new SystemException(ResponseCodes.EmptyServicer.getCode(), ResponseCodes.EmptyServicer.getMessage());
            }
            orderInfo.setServicerId(serviceId);
        }
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

        orderInfo.setTotalPrice(orderInfo.getTotalPrice().setScale(2, RoundingMode.UP));
        if (orderInfo.getTotalPrice().compareTo(new BigDecimal("999999.9999")) >= 0) {
            throw new SystemException(ResponseCodes.TooMuchTotalAccount.getCode(),
                    ResponseCodes.TooMuchTotalAccount.getMessage());
        }
        // 添加订单附加信息
        String orderId = orderIdRedisDAO.getOrderId();
        logger.info("订单号:{}，来源m站/app", orderId);
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderState(OrderState.WaitPayment.getCode()); // 等待付款
        orderInfo.setCreateTime(new Date()); // 下单时间

        // 验证红包是否有效
        if (StringUtils.isNotBlank(orderInfo.getRedEnvelopeCode())
                && StringUtils.isNotBlank(orderInfo.getRedEnvelopePwd())) {
            DiscountCoupon discountCoupon = discountCouponManager.selectUniqueDisCountCoupon(orderInfo.getRedEnvelopeCode(),
                    orderInfo.getRedEnvelopePwd(), CouponType.Hb.getCode(), amount.doubleValue());

            if (discountCoupon != null) {
                // 更新红包为已使用
                discountCouponManager.UpdateCouponUsed(orderInfo.getRedEnvelopeCode(), orderId, CouponType.Hb.getCode());

                // 记录红包金额
                orderInfo.setRedEnvelope(discountCoupon.getPrice());
                // 订单金额=订单金额-红包金额
                BigDecimal totalPrice = orderInfo.getTotalPrice().subtract(new BigDecimal(orderInfo.getRedEnvelope()));
                orderInfo.setTotalPrice(totalPrice);
            }
        }
        // 卖家店铺商品，验证店铺券是否有效
        if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
                && StringUtils.isNotBlank(orderInfo.getShopCouponCode())
                && StringUtils.isNotBlank(orderInfo.getShopCouponPwd())) {
            DiscountCoupon discountCoupon = discountCouponManager.selectUniqueDisCountCoupon(orderInfo.getShopCouponCode(),
                    orderInfo.getShopCouponPwd(), CouponType.Dp.getCode(), amount.doubleValue());

            if (discountCoupon != null) {
                // 更新店铺券为已使用
                discountCouponManager.UpdateCouponUsed(orderInfo.getShopCouponCode(), orderId, CouponType.Dp.getCode());

                // 记录店铺券金额
                orderInfo.setShopCoupon(discountCoupon.getPrice());
                // 订单金额=订单金额-店铺券金额
                BigDecimal totalPrice = orderInfo.getTotalPrice().subtract(new BigDecimal(orderInfo.getShopCoupon()));
                orderInfo.setTotalPrice(totalPrice);
            }
        }

        // 订单没有设置交易方式的，先更新订单交易方式
        if (orderInfo.getTradeType() == null) {
            ConfigPowerEO configPowerInfo = configPowerDBDAO.getByGameName(orderInfo.getGameName(), orderInfo.getGoodsTypeName());
            if (configPowerInfo != null) {
                if (configPowerInfo.getTradeType() == TradeType.Divided.getCode()) {// 邮寄交易
                    orderInfo.setTradeType(TradeType.Divided.getCode());
                } else if (configPowerInfo.getTradeType() == TradeType.UnionTrading.getCode()) {// 公会交易
                    orderInfo.setTradeType(TradeType.UnionTrading.getCode());
                } else { // 当面交易
                    orderInfo.setTradeType(TradeType.NoDivid.getCode());
                }
            } else {
                orderInfo.setTradeType(TradeType.NoDivid.getCode()); // 默认当面交易
            }
        }


        // 保存订单
        orderInfoDBDAO.insert(orderInfo);

        //修改订单状态为待付款
        OrderInfoEO orderInfoEO = orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.WaitPayment);
        orderInfoRedisDAO.saveOrder(orderInfo);

        // 增加订单新增日志
        StringBuffer sb = new StringBuffer();
        sb.append("创建订单，订单号：").append(orderInfo.getOrderId()).append("，单价：")
                .append(orderInfo.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP)).append("元，总价：")
                .append(orderInfo.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)).append("元");
        //logManager.add(ModuleType.ORDER, sb.toString(),CurrentUserContext.getUser());
        log.info(sb.toString());
        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_CREATE);
        logInfo.setOrderId(orderInfo.getOrderId());
        logInfo.setRemark(sb.toString());
        orderLogManager.add(logInfo);

        return orderInfo;
    }

    @Override
    @Transactional
    public OrderInfoEO modifyOrderInfo(OrderInfoEO orderInfo)
            throws SystemException {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        if (StringUtils.isEmpty(orderInfo.getOrderId())) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }

        //修改订单状态
        OrderInfoEO result = orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.getTypeByCode(orderInfo.getOrderState()));
        if (result != null) {
            // 更新订单信息
            orderInfoDBDAO.update(orderInfo);
        } else {
            throw new SystemException(ResponseCodes.ChangeOrderInfoError.getCode(),
                    ResponseCodes.ChangeOrderInfoError.getMessage());
        }
        return orderInfo;
    }

    @Override
    @Transactional
    public void rebackOrder(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }

        //修改订单状态为已付款
        OrderInfoEO result = orderStateManager.changeOrderState(orderId, OrderState.Paid);
        if (result != null) {
            // 增加订单修改日志
            //StringBuffer sb = new StringBuffer();
            //sb.append("库存不够，").append("将订单号为").append(orderId).append("的订单状态退回到已付款状态");
            //logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
            //log.info(sb.toString());
            OrderLogInfo logInfo = new OrderLogInfo();
            logInfo.setOrderId(orderId);
            logInfo.setLogType(LogType.ORDER_OTHER);
            logInfo.setRemark("库存不够，将订单：" + orderId + "状态退回到已付款状态");
            orderLogManager.add(logInfo);
        } else {
            throw new SystemException(ResponseCodes.ChangeOrderInfoError.getCode(), ResponseCodes.ChangeOrderInfoError.getMessage());
        }

    }

    @Override
    public GenericPage<OrderInfoEO> queryOrderInfo(
            Map<String, Object> queryMap, String orderBy, boolean isAsc,
            int pageSize, int start) throws SystemException {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "CREATE_TIME";
        }
        return orderInfoDBDAO.selectByMap(queryMap, pageSize, start, orderBy, isAsc);
    }

    /**
     * 根据条件查询订单
     *
     * @param queryMap
     * @param orderBy
     * @param isAsc
     * @return
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    public List<OrderInfoEO> queryOrderInfo(Map<String, Object> queryMap, String orderBy, boolean isAsc) throws SystemException {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "CREATE_TIME";
        }
        return orderInfoDBDAO.selectByMap(queryMap, orderBy, isAsc);
    }

    @Override
    public GenericPage<ConfigResultInfoEO> querySellerOrder(
            Map<String, Object> queryParam, String orderBy, boolean isAsc,
            int pageSize, int start) throws SystemException {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "CREATE_TIME";
        }
        return configResultInfoDBDAO.selectSellerOrder(queryParam, orderBy, isAsc, pageSize, start);
    }

    /**
     * 根据条件分页查询卖家订单(优化)
     *
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     * @throws SystemException
     */
    @Override
    public GenericPage<ConfigResultInfoEO> querySellerOrderForOptimization(
            Map<String, Object> queryParam, String orderBy, boolean isAsc,
            int pageSize, int start) throws SystemException {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "CREATE_TIME";
        }
        return configResultInfoDBDAO.selectSellerOrderForOptimization(queryParam, orderBy, isAsc, pageSize, start);
    }

    @Override
    public OrderInfoEO selectById(String orderId) throws SystemException {
        return orderInfoDBDAO.selectUniqueByProp("orderId", orderId);
    }

    @Override
    public List<OrderInfoEO> queryOrder(String uid) throws SystemException {
        List<OrderInfoEO> order = orderInfoRedisDAO.queryOrder(uid);
        return order;
    }

    @Override
    @Transactional
    public void configOrder(OrderInfoEO orderInfo,
                            List<RepositoryInfo> repositorys, List<ConfigResultInfoEO> configInfoList) {
        logger.info("配单开始：orderInfo:" + orderInfo + ",repositorys:" + repositorys + ",configInfoList:" + configInfoList);
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        if (orderInfo.getOrderId() == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }
        if (orderInfo.getTradeType() == null) {
            throw new SystemException(ResponseCodes.EmptyTradeType.getCode(),
                    ResponseCodes.EmptyTradeType.getMessage());
        }
        if (repositorys == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryList.getCode(),
                    ResponseCodes.EmptyRepositoryList.getMessage());
        }


        try {
            // 从数据库查询一次数据，并锁定待修改
            OrderInfoEO order = orderInfoDBDAO.queryOrderForUpdateNowait(orderInfo.getOrderId());
            order.setTradeType(orderInfo.getTradeType());
            orderInfo = order;
        } catch (Exception e) {
            throw new SystemException(ResponseCodes.OrderIsEditor.getCode(), ResponseCodes.OrderIsEditor.getMessage());
        }

        if (orderInfo.getOrderState() != OrderState.Paid.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        //锁定子订单
        Boolean locked = orderConfigLockRedisDAO.lock(String.valueOf(orderInfo.getOrderId()));
        if (!locked) {
            throw new SystemException(ResponseCodes.OrderIsEditor.getCode(), ResponseCodes.OrderIsEditor.getMessage());
        }
        try {
            // 店铺订单且使用了店铺券的，不能配其他卖家的库存
            if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
                    && orderInfo.getShopCoupon() != null
                    && orderInfo.getShopCoupon() != 0) {
                // 检查配置的库存是否属于其他卖家
                for (RepositoryInfo repositoryInfo : repositorys) {
                    if (!repositoryInfo.getLoginAccount().equals(orderInfo.getSellerLoginAccount())) {
                        throw new SystemException(ResponseCodes.UseShopCouponCantUseOtherSellerRepository.getCode(),
                                ResponseCodes.UseShopCouponCantUseOtherSellerRepository.getMessage());
                    }
                }
            }

            //配置的库存游戏币数量总和
            Map<Long, ConfigResultInfoEO> configMap = new HashMap<Long, ConfigResultInfoEO>();
            Long countGold = 0L;
            for (ConfigResultInfoEO config : configInfoList) {
                countGold += config.getConfigGoldCount();
                configMap.put(config.getRepositoryId(), config);
                for (RepositoryInfo repInfo : repositorys) {
                    BigDecimal totalPrice = repInfo.getUnitPrice().multiply(BigDecimal.valueOf(config.getConfigGoldCount())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new SystemException(ResponseCodes.notEnoughSubOrderPrice.getCode(), MessageFormat.format(ResponseCodes.notEnoughSubOrderPrice.getMessage(), 0));
                    }
                    if (repInfo.getId().longValue() == config.getRepositoryId().longValue() && repInfo.getGoldCount() < config.getConfigGoldCount()) {
                        throw new SystemException(ResponseCodes.RepositoryGoldLessConfigGold.getCode(),
                                ResponseCodes.RepositoryGoldLessConfigGold.getMessage());
                    }
                    if (!repInfo.getGameName().equals(orderInfo.getGameName()) || !repInfo.getGoodsTypeName().equals(orderInfo.getGoodsTypeName())) {
                        throw new SystemException(ResponseCodes.ConfigInfoNoLegal.getCode(),
                                ResponseCodes.ConfigInfoNoLegal.getMessage());
                    }
                }
            }
            if (countGold.longValue() != orderInfo.getGoldCount().longValue()) {
                throw new SystemException(
                        ResponseCodes.ConfigGoldNotEqOrderGold.getCode(),
                        ResponseCodes.ConfigGoldNotEqOrderGold.getMessage());
            }

            // 查询出之前对订单配置的库存信息，并修改之前的库存信息
            List<ConfigResultInfoEO> oldConfigs = configResultInfoDBDAO.selectByOrderId(orderInfo.getOrderId());
            if (!CollectionUtils.isEmpty(oldConfigs)) {
                for (ConfigResultInfoEO config : oldConfigs) {
                    RepositoryInfo repositoryInfo = config.getRepositoryInfo();
                    if (repositoryInfo != null) {
                        repositoryManager.incrRepositoryCount(repositoryInfo, config.getConfigGoldCount().longValue(), orderInfo.getOrderId());
                    }
                }

                // 删除之前对订单配置过的库存信息
                configResultInfoDBDAO.deleteByOrderId(orderInfo.getOrderId());
            }

            BigDecimal commissionBase = commissionManager.getCommission(orderInfo.getGameName(), orderInfo.getSellerLoginAccount(),
                    orderInfo.getGoodsCat(), orderInfo.getGoodsTypeName());

            BigDecimal commissionBase2 = commissionManager.getCommission(orderInfo.getGameName(), null, GoodsCat.Cat1.getCode(), orderInfo.getGoodsTypeName());

            // 红包平均值，将红包金额平分到多个子订单
            double averageRedEnvelopeVal = 0;
            // 最后一笔子订单的红包金额，等于总的红包金额-前面几笔子订单平分的红包之和
            double lastRedEnvelopeVal = 0;
            // 判断是否有使用红包
            if (orderInfo.getRedEnvelope() != null && orderInfo.getRedEnvelope() != 0) {
                BigDecimal amount = new BigDecimal(orderInfo.getRedEnvelope().toString());
                BigDecimal count = new BigDecimal(repositorys.size());
                averageRedEnvelopeVal = amount.divide(count, 2, RoundingMode.HALF_UP).doubleValue();
                lastRedEnvelopeVal = orderInfo.getRedEnvelope() - averageRedEnvelopeVal * (repositorys.size() - 1);
            }

            // 保存订单配置结果对象
            for (int index = 0, len = repositorys.size(); index < len; index++) {
                RepositoryInfo repositoryInfo = repositorys.get(index);
                if (repositoryInfo == null) {
                    throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
                            ResponseCodes.EmptyRepository.getMessage());
                }
                if (repositoryInfo.getId() == null) {
                    throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(),
                            ResponseCodes.EmptyRepositoryId.getMessage());
                }
                ConfigResultInfoEO configResult = configMap.get(repositoryInfo.getId());
                configResult.setLoginAccount(repositoryInfo.getLoginAccount());
                configResult.setAccountUid(repositoryInfo.getAccountUid());
                configResult.setCreateTime(new Date());
                configResult.setIsDeleted(false);
                configResult.setLastUpdateTime(new Date());
                configResult.setOrderId(orderInfo.getOrderId());
                configResult.setOrderInfoEO(orderInfo);
                configResult.setRepositoryId(repositoryInfo.getId());
                configResult.setServicerId(orderInfo.getServicerId());
                configResult.setTradeType(orderInfo.getTradeType());
                configResult.setOrderUnitPrice(orderInfo.getUnitPrice());
                configResult.setRepositoryUnitPrice(repositoryInfo.getUnitPrice());
                configResult.setOptionId(CurrentUserContext.getUid());
                configResult.setCreatorId(CurrentUserContext.getUid());
                configResult.setGoodsTypeId(orderInfo.getGoodsTypeId()); //ZW_C_JB_00008_20170523 ADD
                configResult.setGoodsTypeName(orderInfo.getGoodsTypeName());//ZW_C_JB_00008_20170523 ADD

                configResult.setGameName(repositoryInfo.getGameName());
                configResult.setRegion(repositoryInfo.getRegion());
                configResult.setServer(repositoryInfo.getServer());
                configResult.setGameRace(repositoryInfo.getGameRace());
                // 总金额 = 库存的单价*购买数量
                BigDecimal goldCount = new BigDecimal(configResult.getConfigGoldCount());
                BigDecimal totalPrice = repositoryInfo.getUnitPrice().multiply(goldCount);
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);

                // 卖家佣金
                BigDecimal commission = null;
                if (orderInfo.getGoodsCat().intValue() == GoodsCat.Cat3.getCode()) {
                    if (StringUtils.isNotBlank(orderInfo.getSellerLoginAccount())
                            && orderInfo.getSellerLoginAccount().equals(repositoryInfo.getLoginAccount())) {
                        // 店铺订单，配置的是当前订单所选的店铺卖家的库存，则按该店铺卖家配置收取佣金
                        commission = totalPrice.multiply(commissionBase);
                    } else {
                        // 店铺订单，配置的是其他卖家的库存则按游戏配置收取佣金
                        commission = totalPrice.multiply(commissionBase2);
                    }
                } else {
                    commission = totalPrice.multiply(commissionBase2);
                }
                commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
                // 卖家收益 = 配单总价-佣金
                BigDecimal income = totalPrice.subtract(commission);

                // 判断是否有使用红包
                if (orderInfo.getRedEnvelope() != null && orderInfo.getRedEnvelope() != 0) {
                    if (index != (len - 1)) {
                        // 设置前几笔子订单的红包平均值
                        configResult.setRedEnvelope(averageRedEnvelopeVal);
                    } else {
                        // 设置最后一笔子订单的红包值
                        configResult.setRedEnvelope(lastRedEnvelopeVal);
                    }
                }

                // 店铺商品，判断是否有使用店铺券,店铺券只记录到卖家第一笔配单记录
                if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
                        && orderInfo.getShopCoupon() != null
                        && orderInfo.getShopCoupon() != 0
                        && index == 0) {
                    configResult.setShopCoupon(orderInfo.getShopCoupon());

                    // 卖家收益= 卖家收益-店铺券金额
                    income = income.subtract(new BigDecimal(orderInfo.getShopCoupon()));
                }

                // 订单价格
                BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
                // 库存价格
                BigDecimal repositoryAmount = configResult.getRepositoryUnitPrice().multiply(goldCount);
                // 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
                BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

                configResult.setTotalPrice(totalPrice);
                configResult.setIncome(income);
                configResult.setCommission(commission);
                configResult.setBalance(balance);

                logger.info("配单开始，安排交易员开始：{}", orderInfo);
                // 安排交易员
                arrangeTraderManager.arrangeTrader(configResult, repositoryInfo.getLoginAccount(), orderInfo.getServicerId(),
                        orderInfo.getGameName(), orderInfo.getRegion());

                logger.info("配单开始，安排交易员结束：{}", orderInfo);
                // 新增配置的库存信息
                configResultInfoDBDAO.insert(configResult);

                // 更新库存量
                //原来的库存减去配置的库存
                logger.info("配单开始，更新库存量开始：{}", configResult);
                repositoryManager.decrRepositoryCount(repositoryInfo, configResult.getConfigGoldCount().longValue(), orderInfo.getOrderId());
                logger.info("配单开始，更新库存量结束：{}", configResult);
            }
            //修改订单状态为待发货
            OrderInfoEO result = orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.WaitDelivery);
            logger.info("配单结束，修改订单状态为待发货：{}", result);
            if (result != null) {
                // 更新订单信息
                orderInfoDBDAO.update(orderInfo);
                // 增加订单修改日志
                StringBuffer sb = new StringBuffer("订单号：").append(orderInfo.getOrderId()).append("，配置库存成功");
                log.info(sb.toString());
                OrderLogInfo logInfo = new OrderLogInfo();
                logInfo.setOrderId(orderInfo.getOrderId());
                logInfo.setLogType(LogType.ORDER_DISTRIBUTION);
                logInfo.setRemark(sb.toString());
                orderLogManager.add(logInfo);
            } else {
                throw new SystemException(ResponseCodes.ChangeOrderInfoError.getCode(), ResponseCodes.ChangeOrderInfoError.getMessage());
            }
        } finally {
            //解锁子订单
            orderConfigLockRedisDAO.unlock(String.valueOf(orderInfo.getId()));
        }

    }

    @Override
    @Transactional
    public OrderInfoEO changeOrderState(String orderId, Integer orderState,
                                        Boolean isDelay) {
        //设置订单是否超时
        if (isDelay != null && isDelay == true) {
            this.isDelay(orderId);
        }
        //修改订单状态
        return orderStateManager.changeOrderState(orderId, OrderState.getTypeByCode(orderState));
    }

    @Override
    @Transactional
    public void notStoreOrder(String orderId, Boolean isHaveStore) {
        if (orderId == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }
        if (isHaveStore == null) {
            throw new SystemException(
                    ResponseCodes.EmptyOrderIsHaveStore.getCode(),
                    ResponseCodes.EmptyOrderIsHaveStore.getMessage());
        }
        // 更新订单的是否有货状态
        OrderInfoEO order = this.selectById(orderId.toString());
        order.setIsHaveStore(isHaveStore);
        this.modifyOrderInfo(order);

        // 增加订单修改日志
        /*StringBuffer sb = new StringBuffer("修改订单的是否有货状态为无货。");
        logManager.add(ModuleType.ORDER, sb.toString(),
				CurrentUserContext.getUser());
		log.info(sb.toString());*/
        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setOrderId(orderId);
        logInfo.setLogType(LogType.ORDER_OTHER);
        logInfo.setRemark("订单号：" + orderId + "，修改订单为无货");
        orderLogManager.add(logInfo);
    }

    @Transactional
    @Override
    public void isDelay(String orderId) {
        OrderInfoEO orderInfoEO = new OrderInfoEO();
        orderInfoEO.setOrderId(orderId);
        orderInfoEO.setIsDelay(true);

        orderInfoDBDAO.update(orderInfoEO);

		/*StringBuffer sb = new StringBuffer("订单号为").append(orderId).append("超时发货，修改是否超时为已超时");
        logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
		log.info(sb.toString());*/

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setOrderId(orderId);
        logInfo.setLogType(LogType.ORDER_OTHER);
        logInfo.setRemark("订单号：" + orderId + "，修改订单为超时发货");
        orderLogManager.add(logInfo);
    }

    /**
     * 分页查询配单记录
     *
     * @param queryMap
     * @param orderBy
     * @param isAsc
     * @param limit
     * @param start
     * @return
     */
    @Override
    public GenericPage<ConfigResultInfoEO> queryConfigResultInfoList(Map<String, Object> queryMap, String orderBy,
                                                                     boolean isAsc, int limit, int start) {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "ID";
        }
        return configResultInfoDBDAO.selectByMap(queryMap, limit, start, orderBy, isAsc);
    }

    @Override
    public GenericPage<ShippingInfoEO> queryShippingList(
            Map<String, Object> queryMap, String orderBy, boolean isAsc,
            int limit, int start) {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "ID";
        }
        queryMap.put("tradePalceIsDel", false);
        queryMap.put("state", OrderState.Cancelled.getCode());
        queryMap.put("configResultIsDel", false);
        return shippingInfoDBDAO.selectByMap(queryMap, limit, start, orderBy,
                isAsc);
    }

    @Override
    public List<ShippingInfoEO> queryShippingList(Map<String, Object> queryMap,
                                                  String orderBy, boolean isAsc) {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "ID";
        }
        queryMap.put("tradePalceIsDel", false);
        queryMap.put("state", OrderState.Cancelled.getCode());
        queryMap.put("configResultIsDel", false);
        return shippingInfoDBDAO.selectByMap(queryMap, orderBy, isAsc);
    }

    @Override
    public List<OrderInfoEO> queryOrderForAutoPlay() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.Delivery.getCode());
        queryMap.put("tradePlaceIsDel", false);
        List<OrderInfoEO> orderInfos = orderInfoDBDAO
                .queryOrderForAutoPlay(queryMap);
        return orderInfos;
    }

    @Override
    @Transactional
    public void replaceConfigOrder(OrderInfoEO orderInfo,
                                   RepositoryInfo repository, ConfigResultInfoEO configResult) {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        if (orderInfo.getOrderId() == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }
        if (repository == null) {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
                    ResponseCodes.EmptyRepository.getMessage());
        }
        if (configResult == null) {
            throw new SystemException(ResponseCodes.EmptyConfigResult.getCode(),
                    ResponseCodes.EmptyConfigResult.getMessage());
        }
        if (configResult.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyConfigResultId.getCode(),
                    ResponseCodes.EmptyConfigResultId.getMessage());
        }



        orderInfo = selectById(orderInfo.getOrderId());

        //锁定子订单
        Boolean locked = orderConfigLockRedisDAO.lock(String.valueOf(configResult.getId()));
        if (!locked) {
            throw new SystemException(ResponseCodes.ConfigResultIsEditor.getCode(),
                    ResponseCodes.ConfigResultIsEditor.getMessage());
        }
        try {
            if (repository.getGoldCount() < configResult.getConfigGoldCount()) {
                throw new SystemException(ResponseCodes.RepositoryGoldLessConfigGold.getCode(),
                        ResponseCodes.RepositoryGoldLessConfigGold.getMessage());
            }
            ConfigResultInfoEO dbConfigResultInfo = configResultInfoDBDAO.selectByIdForUpdate(configResult.getId());
            if (dbConfigResultInfo.getIsDeleted()) {
                throw new SystemException(ResponseCodes.ReplaceConfigOrderIsDeleted.getCode(),
                        ResponseCodes.ReplaceConfigOrderIsDeleted.getMessage());
            }

            if (dbConfigResultInfo.getOrderInfoEO() != null
                    && dbConfigResultInfo.getOrderInfoEO().getOrderState() != OrderState.WaitDelivery.getCode()) {
                throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
            }

            //判断子订单是否已经取消，只有没有取消的订单才把配置的库存加回到之前的库存
            if (dbConfigResultInfo.getState() != OrderState.Cancelled.getCode()) {
                RepositoryInfo repositoryInfo = configResult.getRepositoryInfo();
                if (repositoryInfo != null) {
                    repositoryManager.incrRepositoryCount(repositoryInfo, configResult.getConfigGoldCount().longValue(), orderInfo.getOrderId());
                }
            }
            // 删除之前对订单配置过的库存信息
            configResultInfoDBDAO.deleteConfigById(configResult.getId());

            /*// 如果原先是寄售订单
            if (dbConfigResultInfo.getIsConsignment() != null && dbConfigResultInfo.getIsConsignment() == true) {
               *//* // 寄售客服处理订单数减1
                OrderInfoEO order = configResult.getOrderInfoEO();
                servicerOrderManager.subOrderNum(order.getGameName(),
                        order.getRegion(), order.getServer(),
                        order.getGameRace(), dbConfigResultInfo.getOptionId());*//*

				// 客服待发货订单数量-1
				serviceSortManager.decOrderCount(dbConfigResultInfo.getOptionId());
            }*/


            // 保存配置结果对象
            ConfigResultInfoEO newConfigResult = new ConfigResultInfoEO();
            newConfigResult.setConfigGoldCount(configResult.getConfigGoldCount());
            newConfigResult.setAccountUid(repository.getAccountUid());
            newConfigResult.setCreateTime(new Date());
            newConfigResult.setIsDeleted(false);
            newConfigResult.setLastUpdateTime(new Date());
            newConfigResult.setLoginAccount(repository.getLoginAccount());
            newConfigResult.setOrderId(orderInfo.getOrderId());
            newConfigResult.setOrderInfoEO(orderInfo);
            newConfigResult.setRepositoryId(repository.getId());
            newConfigResult.setServicerId(repository.getServicerId());
            newConfigResult.setTradeType(orderInfo.getTradeType());
            newConfigResult.setOrderUnitPrice(orderInfo.getUnitPrice());
            newConfigResult.setRepositoryUnitPrice(repository.getUnitPrice());
            newConfigResult.setState(OrderState.WaitDelivery.getCode());
            newConfigResult.setOptionId(CurrentUserContext.getUid());
            newConfigResult.setCreatorId(CurrentUserContext.getUid());
            newConfigResult.setGoodsTypeId(orderInfo.getGoodsTypeId()); //ZW_C_JB_00008_20170523 ADD
            newConfigResult.setGoodsTypeName(orderInfo.getGoodsTypeName()); //ZW_C_JB_00008_20170523 ADD

            newConfigResult.setGameName(repository.getGameName());
            newConfigResult.setRegion(repository.getRegion());
            newConfigResult.setServer(repository.getServer());
            newConfigResult.setGameRace(repository.getGameRace());
            BigDecimal commissionBase = commissionManager.getCommission(orderInfo.getGameName(), orderInfo.getSellerLoginAccount(),
                    orderInfo.getGoodsCat(), orderInfo.getGoodsTypeName());


            // 总金额 = 库存的单价*购买数量
            BigDecimal goldCount = new BigDecimal(configResult.getConfigGoldCount());
            BigDecimal totalPrice = repository.getUnitPrice().multiply(goldCount);
            totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);

            // 卖家佣金
            BigDecimal commission = totalPrice.multiply(commissionBase);
            commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
            // 卖家收益 = 配单总价-佣金
            BigDecimal income = totalPrice.subtract(commission);


            // 判断是否有使用红包
            if (dbConfigResultInfo.getRedEnvelope() != null) {
                newConfigResult.setRedEnvelope(dbConfigResultInfo.getRedEnvelope());
            }

            // 判断是否有使用店铺券
            if (dbConfigResultInfo.getShopCoupon() != null) {
                newConfigResult.setShopCoupon(dbConfigResultInfo.getShopCoupon());

                // 卖家收益= 卖家收益-店铺券金额
                income = income.subtract(new BigDecimal(dbConfigResultInfo.getShopCoupon()));
            }

            // 订单价格
            BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
            // 库存价格
            BigDecimal repositoryAmount = repository.getUnitPrice().multiply(goldCount);
            // 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
            BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

            newConfigResult.setTotalPrice(totalPrice);
            newConfigResult.setIncome(income);
            newConfigResult.setCommission(commission);
            newConfigResult.setBalance(balance);

            // 判断该子订单之前是否是从寄售全自动机器人退回的
            if (dbConfigResultInfo.getIsSendBackFromRobot() != null && dbConfigResultInfo.getIsSendBackFromRobot()) {
                newConfigResult.setIsSendBackFromRobot(true);
            }

            // 安排交易员
            arrangeTraderManager.arrangeTrader(newConfigResult, repository.getLoginAccount(), orderInfo.getServicerInfo().getId(),
                    orderInfo.getGameName(), orderInfo.getRegion());

            // 新增配置的库存信息
            configResultInfoDBDAO.insert(newConfigResult);

            // 更新库存量
            /*repository.setGoldCount(repository.getGoldCount()-configResult.getConfigGoldCount());
            repository.setSellableCount(repository.getSellableCount()-configResult.getConfigGoldCount());
			repositoryManager.modifyRepository(repository);*/
            repositoryManager.decrRepositoryCount(repository, configResult.getConfigGoldCount().longValue(), orderInfo.getOrderId());

            //修改配置信息状态为待发货
            orderStateManager.changeConfigState(newConfigResult.getId(), OrderState.WaitDelivery);

            // 增加订单修改日志
            StringBuffer sb = new StringBuffer();
            sb.append("订单号：").append(orderInfo.getOrderId()).append("，重配成功！卖家从")
                    .append(configResult.getLoginAccount()).append("变成").append(repository.getLoginAccount());
            /*logManager.add(ModuleType.ORDER, sb.toString(),
                    CurrentUserContext.getUser());
			log.info(sb.toString());*/

            OrderLogInfo logInfo = new OrderLogInfo();
            logInfo.setOrderId(orderInfo.getOrderId());
            logInfo.setLogType(LogType.ORDER_DISTRIBUTION);
            logInfo.setRemark(sb.toString());
            orderLogManager.add(logInfo);

        } finally {
            //解锁子订单
            orderConfigLockRedisDAO.unlock(String.valueOf(configResult.getId()));
        }
    }

    //    /**
//     * 订单重配，可以配置多条配单记录
//     *
//     * @param configResultId 需要重新配置的配单记录
//     * @param repositoryList 配置的库存记录
//     * @param configInfoList 配单记录
//     */
	/*@Transactional
	public void resetConfigOrder(Long configResultId, List<RepositoryInfo> repositoryList,
							List<ConfigResultInfoEO> configInfoList) {
		if (configResultId == null) {
			throw new SystemException(ResponseCodes.EmptyConfigResultId.getCode(),
					ResponseCodes.EmptyConfigResultId.getMessage());
		}
		ConfigResultInfoEO configResultInfo = configResultInfoDBDAO.selectByIdForUpdate(configResultId);
		if(configResultInfo.getIsDeleted()){
			throw new SystemException(ResponseCodes.ReplaceConfigOrderIsDeleted.getCode(),
					ResponseCodes.ReplaceConfigOrderIsDeleted.getMessage());
		}

		OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderForUpdate(configResultInfo.getOrderId());
		if (orderInfo.getOrderState() != OrderState.WaitDelivery.getCode()) {
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
		}

		// 检查配置的库存数量是否等于重配的游戏币数量总和
		Map<Long, ConfigResultInfoEO> configMap = new HashMap<Long, ConfigResultInfoEO>();
		Long countGold = 0L;
		for(ConfigResultInfoEO config : configInfoList){
			countGold +=config.getConfigGoldCount();
			configMap.put(config.getRepositoryId(), config);
			for(RepositoryInfo repInfo : repositoryList){
				if (repInfo.getId() == config.getRepositoryId() && repInfo.getGoldCount() < config.getConfigGoldCount()) {
					throw new SystemException(ResponseCodes.RepositoryGoldLessConfigGold.getCode(),
							ResponseCodes.RepositoryGoldLessConfigGold.getMessage());
				}
			}
		}
		if (countGold.longValue() != configResultInfo.getConfigGoldCount().longValue()) {
			throw new SystemException(
					ResponseCodes.ConfigGoldNotEqOrderGold.getCode(),
					ResponseCodes.ConfigGoldNotEqOrderGold.getMessage());
		}

		// 删除之前对订单配置过的库存信息
		configResultInfoDBDAO.deleteConfigById(configResultId);

		// 获取店铺配置收取的佣金
		BigDecimal commissionBase = commissionManager.getCommission(orderInfo.getGameName(), orderInfo.getSellerLoginAccount(),
				orderInfo.getGoodsCat());
		// 获取游戏配置收取的佣金
		BigDecimal commissionBase2 = commissionManager.getCommission(orderInfo.getGameName(), null, GoodsCat.Cat1.getCode());

		// 红包平均值，将红包金额平分到多个子订单
		double averageRedEnvelopeVal = 0;
		// 最后一笔子订单的红包金额，等于总的红包金额-前面几笔子订单平分的红包之和
		double lastRedEnvelopeVal = 0;
		// 判断是否有使用红包
		if (orderInfo.getRedEnvelope() != null && orderInfo.getRedEnvelope() != 0) {
			BigDecimal amount = new BigDecimal(orderInfo.getRedEnvelope().toString());
			BigDecimal count = new BigDecimal(repositoryList.size());
			averageRedEnvelopeVal = amount.divide(count, 2, RoundingMode.HALF_UP).doubleValue();
			lastRedEnvelopeVal = orderInfo.getRedEnvelope() - averageRedEnvelopeVal * (repositoryList.size() - 1);
		}

		// 保存订单配置结果对象
		for (int index = 0, len = repositoryList.size(); index < len; index++) {
			RepositoryInfo repositoryInfo = repositoryList.get(index);
			if (repositoryInfo == null) {
				throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
						ResponseCodes.EmptyRepository.getMessage());
			}
			if (repositoryInfo.getId() == null) {
				throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(),
						ResponseCodes.EmptyRepositoryId.getMessage());
			}
			ConfigResultInfoEO configResult = configMap.get(repositoryInfo.getId());
			configResult.setLoginAccount(repositoryInfo.getLoginAccount());
			configResult.setAccountUid(repositoryInfo.getAccountUid());
			configResult.setCreateTime(new Date());
			configResult.setIsDeleted(false);
			configResult.setLastUpdateTime(new Date());
			configResult.setOrderId(orderInfo.getOrderId());
			configResult.setOrderInfoEO(orderInfo);
			configResult.setRepositoryId(repositoryInfo.getId());
			configResult.setServicerId(repositoryInfo.getServicerId());
			configResult.setTradeType(orderInfo.getTradeType());
			configResult.setOrderUnitPrice(orderInfo.getUnitPrice());
			configResult.setRepositoryUnitPrice(repositoryInfo.getUnitPrice());
			configResult.setOptionId(CurrentUserContext.getUid());
			configResult.setCreatorId(CurrentUserContext.getUid());

			// 总金额 = 库存的单价*购买数量
			BigDecimal goldCount = new BigDecimal(configResult.getConfigGoldCount());
			BigDecimal totalPrice = repositoryInfo.getUnitPrice().multiply(goldCount);
			totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);

			// 卖家佣金
			BigDecimal commission = null;
			if (orderInfo.getGoodsCat().intValue() == GoodsCat.Cat3.getCode()) {
				if (StringUtils.isNotBlank(orderInfo.getSellerLoginAccount())
						&& orderInfo.getSellerLoginAccount().equals(repositoryInfo.getLoginAccount())) {
					// 店铺订单，配置的是当前订单所选的店铺卖家的库存，则按该店铺卖家配置收取佣金
					commission = totalPrice.multiply(commissionBase);
				} else {
					// 店铺订单，配置的是其他卖家的库存则按游戏配置收取佣金
					commission = totalPrice.multiply(commissionBase2);
				}
			} else {
				commission = totalPrice.multiply(commissionBase2);
			}
			commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
			// 卖家收益 = 配单总价-佣金
			BigDecimal income = totalPrice.subtract(commission);

			// 判断是否有使用红包
			if (orderInfo.getRedEnvelope() != null  && orderInfo.getRedEnvelope() != 0) {
				if (index != (len - 1)) {
					// 设置前几笔子订单的红包平均值
					configResult.setRedEnvelope(averageRedEnvelopeVal);
				} else {
					// 设置最后一笔子订单的红包值
					configResult.setRedEnvelope(lastRedEnvelopeVal);
				}
			}

			// 店铺商品，判断是否有使用店铺券,店铺券只记录到卖家第一笔配单记录
			if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
					&& orderInfo.getShopCoupon() != null
					&& orderInfo.getShopCoupon() != 0
					&& index == 0) {
				configResult.setShopCoupon(orderInfo.getShopCoupon());

				// 卖家收益= 卖家收益-店铺券金额
				income = income.subtract(new BigDecimal(orderInfo.getShopCoupon()));
			}

			// 订单价格
			BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
			// 库存价格
			BigDecimal repositoryAmount = configResult.getRepositoryUnitPrice().multiply(goldCount);
			// 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
			BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

			configResult.setTotalPrice(totalPrice);
			configResult.setIncome(income);
			configResult.setCommission(commission);
			configResult.setBalance(balance);

			// 安排交易员
			arrangeTraderManager.arrangeTrader(configResult, repositoryInfo.getLoginAccount(), orderInfo.getServicerId(),
					orderInfo.getGameName(), orderInfo.getRegion());

			// 新增配置的库存信息
			configResultInfoDBDAO.insert(configResult);

			// 更新库存量
			//原来的库存减去配置的库存
			repositoryManager.decrRepositoryCount(repositoryInfo, configResult.getConfigGoldCount().longValue(), orderInfo.getOrderId());
		}
	}*/
    @Override
    //@Transactional
    public void autoCancellTimeoutOrder(Integer autoDeleteTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.WaitPayment.getCode());
        Date now = new Date();
        Date deleteTime = DateUtils.addSeconds(now, -autoDeleteTime);
        queryMap.put("createEndTime", deleteTime);
        queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<OrderInfoEO> deleteList = orderInfoDBDAO.selectByMap(queryMap, "ID", false);
        if (deleteList == null || deleteList.size() == 0) {
            log.info("autoCancellTimeoutOrder没有待支付订单");
            return;
        }
        log.info("自动确认未支付的订单定时器开始" + deleteList.size());
        List<String> orderIdList = new ArrayList<String>();
        for (OrderInfoEO orderInfo : deleteList) {
            try {
                log.info("自动确认未支付的订单定时器开始,orderInfo:" + orderInfo);
                Boolean result = autoPayManager.queryPaymentDetail(orderInfo);
                log.info("自动确认未支付的订单定时器开始,result:" + result);
                if (result == null) {
                    //未返回标准结果，不做操作
                    continue;
                } else if (result) {
                    //已支付，修改订单状态
                    orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.Paid);
                } else if (!result) {
                    //未支付，修改订单状态
                    orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.Cancelled);
                    orderIdList.add(orderInfo.getOrderId());
                }
            } catch (Exception ex) {
                log.error("自动确认未支付的订单定时器开始,单号:" + orderInfo.getOrderId() + ",出错:" + ex);
            }
        }
        // 增加删除指定时间之前的待付款的订单日志
        if (orderIdList.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuffer sb = new StringBuffer();
            sb.append("删除当前系统时间").append(sdf.format(now)).append("之前").append(autoDeleteTime)
                    .append("秒的待付款的订单。").append("订单号：").append(orderIdList.toString());
			/*logManager.add(ModuleType.ORDER, sb.toString(),
					CurrentUserContext.getUser());
			log.info(sb.toString());*/

            OrderLogInfo logInfo = new OrderLogInfo();
            logInfo.setLogType(LogType.ORDER_OTHER);
            logInfo.setRemark(sb.toString());
            orderLogManager.add(logInfo);
        }
    }

    @Override
    //@Transactional
    public void autoConfirmationPayTimeoutOrder(Integer autoDeleteTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.WaitPayment.getCode());
        Date now = new Date();
        Date deleteTime = DateUtils.addSeconds(now, -autoDeleteTime);
        queryMap.put("createEndTime", deleteTime);
        queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<OrderInfoEO> deleteList = orderInfoDBDAO.selectByMap(queryMap, "ID", false);
        if (deleteList == null || deleteList.size() == 0) {
            log.info("autoConfirmationPayTimeoutOrder没有待支付订单");
            return;
        }
        for (OrderInfoEO orderInfo : deleteList) {
            try {
                Boolean result = autoPayManager.queryPaymentDetail(orderInfo);
                if (result == null) {
                    //未返回标准结果，不做操作
                    continue;
                } else if (result) {
                    //已支付，修改订单状态
                    orderStateManager.changeOrderState(orderInfo.getOrderId(), OrderState.Paid);
                }
            } catch (Exception ex) {
                log.error("自动确认未支付的订单定时器开始autoConfirmationPayTimeoutOrder,单号:" + orderInfo.getOrderId() + ",出错:" + ex);
            }
        }
    }

    /**
     * 同步订单付款状态
     *
     * @param orderId
     * @return true 表示同步成功
     */
    @Override
    public boolean syncOrderPaymentStatus(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }

        OrderInfoEO orderInfo = this.selectById(orderId);
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
        }

        boolean success = true;

        // 查询订单是否已经支付
        Boolean result = autoPayManager.queryPaymentDetail(orderInfo);
        if (result == null) {
            //未返回标准结果，不做操作
            success = false;
        } else if (result) {
            //已支付，修改订单状态
            orderStateManager.changeOrderState(orderId, OrderState.Paid);
        }
        return success;
    }

    /**
     * 保存订单取消的原因
     */
    @Override
    public void saveOrederCancelReason(String orderId, String cancelremark) {
        OrderInfoEO orderInfoEO = new OrderInfoEO();
        orderInfoEO.setOrderId(orderId);
        orderInfoEO.setCancelReson(cancelremark);
        orderInfoDBDAO.updateOrderCancelReson(orderInfoEO);
    }

    /**
     * 保存订单退款的原因
     */
    @Override
    public void saveOrderRefundReason(String orderId, Integer refundReason, String remark) {
        OrderInfoEO orderInfoEO = new OrderInfoEO();
        orderInfoEO.setOrderId(orderId);
        orderInfoEO.setRefundReason(refundReason);
        orderInfoEO.setRemark(remark);
        orderInfoDBDAO.update(orderInfoEO);
    }

    /**
     * 根据条件查询导出卖家订单
     *
     * @param orderBy
     * @param isAsc
     * @return
     * @throws SystemException
     */
    @Override
    public List<ConfigResultInfoEO> queryExportSellerOrder(
            Map<String, Object> queryParam, String orderBy, boolean isAsc)
            throws SystemException {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "CREATE_TIME";
        }
        return configResultInfoDBDAO.exprotSellerOrder(queryParam, orderBy, isAsc);
    }

    /**
     * 根据条件统计
     *
     * @param queryMap
     * @return
     */
    @Override
    public int countByMap(Map<String, Object> queryMap) {
        return orderInfoDBDAO.countByMap(queryMap);
    }

    /**
     * 订单移交，只能移交担保的订单/小助手的订单
     *
     * @param id 格式：订单号_配单号,例如：YX1503130000773_2014020，如果只有一笔配单，可以只传订单号(例如：YX1503130000773)
     */
    @Override
    @Transactional
    public void transferOrder(String id) {
        if (StringUtils.isBlank(id)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }
        id = StringUtils.trim(id);

        String orderId = "";
        long configResultId = -1;
        List<ConfigResultInfoEO> configList;

        if (id.indexOf("_") != -1) {
            String[] orderIdArr = id.split("_");
            orderId = orderIdArr[0]; // 订单ID
            configResultId = Long.valueOf(orderIdArr[1]); // 配单ID
        } else {
            orderId = id;
            configList = orderConfigManager.orderConfigList(orderId);
            if (CollectionUtils.isEmpty(configList)) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }

            // 该订单有多个子订单，不能直接移交，请提供子订单号
            if (configList.size() > 1) {
                throw new SystemException(ResponseCodes.ExistMultiConfigResult.getCode(), ResponseCodes.ExistMultiConfigResult.getMessage());
            }

            configResultId = configList.get(0).getId();
        }


        ConfigResultInfoEO configResult = configResultInfoDBDAO.selectByIdForUpdate(configResultId);
        if (configResult == null) {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }

        if (configResult.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (configResult.getIsConsignment()) {
            throw new SystemException(ResponseCodes.TransferOrderFailed.getCode(), ResponseCodes.TransferOrderFailed.getMessage());
        }

        //更新配置中的订单状态
        orderConfigManager.updateConfigState(configResultId, OrderState.Delivery.getCode());

        boolean isDelivery = true;
        configList = orderConfigManager.orderConfigList(orderId);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (!configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = false;
                break;
            }
        }
        if (isDelivery) {
            changeOrderState(orderId, OrderState.Delivery.getCode(), null);
        }

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_DELIVERY);
        logInfo.setOrderId(orderId);
        logInfo.setRemark("移交订单：" + id);
        orderLogManager.add(logInfo);
    }

    /**
     * 订单移交，供RC2使用
     *
     * @param id 格式：订单号_配单号,例如：YX1503130000773_2014020
     */
    @Override
    @Transactional
    public void transferOrderForRC2(String id, String operatorId) {
        if (StringUtils.isBlank(id)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }
        id = StringUtils.trim(id);

        String orderId = "";
        long configResultId = -1;
        List<ConfigResultInfoEO> configList;

        if (id.indexOf("_") != -1) {
            String[] orderIdArr = id.split("_");
            orderId = orderIdArr[0]; // 订单ID
            configResultId = Long.valueOf(orderIdArr[1]); // 配单ID
        } else {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }

        ConfigResultInfoEO configResult = configResultInfoDBDAO.selectByIdForUpdate(configResultId);
        if (configResult == null) {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }

        if (configResult.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (!configResult.getIsConsignment()) {
            throw new SystemException(ResponseCodes.TransferOrderFailed2.getCode(), ResponseCodes.TransferOrderFailed2.getMessage());
        }

        if (!(configResult.getOptionUser().getLoginAccount()).equals(operatorId)) {
            throw new SystemException(ResponseCodes.ErrorOperatorIdWithOrder.getCode(), ResponseCodes.ErrorOperatorIdWithOrder.getMessage());
        }

        //更新配置中的订单状态
        orderConfigManager.updateConfigState(configResultId, OrderState.Delivery.getCode());

        boolean isDelivery = true;
        configList = orderConfigManager.orderConfigList(orderId, true);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (!configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = false;
                break;
            }
        }
        if (isDelivery) {
            changeOrderState(orderId, OrderState.Delivery.getCode(), null);
        }

        // 如果原先是寄售订单
        if (configResult.getIsConsignment() != null && configResult.getIsConsignment() == true) {
            // 客服待发货订单数量-1
            serviceSortManager.decOrderCount(configResult.getOptionId());
        }

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_DELIVERY);
        logInfo.setOrderId(orderId);
        logInfo.setRemark("RC2接口移交订单：" + id);
        orderLogManager.add(logInfo);
    }

    /**
     * 订单移交，供寄售全自动机器人使用
     *
     * @param orderId
     * @param subOrderId
     */
    @Override
    @Transactional
    public void transferOrderForJsRobot(String orderId, Long subOrderId) {
        if (StringUtils.isBlank(orderId) || subOrderId == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }

        ConfigResultInfoEO configResult = configResultInfoDBDAO.selectByIdForUpdate(subOrderId);
        if (configResult == null) {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }

        if (configResult.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (!configResult.getIsConsignment()) {
            throw new SystemException(ResponseCodes.TransferOrderFailed2.getCode(), ResponseCodes.TransferOrderFailed2.getMessage());
        }
        if (configResult.getIsJsRobot() == null || configResult.getIsJsRobot() == false) {
            throw new SystemException(ResponseCodes.NotJsRobotOrder.getCode(), ResponseCodes.NotJsRobotOrder.getMessage());
        }

        //更新配置中的订单状态
        orderConfigManager.updateConfigState(subOrderId, OrderState.Delivery.getCode());

        boolean isDelivery = true;
        List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId, true);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (!configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = false;
                break;
            }
        }
        if (isDelivery) {
            changeOrderState(orderId, OrderState.Delivery.getCode(), null);
        }

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_DELIVERY);
        logInfo.setOrderId(orderId);
        logInfo.setRemark("寄售全自动接口移交订单：" + orderId + "_" + subOrderId);
        orderLogManager.add(logInfo);
    }

    /**
     * 订单移交，供小助手使用
     *
     * @param orderId    主订单号
     * @param subOrderId 子订单号
     */
    @Override
    @Transactional
    public void transferOrderForHelper(String orderId, Long subOrderId, IUser seller) {
        // 获取卖家信息
        SellerInfo sellerInfo = sellerManager.findByAccountAndUid(seller.getLoginAccount(), seller.getUid());
        /*******************************新增卖家信息判断_20170713_START****************************/
        if (sellerInfo == null) {
            throw new SystemException(ResponseCodes.NotFoundSellerInfo.getCode(), ResponseCodes.NotFoundSellerInfo.getMessage());
        }
        /******************************新增卖家信息判断_20170713_END******************************/
        if (sellerInfo.getIsShielded()) {
            // 寄售的订单不能移交
            throw new SystemException(ResponseCodes.TransferOrderFailed.getCode(), ResponseCodes.TransferOrderFailed.getMessage());
        } else if (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper()) {
            // 未开通小助手的卖家，没有移交订单的权限
            throw new SystemException(ResponseCodes.SellerDidNotOpenHelperPerm.getCode(), ResponseCodes.SellerDidNotOpenHelperPerm.getMessage());
        }

        if (StringUtils.isBlank(orderId) || subOrderId == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }
        ConfigResultInfoEO configResult = configResultInfoDBDAO.selectByIdForUpdate(subOrderId);
        if (configResult == null) {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }
        /***********新增当前用户与订单所属用户安全校验_20170713_START*************/
        if (!seller.getLoginAccount().equals(configResult.getLoginAccount())) {
            throw new SystemException(ResponseCodes.NoOperationAuthority.getCode(), ResponseCodes.NoOperationAuthority.getMessage());
        }
        /**********新增当前用户与订单所属用户安全校验_20170713_END*************/
        if (configResult.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        configResultInfoDBDAO.transfer(orderId, subOrderId, seller.getLoginAccount(), seller.getUid());

        boolean isDelivery = true;
        List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (!configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = false;
                break;
            }
        }
        if (isDelivery) {
            changeOrderState(orderId, OrderState.Delivery.getCode(), null);
        }

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_DELIVERY);
        logInfo.setOrderId(orderId);
        logInfo.setRemark("小助手移交订单：" + orderId + "_" + subOrderId);
        orderLogManager.add(logInfo);
    }

    /**
     * 取消订单
     *
     * @param id
     * @param reconfig
     * @param cancelRemark
     */
    @Transactional
    @Override
    public void cancelOrder(String id, int reconfig, String cancelRemark, String operatorId) {
        if (StringUtils.isBlank(id)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }
        id = StringUtils.trim(id);

        String orderId = "";
        long configResultId = -1;

        if (id.indexOf("_") != -1) {
            String[] orderIdArr = id.split("_");
            orderId = orderIdArr[0]; // 订单ID
            configResultId = Long.valueOf(orderIdArr[1]); // 配单ID
        } else {
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        }

        ConfigResultInfoEO configResult = configResultInfoDBDAO.selectByIdForUpdate(configResultId);
        if (configResult.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (StringUtils.isNotBlank(operatorId) && !operatorId.equals(configResult.getOptionUser().getLoginAccount())) {
            throw new SystemException(ResponseCodes.ErrorOperatorIdWithOrder.getCode(), ResponseCodes.ErrorOperatorIdWithOrder.getMessage());
        }

        //更新配置中的订单状态
        orderConfigManager.updateConfigState(configResultId, OrderState.Cancelled.getCode());

        // 退款操作
        if (reconfig != 1) {
            boolean isDelivery = false;
            List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId);
            for (ConfigResultInfoEO configResultInfoEO : configList) {
                if (configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                    isDelivery = true;
                    break;
                }
            }

            if (!isDelivery) {
                changeOrderState(orderId, OrderState.Refund.getCode(), null);
            }
        }
        if (StringUtils.isNotBlank(cancelRemark)) {
            try {
                cancelRemark = new String(Base64.decode(cancelRemark), "GB2312");
                saveOrederCancelReason(orderId, cancelRemark);
            } catch (UnsupportedEncodingException e) {
                logger.error("解析订单取消原因出错了", e);
            }
        }

        // 如果原先是寄售订单
        configResult = orderConfigManager.orderConfigById(configResultId);
        if (configResult.getIsConsignment() != null && configResult.getIsConsignment() == true) {
            /*// 寄售客服处理订单数减1
            OrderInfoEO order = configResult.getOrderInfoEO();
            servicerOrderManager.subOrderNum(order.getGameName(),
                    order.getRegion(), order.getServer(),
                    order.getGameRace(), configResult.getOptionId());*/

            // 客服待发货订单数量-1
            serviceSortManager.decOrderCount(configResult.getOptionId());
        }

        StringBuffer sb = new StringBuffer("取消配单:");
        sb.append(id).append(",卖家角色名:").append(configResult.getLoginAccount()).append(",数量:");
        sb.append(configResult.getConfigGoldCount()).append(",交易员：");
        if (configResult.getOptionUser() != null) {
            sb.append(configResult.getOptionUser().getLoginAccount()).append("/").append(configResult.getOptionUser().getRealName());
        }

        OrderLogInfo logInfo = new OrderLogInfo();
        logInfo.setLogType(LogType.ORDER_DISTRIBUTION);
        logInfo.setOrderId(orderId);
        logInfo.setRemark(sb.toString());
        orderLogManager.add(logInfo);
    }

    /**
     * 统计保费
     *
     * @param queryMap
     * @return
     */
    @Override
    public BigDecimal statisticPremiums(Map<String, Object> queryMap) {
        return orderInfoDBDAO.statisticPremiums(queryMap);
    }

    /**
     * 退款
     *
     * @param orderId      订单号
     * @param refundReason 退款原因
     * @param remark       备注
     * @return boolean
     */
    @Override
    @Transactional
    public boolean refund(String orderId, int refundReason, String remark) {
        boolean isDelivery = false;
        List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = true;
                break;
            }
        }

        if (!isDelivery) {
            changeOrderState(orderId, OrderState.Refund.getCode(), null);
            OrderInfoEO orderInfoEO = new OrderInfoEO();
            orderInfoEO.setOrderId(orderId);
            orderInfoEO.setRefundReason(refundReason);
            orderInfoEO.setRemark(remark);
            orderInfoDBDAO.update(orderInfoEO);

            OrderLogInfo logInfo = new OrderLogInfo();
            logInfo.setLogType(LogType.ORDER_REFUND);
            logInfo.setOrderId(orderId);
            logInfo.setRemark("取消退款：" + orderId);
            orderLogManager.add(logInfo);

            return true;
        }

        return false;
    }

    /**
     * 结单
     *
     * @param orderId
     * @return boolean
     */
    @Transactional
    @Override
    public boolean statement(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
        }

        boolean isDelivery = true;
        List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId);
        for (ConfigResultInfoEO configResultInfoEO : configList) {
            if (!configResultInfoEO.getState().equals(OrderState.Delivery.getCode())) {
                isDelivery = false;
                break;
            }
        }

        // 子订单都已经发货的进行结单
        if (isDelivery) {
            // 先进行已发货操作
            //changeOrderState(orderId, OrderState.Delivery.getCode(), null);
            // 再进行结单操作
            orderStateManager.changeOrderState(orderId, OrderState.Statement);

            OrderLogInfo logInfo = new OrderLogInfo();
            logInfo.setLogType(LogType.ORDER_STATEMENT);
            logInfo.setOrderId(orderId);
            logInfo.setRemark("结单：" + orderId);
            orderLogManager.add(logInfo);

            return true;
        }


        return false;
    }

    public List<OrderInfoEO> queryOrderForCreateBQ() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.Statement.getCode());
        queryMap.put("isBuyInsurance", true);
        queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<OrderInfoEO> orders = orderInfoDBDAO.selectByMap(queryMap);
        return orders;
    }

    /**
     * 订单从寄售全自动机器人退回寄售物服发货
     *
     * @param orderId
     * @param subOrderId
     * @param reason
     */
    @Override
    @Transactional
    public void sendbackFromJsRobot(String orderId, Long subOrderId, String reason) {
        ConfigResultInfoEO result = configResultInfoDBDAO.selectByIdForUpdate(subOrderId);
        if (result == null) {
            throw new SystemException(ResponseCodes.IllegalArguments.getCode(), "未找到订单");
        }
        if (!result.getIsConsignment() || result.getIsJsRobot() == null || !result.getIsJsRobot()) {
            throw new SystemException(ResponseCodes.NotJsRobotOrder.getCode(), ResponseCodes.NotJsRobotOrder.getMessage());
        }

        if (result.getState() != OrderState.WaitDelivery.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), "订单状态已改变，不能退回");
        }

        // 重新安排寄售客服
        Long traderId = serviceSortManager.getNextServiceId(UserType.ConsignmentService.getCode(), null);
        if (traderId == null) {
            throw new SystemException(ResponseCodes.EmptyTraderList.getCode(), ResponseCodes.EmptyTraderList.getMessage());
        }

        // 更新配单记录为寄售，不自动上号交易
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", subOrderId);
        params.put("orderId", orderId);
        params.put("reason", reason);
        params.put("traderId", traderId);
        int count = configResultInfoDBDAO.sendbackFromJsRobot(params);
        // 更新成功
        if (count > 0) {
            // 客服待发货订单数量+1
            serviceSortManager.incOrderCount(traderId);

            OrderLogInfo log = new OrderLogInfo();
            log.setLogType(LogType.ORDER_DISTRIBUTION);
            log.setOrderId(orderId);
            log.setRemark(orderId + "_" + subOrderId + ",订单从寄售全自动机器人退回重配，原因：" + reason);
            orderLogManager.add(log);
        }
    }

    /**
     * 更新订单为已评价
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void updateOrderAlreadyEvaluate(String orderId) {
        orderInfoDBDAO.updateOrderAlreadyEvaluate(orderId);
    }

    /**
     * 更新订单为已追加评价
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void updateOrderAlreadyReEvaluate(String orderId) {
        orderInfoDBDAO.updateOrderAlreadyReEvaluate(orderId);
    }

    /**
     * 查询指定订单状态下，超过4个小时未评价的订单号
     */
    @Override
    public List<String> queryAfter4HourNotEvaluateOrders() {
        // 开始时间，2016-02-04
        Calendar c = Calendar.getInstance();
        //  //start by汪俊杰 20170517
        c.set(2016, 1, 4, 0, 0, 0);
        //end
        Map<String, Object> params = Maps.newHashMap();
        params.put("beginTime", c.getTime());
        return orderInfoDBDAO.queryAfter4HourNotEvaluateOrders(params);
    }

    /**
     * 返回订单信息
     *
     * @param params
     * @return
     */
    @Override
    public List<SimpleOrderDTO> selectOrderByMap(Map<String, Object> params) {
        return orderInfoDBDAO.selectOrderByMap(params);
    }


}

package com.wzitech.gamegold.shorder.business.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
import com.wzitech.gamegold.common.expection.OrderToCompletePartException;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.dto.SellerDTO;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.IncomeType;
import com.wzitech.gamegold.shorder.enums.LogTypeEnum;
import com.wzitech.gamegold.shorder.enums.OrderPrefix;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出货单管理
 *
 * @author yemq
 */
@Component
public class DeliveryOrderManagerImpl extends AbstractBusinessObject implements IDeliveryOrderManager {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String SELLER_REASON = "出货商下错区服";

    @Autowired
    private AsyncPushToMainMethodsImple asyncPushToMainMehtods;

    @Autowired
    private IPurchaseOrderDBDAO purchaseOrderDBDAO;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    private IDeliveryOrderIdRedisDao deliveryOrderIdRedisDao;

    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private IPurchaserDataManager purchaserDataManager;

    @Autowired
    private IPayDetailManager payDetailManager;

    @Autowired
    private IFundManager fundManager;

    @Autowired
    private IDeliveryOrderAutoConfigManager autoConfigManager;

    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;

    @Autowired
    private ISystemConfigManager systemConfigManager;

    @Autowired
    private IGameAccountDBDAO gameAccountDBDAO;

    @Autowired
    private ISplitRepositoryLogManager splitRepositoryLogManager;

    @Autowired
    private IShGameConfigManager shGameConfigManager;
    @Autowired
    private IQqOnLineManager qqOnLineManager;

    @Autowired
    private IOrderLogRedisDao orderLogRedisDao;

    @Autowired
    private IHxChatroomNetWorkDao hxChatroomNetWorkDao;

    @Autowired
    private IOrderUserLogManager orderUserLogManager;

    @Autowired
    private IUserInfoDBDAO userInfoDBDAO;

    @Autowired
    private IGameAccountManager gameAccountManager;

    @Autowired
    private IDeliveryOrderAutoConfigManager deliveryOrderAutoConfigManager;

    @Autowired
    private IDeliveryOrderStartRedisDao deliveryOrderStartRedisDao;

    @Autowired
    private ISellerDTOdao sellerDTOdao;

    @Autowired
    ImqUtilForOrderCenterToMain mqUtilForOrderCenterToMain;

    @Autowired
    private IBalckListDao balckListDao;

    @Autowired
    IShGameConfigDao shGameConfigDao;

    @Autowired
    private ITradeManager tradeManager;

    @Autowired
    private IPurchaseGameManager purchaseGameManager;

    @Autowired
    private ISyncRepositoryManager syncRepositoryManager;


//    @Autowired
//    private IOrderLogDao orderLogDao;

    /**
     * 创建出货单
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder createOrder(DeliveryOrder order) {
        //判断当前出货商是不是,黑名单用户
        BlackListEO userByLoginAccount = balckListDao.blackListAccount(CurrentUserContext.getUserLoginAccount(), false);
        if (userByLoginAccount != null) {
            throw new SystemException(ResponseCodes.ErrorBlacklistUser.getCode(),
                    ResponseCodes.ErrorBlacklistUser.getMessage());
        }

        if (StringUtils.isBlank(order.getRoleName()))
            throw new SystemException(ResponseCodes.EmptyRoleName.getCode(),
                    ResponseCodes.EmptyRoleName.getMessage());

        if (order.getCount() == null || order.getCount() <= 0)
            throw new SystemException(ResponseCodes.EmptyDeliveryCount.getCode(),
                    ResponseCodes.EmptyDeliveryCount.getMessage());

        if (order.getDeliveryType() == null) {
            throw new SystemException(ResponseCodes.EmptyDeliveryType.getCode(),
                    ResponseCodes.EmptyDeliveryType.getMessage());
        }
//        if (order.getTradeType() == null) {
//            throw new SystemException(ResponseCodes.TheMachineOrderNotTradeTypeId.getCode(),
//                    ResponseCodes.TheMachineOrderNotTradeTypeId.getMessage());
//        }
        if (order.getTradeLogo() == null) {
            throw new SystemException(ResponseCodes.TheOrderNotTradeTypeId.getCode(),
                    ResponseCodes.TheOrderNotTradeTypeId.getMessage());
        }
        // 获取采购单信息
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectById(order.getCgId());
        //采购单是特殊的订单无视库存信息
        DeliverySubOrder deliverySubOrder = null;
        if (StringUtils.isBlank(order.getAppealOrder())) {
            // 采购单不存在或已下架
            if (purchaseOrder == null || !purchaseOrder.getIsOnline())
                throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                        ResponseCodes.EmptyPurchaseOrder.getMessage());

            //从配置中取交易类型
            PurchaseGame purchaseGame = purchaseGameManager.selectByPurchaseAccount(purchaseOrder.getBuyerAccount(), purchaseOrder.getGameName(), purchaseOrder.getGoodsTypeName());
            logger.info("创建出货单,purchaseGame:{}", purchaseGame);
            if (purchaseGame == null || purchaseGame.getDeliveryTypeId() == null) {
                throw new SystemException(ResponseCodes.NoConfig.getCode(), ResponseCodes.NoConfig.getMessage());
            }
            if (purchaseGame.getDeliveryTypeId().intValue() == DeliveryTypeEnum.Stop.getCode()) {
                throw new SystemException(ResponseCodes.NoSeller.getCode(), ResponseCodes.NoSeller.getMessage());
            }
            if (purchaseGame.getDeliveryTypeId().intValue() != order.getDeliveryType().intValue()) {
                throw new SystemException(ResponseCodes.DeliveryTypeChange.getCode(), ResponseCodes.DeliveryTypeChange.getMessage());
            }
            List<String> tradeTypeIdList = Arrays.asList(purchaseGame.getTradeTypeId().split(","));
            if (!tradeTypeIdList.contains(order.getTradeType().toString())) {
                throw new SystemException(ResponseCodes.NoSupportTrade.getCode(), ResponseCodes.NoSupportTrade.getMessage());
            }
//            order.setDeliveryType(purchaseGame.getDeliveryTypeId() == DeliveryTypeEnum.Stop.getCode() ? order.getDeliveryType() : purchaseGame.getDeliveryTypeId());

            if (order.getCgId() == null)
                throw new SystemException(ResponseCodes.EmptyPurchaseOrderId.getCode(),
                        ResponseCodes.EmptyPurchaseOrderId.getMessage());

            if (DeliveryTypeEnum.Manual.getCode() == order.getDeliveryType() && StringUtils.isBlank(order.getAddress()))
                throw new SystemException(ResponseCodes.EmptyPlaceName.getCode(),
                        ResponseCodes.EmptyPlaceName.getMessage());

            if (StringUtils.isBlank(order.getPhone()))
                throw new SystemException(ResponseCodes.NullPhoneNumber.getCode(),
                        ResponseCodes.NullPhoneNumber.getMessage());

            if (StringUtils.isBlank(order.getQq()))
                throw new SystemException(ResponseCodes.EmptyQQ.getCode(),
                        ResponseCodes.EmptyQQ.getMessage());
            // 出货数量不能大于采购数量
            if (order.getCount().longValue() > purchaseOrder.getCount().longValue())
                throw new SystemException(ResponseCodes.DeliveryCountGtPurchaseCount.getCode(),
                        ResponseCodes.DeliveryCountGtPurchaseCount.getMessage());

            // 出货数量不能小于单笔最小收货量
            if (order.getCount().longValue() < purchaseOrder.getMinCount().longValue()) {
                throw new SystemException(ResponseCodes.DeliveryCountLtPurchaseCount.getCode(),
                        ResponseCodes.DeliveryCountLtPurchaseCount.getMessage());
            }
        } else {
            //使用申诉单检测条件
            deliverySubOrder = this.checkDeliveryAppealOrder(order);
        }

        TradeLogoEnum typeByCode = null;
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()) {
            typeByCode = TradeLogoEnum.getTypeByCode(order.getTradeLogo());
            if (typeByCode == null) {
                throw new SystemException(ResponseCodes.TheMachineOrderErrorTradeTypeId.getCode(),
                        ResponseCodes.TheMachineOrderErrorTradeTypeId.getMessage());
            }
            //对订单是拍卖交易，并且等级为空的
            if (typeByCode.getCode() == TradeLogoEnum.AuctionTrade.getCode()) {
                if (order.getSellerRoleLevel() == null)
                    throw new SystemException(ResponseCodes.TheMachineOrderNotSellerRoleLevel.getCode(),
                            ResponseCodes.TheMachineOrderNotSellerRoleLevel.getMessage());
                if (order.getSellerRoleLevel() < 35)
                    throw new SystemException(ResponseCodes.NotLessThan35LevelSh.getCode(),
                            ResponseCodes.NotLessThan35LevelSh.getMessage());
            }
        }
        //机器收货订单添加检测
//            if( order.getSellerRoleLevel() == null){
//                throw new SystemException(ResponseCodes.TheMachineOrderNotSellerRoleLevel.getCode(),
//                        ResponseCodes.TheMachineOrderNotSellerRoleLevel.getMessage());
//            }
        //因转为邮寄交易,原有机器的代码,现已无效
//        // 检查出货量是不是1000的整数倍
//        long count = order.getCount();
//        if (order.getDeliveryType().intValue() == ShDeliveryTypeEnum.Robot.getCode()) {
//            if (count % 1000 != 0) {
//                throw new SystemException(ResponseCodes.NotInMultiplesOf1000.getCode(),
//                        ResponseCodes.NotInMultiplesOf1000.getMessage());
//            }
//        }

        // 计算出货金额  cxymark1
        BigDecimal amount = purchaseOrder.getPrice().multiply(new BigDecimal(order.getCount().toString()))
                .setScale(2, RoundingMode.DOWN);

        SellerDTO buyer = sellerDTOdao.findByAccountAndUid(purchaseOrder.getBuyerAccount(), purchaseOrder.getBuyerUid());
        if (buyer == null) {
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());
        }
        // 验证收货方资金是否充足
        PurchaserData purchaserData = purchaserDataManager.selectByAccountForUpdate(purchaseOrder.getBuyerAccount());
        if (purchaserData == null) {
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());
        }

        BigDecimal availableAmount;
        if (buyer.getIsNewFund() != null && buyer.getIsNewFund() && buyer.getisAgree() != null && buyer.getisAgree()) {
            availableAmount = purchaserData.getAvailableAmountZBao() == null ? BigDecimal.ZERO : purchaserData.getAvailableAmountZBao();
        } else {
            availableAmount = purchaserData.getAvailableAmount() == null ? BigDecimal.ZERO : purchaserData.getAvailableAmount();
        }
        if (amount.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.PurchaserFundIsNotEnough.getCode(),
                    ResponseCodes.PurchaserFundIsNotEnough.getMessage());
        }

        BigDecimal balanceLimit = systemConfigManager.getBigDecimalValueByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
        BigDecimal balance = availableAmount.subtract(amount);
        if (balance.compareTo(balanceLimit) < 0) {
            logger.info("采购商余额小于:{}，停止收货", balanceLimit);
            throw new SystemException(ResponseCodes.PurchaserFundIsNotEnough.getCode(),
                    ResponseCodes.PurchaserFundIsNotEnough.getMessage());
        }

//        if (buyer.getIsNewFund() != null && buyer.getIsNewFund() && buyer.getisAgree() != null && buyer.getisAgree()) {
//            if (amount.compareTo(purchaserData.getAvailableAmountZBao()) == 1) {
//                throw new SystemException(ResponseCodes.PurchaserFundIsNotEnough.getCode(),
//                        ResponseCodes.PurchaserFundIsNotEnough.getMessage());
//            }
//        }
//        if (!(buyer.getIsNewFund() != null && buyer.getIsNewFund() && buyer.getisAgree() != null && buyer.getisAgree())) {
//            if ((amount.compareTo(purchaserData.getAvailableAmount()) == 1))
//                throw new SystemException(ResponseCodes.PurchaserFundIsNotEnough.getCode(),
//                        ResponseCodes.PurchaserFundIsNotEnough.getMessage());
//            // 采购商余额小于该值，停止收货
//            SystemConfig config = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
//            if (config != null) {
//                BigDecimal balanceLimit = new BigDecimal(config.getConfigValue());
//                if (balanceLimit != null) {
//                    BigDecimal balance = purchaserData.getAvailableAmount().subtract(amount);
//                    if (balance.compareTo(balanceLimit) < 0) {
//                        logger.info("采购商余额小于:{}，停止收货", balanceLimit);
//                        throw new SystemException(ResponseCodes.PurchaserFundIsNotEnough.getCode(),
//                                ResponseCodes.PurchaserFundIsNotEnough.getMessage());
//                    }
//                }
//            }
//        }

        //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
        //手工收货，采购单库存直接减掉
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
//            purchaseOrderManager.updatePurchaseOrderCount(purchaseOrder.getBuyerAccount(), purchaseOrder.getGameName(), purchaseOrder.getRegion(), purchaseOrder.getServer(), purchaseOrder.getGameRace(), -order.getCount());
            purchaseOrderManager.updatePurchaseOrderCountById(purchaseOrder.getId(), -order.getCount());
        }
        //end by 20170728 汪俊杰 解决线上数据堵塞等待问题
        // 生成出货主订单号
        //老资金订单号SH开头，新资金订单号SG开头 ZW_C_JB_00021
        String orderId;
        //如果出货单卖家开通了新资金
        if (buyer.getIsNewFund() != null && buyer.getIsNewFund() && buyer.getisAgree() != null && buyer.getisAgree()) {
            orderId = deliveryOrderIdRedisDao.getNewFundOrderId();
        } else {
            orderId = deliveryOrderIdRedisDao.getOrderId();
        }
        //申诉单逻辑
        if (deliverySubOrder != null) {
            deliverySubOrder.setAppealOrder(orderId);
            deliverySubOrderDao.update(deliverySubOrder);
        }


        //获取单位
        String moneyName = "";
        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(purchaseOrder.getGameName(), ServicesContants.GOODS_TYPE_GOLD, true, null);
        if (shGameConfig != null) {
            moneyName = shGameConfig.getUnitName();
        }
        //手工收货订单的物服全部设置为手动收货商
        if (order.getDeliveryType().intValue() == ShDeliveryTypeEnum.Manual.getCode()) {
            order.setTakeOverSubjectId("手动收货商");
        }
        order.setOrderId(orderId);
        order.setBuyerAccount(purchaseOrder.getBuyerAccount());
        order.setBuyerUid(purchaseOrder.getBuyerUid());
        order.setSellerAccount(CurrentUserContext.getUserLoginAccount());
        order.setSellerUid(CurrentUserContext.getUidStr());
        order.setGameName(purchaseOrder.getGameName());
        order.setRegion(purchaseOrder.getRegion());
        order.setServer(purchaseOrder.getServer());
        order.setGameRace(purchaseOrder.getGameRace());
        order.setPrice(purchaseOrder.getPrice());
        order.setAmount(amount);
        order.setMoneyName(moneyName);
        order.setRealCount(0L);
        order.setGoodsType(purchaseOrder.getGoodsType());
        order.setGoodsTypeName(purchaseOrder.getGoodsTypeName());
        order.setRealAmount(BigDecimal.ZERO);
        order.setStatus(DeliveryOrderStatus.WAIT_TRADE.getCode()); // 等待处理
        order.setTransferStatus(DeliveryOrder.NOT_TRANSFER);    // 未转账
        order.setCreateTime(new Date());
        QqOnLineEO qqEO = qqOnLineManager.getRandomQq();
        if (qqEO != null) {
            order.setServiceQq(qqEO.getQqNumber());
            order.setServiceNickname(qqEO.getNickname());
        }
        order.setBuyerPhone("");
        deliveryOrderDao.insert(order);


        //start by 20170728 汪俊杰 解决线上数据堵塞等待问题
//        //手工收货，采购单库存直接减掉
//        if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), -order.getCount());
//        }
        //end by 20170728 汪俊杰 解决线上数据堵塞等待问题

        // 写入订单日志
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(order.getOrderId());

        String tradeTypeName = order.getTradeTypeName();
        // orderLog.setLog("欢迎光临5173，收货商已收到订单提醒，当前交易方式为：" + tradeTypeName + "，请登录游戏到达交易地点，并点击右上角按钮【我已登录】。...");
        // deliveryOrderLogManager.writeLog(orderLog);

        String sellerLog = "订单创建成功，收货商已收到订单提醒。当前为" + tradeTypeName + "，请登录游戏到达交易地点，并点击右上角按钮【我已登录】。";
        String buyerLog = null;
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
            buyerLog = "欢迎光临5173，当前交易方式为：" + tradeTypeName + "，请登录游戏到达交易地点。";
        } else if (order.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()) {
            sellerLog = "订单创建成功，收货商已收到订单提醒。当前为" + typeByCode.getType() + "，机器收货";
        }
        Map<String, String> ids = orderUserLogManager.saveChatLog(sellerLog, buyerLog, UserLogType.SystemLogType, order);
        // 写入订单日志
        String sellerLogOne = "请在10分钟内点击【我已登录】按钮，超时自动撤单";
        orderUserLogManager.saveChatLog(sellerLogOne, null, UserLogType.SystemLogType, order);
        logger.info("生成一笔出货主订单，订单号：{}", orderId);

        // 冻结收货商相应资金

        //340096-0823  ZW_C_JB_00021
        //如果开通了新资金冻结商城资金的同时，还要去冻结7bao的资金
        if (orderId.contains(OrderPrefix.NEWORDERID.getName())) {
            fundManager.freezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER, purchaseOrder.getBuyerAccount(),
                    purchaseOrder.getBuyerUid(), orderId, amount);
        } else {
            fundManager.freezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, purchaseOrder.getBuyerAccount(),
                    orderId, amount);
        }
        return order;
    }

    private DeliverySubOrder checkDeliveryAppealOrder(DeliveryOrder order) {
        DeliverySubOrder deliverySubOrder;
        deliverySubOrder = deliverySubOrderDao.selectByIdForUpdate(Long.parseLong(order.getAppealOrder()));
        //对是否已过期做判断
        if (deliverySubOrder == null) {
            throw new SystemException(ResponseCodes.NoDeliverySubOrder.getCode(), ResponseCodes.NoDeliverySubOrder.getMessage());
        }
        if (deliverySubOrder.getAppealOrderStatus() != null) {
            throw new SystemException(ResponseCodes.AppealDeliveySubOrderTimeOut.getCode(), ResponseCodes.AppealDeliveySubOrderTimeOut.getMessage());
        }
        DeliveryOrder oldDeliveryOrder = deliveryOrderDao.selectByOrderId(deliverySubOrder.getOrderId());
        Boolean isOverTime = System.currentTimeMillis() - oldDeliveryOrder.getTradeEndTime().getTime() > systemConfigManager.getOverTime();
        if (isOverTime) {
            //overtime forbid appeal
            throw new SystemException(ResponseCodes.AppealDeliveySubOrderTimeOut.getCode(), ResponseCodes.AppealDeliveySubOrderTimeOut.getMessage());
        }
        deliverySubOrderDao.updateDeliverySubOrderAppealOrderStatusById(Long.parseLong(order.getAppealOrder()), AppealOrderStatus.TRADING.getCode());
        //是申诉单的情况下需要保存进入物服信息
        List<UserInfoEO> userInfoEOs = userInfoDBDAO.selectFreeConsignmentService();
        if (userInfoEOs == null || userInfoEOs.size() == 0) {
            order.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferFailed.getCode());
        } else {
            UserInfoEO userInfoEO = userInfoEOs.get(0);
            order.setTakeOverSubjectId(userInfoEO.getLoginAccount());
            order.setTakeOverSubject(userInfoEO.getRealName());
            order.setBuyerTel(userInfoEO.getPhoneNumber() == null ? "" : userInfoEO.getPhoneNumber());
            order.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
            order.setMachineArtificialTime(new Date());
        }
        order.setRelationOrderId(deliverySubOrder.getOrderId());
        return deliverySubOrder;
    }


    /**
     * 创建子订单
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder createAppealOrder(DeliveryOrder order) {
        DeliveryOrder deliveryOrder = this.createOrder(order);
        deliveryOrderAutoConfigManager.autoConfigOrderReady(deliveryOrder.getId());
        asyncPushToMainMehtods.orderPushToMain(deliveryOrder, 1);
        return deliveryOrder;
    }


    /**
     * 开始交易(我已登录游戏)
     * 没有订单号使用 机器邮寄收货使用
     *
     * @param order 订单对象
     */
    @Override
    public void startTradingFormOrder(DeliveryOrder order) {
        // 写入订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setLog("您已点击【我已登录】按钮。");
        deliveryOrderLogManager.writeLog(orderLog);
        // 自动配单
        try {
            autoConfigManager.autoConfigOrderReady(order.getId());
        } catch (NotEnoughRepertoryException e) {
            //撤单
            cancelOrderByNotEnoughRepertory(order.getOrderId(), DeliveryOrder.OHTER_REASON,
                    NotEnoughRepertoryException.MESSAGE);
            logger.info("自动化收货异常转数量不足撤单开始：+" + order.getOrderId() + "");
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int queryFund(String loginAccount) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusList", "1,2,3,7,8,9,10,11");
        map.put("sellerAccount", loginAccount);
        map.put("buyerAccount", loginAccount);
        int count = deliveryOrderDao.countUnEndOrder(map);
        return count;
    }

    /**
     * 查询等待排队的订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> queryInQueueOrderIds() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", DeliveryOrderStatus.INQUEUE.getCode());
        return deliveryOrderDao.queryInQueueOrderIds(params);
    }

    /**
     * 根据ID查询出货单
     *
     * @param id
     */
    @Override
    public DeliveryOrder getById(Long id) {
        return deliveryOrderDao.selectById(id);
    }

    /**
     * 根据ID查询出货单并锁住待修改
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder getByIdForUpdate(Long id) {
        return deliveryOrderDao.selectByIdForUpdate(id);
    }

    /**
     * 更新订单
     *
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeliveryOrder order) {
        deliveryOrderDao.update(order);
    }

    /**
     * 增加出货单实际出货数量，并计算实际出货金额
     *
     * @param id
     * @param count
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrRealCount(long id, long count) {
        DeliveryOrder order = this.getByIdForUpdate(id);

        // 累加实际出货数量
        long realCount = order.getRealCount() + count;

        // 计算实际金额
        BigDecimal realAmount = order.getPrice().multiply(new BigDecimal(order.getRealCount()))
                .setScale(2, RoundingMode.DOWN);

        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(id);
        deliveryOrder.setRealCount(realCount);
        deliveryOrder.setRealAmount(realAmount);
        deliveryOrderDao.update(order);
    }

    /**
     * 撤单,
     * ZW_C_JB_00004 jiyx 2017/5/12
     *
     * @param id
     * @param reason
     * @param remark
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(long id, int reason, String remark) {
        DeliveryOrder order = deliveryOrderDao.selectById(id);

        //机器收货，用户输入数量大于0，代表点击了我已发货，此后禁止撤单
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()) {
//            Long inputCount = deliverySubOrderManager.queryInputCount(id);
//            if (inputCount > 0){
//                throw new SystemException(ResponseCodes.InputCountGreatThanZeroForbiddenCancelOrder.getCode(),
//                        ResponseCodes.InputCountGreatThanZeroForbiddenCancelOrder.getMessage());
//            }
        }

        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!StringUtils.equals(loginAccount, order.getSellerAccount())) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),
                    ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }
        cancelOrderCommon(order.getId(), reason, remark, order, false);
    }

    /**
     * rc物服撤单 没有权限认证
     * ZW_C_JB_00004 jiyx 2017/5/12
     *
     * @param
     * @param reason
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderId, int reason, String remark) {
//        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(orderId);
//        String loginAccount = CurrentUserContext.getUserLoginAccount();
        cancelOrderCommon(deliveryOrder.getId(), reason, remark, deliveryOrder, true);
    }


    @Transactional(rollbackFor = Exception.class)
    private void cancelOrderCommon(long id, int reason, String remark, DeliveryOrder order, Boolean mark) {
        logger.info("撤单:id:" + id + "，reason:" + reason + "，remark:" + remark + "，order:" + order + "，mark:" + mark);
        // 查询交易中的子订单，更新子订单状态为“已取消”
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()) {
            //start ZW_C_JB_00004 jiyx
            List<DeliverySubOrder> subOrders = deliverySubOrderDao.queryWaitForTradeOrders(id, true);
            if (!CollectionUtils.isEmpty(subOrders)) {
//                HashMap<String, Object> selectMap = new HashMap<String, Object>();
//                selectMap.put("gameAccount", subOrders.get(0).getGameAccount());
//                selectMap.put("roleName", subOrders.get(0).getGameRole());
//                selectMap.put("locked", true);
//                selectMap.put("status", DeliveryOrderStatus.TRADING.getCode());
//                selectMap.put("deliveryType", ShDeliveryTypeEnum.Robot.getCode());
//                selectMap.put("notIn", "order_id NOT IN ('" + order.getOrderId() + "')");
//                int subOrderCount = deliverySubOrderDao.countByMap(selectMap);
//                logger.info("取消订单的参数：{} 查询到的数量 :" + subOrderCount + "", selectMap);
                for (DeliverySubOrder subOrder : subOrders) {
                    // 增加收货角色数量采购单收货数量，设置收货账号为空闲
                    //当有1笔的时候进行订单更新game_account 操作 <= 0 时 保持 空闲
                    if (StringUtils.isBlank(order.getAppealOrder())) {
                        purchaseOrderManager.updatePurchaseOrderCount(subOrder.getBuyerAccount(),
                                subOrder.getGameName(), subOrder.getRegion(), subOrder.getServer(),
                                subOrder.getGameRace(), subOrder.getGameAccount(), subOrder.getGameRole(),
                                subOrder.getCount(), GameAccount.S_FREE, true, subOrder.getOrderId());
                    } else {
                        //appealOrder不为空表示是申诉单
                        purchaseOrderManager.updatePurchaseOrderCountForAppealOrder(subOrder.getBuyerAccount(),
                                subOrder.getGameName(), subOrder.getRegion(), subOrder.getServer(),
                                subOrder.getGameRace(), subOrder.getGameAccount(),
                                subOrder.getGameRole(), GameAccount.S_FREE);
                        deliverySubOrderDao.updateDeliverySubOrderAppealOrderStatusById(id, AppealOrderStatus.CANCEL.getCode());

                    }
                    subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
                    subOrder.setReason(reason);
                    subOrder.setOtherReason(remark);
                    deliverySubOrderDao.update(subOrder);

                }
            }
            //end ZW_C_JB_00004 jiyx
        }
        //手工收货，子订单待发货状态，更新成撤单，采购单库存直接减掉。否则不让撤单
        else if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
            List<DeliverySubOrder> subOrders = deliverySubOrderDao.queryAllByOrderIdForUpdate(order.getOrderId());
            if (subOrders != null && subOrders.size() > 0) {
                if (subOrders.size() == 1) {
                    DeliverySubOrder subOrder = subOrders.get(0);
                    if (subOrder.getStatus() == DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
                        subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
                        subOrder.setReason(reason);
                        subOrder.setOtherReason(remark);
                        deliverySubOrderDao.update(subOrder);
                    } else {
                        throw new SystemException(ResponseCodes.CannotCancelOrderRealCountGtZero.getCode(),
                                ResponseCodes.CannotCancelOrderRealCountGtZero.getMessage());
                    }
                } else {
                    throw new SystemException(ResponseCodes.CannotCancelOrderRealCountGtZero.getCode(),
                            ResponseCodes.CannotCancelOrderRealCountGtZero.getMessage());
                }
            }

            // 删除聊天室
            if (StringUtils.isNotBlank(order.getChatroomId())) {
                hxChatroomNetWorkDao.deleteChatroom(order.getChatroomId());
            }
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount());

            purchaseOrderManager.updatePurchaseOrderCountById(order.getCgId(), order.getCount());
        }

        DeliveryOrder orderLock = deliveryOrderDao.selectByOrderIdForUpdate(order.getOrderId());
        logger.info("撤单:{}", orderLock);
        if (orderLock.getStatus() != DeliveryOrderStatus.WAIT_TRADE.getCode()
                && orderLock.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()
                && orderLock.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
            throw new SystemException(ResponseCodes.ErrorOrderStatusCantCancelOrder.getCode(),
                    ResponseCodes.ErrorOrderStatusCantCancelOrder.getMessage());
        }

        if (orderLock.getRealCount().longValue() > 0 && orderLock.getMachineArtificialStatus() != null && orderLock.getMachineArtificialStatus() != MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
            throw new SystemException(ResponseCodes.CannotCancelOrderRealCountGtZero.getCode(),
                    ResponseCodes.CannotCancelOrderRealCountGtZero.getMessage());
        }

        orderLock.setReason(reason);
        orderLock.setOtherReason(remark);
        orderLock.setTradeEndTime(new Date());
        orderLock.setStatus(DeliveryOrderStatus.CANCEL.getCode());
        orderLock.setTradeEndTime(new Date());
        deliveryOrderDao.update(orderLock);

        int orderCenterStauts = OrderCenterOrderStatus.FAILD_TRADE.getCode();
        asyncPushToMainMehtods.orderPushToMain(orderLock, orderCenterStauts);

        // 解冻资金

        //如果开通了新资金，还需解冻7bao的资金  ZW_C_JB_00021 340096-20170825
//        String buyerAccount = orderLock.getBuyerAccount();
//        String uid = orderLock.getBuyerUid();
//        SellerDTO seller = sellerDTOdao.findByAccountAndUid(buyerAccount, uid);

        //redis添加状态 方便返回给rc
        deliveryOrderStartRedisDao.save(orderLock);

        OrderLog log = new OrderLog();
        log.setOrderId(orderLock.getOrderId());
        //start ZW_C_JB_00004 jiyx
        if (orderLock.getMachineArtificialStatus() != null && orderLock.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode() && mark == true) {
            if (SELLER_REASON.equals(remark)) {
                log.setLog("由于【出货商】的【" + remark + "】原因，客服进行订单取消操作。");
            } else {
                log.setLog("由于【收货商】的【" + remark + "】原因，客服进行订单取消操作。");
            }
        } else {
            log.setLog("发起撤单请求，撤单成功");
        }
        //end ZW_C_JB_00004 jiyx
        if (orderLock.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
            orderUserLogManager.saveChatLog(log.getLog(), log.getLog(), UserLogType.SystemLogType, orderLock);
        } else {
            deliveryOrderLogManager.writeLog(log);
        }

        if (order.getOrderId().contains(OrderPrefix.NEWORDERID.getName())) {
            DeliveryOrder deliveryOrder = this.getByOrderId(orderLock.getOrderId());
            fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER, deliveryOrder, orderLock.getAmount());
        } else {
            //未开通新资金继续走原有资金
            fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, orderLock.getBuyerAccount(),
                    orderLock.getOrderId(), orderLock.getAmount());
        }
    }

    /**
     * 申请部分完单
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyForCompletePart(long id) {
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);

        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (!StringUtils.equals(loginAccount, order.getSellerAccount())) {
            throw new SystemException(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),
                    ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }

        if (order.getDeliveryType() != null && order.getDeliveryType().intValue() != ShDeliveryTypeEnum.Robot.getCode()) {
            throw new SystemException(ResponseCodes.ErrorDeliveryType.getCode(), ResponseCodes.ErrorDeliveryType.getMessage());
        }
        if (order.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (order.getRealCount().longValue() == 0) {
            throw new SystemException(ResponseCodes.ZeroCantCompletePartOrder.getCode(),
                    ResponseCodes.ZeroCantCompletePartOrder.getMessage());
        }

        if (order.getRealCount().longValue() >= order.getCount().longValue()) {
            throw new SystemException(ResponseCodes.CannotCompletePartRealCountGtCount.getCode(),
                    ResponseCodes.CannotCompletePartRealCountGtCount.getMessage());
        }

        order.setStatus(DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode());
        order.setTradeEndTime(new Date());
        deliveryOrderDao.update(order);

        int orderCenterStatus = OrderCenterOrderStatus.ALREADY_SEND.getCode();
        asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);

        // 查询交易中的子订单，更新子订单状态为“已取消”
        /*List<DeliverySubOrder> subOrders = deliverySubOrderDao.queryWaitForTradeOrders(id, true);
        if (!CollectionUtils.isEmpty(subOrders)) {
            for (DeliverySubOrder subOrder : subOrders) {
                subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
                subOrder.setReason(102);
                subOrder.setOtherReason("申请部分完单交易取消");
                deliverySubOrderDao.update(subOrder);

                // 增加收货角色数量采购单收货数量，设置收货账号为空闲
                boolean isSuccess = purchaseOrderManager.updatePurchaseOrderCount(subOrder.getBuyerAccount(),
                        subOrder.getGameName(), subOrder.getRegion(), subOrder.getServer(),
                        subOrder.getGameRace(), subOrder.getGameAccount(), subOrder.getGameRole(),
                        subOrder.getCount(), GameAccount.S_FREE, true);
            }
        }

        // 订单结算
        settlement(order.getOrderId());*/

        //redis添加状态 方便返回给rc
        deliveryOrderStartRedisDao.save(order);

        OrderLog log = new OrderLog();
        log.setOrderId(order.getOrderId());
        log.setLog("卖家发起部分完单请求");
//        orderUserLogManager.saveChatLog(log.getLog(),log.getLog(),UserLogType.SystemLogType,order);
        deliveryOrderLogManager.writeLog(log);

        logger.info("订单：{}申请部分完单成功", order.getOrderId());
    }

    /**
     * 分页查找订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<DeliveryOrder> queryListInPage(Map<String, Object> map, int start, int pageSize, String
            orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return deliveryOrderDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    /**
     * 分页查找订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<DeliveryOrder> queryAppealOrderInPage(Map<String, Object> map, int start, int pageSize, String
            orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        List<SortField> sortFields = Lists.newArrayList();
        sortFields.add(new SortField(orderBy, isAsc ? SortField.ASC : SortField.DESC));
        return deliveryOrderDao.queryAppealOrderInPage(map, sortFields, start, pageSize);
    }

    /**
     * 统计金额和数量
     *
     * @param paramMap
     * @return
     */
    @Override
    public DeliveryOrder statisAmountAndCount(Map<String, Object> paramMap) {
        return deliveryOrderDao.statisAmountAndCount(paramMap);
    }

    /**
     * 查找所有符合条件的数据
     *
     * @param paramMap
     * @return
     */
    @Override
    public List<DeliveryOrder> selectAllOrder(Map<String, Object> paramMap) {
        return deliveryOrderDao.selectByMap(paramMap);
    }

    /**
     * 查找所有符合条件的数据不分页
     *
     * @param paramMap
     * @return
     */
    @Override
    public List<DeliveryOrder> selectAllorderByMap(Map<String, Object> paramMap) {
        return deliveryOrderDao.selectAllOrder(paramMap);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderId
     * @return
     */
    @Override
    public DeliveryOrder getByOrderId(String orderId) {
        return deliveryOrderDao.getByOrderId(orderId);
    }

    /**
     * 查询交易超时的订单(一直未点上游戏交易或是一直在排队的订单，超过30分钟后将被视为交易超时)
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> queryTradeTimeoutOrders() {
        return deliveryOrderDao.queryTradeTimeoutOrders();
    }

    /**
     * 超时订单，收货商长时间未分配角色/出货商长时间未点击我已发货
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> queryTimeoutOrders() {
        return deliveryOrderDao.queryTimeoutOrders();
    }

    /**
     * 机器收货超时订单
     */
    @Override
    public List<String> queryMachineDeliveryTimeoutOrders() {
        int tradeTimeOut = systemConfigManager.tradeTimeOut();
        return deliveryOrderDao.queryMachineDeliveryTimeoutOrders(tradeTimeOut);
    }

    /**
     * 修改收到的数量
     *
     * @param mainOrderId
     * @param subOrderId
     * @param receiveCount
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeReceiveCount(String mainOrderId, long subOrderId, long receiveCount) {
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
        subOrder.setRealCount(receiveCount);
        subOrder.setUpdateTime(new Date());
        deliverySubOrderDao.update(subOrder);

        long realCount = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", mainOrderId);
        List<DeliverySubOrder> subOrders = deliverySubOrderDao.selectByMap(params);
        for (DeliverySubOrder so : subOrders) {
            realCount += so.getRealCount();
        }
        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(mainOrderId);
        order.setRealCount(realCount);
        // 计算实际金额
        BigDecimal realAmount = null;
        if (order.getRealCount().compareTo(order.getCount()) == 1) {
            realAmount = order.getPrice().multiply(new BigDecimal(order.getCount()));
        } else {
            realAmount = order.getPrice().multiply(new BigDecimal(String.valueOf(realCount)));
        }
        realAmount = realAmount.setScale(2, RoundingMode.DOWN);
        order.setRealAmount(realAmount);
        deliveryOrderDao.update(order);

        String s = String.format("订单号：%s_%s,收到数量：%s, 订单实际收到数量:%s", mainOrderId, subOrderId,
                receiveCount, order.getRealCount());
        OrderLog log = new OrderLog();
        log.setOrderId(mainOrderId);
        log.setType(OrderLog.TYPE_INNER);
        log.setLog(s);
        deliveryOrderLogManager.writeLog(log);
    }

    /**
     * 取消超时的订单(一直未点上游戏交易或是一直在排队的订单，超过30分钟后将被视为交易超时)
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTimeoutOrder(Long id) {
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        logger.info("自动撤单:{}", order);

        if (order.getStatus() != DeliveryOrderStatus.WAIT_TRADE.getCode()
                && order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (order.getStatus() == DeliveryOrderStatus.WAIT_TRADE.getCode()) {
            order.setReason(408);
            if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode() ||
                    (order.getStatus() == DeliveryOrderStatus.WAIT_TRADE.getCode() && order.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode())) {
                order.setOtherReason("出货商10分钟内未进行交易");
            } else {
                order.setOtherReason("出货商30分钟内未进行交易");
            }
        } else if (order.getStatus() == DeliveryOrderStatus.INQUEUE.getCode()) {
            order.setReason(502);
            order.setOtherReason("排队超时");
        }

        // 写订单日志
        OrderLog log = new OrderLog();
        log.setOrderId(order.getOrderId());
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
            log.setLog("发起超时撤单请求，撤单成功");
            orderUserLogManager.saveChatLog(log.getLog(), log.getLog(), UserLogType.SystemLogType, order);
        } else if (order.getStatus() == DeliveryOrderStatus.INQUEUE.getCode()) {
            log.setLog("由于排队超过30分钟，发起超时撤单请求，撤单成功");
            deliveryOrderLogManager.writeLog(log);
        } else {
            log.setLog("由于您未在10分钟内操作，订单撤单");
            deliveryOrderLogManager.writeLog(log);
        }

        order.setStatus(DeliveryOrderStatus.CANCEL.getCode());
        order.setTradeEndTime(new Date());
        deliveryOrderDao.update(order);

        int orderCenterStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
        asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);

        //手工收货，采购单库存直接减掉
        if (order.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
//            purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), order.getCount());
            purchaseOrderManager.updatePurchaseOrderCountById(order.getCgId(), order.getCount());

            // 删除聊天室
            if (StringUtils.isNotBlank(order.getChatroomId())) {
                hxChatroomNetWorkDao.deleteChatroom(order.getChatroomId());
            }
        }

        //如果开通了新资金，还需解冻7bao的资金  ZW_C_JB_00021 340096-20170825
        if (order.getOrderId().contains(OrderPrefix.NEWORDERID.getName())) {
            //解冻新资金
            DeliveryOrder deliveryOrder = this.getByOrderId(order.getOrderId());
            fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER, deliveryOrder, order.getAmount());
        } else {
            //未开通新资金继续走原有资金
            fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, order.getBuyerAccount(),
                    order.getOrderId(), order.getAmount());
        }
    }


    /**
     * 取消所有的子订单
     *
     * @param mainOrderId
     * @param subOrderId
     * @param type
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAllSubOrder(String mainOrderId, Long subOrderId, DeliveryOrderGTRType type) {
        // 将该主订单下所有交易中的子订单状态改为撤单
        List<DeliverySubOrder> subOrders = deliverySubOrderDao.queryWaitForTradeOrders(subOrderId, true);
        if (!CollectionUtils.isEmpty(subOrders)) {
            DeliveryOrder order = deliveryOrderDao.getByOrderId(mainOrderId);
            for (DeliverySubOrder subOrder : subOrders) {
                if (subOrder.getId().longValue() == subOrderId.longValue()) continue;
                if (subOrder.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
                        && subOrder.getStatus().intValue() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode())
                    continue;

                // 用户申请部分完单的
                if (order.getStatus().intValue() == DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()) {
                    subOrder.setReason(102);
                    subOrder.setOtherReason("卖家申请部分完单，这笔订单取消");
                } else {
                    if (type == DeliveryOrderGTRType.MANUAL_INTERVENTION || type == DeliveryOrderGTRType.EXCEPTION_TIMEOUT_MANUAL) {
                        subOrder.setReason(DeliveryOrderGTRType.MANUAL_INTERVENTION.getCode());
                        subOrder.setOtherReason("有一笔人工介入，这笔订单取消");
                    } else if (type == DeliveryOrderGTRType.TRADE_TIMEOUT) {
                        subOrder.setReason(DeliveryOrderGTRType.TRADE_TIMEOUT.getCode());
                        subOrder.setOtherReason("有一笔交易超时，这笔订单取消");
                    }
                }

                subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
                subOrder.setUpdateTime(new Date());
                deliverySubOrderDao.update(subOrder);

                // 更新采购单和对应账号角色的采购数量和状态
                updateGameAccountInfo(subOrder, type, false);

                String content = String.format("订单号：%s_%s，%s，取消交易", mainOrderId, subOrderId,
                        type.getMessage());
                OrderLog log1 = new OrderLog();
                log1.setOrderId(mainOrderId);
                log1.setLog(content);
                log1.setType(OrderLog.TYPE_INNER);
//                orderUserLogManager.saveChatLog(log1.getLog(),log1.getLog(),UserLogType.SystemLogType,order);
                deliveryOrderLogManager.writeLog(log1);
            }
        }
    }

    /**
     * 更新游戏账号数据
     *
     * @param dbSubOrder
     * @param type
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGameAccountInfo(DeliverySubOrder dbSubOrder, DeliveryOrderGTRType type, Boolean offline) {
        logger.info("更新游戏账号信息,type:{},buyerAccount:{}, gameName:{},region:{},server:{},gameAccount:{},roleName:{}, offline:{}",
                new Object[]{type, dbSubOrder.getBuyerAccount(), dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole(), offline});

        if (type == DeliveryOrderGTRType.PACK_IS_FULL || type == DeliveryOrderGTRType.NOT_FMS) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("buyerAccount", dbSubOrder.getBuyerAccount());
            params.put("gameName", dbSubOrder.getGameName());
            params.put("region", dbSubOrder.getRegion());
            params.put("server", dbSubOrder.getServer());
            params.put("gameRace", dbSubOrder.getGameRace());
            params.put("gameAccount", dbSubOrder.getGameAccount());
            params.put("roleName", dbSubOrder.getGameRole());

            if (type == DeliveryOrderGTRType.PACK_IS_FULL) {
                // 更新为背包已满
                params.put("isPackFull", true);
            } else if (type == DeliveryOrderGTRType.NOT_FMS) {
                // 更新为不是收货角色
                params.put("isShRole", false);
            }
            gameAccountDBDAO.updateAccountByMap(params);
        }

        // 更新采购单和对应账号角色的采购数量和状态
        long count = dbSubOrder.getCount() - dbSubOrder.getRealCount();
        if (count < 0) count = 0;
        int status = offline ? GameAccount.S_OFFLINE : GameAccount.S_FREE;
        purchaseOrderManager.updatePurchaseOrderCount(dbSubOrder.getBuyerAccount(),
                dbSubOrder.getGameName(), dbSubOrder.getRegion(), dbSubOrder.getServer(),
                dbSubOrder.getGameRace(), dbSubOrder.getGameAccount(), dbSubOrder.getGameRole(),
                count, status, true, dbSubOrder.getOrderId());

    }

    /**
     * 人工完单
     *
     * @param mainOrderId             出货主订单号
     * @param subOrderReceiveCountMap 每个子订单收到的实际数量
     *                                <p>格式：</p>
     *                                <li>key=子订单ID</li>
     *                                <li>value=实际收到的数量</li>
     *                                status 判断是否是物服端的完单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GameAccount> manualFinishOrder(String mainOrderId, Map<Long, Long> subOrderReceiveCountMap, Boolean status) throws IOException {
        if (StringUtils.isBlank(mainOrderId))
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        if (subOrderReceiveCountMap == null || subOrderReceiveCountMap.size() == 0)
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());

        DeliveryOrder mainOrder = deliveryOrderDao.selectByOrderIdForUpdate(mainOrderId);
        logger.info(mainOrderId + "主订单mainOrder：{}", mainOrder);
        //start ZW_C_JB_00004 jiyx
        if (mainOrder.getStatus().intValue() != DeliveryOrderStatus.TRADING.getCode()
                && mainOrder.getStatus() != DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()
                && mainOrder.getStatus() != DeliveryOrderStatus.APPLY_COMPLETE_PART.getCode()
                && mainOrder.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()
                && mainOrder.getStatus() != DeliveryOrderStatus.WAIT_RECEIVE.getCode()) {
            throw new SystemException(ResponseCodes.CompleteOrder.getCode(), ResponseCodes.CompleteOrder.getMessage());
        }
        //end ZW_C_JB_00004 jiyx

        /*if (mainOrder.getStatus() != DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
            throw new SystemException(ResponseCodes.NotManualIntervention.getCode(),
                    ResponseCodes.NotManualIntervention.getMessage());
        }*/
        List<GameAccount> gameAccountList = new ArrayList<GameAccount>();
        long totalCount = mainOrder.getRealCount(); // 总共收到数量
        for (Map.Entry<Long, Long> entry : subOrderReceiveCountMap.entrySet()) {
            Long subOrderId = entry.getKey(); // 子订单号
            Long count = entry.getValue(); // 实际收到数量

            DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
            if (subOrder == null || subOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE.getCode() || subOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE_PART.getCode() || subOrder.getStatus().intValue() == DeliveryOrderStatus.CANCEL.getCode()) {
                //当子订单已完结，则忽略，注意下面计算主订单总数量时需要把这部分已收到的数量加上去
                logger.info("该子订单已结单subOrder：{}", subOrder);
                continue;
            }
            logger.info(mainOrderId + "子订单subOrder：{}", subOrder);
            subOrder.setRealCount(count);

            if (count.longValue() == 0) {
                //为0撤单
                subOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
            } else if (count.longValue() == subOrder.getCount().longValue()) {
                //恰好等于 完单
                subOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
            } else if (count.longValue() < subOrder.getCount().longValue()) {
                //小于 部分完单
                subOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            } else if (count.longValue() > subOrder.getCount().longValue()) {
                //多了 抛异常
                throw new SystemException(ResponseCodes.DeliveryCountGtPurchaseCount.getCode(), ResponseCodes.DeliveryCountGtPurchaseCount.getMessage());
            }

            subOrder.setUpdateTime(new Date());
            deliverySubOrderDao.update(subOrder);

            totalCount += count;

            // 增加收货角色数量采购单收货数量，设置收货账号为空闲
            long c = 0;
            if (subOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE.getCode()) {
                c = 0;
            } else if (subOrder.getStatus().intValue() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
                c = subOrder.getCount() - subOrder.getRealCount();
            } else if (subOrder.getStatus().intValue() == DeliveryOrderStatus.CANCEL.getCode()) {
                c = subOrder.getCount();
            }
            if (mainOrder.getDeliveryType() == ShDeliveryTypeEnum.Robot.getCode()) {
                //start ZW_C_JB_00004 jiyx
                HashMap<String, Object> selectMap = new HashMap<String, Object>();
                selectMap.put("locked", true);
                selectMap.put("gameAccount", subOrder.getGameAccount());
                selectMap.put("status", DeliveryOrderStatus.TRADING.getCode());
                selectMap.put("deliveryType", ShDeliveryTypeEnum.Robot.getCode());
                selectMap.put("gameRole", subOrder.getGameRole());
                selectMap.put("notIn", "order_id NOT IN ('" + subOrder.getOrderId() + "')");
                int subOrderCount = deliverySubOrderDao.countByMap(selectMap);
                logger.info(subOrderCount + "数量查询的参数：{}", selectMap);
                //当有1笔的时候进行订单更新game_account 操作 >2时 保持收货中状态
                boolean isSuccess = purchaseOrderManager.updatePurchaseOrderCount(subOrder.getBuyerAccount(),
                        subOrder.getGameName(), subOrder.getRegion(), subOrder.getServer(),
                        subOrder.getGameRace(), subOrder.getGameAccount(), subOrder.getGameRole(),
                        c, subOrderCount <= 0 ? GameAccount.S_FREE : GameAccount.S_RECEIVING, true, subOrder.getOrderId());
                //end ZW_C_JB_00004 jiyx

            }

            if (count > 0) {
                //更新帐号库存
                gameAccountManager.addRepositoryCount(subOrder.getBuyerAccount(), subOrder.getGameName(),
                        subOrder.getRegion(), subOrder.getServer(), subOrder.getGameRace(), subOrder.getGameAccount(),
                        subOrder.getGameRole(), count, true);

//                List<GameAccount> gameAccountDataList = gameAccountManager.queryGameAccount(subOrder.getBuyerAccount(), subOrder.getGameName(), subOrder.getRegion(), subOrder.getServer(), subOrder.getGameAccount(), subOrder.getGameRole());
//                if (gameAccountDataList != null && gameAccountDataList.size() > 0) {
//                    GameAccount gameAccount = gameAccountDataList.get(0);
//                    gameAccountList.add(gameAccount);
//                }
            }
        }

        // 更新总的收货数量和实际金额
        mainOrder.setRealCount(totalCount);
        BigDecimal realAmount = null;
        if (mainOrder.getRealCount().compareTo(mainOrder.getCount()) == 1) {
            realAmount = mainOrder.getPrice().multiply(new BigDecimal(mainOrder.getCount()));
        } else {
            realAmount = mainOrder.getPrice().multiply(new BigDecimal(String.valueOf(totalCount)));
        }


        realAmount = realAmount.setScale(2, RoundingMode.DOWN);
        // 实际金额大于原先出货金额的，以原先出货金额为准打款
        if (realAmount.compareTo(mainOrder.getAmount()) == 1) {
            realAmount = mainOrder.getAmount();
        }

        mainOrder.setRealAmount(realAmount);
//        mainOrder.setRealCount(totalCount);
        mainOrder.setTradeEndTime(new Date());
        int orderCenterStatus = 0;
        // 更新主订单状态
        if (mainOrder.getRealCount().longValue() == 0) {
            mainOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
            orderCenterStatus = OrderCenterOrderStatus.FAILD_TRADE.getCode();
        } else if (mainOrder.getRealCount().longValue() >= mainOrder.getCount().longValue()) {
            mainOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
            orderCenterStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
        } else if (mainOrder.getRealCount().longValue() < mainOrder.getCount().longValue()) {
            mainOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            orderCenterStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
        }

        deliveryOrderDao.update(mainOrder);
        asyncPushToMainMehtods.orderPushToMain(mainOrder, orderCenterStatus);

        //手工收货，采购单库存直接减掉
        if (mainOrder.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
//            purchaseOrderManager.updatePurchaseOrderCount(mainOrder.getBuyerAccount(), mainOrder.getGameName(), mainOrder.getRegion(), mainOrder.getServer(), mainOrder.getGameRace(), mainOrder.getCount() - mainOrder.getRealCount());
            purchaseOrderManager.updatePurchaseOrderCountById(mainOrder.getCgId(), mainOrder.getCount() - mainOrder.getRealCount());
        }

        // 写入订单日志
        //start ZW_C_JB_00004 jiyx
        String log = "人工完单：";
        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
            if (status == true && mainOrder.getMachineArtificialStatus() != null && mainOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                OrderLog orderLog = new OrderLog();
                orderLog.setOrderId(mainOrder.getOrderId());
                orderLog.setLog("物服【" + mainOrder.getTakeOverSubjectId() + "】进行确认收货操作");
                orderLog.setType(OrderLog.TYPE_INNER);
                deliveryOrderLogManager.writeLog(orderLog);
                orderLog.setType(OrderLog.TYPE_NORMAL);
                log = "客服进行确认收货操作，资金5分钟左右转账至您的5173账户，请注意查收。";
            } else {
                log += "交易成功";
            }
        } else if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
            if (status == true && mainOrder.getMachineArtificialStatus() != null && mainOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                OrderLog orderLog = new OrderLog();
                orderLog.setOrderId(mainOrder.getOrderId());
                orderLog.setLog("物服【" + mainOrder.getTakeOverSubjectId() + "】进行部分完单操作，计划收货:" + mainOrder.getCount() + "，完成实际收货:" + totalCount);
                orderLog.setType(OrderLog.TYPE_INNER);
                deliveryOrderLogManager.writeLog(orderLog);
                orderLog.setType(OrderLog.TYPE_NORMAL);
                log = "客服进行部分完单操作，计划收货:" + mainOrder.getCount() + "，完成实际收货:" + totalCount;
            } else {
                log += "交易结束，已部分完单";
            }
        } else if (mainOrder.getStatus() == DeliveryOrderStatus.MANUAL_INTERVENTION.getCode()) {
            log += "交易结束，需人工介入";
        } else if (mainOrder.getStatus().intValue() == DeliveryOrderStatus.CANCEL.getCode()) {
            log += "交易取消";
        }
        //end ZW_C_JB_00004 jiyx
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(mainOrder.getOrderId());
        orderLog.setLog(log);
        orderLog.setType(OrderLog.TYPE_NORMAL);
        if (mainOrder.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
            orderUserLogManager.saveChatLog(orderLog.getLog(), orderLog.getLog(), UserLogType.SystemLogType, mainOrder);
        } else {
            deliveryOrderLogManager.writeLog(orderLog);
        }

        //rc物服没有获取登录用户
        //客户手动结单结束聊天室
        if (mainOrder.getDeliveryType().intValue() == ShDeliveryTypeEnum.Manual.getCode() && StringUtils.isNotBlank(mainOrder.getChatroomId()))
            hxChatroomNetWorkDao.deleteChatroom(mainOrder.getChatroomId());


        //start ZW_C_JB_00004 jiyx
        if (mainOrder.getMachineArtificialStatus() == null || mainOrder.getMachineArtificialStatus() != MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
            UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
            //删除redis
            //先查询到聊天记录
            String loginAccount = user.getLoginAccount();
            String orderId = mainOrder.getOrderId();
            List<OrderLog> list = orderUserLogManager.selectChattingRecordsByOrderId(orderId);
            //再对聊天记录循环删除
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    orderLogRedisDao.deleteChatRecord(orderId, loginAccount, list.get(i));
                }
            }
        } else {
            //rc物服没有获取登录用户
            //删除redis
            //先查询到聊天记录 获取主订单收货商的登录账号
            String loginAccount = mainOrder.getSellerAccount();
            String orderId = mainOrder.getOrderId();
            List<OrderLog> list = orderUserLogManager.selectChattingRecordsByOrderId(orderId);
            //再对聊天记录循环删除
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    orderLogRedisDao.deleteChatRecord(orderId, loginAccount, list.get(i));
                }
            }
        }
        //end ZW_C_JB_00004 jiyx


        //starat ZW_C_JB_00004 sunyang
        //redis添加状态 方便返回给rc
        deliveryOrderStartRedisDao.save(mainOrder);
        //end ZW_C_JB_00004 sunyang


        // 交易完成或部分完单的，生成分仓请求，资金结算
        if (mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()
                || mainOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {

            // 机器收货才能分仓，查询该订单下的账号信息
//            if (mainOrder.getDeliveryType().intValue() == ShDeliveryTypeEnum.Robot.getCode()) {
//                List<DeliverySubOrder> list = deliverySubOrderDao.findGameAccountGroupData(mainOrder.getId());
//                for (DeliverySubOrder subOrder : list) {
//                    // 交易完成后，生成分仓请求
//                    splitRepositoryRequestManager.create(subOrder);
//                }
//            }

            // 资金结算
            settlement(mainOrderId);
        } else if (mainOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
            // 交易取消，解冻资金
            //如果开通了新资金，还需解冻7bao的资金  ZW_C_JB_00021 340096-20170825
            String buyerAccount = mainOrder.getBuyerAccount();
            String uid = mainOrder.getBuyerUid();
            SellerDTO seller = sellerDTOdao.findByAccountAndUid(buyerAccount, uid);
            if (mainOrderId.contains(OrderPrefix.NEWORDERID.getName())) {
                DeliveryOrder deliveryOrder = this.getByOrderId(mainOrder.getOrderId());
                //解冻新资金
                fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER, deliveryOrder, mainOrder.getAmount());
            } else {
                //未开通新资金继续走原有资金
                fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, mainOrder.getBuyerAccount(),
                        mainOrder.getOrderId(), mainOrder.getAmount());
            }
        }

        logger.info("人工完单：{}，实际数量：{}，实际金额{}，操作者：{}", new Object[]{mainOrderId,
                mainOrder.getRealCount(), mainOrder.getRealAmount(), CurrentUserContext.getUserLoginAccount()});
        return gameAccountList;
    }

    /**
     * 查询待转账的订单ID集合
     *
     * @return
     */
    @Override
    public List<String> queryWaitTransferOrderIds() {
        return deliveryOrderDao.queryWaitTransferOrderIds();
    }

    /**
     * 出货单结算
     *
     * @param deliveryOrderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settlement(String deliveryOrderId) throws IOException {
        // 查询出货单信息
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(deliveryOrderId);

        // 没有交易完成的订单，不能进行付款
        if (deliveryOrder.getStatus().intValue() != DeliveryOrderStatus.COMPLETE.getCode()
                && deliveryOrder.getStatus().intValue() != DeliveryOrderStatus.COMPLETE_PART.getCode()) {
            throw new SystemException(ResponseCodes.OrderInTradingCantPay.getCode(),
                    ResponseCodes.OrderInTradingCantPay.getMessage());
        }

        if (deliveryOrder.getRealAmount() == BigDecimal.ZERO) {
            // 如果实际成交金额为0的，设置为交易完成
            DeliveryOrder order = new DeliveryOrder();
            order.setOrderId(deliveryOrderId);
            order.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
            order.setTransferStatus(DeliveryOrder.TRANSFER);
            order.setTradeEndTime(new Date());
            deliveryOrderDao.update(order);

            int orderCenterStatus = OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            asyncPushToMainMehtods.orderPushToMain(order, orderCenterStatus);
            logger.info("订单号：{}，实际交易金额为0，不需要结算，订单交易完成。", deliveryOrderId);
            return;
        }

        //完单资金结算，采购款在此开始
        boolean success = false;
        // 资金改造 ZW_C_JB_00021 3400960821

//        if (success) {
        // 设置转账状态为待转账
        DeliveryOrder order = new DeliveryOrder();
        order.setOrderId(deliveryOrderId);
        order.setTransferStatus(DeliveryOrder.WAIT_TRANSFER);
        deliveryOrderDao.update(order);

        logger.info("订单号：{}，结算成功，等待转账。", deliveryOrderId);
//        } else {
//            logger.info("订单号：{}，结算失败。", deliveryOrderId);
//        }

        //如果出货商开通了新资金，资金走新流程
        if (deliveryOrderId.contains(OrderPrefix.NEWORDERID.getName())) {
            //收货商新资金
            fundManager.newSettlement(deliveryOrder);
        } else {
            //调用资金结算
            fundManager.settlement(deliveryOrderId, deliveryOrder.getAmount(),
                    deliveryOrder.getRealAmount(), deliveryOrder.getBuyerAccount());
        }
    }

    /**
     * 出货单转账
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String orderId) {
        logger.info("出货订单号:{}，开始转账。", orderId);

        // 查询出货单信息
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(orderId);

        if (deliveryOrder.getStatus().intValue() != DeliveryOrderStatus.COMPLETE.getCode()
                && deliveryOrder.getStatus().intValue() != DeliveryOrderStatus.COMPLETE_PART.getCode()) {
            throw new SystemException(ResponseCodes.OrderInTradingCantPay.getCode(),
                    ResponseCodes.OrderInTradingCantPay.getMessage());
        }

        if (deliveryOrder.getTransferStatus() != DeliveryOrder.WAIT_TRANSFER) {
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(),
                    ResponseCodes.OrderStatusHasChangedError.getMessage());
        }

        // 查询需要转账的付款明细记录
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chOrderId", orderId);
        params.put("status", PayDetail.WAIT_PAID);
        params.put("isLocked", true);
        List<PayDetail> waitPayDetailList = payDetailManager.queryByMap(params);
        if (CollectionUtils.isEmpty(waitPayDetailList)) return;
        int orderSubfix = 0;
        // 逐笔转账
        for (PayDetail payDetail : waitPayDetailList) {
            //
            String account = deliveryOrder.getSellerAccount();
            String uid = deliveryOrder.getSellerUid();
            // deliveryOrderDao.findByAccountAndUid(account, uid);
//            SellerDTO sellerInfo = sellerDTOdao.findByAccountAndUid(account,uid);

            boolean success = false;
            if (payDetail.getOrderId().contains(OrderPrefix.OLD_PAY_ID.getName())) {
                // 调用资金转账接口
                success = fundManager.transfer(orderId, payDetail.getPayOrderId(), deliveryOrder.getBuyerUid(),
                        deliveryOrder.getBuyerAccount(), deliveryOrder.getSellerUid(), deliveryOrder.getSellerAccount(),
                        payDetail.getAmount(), payDetail);
            } else {
                if (waitPayDetailList.size() > 1) {
                    orderSubfix += 1;
                }
                success = fundManager.transferFor7Bao(deliveryOrder, orderId, payDetail.getPayOrderId(), deliveryOrder.getBuyerUid(),
                        deliveryOrder.getBuyerAccount(), deliveryOrder.getSellerUid(), deliveryOrder.getSellerAccount(),
                        payDetail.getAmount(), payDetail, orderSubfix);
            }

            if (success) {
                payDetail.setStatus(PayDetail.PAID); // 设置为已付款
                payDetail.setPayTime(new Date());
                payDetailManager.update(payDetail);
                logger.info("出货订单号：{}，付款明细订单号：{}，转账成功。", orderId, payDetail.getOrderId());
            } else {
                logger.info("出货订单号：{}，付款明细订单号：{}，转账失败。", orderId, payDetail.getOrderId());
            }
        }

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("chOrderId", orderId);
        queryParams.put("isLocked", true);
        List<PayDetail> payDetails = payDetailManager.queryByMap(queryParams);
        boolean isAllPay = true; // 是否已经全部转账成功
        for (PayDetail payDetail : payDetails) {
            if (payDetail.getStatus().intValue() != PayDetail.PAID) {
                isAllPay = false;
                break;
            }
        }
        if (isAllPay) {
            // 设置出货单转账状态为已转账
            DeliveryOrder order = new DeliveryOrder();
            order.setOrderId(orderId);
            order.setTransferStatus(DeliveryOrder.TRANSFER);
            deliveryOrderDao.update(order);
            logger.info("出货订单号：{}，全部转账成功。", orderId);
        }
    }

    /**
     * 设置超时
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTimeoutOrder(Long id) {
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        logger.info("超时订单：{}。", order);
        if (order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode() && order.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }

        if (order.getStatus() == DeliveryOrderStatus.INQUEUE.getCode()) {
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setCreateTime(new Date());
            orderLog.setType(OrderLog.TYPE_NORMAL);
            orderLog.setLog("本次排队已经超时，系统已为您撤销此笔订单；您可以重新出货或者选择其他空闲商家。");
            deliveryOrderLogManager.writeLog(orderLog);
            //排队中，长时间未分配角色
            order.setOtherReason(ShTimeOutTypeEnum.NO_DISTRIB_ROLE.getName());
            order.setIsTimeout(true);
            deliveryOrderDao.update(order);
        } else if (order.getStatus() == DeliveryOrderStatus.TRADING.getCode()) {

            List<DeliverySubOrder> list = deliverySubOrderDao.querySubOrdersForUpdate(order.getId());
            if (list != null && list.size() > 0) {
                boolean isNotAssignRole = true;
                for (DeliverySubOrder subOrder : list) {
                    if (subOrder.getStatus() == DeliveryOrderStatus.WAIT_DELIVERY.getCode()) {
                        //交易中，长时间未点击发货
                        subOrder.setOtherReason(ShTimeOutTypeEnum.NO_CLICK_DELIVERY.getName());
                        subOrder.setIsTimeout(true);
                        subOrder.setUpdateTime(new Date());
                        deliverySubOrderDao.update(subOrder);

                        order.setOtherReason(ShTimeOutTypeEnum.NO_CLICK_DELIVERY.getName());
                        order.setIsTimeout(true);
                        deliveryOrderDao.update(order);
                        isNotAssignRole = false;
                    } else if (subOrder.getStatus() == DeliveryOrderStatus.DELIVERY_FINISH.getCode()) {
                        //交易中，出货商长时间未点击收货
                        subOrder.setOtherReason(ShTimeOutTypeEnum.NO_CLICK_RECEIVE.getName());
                        subOrder.setIsTimeout(true);
                        subOrder.setUpdateTime(new Date());
                        deliverySubOrderDao.update(subOrder);

                        order.setOtherReason(ShTimeOutTypeEnum.NO_CLICK_RECEIVE.getName());
                        order.setIsTimeout(true);
                        deliveryOrderDao.update(order);
                        isNotAssignRole = false;
                    }
                }
                if (isNotAssignRole) {
                    order.setOtherReason(ShTimeOutTypeEnum.NO_DISTRIB_ROLE.getName());
                    order.setIsTimeout(true);
                    deliveryOrderDao.update(order);
                }
            }

        }
    }


    /**
     * 统计金额汇总
     *
     * @param paramMap
     * @return
     */
    @Override
    public DeliveryOrder statisAmount(Map<String, Object> paramMap) {
        return deliveryOrderDao.statisAmount(paramMap);
    }


    //生成环信id

    public String registerHuanXin(Long id) {
        return null;
    }

    //根据收货商id查询出货商id
    @Override
    public DeliveryOrder queryDeliveryOrderByOrderId(String orderId) {
        return deliveryOrderDao.queryDeliveryOrderByOrderId(orderId);
    }

    @Override
    public void updateOrder(DeliveryOrder deliveryOrder) {
        deliveryOrderDao.updateOrder(deliveryOrder);
    }

    /**
     * 全自动异常订单分配寄售客服(手动)
     * ZW_C_JB_00004 mj
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributionService(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptySubOrderId.getCode(),
                    ResponseCodes.EmptySubOrderId.getMessage());
        }
        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
        logger.info("手动分配物服的主订单：{}", order);
        if (order.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyChId.getCode(),
                    ResponseCodes.EmptyChId.getMessage());
        }
        if (order == null) {
            throw new SystemException(ResponseCodes.EmptyMachineArtificialDeliveryOrder.getCode(),
                    ResponseCodes.EmptyMachineArtificialDeliveryOrder.getMessage());
        }
        //不是转人工失败的订单也不能分配寄售客服
        if (order.getMachineArtificialStatus() == null || MachineArtificialStatus.ArtificialTransferFailed.getCode() != order.getMachineArtificialStatus().intValue()) {
            throw new SystemException(ResponseCodes.NotTurnedMachineArtificialError.getCode(),
                    ResponseCodes.NotTurnedMachineArtificialError.getMessage());
        }
        //订单状态为交易中的才能分配寄售客服
        if (DeliveryOrderStatus.TRADING.getCode() != order.getStatus().intValue()) {
            throw new SystemException(ResponseCodes.ErrorOrderStatusDistributionService.getCode(),
                    ResponseCodes.ErrorOrderStatusDistributionService.getMessage());
        }

        //查询订单量最少的物服
        List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
        if (list == null || list.size() < 1) {
            throw new SystemException(ResponseCodes.NullConsignmentService.getCode(),
                    ResponseCodes.NullConsignmentService.getMessage());
        }
        UserInfoEO user = list.get(0);
        //对寄售客服信息进行判空
        if (user.getLoginAccount() == null || StringUtils.isBlank(user.getRealName())) {
            throw new SystemException(ResponseCodes.ErrorConsignmentService.getCode(),
                    ResponseCodes.ErrorConsignmentService.getMessage());
        }
        Date time = new Date();
        //将寄售客服信息更新到主订单中
        order.setTakeOverSubjectId(user.getLoginAccount());
        order.setTakeOverSubject(user.getRealName());
        order.setServiceNickname(user.getNickName() == null ? "" : user.getNickName());
        order.setServiceQq(user.getQq() == null ? "" : user.getQq());
        order.setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        order.setBuyerPhone(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        order.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
        order.setMachineArtificialTime(time);
        deliveryOrderDao.update(order);
        logger.info("订单号：{},成功分配物服", orderId);
        //插入对应日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setCreateTime(new Date());
        //写入内部日志
        orderLog.setType(OrderLog.TYPE_INNER);
        orderLog.setLog("本订单异常，自动分配物服失败，由人工分配给物服【" + user.getLoginAccount() + "】。");
        deliveryOrderLogManager.writeLog(orderLog);
        orderLog.setType(OrderLog.TYPE_NORMAL);
        orderLog.setLog("5173客服即将登录收货账号手动收货，请稍候。");
        deliveryOrderLogManager.writeLog(orderLog);

        //查询主订单对应的子订单
        List<DeliverySubOrder> deliverySubOrderList = deliverySubOrderDao.queryAllByOrderIdForUpdate(order.getOrderId());
        if (deliverySubOrderList != null && deliverySubOrderList.size() > 0) {
            for (DeliverySubOrder subOrder : deliverySubOrderList) {
                //将寄售客服信息更新到状态为交易中的子订单中(若有多笔交易中的子订单，只操作第一笔)
                if (DeliveryOrderStatus.TRADING.getCode() == subOrder.getStatus().intValue() && subOrder.getMachineArtificialStatus() != null
                        && MachineArtificialStatus.ArtificialTransferFailed.getCode() == subOrder.getMachineArtificialStatus()) {
                    logger.info("子订单分配物服：{}", subOrder);
                    subOrder.setTakeOverSubjectId(user.getLoginAccount());
                    subOrder.setTakeOverSubject(user.getRealName());
                    subOrder.setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
                    subOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
                    subOrder.setMachineArtificialTime(time);
                    deliverySubOrderDao.update(subOrder);
                    logger.info("订单号：{},子订单成功分配物服", subOrder.getOrderId());
                    break;
                }
            }
        }
    }

    /**
     * 查询自动化异常超时的订单
     *
     * @param minute
     * @return
     */
    @Override
    public List<Long> selectByAutometaTimeout(int minute) {
        return deliveryOrderDao.selectByAutometaTimeout(minute);
    }

//    /**
//     * 自动化异常创建子订单 修改主订单
//     * ZW_C_JB_00004 jiyx
//     *
//     * @param
//     * @param minute
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean createSubOrderByAutoMateError(Long deliveryOrderIds, Integer minute) {
//        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByIdForUpdate(deliveryOrderIds);
//        // 查询收货方可以分配的游戏角色账号,排序按账号空闲状态，同个游戏账号，收货数量多的靠前规则
//        if (deliveryOrder.getMachineArtificialStatus() != null && deliveryOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
//            logger.info("自动化异常时该订单已经被配单成功防止重复配置：{}", deliveryOrder);
//            return true;
//        }
//
//        Map<String, Object> params = Maps.newHashMap();
//        params.put("buyerAccount", deliveryOrder.getBuyerAccount());
//        params.put("gameName", deliveryOrder.getGameName());
//        params.put("region", deliveryOrder.getRegion());
//        params.put("server", deliveryOrder.getServer());
//        params.put("gameRace", deliveryOrder.getGameRace());
//        params.put("orderBy", "status asc,game_account asc,count desc");
//        params.put("isLock", true);
//        //查询正在交易中的订单
//        List<GameAccount> gameAccountList;
//        //如果有直接将这个交易中的订单角色返回
//        gameAccountList = gameAccountDBDAO.queryGameAccountByReceiving(params);
//        if (gameAccountList == null || gameAccountList.size() == 0) {
//            gameAccountList = gameAccountManager.queryGameAccountCanSh(deliveryOrder);
//            if (CollectionUtils.isEmpty(gameAccountList)) {
//                //这种情况没有办法创建子订单等待下次调用
//                logger.info("该订单的游戏账号都不处于空闲状态：{}", params.toString());
//                return false;
//            }
//            Iterator<GameAccount> iterator = gameAccountList.iterator();
//            while (iterator.hasNext()) {
//                GameAccount gameAccount = iterator.next();
//                // 查询这个账号是否在收货中，有的话删除，防止顶号
//                boolean inTrading = deliverySubOrderManager.queryGameAccountIsInTrading(gameAccount.getBuyerAccount(),
//                        gameAccount.getBuyerUid(), gameAccount.getGameName(), gameAccount.getRegion(),
//                        gameAccount.getServer(), gameAccount.getGameRace(), gameAccount.getGameAccount());
//                if (inTrading) {
//                    iterator.remove();
//                }
//            }
//            //创建游戏收货失败 没有可用的游戏账号 这种情况没有办法创建子订单等待下次调用
//            if (gameAccountList == null || gameAccountList.size() == 0) {
//                logger.info("该订单的游戏账号都处于收货中状态：{}", params.toString());
//                return false;
//            }
//        }
//
//        //设置异常转人工时间 原因
//        deliveryOrder.setStatus(DeliveryOrderStatus.TRADING.getCode());
//        deliveryOrder.setMachineArtificialReason(DeliveryOrderGTRType.EXCEPTION_TIMEOUT_MANUAL.getMessage());
//        deliveryOrder.setMachineArtificialTime(new Date());
////                deliverySubOrder.setDeliveryType(ShDeliveryTypeEnum.Manual.getCode());
//        //算法根据后台的客服接受的订单数量取第一个
//        List<UserInfoEO> userInfoEOs = userInfoManager.selectFreeConsignmentService();
//        //判断是否客服有可用的情况下
//        if (userInfoEOs == null || userInfoEOs.size() == 0) {
//            deliveryOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferFailed.getCode());
//            deliveryOrder.setTakeOverSubject("");
//            deliveryOrder.setTakeOverSubjectId("");
//            //没有客服可用 创建异常子订单 跳出
//            if (!createDeliverySubOrder(deliveryOrder, null, gameAccountList)) {
//                return false;
//            }
//            OrderLog orderLog = new OrderLog();
//            orderLog.setCreateTime(new Date());
//            orderLog.setOrderId(deliveryOrder.getOrderId());
//            if (minute != null) {
//                orderLog.setLog("由于附魔交易官方调整,无法完成自动收货。5173正在分配客服，请勿关闭本页面。刷新页面显示最新出货信息。");
//            } else {
//                orderLog.setLog("由于" + deliveryOrder.getReason() + ",无法完成自动收货。5173正在分配客服，请勿关闭本页面。刷新页面显示最新出货信息。");
//            }
//            deliveryOrderLogManager.writeLog(orderLog);
//            logger.info("没有可用的物服调用");
//
//            update(deliveryOrder);
//
//            int orderCenterStatus = OrderCenterOrderStatus.WAIT_SEND.getCode();
//            orderPushToMain(deliveryOrder, orderCenterStatus);
//            return true;
//        } else {
//            //有客服可用 分配客服
//            deliveryOrder.setTakeOverSubject(userInfoEOs.get(0).getRealName());
//            deliveryOrder.setTakeOverSubjectId(userInfoEOs.get(0).getLoginAccount());
//            deliveryOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
//            if (!createDeliverySubOrder(deliveryOrder, userInfoEOs, gameAccountList)) {
//                return false;
//            }
//            OrderLog orderLog = new OrderLog();
//            orderLog.setCreateTime(new Date());
//            orderLog.setOrderId(deliveryOrder.getOrderId());
//            if (minute != null) {
//                orderLog.setLog("系统将该异常收货订单分配给物服【" + userInfoEOs.get(0).getLoginAccount() + "】。");
//                orderLog.setType(OrderLog.TYPE_INNER);
//                deliveryOrderLogManager.writeLog(orderLog);
//                orderLog.setType(OrderLog.TYPE_NORMAL);
//                //TODO 修改提示
////                orderLog.setLog("由于附魔交易官方调整，无法完成自动收货。5173客服即将登录收货账号手动手货，请勿关闭本页面。刷新页面显示最新出货提示。");
//                orderLog.setLog("由于附魔交易官方调整，无法完成自动收货。5173客服即将登录收货账号手动手货，请勿关闭本页面。刷新页面显示最新出货提示。");
//            } else {
//                orderLog.setLog("系统将该异常收货订单分配给物服【" + userInfoEOs.get(0).getLoginAccount() + "】。");
//                orderLog.setType(OrderLog.TYPE_INNER);
//                deliveryOrderLogManager.writeLog(orderLog);
//                orderLog.setType(OrderLog.TYPE_NORMAL);
//                orderLog.setLog("由于" + deliveryOrder.getReason() + ",无法完成自动收货。5173客服即将登录收货账号手动手货，请勿关闭本页面。刷新页面显示最新出货提示。");
//            }
//            deliveryOrderLogManager.writeLog(orderLog);
//            logger.info("物服分配成功");
//
//            update(deliveryOrder);
//            return true;
//        }
//    }

    /**
     * 创建子订单更加是否有rc物服
     * ZW_C_JB_00004 jiyx
     *
     * @param deliveryOrder
     * @param userInfoEOs
     * @param gameAccountList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean createDeliverySubOrder(DeliveryOrder deliveryOrder, List<UserInfoEO> userInfoEOs, List<GameAccount> gameAccountList) {
        //查询是否有对应的子订单
        List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.queryAllByOrderId(deliveryOrder.getOrderId());
        if (deliverySubOrders == null || deliverySubOrders.size() == 0) {
            //没有创建子订单
            boolean subOrder = deliveryOrderAutoConfigManager.createSubOrder(deliveryOrder, gameAccountList.get(0), deliveryOrder.getCount());
            if (!subOrder) {
                return false;
            }
        } else {
            //有查询有几笔子订单
            if (deliverySubOrders.size() >= 2) {
                for (DeliverySubOrder d : deliverySubOrders) {
                    //当子订单是有一笔交易完成时进行部分完单操作 之前的所以操作回滚
                    if (d.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
                        throw new OrderToCompletePartException();
                    }
                }
                logger.info("错误的订单数据：{}", deliveryOrder.getOrderId() + "有生成2条或以上的子订单");
                return false;
            } else {
                DeliverySubOrder deliverySubOrder = deliverySubOrders.get(0);
                //更新子订单
//                deliverySubOrder.setDeliveryType(ShDeliveryTypeEnum.Manual.getCode());
                deliverySubOrder.setMachineArtificialReason(DeliveryOrderGTRType.EXCEPTION_TIMEOUT_MANUAL.getMessage());
                deliverySubOrder.setMachineArtificialTime(new Date());
                deliverySubOrder.setMachineArtificialStatus(deliveryOrder.getMachineArtificialStatus());
//                deliverySubOrder.setTakeOverSubject(deliveryOrder.getTakeOverSubject());
//                deliverySubOrder.setTakeOverSubjectId(deliveryOrder.getTakeOverSubjectId());
//                deliverySubOrder.set (userInfoEOs.get(0).getNickName());
                if (userInfoEOs != null && userInfoEOs.size() > 0) {
                    deliverySubOrder.setTakeOverSubject(userInfoEOs.get(0).getRealName());
                    deliverySubOrder.setTakeOverSubjectId(userInfoEOs.get(0).getLoginAccount());
                } else {
                    deliverySubOrder.setTakeOverSubject("");
                    deliverySubOrder.setTakeOverSubjectId("");
                }
                deliverySubOrderDao.update(deliverySubOrder);
            }
        }
        return true;
    }

    /**
     * 物服完单接口
     * ZW_C_JB_00004 jiyx
     *
     * @param orderId
     * @param goldCount
     */
    @Override
    public void logisticsSheet(String orderId, Long goldCount) throws IOException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", orderId);
        List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.selectByMap(queryMap);
        if (deliverySubOrders == null || deliverySubOrders.size() == 0) {
            logger.info("错误的订单没有子订单:{}", orderId);
            throw new SystemException(ResponseCodes.EmptyMachineArtificialDeliverySubOrder.getCode(),
                    ResponseCodes.EmptyMachineArtificialDeliverySubOrder.getMessage());
        } else if (deliverySubOrders.size() >= 2) {
            logger.info("错误的订单数据有多条子订单:{}", orderId);
            throw new SystemException(ResponseCodes.TroppoMachineArtificialDeliverySubOrder.getCode(),
                    ResponseCodes.TroppoMachineArtificialDeliverySubOrder.getMessage());
        }
        Map<Long, Long> subOrderReceiveCountMap = new HashMap<Long, Long>();
        subOrderReceiveCountMap.put(deliverySubOrders.get(0).getId(), goldCount);
        List<GameAccount> gameAccountList = manualFinishOrder(orderId, subOrderReceiveCountMap, true);
    }

    /**
     * 确认发货数量
     * ZW_C_JB_00004 jyx
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmShipment(Map<String, Integer> map) {
        UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
        String loginAccount = userInfo.getLoginAccount();
        List<DeliverySubOrder> deliverySubOrders = new ArrayList<DeliverySubOrder>();
        String moneyName = null;
        long sumCount = 0;
        OrderLog orderLog = new OrderLog();

        for (Map.Entry<String, Integer> m : map.entrySet()) {
            DeliverySubOrder selectDeliverySubOrder = deliverySubOrderDao.selectByIdForUpdate(Long.parseLong(m.getKey()));
            if (selectDeliverySubOrder == null) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }
            if (!loginAccount.equals(selectDeliverySubOrder.getSellerAccount())) {
                logger.info("确认收货人不是订单卖家，禁止操作");
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(), ResponseCodes.NotYourOrder.getMessage());
            }
            if (selectDeliverySubOrder.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
                throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
            }
            if (sumCount == 0L) {
                orderLog.setOrderId(selectDeliverySubOrder.getOrderId());
                //写入内部日志
                orderLog.setLog("您已点击【已发货】按钮并确认。");
                deliveryOrderLogManager.writeLog(orderLog);
            }
            moneyName = selectDeliverySubOrder.getMoneyName();
            orderLog.setLog("收货角色：" + selectDeliverySubOrder.getGameRole() + "。您已完成邮寄，填写的出货数量：" + m.getValue() + "  " + (moneyName == null ? "万金" : moneyName) + "。");
            deliveryOrderLogManager.writeLog(orderLog);
            //订单更新数量，机器邮寄收货开始抓取数据
            sumCount += m.getValue();
            selectDeliverySubOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialAuto.getCode());
            selectDeliverySubOrder.setShInputCount(m.getValue().longValue());
            selectDeliverySubOrder.setUpdateTime(new Date());
            selectDeliverySubOrder.setSellerDeliveryTime(new Date());
            deliverySubOrders.add(selectDeliverySubOrder);
        }


        //日志继续写入
        StringBuffer sb = new StringBuffer();
        orderLog.setLog(sb.append("汇总").append(sumCount).append("  ").append(moneyName).append("。").toString());
        deliveryOrderLogManager.writeLog(orderLog);
        orderLog.setLog("注：出货后请小退或大退游戏，否则无法立即收到邮件。机器人开始上号查收邮件。");
        deliveryOrderLogManager.writeLog(orderLog);
        //对子订单批量更新
        deliverySubOrderDao.batchUpdate(deliverySubOrders);

//        // 查找订单
//        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
//        String buyerAccount = deliveryOrder.getBuyerAccount();
//        if (!loginAccount.equals(buyerAccount)) {
//            logger.info("确认收货人不是订单买家，禁止操作");
//            throw new SystemException(ResponseCodes.NotYourOrder.getCode(), ResponseCodes.NotYourOrder.getMessage());
//        }
//        if (deliveryOrder == null) {
//            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),
//                    ResponseCodes.OrderLogIdInvalid.getMessage());
//        }
//        deliveryOrder.setRealCount(realCount);
//        // 交易完成时间
//        //deliveryOrder.setTradeEndTime(new Date());
//
//        deliveryOrderDao.update(deliveryOrder);
    }


    /**
     * ZW_C_JB_00004 sunyang
     * 获取订单状态
     */
    @Override
    public int getOrderStatus(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        DeliveryOrder deliveryOrder = deliveryOrderStartRedisDao.get(orderId);
        if (deliveryOrder != null) {
            return deliveryOrder.getStatus();
        } else {
            DeliveryOrder deliveryOrderDB = deliveryOrderDao.getByOrderId(orderId);
            if (deliveryOrderDB == null) {
                throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),
                        ResponseCodes.OrderLogIdInvalid.getMessage());
            } else {
                deliveryOrderStartRedisDao.save(deliveryOrderDB);
                return deliveryOrderDB.getStatus();
            }
        }
    }

    /**
     * 根据订单id获取订单并锁表
     * <p>
     * ZW_C_JB_00004 sunyang
     *
     * @param orderId
     * @return
     */
    @Override
    public DeliveryOrder selectByOrderIdForUpdate(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),
                    ResponseCodes.OrderLogIdInvalid.getMessage());
        }
        return deliveryOrder;
    }

    /**
     * 设置分配失败原因
     * ZW_C_JB_00004 sunyang
     *
     * @param orderId
     * @param type
     */
    @Override
    public void setStartByOrderForUpdata(String orderId, Integer type) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),
                    ResponseCodes.EmptyOrderInfo.getMessage());
        }
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),
                    ResponseCodes.OrderLogIdInvalid.getMessage());
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setCreateTime(new Date());
        orderLog.setOrderId(deliveryOrder.getOrderId());
        orderLog.setLog("由于" + (DeliveryOrderGTRType.getTypeByCode(type).getMessage() == null ? "其他原因" : DeliveryOrderGTRType.getTypeByCode(type).getMessage()) +
                "，无法完成自动收货。5173正在分配客服，请勿关闭本页面。刷新页面显示最新出货信息。");
        deliveryOrderLogManager.writeLog(orderLog);

        List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.queryAllByOrderId(orderId);
        if (CollectionUtils.isEmpty(deliverySubOrders)) {
            //设置出货单 转人工状态(失败)
            deliverySubOrders.get(0).setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferFailed.getCode());
            deliverySubOrders.get(0).setTakeOverSubjectId("");
            deliverySubOrderDao.update(deliverySubOrders.get(0));
        }
        deliveryOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferFailed.getCode());
        deliveryOrder.setTakeOverSubjectId("");
        deliveryOrderDao.update(deliveryOrder);
    }

    /**
     * 存库不足撤单（配单逻辑调用）
     *
     * @param orderId
     * @param reason
     * @param remark
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderByNotEnoughRepertory(String orderId, int reason, String remark) {
        DeliveryOrder order = deliveryOrderDao.selectByOrderId(orderId);
        //写日志
        OrderLog orderLog = new OrderLog();
        orderLog.setLog(remark);
        orderLog.setOrderId(order.getOrderId());
        deliveryOrderLogManager.writeLog(orderLog);
        cancelOrderCommon(order.getId(), reason, remark, order, false);
    }


    /**
     * 机器收货订单出现异常，转RC物服
     * 按子订单分配寄售客服，不改变主订单状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoDistributionManager(String orderId, int source, Long id) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }
        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(orderId);
        if (order == null) {
            throw new SystemException(ResponseCodes.EmptyChId.getCode(),
                    ResponseCodes.EmptyChId.getMessage());
        }
        logger.info("开始分配物服的订单:{}", orderId);
        if (order.getDeliveryType() != ShDeliveryTypeEnum.Robot.getCode()) {
            throw new SystemException("不是机器收货订单，无法转人工");
        }
        //订单状态为交易中的才能分配寄售客服(物服)
        if (DeliveryOrderStatus.TRADING.getCode() != order.getStatus().intValue()) {
            throw new SystemException(ResponseCodes.ErrorOrderStatusDistributionService.getCode(),
                    ResponseCodes.ErrorOrderStatusDistributionService.getMessage());
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setCreateTime(new Date());
        //写入内部日志
        orderLog.setType(OrderLog.TYPE_INNER);
        orderLog.setLog("本订单机器自动处理异常，已开始转交给人工客服。");
        deliveryOrderLogManager.writeLog(orderLog);
        orderLog.setType(OrderLog.TYPE_NORMAL);

        //查询主订单对应的子订单
        List<DeliverySubOrder> deliverySubOrderList = null;
        if (order.getTradeLogo() == TradeLogoEnum.PostTrade.getCode()) {
            //查询主订单对应的子订单
            deliverySubOrderList = deliverySubOrderDao.queryAllByOrderIdForUpdate(order.getOrderId());
            logger.info("邮寄交易转人工,主订单{},共{}子订单", orderId, String.valueOf(deliverySubOrderList.size()));
        } else if (order.getTradeLogo() == TradeLogoEnum.AuctionTrade.getCode()) {
            DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(id);
            deliverySubOrderList = deliverySubOrderDao.queryAllByOrderForUpdate(order.getOrderId(), order.getBuyerAccount(), subOrder.getGameAccount());
            logger.info("拍卖交易转人工,主订单,共{}子订单", order.getOrderId(), String.valueOf(deliverySubOrderList.size()));
        }
        logger.info("主订单:{},共:{}子订单", orderId, String.valueOf(deliverySubOrderList.size()));
        if (deliverySubOrderList != null && deliverySubOrderList.size() > 0) {
            for (int i = 0; i < deliverySubOrderList.size(); i++) {
                //如果主订单下的某一笔子订单已经转过人工，跳过此笔子订单
                if (deliverySubOrderList.get(i).getMachineArtificialStatus() != null && deliverySubOrderList.get(i).getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                    continue;
                }
                //将寄售客服信息更新到状态为交易中的子订单中
                //Integer类型比较在-128到127之间可以直接用==，不必拆装箱
                //子订单状态是：交易中；并且转人工状态是：未转人工的，才可以转人工（寄售物服）
                if (DeliveryOrderStatus.TRADING.getCode() == deliverySubOrderList.get(i).getStatus() &&
                        (deliverySubOrderList.get(i).getMachineArtificialStatus() == null ||
                                MachineArtificialStatus.ArtificialTransferSuccess.getCode() != deliverySubOrderList.get(i).getMachineArtificialStatus())) {

                    //查询订单量最少的物服
                    List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
                    if (list == null || list.size() < 1) {
                        logger.info("当前没有物服在线，分配失败！");
                        throw new SystemException(ResponseCodes.NullConsignmentService.getCode(),
                                ResponseCodes.NullConsignmentService.getMessage());
                    }
                    UserInfoEO user = list.get(0);
                    //对寄售客服信息进行判空
                    if (user.getLoginAccount() == null || StringUtils.isBlank(user.getRealName())) {
                        throw new SystemException(ResponseCodes.ErrorConsignmentService.getCode(),
                                ResponseCodes.ErrorConsignmentService.getMessage());
                    }

                    deliverySubOrderList.get(i).setTakeOverSubjectId(user.getLoginAccount());
                    deliverySubOrderList.get(i).setTakeOverSubject(user.getRealName());
                    deliverySubOrderList.get(i).setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
                    deliverySubOrderList.get(i).setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
                    deliverySubOrderList.get(i).setMachineArtificialTime(new Date());
                    deliverySubOrderList.get(i).setWaitToConfirm(false);
                    if (deliverySubOrderList.get(i).getOtherReason() == null) {
                        deliverySubOrderList.get(i).setOtherReason("该笔订单下的子订单转人工,其他子单也一并转人工!");
                    }
                    if (source == 1) {
                        order.setIsTimeout(true);
                        deliveryOrderDao.update(order);
                        deliverySubOrderList.get(i).setIsTimeout(true);
                    }
                    deliverySubOrderDao.update(deliverySubOrderList.get(i));
                    logger.info("订单号：{},子订单成功分配物服：{}", deliverySubOrderList.get(i).getOrderId(), user.getNickName());

                    orderLog.setLog("【" + deliverySubOrderList.get(i).getId() + "号机器人】" + "【" + deliverySubOrderList.get(i).getGameRole() + "】，该笔订单超时或出现异常，订单转人工处理，请勿关闭本页面。");
                    deliveryOrderLogManager.writeLog(orderLog);
                }
            }
//            order.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
//            deliveryOrderDao.update(order);
//            logger.info("收货订单：{}因超时或出现异常已转人工物服", order.getOrderId());
        }
    }

    /**
     * 拍卖订单出现异常,自动分配物服
     *
     * @param id
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)(rollbackFor = Exception.class)
//    public void auctionDistributionManager(Long id, int source) {
//        if (id == null) {
//            throw new SystemException(ResponseCodes.EmptySubOrderId.getCode(),
//                    ResponseCodes.EmptySubOrderId.getMessage());
//        }
//        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(id);
//        if (subOrder == null) {
//            throw new SystemException(ResponseCodes.NoDeliverySubOrder.getCode(),
//                    ResponseCodes.NoDeliverySubOrder.getMessage());
//        }
//        DeliveryOrder order = deliveryOrderDao.selectByOrderIdForUpdate(subOrder.getOrderId());
//        if (order == null) {
//            throw new SystemException(ResponseCodes.EmptyChId.getCode(),
//                    ResponseCodes.EmptyChId.getMessage());
//        }
//        logger.info("开始分配物服的订单:{}", subOrder.getOrderId());
//        if (order.getDeliveryType() != ShDeliveryTypeEnum.Robot.getCode()) {
//            throw new SystemException("不是机器收货订单，无法转人工");
//        }
//        //订单状态为交易中的才能分配寄售客服(物服)
//        if (DeliveryOrderStatus.TRADING.getCode() != order.getStatus().intValue()) {
//            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
//                    ResponseCodes.StateAfterNotIn.getMessage());
//        }
//
//        OrderLog orderLog = new OrderLog();
//        orderLog.setOrderId(order.getOrderId());
//        orderLog.setCreateTime(new Date());
//        //写入内部日志
//        orderLog.setType(OrderLog.TYPE_INNER);
//        orderLog.setLog("本订单机器自动处理异常，已开始转交给人工客服。");
//        deliveryOrderLogManager.writeLog(orderLog);
//        orderLog.setType(OrderLog.TYPE_NORMAL);
//
//        //查询主订单对应的子订单
//        List<DeliverySubOrder> deliverySubOrderList = deliverySubOrderDao.queryAllByOrderForUpdate(order.getOrderId(), order.getBuyerAccount(), subOrder.getGameAccount());
//        logger.info("主订单:{},共:{}子订单", subOrder.getOrderId(), String.valueOf(deliverySubOrderList.size()));
//        if (deliverySubOrderList != null && deliverySubOrderList.size() > 0) {
//            for (int i = 0; i < deliverySubOrderList.size(); i++) {
//                //如果主订单下的某一笔子订单已经转过人工，跳过此笔子订单
//                if (deliverySubOrderList.get(i).getMachineArtificialStatus() != null && deliverySubOrderList.get(i).getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
//                    continue;
//                }
//                //将寄售客服信息更新到状态为交易中的子订单中
//                //Integer类型比较在-128到127之间可以直接用==，不必拆装箱
//                //子订单状态是：交易中；并且转人工状态是：未转人工的，才可以转人工（寄售物服）
//                if (DeliveryOrderStatus.TRADING.getCode() == deliverySubOrderList.get(i).getStatus() &&
//                        (deliverySubOrderList.get(i).getMachineArtificialStatus() == null ||
//                                MachineArtificialStatus.ArtificialTransferSuccess.getCode() != deliverySubOrderList.get(i).getMachineArtificialStatus())) {
//
//                    //查询订单量最少的物服
//                    List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
//                    if (list == null || list.size() < 1) {
//                        logger.info("当前没有物服在线，分配失败！");
//                        throw new SystemException(ResponseCodes.NullConsignmentService.getCode(),
//                                ResponseCodes.NullConsignmentService.getMessage());
//                    }
//                    UserInfoEO user = list.get(0);
//                    //对寄售客服信息进行判空
//                    if (user.getLoginAccount() == null || StringUtils.isBlank(user.getRealName())) {
//                        throw new SystemException(ResponseCodes.ErrorConsignmentService.getCode(),
//                                ResponseCodes.ErrorConsignmentService.getMessage());
//                    }
//
//                    deliverySubOrderList.get(i).setTakeOverSubjectId(user.getLoginAccount());
//                    deliverySubOrderList.get(i).setTakeOverSubject(user.getRealName());
//                    deliverySubOrderList.get(i).setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
//                    deliverySubOrderList.get(i).setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
//                    deliverySubOrderList.get(i).setMachineArtificialTime(new Date());
//                    deliverySubOrderList.get(i).setWaitToConfirm(false);
//                    if (source == 1) {
//                        order.setIsTimeout(true);
//                        deliveryOrderDao.update(order);
//                        deliverySubOrderList.get(i).setIsTimeout(true);
//                    }
//                    deliverySubOrderDao.update(deliverySubOrderList.get(i));
//                    logger.info("订单号：{},子订单成功分配物服：{}", deliverySubOrderList.get(i).getOrderId(), user.getNickName());
//
//                    orderLog.setLog("该笔订单超时或出现异常，订单转人工处理，请勿关闭本页面。");
//                    deliveryOrderLogManager.writeLog(orderLog);
//                }
//            }
//            order.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
//            deliveryOrderDao.update(order);
//            logger.info("收货订单：{}因超时或出现异常已转人工物服", order.getOrderId());
//        }
//    }

    /**
     * 接收子订单号，为子订单分配物服
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subOrderAutoDistributionManager(Long subOrderId, Integer clickFromUser, String otherReason) {
        DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectByIdForUpdate(subOrderId);
        if (deliverySubOrder.getDeliveryType() != ShDeliveryTypeEnum.Robot.getCode()) {
            throw new SystemException("不是机器收货订单，无法转人工");
        }
        if (deliverySubOrder.getMachineArtificialStatus() != null && deliverySubOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
            throw new SystemException("该订单已移交至人工物服，请勿重复操作");
        }
        if (deliverySubOrder.getStatus() == null || deliverySubOrder.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
            throw new SystemException("不是交易中的订单无法转人工");
        }
        //Integer类型比较在-128到127之间可以直接用==
        if (DeliveryOrderStatus.TRADING.getCode() == deliverySubOrder.getStatus()) {
            //查询订单量最少的物服
            List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
            if (list == null || list.size() < 1) {
                logger.info("当前没有物服在线，分配失败！");
                throw new SystemException(ResponseCodes.NullConsignmentService.getCode(),
                        ResponseCodes.NullConsignmentService.getMessage());
            }
            UserInfoEO user = list.get(0);
            //对寄售客服信息进行判空
            if (user.getLoginAccount() == null || StringUtils.isBlank(user.getRealName())) {
                throw new SystemException(ResponseCodes.ErrorConsignmentService.getCode(),
                        ResponseCodes.ErrorConsignmentService.getMessage());
            }
            //为超时转人工的子订单增加转人工原因
            if (StringUtils.isNotBlank(otherReason)) {
                deliverySubOrder.setOtherReason(otherReason);
            }
            deliverySubOrder.setTakeOverSubjectId(user.getLoginAccount());
            deliverySubOrder.setTakeOverSubject(user.getRealName());
            deliverySubOrder.setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
            deliverySubOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
            deliverySubOrder.setMachineArtificialTime(new Date());
            deliverySubOrder.setWaitToConfirm(false);
            deliverySubOrderDao.update(deliverySubOrder);
            logger.info("订单号：{},子订单成功分配物服：{}", deliverySubOrder.getOrderId(), user.getNickName());

            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(deliverySubOrder.getOrderId());
            orderLog.setCreateTime(new Date());
            //写入内部日志
            orderLog.setType(OrderLog.TYPE_INNER);
            orderLog.setLog("本订单处理异常，已开始转交给人工客服。");
            deliveryOrderLogManager.writeLog(orderLog);
            orderLog.setType(OrderLog.TYPE_NORMAL);
            if (clickFromUser == 1) {
                orderLog.setLog("【" + deliverySubOrder.getId() + "号机器人】" + "【" + deliverySubOrder.getSellerRoleName() + "】，您已点击【转人工】按钮。");
                deliveryOrderLogManager.writeLog(orderLog);
            }
            orderLog.setLog("【" + deliverySubOrder.getId() + "号机器人】" + "【" + deliverySubOrder.getSellerRoleName() + "】，该笔订单超时或出现异常，订单转人工处理，请勿关闭本页面。");
            deliveryOrderLogManager.writeLog(orderLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDeliveryOrder(DeliveryOrder deliveryOrder, long totalReceivedAmount, int statusForChange, StringBuffer sb, List<DeliverySubOrder> deliverySubOrders) throws IOException {
        deliveryOrder.setStatus(statusForChange);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(deliveryOrder.getOrderId());
        orderLog.setLog(sb.toString());
        //邮寄交易给日志记录 其他自己log记
        if (TradeLogoEnum.PostTrade.getCode() == deliveryOrder.getTradeLogo()) {
            orderLog.setType(OrderLog.TYPE_NORMAL);
            deliveryOrderLogManager.writeLog(orderLog);
        } else {
            logger.info(sb.toString());
        }
        //插入两条日志记录
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("系统信息：订单数量").append(deliveryOrder.getCount());
        stringBuffer.append("万金，已收货数量").append(totalReceivedAmount).append("万金，未交易数量");
        stringBuffer.append(deliveryOrder.getCount() - totalReceivedAmount).append("万金");
        orderLog = new OrderLog();
        orderLog.setOrderId(deliveryOrder.getOrderId());
        orderLog.setLog(stringBuffer.toString());
        if (TradeLogoEnum.PostTrade.getCode() == deliveryOrder.getTradeLogo()) {
            orderLog.setType(OrderLog.TYPE_NORMAL);
            deliveryOrderLogManager.writeLog(orderLog);
        } else {
            logger.info(stringBuffer.toString());
        }
        deliveryOrder.setRealAmount(deliveryOrder.getPrice().multiply(new BigDecimal(deliveryOrder.getRealCount())).setScale(2, RoundingMode.DOWN));
        logger.info("订单" + deliveryOrder.getOrderId() + "以" + deliveryOrder.getRealCount() + "万金" + deliveryOrder.getRealAmount() + "元完单");
        deliveryOrder.setTradeEndTime(new Date());
        deliveryOrderDao.update(deliveryOrder);

        //一笔申诉主订单 只有一笔申诉子订单  所以主订单appealorder（对应申诉单的id）不为空就说明是申诉单了  主订单及子订单已经改变  就把对应的被申诉单的申诉状态改成成功
        //只有邮寄会有申诉单
        if (StringUtils.isNotBlank(deliveryOrder.getAppealOrder())) {
            //更新的是其他的单
            deliverySubOrderDao.updateDeliverySubOrderAppealOrderStatusById(Long.parseLong(deliveryOrder.getAppealOrder()), AppealOrderStatus.COMPLETE.getCode());
        }

        //改变状态要推送订单中心
        asyncPushToMainMehtods.orderPushToMain(deliveryOrder, OrderCenterOrderStatus.SUCCESS_TRADE.getCode());
        if (statusForChange == DeliveryOrderStatus.COMPLETE.getCode()
                || statusForChange == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
            //资金结算
            settlement(deliveryOrder.getOrderId());
        } else if (statusForChange == DeliveryOrderStatus.CANCEL.getCode()) {
            //交易取消解冻资金
            if (deliveryOrder.getOrderId().contains(OrderPrefix.NEWORDERID.getName())) {
                fundManager.releaseFreezeFundZBao(IFundManager.FREEZE_BY_DELIVERY_ORDER, deliveryOrder, deliveryOrder.getAmount());
            } else {
                fundManager.releaseFreezeFund(IFundManager.FREEZE_BY_DELIVERY_ORDER, deliveryOrder.getBuyerAccount(), deliveryOrder.getOrderId(), deliveryOrder.getAmount());
            }
        }
    }


    /**
     * 调用此方法前 请参阅interface处对此方法的使用注解
     * 由此方法订单均进入最终状态 调用此方法之前需确保超过阈值+填写金额已转为人工处理了
     * 申诉单亦执行此方法  申诉单在创建时不扣除收货数量  故只做资金加减处理 故不作PurchaseOrder与GameAccount的数量操作
     * 2018.4.3 本方法可由机器收货的拍卖调用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderForMailDeliveryOrderMax(Map<Long, Long> subOrdersInfos, String mainOrderId, Map<Long, String> appealReason, Map<Long, Integer> reason, Map<Long, String> remark) throws IOException {
        List<DeliverySubOrder> deliverySubOrders = new ArrayList<DeliverySubOrder>(8);
        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderIdForUpdate(mainOrderId);
        if (subOrdersInfos == null || subOrdersInfos.size() <= 0) {
            throw new SystemException(ResponseCodes.EmptyReceivingCount.getCode(), ResponseCodes.EmptyReceivingCount.getMessage());
        }
        Long totalSubRealCount = setFinalValueOfOrders(subOrdersInfos, mainOrderId, appealReason, reason, remark, deliverySubOrders, deliveryOrder);
        //更新主订单的实际收货数量 具体资金realAmount的计算在最后改变主订单状态时再更新 防止因向下取整规则造成多次计算取两位小数产生误差
        if (deliveryOrder.getRealCount() == null) {
            deliveryOrder.setRealCount(0L);
        }
        deliveryOrder.setRealCount(deliveryOrder.getRealCount() + totalSubRealCount);
        deliveryOrderDao.update(deliveryOrder);
        logger.info("此次处理总订单,更新实际收货数量为:" + deliveryOrder.getRealCount());
        deliverySubOrderDao.batchUpdate(deliverySubOrders);
        /**
         *  只有最终状态的子订单才可以进行 角色释放以及更新账号角色信息  及采购单信息  注意 必须等所有子订单状态更改完之后才能进行这一步 否则如果是批量数据会出现不释放的问题
         */
        handleGameAccountAndParOrder(subOrdersInfos);
        /***
         * 更新主订单状态 以及日志
         */
        updateMainOrderAndLog(mainOrderId, deliveryOrder);
    }

    private void updateMainOrderAndLog(String mainOrderId, DeliveryOrder deliveryOrder) throws IOException {
        //同一主订单下所有子订单都必须是最终状态再改变主订单状态 主订单需要验证收货数量以决定改变为何种状态 且只有主订单改变状态之后再推送MQ
        List<DeliverySubOrder> deliverySubOrdersForUpdate = deliverySubOrderDao.queryAllByOrderId(mainOrderId);
        int countForDetailFinalState = 0;
        long totalReceivedAmountForMainOrder = 0;
        int completeCount = 0;
        int completePartCount = 0;
        int cancel = 0;
        StringBuffer completeName = new StringBuffer();
        StringBuffer completePatName = new StringBuffer();
        StringBuffer cancleName = new StringBuffer();
        for (DeliverySubOrder deliverySubOrder1 : deliverySubOrdersForUpdate) {
            if (deliverySubOrder1.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()
                    || deliverySubOrder1.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()
                    || deliverySubOrder1.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                countForDetailFinalState++;
                //这里不用担心超量  因为在子订单的处理中 已经将超收状态的子订单收货数量设置为了预计收货数量
                totalReceivedAmountForMainOrder += deliverySubOrder1.getRealCount();
            }
            if (deliverySubOrder1.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()) {
                completeName.append("【").append(deliverySubOrder1.getGameRole()).append("】");
                completeCount++;
            } else if (deliverySubOrder1.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
                completePatName.append("【").append(deliverySubOrder1.getGameRole()).append("】");
                completePartCount++;
            } else if (deliverySubOrder1.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                cancleName.append("【").append(deliverySubOrder1.getGameRole()).append("】");
                cancel++;
            }
        }
        logger.info("RealReceivedCountOfMainOrderIs:" + totalReceivedAmountForMainOrder);
        if (StringUtils.isNotBlank(completeName.toString())) {
            completeName.append(completeCount).append("个子订单交易成功,");
        }
        if (StringUtils.isNotBlank(completePatName.toString())) {
            completePatName.append(completePartCount).append("个子订单部分完单,");
        }
        if (StringUtils.isNotBlank(cancleName.toString())) {
            cancleName.append(cancel).append("个子订单撤单,");
        }

        //只有当主订单下所有子订单都是最终状态之后 再通过计算的总收货金额改变主订单状态,因为有最终状态的判断条件
        if (deliverySubOrdersForUpdate.size() == countForDetailFinalState) {
            //但凡总实际收货数量小于预计收货数量都属于部分完单
            if (totalReceivedAmountForMainOrder < deliveryOrder.getCount() && totalReceivedAmountForMainOrder != 0) {
                completeName.append(completePatName).append(cancleName).append("当前主订单部分完单。");
                updateDeliveryOrder(deliveryOrder, totalReceivedAmountForMainOrder, DeliveryOrderStatus.COMPLETE_PART.getCode(), completeName, deliverySubOrdersForUpdate);
            }
            //子订单收货数量其实经过处理只可能等于主订单预计收货数量了  不存在大于
            if (totalReceivedAmountForMainOrder == deliveryOrder.getCount()) {
                completeName.append(completePatName).append(cancleName).append("当前主订单完单。");
                updateDeliveryOrder(deliveryOrder, totalReceivedAmountForMainOrder, DeliveryOrderStatus.COMPLETE.getCode(), completeName, deliverySubOrdersForUpdate);
            }
            if (totalReceivedAmountForMainOrder > deliveryOrder.getCount()) {
                throw new SystemException(ResponseCodes.TotalSubsCountNotEqualMainOrderCounts.getCode(), ResponseCodes.TotalSubsCountNotEqualMainOrderCounts.getMessage());
            }
            if (totalReceivedAmountForMainOrder == 0) {
                completeName.append(completePatName).append(cancleName).append("当前主订单撤单。");
                updateDeliveryOrder(deliveryOrder, totalReceivedAmountForMainOrder, DeliveryOrderStatus.CANCEL.getCode(), completeName, deliverySubOrdersForUpdate);
            }
        }
    }

    private void handleGameAccountAndParOrder(Map<Long, Long> subOrdersInfos) {
        for (Map.Entry<Long, Long> entry : subOrdersInfos.entrySet()) {
            DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectById(entry.getKey());
            if (deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode() ||
                    deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode() ||
                    deliverySubOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                Map<String, Object> queryMapForGameAccount = new HashMap<String, Object>(8);
                //更新收货角色信息,确保释放角色是正确 需要买家账号 游戏 区服 登录账号  防止不同区 不同服 有重名
                /**
                 * @see RepositoryCountAspect
                 * 如修改queryMapForGameAccount键 请同步修改RepositoryCountAspect
                 */
                queryMapForGameAccount.put("buyerAccount", deliverySubOrder.getBuyerAccount());
                queryMapForGameAccount.put("gameName", deliverySubOrder.getGameName());
                queryMapForGameAccount.put("region", deliverySubOrder.getRegion());
                queryMapForGameAccount.put("server", deliverySubOrder.getServer());
                queryMapForGameAccount.put("gameAccount", deliverySubOrder.getGameAccount());
                queryMapForGameAccount.put("roleName", deliverySubOrder.getGameRole());
                queryMapForGameAccount.put("gameRace", deliverySubOrder.getGameRace());
                if (deliverySubOrder.getTradeLogo() != null && TradeLogoEnum.AuctionTrade.getCode() == deliverySubOrder.getTradeLogo()) {
                    queryMapForGameAccount.put("notInStatus", "4,5,6");
                    queryMapForGameAccount.put("gameRole", deliverySubOrder.getGameRole());
                    int notCompleteCount = deliverySubOrderDao.countByMap(queryMapForGameAccount);
                    //拍卖交易的特殊性  需要当前角色所有子订单都是最终状态才释放角色 即不是完单、部分完单、撤单的数量都为0才可以释放角色
                    if (notCompleteCount == 0) {
                        queryMapForGameAccount.put("status", GameAccount.S_FREE);
                    }
                } else {
                    //非拍卖交易的 可以直接释放
                    queryMapForGameAccount.put("status", GameAccount.S_FREE);
                }
                //原本收货数量加上 预计收货数量与实际收货数量之差
                // 最终状态的单子 所以不存在超过阈值一说 只有超过预计收货额度
                if (deliverySubOrder.getCount() <= entry.getValue()) {
                    entry.setValue(deliverySubOrder.getCount());
                }
                if (StringUtils.isBlank(deliverySubOrder.getAppealOrder())) {
                    queryMapForGameAccount.put("addCount", deliverySubOrder.getCount() - entry.getValue());
                }
                //增加库存数量
                queryMapForGameAccount.put("addRepositoryCount", entry.getValue());
                gameAccountManager.updateStatus(queryMapForGameAccount);

                if (entry.getValue() > 0) {
                    //添加收货日志
                    SplitRepositoryLog splitRepositoryLog = new SplitRepositoryLog();
                    splitRepositoryLog.setBuyerAccount(deliverySubOrder.getBuyerAccount());
                    splitRepositoryLog.setGameName(deliverySubOrder.getGameName());
                    splitRepositoryLog.setRegion(deliverySubOrder.getRegion());
                    splitRepositoryLog.setServer(deliverySubOrder.getServer());
                    splitRepositoryLog.setGameRace(deliverySubOrder.getGameRace());
                    splitRepositoryLog.setGameAccount(deliverySubOrder.getGameAccount());
                    splitRepositoryLog.setRoleName(deliverySubOrder.getGameRole());
                    //TODO jyx 此处是收货添加
                    splitRepositoryLog.setLogType(LogTypeEnum.DELIVERYSALED.getCode());
                    splitRepositoryLog.setCreateTime(new Date());
                    splitRepositoryLog.setIncomeType(IncomeType.INCOME.getCode());
                    splitRepositoryLog.setCount(entry.getValue());
                    splitRepositoryLog.setFcOrderId(deliverySubOrder.getOrderId());
                    splitRepositoryLogManager.saveLog(splitRepositoryLog);
                }
//                //保存角色日志
//                if (entry.getValue() > 0) {
//                    StringBuffer sbData = new StringBuffer();
//                    sbData.append("收货增加库存，收货单号：").append(deliverySubOrder.getOrderId()).append("_").append(deliverySubOrder.getId()).append("，增加库存：").append(entry.getValue()).append(deliverySubOrder.getMoneyName());
//                    splitRepositoryLogManager.saveLog(deliverySubOrder, sbData.toString());
//                }

                //非申诉单才扣除purchaseOrder与gameAccount 申诉单会与旧订单关联 appealOrders为空才是普通订单 故非申诉单不加
                if (StringUtils.isBlank(deliverySubOrder.getAppealOrder())) {
//                PurchaseOrder purchaseOrder = purchaseOrderManager.selectByIdForUpdate(deliveryOrder.getCgId());
//                    实际收货数量不会大于预计收货数量  就算大于预计收货数量 能进本方法体的也是在阈值范围内的 就算大于预计收货数量也被set成了预计收货数量 或是撤单 为0
//                     角色上收货信息在下单时就扣除掉了 这只是加回差额部分 子订单预计收货数量减去实际收货数量
//                    purchaseOrder.setCount(purchaseOrder.getCount() + (deliverySubOrder.getCount() - entry.getValue()));
//                    purchaseOrderDBDAO.update(purchaseOrder);
                    Map<String, Object> purchaseMap = new HashMap<String, Object>(4);
                    purchaseMap.put("buyerAccount", deliverySubOrder.getBuyerAccount());
                    purchaseMap.put("gameName", deliverySubOrder.getGameName());
                    purchaseMap.put("region", deliverySubOrder.getRegion());
                    purchaseMap.put("server", deliverySubOrder.getServer());
                    purchaseOrderDBDAO.updatePurchaseOrderCount(purchaseMap);
                }
            }
        }
    }

    private Long setFinalValueOfOrders(Map<Long, Long> subOrdersInfos, String mainOrderId, Map<Long, String> appealReason, Map<Long, Integer> reason, Map<Long, String> remark, List<DeliverySubOrder> deliverySubOrders, DeliveryOrder deliveryOrder) {
        Long totalSubRealCount = 0L;
        for (Map.Entry<Long, Long> entry : subOrdersInfos.entrySet()) {
            if (logger.isInfoEnabled()) {
                logger.info("处理子订单开始,子订单id号:" + entry.getKey() + ",实际收货数量:" + entry.getValue());
            }
            /**
             * 更新子订单状态  可能会 完单  部分完单  撤单
             */
            DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectById(entry.getKey());
            if (deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode() ||
                    deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode() ||
                    deliverySubOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
            }
            if (deliverySubOrder == null) {
                throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
            }
            if (!deliverySubOrder.getOrderId().equals(mainOrderId)) {
                throw new SystemException(ResponseCodes.NotMatchError.getCode(), ResponseCodes.NotMatchError.getMessage());
            }
            //大于等于预计收货数量  就按照预计收货数量完单
            if (entry.getValue() >= deliverySubOrder.getCount() && entry.getValue() != 0L) {
                logger.info("订单:" + deliverySubOrder.getId() + "实际收货：" + entry.getValue() + "以" + deliverySubOrder.getCount() + "完单");
                entry.setValue(deliverySubOrder.getCount());
                deliverySubOrder.setRealCount(entry.getValue());
                deliverySubOrder.setStatus(DeliveryOrderStatus.COMPLETE.getCode());
            }
            //如果在阈值范围内 但是小于预计收货数量 且不为0 就算部分完单
            if (entry.getValue() < deliverySubOrder.getCount() && entry.getValue() != 0L) {
                logger.info("订单:" + deliverySubOrder.getId() + "实际收货：" + entry.getValue() + "以" + entry.getValue() + "完单");
                deliverySubOrder.setRealCount(entry.getValue());
                deliverySubOrder.setStatus(DeliveryOrderStatus.COMPLETE_PART.getCode());
            }
            if (appealReason != null) {
                for (Map.Entry<Long, String> entryReason : appealReason.entrySet()) {
                    if (entryReason.getKey().compareTo(entry.getKey()) == 0 && null != entryReason.getValue()) {
                        deliverySubOrder.setOtherReason(entryReason.getValue());
                    }
                }
            }
            if (reason != null) {
                for (Map.Entry<Long, Integer> entryReasonSimple : reason.entrySet()) {
                    if (entryReasonSimple.getKey().compareTo(entry.getKey()) == 0 && null != entryReasonSimple.getValue()) {
                        deliverySubOrder.setReason(entryReasonSimple.getValue());
                    }
                }
            }
            if (remark != null) {
                for (Map.Entry<Long, String> remarkEntry : remark.entrySet()) {
                    if (remarkEntry.getKey().compareTo(entry.getKey()) == 0 && null != remarkEntry.getValue()) {
                        deliverySubOrder.setRemarks(remarkEntry.getValue());
                    }
                }
            }
            //如果实际收货数量为0作撤单处理
            if (entry.getValue() == 0L) {
                logger.info("订单:" + deliverySubOrder.getId() + "实际收货：" + entry.getValue() + "以" + entry.getValue() + "完单");
                deliverySubOrder.setRealCount(entry.getValue());
                deliverySubOrder.setStatus(DeliveryOrderStatus.CANCEL.getCode());
            }
            if (deliveryOrder.getDeliveryType() == DeliveryTypeEnum.Robot.getCode() && TradeLogoEnum.PostTrade.getCode() == deliveryOrder.getTradeLogo()) {
                deliverySubOrder.setWaitToConfirm(false);
            }
            deliverySubOrder.setUpdateTime(new Date());
            deliverySubOrders.add(deliverySubOrder);
            /**
             * 子订单每完单一笔 记一笔记录
             */
            StringBuffer sb = new StringBuffer();
            if (deliverySubOrder.getMachineArtificialStatus() != null && deliverySubOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                if (deliverySubOrder.getTakeOverSubjectId() == null) {
                    sb.append("客服:" + deliveryOrder.getServiceNickname() + "信息:");
                } else {
                    sb.append("物服:" + deliverySubOrder.getTakeOverSubjectId() + "信息:");
                }
            }
            sb.append("订单id:" + deliverySubOrder.getId() + ",订单数量:" + deliverySubOrder.getCount() + "万金,已经确认收货数量:" +
                    entry.getValue() + "万金,未交易数量:" + (deliverySubOrder.getCount() - entry.getValue()) + "万金");
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(mainOrderId);
            orderLog.setLog(sb.toString());
            orderLog.setType(OrderLog.TYPE_INNER);
            orderLog.setCreateTime(new Date());
            orderLog.setSubId(entry.getKey());
            deliveryOrderLogManager.writeLog(orderLog);
            totalSubRealCount += entry.getValue();
        }
        logger.info("本次处理子订单总计收货数量;" + totalSubRealCount);
        return totalSubRealCount;
    }

    /**
     * 申诉单完单接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appealFinishDeliveryOrder(String orderId, Long realCount, String otherReason) {

        List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.queryAllByOrderId(orderId);
        DeliverySubOrder deliverySubOrder = deliverySubOrders.get(0);
        if (deliverySubOrder.getStatus() != DeliveryOrderStatus.TRADING.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), ResponseCodes.StateAfterNotIn.getMessage());
        }
        Map<Long, Long> map = new HashMap<Long, Long>();
        map.put(deliverySubOrder.getId(), realCount);
        //1.订单更新主订单，更新子订单 收货数量 客服填写原因
        Map<Long, String> appealReason = new HashMap<Long, String>();
        appealReason.put(deliverySubOrder.getId(), otherReason);
        try {
            this.handleOrderForMailDeliveryOrderMax(map, orderId, appealReason, null, null);
        } catch (IOException e) {
            logger.info("申诉单完单方法报错handleOrderForMailDeliveryOrderMax：{}", e);
        }
    }

    /**
     * 根据订单号查询订单(不锁表)
     */
    @Override
    public DeliveryOrder selectByOrderId(String orderId) {
        return deliveryOrderDao.selectByOrderId(orderId);
    }

    /**
     * 根据申诉单号查询订单详情
     */
    @Override
    public DeliveryOrder selectByAppealOrder(String appealOrder) {
        return deliveryOrderDao.selectByAppealOrder(appealOrder);
    }
}

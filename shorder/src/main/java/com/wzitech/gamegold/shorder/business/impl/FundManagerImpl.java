package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.entity.BaseResponse;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.DirectPartialTransferResponse;
import com.wzitech.gamegold.common.paymgmt.dto.RefundResponse;
import com.wzitech.gamegold.common.utils.EncryptHelper;
/*
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;*/
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.dto.UpdateFundDTO;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.OrderPrefix;
import com.wzitech.gamegold.shorder.utils.HttpToConn;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货商资金管理
 *
 * @author yemq
 */
@Component
public class FundManagerImpl implements IFundManager {
    protected static final Logger logger = LoggerFactory.getLogger(FundManagerImpl.class);

    @Autowired
    protected IPayOrderManager payOrderManager;
    @Autowired
    protected IPayDetailManager payDetailManager;
    @Autowired
    protected IFundDetailManager fundDetailManager;
    @Autowired
    protected IPayManager payManager;
    @Autowired
    protected IPayDetailDao payDetailDao;
    @Autowired
    protected IPurchaserDataDao purchaserDataDao;
    @Autowired
    protected IPayOrderDao payOrderDao;
    @Autowired
    private IAutoPayShManager autoPayShManager;
    @Autowired
    private IAmoutHttp amoutHttp;
    @Autowired
    private IPurchaserDataManager purchaserDataManager;

    @Value("${7bao.freeze.url}")
    private String freezeUrl;

    @Value("${7Bao.fund.serKey}")
    private String freezeSerKey;

    /**
     * 处理充值成功通知
     *
     * @param orderId 订单号
     * @param amount  支付金额
     */
    @Override
    @Transactional
    public void processRechargeSuccessNotify(String orderId, BigDecimal amount) {
        PayOrder order = payOrderManager.queryByOrderId(orderId, true);
        if (order == null)
            throw new SystemException(ResponseCodes.NotExistPayOrder.getCode(),
                    ResponseCodes.NotExistPayOrder.getMessage());

        if (order.getStatus().intValue() != PayOrder.WAIT_PAYMENT)
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(),
                    ResponseCodes.OrderStatusHasChangedError.getMessage());

        // 将支付单修改成已支付
        order.setStatus(PayOrder.PAID);
        order.setAmount(amount);
        order.setUsedAmount(BigDecimal.ZERO.setScale(2));
        order.setBalance(amount);
        order.setPayTime(new Date());
        order.setLastUpdateTime(new Date());
        payOrderDao.update(order);

        // 充值成功，增加采购商资金
        this.recharge(order.getAccount(), order.getOrderId(), amount);
    }

    /**
     * 处理充值成功通知 7bao充值接口
     *
     * @param orderId 订单号
     * @param amount  支付金额
     */
  /*  @Override
    @Transactional
    public void processRechargeSuccess7Bao(String orderId, BigDecimal amount) {
       OrderInfoEO orderInfoEO =  orderInfoManager.selectById(orderId);
        // 充值成功，增加采购商资金
        this.recharge7Bao(orderInfoEO.getUserAccount(), amount);
    }*/

    /**
     * 充值单补单
     *
     * @param autoDeleteTime
     */
    @Override
    public void autoConfirmationPayTimeoutOrder(Integer autoDeleteTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("status", PayOrder.WAIT_PAYMENT);
        Date now = new Date();
        Date deleteTime = DateUtils.addSeconds(now, -autoDeleteTime);
        queryMap.put("endCreateTime", deleteTime);
        List<PayOrder> deleteList = payOrderDao.selectByMap(queryMap, "ID", false);
        for (PayOrder order : deleteList) {
            manualPayShOrder(order.getOrderId());
        }
    }

    /**
     * 充值单补单
     *
     * @param orderId
     */
    @Override
    public void manualPayShOrder(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new SystemException("orderId不能为空");
        }
        PayOrder order = payOrderDao.selectByOrderId(orderId, false);
        if (null != order && order.getStatus().intValue() == PayOrder.WAIT_PAYMENT) {
            // 查询资金明细，确认已支付
            Boolean result = autoPayShManager.queryPaymentDetail(order.getOrderId(), order.getUid());
            if (result == null) {
                //未返回标准结果，不做操作
                logger.info("充值单补单，查询资金明细，查询无结果，返回false");
            } else if (result) {
                //已支付，修改订单状态
                logger.info("充值单补单，查询资金明细，查询已付款，修改订单状态，返回true");
                BigDecimal amount = order.getAmount();
                amount = amount.setScale(2, RoundingMode.DOWN);
                this.processRechargeSuccessNotify(order.getOrderId(), amount);
            } else if (!result) {
                //未支付，修改订单状态
                logger.info("充值单补单，查询资金明细，查询未支付，返回true");
            }
        }
    }

    /**
     * 采购商充值成功，增加总资金
     *
     * @param account    采购商账号
     * @param payOrderId 充值单号
     * @param amount     增加的金额
     * @return
     */
    @Transactional
    protected PurchaserData recharge(String account, String payOrderId, BigDecimal amount) {
        logger.info("支付同步回调，增加总资金开始，payOrderId," + payOrderId);
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        logger.info("支付同步回调，增加总资金开始，purchaserData," + purchaserData);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmount();
        totalAmount = totalAmount.add(amount).setScale(2, RoundingMode.DOWN);
        purchaserData.setTotalAmount(totalAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(purchaserData.getFreezeAmount());
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        purchaserDataDao.update(purchaserData);


        logger.info("支付同步回调，写入资金明细，purchaserData," + purchaserData);
        // 写入资金明细
        String log = String.format("【充值】充值订单号:%s,充值金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                payOrderId, amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                purchaserData.getAvailableAmount());

        //判断资金明细中是否已存在该充值明细
        boolean flag = fundDetailManager.isExistFundDetail(account, payOrderId, FundType.RECHARGE.getCode());
        if (flag) {
            throw new SystemException(ResponseCodes.ExistFundDetail.getCode(), ResponseCodes.ExistFundDetail.getMessage());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.RECHARGE.getCode());
        fundDetail.setPayOrderId(payOrderId);
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }


    /**
     * 冻结采购商资金
     *
     * @param freezeType 资金冻结类型
     * @param account    采购商账号
     * @param orderId    订单号
     * @param amount     冻结金额
     * @return
     */
    @Override
    @Transactional
    public PurchaserData freezeFund(int freezeType, String account, String orderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.add(amount).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = "";
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            log = String.format("【冻结资金】出货订单号:%s,冻结金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                    purchaserData.getAvailableAmount());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            log = String.format("【冻结资金】退款订单号:%s,冻结金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                    purchaserData.getAvailableAmount());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.FREEZE.getCode());
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            fundDetail.setDeliveryOrderId(orderId);
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            fundDetail.setRefundOrderId(orderId);
        }
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 冻结采购商资金 新资金；
     */
    @Override
    @Transactional
    public PurchaserData freezeFundZBao(int freezeType, String account, String uid, String orderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmountZBao();
        freezeAmount = freezeAmount.add(amount).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmountZBao(freezeAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmountZBao().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmountZBao(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        purchaserDataDao.update(purchaserData);
        //通知7bao冻结资金
        Integer yesOrNo = 1;
        this.freezeAmountZBao(account, uid, amount, yesOrNo, freezeUrl, orderId);

        // 写入资金明细
        String log = "";
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            log = String.format("【冻结资金】出货订单号:%s,冻结金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            log = String.format("【冻结资金】退款订单号:%s,冻结金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.FREEZE.getCode());
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            fundDetail.setDeliveryOrderId(orderId);
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            fundDetail.setRefundOrderId(orderId);
        }
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 解冻采购商资金
     *
     * @param freezeType 资金解冻类型
     * @param account    采购商账号
     * @param orderId    订单号
     * @param amount     解冻金额
     * @return
     */
    @Override
    @Transactional
    public PurchaserData releaseFreezeFund(int freezeType, String account, String orderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.subtract(amount).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        purchaserDataDao.update(purchaserData);


        // 写入资金明细
        String log = "";
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            log = String.format("【解冻资金】出货订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                    purchaserData.getAvailableAmount());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            log = String.format("【解冻资金】退款订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    orderId, amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                    purchaserData.getAvailableAmount());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            fundDetail.setDeliveryOrderId(orderId);
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            fundDetail.setRefundOrderId(orderId);
        }
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 新资金：解冻资金
     */
    @Override
    @Transactional
    public PurchaserData releaseFreezeFundZBao(int freezeType, DeliveryOrder deliveryOrder, BigDecimal balance) {
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(deliveryOrder.getBuyerAccount());
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmountZBao();
        logger.info("订单号：" + deliveryOrder.getOrderId() + "冻结资金为：" + freezeAmount);
        freezeAmount = freezeAmount.subtract(balance).setScale(2, RoundingMode.DOWN);
        logger.info("订单号：" + deliveryOrder.getOrderId() + "扣除订单资金后" + balance + "冻结资金为：" + freezeAmount);
        purchaserData.setFreezeAmountZBao(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmountZBao().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmountZBao(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = "";
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            log = String.format("【解冻资金】出货订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    deliveryOrder.getOrderId(), deliveryOrder.getAmount(), purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            log = String.format("【解冻资金】退款订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    deliveryOrder.getOrderId(), deliveryOrder.getAmount(), purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(deliveryOrder.getBuyerAccount());
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            fundDetail.setDeliveryOrderId(deliveryOrder.getOrderId());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            fundDetail.setRefundOrderId(deliveryOrder.getOrderId());
        }
        fundDetail.setAmount(balance);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        Integer yesOrNo = 2;
//        try {
        //通知7bao解冻资金
        this.freezeAmountZBao(deliveryOrder.getBuyerAccount(), deliveryOrder.getBuyerUid(), balance, yesOrNo, freezeUrl, deliveryOrder.getOrderId());
        logger.info("通知7bao解冻资金成功{}：", deliveryOrder.getOrderId());
//        } catch (Exception e) {
//            logger.error("通知7bao解冻资金失败{}：", deliveryOrder.getOrderId());
//            throw new SystemException(ResponseCodes.FailToUpdateZBaoFund.getCode(), ResponseCodes.FailToUpdateZBaoFund.getMessage());
//        }
        return purchaserData;
    }

    /**
     * 新资金：解冻资金
     */
    @Override
    @Transactional
    public PurchaserData releaseFreezeFund(int freezeType, DeliveryOrder deliveryOrder, BigDecimal balance) {
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(deliveryOrder.getBuyerAccount());
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmountZBao();
        freezeAmount = freezeAmount.subtract(balance).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmountZBao(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmountZBao().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmountZBao(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = "";
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            log = String.format("【解冻资金】出货订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    deliveryOrder.getOrderId(), deliveryOrder.getAmount(), purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            log = String.format("【解冻资金】退款订单号:%s,解冻金额:%s,当前总金额:%s,冻结总金额:%s,可用金额:%s",
                    deliveryOrder.getOrderId(), deliveryOrder.getAmount(), purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());
        }

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(deliveryOrder.getBuyerAccount());
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        if (freezeType == FREEZE_BY_DELIVERY_ORDER) {
            fundDetail.setDeliveryOrderId(deliveryOrder.getOrderId());
        } else if (freezeType == FREEZE_BY_REFUND_ORDER) {
            fundDetail.setRefundOrderId(deliveryOrder.getOrderId());
        }
        fundDetail.setAmount(balance);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 付款，扣除采购商资金
     *
     * @param account
     * @param payDetail
     * @return
     */
    @Transactional
    protected PurchaserData payment(String account, PayDetail payDetail) {
        logger.info("payment开始,account:{},orderId:{},payOrderId:{},chOrderId:{},amount:{}",
                new Object[]{account, payDetail.getOrderId(), payDetail.getPayOrderId(), payDetail.getChOrderId(), payDetail.getAmount()});
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmount();
        totalAmount = totalAmount.subtract(payDetail.getAmount()).setScale(2, RoundingMode.DOWN);
        purchaserData.setTotalAmount(totalAmount);

        if (BigDecimal.ZERO.compareTo(totalAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        logger.info("冻结金额扣除前,loginAccount:{},freezeAmount:{}", new Object[]{purchaserData.getLoginAccount(), freezeAmount});
        freezeAmount = freezeAmount.subtract(payDetail.getAmount()).setScale(2, RoundingMode.DOWN);
        logger.info("冻结金额扣除后,loginAccount:{},freezeAmount:{}", new Object[]{purchaserData.getLoginAccount(), freezeAmount});
        purchaserData.setFreezeAmount(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 更新采购商数据
        purchaserDataDao.update(purchaserData);

        // -------------------更新支付单信息------------------------
        PayOrder payOrder = payOrderDao.selectByOrderId(payDetail.getPayOrderId(), true);
        // 支付单已用金额
        BigDecimal usedAmount = payOrder.getUsedAmount();
        usedAmount = usedAmount.add(payDetail.getAmount()).setScale(2, RoundingMode.DOWN);
        payOrder.setUsedAmount(usedAmount);

        // 支付单余额
        BigDecimal balance = payOrder.getAmount().subtract(usedAmount);
        balance = balance.setScale(2, RoundingMode.DOWN);
        payOrder.setBalance(balance);

        if (BigDecimal.ZERO.compareTo(balance) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }


        payOrder.setLastUpdateTime(new Date());
        // 更新支付单数据
        payOrderDao.update(payOrder);

        StringBuffer s = new StringBuffer();
        s.append("【付款】出货订单号：").append(payDetail.getChOrderId()).append("，扣除金额：").append(payDetail.getAmount());
        s.append("，当前总金额：").append(purchaserData.getTotalAmount());
        s.append("，冻结金额：").append(purchaserData.getFreezeAmount());
        s.append("，可用金额：").append(purchaserData.getAvailableAmount());
        s.append("，充值订单号：").append(payDetail.getPayOrderId());
        s.append("，扣除金额：").append(payDetail.getAmount());
        s.append("，已用金额：").append(payOrder.getUsedAmount());
        s.append("，可用金额：").append(payOrder.getBalance());
        s.append("\r\n");
        s.append(",付款明细订单号：").append(payDetail.getOrderId());

        // 写入资金明细
        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.PAYMENT.getCode());
        fundDetail.setPayOrderId(payDetail.getPayOrderId());
        fundDetail.setDeliveryOrderId(payDetail.getChOrderId());
        fundDetail.setPayDetailOrderId(payDetail.getOrderId());
        fundDetail.setAmount(payDetail.getAmount());
        fundDetail.setLog(s.toString());
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 新资金结算付款 340096
     */
    @Transactional
    public PurchaserData paymentZBao(DeliveryOrder deliveryOrder, PayDetail payDetail, int orderSubfix) throws IOException {
        String account = deliveryOrder.getBuyerAccount();//采购商账号
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());
        //新资金总金额
        BigDecimal totalAmountZBao = purchaserData.getTotalAmountZBao();
        //总金额减去该单的金额
        BigDecimal newTotalAmount = totalAmountZBao.subtract(payDetail.getAmount().setScale(2, RoundingMode.DOWN));
        purchaserData.setTotalAmountZBao(newTotalAmount);
        //判断新资金总金额是否还大于0
        if (BigDecimal.ZERO.compareTo(newTotalAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        // 冻结金额的更新
        BigDecimal freezeAmount = purchaserData.getFreezeAmountZBao();
        logger.info("冻结金额扣除前,loginAccount:{},freezeAmountZBao:{}", new Object[]{purchaserData.getLoginAccount(), freezeAmount});
        logger.info("paydetail金额" + payDetail.getAmount());
        freezeAmount = freezeAmount.subtract(payDetail.getAmount().setScale(2, RoundingMode.DOWN));
        logger.info("冻结金额扣除后,loginAccount:{},freezeAmountZBao:{}", new Object[]{purchaserData.getLoginAccount(), freezeAmount});
        purchaserData.setFreezeAmountZBao(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        //可用资金=总金额-冻结金额
        BigDecimal availableAmountZBao = newTotalAmount.subtract(freezeAmount);
        availableAmountZBao = availableAmountZBao.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmountZBao(availableAmountZBao);

        if (BigDecimal.ZERO.compareTo(availableAmountZBao) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        //更新采购商新资金
        purchaserDataDao.update(purchaserData);
//        //调用7bao去扣除7bao的收货商资金
//        Boolean toZBao = amoutHttp.conToZBao(deliveryOrder,payDetail.getAmount().setScale(2,RoundingMode.DOWN),orderSubfix);
//        if (toZBao){
//            logger.info("请求7bao扣除收货商资金成功{}：",deliveryOrder.getOrderId());
//        }
//        if (!toZBao){
//            logger.error("请求7bao扣除收货商资金失败{}：",deliveryOrder.getOrderId());
//            throw new SystemException("7bao扣除资金失败");
//        }

        // -------------------更新支付单信息------------------------
        PayOrder payOrder = payOrderDao.selectByOrderId(payDetail.getPayOrderId(), true);
        // 支付单已用金额
        BigDecimal usedAmount = payOrder.getUsedAmount();
        usedAmount = usedAmount.add(payDetail.getAmount()).setScale(2, RoundingMode.DOWN);
        payOrder.setUsedAmount(usedAmount);

        // 支付单余额
        BigDecimal balance = payOrder.getAmount().subtract(usedAmount);
        balance = balance.setScale(2, RoundingMode.DOWN);
        payOrder.setBalance(balance);

        if (BigDecimal.ZERO.compareTo(balance) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }


        payOrder.setLastUpdateTime(new Date());
        // 更新支付单数据
        payOrderDao.update(payOrder);

        StringBuffer s = new StringBuffer();
        s.append("【付款】出货订单号：").append(payDetail.getChOrderId()).append("，扣除金额：").append(payDetail.getAmount());
        s.append("，当前总金额：").append(purchaserData.getTotalAmount());
        s.append("，冻结金额：").append(purchaserData.getFreezeAmount());
        s.append("，可用金额：").append(purchaserData.getAvailableAmount());
        s.append("，充值订单号：").append(payDetail.getPayOrderId());
        s.append("，扣除金额：").append(payDetail.getAmount());
        s.append("，已用金额：").append(payOrder.getUsedAmount());
        s.append("，可用金额：").append(payOrder.getBalance());
        s.append("\r\n");
        s.append(",付款明细订单号：").append(payDetail.getOrderId());

        // 写入资金明细
        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.PAYMENT.getCode());
        fundDetail.setPayOrderId(payDetail.getPayOrderId());
        fundDetail.setDeliveryOrderId(payDetail.getChOrderId());
        fundDetail.setPayDetailOrderId(payDetail.getOrderId());
        fundDetail.setAmount(payDetail.getAmount());
        fundDetail.setLog(s.toString());
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    /**
     * 资金结算
     *
     * @param orderId      出货单号
     * @param amount       订单金额
     * @param realAmount   实际结算金额
     * @param buyerAccount 采购商账号
     */
    @Override
    @Transactional
    public boolean settlement(String orderId, BigDecimal amount, BigDecimal realAmount, String buyerAccount) {
        // 根据出货单号查询付款明细是否有记录，有的话直接返回，无的话生成付款记录
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("chOrderId", orderId);
        paramMap.put("isLocked", true);
        List<PayDetail> payDetails = payDetailDao.selectByMap(paramMap);
        if (!CollectionUtils.isEmpty(payDetails)) {
            logger.info("出货单号：{}，已存在付款明细记录，不再重复创建", orderId);
            return false;
        }

        // 查询收货商可用的支付单
        List<PayOrder> payOrders = payOrderManager.queryAvailablePayOrders(buyerAccount, true);
        if (CollectionUtils.isEmpty(payOrders)) {
            logger.error("收货商：{}的资金不足，不能完成付款", buyerAccount);
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        BigDecimal totalBalance = BigDecimal.ZERO; //收货商总余额
        for (PayOrder payOrder : payOrders) {
            totalBalance = totalBalance.add(payOrder.getBalance());
        }
        if (totalBalance.compareTo(realAmount) == -1) {
            logger.error("收货商：{}的资金不足，不能完成付款", buyerAccount);
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        BigDecimal configTotalAmount = BigDecimal.ZERO; // 配置的总金额
        BigDecimal needAmount = realAmount;
        Map<String, BigDecimal> payOrderMap = new HashMap<String, BigDecimal>(); // 用哪些支付单进行付款
        for (int i = 0, j = payOrders.size(); i < j && needAmount.doubleValue() > 0; i++) {
            PayOrder payOrder = payOrders.get(i);
            if (payOrder.getBalance().compareTo(needAmount) >= 0) {
                payOrderMap.put(payOrder.getOrderId(), needAmount);
                configTotalAmount = configTotalAmount.add(needAmount);
                break;
            }

            payOrderMap.put(payOrder.getOrderId(), payOrder.getBalance());
            configTotalAmount = configTotalAmount.add(payOrder.getBalance());
            needAmount = needAmount.subtract(payOrder.getBalance());
        }

        // 配单金额不等于付款金额
        if (configTotalAmount.compareTo(realAmount) != 0) {
            throw new SystemException(ResponseCodes.ConfigAmountIsNotEqualPayAmount.getCode(),
                    ResponseCodes.ConfigAmountIsNotEqualPayAmount.getMessage());
        }

        // 逐笔生成付款明细
        for (Map.Entry<String, BigDecimal> entry : payOrderMap.entrySet()) {
            // 创建付款明细
            String payOrderId = entry.getKey();
            BigDecimal payAmount = entry.getValue();
            PayDetail payDetail = payDetailManager.create(payOrderId, orderId, payAmount);
        }

        // 扣款时，再查一次数据库，以防多生成付款明细记录
        BigDecimal totalAmount = BigDecimal.ZERO;
        payDetails = payDetailDao.selectByMap(paramMap);
        for (PayDetail payDetail : payDetails) {
            totalAmount = totalAmount.add(payDetail.getAmount());
        }
        if (totalAmount.compareTo(realAmount) != 0) {
            // 付款金额不等于实际交易金额
            throw new SystemException(ResponseCodes.ConfigAmountIsNotEqualPayAmount.getCode(),
                    ResponseCodes.ConfigAmountIsNotEqualPayAmount.getMessage());
        }
        for (PayDetail payDetail : payDetails) {
            // 付款，扣收货商资金
            this.payment(buyerAccount, payDetail);
        }

        if (amount.compareTo(realAmount) == 1) {
            // 部分结算情况，解冻剩余的资金

            // 解冻采购商资金
            BigDecimal balance = amount.subtract(realAmount);
            this.releaseFreezeFund(FREEZE_BY_DELIVERY_ORDER, buyerAccount, orderId, balance);
        }

        return true;
    }

    /**
     * 新资金结算
     * ZW_C_JB_00021
     */
    @Override
    @Transactional
    public boolean newSettlement(DeliveryOrder deliveryOrder) throws IOException {
        // 根据出货单号查询付款明细是否有记录，有的话直接返回，无的话生成付款记录
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("chOrderId", deliveryOrder.getOrderId());
        paramMap.put("isLocked", true);
        List<PayDetail> payDetails = payDetailDao.selectByMap(paramMap);
        if (!CollectionUtils.isEmpty(payDetails)) {
            logger.info("出货单号：{}，已存在付款明细记录，不再重复创建", deliveryOrder.getOrderId());
            return false;
        }
        //查询新资金收货商可用的支付单
        List<PayOrder> payOrders = payOrderManager.queryAvailablePayOrdersZBao(deliveryOrder.getBuyerAccount(), true);
        if (CollectionUtils.isEmpty(payOrders)) {
            logger.error("收货商：{}的资金不足，不能完成付款", deliveryOrder.getBuyerAccount());
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        BigDecimal totalBalance = BigDecimal.ZERO; //收货商总余额
        for (PayOrder payOrder : payOrders) {
            totalBalance = totalBalance.add(payOrder.getBalance());
        }
        if (totalBalance.compareTo(deliveryOrder.getRealAmount()) == -1) {
            logger.error("收货商：{}的资金不足，不能完成付款", deliveryOrder.getRealAmount());
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }
        BigDecimal configTotalAmount = BigDecimal.ZERO; // 配置的总金额
        BigDecimal needAmount = deliveryOrder.getRealAmount();
        Map<String, BigDecimal> payOrderMap = new HashMap<String, BigDecimal>(); // 用哪些支付单进行付款
        for (int i = 0, j = payOrders.size(); i < j && needAmount.doubleValue() > 0; i++) {
            PayOrder payOrder = payOrders.get(i);
            if (payOrder.getBalance().compareTo(needAmount) >= 0) {
                payOrderMap.put(payOrder.getOrderId(), needAmount);
                configTotalAmount = configTotalAmount.add(needAmount);
                break;
            }

            payOrderMap.put(payOrder.getOrderId(), payOrder.getBalance());
            configTotalAmount = configTotalAmount.add(payOrder.getBalance());
            needAmount = needAmount.subtract(payOrder.getBalance());
        }
        // 配单金额不等于付款金额
        if (configTotalAmount.compareTo(deliveryOrder.getRealAmount()) != 0) {
            throw new SystemException(ResponseCodes.ConfigAmountIsNotEqualPayAmount.getCode(),
                    ResponseCodes.ConfigAmountIsNotEqualPayAmount.getMessage());
        }

        // 逐笔生成付款明细
        for (Map.Entry<String, BigDecimal> entry : payOrderMap.entrySet()) {
            // 创建付款明细
            String payOrderId = entry.getKey();
            BigDecimal payAmount = entry.getValue();
            PayDetail payDetail = payDetailManager.create(payOrderId, deliveryOrder.getOrderId(), payAmount);
        }

        // 扣款时，再查一次数据库，以防多生成付款明细记录
        BigDecimal totalAmount = BigDecimal.ZERO;
        payDetails = payDetailDao.selectByMap(paramMap);
        for (PayDetail payDetail : payDetails) {
            totalAmount = totalAmount.add(payDetail.getAmount());
        }
        if (totalAmount.compareTo(deliveryOrder.getRealAmount()) != 0) {
            // 付款金额不等于实际交易金额
            throw new SystemException(ResponseCodes.ConfigAmountIsNotEqualPayAmount.getCode(),
                    ResponseCodes.ConfigAmountIsNotEqualPayAmount.getMessage());
        }
        int orderSubfix = 0;
        StringBuilder sbIransferList = new StringBuilder();
        BigDecimal amount = BigDecimal.ZERO;
        for (PayDetail payDetail : payDetails) {
            if (payDetails.size() > 1) {
                orderSubfix += 1;
            }
            // 付款，扣收货商资金
            this.paymentZBao(deliveryOrder, payDetail, orderSubfix);

            String orderId = deliveryOrder.getOrderId();
            if (orderSubfix > 0) {
                orderId = deliveryOrder.getOrderId() + "_" + orderSubfix;
            }
            BigDecimal money = payDetail.getAmount().setScale(2, RoundingMode.DOWN);
            amount = amount.add(money);
            sbIransferList.append(orderId + "," + money + ";");
        }

        if (deliveryOrder.getAmount().compareTo(deliveryOrder.getRealAmount()) == 1) {
            // 部分结算情况，解冻剩余的资金
            // 解冻采购商资金
            BigDecimal balance = deliveryOrder.getAmount().subtract(deliveryOrder.getRealAmount());
            this.releaseFreezeFund(FREEZE_BY_DELIVERY_ORDER, deliveryOrder, balance);
        }

        if (StringUtils.isNotBlank(sbIransferList)) {
            //7bao批量扣款
            String transferData = sbIransferList.substring(0, sbIransferList.length() - 1);
            logger.info("7bao批量扣款,transferData:{},deliveryOrder:{}", transferData, deliveryOrder);
            BigDecimal balance = deliveryOrder.getAmount().subtract(deliveryOrder.getRealAmount());

            PurchaserData purchaserData = purchaserDataManager.selectByAccountForUpdate(deliveryOrder.getBuyerAccount());
            if (purchaserData == null) {
                throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(), ResponseCodes.EmptyPurchaseAccount.getMessage());
            }
            //查询资金明细
            Boolean isSuccess = amoutHttp.queryFundDetail(deliveryOrder.getOrderId(), deliveryOrder.getBuyerAccount(), amount);
            if (!isSuccess) {
                amoutHttp.mulitCreatTransferDeduction(deliveryOrder.getBuyerUid(), transferData, deliveryOrder.getBuyerAccount(), balance, 2, deliveryOrder.getOrderId(), purchaserData.getTotalAmountZBao(), purchaserData.getAvailableAmountZBao(), purchaserData.getFreezeAmountZBao());
            }
        }
        return true;
    }

    /**
     * 调用资金接口转账给出货方
     *
     * @param deliveryOrderId 出货单号
     * @param payOrderId      支付单号
     * @param buyerId         采购商ID
     * @param buyerAccount    采购商账号
     * @param sellerId        获得转账的用户ID
     * @param sellerName      获得转账的用户名
     * @param totalFee        转账总金额
     * @param payDetail       付款明细
     */
    @Override
    @Transactional
    public boolean transfer(String deliveryOrderId, String payOrderId, String buyerId, String buyerAccount,
                            String sellerId, String sellerName,
                            BigDecimal totalFee, PayDetail payDetail) {
        // 生成转账明细
        StringBuffer feeDetails = new StringBuffer();
        if (payDetail != null) {
            feeDetails.append("sub_transfer_fee,");
            feeDetails.append(payDetail.getOrderId()).append(",");
            feeDetails.append(payDetail.getAmount()).append(",");
            feeDetails.append(",,");
            feeDetails.append(sellerId).append(",");
            feeDetails.append(sellerName).append(";");
        }

        logger.info("发起部分转账请求，出货单号：{}，支付订单号:{}，转账明细:{}", new Object[]{deliveryOrderId,
                payOrderId, feeDetails.toString()});

        DirectPartialTransferResponse response = payManager.directPartialTransfer(payOrderId, buyerId, buyerAccount,
                sellerId, sellerName, totalFee, feeDetails.toString());

        // 付款成功
        if (response != null && response.isResult() && payOrderId.equals(response.getOrderId())) {
            // 创建资金明细
            String log = String.format("【转账】已转账给出货方，付款单号：%s，转账金额：%s", payOrderId, totalFee);
            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(buyerAccount);
            fundDetail.setType(FundType.TRANSFER.getCode());
            fundDetail.setPayOrderId(payOrderId);
            fundDetail.setDeliveryOrderId(deliveryOrderId);
            fundDetail.setAmount(totalFee);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);

            logger.info("订单号：{}，转账成功", payOrderId);
            return true;
        }

        logger.info("订单号：{}，转账失败。", payOrderId);
        return false;
    }

    /**
     * 调用资金接口转账给出货方
     *
     * @param deliveryOrderId 出货单号
     * @param payOrderId      支付单号
     * @param buyerId         采购商ID
     * @param buyerAccount    采购商账号
     * @param sellerId        获得转账的用户ID
     * @param sellerName      获得转账的用户名
     * @param totalFee        转账总金额
     * @param payOrder        付款明细
     */
    @Override
    @Transactional
    public boolean transfer(String deliveryOrderId, String payOrderId, String buyerId, String buyerAccount, String sellerId, String sellerName, BigDecimal totalFee, PayOrder payOrder) {
        // 生成转账明细
        StringBuffer feeDetails = new StringBuffer();
        if (payOrder != null) {
            feeDetails.append("sub_transfer_fee,");
            feeDetails.append(payOrder.getOrderId()).append(",");
            feeDetails.append(totalFee).append(",");
            feeDetails.append(",,");
            feeDetails.append(sellerId).append(",");
            feeDetails.append(sellerName).append(";");
        }

        logger.info("发起部分转账请求，出货单号：{}，支付订单号:{}，转账明细:{}", new Object[]{deliveryOrderId,
                payOrderId, feeDetails.toString()});

        DirectPartialTransferResponse response = payManager.directPartialTransfer(payOrderId, buyerId, buyerAccount,
                sellerId, sellerName, totalFee, feeDetails.toString());

        // 付款成功
        if (response != null && response.isResult() && payOrderId.equals(response.getOrderId())) {
            // 创建资金明细
            String log = String.format("【老资金转账】已转账给出货方，付款单号：%s，转账金额：%s", payOrderId, totalFee);
            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(buyerAccount);
            fundDetail.setType(FundType.TRANSFER.getCode());
            fundDetail.setPayOrderId(payOrderId);
            fundDetail.setDeliveryOrderId(deliveryOrderId);
            fundDetail.setAmount(totalFee);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);

            logger.info("订单号：{}，转账成功", payOrderId);
            return true;
        }

        logger.info("订单号：{}，转账失败。", payOrderId);
        return false;
    }

    /**
     * 冻结申请退款资金
     *
     * @param refundOrder 退款订单
     */
    @Override
    @Transactional
    public void freezeRefundFund(RefundOrder refundOrder) {
        // 修改当前支付单状态为退款审核中
        PayOrder payOrder = payOrderManager.queryByOrderId(refundOrder.getPayOrderId(), true);
        payOrder.setStatus(PayOrder.APPLY_REFUND);
        payOrder.setLastUpdateTime(new Date());
        payOrderDao.update(payOrder);

        // 冻结采购商资金
        this.freezeFund(FREEZE_BY_REFUND_ORDER, payOrder.getAccount(), refundOrder.getOrderId(),
                refundOrder.getRefundAmount());
    }

    /**
     * 解冻申请退款资金
     *
     * @param refundOrder
     */
    @Override
    @Transactional
    public void releaseFreezeRefundFund(RefundOrder refundOrder) {
        // 修改当前支付单状态
        PayOrder payOrder = payOrderManager.queryByOrderId(refundOrder.getPayOrderId(), true);
        payOrder.setStatus(PayOrder.PAID);
        payOrder.setLastUpdateTime(new Date());
        payOrderDao.update(payOrder);

        // 解冻采购商资金
        this.releaseFreezeFund(FREEZE_BY_REFUND_ORDER, payOrder.getAccount(), refundOrder.getOrderId(),
                refundOrder.getRefundAmount());

    }

    /**
     * 退款给收货方
     *
     * @param refundOrder
     */
    @Override
    @Transactional
    public void refund(RefundOrder refundOrder) {
        // 修改当前支付单数据
        PayOrder payOrder = payOrderManager.queryByOrderId(refundOrder.getPayOrderId(), true);

        // 支付单余额
        BigDecimal balance = payOrder.getBalance();
        balance = balance.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);

        if (BigDecimal.ZERO.compareTo(balance) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        payOrder.setBalance(balance);
        payOrder.setStatus(PayOrder.REFUNDED);
        payOrder.setLastUpdateTime(new Date());
        payOrderDao.update(payOrder);

        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(payOrder.getAccount());
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmount();
        totalAmount = totalAmount.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);
        purchaserData.setTotalAmount(totalAmount);

        if (BigDecimal.ZERO.compareTo(totalAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 更新采购商数据
        purchaserDataDao.update(purchaserData);

        // 写资金明细记录
        String log = String.format("【退款】退款订单号:%s,退款金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                refundOrder.getOrderId(), refundOrder.getRefundAmount(), purchaserData.getTotalAmount(),
                purchaserData.getFreezeAmount(), purchaserData.getAvailableAmount());

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(payOrder.getAccount());
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        fundDetail.setRefundOrderId(refundOrder.getOrderId());
        fundDetail.setAmount(refundOrder.getRefundAmount());
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        //  调用部分退款接口
        boolean isSuccess = refund(refundOrder.getPayOrderId(), refundOrder.getOrderId(), payOrder.getUid(),
                payOrder.getAccount(), refundOrder.getRefundAmount());
        if (!isSuccess) {
            throw new SystemException(ResponseCodes.RefundIsFail.getCode(),
                    ResponseCodes.RefundIsFail.getMessage());
        }
    }

    @Override
    public boolean transferFor7Bao(DeliveryOrder deliveryOrder, String deliveryOrderId, String payOrderId, String buyerId, String buyerAccount,
                                   String sellerId, String sellerName,
                                   BigDecimal totalFee, PayDetail payDetail, int orderSubfix) {
        boolean aBoolean = amoutHttp.conToMain(deliveryOrder, totalFee, orderSubfix);
        if (aBoolean) {
            // 创建资金明细
            String log = String.format("【转账】已转账给出货方，付款单号：%s，转账金额：%s", payOrderId, totalFee);
            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(buyerAccount);
            fundDetail.setType(FundType.TRANSFER.getCode());
            fundDetail.setPayOrderId(payOrderId);
            fundDetail.setDeliveryOrderId(deliveryOrderId + "_" + orderSubfix);
            fundDetail.setAmount(totalFee);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);
            logger.info("订单号：{}，转账成功", payOrderId);
            return true;
        }
        logger.info("订单号：{}，转账失败。", payOrderId);
        return false;
    }

    /**
     * 调用资金接口退款给出货方
     *
     * @param payOrderId    支付单号
     * @param refundOrderId 出货单号
     * @param buyerId       获得退款的用户ID
     * @param buyerName     获得退款的用户名
     * @param totalFee      退款总金额
     */
    @Transactional
    public boolean refund(String payOrderId, String refundOrderId, String buyerId, String buyerName, BigDecimal totalFee) {

        // 生成转账明细
        StringBuffer feeDetails = new StringBuffer();
        feeDetails.append("sub_refund_fee,");
        feeDetails.append(refundOrderId).append(",");
        feeDetails.append(totalFee).append(",");
        feeDetails.append(",,");
        feeDetails.append(buyerId).append(",");
        feeDetails.append(buyerName).append(";");

        logger.info("发起部分退款请求，支付单号：{}，退款订单号:{}，退款明细:{}", new Object[]{payOrderId,
                refundOrderId, feeDetails.toString()});

        RefundResponse response = payManager.directPartialRefund(payOrderId, buyerId, buyerName,
                totalFee, feeDetails.toString());

        // 付款成功
        if (response != null && response.isResult() && payOrderId.equals(response.getOrderId())) {
            // 创建资金明细
            String log = String.format("【退款】已退款给收货方，退款单号：%s，退款金额：%s", refundOrderId, totalFee);
            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(buyerName);
            fundDetail.setType(FundType.REFUND.getCode());
            fundDetail.setPayOrderId(payOrderId);
            fundDetail.setRefundOrderId(refundOrderId);
            fundDetail.setAmount(totalFee);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);

            logger.info("退款订单号：{}，退款成功", refundOrderId);
            return true;
        }

        logger.info("退款订单号：{}，退款失败。", refundOrderId);
        return false;
    }

    /**
     * 新资金：给此出货单的卖家加钱
     */
    @Override
    public boolean plusAmount(DeliveryOrder deliveryOrder) {
        String sellerAccount = deliveryOrder.getSellerAccount();//此单的卖家账号
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(sellerAccount);//查出此卖家的信息，进行更新

        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());
        //新资金总金额
        BigDecimal totalAmountZBao = purchaserData.getTotalAmountZBao();
        if (totalAmountZBao == null) {
            totalAmountZBao = BigDecimal.ZERO;
        }
        //总金额加上该单的金额
        BigDecimal newTotalAmount = totalAmountZBao.add(deliveryOrder.getRealAmount().setScale(2, RoundingMode.DOWN));
        purchaserData.setTotalAmountZBao(newTotalAmount);
        //可用资金加上该单的金额
        BigDecimal newAvailableAmount = purchaserData.getAvailableAmountZBao();
        if (newAvailableAmount == null) {
            newAvailableAmount = BigDecimal.ZERO;
        }
        newAvailableAmount = newAvailableAmount.add(deliveryOrder.getRealAmount().setScale(2, RoundingMode.DOWN));
        purchaserData.setAvailableAmountZBao(newAvailableAmount);
        //更新此单卖家的purchasedata
        purchaserDataDao.update(purchaserData);
        //生成支付单
        String payOrder = payOrderManager.create7BaoOrder(deliveryOrder.getRealAmount(), purchaserData.getLoginAccount(), null);
        String log = String.format("【7Bao自动充值单】充值订单号:%s,充值金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                payOrder, deliveryOrder.getRealAmount(), purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                purchaserData.getAvailableAmountZBao());
        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(sellerAccount);
        fundDetail.setType(FundType.RECHARGE_AUTO_7BAO.getCode());
        fundDetail.setPayOrderId(payOrder);
        fundDetail.setAmount(deliveryOrder.getRealAmount());
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        return true;
    }

    /**
     * 释放7宝资金，但不涉及商城收货资金
     *
     * @param deliveryOrder
     */
    @Override
    public void freezeZBao(DeliveryOrder deliveryOrder) {
        if (deliveryOrder.getOrderId().contains(OrderPrefix.NEWORDERID.getName())) {
            Integer yesOrNo = 2;
            this.freezeAmountZBao(deliveryOrder.getBuyerAccount(), deliveryOrder.getBuyerUid(), deliveryOrder.getAmount(), yesOrNo, freezeUrl, deliveryOrder.getOrderId());
            logger.info("通知7bao解冻资金成功{}：", deliveryOrder.getOrderId());
        }
    }

    /**
     * 发送请求至7bao冻结 对应账户的7bao资金  340096
     * 参数:1.5173登录账号  2.5173Uid  3.冻结/解冻金额
     * 4.冻结还是解冻 yesOrNo = 1  表示冻结； yesOrNo= 2 表示解冻
     * 5.要发送到7bao的http地址
     */
    @Override
    public void freezeAmountZBao(String account, String uid, BigDecimal amount, Integer yesOrNo, String url, String orderId) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("冻结/解冻资金为0直接返回");
            return;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        PurchaserData purchaserData = purchaserDataManager.selectByAccount(account);
        BigDecimal totalAmountZBao = purchaserData.getTotalAmountZBao();
        BigDecimal availableAmountZbao = purchaserData.getAvailableAmountZBao();
        BigDecimal freezeAmountZBao = purchaserData.getFreezeAmountZBao();
        //调用7bao接口冻结7bao资金  参数  冻结金额
        UpdateFundDTO fundDTO = new UpdateFundDTO();
        fundDTO.setLoginAccount(account);
        fundDTO.setUid(uid);
        fundDTO.setFreezeFund(amount);
        fundDTO.setYesOrNo(yesOrNo);
        fundDTO.setOrderId(orderId);
        fundDTO.setTotalAmount(totalAmountZBao.setScale(2));
        fundDTO.setAvailableAmount(availableAmountZbao.setScale(2));
        fundDTO.setFreezeAmount(freezeAmountZBao.setScale(2));
        String sign = "";
        try {
            String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", fundDTO.getLoginAccount(), fundDTO.getUid(),
                    fundDTO.getFreezeFund(), fundDTO.getYesOrNo(), orderId, df.format(totalAmountZBao), df.format(availableAmountZbao),
                    df.format(freezeAmountZBao), freezeSerKey);
            logger.info("冻结/解冻参数{}：", format);
            sign = EncryptHelper.md5(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fundDTO.setSign(sign);
        //调用http请求7bao冻结资金接口
        HttpToConn httpToZBaoFund = new HttpToConn();
        JSONObject jsonParam = JSONObject.fromObject(fundDTO);
//        //调用
//        JSONObject jsonResult = httpToZBaoFund.httpPost(url, jsonParam);
//        //判断返回值
//        String responseStatusStr = jsonResult.getString("responseStatus");
//        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
//        ResponseStatus responseStatus = jsonMapper.fromJson(responseStatusStr, ResponseStatus.class);
//        if (!"00".equals(responseStatus.getCode())) {
//            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
//        }


        //调用
        String response = httpToZBaoFund.sendHttpPost(url, jsonParam);
        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
        BaseResponse baseResponse = jsonMapper.fromJson(response, BaseResponse.class);
        logger.info("freezeAmountZBao,baseResponse,{}", baseResponse);
        if (baseResponse == null) {
            throw new SystemException(ResponseCodes.ResponseError.getCode(), ResponseCodes.ResponseError.getMessage());
        }
        ResponseStatus responseStatus = baseResponse.getResponseStatus();
        if (responseStatus == null || !responseStatus.getCode().equals("00")) {
            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
        }
    }
}

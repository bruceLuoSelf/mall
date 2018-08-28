/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		AccountManagerImpl
 * 包	名：		com.wzitech.chinabeauty.usermgmt.business.impl
 * 项目名称：	chinabeauty-usermgmt
 * 作	者：		SunChengfei
 * 创建时间：	2013-9-26
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2013-9-26 上午14:04:26
 ************************************************************************************/
package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.FundsQueryType;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.*;
import com.wzitech.gamegold.order.business.IAutoPayManager;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderLogManager;
import com.wzitech.gamegold.order.business.IZbaoFundManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import com.wzitech.gamegold.shorder.enums.IncomeType;
import com.wzitech.gamegold.shorder.enums.LogTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ztjie
 */
@Component
public class AutoPayManagerImpl extends AbstractBusinessObject implements IAutoPayManager {

    protected static final Log log = LogFactory.getLog(OrderInfoManagerImpl.class);

    @Autowired
    IOrderConfigManager orderConfigQuery;

    @Autowired
    IPayManager payManager;

    /*@Autowired
    ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

    @Autowired
    IZbaoFundManager zbaoFundManager;


    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IGameAccountManager gameAccountManager;

    @Autowired
    private ISplitRepositoryLogManager splitRepositoryLogManager;
    /**
     * 售得用户Id
     */
    @Value("${fund.batch.transfer.get.user.id}")
    private String getUserId = "";

    @Override
    public boolean pay(OrderInfoEO orderInfo) {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
        }

        StringBuffer sb = new StringBuffer();
        sb.append("订单号：").append(orderInfo.getOrderId()).append("，结单打款\n");

        List<ConfigResultInfoEO> configList = orderConfigQuery.orderConfigList(orderInfo.getOrderId());
        BigDecimal transferFees = new BigDecimal(0);
        ArrayList<TransferUserInfo> transferList = new ArrayList<TransferUserInfo>();

        for (ConfigResultInfoEO config : configList) {

            // 兼容性代码，稳定后需要删除 start
            /*if (config.getIncome() == null) {
                BigDecimal goldCount = new BigDecimal(config.getConfigGoldCount());
                BigDecimal totalPrice = config.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                // 获取游戏佣金比例
                double commissionBase = CommissionUtil.getCommission(orderInfo.getCreateTime(),
                        orderInfo.getGameName(), orderInfo.getRegion());
                // 卖家佣金
                BigDecimal commission = totalPrice.multiply(new BigDecimal(commissionBase));
                commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
                // 卖家收益 = 配单总价-佣金
                BigDecimal income = totalPrice.subtract(commission);
                // 订单价格
                BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
                // 库存价格
                BigDecimal repositoryAmount = config.getRepositoryUnitPrice().multiply(goldCount);
                // 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
                BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

                config.setTotalPrice(totalPrice);
                config.setIncome(income);
                config.setCommission(commission);
                config.setBalance(balance);
            }*/
            // 兼容性代码，稳定后需要删除 end

//            //判断是否为已发货的订单 判断是否是走新流程
//            if (config.getState().equals(OrderState.Delivery.getCode())) {
//                String loginAccount = config.getLoginAccount();
//                //查询当前卖家的信息，判断是否已绑定7bao并且开通新资金
//                SellerInfo sellerInfo = sellerManager.findByAccountAndUid(loginAccount, config.getAccountUid());
//                if (sellerInfo.getIsNewFund()!=null && sellerInfo.getIsNewFund() && sellerInfo.getisAgree()!=null && sellerInfo.getisAgree()) {
//                    //调用7bao增加资金接口，注意增加的资金为卖家的收益款
//                    zbaoFundManager.recharge7Bao(loginAccount, config.getIncome(), FundType.RECHARGE_AUTO_7BAO.getCode());
//                }
//            }


            TransferUserInfo transferUserInfo = new TransferUserInfo();
            transferUserInfo.setGetUserId(config.getAccountUid());
            transferUserInfo.setGetUserName(config.getLoginAccount());
            transferUserInfo.setTransferFee(config.getIncome());
            transferFees = transferFees.add(config.getIncome());
            transferList.add(transferUserInfo);

            sb.append("配单金额:").append(config.getTotalPrice()).append("元,");
            sb.append("卖家(").append(config.getLoginAccount()).append(")收益:").append(config.getIncome()).append("元,");
            sb.append("卖家佣金:").append(config.getCommission()).append("元，");
            sb.append("差额收入:").append(config.getBalance()).append("元 ");

            //因为需要repositoryInfo ConfigResultInfoEO orderInfo表数据避免查询只能添加在此 添加销售日志
            SplitRepositoryLog splitRepositoryLog = new SplitRepositoryLog();
            RepositoryInfo repositoryInfo = config.getRepositoryInfo();
            splitRepositoryLog.setBuyerAccount(config.getLoginAccount());
            splitRepositoryLog.setGameName(orderInfo.getGameName());
            splitRepositoryLog.setRegion(orderInfo.getRegion());
            splitRepositoryLog.setServer(orderInfo.getServer());
            splitRepositoryLog.setGameRace(orderInfo.getGameRace());
            splitRepositoryLog.setGameAccount(repositoryInfo.getGameAccount());
            splitRepositoryLog.setRoleName(repositoryInfo.getSellerGameRole());
            splitRepositoryLog.setLogType(LogTypeEnum.SELLERPAID.getCode());
            splitRepositoryLog.setCreateTime(new Date());
            splitRepositoryLog.setCount(config.getConfigGoldCount());
            splitRepositoryLog.setFcOrderId(orderInfo.getOrderId());
            splitRepositoryLog.setIncomeType(IncomeType.SPEND.getCode());
            splitRepositoryLogManager.saveLog(splitRepositoryLog);
            gameAccountManager.addTodaySaleCount(config.getConfigGoldCount(),config.getLoginAccount(),orderInfo.getGameName(),orderInfo.getRegion(),orderInfo.getServer(),orderInfo.getGameRace(),repositoryInfo.getGameAccount(),repositoryInfo.getSellerGameRole());
        }

        BigDecimal subCommission = orderInfo.getTotalPrice().subtract(transferFees);
        sb.append("总佣金(订单总金额-支付卖家金额)=").append(orderInfo.getTotalPrice()).append("-")
                .append(transferFees).append("=").append(subCommission).append("元。\n");

        QueryDetailResponse queryDetailResponse = payManager.queryDetail(FundsQueryType.Transfer.getCode(), orderInfo.getOrderId(), getUserId);
        BatchTransferResponse batchTransferResponse = new BatchTransferResponse();
        boolean flag = true;
        if (queryDetailResponse == null
                || queryDetailResponse.getEarnStatus() != 1
                || !StringUtils.equals(queryDetailResponse.getTradeNo(), orderInfo.getOrderId())) {

            batchTransferResponse = payManager.batchTransfer(
                    orderInfo.getOrderId(),
                    orderInfo.getTotalPrice(),
                    orderInfo.getUid(),
                    orderInfo.getUserAccount(),
                    subCommission,
                    transferList);

            if (batchTransferResponse == null || !batchTransferResponse.isResult()
                    || !StringUtils.equals(batchTransferResponse.getOrderId(), orderInfo.getOrderId())
                    || !StringUtils.equalsIgnoreCase("SUCCEES", batchTransferResponse.getMessage())) {

                queryDetailResponse = payManager.queryDetail(FundsQueryType.Transfer.getCode(), orderInfo.getOrderId(), getUserId);
                if (queryDetailResponse == null
                        || queryDetailResponse.getEarnStatus() != 1
                        || !StringUtils.equals(queryDetailResponse.getTradeNo(), orderInfo.getOrderId())) {
                    flag = false;
                }
            }
        }
        if (flag) {
            sb.append("打款成功,主站返回消息：").append(batchTransferResponse.getMessage());
        } else {
            sb.append("打款失败,主站返回消息：").append(batchTransferResponse.getMessage());
        }
        //logManager.add(ModuleType.FUND, sb.toString(), CurrentUserContext.getUser());
        log.info(sb.toString());

        OrderLogInfo log = new OrderLogInfo();
        log.setLogType(LogType.ORDER_STATEMENT);
        log.setOrderId(orderInfo.getOrderId());
        log.setRemark(sb.toString());
        orderLogManager.add(log);

        return flag;
    }

    @Override
    public boolean refund(OrderInfoEO orderInfo) {
        // 先查询退款明细
        QueryDetailResponse detailResponse = payManager.queryDetail(FundsQueryType.Refund.getCode(), orderInfo.getOrderId(), orderInfo.getUid());

        boolean isRefund = true;
        if (detailResponse == null
                || !orderInfo.getOrderId().equals(detailResponse.getTradeNo())
                || detailResponse.getEarnStatus() != 1
                || !StringUtils.equalsIgnoreCase("SUCCESS", detailResponse.getMessage())) {

            // 退款
            RefundResponse refundResponse = payManager.refund(
                    orderInfo.getOrderId(),
                    orderInfo.getUid(),
                    orderInfo.getUserAccount(),
                    orderInfo.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            if (null == refundResponse) {
                isRefund = false;
            } else if (!refundResponse.isResult()) {
                isRefund = false;
            } else if (!orderInfo.getOrderId().equals(refundResponse.getOrderId())) {
                isRefund = false;
            }
        }

        if (!isRefund) {
            // 退款失败的，再查询一次是否真的是退款失败的
            detailResponse = payManager.queryDetail(FundsQueryType.Refund.getCode(), orderInfo.getOrderId(), orderInfo.getUid());
            if (detailResponse != null
                    && orderInfo.getOrderId().equals(detailResponse.getTradeNo())
                    && detailResponse.getEarnStatus() == 1
                    && StringUtils.equalsIgnoreCase("SUCCESS", detailResponse.getMessage())) {
                isRefund = true;
            }
        }


        StringBuffer sb = new StringBuffer("\n");
        if (isRefund) {
            sb.append("退款成功。订单号：").append(orderInfo.getOrderId()).append("，退款金额：")
                    .append(orderInfo.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP)).append("元，退回帐号：")
                    .append(orderInfo.getUserAccount());
        } else {
            sb.append("退款失败。订单号：").append(orderInfo.getOrderId());
        }
        //logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
        log.info(sb.toString());

        OrderLogInfo log = new OrderLogInfo();
        log.setLogType(LogType.ORDER_REFUND);
        log.setOrderId(orderInfo.getOrderId());
        log.setRemark(sb.toString());
        orderLogManager.add(log);

        return isRefund;
    }

    @Override
    public Boolean queryPaymentDetail(OrderInfoEO orderInfo) {
        QueryDetailResponse queryDetailResponse = payManager.queryDetail(FundsQueryType.Payment.getCode(), orderInfo.getOrderId(), orderInfo.getUid());
        /**
         * 提交状态(CommitStatus)：0：初始，1：已提交，2：取消
         * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
         * 注：判断资金明细是否生效一般判断转账状态为1即可。除(1：支付以外)
         */
        if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() == 1
                && queryDetailResponse.getTradeNo().equals(orderInfo.getOrderId())) {
            return true;
        } else if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() != 1) {
            return false;
        } else {
            return null;
        }
    }

    public Boolean queryPaymentDetail(String orderId, String uid) {
        QueryDetailResponse queryDetailResponse = payManager.queryDetail(FundsQueryType.Payment.getCode(), orderId, uid);
        /**
         * 提交状态(CommitStatus)：0：初始，1：已提交，2：取消
         * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
         * 注：判断资金明细是否生效一般判断转账状态为1即可。除(1：支付以外)
         */
        if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() == 1
                && queryDetailResponse.getTradeNo().equals(orderId)) {
            return true;
        } else if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() != 1) {
            return false;
        } else {
            return null;
        }
    }

    /**
     * 根据业务单号查询主站资金明细
     *
     * @param orderId
     * @return
     */
    public VaQueryDetailResponse queryWithdrawalsDetailByBillId(String orderId) {
        VaQueryDetailResponse queryDetailResponse = payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), "", orderId);
        /**
         * 提交状态(CommitStatus)：0：初始，1：已提交，2：取消
         * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
         * 注：判断资金明细是否生效一般判断转账状态为1即可
         */
        if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() == 1 && queryDetailResponse.getBillId().equals(orderId) && StringUtils.equalsIgnoreCase("0000", queryDetailResponse.getCode())) {
            return queryDetailResponse;
        } else {
            return null;
        }
    }

    /**
     * 根据主站单号单号查询主站资金明细
     *
     * @param orderId
     * @return
     */
    public VaQueryDetailResponse queryWithdrawalsDetailByOrderId(String orderId) {
        VaQueryDetailResponse queryDetailResponse = payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), orderId, "");
        /**
         * 提交状态(CommitStatus)：0：初始，1：已提交，2：取消
         * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
         * 注：判断资金明细是否生效一般判断转账状态为1即可
         */
        if (queryDetailResponse != null && queryDetailResponse.getCommitStatus() == 1 && queryDetailResponse.getOrderId().equals(orderId) && StringUtils.equalsIgnoreCase("0000", queryDetailResponse.getCode())) {
            return queryDetailResponse;
        } else {
            return null;
        }
    }
//    //增加出货流程出货商资金
//    public Boolean add7BaoCHPayorder(String orderid) {
//        List<ConfigResultInfoEO> configList = orderConfigQuery.orderConfigList(orderid);
//        BigDecimal transferFees = new BigDecimal(0);
//        ArrayList<TransferUserInfo> transferList = new ArrayList<TransferUserInfo>();
//
//        for (ConfigResultInfoEO config : configList) {
//
//            // 兼容性代码，稳定后需要删除 start
//            /*if (config.getIncome() == null) {
//                BigDecimal goldCount = new BigDecimal(config.getConfigGoldCount());
//                BigDecimal totalPrice = config.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
//                // 获取游戏佣金比例
//                double commissionBase = CommissionUtil.getCommission(orderInfo.getCreateTime(),
//                        orderInfo.getGameName(), orderInfo.getRegion());
//                // 卖家佣金
//                BigDecimal commission = totalPrice.multiply(new BigDecimal(commissionBase));
//                commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
//                // 卖家收益 = 配单总价-佣金
//                BigDecimal income = totalPrice.subtract(commission);
//                // 订单价格
//                BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
//                // 库存价格
//                BigDecimal repositoryAmount = config.getRepositoryUnitPrice().multiply(goldCount);
//                // 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
//                BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
//
//                config.setTotalPrice(totalPrice);
//                config.setIncome(income);
//                config.setCommission(commission);
//                config.setBalance(balance);
//            }*/
//            // 兼容性代码，稳定后需要删除 end
//
//            //判断是否为已发货的订单 判断是否是走新流程
//            if (config.getState().equals(OrderState.Delivery.getCode())) {
//                String loginAccount = config.getLoginAccount();
//                //查询当前卖家的信息，判断是否已绑定7bao并且开通新资金
//                SellerInfo sellerInfo = sellerManager.findByAccountAndUid(loginAccount, config.getAccountUid());
//                if (sellerInfo.getIsNewFund() != null && sellerInfo.getIsNewFund() && sellerInfo.getisAgree() != null && sellerInfo.getisAgree()) {
//                    //调用7bao增加资金接口，注意增加的资金为卖家的收益款
//                    zbaoFundManager.recharge7Bao(loginAccount, config.getIncome(), FundType.RECHARGE_AUTO_7BAO.getCode());
//                }
//            }
//            return true;
//        }
//        return false;
//    }
}

package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.PurchaserData;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 采购商数据管理
 */
public interface IPurchaserDataManager {
    /**
     * 分页查询采购商
     * @param paramMap
     * @param limit
     * @param startIndex
     * @return
     */
    GenericPage<PurchaserData> queryPage(Map<String, Object> paramMap, int limit, int startIndex);

    /**
     * 修改采购商
     */
    void updatePurchaser(PurchaserData purchaserData);

    PurchaserData selectById(Long id);

    PurchaserData selectByIdForUpdate(Long id);

    int update(PurchaserData purchaserData);

    /**
     * 根据采购商账号进行查找
     * @param account
     * @return
     */
    PurchaserData selectByAccountForUpdate(String account);

    /**
     * 防止锁表 查询无锁
     * @param account
     * @return
     */
    PurchaserData selectByAccount(String account);

    /**
     * 余额低于数值，自动提醒
     * @param remindLine
     * @return
     */
    boolean autoBalanceRemind(BigDecimal remindLine);

    /**
     * 余额低于数值，自动停止收货
     * @param stopLine
     * @return
     */
    boolean autoBalanceStopSh(BigDecimal stopLine,int oldFundOrNewFund);

    /**
     * 查询单条
     */
    PurchaserData queryUnique(String loginAccount);

    /**
     * 采购商充值成功，增加总资金
     * @param account 采购商账号
     * @param payOrderId 充值单号
     * @param amount 充值的金额
     * @return
     *//*
    @Deprecated
    PurchaserData recharge(String account, String payOrderId, BigDecimal amount);

    *//**
     * 冻结采购商资金
     * @param account 采购商账号
     * @param deliveryOrderId 出货单ID
     * @param amount 冻结金额
     * @return
     *//*
    @Deprecated
    PurchaserData freezeFund(String account, String deliveryOrderId, BigDecimal amount);

    *//**
     * 解冻资金
     * @param account 采购商账号
     * @param deliveryOrderId 出货单号
     * @param amount 解冻金额
     * @return
     *//*
    @Deprecated
    PurchaserData releaseFreezeFund(String account, String deliveryOrderId, BigDecimal amount);*/

    /**
     * 获取当前收货商数据
     * @return
     */
    PurchaserData getCurrentPurchaserData();

    void updateDeliveryType(int deliveryType);

    void updateTradeType(String tradeType, String tradeTypeName);

    /**
     * 修改收货商库存限制
     * @param isSplit
     * @param repositoryCount
     * @param needCount
     */
    void updateRepositoryCount(Boolean isSplit, Long repositoryCount, Long needCount);
}

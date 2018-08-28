package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 采购商数据管理
 */
@Component
public class PurchaserDataManagerImpl extends AbstractBusinessObject implements IPurchaserDataManager{
    @Autowired
    private IPurchaserDataDao purchaserDataDao;
    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    IMobileMsgManager mobileMsgManager;
    @Autowired
    IShGameConfigManager shGameConfigManager;

    @Override
    public GenericPage<PurchaserData> queryPage(Map<String, Object> paramMap, int limit, int start) {
        return purchaserDataDao.selectByMap(paramMap,limit,start);
    }

    @Override
    @Transactional
    public void updatePurchaser(PurchaserData purchaserData) {
         purchaserDataDao.update(purchaserData);
    }

    @Override
    @Transactional
    public PurchaserData selectById(Long id) {
        return purchaserDataDao.selectById(id);
    }

    @Override
    @Transactional
    public PurchaserData selectByIdForUpdate(Long id) {
        return purchaserDataDao.selectByIdForUpdate(id);
    }

    @Override
    public int update(PurchaserData purchaserData) {
        return purchaserDataDao.update(purchaserData);
    }

    /**
     * 根据采购商账号进行查找
     *
     * @param account
     * @return
     */
    @Override
    @Transactional
    public PurchaserData selectByAccountForUpdate(String account) {
        return purchaserDataDao.selectByAccountForUpdate(account);
    }

    /**
     * 余额低于数值，自动提醒
     * @param remindLine
     * @return
     */
    public boolean autoBalanceRemind(BigDecimal remindLine){
        //取出低于数值的采购商数据
        Map paramMap = new HashMap();
        paramMap.put("availableAmountMax", remindLine);
        List<PurchaserData> purchaseDataList = purchaserDataDao.selectByMap(paramMap);

        if(purchaseDataList==null || purchaseDataList.isEmpty()){
            return true;
        }

        for(PurchaserData data:purchaseDataList){
            sendMessage(data.getPhoneNumber(), "您的余额为："+data.getAvailableAmount()+",低于提醒线："+remindLine+"，请及时充值，避免下架");
        }
        return true;
    }

    /**
     * 发送短信
     *
     * @param content
     * @return
     */
    private boolean sendMessage(String mobilePhone, String content) {
        try {
            mobileMsgManager.sendMessage(mobilePhone, content);
        } catch (Exception e) {
            logger.error("发送短信出错了", e);
            return false;
        }
        return true;
    }

    /**
     * 余额低于数值，自动停止收货
     * @param stopLine
     * @return
     */
    public boolean autoBalanceStopSh(BigDecimal stopLine,int oldFundOrNewFund){
        //取出低于数值的采购商数据
        Map paramMap = new HashMap();
        paramMap.put("purchaseOrderOnline", true);
        if (oldFundOrNewFund == 1) {
            paramMap.put("availableAmountMax", stopLine);
            paramMap.put("isNewFund", false);
        }
        if (oldFundOrNewFund == 2) {
            paramMap.put("availableAmountMaxNewFund", stopLine);
            paramMap.put("isNewFund", true);
        }
        List<PurchaserData> purchaseDataList = purchaserDataDao.selectByMap(paramMap);
        if(purchaseDataList==null || purchaseDataList.isEmpty()){
            return true;
        }
        List<PurchaseOrder> orderList = new ArrayList<PurchaseOrder>();
        for(PurchaserData data:purchaseDataList){
            PurchaseOrder order = new PurchaseOrder();
            if (oldFundOrNewFund==2){
                order.setIsRobotDown(false);
            }
            order.setIsOnline(false);
            order.setBuyerAccount(data.getLoginAccount());
            orderList.add(order);
        }
        purchaseOrderManager.batchUpdate(orderList);
        return true;
    }

    /**
     * 采购商充值成功，增加总资金
     * @param account 采购商账号
     * @param payOrderId 充值单号
     * @param amount 增加的金额
     * @return
     *//*
    @Override
    @Transactional
    public PurchaserData recharge(String account, String payOrderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmount();
        totalAmount = totalAmount.add(amount).setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setTotalAmount(totalAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(purchaserData.getFreezeAmount());
        availableAmount = availableAmount.setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = String.format("充值成功,订单号:%s,金额:%s,当前总金额:%s,冻结金额:%s,可用金额:", payOrderId,
                amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                purchaserData.getAvailableAmount());

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.RECHARGE.getCode());
        fundDetail.setPayOrderId(payOrderId);
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Date());
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    *//**
     * 冻结采购商资金
     *
     * @param account         采购商账号
     * @param deliveryOrderId 出货单ID
     * @param amount          冻结金额
     * @return
     *//*
    @Override
    @Transactional
    public PurchaserData freezeFund(String account, String deliveryOrderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.add(amount).setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = String.format("冻结金额,出货订单号:%s,金额:%s,当前总金额:%s,冻结金额:%s,可用金额:", deliveryOrderId,
                amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                purchaserData.getAvailableAmount());

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.FREEZE.getCode());
        fundDetail.setPayOrderId(deliveryOrderId);
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Date());
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }

    *//**
     * 解冻资金
     * @param account 采购商账号
     * @param deliveryOrderId 出货单号
     * @param amount 解冻金额
     * @return
     *//*
    public PurchaserData releaseFreezeFund(String account, String deliveryOrderId, BigDecimal amount) {
        // 获取采购商数据
        PurchaserData purchaserData = selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.subtract(amount).setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.HALF_DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        purchaserDataDao.update(purchaserData);

        // 写入资金明细
        String log = String.format("解冻金额,出货订单号:%s,金额:%s,当前总金额:%s,冻结金额:%s,可用金额:", deliveryOrderId,
                amount, purchaserData.getTotalAmount(), purchaserData.getFreezeAmount(),
                purchaserData.getAvailableAmount());

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(account);
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        fundDetail.setPayOrderId(deliveryOrderId);
        fundDetail.setAmount(amount);
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Date());
        fundDetailManager.save(fundDetail);

        return purchaserData;
    }*/

    /**
     * 获取当前收货商数据
     * @return
     */
    @Override
    public PurchaserData getCurrentPurchaserData(){
        return purchaserDataDao.selectByAccountForUpdate(CurrentUserContext.getUserLoginAccount());
    }

    @Override
    public void updateDeliveryType(int deliveryType) {
        PurchaserData purchaserData= purchaserDataDao.selectByAccountForUpdate(CurrentUserContext.getUserLoginAccount());
        if(purchaserData==null){
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),ResponseCodes.NotFindPurchaser.getMessage());
        }
        purchaserData.setDeliveryType(deliveryType);
        purchaserData.setDeliveryTypeName(ShDeliveryTypeEnum.getTypeByCode(deliveryType).getName());
        purchaserDataDao.update(purchaserData);
    }

    @Override
    public void updateTradeType(String tradeType, String tradeTypeName) {
        PurchaserData purchaserData=purchaserDataDao.selectByAccountForUpdate(CurrentUserContext.getUserLoginAccount());
        if(purchaserData==null){
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),ResponseCodes.NotFindPurchaser.getMessage());
        }
        purchaserData.setTradeType(tradeType);
        purchaserData.setTradeTypeName(tradeTypeName);
        purchaserDataDao.update(purchaserData);
    }

    @Override
    @Transactional
    public PurchaserData queryUnique(String loginAccount) {
        return purchaserDataDao.selectUniqueByProp("loginAccount",loginAccount);
    }



    /**
     * 根据采购商账号进行查找
     *
     * @param account
     * @return
     */
    @Override
    public PurchaserData selectByAccount(String account) {
        return purchaserDataDao.selectByAccount(account);
    }

    /**
     * 修改收货商库存限制
     * @param isSplit
     * @param repositoryCount
     * @param needCount
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRepositoryCount(Boolean isSplit, Long repositoryCount, Long needCount) {
        String loginAccount = CurrentUserContext.getUser().getLoginAccount();
        if (StringUtils.isBlank(loginAccount)) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }
        PurchaserData purchaserData = this.queryUnique(loginAccount);
        if (purchaserData == null) {
            throw new SystemException(ResponseCodes.NoPurchaseData.getCode(),ResponseCodes.NoPurchaseData.getMessage());
        }
        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName("地下城与勇士", "游戏币", null, null);
        if (shGameConfig == null) {
            throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(),ResponseCodes.NotAvailableConfig.getMessage());
        }
        if (shGameConfig.getRepositoryCount() == null || shGameConfig.getNeedCount() == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryCount.getCode(),ResponseCodes.EmptyRepositoryCount.getMessage());
        }
        //开启分仓
        if (isSplit != null && isSplit) {
            if (repositoryCount < shGameConfig.getRepositoryCount()) {
                throw new SystemException(ResponseCodes.RepositoryCountToBeLess.getCode(),ResponseCodes.RepositoryCountToBeLess.getMessage());
            }
            if (needCount < shGameConfig.getNeedCount()) {
                throw new SystemException(ResponseCodes.NeedCountToBeLess.getCode(),ResponseCodes.NeedCountToBeLess.getMessage());
            }
            purchaserData.setRepositoryCount(repositoryCount);
            purchaserData.setNeedCount(needCount);
        } else {
            isSplit = false;
        }
        purchaserData.setIsSplit(isSplit);
        this.update(purchaserData);
    }


}

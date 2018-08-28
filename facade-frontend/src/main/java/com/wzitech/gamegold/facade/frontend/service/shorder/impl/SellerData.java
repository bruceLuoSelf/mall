package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.CheckState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShOpenState;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by 汪俊杰 on 2016/12/19.
 */
@Component
public class SellerData implements ISellerData {
    @Autowired
    ISellerManager sellerManager;
    @Autowired
    ISystemConfigManager systemConfigManager;
    @Autowired
    IPurchaserDataManager purchaserDataManager;
    @Autowired
    SevenBaoFund sevenBaoFund;
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 检查卖家信息
     *
     * @return
     */
    @Override
    public boolean checkSeller(AbstractServiceResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();

        // 获取当前用户
        IUser user = CurrentUserContext.getUser();
        if (user == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }
        //获取用户信息
        SellerInfo seller = sellerManager.querySellerInfo(user.getLoginAccount());
        if (seller == null) {
            responseStatus.setStatus(ResponseCodes.NotRegisterMall.getCode(), ResponseCodes.NotRegisterMall.getMessage());
            return false;
        }
        //是否新资金
        Boolean isNewFund = seller.getIsNewFund();
        logger.info("卖家信息seller:{}", seller);
        if (seller.getCheckState() != CheckState.PassAudited.getCode()) {
            if (seller.getCheckState() == CheckState.UnAudited.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerUnAudited.getCode(), ResponseCodes.SellerUnAudited.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SellerUnPassAudited.getCode(), ResponseCodes.SellerUnPassAudited.getMessage());
            }
            return false;
        } else {
            if (seller.getOpenShState() == null || seller.getOpenShState().intValue() != ShOpenState.OPEN.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerNoOpenSh.getCode(), ResponseCodes.SellerNoOpenSh.getMessage());
                return false;
            } else if (isNewFund == null || isNewFund == false) {
                PurchaserData purchaserData = purchaserDataManager.selectByAccount(seller.getLoginAccount());
                SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
                logger.info("PurchaserData:{}", purchaserData);
                logger.info("SystemConfig:{}", systemConfig);
                if (purchaserData == null) {
                    responseStatus.setStatus(ResponseCodes.NoPurchaseData.getCode(), ResponseCodes.NoPurchaseData.getMessage());
                    return false;
                }
                if (systemConfig != null) {
                    if (StringUtils.isNotBlank(systemConfig.getConfigValue()) && new BigDecimal(systemConfig.getConfigValue()).compareTo(purchaserData.getAvailableAmount()) >= 0) {
                        responseStatus.setStatus(ResponseCodes.SellerWarningMoney.getCode(), ResponseCodes.SellerWarningMoney.getMessage());
                        return false;
                    }
                }
            } else {
                SystemConfig systemConfig = sevenBaoFund.createFund();
                if (systemConfig == null) {
                    throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
                }
                String configValue = systemConfig.getConfigValue();
                String availableFundValue = systemConfig.getAvailableFundValue();
                PurchaserData purchaserData = purchaserDataManager.selectByAccount(seller.getLoginAccount());
                logger.info("PurchaserData:{}", purchaserData);
                logger.info("SystemConfig:{}", systemConfig);

                if (purchaserData == null) {
                    responseStatus.setStatus(ResponseCodes.NoPurchaseData.getCode(), ResponseCodes.NoPurchaseData.getMessage());
                    return false;
                }
                //可用资金余额值
                if (systemConfig != null) {
                    if (StringUtils.isNotBlank(systemConfig.getAvailableFundValue()) && new BigDecimal(configValue).add(new BigDecimal(availableFundValue)).compareTo(purchaserData.getAvailableAmountZBao()) >= 0) {
                        responseStatus.setStatus(ResponseCodes.AvailableFundKey.getCode(), ResponseCodes.SellerWarningMoney.getMessage());
                    }
                }
                //保证金
                if (systemConfig != null) {
                    if (StringUtils.isNotBlank(systemConfig.getConfigValue()) && new BigDecimal(systemConfig.getConfigValue()).compareTo(purchaserData.getAvailableAmountZBao()) >= 0) {
                        responseStatus.setStatus(ResponseCodes.MarginConfigKey.getCode(), ResponseCodes.SellerWarningMoney.getMessage());
                    }
                }
            }
        }

        return true;
    }

    /**
     * 检查卖家信息
     *
     * @return
     */
    @Override
    public boolean checkSellerForRecharge(AbstractServiceResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();

        // 获取当前用户
        IUser user = CurrentUserContext.getUser();
        if (user == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }

        SellerInfo seller = sellerManager.querySellerInfo(user.getLoginAccount());
        if (seller == null) {
            responseStatus.setStatus(ResponseCodes.NotRegisterMall.getCode(), ResponseCodes.NotRegisterMall.getMessage());
            return false;
        }
        logger.info("卖家信息seller:{}", seller);
        if (seller.getCheckState() != CheckState.PassAudited.getCode()) {
            if (seller.getCheckState() == CheckState.UnAudited.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerUnAudited.getCode(), ResponseCodes.SellerUnAudited.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SellerUnPassAudited.getCode(), ResponseCodes.SellerUnPassAudited.getMessage());
            }
            return false;
        } else {
            if (seller.getOpenShState() == null || seller.getOpenShState().intValue() != ShOpenState.OPEN.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerNoOpenSh.getCode(), ResponseCodes.SellerNoOpenSh.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * 检查收货商上传发布单时的资金
     */
    public boolean checkSellerAmount(AbstractServiceResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();
        //保证金不足，不让上传发布单
        //检查新采购商保证金,如果保证金额不足，将抛出对应异常
        IUser user = CurrentUserContext.getUser();
        PurchaserData purchaserData = purchaserDataManager.selectByAccount(user.getLoginAccount());
        SellerInfo sellerInfo = sellerManager.querySellerInfo(user.getLoginAccount());
        //如果开通了新资金
        if (sellerInfo != null && sellerInfo.getIsNewFund() != null && sellerInfo.getIsNewFund() && sellerInfo.getisAgree() != null && sellerInfo.getisAgree()) {
            SystemConfig fund = sevenBaoFund.createFund();
            if (fund == null) {
                logger.info("可用收货金配置不能为空:{}", fund);
                throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
            }
//            String availableFundValue = fund.getAvailableFundValue();//获得500数值
            String configValue = fund.getConfigValue();
            BigDecimal availableBigDecimal = new BigDecimal(configValue);

            BigDecimal availableAmountZBao = purchaserData.getAvailableAmountZBao();
            //如果是上传发布单目前要求可用金额500
            if (availableAmountZBao.compareTo(availableBigDecimal) == -1) {
                responseStatus.setStatus(ResponseCodes.AvailableFundKey.getCode(), ResponseCodes.AvailableFundKey.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * 检查收货商上架时的资金
     */
    public boolean checkOnlineAmount(AbstractServiceResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();
        //检查新采购商保证金,如果保证金额不足，将抛出对应异常
        IUser user = CurrentUserContext.getUser();
        PurchaserData purchaserData = purchaserDataManager.selectByAccount(user.getLoginAccount());
        SellerInfo sellerInfo = sellerManager.querySellerInfo(user.getLoginAccount());
        //绑定新资金字段为空的设置为null
        if (null == sellerInfo.getIsNewFund()) {
            sellerInfo.setIsNewFund(false);
        }
        //如果开通了新资金
        if (sellerInfo.getIsNewFund() && sellerInfo.getisAgree() != null && sellerInfo.getisAgree()) {
            SystemConfig fund = sevenBaoFund.createFund();
            if (fund == null) {
                throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
            }
            String availableFundValue = fund.getAvailableFundValue();//获得500数值
            String configValue = fund.getConfigValue();//获得200数值
            BigDecimal availableBigDecimal = new BigDecimal(availableFundValue);
            BigDecimal configBigDecimal = new BigDecimal(configValue);
            BigDecimal add = availableBigDecimal.add(configBigDecimal); //700
            BigDecimal availableAmountZBao = purchaserData.getAvailableAmountZBao();
            //如果是上架目前要求大于700

            if (availableAmountZBao.compareTo(add) == -1) {
                responseStatus.setStatus(ResponseCodes.MarginConfigKey.getCode(), ResponseCodes.MarginConfigKey.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * 检查新资金卖家信息
     *
     * @return
     */
    @Override
    public boolean checkNewFundSellerForRecharge(AbstractServiceResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();

        // 获取当前用户
        IUser user = CurrentUserContext.getUser();
        if (user == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }

        SellerInfo seller = sellerManager.querySellerInfo(user.getLoginAccount());
        if (seller == null) {
            responseStatus.setStatus(ResponseCodes.NotRegisterMall.getCode(), ResponseCodes.NotRegisterMall.getMessage());
            return false;
        }
        if (seller.getIsNewFund() != null && seller.getisAgree() != null && seller.getIsNewFund() && seller.getisAgree()) {
            responseStatus.setStatus(ResponseCodes.NoSupportPayWay.getCode(), ResponseCodes.NoSupportPayWay.getMessage());
            return false;
        }
        logger.info("卖家信息seller:{}", seller);
        if (seller.getCheckState() != CheckState.PassAudited.getCode()) {
            if (seller.getCheckState() == CheckState.UnAudited.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerUnAudited.getCode(), ResponseCodes.SellerUnAudited.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SellerUnPassAudited.getCode(), ResponseCodes.SellerUnPassAudited.getMessage());
            }
            return false;
        } else {
            if (seller.getOpenShState() == null || seller.getOpenShState().intValue() != ShOpenState.OPEN.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerNoOpenSh.getCode(), ResponseCodes.SellerNoOpenSh.getMessage());
                return false;
            }
        }

        return true;
    }
}

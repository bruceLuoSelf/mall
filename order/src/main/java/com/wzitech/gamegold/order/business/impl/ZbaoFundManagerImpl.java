package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.IZbaoFundManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.shorder.business.IFundDetailManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.dao.IPayOrderDao;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.FundDetail;
import com.wzitech.gamegold.shorder.entity.FundType;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Created by wangmin
 * Date:2017/9/7
 */
@Component
public class ZbaoFundManagerImpl extends AbstractBusinessObject implements IZbaoFundManager {

    @Autowired
    protected IPurchaserDataDao purchaserDataDao;

    @Autowired
    protected IFundDetailManager fundDetailManager;

    @Autowired
    IPayOrderManager payOrderManager;

    @Autowired
    IPayOrderDao payOrderDao;

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    IPurchaseOrderManager purchaseOrderManager;

//    /**
//     * 处理充值成功通知 7bao充值接口
//     *
//     * @param orderId 订单号
//     * @param amount  支付金额
//     */
//    @Override
//    @Transactional
//    public void processRechargeSuccess7Bao(String account, BigDecimal amount) {
//        // 充值成功，增加采购商资金
//        this.recharge7Bao(account, amount);
//    }

    /**
     * 采购商充值成功，增加7bao资金
     *
     * @param account 采购商账号
     * @param amount  增加的金额
     * @return
     */
    @Transactional
    public PurchaserData recharge7Bao(String account, BigDecimal amount, Integer type, String orderId) {
        PayOrder payOrder = payOrderDao.selectUniqueByProp("zbaoRelatedOrderId", orderId);
        if (payOrder != null) {
            throw new SystemException(ResponseCodes.PayOrderRelatedZbaoHasAlreadyCreated.getCode(), ResponseCodes.PayOrderRelatedZbaoHasAlreadyCreated.getMessage());
        }
        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(account);
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());
        if (purchaserData.getAvailableAmountZBao() == null) {
            purchaserData.setAvailableAmountZBao(new BigDecimal(0));
        }
        if (purchaserData.getFreezeAmountZBao() == null) {
            purchaserData.setFreezeAmountZBao(new BigDecimal(0));
        }
        if (purchaserData.getTotalAmountZBao() == null) {
            purchaserData.setTotalAmountZBao(new BigDecimal(0));
        }
        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmountZBao();
        totalAmount = totalAmount.add(amount).setScale(2, RoundingMode.DOWN);
        purchaserData.setTotalAmountZBao(totalAmount);

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmountZBao().subtract(purchaserData.getFreezeAmountZBao());
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmountZBao(availableAmount);

        //当可用金额大于200元,自动上架被下架的采购单
        purchaseOrderManager.grounding(account, availableAmount);
        purchaserDataDao.update(purchaserData);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return create7BaoPayorder(account, amount, purchaserData, type, orderId);
        } else {
            return null;
        }

    }

    /**
     * 创建7bao充值单 已经做过资金处理调用
     *
     * @param account
     * @param amount
     * @param purchaserData
     * @param type
     * @return
     */

    public PurchaserData create7BaoPayorder(String account, BigDecimal amount, PurchaserData purchaserData, Integer type, String zbaoRelatedOrderId) {
        String baoOrder = payOrderManager.create7BaoOrder(amount, account, zbaoRelatedOrderId);
        if (type.equals(FundType.RECHARGE_AUTO_7BAO.getCode())) {
            // 写入资金明细
            String log = String.format("【7Bao自动充值单】充值订单号:%s,充值金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                    baoOrder, amount, purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());

            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(account);
            fundDetail.setType(FundType.RECHARGE_AUTO_7BAO.getCode());
            fundDetail.setPayOrderId(baoOrder);
            fundDetail.setAmount(amount);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);
            return purchaserData;
        } else if (type.equals(FundType.RECHARGE7Bao.getCode())) {
            // 写入资金明细
            String log = String.format("【7Bao手动充值单】充值订单号:%s,充值金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                    baoOrder, amount, purchaserData.getTotalAmountZBao(), purchaserData.getFreezeAmountZBao(),
                    purchaserData.getAvailableAmountZBao());

            FundDetail fundDetail = new FundDetail();
            fundDetail.setBuyerAccount(account);
            fundDetail.setType(FundType.RECHARGE7Bao.getCode());
            fundDetail.setPayOrderId(baoOrder);
            fundDetail.setAmount(amount);
            fundDetail.setLog(log);
            fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fundDetailManager.save(fundDetail);
            return purchaserData;
        }
        throw new SystemException(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
    }
}

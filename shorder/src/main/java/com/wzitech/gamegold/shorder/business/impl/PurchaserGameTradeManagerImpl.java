package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.ParamUtils;
import com.wzitech.gamegold.shorder.business.IPurchaserGameTradeManager;
import com.wzitech.gamegold.shorder.dao.IPurchaserGameTradeDao;
import com.wzitech.gamegold.shorder.dao.IPurchaserGameTradeRedis;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ljn
 * @date 2018/4/10
 */
@Service
public class PurchaserGameTradeManagerImpl implements IPurchaserGameTradeManager {

    @Autowired
    private IPurchaserGameTradeDao purchaserGameTradeDao;
    @Autowired
    private IPurchaserGameTradeRedis purchaserGameTradeRedis;

    /**
     * 增加
     * @param purchaserGameTrade
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchaserGameTrade(PurchaserGameTrade purchaserGameTrade) {
        Map<String, Object> queryMap = ParamUtils.convertMap(purchaserGameTrade);
        int count = purchaserGameTradeDao.countByMap(queryMap);
        if (count > 0) {
            throw new SystemException(ResponseCodes.ExistPurchaserGameTrade.getCode(),
                    ResponseCodes.ExistPurchaserGameTrade.getMessage());
        }
        purchaserGameTradeDao.insert(purchaserGameTrade);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePurchaserGameTrade(Long id) {
        purchaserGameTradeDao.deleteById(id);
    }

    /**
     * 批量增加
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<PurchaserGameTrade> list) {
        purchaserGameTradeDao.batchInsert(list);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        purchaserGameTradeDao.batchDeleteByIds(ids);
    }

    /**
     * 根据采购商游戏配置id删除
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPurchaserId(Long id) {
        purchaserGameTradeDao.deleteByPurchaserId(id);
    }

    /**
     * 根据采购商游戏配置id查询交易方式配置
     * @param id
     * @return
     */
    @Override
    public List<PurchaserGameTrade> selectByPurchaserId(Long id) {
        List<PurchaserGameTrade> purchaserGameTrades = purchaserGameTradeRedis.getPurchaserGameTrade(id);
        if (CollectionUtils.isEmpty(purchaserGameTrades)) {
            purchaserGameTrades = purchaserGameTradeDao.selectByPurchaserId(id);
            if (!CollectionUtils.isEmpty(purchaserGameTrades)) {
                purchaserGameTradeRedis.add(id,purchaserGameTrades);
            }
        }
        return purchaserGameTrades;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByGameTrade(Map<String,Object> map) {
        purchaserGameTradeDao.deleteByGameTrade(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setParameter(PurchaseOrder purchase) {
        List<PurchaserGameTrade> purchaserGameTrades = this.selectByPurchaserId(purchase.getPurchaserId());
        if (CollectionUtils.isEmpty(purchaserGameTrades)) {
            purchase.setTradeTypeName("");
            purchase.setTradeTypeId("");
            purchase.setTradeLogo("");
            return true;
        }
        String tradeTypeId = "";
        String tradeTypeName = "";
        String tradeLogo = "";
        for (PurchaserGameTrade gameTrade : purchaserGameTrades) {
            tradeTypeId += gameTrade.getTradeTypeId() + ",";
            tradeTypeName += gameTrade.getTradeTypeName() + ",";
            tradeLogo += gameTrade.getTradeLogo() == null ? "" : gameTrade.getTradeLogo() + ",";
        }
        if (StringUtils.isNotBlank(tradeTypeId)) {
            tradeTypeId = tradeTypeId.substring(0,tradeTypeId.length() - 1);
        }
        if (StringUtils.isNotBlank(tradeTypeName)) {
            tradeTypeName = tradeTypeName.substring(0,tradeTypeName.length() - 1);
        }
        if (StringUtils.isNotBlank(tradeLogo)) {
            tradeLogo = tradeLogo.substring(0,tradeLogo.length() - 1);
        }
        purchase.setTradeTypeName(tradeTypeName);
        purchase.setTradeTypeId(tradeTypeId);
        purchase.setTradeLogo(tradeLogo);
        return false;
    }
}

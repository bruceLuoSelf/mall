package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.common.enums.GoodsCat;
import com.wzitech.gamegold.order.business.ICommissionManager;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import com.wzitech.gamegold.repository.business.ISellerSettingManager;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 佣金获取
 * @author yemq
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/15    wrf              ZW_C_JB_00008商城增加通货
 */
@Component
public class CommissionManagerImpl implements ICommissionManager {
    @Autowired
    ISellerSettingManager sellerSettingManager;

    @Autowired
    IGameConfigManager gameConfigManager;

    /**
     * 获取佣金
     *
     * @param gameName
     * @param sellerLoginAccount
     * @param goodsCat
     * @return
     */
    @Override
    public BigDecimal getCommission(String gameName, String sellerLoginAccount, Integer goodsCat,String goodsTypeName) {
        // 获取游戏佣金比例
        BigDecimal commissionBase = new BigDecimal("0.06");
        // 栏目3商品，佣金根据卖家店铺配置收取
        if (goodsCat == GoodsCat.Cat3.getCode()) {
            // 查询店铺卖家的佣金
            SellerSetting sellerSetting = sellerSettingManager.querySellSettingByGameNameAndSeller(gameName, sellerLoginAccount,goodsTypeName);  /**ZW_C_JB_00008_20170515 add**/
            if (sellerSetting != null) {
                commissionBase = sellerSetting.getCommision();
            } else {
                GameConfigEO gameConfig = gameConfigManager.selectGameConfig(gameName,goodsTypeName);
                if (gameConfig != null)
                    commissionBase = gameConfig.getCommision();
            }
        } else {
            GameConfigEO gameConfig = gameConfigManager.selectGameConfig(gameName,goodsTypeName);
            if (gameConfig != null)
                commissionBase = gameConfig.getCommision();
        }

        return commissionBase;
    }
}

package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * Created by 340032 on 2018/3/19.
 */
public class CurrencyConfigRequest extends AbstractServiceRequest {
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 商品类型
     * @return
     */
    private String goodsType;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


}

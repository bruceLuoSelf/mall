package com.wzitech.gamegold.common.enums;

/**
 * Created by ljn on 2018/4/2.
 */
public enum TradeLogoEnum {

    FaceTrade(1, "当面交易"),

    EnchantTrade(2, "附魔交易"),

    PostTrade(3, "邮寄交易"),

    BlackMarketTrade(4, "黑市交易"),

    AuctionTrade(5, "拍卖交易");

    private int code;

    private String type;

    TradeLogoEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static TradeLogoEnum getTypeByCode(int code) {
        for (TradeLogoEnum trade : TradeLogoEnum.values()) {
            if (trade.getCode() == code) {
                return trade;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的TradeLogo:"+code);
    }
}

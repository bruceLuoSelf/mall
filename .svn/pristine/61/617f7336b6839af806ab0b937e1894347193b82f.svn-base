package com.wzitech.gamegold.common.enums;

/**
 * Created by chengXY on 2017/10/24.
 */
public enum OrderSource {

    GAME_XS_GOLD(1,"YXB.XSGameMoney"),
    GAME_XS_GOODS(2,"YXB.XSGameGoods"),
    GAME_SH_GOLD(3,"YXB.SHGameMoney");

    private int code;

    private String name;

    OrderSource(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
            * 根据code值获取对应的枚举
    *
            * @param code
    * @return
            */
    public static OrderSource getOrderSource(int code) {
        for (OrderSource type : OrderSource.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}

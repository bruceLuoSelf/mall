package com.wzitech.gamegold.common.enums;

/**订单标识
 * Created by ${SunYang} on 2017/3/20.
 */
public enum OrderType {
    goldOrder(1, "金币商城内部"),//1：金币商城内部
    mOrder(2, "M站"),
    AppOrder(3,"app订单"),//2：M站
    GAME_GOLD_SELLER_ORDER(6,"金币商城销售单"),
    GAME_GOLD_DELIVERY_ORDER(10,"金币商城收货单");
    //GAME_GOLD_TRADE_ORDER(6,"金币商城通货单");
    private int code;

    private String name;

    OrderType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static OrderType getOrderType(int code) {
        for (OrderType type : OrderType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}

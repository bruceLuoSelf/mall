package com.wzitech.gamegold.common.enums;

/**
 * 订单来源类型
 * @author yemq
 */
public enum RefererType {
    CUSTOMERS_SERVICE_CENTER(1, "便民中心"),
    InternetBarAlliance(2, "网吧联盟"),
    goldOrder(3, "金币商城内部"),
    mOrder(4, "M站"),
    AppOrder(5,"app订单");

    private String name;
    private int code;

    RefererType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RefererType getTypeByCode(int code) {
        for (RefererType referer : RefererType.values()) {
            if (referer.getCode() == code) {
                return referer;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的RefererType:" + code);
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

}

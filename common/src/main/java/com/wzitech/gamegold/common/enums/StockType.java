package com.wzitech.gamegold.common.enums;

/**
 * Created by 340082 on 2018/6/20.
 */
public enum StockType {
    Seller(1,"销售"),
    Delivery(2,"收货"),
    SplitRepository(3,"分仓");

    private int code;

    private String name;

    StockType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static DeliveryOrderStatus getTypeByCode(int code) {
        for (DeliveryOrderStatus status : DeliveryOrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的OrderState:" + code);
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        if (code != null) {
            for (DeliveryOrderStatus status : DeliveryOrderStatus.values()) {
                if (status.getCode() == code) {
                    return status.getName();
                }
            }
        }

        return "未能找到匹配的OrderState:" + code;
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
}

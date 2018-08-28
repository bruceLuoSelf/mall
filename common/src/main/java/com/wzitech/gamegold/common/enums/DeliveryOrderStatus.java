package com.wzitech.gamegold.common.enums;

/**
 * 出货单状态
 */
public enum DeliveryOrderStatus {
    /**
     * 等待交易
     * 1,2,3,7,8,9,10,11
     */
    WAIT_TRADE(1, "等待交易"),
    /**
     * 排队中
     */
    INQUEUE(2, "待分配角色"),
    /**
     * 状态：交易中
     */
    TRADING(3, "交易中"),
    /**
     * 状态：交易完成
     */
    COMPLETE(4, "交易完成"),
    /**
     * 状态：部分完单
     */
    COMPLETE_PART(5, "部分完单"),
    /**
     * 状态：撤单
     */
    CANCEL(6, "撤单"),
    /**
     * 状态：需人工介入
     */
    MANUAL_INTERVENTION(7, "需人工介入"),
    /**
     * ;
     * 申请部分完单
     */
    APPLY_COMPLETE_PART(8, "申请部分完单"),
    /**
     * ;
     * 手动模式，待发货
     */
    WAIT_DELIVERY(9, "待发货"),
    /**
     * ;
     * 手动模式，已发货
     */
    DELIVERY_FINISH(10, "已发货"),
    /**
     * 状态: 待确认收货
     */
    WAIT_RECEIVE(11, "待确认收货"),;


    private int code;

    private String name;

    DeliveryOrderStatus(int code, String name) {
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

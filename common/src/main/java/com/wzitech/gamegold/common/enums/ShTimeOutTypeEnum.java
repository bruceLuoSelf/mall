package com.wzitech.gamegold.common.enums;

/**
 * 交易超时类型
 * Created by 汪俊杰 on 2017/1/6.
 */
public enum ShTimeOutTypeEnum {
    /**
     * 收货商长时间未分配角色
     */
    NO_DISTRIB_ROLE(1, "收货商长时间未分配角色"),

    /**
     * 部分完单
     */
    NO_CLICK_DELIVERY(2, "出货商长时间未点击我已发货"),

    /**
     * 长时间未点收货
     */
    NO_CLICK_RECEIVE(3, "收货商长时间未点击确认收货"),
    ;

    /**
     * 类型码
     */
    private int code;

    ShTimeOutTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static ShTimeOutTypeEnum getTypeByCode(int code) {
        for (ShTimeOutTypeEnum type : ShTimeOutTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的ShTimeOutTypeEnum:" + code);
    }
}

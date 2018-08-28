package com.wzitech.gamegold.common.enums;

/**
 * 收货GTR回传订单类型
 * Created by 汪俊杰 on 2017/1/4.
 */
public enum ShServiceType {
    /**
     * 交易成功
     */
    COMPLETE("1", "交易成功"),

    /**
     * 部分完单
     */
    COMPLETE_PART("2", "部分完单"),

    /**
     * 撤单
     */
    CANCEL("3", "撤单"),

    /**
     * 人工介入
     */
    MANUAL_INTERVENTION("4", "人工介入"),

    /**
     * 更新前台库存
     */
    UPDATE_STORE("5", "更新前台库存");

    /**
     * 类型码
     */
    private String code;

    ShServiceType(String code, String name) {
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
    public String getCode() {
        return code;
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static ShServiceType getTypeByCode(String code) {
        for (ShServiceType type : ShServiceType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的ShServiceType:" + code);
    }
}

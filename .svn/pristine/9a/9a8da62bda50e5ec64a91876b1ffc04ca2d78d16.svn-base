package com.wzitech.gamegold.common.enums;

/**
 * 日志类型
 * @author yemq
 */
public enum LogType implements GenericEnum<LogType> {

    /**
     * 订单
     */
    ORDER_CREATE("101", "下单"),
    ORDER_PAID("102", "付款"),
    ORDER_DISTRIBUTION("103", "配单"),
    ORDER_DELIVERY("104", "发货"),
    ORDER_STATEMENT("105", "结单"),
    ORDER_REFUND("106", "退款"),
    ORDER_CANCEL("107", "取消订单"),
    ORDER_RECEIVER("108", "已收货"),
    ORDER_OTHER("109", "其它"),

    /**
     * 库存
     */
    REPOSITORY_ADD("201", "添加库存"),
    REPOSITORY_MODIFY("202", "修改库存"),
    REPOSITORY_REMOVE("203", "删除库存"),

    /**
     * 商品
     */
    GOODS_ADD("301", "添加商品"),
    GOODS_MODIFY("302", "修改商品"),
    GOODS_ENABLE("303", "启用商品"),
    GOODS_DISABLE("304", "禁用商品"),
    GOODS_REMOVE("305", "删除商品"),
    GOODS_OTHER("306", "其它");

    private String code;
    private String name;

    LogType(){}

    LogType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 获取枚举代码
     *
     * @return
     */
    @Override
    public String getEnumCode() {
        return code;
    }

    /**
     * 根据代码获取枚举类型
     *
     * @param code
     * @return
     */
    public GenericEnum getByCode(String code) {
        for(LogType type : LogType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到指定的日志类型:" + code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

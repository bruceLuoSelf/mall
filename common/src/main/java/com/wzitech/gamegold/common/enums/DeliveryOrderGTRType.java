package com.wzitech.gamegold.common.enums;

/**
 * 出货GTR返回类型枚举类
 */
public enum DeliveryOrderGTRType {
    /**
     * 交易完成
     */
    TRADE_COMPLETE(100, "交易完成"),
    /**
     * 更新收货数量
     */
    CHANGE_RECEIVE_COUNT(101, "更新收货数量"),
    /**
     * 需人工介入
     */
    MANUAL_INTERVENTION(200, "需人工介入"),
    /**
     * 其他情况
     */
    OTHER_REASON(300, "其他情况"),
    /**
     * 交易超时
     */
    TRADE_TIMEOUT(301, "交易超时"),
    /**
     * 背包已满
     */
    PACK_IS_FULL(302, "背面已满"),
    /**
     * 不是附魔师
     */
    NOT_FMS(303, "不是附魔师"),

    /**
     * 帐号密码错
     */
    ERROR_PWD(304, "帐号密码错"),

    /**
     * 动态令牌
     */
    DYNAMIC_TOKEN(305, "动态令牌"),

    /**
     * 不在交易地点
     */
    NOT_IN_ADDRESS(306, "不在交易地点"),

    /**
     * 出战状态
     */
    BATTLE_STATE(307, "出战状态"),

    /**
     * 异常超时撤单
     */
    EXCEPTION_TIMEOUT_CANCEL(350, "交易超时撤单"),

    /**
     * 异常超时人工介入
     */
    EXCEPTION_TIMEOUT_MANUAL(351, "交易超时人工介入"),

    /**
     * 上一笔订单是人工交易状态，自动转人工
     */
    AUTOMETA_CONTINUE(352, "自动化异常部分完单"),
    ;

    private int code;
    private String message;

    DeliveryOrderGTRType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static DeliveryOrderGTRType getTypeByCode(int code){
        for(DeliveryOrderGTRType type : DeliveryOrderGTRType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        return OTHER_REASON;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

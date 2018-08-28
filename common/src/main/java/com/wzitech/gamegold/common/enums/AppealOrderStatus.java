package com.wzitech.gamegold.common.enums;

/**
 * Created by 339928 on 2018/1/9.
 */
public enum AppealOrderStatus {

    COMPLETE(1,"申诉单已处理完毕"),

    CANCEL(2,"申诉单已撤单"),

    TRADING(3,"售后处理中"),

    OVERTIME(4,"超时禁止申诉"),

    ALLOWAPPEAL(5,"允许申诉"),
    ;


    private int code;

    private String name;

    AppealOrderStatus(int code,String name){
        this.code=code;
        this.name=name;
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

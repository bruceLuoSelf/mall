package com.wzitech.gamegold.shorder.enums;

/**
 * Created by ljn on 2018/3/13.
 */
public enum ShType {

    MACHINE(1,"机器收货"),

    HANDWORK(2,"手工收货");

    private Integer code;

    private String type;

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    ShType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ShType getTypeByCode(int code) {
        for (ShType shType : ShType.values()) {
            if (shType.getCode() == code) {
                return shType;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的ShType:" + code);
    }
}

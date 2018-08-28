package com.wzitech.gamegold.shorder.enums;

/**
 * @author ljn
 * @date 2018/6/14.
 */
public enum IncomeType {

    INCOME(1,"收入"),

    SPEND(2,"支出");

    private Integer code;

    private String type;

    IncomeType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static IncomeType getTypeByCode(int code) {
        for (IncomeType type : IncomeType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的IncomeType:"+ code);
    }
}

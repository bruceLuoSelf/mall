package com.wzitech.gamegold.shorder.enums;

/**
 * @author ljn
 * @date 2018/6/14.
 */
public enum SplitRepositoryLogType {


    SPLIT_REPOSITORY_GET(1,"分仓所得"),

    SH_GET(2,"收货所得"),

    SPLIT_REPOSITORY_SPEND(3,"分仓支出"),

    SALE_SPEND(4,"销售支出");

    private Integer code;

    private String type;

    SplitRepositoryLogType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static SplitRepositoryLogType getTypeByCode(int code) {
        for (SplitRepositoryLogType type : SplitRepositoryLogType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的分仓日志类型:" + code);
    }
}

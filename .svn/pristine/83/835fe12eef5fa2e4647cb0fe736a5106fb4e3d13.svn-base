package com.wzitech.gamegold.shorder.enums;

/**
 * @author ljn
 * @date 2018/6/15.
 */
public enum SplitRepositoryStatus {

    SPLITTING(1,"分仓中"),

    SUCCESS(2,"成功"),

    FAIL(3,"失败"),

    PART_SPLIT(4,"部分分仓");

    private int code;

    private String status;

    SplitRepositoryStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static SplitRepositoryStatus getSplitRepositoryStatus(int code) {
        for (SplitRepositoryStatus status : SplitRepositoryStatus.values()) {
            if(status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的订单状态:"+code);
    }
}

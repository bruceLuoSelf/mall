package com.wzitech.gamegold.common.enums;

/**
 *优惠券类型
 */
public enum CouponType {
    Hb(1,"红包"),

    Dp(2,"店铺券");

    /**
     * 类型码
     */
    private int code;

    private String name;

    CouponType(int code, String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static CouponType getTypeByCode(int code){
        for(CouponType type : CouponType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的CouponType:" + code);
    }

    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }
}

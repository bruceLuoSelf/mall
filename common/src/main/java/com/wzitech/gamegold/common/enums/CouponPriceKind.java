package com.wzitech.gamegold.common.enums;

/**
 * 优惠券面值种类
 */
public enum CouponPriceKind {
    //2元优惠券，100为2元优惠券门槛
    Level1(100,2),

    //5元优惠券，250为5元优惠券门槛
    Level2(250,5),

    //10元优惠券，500为10元优惠券门槛
    Level3(500,10);

    /**
     * 类型码
     */
    private int code;

    private int price;

    CouponPriceKind(int code, int price){
        this.code = code;
        this.price = price;
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

        throw new IllegalArgumentException("未能找到匹配的CouponPriceKind:" + code);
    }

    public int getPrice() {
        return price;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }
}

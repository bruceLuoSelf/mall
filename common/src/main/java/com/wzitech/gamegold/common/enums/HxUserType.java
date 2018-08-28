package com.wzitech.gamegold.common.enums;

/**
 * Created by 汪俊杰 on 2017/9/25.
 */
public enum HxUserType {
    /**
     * 等待付款
     */
    Seller(0, "卖家"),

    Buyer(1,"买家");

    /**
     * 类型码
     */
    private int code;

    private String name;

    HxUserType(int code, String name){
        this.code = code;
        this.name = name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static OrderState getTypeByCode(int code){
        for(OrderState type : OrderState.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的HxUserType:" + code);
    }
}
